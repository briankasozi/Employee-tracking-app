package com.menuHandler;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bbh.ets.bo.UserBo;
import com.bbh.ets.dao.EtsServiceDao;

public class menuHandler {
	EtsServiceDao etsServiceDao=new EtsServiceDao();
	private String loggedUser;
	private String rollDesc;
	Boolean disabled=false;
	
	UserBo userBo=new UserBo();
	
	public menuHandler(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(true);
		if(session.getAttribute("loggedEtsUser") !=null ){
		userBo = ((UserBo) session.getAttribute("loggedEtsUser"));
		loggedUser=userBo.getUserName();
		rollDesc=etsServiceDao.getRoleDescription(loggedUser);
		}
		if(rollDesc.equalsIgnoreCase("Administrator")){
			disabled=false;
		}else{
			disabled=true;
		}
		
	}
	
	
	public String viewHomePage() {
		//requestedPage="home";
		return "ets_home";
	}
	public String viewTaskList(){
		return "taskList";
		
	}
	public String viewReports(){
		return "viewReports";
	}
	
	public String addEmp(){
		return"add_employee";
	}
	public String employeeList(){
		return"employee_list";
	}
	public String users(){
		return "user_list";
	}
	
	public String projectHomePage(){
		return "projectHome";
	}
	
	
	
	public Boolean disableMenu(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(true);
		if(session.getAttribute("loggedEtsUser") !=null ){
		userBo = ((UserBo) session.getAttribute("loggedEtsUser"));
		loggedUser=userBo.getUserName();
		rollDesc=etsServiceDao.getRoleDescription(loggedUser);
		}
		if(rollDesc.equalsIgnoreCase("Administrator")){
			disabled=true;
		}
		return disabled;
		}
	

public Boolean getDisabled() {
	return disabled;
}

public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
}
}