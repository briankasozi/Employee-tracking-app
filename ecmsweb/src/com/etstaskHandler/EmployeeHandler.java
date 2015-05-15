package com.etstaskHandler;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.bbh.ets.bo.EtsEmployeeVo;
import com.bbh.ets.bo.UserListVo;
import com.bbh.ets.dao.EtsServiceDao;
import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.ets.baaja.jsf.basis.BaseHandler;

public class EmployeeHandler extends BaseHandler  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String employeeName;	
	private String empDesignation;
	private String empContactNo;
	private String empAdd;
	private String empEmail;
	private Date empDOJ;
	private String empCode;
	private int empId;
	private List DepartmentList;
	private Integer[] departments;
	private String experience;
	private String previousEmployeer;
	
	
	
	
	
	
	private ArrayList<EtsEmployeeVo> empList;
	EtsServiceDao etsServiceDao=new EtsServiceDao();
	
	public String addEmployee() throws Exception {
		if(!validate()){
			return "";
		}
			
			if((employeeName!=null && employeeName.length()>1)&&(empDesignation!=null && empDesignation.length()>1)){
				
				etsServiceDao.addNewEmployee(employeeName,empDesignation,empCode,empEmail,empDOJ,empContactNo,empAdd,departments,experience,previousEmployeer);
				addInfoMessage("New Employee Added Successfully");
				return "employee_list";
			}
			else{
				addErrorMessage("Error occured while adding new Employee");
				return "";
			}
		}
	
	public String updateEmployee() throws Exception {
		
		if((employeeName!=null && employeeName.length()>1)&&(empDesignation!=null && empDesignation.length()>1) && empDOJ!=null){
			
			etsServiceDao.updateEmployee(empId,employeeName,empDesignation,empCode,empEmail,empDOJ,empContactNo,empAdd,experience,previousEmployeer);
			
			
			addInfoMessage("Employee Credentials Updated Successfully");
		}
		else{
			addErrorMessage("Error occured while Updating Employee Credentials ");
			return "";
		}
		
		return "employee_list";
	}
	public String editEmployee(EtsEmployeeVo empvo)
	{
		
		employeeName=	empvo.getEmployeeName();
		empDesignation=	empvo.getDesignation();
		empAdd=empvo.getEmpAddress();
		empCode=empvo.getEmpCode();
		empContactNo=empvo.getEmpPhoneNo();
		empEmail=empvo.getEmpEmailID();
		empId=empvo.getEmployeeID();
		experience=empvo.getExperience();
		previousEmployeer=empvo.getPreviousEmployeer();
		
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String date=empvo.getEmpDateofJoining();
		try {
			empDOJ=dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "emp_edition";
		
	}
	
	public ArrayList<EtsEmployeeVo> getEmpList() throws BaajaException {
		
		if(empList==null || empList.isEmpty()){
			
			empList=etsServiceDao.getEmpList();
			
			Collections.sort(empList, new Comparator<EtsEmployeeVo>() {
				public int compare(EtsEmployeeVo sItem1, EtsEmployeeVo sItem2) {
					return (sItem1.getEmployeeName().compareToIgnoreCase(sItem2.getEmployeeName()));
				}
			});
		}
		return empList;
	}

	public void setEmpList(ArrayList<EtsEmployeeVo> empList) {
		this.empList = empList;
	}

		public String getEmployeeName() {
			return employeeName;
		}

		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}

		
		
		public String cancelEmployee(){
			return "cancel_emp_button";
		}

		

		public String getEmpCode() {
			return empCode;
		}

		public void setEmpCode(String empCode) {
			this.empCode = empCode;
		}

		public String getEmpEmail() {
			return empEmail;
		}

		public void setEmpEmail(String empEmail) {
			this.empEmail = empEmail;
		}

		public Date getEmpDOJ() {
			return empDOJ;
		}

		public void setEmpDOJ(Date empDOJ) {
			this.empDOJ = empDOJ;
		}
		public String getEmpContactNo() {
			return empContactNo;
		}

		public void setEmpContactNo(String empContactNo) {
			this.empContactNo = empContactNo;
		}

		public String getEmpAdd() {
			return empAdd;
		}

		public void setEmpAdd(String empAdd) {
			this.empAdd = empAdd;
		}
		public String getEmpDesignation() {
			
			return empDesignation;
		}

		public void setEmpDesignation(String empDesignation) {
			this.empDesignation = empDesignation;
		}

		public int getEmpId() {
			return empId;
		}

		public void setEmpId(int empId) {
			this.empId = empId;
		}

		
		
		public boolean validate(){
			
			
			if(employeeName.equals(null)||employeeName.equals("")){
				addErrorMessage("Employee Name can't be empty");
				return false;
			}else if(departments==null || departments.length==0){
				addErrorMessage("Please Select at least One Department ");
				return false;
			}else if(empDesignation.equals(null)||empDesignation.equals("")){
				addErrorMessage("Employee Designation can't be empty");
				return false;
			}else if(empContactNo.equals(null)||empContactNo.equals("")){
				addErrorMessage("Employee ContactNo. can't be empty");
				return false;
			}else if(empAdd.equals(null)||empAdd.equals("")){
				addErrorMessage("Employee Address can't be empty");
				return false;
			}else if(empEmail.equals(null)||empEmail.equals("")){
				addErrorMessage("Employee Email can't be empty");
				return false;
			}else if(empDOJ.equals(null)||empDOJ.equals("")){
				addErrorMessage("Employee Date Of Joining can't be empty");
				return false;
			}else if(empCode.equals(null)||empCode.equals("")){
				addErrorMessage("Employee Code can't be empty");
				return false;
			}
			
			return true;
		}

		

		public void setDepartmentList(List departmentList) {
			DepartmentList = departmentList;
		}

		public List getDepartmentList() {
			List selectItems = new ArrayList<SelectItem>();
			HashMap<Integer, String> list = etsServiceDao.getDepartmentList();
			for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
				Map.Entry pairs = (Map.Entry) iter.next();
				selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
			}
			
	
			return selectItems;
		}

		public Integer[] getDepartments() {
			return departments;
		}

		public void setDepartments(Integer[] departments) {
			this.departments = departments;
		}

		

		public String getPreviousEmployeer() {
			return previousEmployeer;
		}

		public void setPreviousEmployeer(String previousEmployeer) {
			this.previousEmployeer = previousEmployeer;
		}

		public String getExperience() {
			return experience;
		}

		public void setExperience(String experience) {
			this.experience = experience;
		}

	
		public String cancel(){
			return "employee_list";
		}
		

}
