package com.carlt.ucenter.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carlt.ucenter.model.AuthInfo;
import com.carlt.ucenter.service.AuthorityService;
import com.carlt.ucenter.service.ResourceService;
import com.carlt.ucenter.service.TokenService;
import com.carlt.ucenter.util.Constant;
import com.carlt.ucenter.util.Utils;

/**
 * 权限、菜单API
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月15日 上午11:51:24
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthorityController {

	Logger log = LoggerFactory.getLogger(AuthorityController.class);

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private ResourceService resourceService;

	/**
	 * 根据用户ID获取用户权限信息
	 * 
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> selectByUserId(HttpSession session, int userId) {

		Map<String, Object> ret = new HashMap<>();
		AuthInfo authInfo = null;
		List<AuthInfo> authInfos = authorityService.selectByUserId(userId);
		if (!authInfos.isEmpty()) {
			authInfo = authInfos.get(0);
		}

		ret.put("data", authInfo);
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		return ResponseEntity.ok(ret);
	}

	/**
	 * 根据Session中的用户ID获取用户权限信息
	 * 
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/getByToken", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getByToken(HttpServletRequest req) {

		Map<String, Object> ret = new HashMap<>();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);
		Integer id = 0;

		if (!StringUtils.isEmpty(token)) {
			id = Integer.valueOf(token.split(":")[1]);
		}

		Object authInfo = null;
		if (id > 0) {
			// 从redis中获取缓存信息
			authInfo = tokenService.getCacheFromToken(token, "authinfo");
		}

		if (authInfo == null) {
			// 没有获取到则直接从DB中获取
			List<AuthInfo> authInfos = authorityService.selectByUserId(id);
			if (!authInfos.isEmpty()) {
				authInfo = authInfos.get(0);
			}
		}

		ret.put("data", authInfo);
		ret.put("code", Constant.REQ_SUCCESS_CODE);

		return ResponseEntity.ok(ret);
	}

	/**
	 * 通过Token中的用户信息获取菜单列表
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/menu/getByToken", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getMenuByToken(HttpServletRequest req) {

		Map<String, Object> ret = new HashMap<>();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);
		Object data = Collections.EMPTY_LIST;
		Integer id = 0;
		Object authInfo = null;

		if (token.trim().split("#").length != 3 || !tokenService.validateToken(token)) {
			ret.put("code", "1004");
			ret.put("msg", "用户token令牌无效");
			return ResponseEntity.ok(ret);
		}

		if (!StringUtils.isEmpty(token)) {
			id = Integer.valueOf(token.split("#")[1]);
		}

		if (id > 0) {
			// 从redis中获取缓存信息
			authInfo = tokenService.getCacheFromToken(token, "authinfo");
		}

		// 没有获取到则直接从DB中获取
		if (authInfo == null) {
			List<AuthInfo> authInfos = authorityService.selectByUserId(id);
			if (!authInfos.isEmpty()) {
				authInfo = authInfos.get(0);
				tokenService.putCacheByToken(token, "authinfo", authInfos.get(0));
			}
		}

		if (authInfo != null) {
			// 生成菜单栏
			// 判断是否具有超管角色
			Set<Integer> resTypes = new HashSet<>(Arrays.asList(0, 1));
			if (Utils.hasSuperRole((AuthInfo) authInfo)) {
				data = Utils.generateMenu(resourceService.selectAll(), resTypes);
				// 缓存设置用户具有超级管理员角色
				tokenService.putCacheByToken(token, "superrole", "1");
			} else {
				data = Utils.generateMenu((AuthInfo) authInfo, resTypes);
			}
		}

		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", data);

		return ResponseEntity.ok(ret);
	}

	/**
	 * 获取所有菜单列表
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/menu/getAll", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getAllMenu(HttpServletRequest req, String resTypes) {
		Map<String, Object> ret = new HashMap<>();
		List<String> resTypeList = new ArrayList<>();
		Set<Integer> resTypeSet = new HashSet<>();

		if (StringUtils.isEmpty(resTypes)) {
			ret.put("data", Utils.generateMenu(resourceService.selectAll(), resTypeSet));
		} else {
			resTypeList = Arrays.asList(resTypes.trim().split(",")).stream().distinct()
					.filter(type -> !StringUtils.isEmpty(type)).map(type -> type.trim()).collect(Collectors.toList());
			resTypeSet = resTypeList.stream().map(type -> Integer.valueOf(type)).collect(Collectors.toSet());
			ret.put("data", Utils.generateMenu(resourceService.selectAllByType(resTypeList), resTypeSet));
		}
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		return ResponseEntity.ok(ret);
	}

}
