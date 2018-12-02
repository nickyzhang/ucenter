package com.carlt.ucenter.controller;

import java.util.Arrays;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carlt.ucenter.domain.BtnResourceDomain;
import com.carlt.ucenter.model.BtnResource;
import com.carlt.ucenter.service.BtnResourceService;
import com.carlt.ucenter.service.TokenService;
import com.carlt.ucenter.util.Constant;
import com.github.pagehelper.Page;

/**
 * 按钮级资源控制器
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年10月11日 上午9:05:42
 */
@RestController
@RequestMapping("/api/btnres")
public class BtnResourceController {

	private static Logger log = LoggerFactory.getLogger(BtnResourceController.class);

	@Autowired
	private BtnResourceService btnResourceService;

	@Autowired
	private TokenService tokenService;

	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getById(int id) {
		Map<String, Object> ret = new HashMap<>();
		BtnResource res = btnResourceService.getById(id);
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", res);
		return ResponseEntity.ok(ret);
	}

	/**
	 * 
	 * {@link #getByGroupCode}
	 * 
	 * @param userId
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/getByUserId", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getByUserId(int userId) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", btnResourceService.getByUserId(userId));
		return ResponseEntity.ok(ret);
	}

	/**
	 * {@link #getByGroupCode}
	 * 
	 * @param req
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/getByToken", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getByToken(HttpServletRequest req) {
		Map<String, Object> ret = new HashMap<>();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);

		if (!StringUtils.isEmpty(token)) {
			int userId = Integer.valueOf(token.trim().split("#")[1]);
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			ret.put("data", btnResourceService.getByUserId(userId));
		}

		return ResponseEntity.ok(ret);
	}

	/**
	 * 通过token与组编码获取按钮组列表信息
	 * 
	 * @param req
	 * @param groupCode
	 * @return
	 */
	@RequestMapping(value = "/getByGroupCode", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getByGroupCode(HttpServletRequest req, String groupCode) {
		Map<String, Object> ret = new HashMap<>();
		String token = req.getHeader(Constant.REQ_TOKEN_HEADER_KEY);

		if (!StringUtils.isEmpty(token)) {
			int userId = Integer.valueOf(token.trim().split("#")[1]);
			Object superRole = tokenService.getCacheFromToken(token, "superrole");
			if (superRole != null && "1".equals(superRole)) {
				ret.put("data", btnResourceService.getByGroupCode(groupCode));
			} else {
				ret.put("data", btnResourceService.getByGroupCode(userId, groupCode));
			}
			ret.put("code", Constant.REQ_SUCCESS_CODE);
		} else {
			ret.put("code", "1002");
			ret.put("msg", "请求token不存在");
		}

		return ResponseEntity.ok(ret);
	}

	/**
	 * 通过角色ID获取按钮组列表信息
	 * 
	 * @param req
	 * @param groupCode
	 * @return
	 */
	@RequestMapping(value = "/getByRoleId", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getByRoleId(HttpServletRequest req, String roleId) {
		Map<String, Object> ret = new HashMap<>();
		ret.put("data", btnResourceService.getByRoleId(roleId));
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		return ResponseEntity.ok(ret);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Map<String, Object> selectAll() {
		Map<String, Object> ret = new HashMap<>();
		List<BtnResource> roles = btnResourceService.selectAll();
		ret.put("code", Constant.REQ_SUCCESS_CODE);
		ret.put("data", roles);
		return ret;
	}

	/**
	 * 过滤查询列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param queryRes
	 * @return
	 */
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public Map<String, Object> selectByPage(int pageNo, int pageSize, BtnResourceDomain queryRes) {

		Map<String, Object> ret = new HashMap<>();
		if (!StringUtils.isEmpty(queryRes.getSortField()))
			queryRes.setSortFiled();

		Page<BtnResource> pageRet = btnResourceService.select(pageNo, pageSize, queryRes);

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
		log.info("start delete res:{}", ids);
		List<String> resIds = Arrays.asList(ids.trim().split(","));

		int num = btnResourceService.delete(resIds);

		if (num > 0) {
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			data.put("num", num);
		} else {
			ret.put("code", "5004");
			ret.put("msg", "删除资源失败，系统删除没有删除任何资源");
		}

		ret.put("data", data);
		return ret;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Map<String, Object> insert(BtnResource res) {
		res.setUpdateTime(new Date());
		Map<String, Object> ret = new HashMap<>();
		try {
			int num = btnResourceService.insert(res);
			if (num > 0) {
				ret.put("code", Constant.REQ_SUCCESS_CODE);
			} else {
				ret.put("code", "5001");
				ret.put("msg", "添加资源失败");
			}
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				ret.put("code", "5003");
				ret.put("msg", "数据库唯一键冲突，可能是资源编码重复导致");
			} else {
				ret.put("code", "5001");
				ret.put("msg", "添加资源失败," + e.getMessage());
			}
		}

		ret.put("data", res);
		return ret;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public Map<String, Object> update(BtnResource res) {
		res.setUpdateTime(new Date());
		int num = btnResourceService.update(res);
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();

		if (num > 0) {
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			data.put("num", num);
		} else {
			ret.put("code", "5002");
			ret.put("msg", "更新资源失败");
		}

		ret.put("data", data);
		return ret;
	}

	@RequestMapping(value = "/linkRole", method = RequestMethod.POST)
	public Map<String, Object> linkRole(String roleId, String resIds) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			List<String> resIdList = Arrays.asList(resIds.trim().split(",")).stream().map(id -> id.trim())
					.filter(id -> !StringUtils.isEmpty(id)).distinct().collect(Collectors.toList());
			int num = btnResourceService.linkRole(roleId, resIdList);
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			data.put("num", num);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				ret.put("code", "5003");
				ret.put("msg", "数据库唯一键冲突，可能是资源编码重复导致");
			} else {
				ret.put("code", "5005");
				ret.put("msg", "资源关联角色失败," + e.getMessage());
			}
		}

		ret.put("data", data);
		return ret;
	}

	@RequestMapping(value = "/unlinkRole", method = RequestMethod.POST)
	public Map<String, Object> unlinkRole(String roleId, String resIds) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		try {
			List<String> resIdList = Arrays.asList(resIds.trim().split(",")).stream().map(id -> id.trim())
					.filter(id -> !StringUtils.isEmpty(id)).distinct().collect(Collectors.toList());
			int num = btnResourceService.unlinkRole(roleId, resIdList);
			ret.put("code", Constant.REQ_SUCCESS_CODE);
			data.put("num", num);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				ret.put("code", "5003");
				ret.put("msg", "数据库唯一键冲突，可能是资源编码重复导致");
			} else {
				ret.put("code", "5006");
				ret.put("msg", "解除资源关联角色失败," + e.getMessage());
			}
		}

		ret.put("data", data);
		return ret;
	}

}
