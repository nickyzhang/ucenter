package com.carlt.ucenter.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.carlt.ucenter.exception.PageException;
import com.carlt.ucenter.exception.RestfulJsonException;
import com.carlt.ucenter.model.Role;

/**
 * 测试使用
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月21日 下午4:01:14
 */
@Controller
@RequestMapping("/test")
@Deprecated
public class HelloController {

	/**
	 * restful JSON异常测试
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@RequestMapping("/json")
	public Object json() throws Exception {
		try {
			int test = 1 / 0;
		} catch (Exception e) {
			throw new RestfulJsonException("发生restful错误测试", e);
		}

		return "test";
	}

	/**
	 * page异常测试
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	@RequestMapping("/hello")
	public String hello() throws Exception {
		try {
			int test = 1 / 0;
		} catch (Exception e) {
			throw new PageException("发生page错误测试", e);
		}

		return "test";
	}

	@Deprecated
	@RequestMapping(value = "/linkRoleTest")
	public int linkRole(@RequestBody ArrayList<Role> roles) {

		if (roles != null) {
			roles.forEach(role -> {
				System.out.println(role.getId() + "," + role.getRoleName());
			});
			return roles.size();
		}

		return 0;
	}

}
