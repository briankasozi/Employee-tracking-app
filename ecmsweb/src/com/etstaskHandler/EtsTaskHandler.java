package com.etstaskHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bbh.ets.bo.UserBo;
import com.bbh.ets.common.TaskConstant;
import com.bbh.ets.dao.EmployeeTaskDao;
import com.bbh.ets.vo.RemarksVo;
import com.bbh.ets.vo.TaskVo;
import com.bursys.baaja.basis.utils.Validator;
import com.bursys.ets.baaja.jsf.basis.BaseHandler;

public class EtsTaskHandler extends BaseHandler  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	HttpSession session = request.getSession(false);
	UserBo userBo = (UserBo) session.getAttribute("loggedEtsUser");
	EmployeeTaskDao employeeTaskDao=new EmployeeTaskDao();
	
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Integer employeeID=0;
	private List employeeList;
	private Integer assignedByEmployeeID;
	private List assignedByEmployeeList;
	private Integer reviewedByEmployeeID;
	private List reviewedByEmployeeList;
	private String task;
	private String taskDetail;
	private Integer statusID;
	private List statusList;
	private String remarks;
	private UploadedFile assignedDocs;
	private UploadedFile reviewedDocs;
	private Integer taskSummaryID;
	private String employeeName;
	private String empDesignation;
	private String assigneDesignation;
	private String reviewerDesignation;
	private Date fromDate=null;
	private Date toDate=null;

	
	private List projectList;
	private Integer projectID=0;
	private String 	projectName;
	private String 	moduleName;
	private String  developerRemaraks;
	
	ArrayList<TaskVo> taskList;
	private boolean taskPrev;

	
	private Date taskStartDate;
	private Date taskCompletionExpectedDate;
	private List priorityList;
	private Integer priorityId=2;
	
	private Date completionTime;
	
	private String assignerRemaraks;
	private String reviewerRemaraks;
	private UploadedFile developerDocs;
	private String docUpdated;
	private String popUpShow;
	private Integer departmentID=0;
	private List departmentList;
	private boolean multiDepartment=false;
	private Boolean searchStage=false;
	
	private Boolean assignedDocAttached;
	private Boolean reviewedDocAttached;
	private Boolean developerDocAttached;
	private String assignedDocName;
	private String reviewedDocName;
	private String developerDocName;
	private String multipleAssignees;
	private List<String> autocompleteList = new ArrayList<String>();
	
	private String taskType="false";
	private String timeDiff;
	private Integer oldStatus;
	private String message;

	private ArrayList<RemarksVo> assineeRemarksList=new ArrayList<RemarksVo>();
	private ArrayList<RemarksVo> assinerRemarksList=new ArrayList<RemarksVo>();
	private ArrayList<RemarksVo> reviewerRemarksList=new ArrayList<RemarksVo>();
	private Boolean validDate;
	private Integer index;
	
	{getDepartmenAndEmployeetList();}
	
	
	public String saveTask() throws IOException {

		if (!validation()) {
			return "";
		}
		if(!validateTime()){
			return "";
		}
		
		File file = null;

		
		if (assignedDocs != null) {
			file = new File(assignedDocs.getName());
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			IOUtils.copy(assignedDocs.getInputStream(), fileOutputStream);
			fileOutputStream.close();
		}

		reSetTextInfoDelimeiters();
		if (taskType.equals("true")) {
			String[] names = multipleAssignees.split(",");
			HashMap<Integer, String> list = employeeTaskDao.getEmployeeList(departmentID);
			String newEmp = null;

			for (int i = 0; i < names.length; i++) {
				for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
					Map.Entry pairs = (Map.Entry) iter.next();
					Integer newEmpId = (Integer) pairs.getKey();
					newEmp = (String) pairs.getValue();
				
					if (newEmp.trim().toLowerCase().equals(names[i].trim().toLowerCase())) {
						employeeID = newEmpId;
						if (employeeTaskDao.saveTaskSummary(employeeID,reviewedByEmployeeID, assignedByEmployeeID,task, taskDetail, file, projectID, moduleName,taskStartDate, taskCompletionExpectedDate,priorityId, departmentID)) {
							break;
						} else {
							addErrorMessage("Error In Saving Task");
							if (file != null)
								file.delete();
							return "";
						}
					}
				}
				
			}
			addInfoMessage("Task Created Successfully");
			return taskListPage();
		}else {
			if (employeeTaskDao.saveTaskSummary(employeeID,reviewedByEmployeeID, assignedByEmployeeID, task,taskDetail, file, projectID, moduleName, taskStartDate,taskCompletionExpectedDate, priorityId, departmentID)) {
				addInfoMessage("Task Created Successfully");
				if (file != null)
					file.delete();
				searchStage = false;
				return taskListPage(); // return tasklistPage
			} else {
				addErrorMessage("Error In Saving Task");
				if (file != null)
					file.delete();
				return "";
			}
		}
	}

	public String updateTask() throws IOException, ParseException{
		if(!validation()){
			return "";
		}
		if(!validateSize()){
			return "";
		}
		/* if(priorityId==0){
		addErrorMessage("Please Select Task Priority");
		return "";
		
		 }*/

		 if(!statusPermissions(null)){
			 return "";
		 }

		if(reviewedByEmployeeID==0){
				addErrorMessage("Please Select Reviewer");
				return "";
		}
		File assignedDocsFile=null,reviewedDocsFile=null,developerDocsFile=null;
		if(statusID==7){
			if(completionTime==null){
				addErrorMessage("Completion time can't be empty");
				return "";
			}
		}
		
		
		if(assignedDocs!=null){
			assignedDocsFile=new File(assignedDocs.getName());
			FileOutputStream fileOutputStream=new FileOutputStream(assignedDocsFile);
	        IOUtils.copy(assignedDocs.getInputStream(), fileOutputStream);
	        fileOutputStream.close();
		}
		
		if(reviewedDocs!=null){
			reviewedDocsFile=new File(reviewedDocs.getName());
			FileOutputStream fileOutputStream=new FileOutputStream(reviewedDocsFile);
	        IOUtils.copy(reviewedDocs.getInputStream(), fileOutputStream);
	        fileOutputStream.close();
		}
		
		if(developerDocs!=null){
			developerDocsFile=new File(developerDocs.getName());
			FileOutputStream fileOutputStream=new FileOutputStream(developerDocsFile);
	        IOUtils.copy(developerDocs.getInputStream(), fileOutputStream);
	        fileOutputStream.close();
		}
		
		
		reSetTextInfoDelimeiters();
		if(employeeTaskDao.updateTaskSummary(taskSummaryID,employeeID, statusID, assignedByEmployeeID,
				reviewedByEmployeeID, task,taskDetail, assignedDocsFile,
				reviewedDocsFile,projectID,moduleName,completionTime,
				assignerRemaraks,reviewerRemaraks,developerRemaraks,developerDocsFile,docUpdated,taskStartDate,taskCompletionExpectedDate)){
			
			saveUpdatedRemaks();  // save updated remarks
			//taskList=employeeTaskDao.getTaskList(null,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,searchStage);
			
			
			
			if(searchStage){
				ArrayList<Integer> temp=new ArrayList<Integer>();
				for(TaskVo taskVo:taskList){
					temp.add(taskVo.getTaskSummaryID());
				}
				
				taskList.clear();
				ArrayList<TaskVo> t;
				int i=0;
				for(Integer tempTaskSummaryID:temp){
					t=employeeTaskDao.getTaskList(tempTaskSummaryID,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,false);
					t.get(0).setIndex(i++);
					taskList.addAll(t);
				}
				openTask(taskList.get(index));
			}else{
				taskList=employeeTaskDao.getTaskList(null,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,searchStage);
				openTask(taskList.get(index));
			}
			addInfoMessage("Task Update Successfully");
		}else{
			addErrorMessage("Error In Saving Task");
		}
		return "";
		
	}
	
	private void reSetTextInfoDelimeiters() {
		
		
		if(!Validator.isNullOrEmpty(task)){
			task=task.replaceAll("\'", "''");
			task=task.replace("\\", "\\\\");
		}
		
		
		if(!Validator.isNullOrEmpty(taskDetail)){
			taskDetail=taskDetail.replaceAll("\'", "''");
			taskDetail=taskDetail.replace("\\", "\\\\");
		}
		
		if(!Validator.isNullOrEmpty(moduleName)){
			moduleName=moduleName.replaceAll("\'", "''");
			moduleName=moduleName.replace("\\", "\\\\");
		}
		
		if(!Validator.isNullOrEmpty(assignerRemaraks)){
			assignerRemaraks=assignerRemaraks.replaceAll("\'", "''");
			assignerRemaraks=assignerRemaraks.replace("\\", "\\\\");
		}
		
		if(!Validator.isNullOrEmpty(reviewerRemaraks)){
			reviewerRemaraks=reviewerRemaraks.replaceAll("\'", "''");
			reviewerRemaraks=reviewerRemaraks.replace("\\", "\\\\");
		}
		
		if(!Validator.isNullOrEmpty(developerRemaraks)){
			developerRemaraks=developerRemaraks.replaceAll("\'", "''");
			developerRemaraks=developerRemaraks.replace("\\", "\\\\");
		}
		
	}

	private void saveUpdatedRemaks() {
		
		if(!developerRemaraks.trim().isEmpty() && (assineeRemarksList.size()==0 || !cheackRemarks(assineeRemarksList.get(assineeRemarksList.size()-1).getRemarks(),developerRemaraks))){
			employeeTaskDao.saveUpdatedRemarks(taskSummaryID,userBo.getLoginEmpID(),TaskConstant.ASSIGNEE,developerRemaraks);
		}
		
		if(!assignerRemaraks.trim().isEmpty() && (assinerRemarksList.size()==0 || !cheackRemarks(assinerRemarksList.get(assinerRemarksList.size()-1).getRemarks(),assignerRemaraks))){
			employeeTaskDao.saveUpdatedRemarks(taskSummaryID,userBo.getLoginEmpID(),TaskConstant.ASSIGNER,assignerRemaraks);
		}
		
		if(!reviewerRemaraks.trim().isEmpty() && (reviewerRemarksList.size()==0 || !cheackRemarks(reviewerRemarksList.get(reviewerRemarksList.size()-1).getRemarks(),reviewerRemaraks))){
			employeeTaskDao.saveUpdatedRemarks(taskSummaryID,userBo.getLoginEmpID(),TaskConstant.REVIEWER,reviewerRemaraks);
		}
	}
	
	private boolean cheackRemarks(String oldRemarks, String newRemarks){
		oldRemarks=oldRemarks.replaceAll("\'", "''");
		oldRemarks=oldRemarks.replace("\\", "\\\\");
		
		oldRemarks=oldRemarks.replace("[,\'\"\\]", "");
		newRemarks=newRemarks.replace("[,\'\"\\]", "");
		
		if(oldRemarks.equalsIgnoreCase(newRemarks)){
			return true;
		}else{
			return false;
		}
		
	}

	public String addNewTask(){
		departmentList=getDepartmenAndEmployeetList();
		assignedByEmployeeID=userBo.getLoginEmpID();
		reviewedByEmployeeID=userBo.getLoginEmpID();
		
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		taskStartDate=calendar.getTime();
		
		calendar.add(Calendar.HOUR, 2);
		taskCompletionExpectedDate=calendar.getTime();
		
		return "addTask";
	}
	
	public String deleteTask(TaskVo list){
		employeeTaskDao.deleteTaskSummary(list.getTaskSummaryID());
		addInfoMessage("Task Deleted");
		return "taskListPerventRefesh";
	}
	
	public String download(TaskVo list){
		
		HttpServletResponse response =((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse());
		response.setContentType("application/zip");
		FileInputStream fileInputStream=null;
		BufferedInputStream bufferedInputStream=null;
		
		
		File file=employeeTaskDao.getZipDocs(list.getTaskSummaryID());
		
		
		 
		if(file==null){
			addInfoMessage("no Attachment");
			return "";
		}
		
		response.addHeader("Content-disposition", "attachment;filename="+file.getName());
		 try {
			 fileInputStream= new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			
			int rLength = -1;
            byte[] buffer = new byte[1000];
            while((rLength = bufferedInputStream.read(buffer, 0, 100)) != -1){
            	response.getOutputStream().write(buffer, 0, rLength);
            }

            FacesContext.getCurrentInstance().responseComplete();
            	
		} catch (Exception e) {
			addErrorMessage("error");
			e.printStackTrace();
		}finally{
			try {
				bufferedInputStream.close();
				fileInputStream.close();
				file.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return "";
	}
	
	
	
	
	public String openTask(TaskVo taskVo) throws ParseException{
		index=taskVo.getIndex();
		taskSummaryID=taskVo.getTaskSummaryID();
		employeeID=taskVo.getEmployeeID();
		assignedByEmployeeID=taskVo.getAssignedByEmployeeID();
		task=taskVo.getTask();
		taskDetail=taskVo.getTaskDetail();
		statusID=taskVo.getStatusID();
		oldStatus=statusID;
		remarks=taskVo.getRemarks();
		projectID=taskVo.getProjectID();
		moduleName=taskVo.getModuleName();
		priorityId=taskVo.getPriorityId();
		reviewedByEmployeeID=taskVo.getReviewedByEmployeeID();
		
		developerRemaraks=taskVo.getDeveloperRemaraks();
		reviewerRemaraks=taskVo.getReviewerRemaraks();
		assignerRemaraks=taskVo.getAssignerRemaraks();
		docUpdated=taskVo.getDocUpdated();
		departmentID=taskVo.getDepartmentID();
		employeeList=getAllEmployeeList(departmentID);
		assignedByEmployeeList=getAllEmployeeList(departmentID);
		reviewedByEmployeeList=getAllEmployeeList(departmentID);
		
		developerDocAttached=taskVo.getDeveloperDocAttached();
		developerDocName=taskVo.getDeveloperDocName();
		
		assignedDocAttached=taskVo.getAssignedDocAttached();
		assignedDocName=taskVo.getAssignedDocName();
		
		reviewedDocAttached=taskVo.getReviewedDocAttached();
		reviewedDocName=taskVo.getReviewedDocName();
		
		
		
		try {
			if(taskVo.getTaskCompletionDate()!=null && !taskVo.getTaskCompletionDate().isEmpty())
			completionTime=format.parse(taskVo.getTaskCompletionDate());
			
			if(taskVo.getTaskCompletionExpectedDate()!=null && !taskVo.getTaskCompletionExpectedDate().isEmpty())
			taskCompletionExpectedDate=format.parse(taskVo.getTaskCompletionExpectedDate());
			
			if(taskVo.getTaskStartDate()!=null && !taskVo.getTaskStartDate().isEmpty())
			taskStartDate=format.parse(taskVo.getTaskStartDate());
			
			
			assineeRemarksList.clear();
			assinerRemarksList.clear();
			reviewerRemarksList.clear();
			for(RemarksVo remarksVo:employeeTaskDao.getRemarksVoList(taskSummaryID)){
				if(remarksVo.getEmployeeTypeId()==TaskConstant.ASSIGNEE){
					assineeRemarksList.add(remarksVo);
				}else if(remarksVo.getEmployeeTypeId()==TaskConstant.ASSIGNER){
					assinerRemarksList.add(remarksVo);
				}else if(remarksVo.getEmployeeTypeId()==TaskConstant.REVIEWER){
					reviewerRemarksList.add(remarksVo);
				}
			}
			
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "editTask";
	}
	
	public String previousTask() throws ParseException{
		if(taskList==null){
			taskList=employeeTaskDao.getTaskList(null,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,searchStage);
		}
		
		TaskVo taskVo=null;
		
		if(index==0){
			taskVo=taskList.get(taskList.size()-1);
		}else{
			taskVo=taskList.get(--index);
		}
		
		return openTask(taskVo);
	}
	
	
	public String nextTask() throws ParseException{
		if(taskList==null){
			taskList=employeeTaskDao.getTaskList(null,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,searchStage);
		}
		
		TaskVo taskVo=null;
		
		if(index==taskList.size()-1){
			taskVo=taskList.get(0);
		}else{
			taskVo=taskList.get(++index);
		}
		
		return openTask(taskVo);
	}
	
	public String taskListPage(){
		return "taskListPerventRefesh";
	}
	
	public String taskListPageWithPreviousFilter(){
		if(!searchStage){
			return "taskListPerventRefesh";
		}else{
			employeeID=0;
			assignedByEmployeeID=0;
			reviewedByEmployeeID=0;
			projectID=0;
			statusID=0;
			toDate=null;
			fromDate=null;
			taskCompletionExpectedDate=null;
			getDepartmenAndEmployeetList();
			return "taskList";
		}
		
	}
	
	
	public Boolean validation(){
		if(!taskType.equals("true")){
			if(employeeID==0){
				addErrorMessage("Please Select Employee");
				return false;
			}
		}else{
			if(multipleAssignees.trim().isEmpty()){
				addErrorMessage("Please Put Employee Name");
				return false;
			}
		}
		if(projectID==0){
			addErrorMessage("Please Select Project");
			return false;
		}/*else if(moduleName==null || moduleName.trim().isEmpty()){
			addErrorMessage("Please Enter Task Module Name");
			return false;
		}*/else if(assignedByEmployeeID==0){
			addErrorMessage("Please Select Assignee");
			return false;
		}else if(reviewedByEmployeeID==0){
			addErrorMessage("Please Select Reviewer");
			return false;
		}else if(task==null || task.trim().isEmpty()){
			addErrorMessage("Please Enter Task Description");
			return false;
		}
		//.getTime()-.getTime();
		return true;
	}
	
	public String search(){
			searchStage=true;
			taskList=employeeTaskDao.getTaskList(null,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,searchStage);
		return "";
	}
	public void searchOnClick(AjaxBehaviorEvent event){
		
		searchStage=true;
		taskList=employeeTaskDao.getTaskList(null,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,searchStage);
	
}
	
	public void changeEmployeeList(AjaxBehaviorEvent event) {

		HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
		Integer selectedEmpID = (Integer) choice.getValue();

		empDesignation = employeeTaskDao.getDesignation(selectedEmpID);

	}
	
	public void changeAssigneList(AjaxBehaviorEvent event) {

		HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
		Integer selectedEmpID = (Integer) choice.getValue();

		assigneDesignation=employeeTaskDao.getDesignation(selectedEmpID);

	}
	
	public void changeReviewerList(AjaxBehaviorEvent event) {

		HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
		Integer selectedEmpID = (Integer) choice.getValue();

		reviewedByEmployeeID=selectedEmpID;
		reviewerDesignation=employeeTaskDao.getDesignation(reviewedByEmployeeID);

	}
	
	

	/**
	 * @return the employeeID
	 */
	public Integer getEmployeeID() {
		return employeeID;
	}

	/**
	 * @param employeeID the employeeID to set
	 */
	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * @return the employeeList
	 */
	public List getEmployeeList() {
		return employeeList;
	}

	/**
	 * @param employeeList the employeeList to set
	 */
	public void setEmployeeList(List employeeList) {
		this.employeeList = employeeList;
	}

	/**
	 * @return the assignedByEmployeeID
	 */
	public Integer getAssignedByEmployeeID() {
		return assignedByEmployeeID;
	}

	/**
	 * @param assignedByEmployeeID the assignedByEmployeeID to set
	 */
	public void setAssignedByEmployeeID(Integer assignedByEmployeeID) {
		this.assignedByEmployeeID = assignedByEmployeeID;
	}

	/**
	 * @return the assignedByEmployeeList
	 */
	public List getAssignedByEmployeeList() {
		return assignedByEmployeeList;
	}

	/**
	 * @param assignedByEmployeeList the assignedByEmployeeList to set
	 */
	public void setAssignedByEmployeeList(List assignedByEmployeeList) {
		this.assignedByEmployeeList = assignedByEmployeeList;
	}

	/**
	 * @return the reviewedByEmployeeID
	 */
	public Integer getReviewedByEmployeeID() {
		return reviewedByEmployeeID;
	}

	/**
	 * @param reviewedByEmployeeID the reviewedByEmployeeID to set
	 */
	public void setReviewedByEmployeeID(Integer reviewedByEmployeeID) {
		this.reviewedByEmployeeID = reviewedByEmployeeID;
	}

	/**
	 * @return the reviewedByEmployeeList
	 */
	public List getReviewedByEmployeeList() {
		return reviewedByEmployeeList;
	}

	/**
	 * @param reviewedByEmployeeList the reviewedByEmployeeList to set
	 */
	public void setReviewedByEmployeeList(List reviewedByEmployeeList) {
		this.reviewedByEmployeeList = reviewedByEmployeeList;
	}

	/**
	 * @return the task
	 */
	public String getTask() {
		return task;
	}

	/**
	 * @param task the task to set
	 */
	public void setTask(String task) {
		this.task = task;
	}

	/**
	 * @return the statusID
	 */
	public Integer getStatusID() {
		return statusID;
	}

	/**
	 * @param statusID the statusID to set
	 */
	public void setStatusID(Integer statusID) {
		this.statusID = statusID;
	}

	/**
	 * @return the statusList
	 */
	public List getStatusList() {
		List selectItems = new ArrayList<SelectItem>();
		HashMap<Integer, String> list = employeeTaskDao.getTaskStatusList();

		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			Map.Entry pairs = (Map.Entry) iter.next();
			selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
		}
		return selectItems;
	}

	/**
	 * @param statusList the statusList to set
	 */
	public void setStatusList(List statusList) {
		this.statusList = statusList;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the assignedDocs
	 */
	public UploadedFile getAssignedDocs() {
		return assignedDocs;
	}

	/**
	 * @param assignedDocs the assignedDocs to set
	 */
	public void setAssignedDocs(UploadedFile assignedDocs) {
		this.assignedDocs = assignedDocs;
	}

	/**
	 * @return the reviewedDocs
	 */
	public UploadedFile getReviewedDocs() {
		return reviewedDocs;
	}

	/**
	 * @param reviewedDocs the reviewedDocs to set
	 */
	public void setReviewedDocs(UploadedFile reviewedDocs) {
		this.reviewedDocs = reviewedDocs;
	}

	/**
	 * @return the taskList
	 */
	public ArrayList<TaskVo> getTaskList() {
		if(taskList==null){
			taskList=employeeTaskDao.getTaskList(null,fromDate,toDate,taskCompletionExpectedDate,employeeID,assignedByEmployeeID,reviewedByEmployeeID,projectID,userBo.getLoginEmpID(),userBo.getRoleName(),statusID,searchStage);
		}
		return taskList;
	}

	/**
	 * @param taskList the taskList to set
	 */
	public void setTaskList(ArrayList<TaskVo> taskList) {
		this.taskList = taskList;
	}

	/**
	 * @return the taskDetail
	 */
	public String getTaskDetail() {
		return taskDetail;
	}

	/**
	 * @param taskDetail the taskDetail to set
	 */
	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}

	public Integer getTaskSummaryID() {
		return taskSummaryID;
	}

	public void setTaskSummaryID(Integer taskSummaryID) {
		this.taskSummaryID = taskSummaryID;
	}
	

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	
	
	public String cancel(){
		return "cancel";
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	


	public String getEmpDesignation() {
	
		return empDesignation;
	}

	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}

	/**
	 * @param assigneDesignation the assigneDesignation to set
	 */
	public void setAssigneDesignation(String assigneDesignation) {
		this.assigneDesignation = assigneDesignation;
	}

	/**
	 * @return the assigneDesignation
	 */
	public String getAssigneDesignation() {
		assigneDesignation=employeeTaskDao.getDesignation(assignedByEmployeeID);
		return assigneDesignation;
	}

	/**
	 * @return the projectID
	 */
	public Integer getProjectID() {
		return projectID;
	}

	/**
	 * @param projectID the projectID to set
	 */
	public void setProjectID(Integer projectID) {
		this.projectID = projectID;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the developerRemaraks
	 */
	public String getDeveloperRemaraks() {
		return developerRemaraks;
	}

	/**
	 * @param developerRemaraks the developerRemaraks to set
	 */
	public void setDeveloperRemaraks(String developerRemaraks) {
		this.developerRemaraks = developerRemaraks;
	}

	/**
	 * @param projectList the projectList to set
	 */
	public void setProjectList(List projectList) {
		this.projectList = projectList;
	}

	



	/**
	 * @return the reviewerDesignation
	 */
	public String getReviewerDesignation() {
		reviewerDesignation=employeeTaskDao.getDesignation(reviewedByEmployeeID);
		return reviewerDesignation;
	}

	/**
	 * @param reviewerDesignation the reviewerDesignation to set
	 */
	public void setReviewerDesignation(String reviewerDesignation) {
		this.reviewerDesignation = reviewerDesignation;
	}

	public Date getTaskStartDate() {
		return taskStartDate;
	}

	public void setTaskStartDate(Date taskStartDate) {
		this.taskStartDate = taskStartDate;
	}

	public Date getTaskExpectedDate() {
		return taskCompletionExpectedDate;
	}

	public void setTaskExpectedDate(Date taskExpectedDate) {
		this.taskCompletionExpectedDate = taskCompletionExpectedDate;
	}

	public String checkTimeDiff(Date expectedDate,Date startdate) {
		
		long diff=expectedDate.getTime()-startdate.getTime();
		
		long secondDiff=diff/1000;
		long minutesDiff=diff/(60*1000);
		long hourDiff=diff/(60*60*1000);
		long dayDiff=diff/(24*60*60*1000);
		long monthDiff=(long)(diff/(60 * 60 * 1000 * 24 * 30.41666666));
		long yearDiff=diff/(60 * 60 * 1000 * 24 * 365);
		
		String dateDiff = "";
		
		if(yearDiff!=0){
			dateDiff=yearDiff+" Years ";
		}else if(monthDiff!=0){
			dateDiff+=monthDiff+" Months ";
		}else if(dayDiff!=0){
			dateDiff+=dayDiff+" Days ";
		}else if(hourDiff!=0){
			dateDiff+=hourDiff+" Hours ";
		}else if(minutesDiff!=0){
			dateDiff+=minutesDiff+" Minutes ";
		}else if(secondDiff!=0){
			dateDiff+=secondDiff+" Seconds";
		}
		return dateDiff;
		
	}
	/**
	 * @return the projectList
	 */
	public List getProjectList() {
		List selectItems = new ArrayList<SelectItem>();
		HashMap<Integer, String> list = employeeTaskDao.getProjectList();
		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			Map.Entry pairs = (Map.Entry) iter.next();
			selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
		}
		return selectItems;
	}



	public List getPriorityList() {
		List selectItems = new ArrayList<SelectItem>();
		HashMap<Integer, String> list = employeeTaskDao.getTaskPriorityList();
		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			Map.Entry pairs = (Map.Entry) iter.next();
			selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
		}
		
		return selectItems;
	}

	public void setPriorityList(List priorityList) {
		this.priorityList = priorityList;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	public boolean isTaskPrev() {
		if(userBo.getTaskPrev())
			taskPrev=true ;
			else
			taskPrev=false;
		
		return taskPrev;
	}

	public void setTaskPrev(boolean taskPrev) {
		this.taskPrev = taskPrev;
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

	public UploadedFile getDeveloperDocs() {
		return developerDocs;
	}

	public void setDeveloperDocs(UploadedFile developerDocs) {
		this.developerDocs = developerDocs;
	}

	public UserBo getUserBo() {
		return userBo;
	}

	public void setUserBo(UserBo userBo) {
		this.userBo = userBo;
	}

	public String getDocUpdated() {
		return docUpdated;
	}

	public void setDocUpdated(String docUpdated) {
		this.docUpdated = docUpdated;
	}
	
	public String checkTask(){
		
		Integer count=employeeTaskDao.checkTask(userBo.getLoginEmpID());
		if(count!=0){
			return "data";
		}else{
			return "";
		}
		
	}

	public String getPopUpShow() {
		return popUpShow;
	}

	public void setPopUpShow(String popUpShow) {
		this.popUpShow = popUpShow;
	}

	public Boolean getSearchStage() {
		return searchStage;
	}

	public void setSearchStage(Boolean searchStage) {
		this.searchStage = searchStage;
	}
	
	public boolean validateSize(){
		if(task!=null&&task.length()>=500){
			   task=task.substring(0, 500);
				//addErrorMessage("Task Decription has exceede maximum length");
				//return false;
		}else if(taskDetail!=null&&taskDetail.length()>=2000){
			taskDetail=taskDetail.substring(0, 2000);
			//addErrorMessage("Task Detail has exceede maximum length");
			//return false;
		}else if(moduleName!=null&&moduleName.length()>=100){
			moduleName=moduleName.substring(0, 100);
			//addErrorMessage("Module Name has exceede maximum length");
			//return false;
		}else if(assignerRemaraks!=null&&assignerRemaraks.length()>=2000){
			assignerRemaraks=assignerRemaraks.substring(0, 2000);
			//addErrorMessage("Assigner Remarks has exceede maximum length");
			//return false;
		}else if(reviewerRemaraks!=null&&reviewerRemaraks.length()>=2000){
			reviewerRemaraks=reviewerRemaraks.substring(0, 2000);
			//addErrorMessage("Reviewer Remarks has exceede maximum length");
			//return false;
		}else if(developerRemaraks!=null&&developerRemaraks.length()>=2000){
			developerRemaraks=developerRemaraks.substring(0, 2000);
			//addErrorMessage("Assignee Remarks has exceede maximum length");
			//return false;
		}
		
		
		
		return true;
	}

	public Integer getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(Integer departmentID) {
		this.departmentID = departmentID;
	}

	public List getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List departmentList) {
		this.departmentList = departmentList;
	}

	public boolean isMultiDepartment() {
		return multiDepartment;
	}

	public void setMultiDepartment(boolean multiDepartment) {
		this.multiDepartment = multiDepartment;
	}

	public List getAllEmployeeList(Integer localDepartmentID) {
		List selectItems = new ArrayList<SelectItem>();
		HashMap<Integer, String> list = employeeTaskDao.getEmployeeList(localDepartmentID);
		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			Map.Entry pairs = (Map.Entry) iter.next();
			selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
		}
		
		Collections.sort(selectItems, new Comparator<SelectItem>() {  
			 public int compare(SelectItem sItem1, SelectItem sItem2) {  
			 String sItem1Label = sItem1.getLabel();  
			 String sItem2Label = sItem2.getLabel();  
			  
			 return (sItem1Label.compareToIgnoreCase(sItem2Label));  
			 }  
			 }); 
		return selectItems;
	}
	
	public List getDepartmenAndEmployeetList() {
		List selectItems = new ArrayList<SelectItem>();
		ArrayList<Integer> tempDepartmentIDList=new ArrayList<Integer>();
		HashMap<Integer, String> list = employeeTaskDao.getDepartmentList(userBo.getLoginEmpID());
		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			Map.Entry pairs = (Map.Entry) iter.next();
			tempDepartmentIDList.add((Integer) pairs.getKey());
			selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
		}
		
		if(tempDepartmentIDList.size()>1){
			employeeList=new ArrayList();
			assignedByEmployeeList=new ArrayList();
			reviewedByEmployeeList=new ArrayList();
			
			for(Integer tempDepartmentID:tempDepartmentIDList){
				employeeList.addAll(getAllEmployeeList(tempDepartmentID));
				assignedByEmployeeList.addAll(getAllEmployeeList(tempDepartmentID));
				reviewedByEmployeeList.addAll(getAllEmployeeList(tempDepartmentID));
			}
			departmentID=0;
			multiDepartment=true;
		}else if (tempDepartmentIDList.size()==1){
			departmentID=tempDepartmentIDList.get(0);
			employeeList=getAllEmployeeList(departmentID);
			assignedByEmployeeList=getAllEmployeeList(departmentID);
			reviewedByEmployeeList=getAllEmployeeList(departmentID);
		}
		
		
		return selectItems;
	}
	
	public void changeDepartment(AjaxBehaviorEvent event) {
		HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
		departmentID=(Integer)choice.getValue();
		employeeList=getAllEmployeeList(departmentID);
		assignedByEmployeeList=getAllEmployeeList(departmentID);
		reviewedByEmployeeList=getAllEmployeeList(departmentID);
		assignedByEmployeeID=userBo.getLoginEmpID();
		reviewedByEmployeeID=userBo.getLoginEmpID();
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
	
	public String downloadSpecificDocs(String docName){
		
		HttpServletResponse response =((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse());
		
		FileInputStream fileInputStream=null;
		BufferedInputStream bufferedInputStream=null;
		String fileType="NA";
		
		
		File file=employeeTaskDao.getSpecificDocs(taskSummaryID,docName);
		
		
		 
		if(file==null){
			addInfoMessage("no Attachment");
			return "";
		}
		
		if(file.getName().length()>8){
			fileType=file.getName().substring(file.getName().indexOf('.', file.getName().length()-8)+1,file.getName().length());
		}else if(file.getName().length()>5){
			fileType=file.getName().substring(file.getName().indexOf('.', file.getName().length()-5)+1,file.getName().length());
		}else if(file.getName().length()>3){
			fileType=file.getName().substring(file.getName().length()-3,file.getName().length());
		}
		
		response.setContentType("application/"+fileType);
		response.addHeader("Content-disposition", "attachment;filename="+file.getName().trim().replaceAll("\\s", "_"));
		 try {
			 fileInputStream= new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			
			int rLength = -1;
            byte[] buffer = new byte[1000];
            while((rLength = bufferedInputStream.read(buffer, 0, 100)) != -1){
            	response.getOutputStream().write(buffer, 0, rLength);
            }

            FacesContext.getCurrentInstance().responseComplete();
            	
		} catch (Exception e) {
			addErrorMessage("error");
			e.printStackTrace();
		}finally{
			try {
				bufferedInputStream.close();
				fileInputStream.close();
				file.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return "";
	}
	
	public void changeMultipleAssigneesListener(AjaxBehaviorEvent event) {

		
		String multipleAssignees = (String) ((UIOutput) event.getSource()).getValue();
		if(multipleAssignees.length()<3){
			return;
		}
		
		String last=multipleAssignees.substring(multipleAssignees.length()-1);
		if (!last.equals(" ")) {
			return;
		}
		
		
		
		String temp[] = multipleAssignees.split(",");
		
		
		if(temp[temp.length - 1].length()<4){
			return;
		}
		
		temp[temp.length - 1]=temp[temp.length - 1].toLowerCase().trim();

		HashMap<Integer, String> list = employeeTaskDao.getEmployeeList(departmentID);
		String newEmp = null;
		boolean checkName=false;
		
		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			
			Map.Entry pairs = (Map.Entry) iter.next();
			newEmp = (String) pairs.getValue();
			if (newEmp.toLowerCase().startsWith(temp[temp.length - 1])) {
				temp[temp.length - 1] = newEmp;
				checkName=true;
			}
		}
		
		if(!checkName){
			temp[temp.length - 1]="";
			
		}

		multipleAssignees = "";
		for (String s : temp) {
			if(s.equals(""))
				continue;
			multipleAssignees = multipleAssignees + s + ", ";
		}
		
		this.multipleAssignees=multipleAssignees;

	}

	public String getMultipleAssignees() {
		return multipleAssignees;
	}

	public void setMultipleAssignees(String multipleAssignees) {
		this.multipleAssignees = multipleAssignees;
	}

	public List<String> getAutocompleteList() {
		return autocompleteList;
	}

	public void setAutocompleteList(List<String> autocompleteList) {
		this.autocompleteList = autocompleteList;
	}
	
	public void taskTypes(AjaxBehaviorEvent event) {

		HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
		String taskId =  (String)choice.getValue();
		employeeID=0;
		if(taskId.equalsIgnoreCase("true")){
			taskType="true";
		}else{
			taskType="false";
		}
		

	}



	public String getTaskType() {
		return taskType;
	}



	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	
	public boolean validateTime(){
		
		if (taskStartDate == null) {
			addErrorMessage("Please Start Date");
			return false;
		} else if (taskCompletionExpectedDate == null) {
			addErrorMessage("Please Select Expected Completion Date");
			return false;
		}

		timeDiff = checkTimeDiff(taskCompletionExpectedDate,taskStartDate);

		if (timeDiff.contains("Years") && timeDiff.startsWith("-")) {
			addErrorMessage("Invalid Years in Expected Completion Date");
			return false;
		}
		if (timeDiff.contains("Months") && timeDiff.startsWith("-")) {
			addErrorMessage("Invalid Months in Expected Completion Date");
			return false;
		}

		if (timeDiff.contains("Days") && timeDiff.startsWith("-")) {
			addErrorMessage("Invalid Days in Expected Completion Date");
			return false;
		}
		if (timeDiff.contains("Hours") && timeDiff.startsWith("-")) {
			addErrorMessage("Invalid Hours in Expected Completion Date, it should be 24 hours Format");
			return false;
		}

		if (!validateSize()) {
			return false;
		}
		if (priorityId == 0) {
			addErrorMessage("Please Select Task Priority");
			return false;

		}
		
		return true;
	}

	public Date getTaskCompletionExpectedDate() {
		
		return taskCompletionExpectedDate;
	}

	public void setTaskCompletionExpectedDate(Date taskCompletionExpectedDate) {
		this.taskCompletionExpectedDate = taskCompletionExpectedDate;
	}

	
	public Boolean statusPermissions(AjaxBehaviorEvent event) {

		if (oldStatus.equals(statusID)) {
			message = "";
			return true;
		}
		
		if(statusID==TaskConstant.STARTED || statusID==TaskConstant.IN_PROGRESS || statusID==TaskConstant.DONE){
			if(!userBo.getLoginEmpID().equals(employeeID)){
				message = "This Status Only For Assignee";
				return false;
			}
			
			if(developerRemaraks.trim().isEmpty() && statusID==TaskConstant.DONE){
				message = "Please Provide Assignee Remaraks";
				return false;
			}
			
		}else if(statusID==TaskConstant.COMPLETED){
			
			
			if(!userBo.getLoginEmpID().equals(reviewedByEmployeeID)){
				message = "This Status Only For Reviewer";
				return false;
			}
			
			if(reviewerRemaraks.trim().isEmpty()){
				message = "Please Provide Reviewer Remaraks";
				return false;
			}
			
		}else if(statusID==TaskConstant.ON_HOLD){
			if(!userBo.getRoleId().equals(TaskConstant.ADMINISTRATOR)){
				message = "This Status Only For Administrator";
				return false;
			}
		}else if(statusID==TaskConstant.RE_ASSIGNED){
			if(userBo.getLoginEmpID().equals(assignedByEmployeeID) || userBo.getLoginEmpID().equals(reviewedByEmployeeID) || userBo.getRoleId().equals(TaskConstant.ADMINISTRATOR)){
				message = "";
				return true;
			}else{
				message = "This Status Only For Assigner, Reviewer And Administraor";
				return false;
			}
		}
		
		message = "";
		return true;
	}

	public Integer getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Integer oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/*public List getEmployeeListOnRole(Integer localDepartmentID) {
		List selectItems = new ArrayList<SelectItem>();
		HashMap<Integer, String> list = employeeTaskDao.getEmployeeListAndRole(localDepartmentID);
		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			Map.Entry pairs = (Map.Entry) iter.next();
			selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
		}
		Collections.sort(selectItems, new Comparator<SelectItem>() {  
			 public int compare(SelectItem sItem1, SelectItem sItem2) {  
			 String sItem1Label = sItem1.getLabel();  
			 String sItem2Label = sItem2.getLabel();  
			  
			 return (sItem1Label.compareToIgnoreCase(sItem2Label));  
			 }  
			 }); 
		
		return selectItems;
	}*/
	
	public String exportTaskData(){
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet worksheet = workbook.createSheet("POI Worksheet");
		
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
        data.put("1", new Object[] {"Task Id", "Assignee", "Assigner","Reviewer","Task Description","Project","Creation Date","Start Date","Completion Date","Status","Priority","Re-assigned","Assignee Remarks","Assigner Remarks","Reviewer Remarks"});
		
        Set<String> keyset = data.keySet();
        HSSFRow row = null;
        int rownum = 0;
        for (String key : keyset){
        	int cellnum = 0;
        	 row = worksheet.createRow(rownum++);
        	
        	Object [] objArr = data.get(key);
        	for (Object obj : objArr){
        		
        		 HSSFCell cell = row.createCell(cellnum++);
       		  if(obj instanceof String)
                     cell.setCellValue((String)obj);
                 else if(obj instanceof Integer)
                     cell.setCellValue((Integer)obj);
        		            
            }
        }
        
        for(TaskVo taskVo:taskList){
        	 row = worksheet.createRow(rownum++);
        	
        	row.createCell((short)0 ).setCellValue(taskVo.getTaskSummaryID().toString());
        	row.createCell((short)1 ).setCellValue(taskVo.getEmployeeName());
        	row.createCell((short)2 ).setCellValue(taskVo.getAssignedByemployeeName());
        	row.createCell((short)3 ).setCellValue(taskVo.getReviewedByemployeeName());
        	
        	if(taskVo.getTask()!=null)
        	row.createCell((short)4 ).setCellValue(taskVo.getTask().replaceAll("[\\t\\n\\r]"," "));
        	
        	row.createCell((short)5 ).setCellValue(taskVo.getProjectName());
        	row.createCell((short)6 ).setCellValue(taskVo.getTaskCreationDate());
        	row.createCell((short)7 ).setCellValue(taskVo.getTaskStartDate());
        	row.createCell((short)8 ).setCellValue(taskVo.getTaskCompletionDate());
        	row.createCell((short)9 ).setCellValue(taskVo.getStatus());
        	row.createCell((short)10 ).setCellValue(taskVo.getPriority());
        	row.createCell((short)11 ).setCellValue(taskVo.getCounter());
        	
        	if(taskVo.getDeveloperRemaraks()!=null)
        	row.createCell((short)12 ).setCellValue(taskVo.getDeveloperRemaraks().replaceAll("[\\t\\n\\r]"," "));
        	
        	if(taskVo.getAssignerRemaraks()!=null)
        	row.createCell((short)13 ).setCellValue(taskVo.getAssignerRemaraks().replaceAll("[\\t\\n\\r]"," "));
        	
        	if(taskVo.getReviewerRemaraks()!=null)
        	row.createCell((short)14 ).setCellValue(taskVo.getReviewerRemaraks().replaceAll("[\\t\\n\\r]"," "));
        	
        	
        }
        for(int i=0;i<row.getLastCellNum();i++){
        	 worksheet.autoSizeColumn((short)i);
        }
       
        
        FileOutputStream out;
		try {
			HttpServletResponse response =((HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse());
			response.setContentType("application/xls");
			response.addHeader("Content-disposition", "attachment;filename=TaskList"+System.currentTimeMillis()+".xls");
			
			workbook.write(response.getOutputStream());
            FacesContext.getCurrentInstance().responseComplete();
            
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
        
		return "";
	}
	
	public void changeExpectedTime(){
		
		Calendar StartDateCalendar=Calendar.getInstance();
		StartDateCalendar.setTime(taskStartDate);
		StartDateCalendar.add(Calendar.HOUR, 2);
		taskCompletionExpectedDate=StartDateCalendar.getTime();
		
	}

	/**
	 * @return the assineeRemarksList
	 */
	public ArrayList<RemarksVo> getAssineeRemarksList() {
		return assineeRemarksList;
	}

	/**
	 * @return the assinerRemarksList
	 */
	public ArrayList<RemarksVo> getAssinerRemarksList() {
		return assinerRemarksList;
	}

	/**
	 * @param assinerRemarksList the assinerRemarksList to set
	 */
	public void setAssinerRemarksList(ArrayList<RemarksVo> assinerRemarksList) {
		this.assinerRemarksList = assinerRemarksList;
	}

	/**
	 * @return the reviewerRemarksList
	 */
	public ArrayList<RemarksVo> getReviewerRemarksList() {
		return reviewerRemarksList;
	}

	/**
	 * @param reviewerRemarksList the reviewerRemarksList to set
	 */
	public void setReviewerRemarksList(ArrayList<RemarksVo> reviewerRemarksList) {
		this.reviewerRemarksList = reviewerRemarksList;
	}

	/**
	 * @param assineeRemarksList the assineeRemarksList to set
	 */
	public void setAssineeRemarksList(ArrayList<RemarksVo> assineeRemarksList) {
		this.assineeRemarksList = assineeRemarksList;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	
	
	
}