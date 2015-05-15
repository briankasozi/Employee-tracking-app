package com.bbh.ets.bo;

import java.io.Serializable;

public class UserBo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer  userId;
	private String userName;
	private Integer  roleId;
	private String roleName;
	private Integer sessionId;
	private Integer loginEmpID;
	private Boolean taskPrev;
	private String loginEmployeeName;
	private String loginEmployeedesignation;
	private String departments;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getSessionId() {
		return sessionId;
	}
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getLoginEmpID() {
		return loginEmpID;
	} 
	public void setLoginEmpID(Integer loginEmpID) {
		this.loginEmpID = loginEmpID;
	}
	public Boolean getTaskPrev() {
		return taskPrev;
	}
	public void setTaskPrev(Boolean taskPrev) {
		this.taskPrev = taskPrev;
	}
	public String getLoginEmployeeName() {
		return loginEmployeeName;
	}
	public void setLoginEmployeeName(String loginEmployeeName) {
		this.loginEmployeeName = loginEmployeeName;
	}
	public String getLoginEmployeedesignation() {
		return loginEmployeedesignation;
	}
	public void setLoginEmployeedesignation(String loginEmployeedesignation) {
		this.loginEmployeedesignation = loginEmployeedesignation;
	}
	public String getDepartments() {
		return departments;
	}
	public void setDepartments(String departments) {
		this.departments = departments;
	}
}
