package com.bbh.ets.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bbh.ets.bo.EtsUserLogin;
import com.bbh.ets.bo.UserBo;
import com.bbh.ets.dao.EtsServiceDao;
import com.bbh.ets.services.user.UserBean;
import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.ets.baaja.jsf.basis.BaseHandler;

@ManagedBean(name = "logOnHandler")
@RequestScoped
public class LogOnHandler extends BaseHandler {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final Log log = LogFactory.getLog(LogOnHandler.class);
	
	
	private String userName;
	private String password;
	public String currentDBPass;
	
	private String encryptedPassword = "";
	private String oldPassword="";
	private String newPassword="";
	private String confirmPassword="";
	private String passwordEncrypted="";
	private String newPasswordEncrypted="";


	
	UserBean userBeanObj=new UserBean();
	EtsServiceDao etsServiceDao =new EtsServiceDao();
	
	UserBo userBo = new UserBo();
	EtsUserLogin etsUserLogin=null;
	
	/*public LogOnHandler() throws Exception {
		try {
			 userBeanObj = (UserBeanLocal) ServiceLookUp.getLSBByName("UserBean");
		} catch (Exception e) {
			log.info("Exception occured while using userBean in logOnHandler constructor ::"+e);
			throw new Exception("Exception occured while using userBean in logOnHandler constructor:: :: " + e.getMessage(), e);
		}
		
	}*/
	
	
	public String logOff() {
		return "log_off";
	}

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	public String userDetails() throws Exception
	{
		String clientIPAddress ;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(true);
		log.info("***************************************");
		log.info("EMPLOYEE TRACKING SYSTEM : ");
		log.info("***************************************");
		
		try {
			if(!validator(session)){
				return "";
			}
			
		} catch (Exception e) {
			log.info("Exception occured in userDetails method of LogOnHandler ::"+e);
			throw new Exception("Exception occured in userDetails method of LogOnHandler :: " + e.getMessage(), e);
		}
		
		try {
			clientIPAddress = getClientIp();
			userBo = etsServiceDao.authenticateEtsUser(userName, password, clientIPAddress, userBo);
		} catch (Exception e) {
			if(e.getMessage().contains("This account is disabled"))
			{
				addErrorMessage("This account is disabled.Please contact your Manager / Supervisor.");
			}
			else
			{
				addErrorMessage(e.getMessage());
			}
			return "";
		}
		
		
		if(userBo == null || userBo.getUserId() == null || userBo.getSessionId() == null){
			addErrorMessage("Error to store information of user");
			return "";
		}
		
		session.setAttribute("loggedEtsUser", userBo);
		
		/*EtsUserLogin user = new EtsUserLogin();
		user = etsServiceDao.authenticateEcmUserLogin(userName, password, clientIPAddress, userBo);
		session.setAttribute(MiscConstants.USER_KEY, user);*/
		
		return "ets_home";
			

	}
	
	
	private Boolean validator(HttpSession session) throws BaajaException {
		Boolean validSuccess = true;
		if (userName == null || userName.trim().length() < 1) {
			addErrorMessage("USER ID cannot be empty");
			validSuccess = false;
		} if  (password == null || password.trim().length() < 1) {
			addErrorMessage("Password cannot be blank");
			validSuccess = false;
		}
		if(userName.contains(" ")){
			addErrorMessage("userId should not contained spaces");
			validSuccess = false;
		}
		if (password.contains(" ")) {
			addErrorMessage("Password should not contained spaces");
			validSuccess = false;
		}
		if(validSuccess){
			encryptedPassword = userBeanObj.passwordEncrypt(password);
			Object loggedUserObj=etsServiceDao.getUserDetails(userName,encryptedPassword);
		
		if(loggedUserObj ==null ){
			addErrorMessage("Login failed :: UserId and Password combination not matched");
			validSuccess=false;
		}
		
		}
		return validSuccess;

	}
	
	private String getClientIp() throws Exception {
		String clientIPAddr = "";
		try {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			clientIPAddr = request.getRemoteAddr();

		} catch (Exception e) {
			log.info("Exception occur while getting IP address of client ::"+ e);
			throw new Exception("Exception occur while getting IP address of client :: " + e.getMessage(), e);
			
		}
		return clientIPAddr;

	}

	
	

	public UserBo getUserBo() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(true);
		if(session.getAttribute("loggedEtsUser") !=null ){
			userBo = ((UserBo) session.getAttribute("loggedEtsUser"));
		}
		
		
		return userBo;
	}


	public void setUserBo(UserBo userBo) {
		this.userBo = userBo;
	}
	
	
	
public String logOffServer() throws BaajaException  {
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(true);
		session.invalidate();
		return "log_off";
	}


public String changePassword()
{
	oldPassword=null;
	newPassword=null;
	confirmPassword=null;
	
	
	return "changePassword";
}


public String updatePassword() throws BaajaException {
		if (!(oldPassword != null && oldPassword.length() > 0)) {
			addErrorMessage("Current Password is Required");
			return "";
		}

		if (!(newPassword != null && newPassword.length() > 0)) {
			addErrorMessage("New Password is Required");
			return "";
		}

		if (!(confirmPassword != null && confirmPassword.length() > 0))

		{
			addErrorMessage("Re-type Password is Required");
			return "";
		}
		
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(true);
		if (session.getAttribute("loggedEtsUser") != null) {
			userBo = ((UserBo) session.getAttribute("loggedEtsUser"));

		}
		try {
			currentDBPass = etsServiceDao.checkCurrentPassword(userBo.getUserName());
		} catch (Exception e) {
			log.error("Exception during fetching Current Password from server");
		}

		passwordEncrypted = userBeanObj.passwordEncrypt(oldPassword);

		if (!(currentDBPass.equals(passwordEncrypted))) {
			addErrorMessage("Current Password is Wrong");
			return "";
		}

		if (!(newPassword.equals(confirmPassword))) {
			addErrorMessage("New Password and Confirm Password do Not Match");
			return "";
		}

		newPasswordEncrypted = userBeanObj.passwordEncrypt(newPassword);

		etsServiceDao.updatePassword(userBo.getUserId(), newPasswordEncrypted);
		addInfoMessage("Password updated sucessfully");

		return "";
}


public String getOldPassword() {
	return oldPassword;
}


public void setOldPassword(String oldPassword) {
	this.oldPassword = oldPassword;
}


public String getNewPassword() {
	return newPassword;
}


public void setNewPassword(String newPassword) {
	this.newPassword = newPassword;
}


public String getConfirmPassword() {
	return confirmPassword;
}


public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
}


public String getPasswordEncrypted() {
	return passwordEncrypted;
}


public void setPasswordEncrypted(String passwordEncrypted) {
	this.passwordEncrypted = passwordEncrypted;
}







	

}
