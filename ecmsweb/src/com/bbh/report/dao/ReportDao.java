package com.bbh.report.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bbh.ets.reports.JasperReportHelper;
import com.bbh.ets.reports.ReportData;

import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.baaja.basis.service.BaseJdbcDao;

/*public class ReportDao extends BaseJdbcDao {
	private static Log log = LogFactory.getLog(EcmsPartDao.class);
	
	private static ReportDao instance;
	
	public  static ReportDao getInstance(){
		if(instance == null){
			instance = new ReportDao();
		}
		return instance;
	}

	
	
	public void getreportData(Integer ecrNumber) throws BaajaException {
		
		String filename = "reports/ecmsReport.jasper";
		String subFileName ="reports/";
		//String subReportFileName = "reports/ecmsReport_subreport0.jasper";
		String imagename="images/WS3image.jpg";
		//String subReportFileName1 ="reports/ecmsReport_subreport1.jasper";
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext(); 


		
		String path = servletContext.getRealPath("/");
		String filePath=path+File.separator+filename;
		String imagepath=path+File.separator+imagename;
		String subreport = path;//+File.separator;//+subReportFileName;
		String subreport1 = path;//+File.separator;//+subReportFileName1;
		
		
		Connection conn = null ;
		Statement stmt = null ;
		Statement stmt1 = null ;
		ResultSet rs1 = null ;
		ResultSet rs = null ;
		Boolean check=true;
		String Sql="select eep.ecr_id,ep.assigned_part_number,ep.description,ep.current_revision,eprh.description" +
				" as doc from ecm_part ep left join ecm_ecr_part eep on ep.part_id = eep.part_id " +
				"left join ecm_part_revision_history eprh on eep.part_id = eprh.part_id where eep.ecr_id = "+ecrNumber 
				+"  group by assigned_part_number ";
		
		String Sql = "select eep.ecr_id,ep.assigned_part_number,ep.description,eep.part_rev,eprh.description"+
		" as doc1,eep.implications as doc from ecm_part ep left join ecm_ecr_part eep on ep.part_id = eep.part_id"+
		" left join ecm_part_revision_history eprh on eep.part_id = eprh.part_id where eep.ecr_id = "+ecrNumber+
		" group by assigned_part_number;";	
		try{
		conn = getDbConnection();
        stmt = conn.createStatement();
        rs1 = stmt.executeQuery(Sql);
      
        ArrayList<EngineeringChangeBo> engDataBoList = new ArrayList<EngineeringChangeBo>();
     //   ArrayList<EngineeringChangeBo> engDataBoList1 = new ArrayList<EngineeringChangeBo>();
        
        String Sql1="select assigned_part_number from ecm_part ";
    	ArrayList<Integer> ecmPartList = new ArrayList<Integer>();
    	conn = getDbConnection();
        stmt1 = conn.createStatement();
        rs = stmt1.executeQuery(Sql1);
  	  EngineeringChangeBo engbo1=null;
        while (rs.next())
        {
        	ecmPartList.add(rs.getInt("assigned_part_number"));
        //	engbo=new EngineeringChangeBo();
        	//engbo.setEcrN0List(ecmPartList);
        	//engDataBoList1.add(engbo);
        }
    	  EngineeringChangeBo engbo = new EngineeringChangeBo();
        ArrayList<PartDataBo> partDataBoList = new ArrayList<PartDataBo>();
        int i=1;
        while(rs1.next())
        {
        	
        	//  EngineeringChangeBo engbo = new EngineeringChangeBo();
        	  PartDataBo partDataBo = new PartDataBo();
        	  
        	  partDataBo.setDoc(rs1.getString("doc"));
        	  partDataBo.setEcoNo(rs1.getString("ecr_id"));
        	  partDataBo.setNo(i);
        
        	  partDataBo.setPartDescription(rs1.getString("description"));
        	  partDataBo.setPartNumber(rs1.getString("assigned_part_number"));
        	  partDataBo.setRevision(rs1.getString("part_rev"));
        	  partDataBoList.add(partDataBo);
        	
        	  i=i+1;
        	
        	
        	
        	//engbo.setEcrN0List(ecmPartList);
        //	engDataBoList.add(engbo);
        	
        
        	 }
        
        sqlCleanUp(conn, stmt1, rs);
       HashMap<String, Object> reportValues = new HashMap<String, Object>();
      // engDataBoList.addAll(engDataBoList1);
       
       
       
       String Sql1="select ep.assigned_part_number,ep.description from ecm_part ep left join ecm_ecr_part eep on  ep.part_id = eep.part_id left join ecm_ecr" +
       		 " ee on eep.ecr_id = ee.ecr_id where eep.ecr_id = "+ecrNumber 
       		+"  group by assigned_part_number ";
 //  List<String> ecmPartList = new ArrayList<String>();
   	conn = getDbConnection();
       stmt1 = conn.createStatement();
       rs = stmt1.executeQuery(Sql1);
       ArrayList<PartNumberBo> partNumBoList = new ArrayList<PartNumberBo>();
       int count=1;
       while(rs.next()){
    	   PartNumberBo partNoBo = new PartNumberBo();
    	   partNoBo.setPartName("("+count+")"+" "+rs.getString("assigned_part_number"));
    	   
    	   partNumBoList.add(partNoBo);
    	   count++;
    	 //  ecmPartList.add(rs.getString("assigned_part_number")+"/"+rs.getString("description")+"\n");
       }
       
    
       engbo.setPartNumberBo(partNumBoList);
       engbo.setPartDataBo(partDataBoList);
       engDataBoList.add(engbo);
	
  	 Sql="select ee.ecr_id ,ee.description,ee.long_desc,ea.change_time as created_date,ees.description as status, concat(ifnull(ebc.first_name,\" \"),\" \",ifnull(ebc.middle_name,\" \"),\" \",ifnull(ebc.last_name,\" \")) " +
  	 		"as created_by,ee.responsible_name as responsible_Name, eet.description as category from ecm_audit ea left join ecm_ecr ee on ea.doc_id = ee.ecr_id  left join ecm_ecr_status ees on ee.ecr_status_id = ees.ecr_status_id " +
  	 		"left join ecm_ecr_type eet on ee.ecr_type_id=eet.ecr_type_id " +
  	 		"left join ecm_user_login eul on ea.user_id = eul.user_id left join ecm_base_contact ebc on eul.contact_id = ebc.contact_id where ea.doc_id =  "+ ecrNumber +" and ea.doc_type_id=10 order by audit_id";
		
       
    
       
  	
  	 
  	 	conn = getDbConnection();
       stmt = conn.createStatement();
       rs1 = stmt.executeQuery(Sql);
       
       if(rs1.next())
       {
       
           SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
           String date=sdf.format(rs1.getDate("created_date"));
              reportValues.put("ecrNo", rs1.getString("ecr_id"));
              reportValues.put("createdDate",date);
              reportValues.put("createdBy", rs1.getString("created_by"));
              reportValues.put("state",rs1.getString("status") );
              reportValues.put("changeDescription",rs1.getString("category"));
            
              reportValues.put("descr", rs1.getString("description"));
              reportValues.put("info", rs1.getString("long_desc"));
              reportValues.put("imgDIR",imagepath);
              reportValues.put("responsibleName", rs1.getString("responsible_Name"));
              reportValues.put("SUBREPORT_FILE_NAME",subreport+"reports//ecmsReport_subreport0.jasper");
              reportValues.put("SUBREPORT_FILE_NAME1",subreport1+"reports//ecmsReport_subreport1.jasper");
             // reportValues.put("partList", "");
       }
       
			ReportData reportData = new ReportData();
			JasperReportHelper reportHelper = new JasperReportHelper();
			
	
			
		
			JasperPrint jasperPrint = JasperReportHelper.fillReportWithArrayBean(filePath, engDataBoList, reportValues);
			HttpServletResponse response =(HttpServletResponse) facesContext.getExternalContext().getResponse();
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			response.setHeader("Content-disposition", "attachment; filename=W"+ecrNumber+".pdf");
			ServletOutputStream sos = response.getOutputStream();
			sos.write(bytes);
			response.setContentType("application/pdf");
			sos.flush();
	
			facesContext.responseComplete();

		}
        
        
			catch (Exception e) {
				e.printStackTrace();
		log.info(e.getMessage());
				// TODO: handle exception
			}
			
			
			finally {
				sqlCleanUp(null, stmt1, rs);
				sqlCleanUp(conn, stmt, rs);
			}
	}
	
	public void generateConsistOfPdf(List<ConsitOfPartBo> consitOfPartData, String partNo, String description) throws IOException, JRException
	{

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext(); 


		
		String filename = "reports/ecmsPartRevisonReport.jasper";
		String imagename="images/WS3image.jpg";
		
		String path = servletContext.getRealPath("/");
		String filePath=path+File.separator+filename;
		String imagepath=path+File.separator+imagename;
	
		HashMap<String, Object> reportValues = new HashMap<String, Object>();

              reportValues.put("parentPartNo", partNo);
              reportValues.put("parentPartDesc",description);
            reportValues.put("imgDIR",imagepath);
            ReportData reportData = new ReportData();
			JasperReportHelper reportHelper = new JasperReportHelper();
			JasperPrint jasperPrint = JasperReportHelper.fillReportWithArrayBean(filePath, consitOfPartData, reportValues);
			HttpServletResponse response =(HttpServletResponse) facesContext.getExternalContext().getResponse();
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			response.setHeader("Content-disposition", "attachment; filename="+partNo+".pdf");
			ServletOutputStream sos = response.getOutputStream();
			sos.write(bytes);
			response.setContentType("application/pdf");
			sos.flush();
	
			facesContext.responseComplete();

		}
	

	*/
	

