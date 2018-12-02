package com.carlt.ucenter.model;

import java.io.Serializable;
import java.util.Date;

public class OozieTask implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String task_name;
	private Date exec_date;
	private int task_status;
	private String mark;
	private Date update_time;

	public OozieTask() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public Date getExec_date() {
		return exec_date;
	}

	public void setExec_date(Date exec_date) {
		this.exec_date = exec_date;
	}

	public int getTask_status() {
		return task_status;
	}

	public void setTask_status(int task_status) {
		this.task_status = task_status;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

}
