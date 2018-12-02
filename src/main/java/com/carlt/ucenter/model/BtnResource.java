package com.carlt.ucenter.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 按钮级资源信息
 * 
 * @author ansen.zhu@carlt.com.cn
 * @version 1.0
 * @date 2018年8月22日 下午4:34:59
 */
public class BtnResource implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int type;
	private String groupCode;
	private String name;
	private String code;
	private String value;
	private String remark;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	public BtnResource() {
	}

	public BtnResource(int id, int type, String groupCode, String name, String code, String value, String remark,
			Date updateTime) {
		super();
		this.id = id;
		this.type = type;
		this.groupCode = groupCode;
		this.name = name;
		this.code = code;
		this.value = value;
		this.remark = remark;
		this.updateTime = updateTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
