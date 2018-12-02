package com.carlt.ucenter.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.carlt.ucenter.model.User;

public class UserDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String userCode;
	private String password;
	private String userName;
	private String mark;
	private String email;
	private String mobilePhone;
	private String deptCode;
	private String position;
	private int loginStatus = -1;
	private int actStatus = -1;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/** 角色ID列表（英文逗号隔开） **/
	private String roleIds;

	/** 排序字段 **/
	private String sortField;
	/** 排序类型(asc/desc) **/
	private String sortType;

	public String getSortField() {
		return sortField;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public int getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}

	public int getActStatus() {
		return actStatus;
	}

	public void setActStatus(int actStatus) {
		this.actStatus = actStatus;
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
	
	public void setSortFiled() {

		if(StringUtils.isEmpty(this.getSortType())) {
			// 默认升序
			this.setSortType("asc");
		}
		
		switch (this.getSortField()) {
		case "userCode":
			this.setSortField("user_code");
			break;
		case "userName":
			this.setSortField("user_name");
			break;
		case "mobilePhone":
			this.setSortField("mobile_phone");
			break;
		case "deptCode":
			this.setSortField("dept_code");
			break;
		case "loginStatus":
			this.setSortField("login_status");
			break;
		case "actStatus":
			this.setSortField("act_status");
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

	public User toUser() {
		return new User(id, userCode, password, userName, mark, email, mobilePhone, deptCode, position, loginStatus,
				actStatus, createTime, updateTime);
	}

}
