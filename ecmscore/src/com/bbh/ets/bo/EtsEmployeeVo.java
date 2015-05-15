package com.bbh.ets.bo;

import java.io.Serializable; 

public class EtsEmployeeVo implements Serializable{
	
	private static final long serialVersionUID = -8130333411987116180L;
	private int employeeID;
	private String employeeName;
	private String designation;
	private String empCode;
	private String empEmailID;
	private String empDateofJoining;
	private String empPhoneNo;
	private String empAddress;
	private String experience;
	private String previousEmployeer;
	public int getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpEmailID() {
		return empEmailID;
	}
	public void setEmpEmailID(String empEmailID) {
		this.empEmailID = empEmailID;
	}
	public String getEmpDateofJoining() {
		return empDateofJoining;
	}
	public void setEmpDateofJoining(String empDateofJoining) {
		this.empDateofJoining = empDateofJoining;
	}
	public String getEmpPhoneNo() {
		return empPhoneNo;
	}
	public void setEmpPhoneNo(String empPhoneNo) {
		this.empPhoneNo = empPhoneNo;
	}
	public String getEmpAddress() {
		return empAddress;
	}
	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getPreviousEmployeer() {
		return previousEmployeer;
	}
	public void setPreviousEmployeer(String previousEmployeer) {
		this.previousEmployeer = previousEmployeer;
	}
}
