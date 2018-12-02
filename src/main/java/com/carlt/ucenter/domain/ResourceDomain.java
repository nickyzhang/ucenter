package com.carlt.ucenter.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

public class ResourceDomain implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int pid = -2;
	private String resType;
	private String resName;
	private String resUri;
	private int rank;
	private String mark;
	private String resIcon;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/** 排序字段 **/
	private String sortField;
	/** 排序类型(asc/desc) **/
	private String sortType;

	public ResourceDomain() {
	}

	public ResourceDomain(int id, int pid, String resType, String resName, String resUri, int rank, String resIcon,
			String mark) {
		super();
		this.id = id;
		this.pid = pid;
		this.resType = resType;
		this.resName = resName;
		this.resUri = resUri;
		this.rank = rank;
		this.resIcon = resIcon;
		this.mark = mark;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
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

	public String getResIcon() {
		return resIcon;
	}

	public void setResIcon(String resIcon) {
		this.resIcon = resIcon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
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

	public String getResUri() {
		return resUri;
	}

	public void setResUri(String resUri) {
		this.resUri = resUri;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void setSortFiled() {

		if(StringUtils.isEmpty(this.getSortType())) {
			// 默认升序
			this.setSortType("asc");
		}

		switch (this.getSortField()) {
		case "resType":
			this.setSortField("res_type");
			break;
		case "resName":
			this.setSortField("res_name");
			break;
		case "resUri":
			this.setSortField("res_uri");
			break;
		case "resIcon":
			this.setSortField("res_icon");
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

	@Override
	public String toString() {
		return this.getId() + ":" + this.getPid() + ":" + this.getResName() + ":" + this.getResType() + ":"
				+ this.getRank() + ":" + this.getResUri() + ":" + this.getMark();
	}

}
