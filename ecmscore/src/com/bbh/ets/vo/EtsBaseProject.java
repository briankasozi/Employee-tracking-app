package com.bbh.ets.vo;

import java.io.Serializable;

public class EtsBaseProject implements Serializable {

	private static final long serialVersionUID = -1997489898600564538L;
	
	private Integer projectId;
	private String projectName;
	private String description;
	
	
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

