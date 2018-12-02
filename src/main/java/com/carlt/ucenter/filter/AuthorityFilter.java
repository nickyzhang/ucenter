package com.carlt.ucenter.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.carlt.ucenter.model.AuditInfo;
import com.carlt.ucenter.model.AuthInfo;
import com.carlt.ucenter.model.Resource;
import com.carlt.ucenter.model.RoleInfo;
import com.carlt.ucenter.msg.sender.AuditMsgSender;
import com.carlt.ucenter.service.AuthorityService;
import com.carlt.ucenter.service.TokenService;
import com.carlt.ucenter.util.Constant;
import com.carlt.ucenter.util.Utils;
import com.github.pagehelper.util.StringUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 权限、审计过滤器
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月25日 下午4:53:53
 */
@Component
public class AuthorityFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(AuthorityFilter.class);

	@Value("${redis.pubsub.audit_topic.send-enabled:false}")
	private boolean auditEnabled;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private AuditMsgSender<AuditInfo> auditMsgSender;

	@Override
	public Object run() throws ZuulException {

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.getRequest();
		HttpSession session = req.getSession();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);
		String uri = req.getRequestURI();
		AuthInfo authInfo = null;

		log.info("session:{}, token:{} ,send {} request to {}", session.getId(), token, req.getMethod(),
				req.getRequestURL().toString());

		// 登录、登出不做验证
		if (uri.contains("login") || uri.contains("logout") 
				|| uri.contains("/api/user/getByToken")
				|| uri.contains("/api/user/updatePWDByToken")
				|| uri.contains("/dict/getDict")
				) {
			sendAuditMsg(req, "1", "");
			return null;
		}

		// 如果请求未携带token直接拒绝请求
		if (StringUtils.isEmpty(token)) {
			log.warn("session:{},user token is empty", session.getId());
			sendAuditMsg(req, "0", "用户token为空");
			return generateResponseBody(ctx, "1003", "用户token为空");
		}
		// 验证token有效性
		if (token.trim().split("#").length != 3 || !tokenService.validateToken(token)) {
			log.warn("session:{},user token token is invalidated, token:{}", session.getId(), token);
			sendAuditMsg(req, "0", "用户token令牌无效");
			return generateResponseBody(ctx, "1004", "用户token令牌无效");
		}

		// 从redis中获取缓存信息
		Object authObj = tokenService.getCacheFromToken(token, "authinfo");
		if (authObj != null && authObj instanceof AuthInfo)
			authInfo = (AuthInfo) authObj;
		// 没有获取到则直接从DB中获取
		if (authInfo == null) {
			List<AuthInfo> authInfos = authorityService.selectByUserId(Integer.valueOf(token.split("#")[1]));
			if (!authInfos.isEmpty())
				authInfo = authInfos.get(0);
		}
		// 如果权限还是为空则返回错误
		if (authInfo == null) {
			log.warn("token:{},用户授信信息为空", token);
			sendAuditMsg(req, "0", "用户授权信息为空");
			return generateResponseBody(ctx, "1001", "用户授权信息为空");
		}
		
		// 判断是否具有超级管理员角色
		if(Utils.hasSuperRole(authInfo)) {
			log.info("session:{}, admin role access uri:{}", session.getId(), uri);
			sendAuditMsg(req, "1", "超管访问");
			return null;
		}
		
		// 判断是否有访问权限
		for (RoleInfo role : authInfo.getRoles()) {
			for (Resource res : role.getResources()) {
				if (StringUtil.isEmpty(res.getResUri()))
					continue;
				// 判断其他权限
				if (uri.equalsIgnoreCase(res.getResUri()) || uri.startsWith(res.getResUri())) {
					log.info("session:{}, can access uri:{}", session.getId(), uri);
					sendAuditMsg(req, "1", "普权访问");
					return null;
				}
			}
		}

		log.warn("token:{},权限验证失败,uri:{}", token, uri);
		sendAuditMsg(req, "0", "权限验证失败");
		return generateResponseBody(ctx, "1002", "权限验证失败，你没有访问" + uri + "的权限");
	}

	/**
	 * 发送访问审计信息
	 * @param req
	 * @param accessRet
	 * @param mark
	 */
	private void sendAuditMsg(HttpServletRequest req, String accessRet, String mark) {

		if (!auditEnabled)
			return;
		// 如果启用审计则发送审计消息
		AuditInfo auditInfo = new AuditInfo(0, req.getHeader(Constant.REQ_TOKEN_HEADER_KEY), req.getRequestURI(),
				req.getQueryString(), Utils.getIPAddress(req), req.getLocalAddr(), req.getMethod(), new Date(),
				accessRet, mark);
		try {
			auditMsgSender.sendMessage(auditInfo);
		} catch (Exception e) {
			log.error("send audit msg fail:" + e.getMessage(), e);
		}
	}

	/**
	 * 生成响应信息
	 * @param ctx
	 * @param code
	 * @param msg
	 * @return
	 */
	private Object generateResponseBody(RequestContext ctx, String code, String msg) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("code", code);
		ret.put("msg", msg);
		// 过滤该请求，不往下级服务去转发请求，到此结束
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(200);
		ctx.setResponseBody(JSONObject.wrap(ret).toString());
		setResponseAttrs(ctx.getResponse());
		return null;
	}

	/**
	 * 设置响应属性
	 * 
	 * @param response
	 */
	private void setResponseAttrs(HttpServletResponse response) {
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setLocale(new java.util.Locale("zh", "CN"));
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		if (request.getMethod().equals("OPTIONS")) {
			return false;
		}
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}
