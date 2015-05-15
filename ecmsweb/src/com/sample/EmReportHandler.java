package com.sample;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AjaxBehaviorEvent;

import net.sf.jasperreports.engine.JRException;

import com.bbh.ets.reports.EmployeeBo;
import com.bbh.ets.reports.jasperDao;
import com.bursys.ets.baaja.jsf.basis.BaseHandler;


public class EmReportHandler extends BaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String reportType="false";
	private ArrayList<EmployeeBo> empList;
	private Integer empId=0;
	private Date from;
	
	private String designation=" ";
	private Date to;
	jasperDao dao=new jasperDao();

	
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	                        
	public ArrayList<EmployeeBo> getEmpList() {
		empList=dao.getEmplList();
		return empList;
	}


	public void setEmpList(ArrayList<EmployeeBo> empList) {
		this.empList = empList;
	}
	
	public void generateAllReport() throws SQLException, JRException, IOException {
		
		
		if(reportType.equalsIgnoreCase("true")){
			generateEmployeeReport();	
		}
		else{
		generateTaskReport();
		}
		
	}
	
	
	public void generateEmployeeReport() {
		SimpleDateFormat sm=new SimpleDateFormat("yyyy-MM-dd");
		String t=sm.format(to);
		String fr=	sm.format(from);
		
		
		EmployeeBo empBo=new EmployeeBo();
		
		for(EmployeeBo em:empList){
			if(em.getEmpId()==empId){
				empBo=em;
				break;
			}
		}
		
			dao.generateEmpReportData(empBo,t,fr);
		
	}
public void generateTaskReport(){
	SimpleDateFormat sm=new SimpleDateFormat("yyyy-MM-dd");
	String t=sm.format(to);
	String fr=	sm.format(from);
	dao.generatetaskReport(t,fr);
	}





public void setDesignation(String designation) {
	this.designation = designation;
}
public String getDesignation() {
	
	EmployeeBo empBo=new EmployeeBo();
	if(empList==null){
		return designation;
	}
	
	for(EmployeeBo em:empList){
		if(em.getEmpId()==empId){
			empBo=em;
			break;
		}
	}
	designation=empBo.getDesignation();
	return designation;
}

public void changeEmployeeList(AjaxBehaviorEvent event) {

	HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
	Integer selectedEmpID = (Integer) choice.getValue();
	EmployeeBo empBo=new EmployeeBo();
	
	for(EmployeeBo em:empList){
		if(em.getEmpId()==selectedEmpID){
			empBo=em;
			break;
		}
	}
	designation=empBo.getDesignation();

}
	
public void changeReportType(AjaxBehaviorEvent event) {

	HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
	String selectedEmpID =  (String)choice.getValue();
	
	if(selectedEmpID.equalsIgnoreCase("true")){
		reportType="true";
	}else{
		reportType="false";
	}
	

}
public String getReportType() {
	return reportType;
}
public void setReportType(String reportType) {
	this.reportType = reportType;
}
}
