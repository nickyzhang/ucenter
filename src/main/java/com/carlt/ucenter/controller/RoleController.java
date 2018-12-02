package com.carlt.ucenter.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.carlt.ucenter.domain.RoleDomain;
import com.carlt.ucenter.model.Role;
import com.carlt.ucenter.model.User;
import com.carlt.ucenter.service.RoleService;
import com.carlt.ucenter.service.UserService;
import com.carlt.ucenter.service.imlp.AuthInfoCacheService;
import com.carlt.ucenter.util.Constant;
import com.github.pagehelper.Page;
import com.github.pagehelper.util.StringUtil;

/**
 * 角色操作API
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月15日 下午7:36:41
 *
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

	private Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private AuthInfoCacheService authInfoCacheService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> selectByPrimaryKey(int id) {
		Map<String, Object> ret = new HashMap<>();
		Role role = roleService.selectByPrimaryKey(id);
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", role);
		return ResponseEntity.ok(ret);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> selectAll() {
		Map<String, Object> ret = new HashMap<>();
		List<Role> roles = roleService.selectAll();
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", roles);
		return ret;
	}

	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public Map<String, Object> selectByPage(int pageNo, int pageSize, RoleDomain roleDomain) {

		Map<String, Object> ret = new HashMap<>();
		if (!StringUtils.isEmpty(roleDomain.getSortField()))
			roleDomain.setSortFiled();

		Page<Role> pageRet = roleService.select(pageNo, pageSize, roleDomain);

		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("total", pageRet.getTotal());
		ret.put("pages", pageRet.getPages());
		ret.put("curPageNum", pageRet.getPageNum());
		ret.put("pageSize", pageRet.getPageSize());
		ret.put("data", pageRet.getResult());

		return ret;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(HttpServletRequest req, String ids) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		// 首先获取与角色有关联的用户，如果删除了角色需要通知到这些用户更新角色和权限缓存信息
		List<User> users = userService.selectUserByRole(Arrays.asList(ids.trim().split(",")));
		log.info("role linked users:{}", users);
		int num = roleService.delete(Arrays.asList(ids.trim().split(",")));
		if (num > 0) {
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			data.put("num", num);
			// 发送权限信息更改消息(redis pub/sub)，告知后台需要更新用户权限信息。做成异步、解耦的方式
			if (!users.isEmpty()) {
				AuthChangeMsg msg = new AuthChangeMsg("role", "delete",
						users.stream().map(u -> u.getId()).collect(Collectors.toList()));
				authInfoCacheService.sendMsg(msg);
				log.info("role delete:{}, send auth info change msg", ids);
			}
		} else {
			ret.put("code", "3004");
			ret.put("msg", "删除角色失败，没有删除任何用户");
		}

		ret.put("data", data);
		return ret;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> insert(Role role) {
		role.setCreateTime(new Date());
		role.setUpdateTime(new Date());
		int num = roleService.insert(role);
		Map<String, Object> ret = new HashMap<>();
		if (num > 0) {
			ret.put("code", Constant.REQ_SUCCESS_CODE);
		} else {
			ret.put("code", "2001");
			ret.put("msg", "添加角色失败");
		}
		ret.put("data", role);
		return ret;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public Map<String, Object> updateByPrimaryKeySelective(RoleDomain roleDomain) {

		Map<String, Object> ret = new HashMap<>();
		List<String> resIds = null;

		if (roleDomain.getResIds() != null && !StringUtils.isEmpty(roleDomain.getResIds().trim()))
			resIds = Arrays.asList(roleDomain.getResIds().trim().split(","));

		int num = roleService.updateByPrimaryKeySelective(roleDomain.toRole(), resIds);

		if (num > 0) {
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			// 暂时又改回为：更新角色不同时更新关联的资源信息，所以不用发送权限更新消息
			// 发送权限信息更改消息(redis pub/sub)，告知后台需要更新用户权限信息。做成异步、解耦的方式
//			AuthChangeMsg msg = new AuthChangeMsg("role", "linkRes",
//					Arrays.asList(roleDomain.getId(), roleDomain.getResIds()));
//			authInfoCacheService.sendMsg(msg);
//			log.info("role:{} link res:{}, send auth info change msg", roleDomain.getId(), roleDomain.getResIds());
		} else {
			ret.put("code", "2002");
			ret.put("msg", "更新角色失败");
		}

		ret.put("data", Collections.emptyMap());
		return ret;
	}

	@RequestMapping(value = "/linkRes", method = RequestMethod.PUT)
	public Map<String, Object> linkResource(String roleId, String resIds) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		List<String> resList = new ArrayList<>();

		if (!StringUtil.isEmpty(resIds.trim())) {
			resList = Arrays.asList(resIds.trim().split(","));
		}

		int num = roleService.linkRes(roleId, resList);

		if (num > 0) {
			// 发送权限信息更改消息(redis pub/sub)，告知后台需要更新用户权限信息。做成异步、解耦的方式
			AuthChangeMsg msg = new AuthChangeMsg("role", "linkRes", Arrays.asList(roleId, resIds));
			authInfoCacheService.sendMsg(msg);
			log.info("role:{} link res:{}, send auth info change msg", roleId, resIds);
		}

		data.put("num", num);
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", data);
		return ret;
	}

	@RequestMapping(value = "/getResourceByRoleId", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getResourceByRoleId(int roleId) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", roleService.getResourceByRole(roleId));
		return ResponseEntity.ok(ret);
	}

}
