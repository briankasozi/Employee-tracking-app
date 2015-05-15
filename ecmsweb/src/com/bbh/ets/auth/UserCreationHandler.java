package com.bbh.ets.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import com.bbh.ets.bo.EtsEmployeeVo;
import com.bbh.ets.bo.EtsRole;
import com.bbh.ets.bo.UserBo;
import com.bbh.ets.bo.UserListVo;
import com.bbh.ets.dao.EtsServiceDao;
import com.bbh.ets.services.user.UserBean;
import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.ets.baaja.jsf.basis.BaseHandler;


@ManagedBean(name = "userCreationHandler")
@RequestScoped
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserCreationHandler extends BaseHandler {   

	
	public String userLoginId;
	public String currentPassword;
	public String passwordRetypeCheck;
    public String encryptedPassword;
	private Integer userId;
	public String passwdHiddenEdit;
	private Integer baseContId;

	public String firstName;
	public String middleName;
	public String lastName;
	public String salutationNameSel = "";
	public String salutationNameDrp;
	public Vector baseRoleItem;
	private ArrayList<EtsRole> baseRoleItemList;
	public Integer roleId;
	public boolean checked;
	public boolean enabledcheck;
	public String description;
	private Integer etsContactNumber;
	private Integer etsUserId;
	private Integer newUserId;
	private Integer rollId;
	private String encryptedPass;
	UserBean userBeanObj=new UserBean();
	UserBo userBo=new UserBo();
	List<EtsRole> baseRoleList = new ArrayList<EtsRole>();
	EtsServiceDao etsServiceDao = new EtsServiceDao();
	
	private Integer employeeId;
	private List employeeList;
	private String empDesignation;
	private Boolean enableprev;
	
	private ArrayList<UserListVo> userList;
	private ArrayList<EtsEmployeeVo> empList=new ArrayList<EtsEmployeeVo>();
	public String userCreation() throws Exception {
		
		userId = null;
		roleId = null;
		
		return "user_creation";
	}
	
	public UserCreationHandler() throws BaajaException
	{

	}

	public Vector getBaseRoleItem() throws BaajaException {

		try {

			if (baseRoleItem == null) {
				baseRoleItem = new Vector();
				EtsServiceDao ecmsServiceDao = new EtsServiceDao();
				ArrayList<EtsRole> role = ecmsServiceDao.getUserRole();
				System.out.println("size---------->" + role.size());
				for (int i = 0; i < role.size(); i++) {

					UserCreationHandler userCreationHandler = new UserCreationHandler();
					EtsRole userbaserole = (EtsRole) role.get(i);
					userCreationHandler.setChecked(false);
					userCreationHandler.setRoleId(userbaserole.getRoleId());
					userCreationHandler.setDescription(userbaserole
							.getDescription());
					System.out.println("Description---------->"
							+ userbaserole.getDescription());
					baseRoleItem.add(userCreationHandler);
				}
				
			}
		} catch (Exception e) {
			addErrorMessage("Exception occur in getting Base Role Item");
			
		}

		return baseRoleItem;
	}

	public String saveUser() {
		String desc = null;
		
		try {
			if(!(currentPassword.equals(passwordRetypeCheck))){
				addErrorMessage("Password and ReType Password do not match");
				return "";
			}
			for(int i=0;i<baseRoleItemList.size();i++)
			{
				if(baseRoleItemList.get(i).getChecked()==true)
				{
					desc=baseRoleItemList.get(i).getDescription();
					rollId=etsServiceDao.getRoleId(desc);
					
				
				}
			}
		/*	if(desc.equals("Administrator")){
				addErrorMessage("There can be only one Admin");
				return "";
			}*/
			baseContId=etsServiceDao.setContactInfo(firstName,middleName,lastName,salutationNameSel);
			encryptedPass = userBeanObj.passwordEncrypt(currentPassword);
			newUserId=etsServiceDao.setUserCredentials(employeeId,baseContId,encryptedPass,enabledcheck,enableprev);
			etsServiceDao.setUserLoginRole(newUserId,rollId);
			addInfoMessage("New User Created Successfully");
		}
				
	 catch (Exception e) {
		 addErrorMessage("Exception occur in saving user");
		    	
	   	 }	
		return "user_list";
	}
	
	public String updateUser() {
		ArrayList<Integer> userContactList = new ArrayList<Integer>();
		Integer userId;
		String desc = null;
		try {
			if ((!currentPassword.isEmpty() && !currentPassword.isEmpty())
					&& !(currentPassword.equals(passwordRetypeCheck))) {
				addErrorMessage("Password and Retype Password do not match");
				return "";
			}
			for (int i = 0; i < baseRoleItemList.size(); i++) {
				if (baseRoleItemList.get(i).getChecked() == true) {
					desc = baseRoleItemList.get(i).getDescription();
					try {
						rollId = etsServiceDao.getRoleId(desc);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			/*
			 * if(desc.equals("Administrator")){
			 * addErrorMessage("There can be only one Admin"); return ""; }
			 */
			encryptedPass = userBeanObj.passwordEncrypt(currentPassword);
			userContactList = etsServiceDao.updateUserCredentials(userLoginId,
					encryptedPass, enabledcheck, enableprev);
			baseContId = userContactList.get(0);
			userId = userContactList.get(1);
			etsServiceDao.updateUserContact(firstName, middleName, lastName,
					salutationNameSel, baseContId);
			try {
				etsServiceDao.updateUserRole(userId, rollId);
				addInfoMessage("User Updated Successfully");
			} catch (Exception e) {
				addErrorMessage("Error Occured while updating the User Roles");
				return "";
			}

		} catch (Exception e) {
			addErrorMessage("Error Occured while updating the User");
			return "";
		}
		return "user_list";
	}
	
	public String editUser(UserListVo uservo)
	{

		userId = uservo.getUserid();
		
		userLoginId=uservo.getUserLoginId();
		firstName=uservo.getFirstName();
		middleName=uservo.getMiddleName();
		lastName=uservo.getLastName();
		enabledcheck=uservo.getEnabled();
		roleId = uservo.getRoleId();
		salutationNameSel=uservo.getSalutation();
		enableprev=uservo.getPrev_enable();
		return "user_edition";
		
	}

	public String cancelUser() {

		return "user_list";
	}
	
	public String userListPage()
	{
		return "user_creation";
		
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getPasswordRetypeCheck() {
		return passwordRetypeCheck;
	}

	public void setPasswordRetypeCheck(String passwordRetypeCheck) {
		this.passwordRetypeCheck = passwordRetypeCheck;
	}



	public String getPasswdHiddenEdit() {
		return passwdHiddenEdit;
	}

	public void setPasswdHiddenEdit(String passwdHiddenEdit) {
		this.passwdHiddenEdit = passwdHiddenEdit;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSalutationNameSel() {
		return salutationNameSel;
	}

	public void setSalutationNameSel(String salutationNameSel) {
		this.salutationNameSel = salutationNameSel;
	}

	public String getSalutationNameDrp() {
		return salutationNameDrp;
	}

	public void setSalutationNameDrp(String salutationNameDrp) {
		this.salutationNameDrp = salutationNameDrp;
	}

	public List<EtsRole> getBaseRoleList() {
		return baseRoleList;
	}

	public void setBaseRoleList(List<EtsRole> baseRoleList) {
		this.baseRoleList = baseRoleList;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<EtsRole> getBaseRoleItemList() throws BaajaException {
		baseRoleItemList= etsServiceDao.getUserRole();
		if(roleId!=null && roleId > 0){
			for(int i = 0;i<baseRoleItemList.size();i++){
			if(baseRoleItemList.get(i).getRoleId() == roleId){
				baseRoleItemList.get(i).setChecked(true);
			 }
		}
	}
		return baseRoleItemList;
	}

	public void setBaseRoleItemList(ArrayList<EtsRole> baseRoleItemList) {
		this.baseRoleItemList = baseRoleItemList;
	}

	public void setBaseRoleItem(Vector baseRoleItem) {
		this.baseRoleItem = baseRoleItem;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public boolean isEnabledcheck() {
		return enabledcheck;
	}

	public void setEnabledcheck(boolean enabledcheck) {
		this.enabledcheck = enabledcheck;
	}

	public Integer getEcmContactNumber() {
		return etsContactNumber;
	}

	public void setEcmContactNumber(Integer etsContactNumber) {
		this.etsContactNumber = etsContactNumber;
	}

	public Integer getEcmUserId() {
		return etsUserId;
	}

	public void setEcmUserId(Integer etsUserId) {
		this.etsUserId = etsUserId;
	}

	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public String getEncryptedPassword() {
		return encryptedPassword;
	}


	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}


	public ArrayList<UserListVo> getUserList() throws BaajaException {
		
		if(userList==null || userList.isEmpty()){
			userList=etsServiceDao.getUserList();
			
			Collections.sort(userList, new Comparator<UserListVo>() {
				public int compare(UserListVo sItem1, UserListVo sItem2) {
					return (sItem1.getUserLoginId().compareToIgnoreCase(sItem2.getUserLoginId()));
				}
			}); 
		}
		return userList;
	}


	public void setUserList(ArrayList<UserListVo> userList) {
		this.userList = userList;
	}
	public ArrayList<EtsEmployeeVo> getEmpList() throws BaajaException {
		
		empList=etsServiceDao.getEmpList();
		
		
		return empList;
	}

	public void setEmpList(ArrayList<EtsEmployeeVo> empList) {
		this.empList = empList;
	}
	
	//pawan changes
	
public List getEmployeeList() {
		
		List selectItems = new ArrayList<SelectItem>();
		HashMap<Integer, String> list = etsServiceDao.getEmployeeList();
		for (Iterator iter = list.entrySet().iterator(); iter.hasNext();) {
			Map.Entry pairs = (Map.Entry) iter.next();
			selectItems.add(new SelectItem((Integer) pairs.getKey(),(String) pairs.getValue()));
		}
		return selectItems;
	}

	public void setEmployeeList(List employeeList) {
		this.employeeList = employeeList;
	}
	
	public void changeEmployeeList(AjaxBehaviorEvent event) {

		HtmlSelectOneMenu choice = (HtmlSelectOneMenu) event.getComponent();
		Integer selectedEmpID = (Integer) choice.getValue();

		empDesignation = etsServiceDao.getDesignation(selectedEmpID);

	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmpDesignation() {
		return empDesignation;
	}

	public void setEmpDesignation(String empDesignation) {
		this.empDesignation = empDesignation;
	}

	public Boolean getEnableprev() {
		return enableprev;
	}

	public void setEnableprev(Boolean enableprev) {
		this.enableprev = enableprev;
	}

}
