package com.bbh.ets.reports;

import java.util.Date;



public class EMPTaskBo {

	private String emp_name;
	private Integer task_id;
	private String task_assigned;
	private String reviewedBy;
	private String assignedBy;
	private String remark;
	private String status;
	private String date_assigned;
	private String date_completed;
	

	
	

	public String getAssignedBy() {
		return assignedBy;
	}
	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
	public void setDate_assigned(String date_assigned) {
		this.date_assigned = date_assigned;
	}
	public String getDate_assigned() {
		return date_assigned;
	}
	public void setDate_completed(String date_completed) {
		this.date_completed = date_completed;
	}
	public String getDate_completed() {
		return date_completed;
	}
	public void setTask_assigned(String task_assigned) {
		this.task_assigned = task_assigned;
	}
	public String getTask_assigned() {
		return task_assigned;
	}
	public String getReviewedBy() {
		return reviewedBy;
	}
	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}
	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}
	public Integer getTask_id() {
		return task_id;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public String getEmp_name() {
		return emp_name;
	}

	
	
}
