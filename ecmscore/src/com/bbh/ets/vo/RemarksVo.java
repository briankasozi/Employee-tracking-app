package com.bbh.ets.vo;

import java.io.Serializable;
import java.util.Date;

public class RemarksVo implements Serializable {
	
	private static final long serialVersionUID = 4428604732113077068L;
	
	private Integer remarksId;
	private Integer taskSummaryID;
	private Integer EmployeeId;
	private String employeeName;
	private Integer EmployeeTypeId;
	private String remarks;
	private Date CreatedOn;
	
	/**
	 * @return the remarksId
	 */
	public Integer getRemarksId() {
		return remarksId;
	}
	/**
	 * @param remarksId the remarksId to set
	 */
	public void setRemarksId(Integer remarksId) {
		this.remarksId = remarksId;
	}
	/**
	 * @return the taskSummaryID
	 */
	public Integer getTaskSummaryID() {
		return taskSummaryID;
	}
	/**
	 * @param taskSummaryID the taskSummaryID to set
	 */
	public void setTaskSummaryID(Integer taskSummaryID) {
		this.taskSummaryID = taskSummaryID;
	}
	/**
	 * @return the employeeId
	 */
	public Integer getEmployeeId() {
		return EmployeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Integer employeeId) {
		EmployeeId = employeeId;
	}
	/**
	 * @return the employeeTypeId
	 */
	public Integer getEmployeeTypeId() {
		return EmployeeTypeId;
	}
	/**
	 * @param employeeTypeId the employeeTypeId to set
	 */
	public void setEmployeeTypeId(Integer employeeTypeId) {
		EmployeeTypeId = employeeTypeId;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return CreatedOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		CreatedOn = createdOn;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
}

