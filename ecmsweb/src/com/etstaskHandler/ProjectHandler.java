package com.etstaskHandler;

import java.io.Serializable;
import java.util.ArrayList;

import com.bbh.ets.dao.EmployeeTaskDao;
import com.bbh.ets.vo.EtsBaseProject;
import com.bursys.ets.baaja.jsf.basis.BaseHandler;

public class ProjectHandler extends BaseHandler  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private EtsBaseProject etsBaseProject=new EtsBaseProject();
	ArrayList<EtsBaseProject> projectList;
	EmployeeTaskDao dao=new EmployeeTaskDao();

	
	public String saveProject(){
		dao.saveProject(etsBaseProject);
		return "projectHome";
	}
	
	public String editProject(EtsBaseProject etsBaseProjec){
		this.etsBaseProject=etsBaseProjec;
		return "project";
	}
	
	public String projectPage(){
		return "project";
	}
	
	public String projectHomePage(){
		return "projectHome";
	}
	
	
	public EtsBaseProject getEtsBaseProject() {
		return etsBaseProject;
	}

	public void setEtsBaseProject(EtsBaseProject etsBaseProject) {
		this.etsBaseProject = etsBaseProject;
	}
	
	

	public ArrayList<EtsBaseProject> getProjectList() {
		if(projectList==null){
			projectList=dao.getEtsBaseProjectList();
		}
		
		return projectList;
	}

	public void setProjectList(ArrayList<EtsBaseProject> projectList) {
		this.projectList = projectList;
	}
}
