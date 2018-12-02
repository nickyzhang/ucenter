package com.carlt.ucenter.domain;

import java.io.Serializable;

import org.json.JSONObject;

public class AuthChangeMsg implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String type;
	private String operate;
	private Object data;

	public AuthChangeMsg() {
		super();
	}

	public AuthChangeMsg(String srcMsg) {
		JSONObject json = new JSONObject(srcMsg);
		this.type = json.getString("type");
		this.operate = json.getString("operate");
		this.data = json.get("data");
	}



	public AuthChangeMsg(String type, String operate, Object data) {
		super();
		this.type = type;
		this.operate = operate;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
