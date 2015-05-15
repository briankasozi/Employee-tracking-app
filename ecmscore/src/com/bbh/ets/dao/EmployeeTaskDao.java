package com.bbh.ets.dao;




import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.bbh.ets.vo.EtsBaseProject;
import com.bbh.ets.vo.RemarksVo;
import com.bbh.ets.vo.TaskVo;
import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.baaja.basis.service.BaseJdbcDao;
import com.bursys.baaja.basis.utils.Validator;
 /**********************
 **********************
 **********************
 @author JosephBrian
 **********************/
 
public class EmployeeTaskDao extends BaseJdbcDao {
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	public HashMap<Integer, String> getEmployeeList(Integer departmentId) {
		HashMap<Integer, String> employeeList = new HashMap<Integer, String>();
		Connection conn = null;
		String query;
		
		if(departmentId==null){
			query = "SELECT * FROM ets_emp_sw ";
		}else{
			query = "SELECT * FROM ets_emp_sw " +
				" where Emp_ID in  (select emp_id from  ets_emp_sw_department where department_id="+departmentId+")";
		}
				
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				employeeList.put(rs.getInt("Emp_ID"),rs.getString("Emp_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return employeeList;

	}
	
	public String getDesignation(Integer empID) {
		String designation="";
		Connection conn = null;
		String query = "SELECT * FROM ets_emp_sw where Emp_ID="+empID;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				designation=rs.getString("Designation");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return designation;

	}
	
	
	
	public HashMap<Integer, String> getTaskStatusList() {
		HashMap<Integer, String> taskList = new HashMap<Integer, String>();
		Connection conn = null;
		String query = "SELECT * FROM ets_task_status";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				taskList.put(rs.getInt("Task_ID"),
						rs.getString("Description"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return taskList;

	}
	
	public Boolean saveTaskSummary(Integer employeeID,Integer reviewedByEmployeeID,Integer assignedByEmployeeID,
									String task,String taskDetail,File file,Integer projectID,String moduleName,
									Date taskStartDate,Date taskExpected,Integer Priority,Integer departmentID){
		Connection conn = null;
		Boolean result=false;
		InputStream inputStream=null;
		String ets_task_summary_INSERT_SQL=null;
		
		String assignedDocsFileName=null;
		
		if(file!=null){
			assignedDocsFileName=file.getName();
			if(assignedDocsFileName.length()>35){
				assignedDocsFileName=assignedDocsFileName.substring(assignedDocsFileName.length()-35, assignedDocsFileName.length());
			}
			
		ets_task_summary_INSERT_SQL = "INSERT INTO ets_task_summary" +
				"(" +
				"Emp_ID,Task_ID, AssignedBy,ReviewedBy, Task_Assigned," +
				" Task_Creation_Date, Assigned_Doc,Task_Detail,Assigned_Doc_Name,IsDeleted,Project_ID,Module_Name," +
				"Task_Start_Date,Expected_Completion_Date,Task_Priority,DEPARTMENT_ID" +
				")VALUES("+
				employeeID+",5,"+assignedByEmployeeID+"," +
				reviewedByEmployeeID+","+
				"'"+task+"'," +
				"now(),?,'"+taskDetail+"',?,0,"+projectID+",'"+moduleName+"','"+format.format(taskStartDate)+"','"+format.format(taskExpected)+"',"+Priority+","+departmentID+")"; 
		}else{
			ets_task_summary_INSERT_SQL = "INSERT INTO ets_task_summary" +
			"(" +
			"Emp_ID,Task_ID, AssignedBy,ReviewedBy, Task_Assigned," +
			" Task_Creation_Date,Task_Detail,IsDeleted,Project_ID,Module_Name," +
			"Task_Start_Date, Expected_Completion_Date, Task_Priority,DEPARTMENT_ID" +
			")VALUES("+
			employeeID+",5,"+assignedByEmployeeID+"," +
			reviewedByEmployeeID+","+
			"'"+task+"'," +
			"now(),'"+taskDetail+"',0,"+projectID+",'"+moduleName+"','"+format.format(taskStartDate)+"','"+format.format(taskExpected)+"',"+Priority+","+departmentID+")"; 
		}
		PreparedStatement pstmt = null;
		
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(ets_task_summary_INSERT_SQL);
			
			if(file!=null){
				inputStream=new FileInputStream(file);
				BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
				pstmt.setBinaryStream(1, bufferedInputStream,(int)file.length());
				pstmt.setString(2,"Assigned_"+assignedDocsFileName);
			}
			pstmt.executeUpdate();
			
			insertNotifyData(employeeID);
			result=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, null);
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
public ArrayList<TaskVo> getTaskList(Integer taskSummaryID,Date fromDate,Date toDate,Date taskCompletionExpectedDate,Integer employeeID,Integer assignedByEmployeeID,Integer reviewedByEmployeeID,Integer projectID, Integer loginEmpID,String roleName,Integer statusID,Boolean searchStage) {
		
		ArrayList<TaskVo> taskList = new ArrayList<TaskVo>();
		
		if(loginEmpID==null){  // If Login Employee ID Not Found
			return taskList;
		}
		
		TaskVo taskVo=null;
		Connection conn = null;
		
		String selectQuery="SELECT Task_Summary_ID, Emp_ID, Task_ID, AssignedBy, ReviewedBy, Task_Assigned, Task_Detail, "
							+ "Remark, Task_Creation_Date, Date_Completed, Assigned_Doc_Name, Reviewed_Doc_Name, IsDeleted, "
							+ "Developer_Remark, Project_ID, Module_Name, Task_Start_Date, Expected_Completion_Date, "
							+ "Expected_Completion_Hours, Task_Priority, Assigner_Remarks, Reviewer_Remarks, "
							+ "Developer_Doc_Name, counter, Manual_Doc_Updated, DEPARTMENT_ID FROM ets_task_summary " +
							"where IsDeleted=0 " +
							"and DEPARTMENT_ID in (select DEPARTMENT_ID from ets_emp_sw_department where Emp_ID="+loginEmpID+") ";  
		
		SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
		

		if(taskSummaryID!=null){
			selectQuery = selectQuery+
			" and Task_Summary_ID="+taskSummaryID;
		}
		
		
		if(searchStage){
			
			
			if(fromDate!=null && toDate!=null){
				selectQuery = selectQuery+
				" and Task_Creation_Date>= '" +DateFormat.format(fromDate)+" 00:00:00' "+
				" and Task_Creation_Date<= '" +DateFormat.format(toDate)+" 23:00:59' ";
			}
			
			if(taskCompletionExpectedDate!=null){
				selectQuery = selectQuery+
				" and Expected_Completion_Date<= '" +DateFormat.format(taskCompletionExpectedDate)+" 23:00:59' ";
			}
			
				
			if(projectID!=null && projectID!=0){
				selectQuery = selectQuery+
				" and Project_ID="+projectID;
			}
			if(statusID!=null && statusID!=0){
				selectQuery = selectQuery+
				" and Task_ID="+statusID;
			}
			
			if(employeeID!=null && employeeID!=0){
				selectQuery = selectQuery+
				" And Emp_ID="+employeeID;
			}
			
			if(assignedByEmployeeID!=null && assignedByEmployeeID!=0){
				selectQuery = selectQuery+
				" And AssignedBy="+assignedByEmployeeID;
			}
			
			if(reviewedByEmployeeID!=null && reviewedByEmployeeID!=0){
				selectQuery = selectQuery+
				" And ReviewedBy="+reviewedByEmployeeID;
			}
		}
		
		if(roleName !=null && !roleName.equalsIgnoreCase("Administrator")){
			selectQuery = selectQuery+
			" And ( Emp_ID="+loginEmpID+" or ReviewedBy="+loginEmpID+" or AssignedBy="+loginEmpID+") ";
			
		}
		
		HashMap<Integer, String> employeeList=getEmployeeList(null);
		HashMap<Integer, String> taskStatusList=getTaskStatusList();
		HashMap<Integer, String> projectList=getProjectList();
		HashMap<Integer, String> PrioritytList=getTaskPriorityList();
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(selectQuery+" order by  Task_Creation_Date desc");
			rs = pstmt.executeQuery();
			int index=0;
			while (rs.next()) {
				taskVo=new TaskVo();
				
				taskVo.setIndex(index++);
				taskVo.setTaskSummaryID(rs.getInt("Task_Summary_ID"));
				taskVo.setEmployeeID(rs.getInt("Emp_ID"));
				taskVo.setEmployeeName(employeeList.get(taskVo.getEmployeeID()));
				taskVo.setStatusID(rs.getInt("Task_ID"));
				taskVo.setStatus(taskStatusList.get(taskVo.getStatusID()));
				taskVo.setAssignedByEmployeeID(rs.getInt("AssignedBy"));
				taskVo.setAssignedByemployeeName(employeeList.get(taskVo.getAssignedByEmployeeID()));
				taskVo.setReviewedByEmployeeID(rs.getInt("ReviewedBy"));
				taskVo.setReviewedByemployeeName(employeeList.get(taskVo.getReviewedByEmployeeID()));
				taskVo.setTask(rs.getString("Task_Assigned"));
				taskVo.setRemarks(rs.getString("Remark"));
				
				taskVo.setDateCompleted(rs.getString("Date_Completed"));
				taskVo.setTaskDetail(rs.getString("Task_Detail"));
				taskVo.setProjectID(rs.getInt("Project_ID"));
				taskVo.setProjectName(projectList.get(taskVo.getProjectID()));
				taskVo.setModuleName(rs.getString("Module_Name"));
				taskVo.setPriorityId(rs.getInt("Task_Priority"));
				taskVo.setPriority(PrioritytList.get(taskVo.getPriorityId()));
				
				taskVo.setTaskCompletionExpectedDate(rs.getString("Expected_Completion_Date"));
				
				taskVo.setDeveloperRemaraks(rs.getString("Developer_Remark"));
				taskVo.setAssignerRemaraks(rs.getString("Assigner_Remarks"));
				taskVo.setReviewerRemaraks(rs.getString("Reviewer_Remarks"));
				
	
				try{
					if(rs.getString("Task_Start_Date")!=null)
					taskVo.setTaskStartDate(format.format(format.parse(rs.getString("Task_Start_Date"))));
				}catch(ParseException e){
					taskVo.setTaskStartDate(rs.getString("Task_Start_Date"));
				}
				
				try{
					if(rs.getString("Date_Completed")!=null)
					taskVo.setTaskCompletionDate(format.format(format.parse(rs.getString("Date_Completed"))));
				}catch(ParseException e){
					taskVo.setTaskCompletionDate(rs.getString("Date_Completed"));
				}
				try{
					if(rs.getString("Task_Creation_Date")!=null)
					taskVo.setTaskCreationDate(DateFormat.format(DateFormat.parse(rs.getString("Task_Creation_Date"))));
				}catch(ParseException e){
					taskVo.setTaskCreationDate(rs.getString("Task_Creation_Date"));
				}
				
				taskVo.setCounter(rs.getInt("Counter"));
				taskVo.setDocUpdated(rs.getString("Manual_Doc_Updated"));
				taskVo.setDepartmentID(rs.getInt("DEPARTMENT_ID"));
				
				if(rs.getString("Assigned_Doc_Name")!=null){
					taskVo.setAssignedDocAttached(true);
					taskVo.setAssignedDocName(rs.getString("Assigned_Doc_Name"));
					
				}
				if(rs.getString("Reviewed_Doc_Name")!=null){
					taskVo.setReviewedDocAttached(true);
					taskVo.setReviewedDocName(rs.getString("Reviewed_Doc_Name"));
				}
				
				if(rs.getString("Developer_Doc_Name")!=null){
					taskVo.setDeveloperDocAttached(true);
					taskVo.setDeveloperDocName(rs.getString("Developer_Doc_Name"));
				}
				
				try{
					if(taskVo.getTaskCompletionExpectedDate()!=null && taskVo.getTaskCompletionDate()!=null ){
						if(DateFormat.parse(taskVo.getTaskCompletionExpectedDate()).getTime() > DateFormat.parse(taskVo.getTaskCompletionDate()).getTime() && taskVo.getStatusID()!=3){
							taskVo.setExpectedDateCoressed(true);
						}else{
							taskVo.setExpectedDateCoressed(false);
						}
					}
					
				}catch(Exception e){
					
				}
				
				taskList.add(taskVo);
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return taskList;

	}

	public void deleteTaskSummary(Integer taskID) {
		Connection conn = null;
		String deleteQuery = "update ets_task_summary set IsDeleted=1 where Task_Summary_ID="+taskID;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt=conn.prepareStatement(deleteQuery);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, null);
		}
	}

	public File getZipDocs(Integer taskID) {

		Connection conn = null;
		Blob assigned_Doc=null,reviewed_Doc=null,developer_Doc=null;
		File zipFile=null;
		ZipEntry zipEntry=null;
		ZipOutputStream zipOutputStream=null;
		FileOutputStream fileOutputStream=null;
		String selectQuery = "select  Assigned_Doc, Assigned_Doc_Name," +
				" Reviewed_Doc, Reviewed_Doc_Name, " +
				"Developer_Docs,Developer_Doc_Name " +
				"from ets_task_summary where Task_Summary_ID="+taskID;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		
		try {
			conn = getDbConnection();
			pstmt=conn.prepareStatement(selectQuery);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				assigned_Doc = rs.getBlob("Assigned_Doc");
				reviewed_Doc = rs.getBlob("Reviewed_Doc");
				developer_Doc=rs.getBlob("Developer_Docs");
				if(assigned_Doc!=null){
					
					zipFile = new File(format.format(new Date())+"_ETS.zip");
					fileOutputStream=new FileOutputStream(zipFile);
					zipOutputStream = new ZipOutputStream(fileOutputStream);
					
					zipEntry = new ZipEntry(rs.getString("Assigned_Doc_Name"));
					
					zipOutputStream.putNextEntry(zipEntry);
					zipOutputStream.write(assigned_Doc.getBytes(1, (int) assigned_Doc.length()), 0, (int) assigned_Doc.length());
					
				}
				
				if(reviewed_Doc!=null){
					
					
					if(zipFile==null){
						zipFile = new File(format.format(new Date())+"_ETS.zip");
						fileOutputStream=new FileOutputStream(zipFile);
						zipOutputStream = new ZipOutputStream(fileOutputStream);
						
					}
					
					zipEntry = new ZipEntry(rs.getString("Reviewed_Doc_Name"));
					
					zipOutputStream.putNextEntry(zipEntry);
					zipOutputStream.write(reviewed_Doc.getBytes(1, (int) reviewed_Doc.length()), 0, (int) reviewed_Doc.length());
				}
				
				if(developer_Doc!=null){
					
					
					if(zipFile==null){
						zipFile = new File(format.format(new Date())+"_ETS.zip");
						fileOutputStream=new FileOutputStream(zipFile);
						zipOutputStream = new ZipOutputStream(fileOutputStream);
						
					}
					
					zipEntry = new ZipEntry(rs.getString("Developer_Doc_Name"));
					
					zipOutputStream.putNextEntry(zipEntry);
					zipOutputStream.write(developer_Doc.getBytes(1, (int) developer_Doc.length()), 0, (int) developer_Doc.length());
				}
				
				
				zipOutputStream.closeEntry();
				zipOutputStream.close();
				fileOutputStream.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, null);
		}
	
		return zipFile;
	}

	/**
	 *  Update Task 
	 * @param Task_Summary_ID
	 * @param employeeID
	 * @param statusID
	 * @param assignedByEmployeeID
	 * @param reviewedByEmployeeID
	 * @param task
	 * @param taskDetail
	 * @param assignedDocsFile
	 * @param reviewedDocsFile
	 * @param projectID
	 * @param moduleName
	 * @param CompletionTime
	 * @param assignerRemaraks
	 * @param reviewerRemaraks
	 * @param developerRemaraks
	 * @param developerDocsFile
	 * @param taskCompletionExpectedDate 
	 * @return 
	 */
	public boolean updateTaskSummary(Integer Task_Summary_ID,Integer employeeID, Integer statusID,
			Integer assignedByEmployeeID, Integer reviewedByEmployeeID,
			String task, String taskDetail, File assignedDocsFile,File reviewedDocsFile,
			Integer projectID,String moduleName,Date CompletionTime,
			String assignerRemaraks,String reviewerRemaraks,String developerRemaraks,File developerDocsFile,String docUpdated
			,Date taskStartDate,Date taskCompletionExpectedDate
			) {
		Connection conn = null;
		Boolean result=false;
		ResultSet rs=null;
		InputStream inputStream1=null,inputStream2=null,inputStream3=null;
		BufferedInputStream bufferedInputStream1=null,bufferedInputStream2=null,bufferedInputStream3=null;
		
		String selectQueryForLastStatusID="select Task_ID from ETS_TASK_SUMMARY WHERE Task_Summary_ID="+Task_Summary_ID;
		Integer oldStatusID=null;
		
		String ets_task_summary_INSERT_SQL = "UPDATE ETS_TASK_SUMMARY SET " +
					"Emp_ID="+employeeID+",Task_ID="+statusID+", AssignedBy="+assignedByEmployeeID+"," +
					" ReviewedBy="+reviewedByEmployeeID+", Task_Assigned='"+task+"'," +
					"Project_ID="+projectID+",Module_Name='"+moduleName+"',"+
					"Developer_remark='"+developerRemaraks+"' "+
					", Assigner_remarks='"+assignerRemaraks+"' "+
					",Reviewer_Remarks='"+reviewerRemaraks+"' "+
					",Manual_Doc_Updated='"+docUpdated+"' "+
					",Task_Detail='"+taskDetail+"' " +
					",Task_Start_Date='"+format.format(taskStartDate)+"' " +
					",Expected_Completion_Date='"+format.format(taskCompletionExpectedDate)+"'" ;
		String assignedDocsFileName=null;
		String reviewedDocsFileName=null;
		String developerDocsFileName=null;
		
		if(assignedDocsFile!=null){
			assignedDocsFileName=assignedDocsFile.getName();
			if(assignedDocsFileName.length()>35){
				assignedDocsFileName=assignedDocsFileName.substring(assignedDocsFileName.length()-35, assignedDocsFileName.length());
			}
		}
		
		if(reviewedDocsFile!=null){
			reviewedDocsFileName=reviewedDocsFile.getName();
			if(reviewedDocsFileName.length()>35){
				reviewedDocsFileName=reviewedDocsFileName.substring(reviewedDocsFileName.length()-35, reviewedDocsFileName.length());
			}
		}
		
		if(developerDocsFile!=null){
			developerDocsFileName=developerDocsFile.getName();
			if(developerDocsFileName.length()>35){
				developerDocsFileName=developerDocsFileName.substring(developerDocsFileName.length()-35, developerDocsFileName.length());
			}
		}
		
		
				
		if(assignedDocsFile!=null && reviewedDocsFile!=null && developerDocsFile!=null){
		ets_task_summary_INSERT_SQL = ets_task_summary_INSERT_SQL+
				", Assigned_Doc=?," +
				"Assigned_Doc_Name=? ," +
				" Reviewed_Doc=?," +
				"Reviewed_Doc_Name=?, "+
				" Developer_Docs=?," +
				"Developer_Doc_Name=? ";
		}else if(assignedDocsFile!=null && reviewedDocsFile!=null ){
			ets_task_summary_INSERT_SQL = ets_task_summary_INSERT_SQL+
			", Assigned_Doc=?," +
			"Assigned_Doc_Name=? ," +
			" Reviewed_Doc=?," +
			"Reviewed_Doc_Name=? ";
		}else if(assignedDocsFile!=null  && developerDocsFile!=null){
			ets_task_summary_INSERT_SQL = ets_task_summary_INSERT_SQL+
			", Assigned_Doc=?," +
			"Assigned_Doc_Name=? ," +
			" Developer_Docs=?," +
			"Developer_Doc_Name=? ";
			
		}else if(reviewedDocsFile!=null && developerDocsFile!=null){
			ets_task_summary_INSERT_SQL = ets_task_summary_INSERT_SQL+
			", Reviewed_Doc=?," +
			"Reviewed_Doc_Name=?, "+
			" Developer_Docs=?," +
			"Developer_Doc_Name=? ";
			
		}else if(assignedDocsFile!=null){
			ets_task_summary_INSERT_SQL = ets_task_summary_INSERT_SQL+
			", Assigned_Doc=?," +
			"Assigned_Doc_Name=? " ;
		}else if( reviewedDocsFile!=null ){
			ets_task_summary_INSERT_SQL = ets_task_summary_INSERT_SQL+
			", Reviewed_Doc=?," +
			"Reviewed_Doc_Name=? ";
			
		}else if(developerDocsFile!=null){
			ets_task_summary_INSERT_SQL = ets_task_summary_INSERT_SQL+
			", Developer_Docs=?," +
			"Developer_Doc_Name=? ";
			
		}
		
		
		
		if(CompletionTime!=null){
			ets_task_summary_INSERT_SQL=ets_task_summary_INSERT_SQL+" ,Date_Completed='"+format.format(CompletionTime)+"' ";
		}
		
		
		PreparedStatement pstmt = null;
		
		try {
			conn = getDbConnection();
			//-----for last status id
			pstmt = conn.prepareStatement(selectQueryForLastStatusID);
			rs=pstmt.executeQuery();
			while(rs.next()){
				oldStatusID=rs.getInt("Task_ID");
			}
			
			
			//------
			
			pstmt = conn.prepareStatement(ets_task_summary_INSERT_SQL+" WHERE Task_Summary_ID="+Task_Summary_ID);
			
		
		
			//-----------
				if(assignedDocsFile!=null && reviewedDocsFile!=null && developerDocsFile!=null){
			
					inputStream1=new FileInputStream(assignedDocsFile);
					bufferedInputStream1=new BufferedInputStream(inputStream1);
					pstmt.setBinaryStream(1, bufferedInputStream1,(int)assignedDocsFile.length());
					pstmt.setString(2,"Assigned_Doc_"+assignedDocsFileName);
					
					inputStream2=new FileInputStream(reviewedDocsFile);
					bufferedInputStream2=new BufferedInputStream(inputStream2);
					pstmt.setBinaryStream(3, bufferedInputStream2,(int)reviewedDocsFile.length());
					pstmt.setString(4,"Reviewed_Doc_"+reviewedDocsFileName);
					
					inputStream3=new FileInputStream(developerDocsFile);
					bufferedInputStream3=new BufferedInputStream(inputStream3);
					pstmt.setBinaryStream(5, bufferedInputStream3,(int)developerDocsFile.length());
					pstmt.setString(6,"Assignee_Doc_"+developerDocsFileName);
					
					
					
					}else if(assignedDocsFile!=null && reviewedDocsFile!=null ){
			
						inputStream1=new FileInputStream(assignedDocsFile);
						bufferedInputStream1=new BufferedInputStream(inputStream1);
						pstmt.setBinaryStream(1, bufferedInputStream1,(int)assignedDocsFile.length());
						pstmt.setString(2,"Assigned_Doc_"+assignedDocsFileName);
						
						inputStream2=new FileInputStream(reviewedDocsFile);
						bufferedInputStream2=new BufferedInputStream(inputStream2);
						pstmt.setBinaryStream(3, bufferedInputStream2,(int)reviewedDocsFile.length());
						pstmt.setString(4,"Reviewed_Doc_"+reviewedDocsFileName);
					}else if(assignedDocsFile!=null  && developerDocsFile!=null){
									
						inputStream1=new FileInputStream(assignedDocsFile);
						bufferedInputStream1=new BufferedInputStream(inputStream1);
						pstmt.setBinaryStream(1, bufferedInputStream1,(int)assignedDocsFile.length());
						pstmt.setString(2,"Assigned_Doc_"+assignedDocsFile.getName());
						
						inputStream3=new FileInputStream(developerDocsFile);
						bufferedInputStream3=new BufferedInputStream(inputStream3);
						pstmt.setBinaryStream(3, bufferedInputStream3,(int)developerDocsFile.length());
						pstmt.setString(4,"Assignee_Doc_"+developerDocsFileName);
						
					}else if(reviewedDocsFile!=null && developerDocsFile!=null){
					
						inputStream2=new FileInputStream(reviewedDocsFile);
						bufferedInputStream2=new BufferedInputStream(inputStream2);
						pstmt.setBinaryStream(1, bufferedInputStream2,(int)reviewedDocsFile.length());
						pstmt.setString(2,"Reviewed_Doc_"+reviewedDocsFileName);
						
						inputStream3=new FileInputStream(developerDocsFile);
						bufferedInputStream3=new BufferedInputStream(inputStream3);
						pstmt.setBinaryStream(3, bufferedInputStream3,(int)developerDocsFile.length());
						pstmt.setString(4,"Assignee_Doc_"+developerDocsFileName);
						
					}else if(assignedDocsFile!=null){
						inputStream1=new FileInputStream(assignedDocsFile);
						bufferedInputStream1=new BufferedInputStream(inputStream1);
						pstmt.setBinaryStream(1, bufferedInputStream1,(int)assignedDocsFile.length());
						pstmt.setString(2,"Assigned_Doc_"+assignedDocsFileName);
		
					}else if( reviewedDocsFile!=null ){
				
						inputStream2=new FileInputStream(reviewedDocsFile);
						bufferedInputStream2=new BufferedInputStream(inputStream2);
						pstmt.setBinaryStream(1, bufferedInputStream2,(int)reviewedDocsFile.length());
						pstmt.setString(2,"Reviewed_Doc_"+reviewedDocsFileName);
					
						
					}else if(developerDocsFile!=null){
								
						inputStream3=new FileInputStream(developerDocsFile);
						bufferedInputStream3=new BufferedInputStream(inputStream3);
						pstmt.setBinaryStream(1, bufferedInputStream3,(int)developerDocsFile.length());
						pstmt.setString(2,"Assignee_Doc_"+developerDocsFileName);
						
					}
			//-----------
				
				
				
				
			pstmt.executeUpdate();
			
			//If Status Changed of Task
			if(oldStatusID!=statusID){
				taskStatusUpdation(Task_Summary_ID,reviewedByEmployeeID,statusID);
			}
			result=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, null);
			
			try {
				if(inputStream1!=null){
					inputStream1.close();
				}
				if(inputStream2!=null){
					inputStream2.close();
				}
				if(inputStream3!=null){
					inputStream3.close();
				}
				if(assignedDocsFile!=null){
					assignedDocsFile.delete();
				}
				if(reviewedDocsFile!=null){
					reviewedDocsFile.delete();
				}
				if(developerDocsFile!=null){
					developerDocsFile.delete();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return result;
	}
	


	public void addNewEmployee(String empName,String empDesignation,String empCode,String empEmail,Date doj,String empPhoneNo,String empAdd) throws BaajaException{
		Connection conn=null;
		ResultSet rs=null;
		PreparedStatement psmt=null;
		Statement stmt = null ;
		String d;
		
		
		try
		{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			d=sdf.format(doj);
			
			//Date conDate=sdf.parse(doj);
			conn = getDbConnection();
			String query = "insert into ets_emp_sw (emp_name,designation,emp_code,emp_email,emp_doj,emp_phoneNumber,emp_address)values('"+empName+"','"+empDesignation+"','"+empCode+"','"+empEmail+"','"+d+"','"+empPhoneNo+"','"+empAdd+"')";
			psmt = conn.prepareStatement(query);
			psmt.executeUpdate();
	    }
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sqlCleanUp(conn, stmt, rs);
			}
		
	}
	
	public int getUserRoleID(Integer empID) {

		int roleID = 0;
		Connection conn = null;

		String query = "SELECT ROLE_ID FROM ets_user_login_role where USER_ID="
				+ empID;

		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				roleID = rs.getInt("ROLE_ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return roleID;

	}

	public HashMap<Integer, String> getProjectList() {
		HashMap<Integer, String> projectList = new HashMap<Integer, String>();
		Connection conn = null;
		String query = "SELECT * FROM ets_base_project";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				projectList.put(rs.getInt("Project_ID"),rs.getString("Project_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return projectList;

	}
	
	public HashMap<Integer, String> getTaskPriorityList() {
		HashMap<Integer, String> projectList = new HashMap<Integer, String>();
		Connection conn = null;
		String query = "SELECT * FROM ets_task_priority";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				projectList.put(rs.getInt("Priority_Id"),rs.getString("Description"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return projectList;

	}
	
	
	private void taskStatusUpdation(Integer task_Summary_ID,Integer reviewedByEmployeeID, Integer statusID) {
		Connection conn=null;
		PreparedStatement pstmt = null;

		String insertQuery="insert into ets_task_status_updation" +
				"(task_summary_id,reviewed_By_EmployeeID,Task_Status_ID,Task_Update_Time) " +
				"values("+task_Summary_ID+","+reviewedByEmployeeID+","+statusID+",now())";

		String updateQuery="update ets_task_summary " +
				"set Counter=" +
				"(select count(*) from  ets_task_status_updation " +
				"where task_summary_id="+task_Summary_ID+" and Task_Status_ID=4) " +
				"where task_summary_id="+task_Summary_ID;
		try {
			conn=getDbConnection();
			pstmt=conn.prepareStatement(insertQuery);
			pstmt.executeUpdate();
			
			pstmt=conn.prepareStatement(updateQuery);
			pstmt.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			sqlCleanUp(conn, pstmt,null);
			
		}
		
	}
	
	public void insertNotifyData(Integer developer_id){
		Connection conn1=null;
		PreparedStatement pstmtSelect = null;
		PreparedStatement pstmtUpdate = null;
		PreparedStatement pstmtInsert = null;
		ResultSet rs=null;
		
		String selectQuery="select * from ets_notification_data where emp_developer_id=?";
		String updateQuery="update ets_notification_data set counter=? where emp_developer_id=?";
		String insertQuery="insert into ets_notification_data(emp_developer_id,counter) values(?,1);";
		
		try{
			
			conn1=getDbConnection();
			pstmtSelect=conn1.prepareStatement(selectQuery);
			pstmtSelect.setInt(1, developer_id);
			rs=pstmtSelect.executeQuery();
			if(rs.next()){
				int counter=rs.getInt("counter");
				counter=counter+1;
				pstmtUpdate=conn1.prepareStatement(updateQuery);
				pstmtUpdate.setInt(1, counter);
				pstmtUpdate.setInt(2, developer_id);
				pstmtUpdate.execute();
			}else{
				pstmtInsert=conn1.prepareStatement(insertQuery);
				pstmtInsert.setInt(1, developer_id);
				pstmtInsert.execute();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public Integer checkTask(Integer logedEmpId){
		Connection conn1=null;
		PreparedStatement pstmtSelect = null;
		PreparedStatement pstmtUpdate = null;
		String selectQuery="select counter from ets_notification_data where emp_developer_id=?";
		String updateQuery="update ets_notification_data set counter=0 where emp_developer_id=?";
		ResultSet rs=null;
		Integer result=0;
		try{
			conn1=getDbConnection();
			pstmtSelect=conn1.prepareStatement(selectQuery);
			pstmtSelect.setInt(1, logedEmpId);
			rs=pstmtSelect.executeQuery();
			if(rs.next()){
				result=rs.getInt("counter");
				pstmtUpdate=conn1.prepareStatement(updateQuery);
				pstmtUpdate.setInt(1, logedEmpId);
				pstmtUpdate.execute();
				
			}
			
		}catch(Exception e){
			
		}
		
		return result;
	}
	
	public HashMap<Integer, String> getDepartmentList(Integer loginEmpID) {
		
		HashMap<Integer, String> departmentList = new HashMap<Integer, String>();
		Connection conn = null;
		
		String query = "SELECT DEPARTMENT_ID,DEPARTMENT_NAME FROM ets_department " +
						"where DEPARTMENT_ID in (SELECT DEPARTMENT_ID FROM ets_emp_sw_department WHERE Emp_ID="+loginEmpID+")";
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				departmentList.put(rs.getInt("DEPARTMENT_ID"),rs.getString("DEPARTMENT_NAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return departmentList;

	}
	
	public File getSpecificDocs(Integer taskID,String docName) {

		Connection conn = null;
		Blob assigned_Doc=null,reviewed_Doc=null,developer_Doc=null;
		File file=null;
		FileOutputStream fileOutputStream=null;
		String selectQuery = "select  Assigned_Doc, Assigned_Doc_Name," +
				" Reviewed_Doc, Reviewed_Doc_Name, " +
				"Developer_Docs,Developer_Doc_Name " +
				"from ets_task_summary where Task_Summary_ID="+taskID;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		
		try {
			conn = getDbConnection();
			pstmt=conn.prepareStatement(selectQuery);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				if(docName.equalsIgnoreCase(rs.getString("Assigned_Doc_Name")))
				assigned_Doc = rs.getBlob("Assigned_Doc");
				
				if(docName.equalsIgnoreCase(rs.getString("Reviewed_Doc_Name")))
				reviewed_Doc = rs.getBlob("Reviewed_Doc");
				
				if(docName.equalsIgnoreCase(rs.getString("Developer_Doc_Name")))
				developer_Doc=rs.getBlob("Developer_Docs");
				
				
				if(assigned_Doc!=null){
					
					file = new File(docName);
					fileOutputStream=new FileOutputStream(file);
					fileOutputStream.write(assigned_Doc.getBytes(1, (int) assigned_Doc.length()), 0, (int) assigned_Doc.length());
					
				}
				
				if(reviewed_Doc!=null){
					
					
					file = new File(docName);
					fileOutputStream=new FileOutputStream(file);
					fileOutputStream.write(reviewed_Doc.getBytes(1, (int) reviewed_Doc.length()), 0, (int) reviewed_Doc.length());
				}
				
				if(developer_Doc!=null){
					
					
					file = new File(docName);
					fileOutputStream=new FileOutputStream(file);
					fileOutputStream.write(developer_Doc.getBytes(1, (int) developer_Doc.length()), 0, (int) developer_Doc.length());
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				sqlCleanUp(conn, pstmt, null);
				fileOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		return file;
	}
	
	
	/*public HashMap<Integer, String> getEmployeeListAndRole(Integer departmentId) {
		HashMap<Integer, String> employeeList = new HashMap<Integer, String>();
		Connection conn = null;
		String selectQuery;
		
		if(departmentId==null){
			selectQuery = "select ees.Emp_Id,ees.Emp_Name from ets_emp_sw as ees,ets_user_login as eul," +
					"ets_user_login_role as eulr where  ees.Emp_Id=eul.Emp_ID and eul.USER_ID=eulr.USER_ID  and" +
					" eulr.ROLE_ID!=10; ";
		}else{
			selectQuery="select ees.Emp_Id,ees.Emp_Name,eulr.ROLE_ID from ets_emp_sw as ees,ets_user_login as eul," +
			"ets_user_login_role as eulr where  ees.Emp_Id=eul.Emp_ID and eul.USER_ID=eulr.USER_ID and" +
			" ees.Emp_ID in  (select emp_id from  ets_emp_sw_department where department_id="+departmentId+") " +
					"and eulr.ROLE_ID!=10";
		}
				
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(selectQuery);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				employeeList.put(rs.getInt("Emp_ID"),rs.getString("Emp_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return employeeList;

	}*/
	
	public Date getLastExpectedDate(Integer empId){
		Date date=new Date();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		
		String selectQuery="select expected_completion_date from ets_task_summary where emp_id="+empId+" order by task_summary_id desc";
		try {
			conn = getDbConnection();
			stmt=conn.createStatement();
			rs=stmt.executeQuery(selectQuery);
			if (rs.next()) {
				date=rs.getDate("expected_completion_date");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, stmt, rs);
		}
		
		return date;
		
		
	}

	public ArrayList<RemarksVo> getRemarksVoList(Integer taskSummaryID) {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		
		ArrayList<RemarksVo> remarksVoList=new ArrayList<RemarksVo>();
		RemarksVo remarksVo=null;
		HashMap<Integer, String> employeeList=getEmployeeList(null);
		String selectQuery="SELECT REMARKS_ID,TASK_SUMMARY_ID,EMP_ID,EMP_TYPE_ID,REMARKS,CREATED_ON FROM  ETS_REMARKS WHERE TASK_SUMMARY_ID="+taskSummaryID+" ORDER BY CREATED_ON";
		try {
			conn = getDbConnection();
			stmt=conn.createStatement();
			rs=stmt.executeQuery(selectQuery);
			while (rs.next()) {
				remarksVo=new RemarksVo();
				remarksVo.setRemarksId(rs.getInt("REMARKS_ID"));
				remarksVo.setTaskSummaryID(rs.getInt("TASK_SUMMARY_ID"));
				remarksVo.setEmployeeId(rs.getInt("EMP_ID"));
				remarksVo.setEmployeeName(employeeList.get(remarksVo.getEmployeeId()));
				remarksVo.setEmployeeTypeId(rs.getInt("EMP_TYPE_ID"));
				remarksVo.setRemarks(rs.getString("REMARKS"));
				
				
				
					try{
						if(rs.getString("CREATED_ON")!=null){
							remarksVo.setCreatedOn(format.parse(rs.getString("CREATED_ON")));
						}
					}catch (Exception e) {
						
					}
				remarksVoList.add(remarksVo);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, stmt, rs);
		}
		return remarksVoList;
	}
	
	
	public void saveUpdatedRemarks(Integer taskSummaryID, Integer empid, int emptypeid, String remarks) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String insertQuery="INSERT INTO ETS_REMARKS (TASK_SUMMARY_ID,EMP_ID,EMP_TYPE_ID,REMARKS,CREATED_ON)" +
							"VALUES ("+taskSummaryID+","+empid+","+emptypeid+",'"+remarks+"',NOW())";
		try {
			conn = getDbConnection();
			pstmt=conn.prepareStatement(insertQuery);
			pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, null);
		}
		
	}

	public void saveProject(EtsBaseProject etsBaseProject) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query=null;
		
		if(Validator.isNullOrEmptyInt(etsBaseProject.getProjectId())){
			query="INSERT INTO ETS_BASE_PROJECT (PROJECT_NAME,DESCRIPTION) VALUES (?,?)";
		}else{
			query="UPDATE ETS_BASE_PROJECT SET PROJECT_NAME=?,DESCRIPTION=? WHERE PROJECT_ID="+etsBaseProject.getProjectId();
		}
		try {
			conn = getDbConnection();
			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, etsBaseProject.getProjectName());
			pstmt.setString(2, etsBaseProject.getDescription());
			pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			sqlCleanUp(conn, pstmt, null);
		}
	}
	
	public ArrayList<EtsBaseProject> getEtsBaseProjectList() {
		ArrayList<EtsBaseProject> projectList = new ArrayList<EtsBaseProject>();
		EtsBaseProject etsBaseProject=null;
		Connection conn = null;
		String query = "SELECT * FROM ETS_BASE_PROJECT";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				etsBaseProject=new EtsBaseProject();
				etsBaseProject.setProjectId(rs.getInt("PROJECT_ID"));
				etsBaseProject.setProjectName(rs.getString("PROJECT_NAME"));
				etsBaseProject.setDescription(rs.getString("DESCRIPTION"));
				projectList.add(etsBaseProject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return projectList;

	}
	
}