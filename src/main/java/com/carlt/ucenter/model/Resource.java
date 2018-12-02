package com.carlt.ucenter.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 资源信息
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:34:59
 */
public class Resource implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int pid = -2;
	private int resType = 1;
	private String resName;
	private String resUri;
	private int rank;
	private String mark;
	private String resIcon;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	public Resource() {
	}

	public Resource(int id, int pid, int resType, String resName, String resUri, int rank, String resIcon,
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

	public int getResType() {
		return resType;
	}

	public void setResType(int resType) {
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
}
