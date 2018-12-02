package com.carlt.ucenter.model;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限信息
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:34:44
 */
public class AuthInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int userId;
	private String userCode;
	private List<RoleInfo> roles;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public List<RoleInfo> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleInfo> roles) {
		this.roles = roles;
	}
}
