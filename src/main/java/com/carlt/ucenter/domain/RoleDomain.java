package com.carlt.ucenter.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.carlt.ucenter.model.Role;

public class RoleDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String roleCode;
	private int roleType = -1;
	private String roleName;
	private String mark;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/** 角色关联的资源列表 **/
	private String resIds;

	/** 排序字段 **/
	private String sortField;
	/** 排序类型(asc/desc) **/
	private String sortType;

	public String getResIds() {
		return resIds;
	}

	public void setResIds(String resIds) {
		this.resIds = resIds;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Role toRole() {
		return new Role(id, roleCode, roleType, roleName, mark, createTime, updateTime);
	}
	
	public void setSortFiled() {

		if(StringUtils.isEmpty(this.getSortType())) {
			// 默认升序
			this.setSortType("asc");
		}

		switch (this.getSortField()) {
		case "roleCode":
			this.setSortField("role_code");
			break;
		case "roleName":
			this.setSortField("role_name");
			break;
		case "roleType":
			this.setSortField("role_type");
			break;
		case "createTime":
			this.setSortField("create_time");
			break;
		case "updateTime":
			this.setSortField("update_time");
			break;
		default:
			break;
		}
	}
}
