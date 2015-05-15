package com.bbh.ets.vo;

import java.io.Serializable;

public class TaskVo implements Serializable {
	

	private static final long serialVersionUID = -5194337631524953559L;
	private Integer index;
	private Integer taskSummaryID;
	private Integer employeeID;
	private String employeeName;
	private Integer assignedByEmployeeID;
	private String assignedByemployeeName;
	private Integer reviewedByEmployeeID;
	private String reviewedByemployeeName;
	private String task;
	private String taskDetail;
	private String status;
	private Integer statusID;
	private String remarks;
	private String taskCreationDate;
	private String dateCompleted;
	private Integer projectID;
	private String 	projectName;
	private String 	moduleName;
	private Integer priorityId;
	private String  priority;
	private String taskStartDate;
	private String taskCompletionExpectedDate;
	private String taskCompletionExpectedHours;
	private String taskCompletionDate;
	private Integer counter;
	private String  developerRemaraks;
	private String assignerRemaraks;
	private String reviewerRemaraks;
	private Integer departmentID;
	private String docUpdated;
	private Boolean assignedDocAttached;
	private Boolean reviewedDocAttached;
	private Boolean developerDocAttached;
	
	private String assignedDocName;
	private String reviewedDocName;
	private String developerDocName;
	private Boolean expectedDateCoressed;
	public Integer getTaskSummaryID() {
		return taskSummaryID;
	}
	public void setTaskSummaryID(Integer taskSummaryID) {
		this.taskSummaryID = taskSummaryID;
	}
	public Integer getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Integer getAssignedByEmployeeID() {
		return assignedByEmployeeID;
	}
	public void setAssignedByEmployeeID(Integer assignedByEmployeeID) {
		this.assignedByEmployeeID = assignedByEmployeeID;
	}
	public String getAssignedByemployeeName() {
		return assignedByemployeeName;
	}
	public void setAssignedByemployeeName(String assignedByemployeeName) {
		this.assignedByemployeeName = assignedByemployeeName;
	}
	public Integer getReviewedByEmployeeID() {
		return reviewedByEmployeeID;
	}
	public void setReviewedByEmployeeID(Integer reviewedByEmployeeID) {
		this.reviewedByEmployeeID = reviewedByEmployeeID;
	}
	public String getReviewedByemployeeName() {
		return reviewedByemployeeName;
	}
	public void setReviewedByemployeeName(String reviewedByemployeeName) {
		this.reviewedByemployeeName = reviewedByemployeeName;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getTaskDetail() {
		return taskDetail;
	}
	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getStatusID() {
		return statusID;
	}
	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getTaskCreationDate() {
		return taskCreationDate;
	}
	public void setTaskCreationDate(String taskCreationDate) {
		this.taskCreationDate = taskCreationDate;
	}
	public String getDateCompleted() {
		return dateCompleted;
	}
	public void setDateCompleted(String dateCompleted) {
		this.dateCompleted = dateCompleted;
	}
	public Integer getProjectID() {
		return projectID;
	}
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getTaskStartDate() {
		return taskStartDate;
	}
	public void setTaskStartDate(String taskStartDate) {
		this.taskStartDate = taskStartDate;
	}
	public String getTaskCompletionExpectedDate() {
		return taskCompletionExpectedDate;
	}
	public void setTaskCompletionExpectedDate(String taskCompletionExpectedDate) {
		this.taskCompletionExpectedDate = taskCompletionExpectedDate;
	}
	public String getTaskCompletionExpectedHours() {
		return taskCompletionExpectedHours;
	}
	public void setTaskCompletionExpectedHours(String taskCompletionExpectedHours) {
		this.taskCompletionExpectedHours = taskCompletionExpectedHours;
	}
	public String getTaskCompletionDate() {
		return taskCompletionDate;
	}
	public void setTaskCompletionDate(String taskCompletionDate) {
		this.taskCompletionDate = taskCompletionDate;
	}
	public Integer getCounter() {
		return counter;
	}
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	public String getDeveloperRemaraks() {
		return developerRemaraks;
	}
	public void setDeveloperRemaraks(String developerRemaraks) {
		this.developerRemaraks = developerRemaraks;
	}
	public String getAssignerRemaraks() {
		return assignerRemaraks;
	}
	public void setAssignerRemaraks(String assignerRemaraks) {
		this.assignerRemaraks = assignerRemaraks;
	}
	public String getReviewerRemaraks() {
		return reviewerRemaraks;
	}
	public void setReviewerRemaraks(String reviewerRemaraks) {
		this.reviewerRemaraks = reviewerRemaraks;
	}
	public Integer getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}
	public String getDocUpdated() {
		return docUpdated;
	}
	public void setDocUpdated(String docUpdated) {
		this.docUpdated = docUpdated;
	}
	public Boolean getAssignedDocAttached() {
		return assignedDocAttached;
	}
	public void setAssignedDocAttached(Boolean assignedDocAttached) {
		this.assignedDocAttached = assignedDocAttached;
	}
	public Boolean getReviewedDocAttached() {
		return reviewedDocAttached;
	}
	public void setReviewedDocAttached(Boolean reviewedDocAttached) {
		this.reviewedDocAttached = reviewedDocAttached;
	}
	public Boolean getDeveloperDocAttached() {
		return developerDocAttached;
	}
	public void setDeveloperDocAttached(Boolean developerDocAttached) {
		this.developerDocAttached = developerDocAttached;
	}
	public String getAssignedDocName() {
		return assignedDocName;
	}
	public void setAssignedDocName(String assignedDocName) {
		this.assignedDocName = assignedDocName;
	}
	public String getReviewedDocName() {
		return reviewedDocName;
	}
	public void setReviewedDocName(String reviewedDocName) {
		this.reviewedDocName = reviewedDocName;
	}
	public String getDeveloperDocName() {
		return developerDocName;
	}
	public void setDeveloperDocName(String developerDocName) {
		this.developerDocName = developerDocName;
	}
	public Boolean getExpectedDateCoressed() {
		return expectedDateCoressed;
	}
	public void setExpectedDateCoressed(Boolean expectedDateCoressed) {
		this.expectedDateCoressed = expectedDateCoressed;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	
}

