package com.carlt.ucenter.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carlt.ucenter.domain.AuthChangeMsg;
import com.carlt.ucenter.domain.UserDomain;
import com.carlt.ucenter.exception.RestfulJsonException;
import com.carlt.ucenter.model.AuthInfo;
import com.carlt.ucenter.model.Role;
import com.carlt.ucenter.model.User;
import com.carlt.ucenter.service.AuthorityService;
import com.carlt.ucenter.service.ResourceService;
import com.carlt.ucenter.service.RoleService;
import com.carlt.ucenter.service.TokenService;
import com.carlt.ucenter.service.UserService;
import com.carlt.ucenter.service.imlp.AuthInfoCacheService;
import com.carlt.ucenter.util.Constant;
import com.carlt.ucenter.util.MD5;
import com.carlt.ucenter.util.Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;

/**
 * 用户操作API
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月15日 下午7:36:15
 *
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	private Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private AuthInfoCacheService authInfoCacheService;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> selectByPrimaryKey(int id) {
		Map<String, Object> ret = new HashMap<>();
		User user = userService.selectByPrimaryKey(id);
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", user);
		return ResponseEntity.ok(ret);
	}

	@RequestMapping(value = "/getByToken", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getByToken(HttpServletRequest req) {

		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);
		Integer id = 0;
		Object authInfo = null;

		if (!StringUtils.isEmpty(token)) {
			id = Integer.valueOf(token.split("#")[1]);
		}

		User user = new User();
		if (id > 0)
			user = userService.selectByPrimaryKey(id);

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
			Object menu = Collections.EMPTY_LIST;
			Set<Integer> resTypes = new HashSet<>(Arrays.asList(0,1));
			if (Utils.hasSuperRole((AuthInfo) authInfo)) {
				menu = Utils.generateMenu(resourceService.selectAll(), resTypes);
				// 缓存设置用户具有超级管理员角色
				tokenService.putCacheByToken(token, "superrole", "1");
			} else {
				menu = Utils.generateMenu((AuthInfo) authInfo, resTypes);
			}
			data.put("menu", menu);
		}

		data.put("user", user);
		ret.put("data", data);
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		return ResponseEntity.ok(ret);
	}

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> selectAll() {
		Map<String, Object> ret = new HashMap<>();
		List<User> users = userService.selectAll();
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", users);
		return ret;
	}

	/**
	 * 根据查询条件和分页参数进行数据过滤查询
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param queryUser
	 * @return
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public Map<String, Object> selectByPage(int pageNo, int pageSize, UserDomain queryUser) {

		Map<String, Object> ret = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(queryUser.getSortField()))
			queryUser.setSortFiled();

		Page<User> pageRet = userService.selectAll(pageNo, pageSize, queryUser);

		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("total", pageRet.getTotal());
		ret.put("pages", pageRet.getPages());
		ret.put("curPageNum", pageRet.getPageNum());
		ret.put("pageSize", pageRet.getPageSize());
		ret.put("data", pageRet.getResult());

		return ret;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(String ids) {

		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		log.info("start delete users:{}", ids);
		int num = userService.delete(Arrays.asList(ids.trim().split(",")));

		if (num > 0) {
			data.put("num", num);
			ret.put("code", Constant.REQ_SUCCESS_CODE);

			Arrays.asList(ids.trim().split(";")).forEach(userId -> {
				String token = tokenService.getToken(Integer.valueOf(userId.trim()));
				// 如果token存在则移除
				if (!StringUtil.isEmpty(token) && tokenService.validateToken(token)) {
					tokenService.removeToken(token);
					log.info("remove token cache, token:{}", token);
				}
			});
		} else {
			ret.put("msg", "删除用户失败，系统删除没有删除任何用户");
			ret.put("code", "2004");
		}

		ret.put("data", data);
		return ret;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> insert(UserDomain userDomain) {
		userDomain.setCreateTime(new Date());
		userDomain.setUpdateTime(new Date());
		User user = userDomain.toUser();
		Map<String, Object> ret = new HashMap<>();
		
		user.setPassword(MD5.encode(user.getPassword()));
		
		boolean userExists = userService.userExists(user.getUserCode());
		
		if(userExists) {
			ret.put("code", "2007");
			ret.put("msg", "用户名已存在");
			return ret;
		}
		
		int num = userService.insert(user);
		if (num > 0) {
			if (null != userDomain.getRoleIds() && !StringUtil.isEmpty(userDomain.getRoleIds().trim())) {
				int roleNum = userService.linkRole(user.getId() + "",
						Arrays.asList(userDomain.getRoleIds().trim().split(",")));
				log.info("user:{}, linked roles:{}, ret:{}", userDomain.getId(), userDomain.getRoleIds(), roleNum);
				// 正常不用发送权限更新消息，因为新增的用户一定是没有登录的
				// if (roleNum > 0) {
				// // 发送用户权限信息更改消息(redis pub/sub)，告知后台需要更新用户权限信息。做成异步、解耦的方式
				// AuthChangeMsg msg = new AuthChangeMsg("user", "linkRole",
				// Arrays.asList(userDomain.getId(), userDomain.getRoleIds()));
				// authInfoCacheService.sendMsg(msg);
				// }
			}
			ret.put("code", Constant.REQ_SUCCESS_CODE);
		} else {
			ret.put("code", "2001");
			ret.put("msg", "添加失败");
		}
		ret.put("data", user);
		return ret;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public Map<String, Object> updateByPrimaryKeySelective(UserDomain userDomain) {

		Map<String, Object> ret = new HashMap<>();
		List<String> roleList = new ArrayList<>();

		// if (null != userDomain.getRoleIds() &&
		// !StringUtil.isEmpty(userDomain.getRoleIds().trim()))
		if (!StringUtil.isEmpty(userDomain.getRoleIds().trim()))
			roleList = Arrays.asList(userDomain.getRoleIds().trim().split(","));

		// 对用户密码进行MD5加密
		if(userDomain.getPassword() != null && !"".equals(userDomain.getPassword())) {
			userDomain.setPassword(MD5.encode(userDomain.getPassword()));
		}

		int num = userService.updateByPrimaryKeySelective(userDomain.toUser(), roleList);

		if (num > 0) {
			updateLoginUserState(userDomain);
			// 发送用户权限信息更改消息(redis pub/sub)，告知后台需要更新用户权限信息。做成异步、解耦的方式
			AuthChangeMsg msg = new AuthChangeMsg("user", "linkRole",
					Arrays.asList(userDomain.getId(), userDomain.getRoleIds()));
			authInfoCacheService.sendMsg(msg);
			ret.put("code", Constant.REQ_SUCCESS_CODE);
		} else {
			ret.put("code", "2002");
			ret.put("msg", "更新用户失败");
		}
		ret.put("data", Collections.emptyMap());
		return ret;
	}

	/**
	 * 更新登录用户状态
	 * 
	 * @param userDomain
	 */
	private void updateLoginUserState(UserDomain userDomain) {
		String token = null;
		// 锁定账户或强制登出
		if (userDomain.getActStatus() != 1 || userDomain.getLoginStatus() == 0) {
			token = tokenService.getToken(userDomain.getId());
		}

		// 如果token存在则移除
		if (token != null) {
			int num = userService.logout(userDomain.getId());
			if (num > 0) {
				tokenService.removeToken(token);
			}
		}
	}

	@RequestMapping(value = "/updateByToken", method = RequestMethod.PUT)
	public Map<String, Object> updateByToken(HttpServletRequest req, User user) {
		Map<String, Object> ret = new HashMap<>();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);
		Integer id = 0;
		if (!StringUtils.isEmpty(token)) {
			id = Integer.valueOf(token.split("#")[1]);
			user.setId(id);
		}
		// 对用户密码进行MD5加密
		user.setPassword(MD5.encode(user.getPassword()));
		int num = userService.updateByToken(user);
		if (num > 0) {
			ret.put("code", Constant.REQ_SUCCESS_CODE);
		} else {
			ret.put("code", "2002");
			ret.put("msg", "更新用户失败");
		}
		return ret;
	}
	
	@RequestMapping(value = "/updatePWDByToken", method = RequestMethod.PUT)
	public Map<String, Object> updatePWDByToken(HttpServletRequest req, String oldPassword, String newPassword) {
		Map<String, Object> ret = new HashMap<>();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);
		Integer id = 0;
		if (!StringUtils.isEmpty(token)) {
			id = Integer.valueOf(token.split("#")[1]);
		}
		String oldMd5 = MD5.encode(oldPassword);
		String newMd5 = MD5.encode(newPassword);
		int num = userService.updatePWDByToken(id, oldMd5, newMd5);
		if (num > 0) {
			ret.put("code", Constant.REQ_SUCCESS_CODE);
		} else {
			ret.put("code", "2006");
			ret.put("msg", "用户修改密码失败，用户不存在或原密码错误");
		}
		return ret;
	}

	@RequestMapping(value = "/linkRole", method = RequestMethod.PUT)
	public Map<String, Object> linkRole(String userId, String roleIds) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		List<String> roleList = new ArrayList<>();
		if (!StringUtil.isEmpty(roleIds.trim())) {
			roleList = Arrays.asList(roleIds.trim().split(","));
		}
		int num = userService.linkRole(userId, roleList);
		// 如果用户角色信息有更改，则发送消息
		if (num > 0) {
			// 发送用户权限信息更改消息(redis pub/sub)，告知后台需要更新用户权限信息。做成异步、解耦的方式
			AuthChangeMsg msg = new AuthChangeMsg("user", "linkRole", Arrays.asList(userId, roleIds));
			authInfoCacheService.sendMsg(msg);
		}

		data.put("num", num);
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", data);
		return ret;
	}

	@RequestMapping(value = "/getRoleByUserId", method = RequestMethod.GET)
	public Map<String, Object> getRoleByUserId(String userId) throws RestfulJsonException {
		Map<String, Object> ret = new HashMap<>();
		try {
			List<Role> roles = roleService.getRoleByUserId(userId);
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			ret.put("data", roles);
			return ret;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			Utils.handleRestfulJsonException(e);
		}

		ret.put("code", "2005");
		ret.put("msg", "查询用户角色列表失败");
		ret.put("data", Collections.emptyList());
		return ret;
	}

}
