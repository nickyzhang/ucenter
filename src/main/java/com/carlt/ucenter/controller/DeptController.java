package com.carlt.ucenter.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carlt.ucenter.model.Dept;
import com.carlt.ucenter.service.DeptService;

/**
 * 部门管理API（暂不实现）
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月16日 下午3:50:16
 *
 */
@RestController
@RequestMapping("/api/dept")
@Deprecated
public class DeptController {

	private Logger log = LoggerFactory.getLogger(DeptController.class);

	@Autowired
	private DeptService deptService;

	@Deprecated
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ResponseEntity<Dept> selectByPrimaryKey(int id) {
		Dept dept = deptService.selectByPrimaryKey(id);
		return ResponseEntity.ok(dept);
	}

	@Deprecated
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<Dept> selectAll() {
		List<Dept> ret = deptService.selectAll();
		ret.forEach(role -> {
			System.out.println(role.getCreateTime());
			System.out.println(role.getUpdateTime());
			System.out.println(role);
		});
		log.info("roles:{}", ret);
		return ret;
	}

	@Deprecated
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public List<Dept> selectByPage(int pageNo, int pageSize) {
		return deptService.selectAll(pageNo, pageSize);
	}

	@Deprecated
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public int deleteByPrimaryKey(int id) {
		return deptService.deleteByPrimaryKey(id);
	}

	@Deprecated
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public int insert(Dept dept) {
		int ret = deptService.insert(dept);
		ret = dept.getId();
		return ret;
	}

	/**
	 * 直接通过传递表单形式的参数绑定到实体T中，可以减少通过N个req.getParameter("xxx")获取参数然后再手动绑定到实体T中带来的麻烦
	 * 
	 * @param dept
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/update1", method = RequestMethod.PUT)
	public int updateByPrimaryKeySelective1(Dept dept) {
		int ret = deptService.updateByPrimaryKeySelective(dept);
		return ret;
	}

	/**
	 * 通过RequestBody绑定参数，可以用来传递List<T>形式参数
	 * 注意：请求的Content-Type必须是"application/json; charset=UTF-8",
	 * 
	 * @param req
	 * @param dept
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/update2", method = RequestMethod.PUT)
	public int updateByPrimaryKeySelective2(@RequestBody Dept dept) {
		int ret = deptService.updateByPrimaryKeySelective(dept);
		return ret;
	}
}
