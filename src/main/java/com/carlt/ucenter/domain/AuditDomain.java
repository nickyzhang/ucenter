package com.carlt.ucenter.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 审计信息
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:34:32
 */
public class AuditDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String token;
	private int userId = -1;
	private String reqUrl;
	private String queryStr;
	private String rmtIpAddr;
	private String localIpAddr;
	private String reqMethod;
	private String mark;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date oprTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	private String userCode;
	private String userName;

	private String accessRet;

	/** 排序字段 **/
	private String sortField;
	/** 排序类型(asc/desc) **/
	private String sortType;

	public AuditDomain() {
		super();
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getAccessRet() {
		return accessRet;
	}

	public void setAccessRet(String accessRet) {
		this.accessRet = accessRet;
	}

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public void setSortFiled() {

		if (StringUtils.isEmpty(this.getSortType())) {
			// 默认升序
			this.setSortType("asc");
		}

		switch (this.getSortField()) {
		case "userId":
			this.setSortField("user_id");
			break;
		case "reqUrl":
			this.setSortField("req_url");
			break;
		case "queryStr":
			this.setSortField("query_str");
			break;
		case "rmtIpAddr":
			this.setSortField("rmt_ip_addr");
			break;
		case "localIpAddr":
			this.setSortField("local_ip_addr");
			break;
		case "reqMethod":
			this.setSortField("req_method");
			break;
		case "oprTime":
			this.setSortField("opr_time");
			break;
		case "userCode":
			this.setSortField("user_code");
			break;
		case "userName":
			this.setSortField("user_name");
		case "accessRet":
			this.setSortField("access_ret");
			break;
		default:
			break;
		}
	}
}
