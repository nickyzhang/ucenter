package com.carlt.ucenter.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户信息
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:35:32
 */
public class User implements Serializable {

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
	private int loginStatus = 1;
	private int actStatus = 1;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	public User(int id, String userCode, String password, String userName, String mark, String email,
			String mobilePhone, String deptCode, String position, int loginStatus, int actStatus, Date createTime,
			Date updateTime) {
		super();
		this.id = id;
		this.userCode = userCode;
		this.password = password;
		this.userName = userName;
		this.mark = mark;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.deptCode = deptCode;
		this.position = position;
		this.loginStatus = loginStatus;
		this.actStatus = actStatus;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public User() {
		super();
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

}
