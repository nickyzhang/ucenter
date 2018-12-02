package com.carlt.ucenter.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.carlt.ucenter.model.Resource;

public class MenuInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int pid;
	private String resName;
	private String resUri;
	private int resType;
	private int rank;
	private String mark;
	private String resIcon;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	private List<MenuInfo> subMenus;

	public MenuInfo() {
		super();
	}

	public MenuInfo(int id, int pid, String resName, int resType, String resUri, int rank, String mark, String resIcon,
			Date createTime, Date updateTime, List<MenuInfo> subMenus) {
		super();
		this.id = id;
		this.pid = pid;
		this.resName = resName;
		this.resType = resType;
		this.resUri = resUri;
		this.rank = rank;
		this.mark = mark;
		this.resIcon = resIcon;
		this.updateTime = updateTime;
		this.createTime = createTime;
		this.subMenus = subMenus;
	}

	public int getResType() {
		return resType;
	}

	public void setResType(int resType) {
		this.resType = resType;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
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

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public List<MenuInfo> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<MenuInfo> subMenus) {
		this.subMenus = subMenus;
	}

	public static MenuInfo resourceToMenu(Resource res) {
		return new MenuInfo(res.getId(), res.getPid(), res.getResName(), res.getResType(), res.getResUri(),
				res.getRank(), res.getMark(), res.getResIcon(), res.getCreateTime(), res.getUpdateTime(),
				new ArrayList<>());
	}
}
