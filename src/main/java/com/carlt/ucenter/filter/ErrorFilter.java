package com.carlt.ucenter.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 系统异常过滤器
 * @author ansen.zhu@carlt.com.cn

 
 * @version 1.0
 * @date 2018年8月25日 下午4:58:52
 */
@Component
public class ErrorFilter extends ZuulFilter {

	Logger log = LoggerFactory.getLogger(ErrorFilter.class);

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return -1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {

		RequestContext ctx = RequestContext.getCurrentContext();
		Throwable throwable = ctx.getThrowable();

		log.error("ErrorFilter : {}", throwable.getCause());
		// ctx.set("error.status_code",
		// HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		// 通过下面两行配置禁用系统默认的错误处理过滤器
		ctx.set("throwable", null);
		ctx.set("sendErrorFilter.ran", true);
		Object statusCode = "";
		String msg = throwable.getCause().getMessage();
		
		if(throwable.getCause() instanceof ZuulRuntimeException) {
			ZuulException e = (ZuulException) ((ZuulRuntimeException) throwable.getCause()).getCause();
			statusCode = e.nStatusCode + "";
		} else if(throwable instanceof ZuulException) {
			statusCode = ((ZuulException) throwable).nStatusCode + "";
		} else if(ctx.get("error.status_code") != null) {
			statusCode = ctx.get("error.status_code");
		}
		
		return generateResponseBody(ctx, "9995", statusCode, msg);
	}

	/**
	 * 生成响应信息
	 * @param ctx
	 * @param code
	 * @param statusCode
	 * @param msg
	 * @return
	 */
	private Object generateResponseBody(RequestContext ctx, String code, Object statusCode, String msg) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("code", code);
		ret.put("statusCode", statusCode);
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

}