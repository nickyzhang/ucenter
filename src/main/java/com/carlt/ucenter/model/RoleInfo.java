package com.carlt.ucenter.model;

import java.io.Serializable;
import java.util.List;

/**
 * 角色资源信息
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:35:20
 */
public class RoleInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int roleId;
	private int roleType = -1;
	private List<Resource> resources;

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
}