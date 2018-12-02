package com.carlt.ucenter.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carlt.ucenter.model.AuthInfo;
import com.carlt.ucenter.model.User;
import com.carlt.ucenter.service.AuthorityService;
import com.carlt.ucenter.service.ResourceService;
import com.carlt.ucenter.service.TokenService;
import com.carlt.ucenter.service.UserService;
import com.carlt.ucenter.util.Constant;
import com.carlt.ucenter.util.MD5;
import com.carlt.ucenter.util.Utils;

/**
 * 登录、登出API
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date	2018年8月15日 上午11:51:39
 *
 */
@RestController
@RequestMapping("/api")
public class LoginController {

	Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private TokenService tokenService;

	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> login(HttpServletRequest req) {

		String userCode = req.getParameter("userCode");
		String password = req.getParameter("password");

		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		User user = userService.selectByUserCode(userCode);
		HttpSession session = req.getSession();

		log.info("session:{}", session.getId());

		if(user == null || !user.getPassword().equals(MD5.encode(password))) {
			ret.put("code", "1001");
			ret.put("msg", "用户不存在或者密码错误");
			return ResponseEntity.ok(ret);
		}
		// 用户状态不正常，锁定状态
		if(user.getActStatus() != 1) {
			ret.put("code", "1003");
			ret.put("msg", "用户状态不正常（锁定）");
			return ResponseEntity.ok(ret);
		}
		
		String token = null;
		// 用户已经登录，则强制清除缓存，然后再重新生成token和缓存数据
		if(tokenService.exists(user.getId())) {
			token = tokenService.getToken(user.getId());
			tokenService.removeToken(token);
		} else {
			User updateUser = new User();
			updateUser.setId(user.getId());
			updateUser.setActStatus(user.getActStatus());
			updateUser.setLoginStatus(1); // 设置为登录状态
			// 更新用户状态信息
			int num = userService.updateByPrimaryKeySelective(updateUser, null);
			if(num < 1) {
				ret.put("code", "1006");
				ret.put("msg", "登录失败，用户登录状态未更改");
				return ResponseEntity.ok(ret);
			}
		}

		token = tokenService.generateToken(user.getId());
		
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		data.put("token", token);
		// 屏蔽密码信息
		user.setPassword("******");
		data.put("user", user);
		List<AuthInfo> authInfos = authorityService.selectByUserId(user.getId());
		if (!authInfos.isEmpty()) {
			AuthInfo authInfo = authInfos.get(0);
			// 生成菜单栏
			// 只获取根节点和菜单类型资源
			Set<Integer> resTypes = new HashSet<>(Arrays.asList(0,1));
			// 判断是否具有超管角色
			if (Utils.hasSuperRole(authInfo)) {
				data.put("menu", Utils.generateMenu(resourceService.selectAll(), resTypes));
				// 缓存设置用户具有超级管理员角色
				tokenService.putCacheByToken(token, "superrole", "1");
			} else {
				data.put("menu", Utils.generateMenu(authInfo, resTypes));
			}
			
			// 放置所有权限
			data.put("authInfo", authInfo);
			// 缓存登录用户权限信息
			tokenService.putCacheByToken(token, "authinfo", authInfo);
		}

		ret.put("data", data);
		return ResponseEntity.ok(ret);
	}

	@RequestMapping(value = "/logout", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> logout(HttpServletRequest req, String userCode, String password) {

		Map<String, Object> ret = new HashMap<>();
		HttpSession session = req.getSession();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);
		log.info("session:{}, token:{}", session.getId(), token);

		// 如果token存在则移除
		if (tokenService.validateToken(token)) {
			int userId = Integer.valueOf(token.trim().split("#")[1]);
			int num = userService.logout(userId);
			if(num > 0) {
				ret.put("code", Constant.REQ_SUCCESS_CODE);
				tokenService.removeToken(token);
			} else {
				ret.put("code", "1005");
				ret.put("msg", "登出失败，用户状态未更改");
			}
		} else {
			ret.put("code", "1002");
			ret.put("msg", "请求token不存在，token：" + token);
		}

		return ResponseEntity.ok(ret);
	}

}
