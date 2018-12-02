package com.carlt.ucenter.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 审计信息
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:34:32
 */
public class AuditInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int userId = -1;
	private String token;
	private String reqUrl;
	private String queryStr;
	private String rmtIpAddr;
	private String localIpAddr;
	private String reqMethod;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date oprTime;
	private String mark;

	private String userCode;
	private String userName;

	private String accessRet;

	public AuditInfo() {
		super();
	}

	public AuditInfo(int id, String token, String reqUrl, String queryStr, String rmtIpAddr, String localIpAddr,
			String reqMethod, Date oprTime, String accessRet, String mark) {
		super();
		this.id = id;
		this.token = token;
		if (this.token != null) {
			String[] arr = this.token.trim().replaceAll(" ", "").split("#");
			if (arr.length > 1)
				this.userId = Integer.valueOf(arr[1]);
		}
		this.reqUrl = reqUrl;
		this.queryStr = queryStr;
		this.rmtIpAddr = rmtIpAddr;
		this.localIpAddr = localIpAddr;
		this.reqMethod = reqMethod;
		this.oprTime = oprTime;
		this.mark = mark;
		this.accessRet = accessRet;
	}

	public String getAccessRet() {
		return accessRet;
	}

	public void setAccessRet(String accessRet) {
		this.accessRet = accessRet;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getRmtIpAddr() {
		return rmtIpAddr;
	}

	public void setRmtIpAddr(String rmtIpAddr) {
		this.rmtIpAddr = rmtIpAddr;
	}

	public String getLocalIpAddr() {
		return localIpAddr;
	}

	public void setLocalIpAddr(String localIpAddr) {
		this.localIpAddr = localIpAddr;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public Date getOprTime() {
		return oprTime;
	}

	public void setOprTime(Date oprTime) {
		this.oprTime = oprTime;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

}
