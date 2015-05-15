package com.bbh.ets.reports;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.baaja.basis.service.BaseJdbcDao;

public class jasperDao extends BaseJdbcDao{

	
	public ArrayList<EmployeeBo> getEmplList() {
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		String sql="select * from ets_emp_sw";
		
		ArrayList<EmployeeBo> empList=new ArrayList<EmployeeBo>();
		try{
			conn=getDbConnection();
			stmt = conn.createStatement();
	        rs = stmt.executeQuery(sql);
	        while(rs.next()){
	        	EmployeeBo empBo=new EmployeeBo();
	        	empBo.setEmpId(rs.getInt("Emp_ID"));
	        	empBo.setEmpName(rs.getString("Emp_Name"));
	        	empBo.setDesignation(rs.getString("Designation"));
	        	empList.add(empBo);
	        }

		}
		catch(BaajaException e){
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return empList;
	
	}
	
	public void generateEmpReportData(EmployeeBo empBo,String to,String from) 

{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext(); 
		String filename = "reports/report1.jasper";
		String path = servletContext.getRealPath("/");
		String filePath=path+File.separator+filename;
		
		EMPTaskBo empB;
		ArrayList<EMPTaskBo> emList=new ArrayList<EMPTaskBo>();
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		
		
		String sql="select task_summary_id as task_id,(Select Emp_name from ets_emp_sw where emp_id=AssignedBy) AS AssignedBy,(Select Emp_name from ets_emp_sw where emp_id=ReviewedBy) AS ReviewedBy,date_assigned,date_completed,Remark,task_assigned from ets_task_summary  where emp_id="+empBo.getEmpId()+" and Date_assigned between '"+from+"' and '"+to+"' ;";
		
		
		HashMap<String, Object> parameterMap=new HashMap<String, Object>();
		try {
			conn=getDbConnection();
			stmt = conn.createStatement();
	        rs = stmt.executeQuery(sql);
	        
	        while(rs.next()){
	        	empB=new EMPTaskBo();
	        	empB.setTask_id(rs.getInt("task_id"));
	        	empB.setAssignedBy(rs.getString("AssignedBy"));
	        	empB.setReviewedBy(rs.getString("ReviewedBy"));
	        	//empB.setRemark(rs.getString("status"));
	        	empB.setDate_assigned((rs.getString("date_assigned")));
	        	empB.setDate_completed((rs.getString("date_completed")));
	        	empB.setRemark(rs.getString("Remark"));
	        	empB.setTask_assigned((rs.getString("task_assigned")));
	        	emList.add(empB);
	        }
	       
	       
	        parameterMap.put("from",from);
	        parameterMap.put("to",to);
	        parameterMap.put("empId",empBo.getEmpId());
	        parameterMap.put("name",empBo.getEmpName());
	       	parameterMap.put("designation", empBo.getDesignation());
	       	
	       	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	       	Date date = new Date();
	       
	        JasperPrint jasperPrint = JasperReportHelper.fillReportWithArrayBean(filePath, emList, parameterMap);
			HttpServletResponse response =(HttpServletResponse) facesContext.getExternalContext().getResponse();
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			response.setHeader("Content-disposition", "attachment; filename="+dateFormat.format(date)+"_"+empBo.getEmpName()+"_report"+".pdf");
			response.setContentType("application/pdf");
			ServletOutputStream sos = response.getOutputStream();
			sos.write(bytes,0,bytes.length);
			sos.flush();
			sos.close();
			facesContext.responseComplete();

	       /* byte[] bytes = JasperExportManager.exportReportToPdf(jp);	
	        HttpServletResponse response =(HttpServletResponse) facesContext.getExternalContext().getResponse();
	        response.addHeader("Content-disposition", "attachment; filename="+empBo.getEmpName()+"report.pdf");
			response.setContentType("application/pdf");
			ServletOutputStream sos = response.getOutputStream();
			sos.write(bytes,0,bytes.length);
			
			sos.flush();
			sos.close();
			facesContext.responseComplete();*/
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BaajaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			sqlCleanUp(conn, stmt, rs);
		}
	
} 
	public void generatetaskReport(String to,String from) {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext(); 
		String filename = "reports/report19.jasper";
		String path = servletContext.getRealPath("/");
		String filePath=path+File.separator+filename;
		HashMap<String, Object> parameterMap=new HashMap<String, Object>();
		ArrayList<EMPTaskBo> taskList=new ArrayList<EMPTaskBo>();
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		String sql="select (Select Emp_name from ets_emp_sw  where emp_id=ets.Emp_id) as emp_name,ets.task_summary_id as task_id,(Select Emp_name from ets_emp_sw where emp_id=ets.AssignedBy) AS AssignedBy,(Select Emp_name from ets_emp_sw where emp_id=ets.ReviewedBy) AS ReviewedBy,ets.date_assigned,ets.date_completed,ets.Remark,ets.task_assigned from ets_task_summary ets where ets.Date_assigned between '"+from+"' and '"+to+"' group by ets.emp_id;";
		
		
		
		
		try {
			
			conn=getDbConnection();
			stmt = conn.createStatement();
	        rs = stmt.executeQuery(sql);
	        EMPTaskBo taskBo=null;
	        
	        while(rs.next()){
	        	taskBo=new EMPTaskBo();
	    	  taskBo.setEmp_name(rs.getString("emp_name"));
	    	  taskBo.setTask_id(rs.getInt("task_id"));
	    	  taskBo.setAssignedBy(rs.getString("AssignedBy"));
	    	  taskBo.setReviewedBy(rs.getString("ReviewedBy"));
	        	//empB.setRemark(rs.getString("status"));
	    	  taskBo.setDate_assigned((rs.getString("date_assigned")));
	    	  taskBo.setDate_completed((rs.getString("date_completed")));
	    	  taskBo.setRemark(rs.getString("Remark"));
	    	  taskBo.setTask_assigned((rs.getString("task_assigned")));
	    	  taskList.add(taskBo);
	    	   
	    	   
	        }
	        
	        parameterMap.put("from",from);
	        parameterMap.put("to",to);
	     	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	       	Date date = new Date();
	       
	        JasperPrint jasperPrint = JasperReportHelper.fillReportWithArrayBean(filePath, taskList, parameterMap);
			HttpServletResponse response =(HttpServletResponse) facesContext.getExternalContext().getResponse();
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			response.setHeader("Content-disposition", "attachment; filename="+dateFormat.format(date)+"_Task"+"_report"+".pdf");
			response.setContentType("application/pdf");
			ServletOutputStream sos = response.getOutputStream();
			sos.write(bytes,0,bytes.length);
			sos.flush();
			sos.close();
			facesContext.responseComplete();

	        
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BaajaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			sqlCleanUp(conn, stmt, rs);
		}
	
	}

}
