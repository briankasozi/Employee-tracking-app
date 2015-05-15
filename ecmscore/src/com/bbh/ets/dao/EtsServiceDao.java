package com.bbh.ets.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bbh.ets.bo.EtsBaseUserLoginHistory;
import com.bbh.ets.bo.EtsEmployeeVo;
import com.bbh.ets.bo.EtsRole;
import com.bbh.ets.bo.EtsUserLogin;
import com.bbh.ets.bo.UserBo;
import com.bbh.ets.bo.UserListVo;
import com.bursys.baaja.basis.constants.AppConstants;
import com.bursys.baaja.basis.encryption.EncryptionPolicy;
import com.bursys.baaja.basis.encryption.EncryptionPolicyFactory;
import com.bursys.baaja.basis.exception.BaajaException;
import com.bursys.baaja.basis.service.BaseJdbcDao;
 /**********************
 **********************
 **********************
 @author JosephBrian
 **********************/
public class EtsServiceDao extends BaseJdbcDao {

	private static Log log = LogFactory.getLog(EtsServiceDao.class);
	private static EtsServiceDao instance;

	public static EtsServiceDao getInstance() {
		if (instance == null) {
			instance = new EtsServiceDao();
		}
		return instance;
	}
public Object getUserDetails(String user,String password) throws BaajaException{
	 {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = " SELECT el.* from ets_user_login el where el.user_login_id='"+user+"'";

		EtsUserLogin etsUserLoginBO = new EtsUserLogin();

		try {
			conn = getDbConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				etsUserLoginBO.setUserId(rs.getInt("user_id"));
				etsUserLoginBO.setUserLoginId(rs.getString("user_login_id"));
				etsUserLoginBO.setEnabled(rs.getBoolean("enabled"));
				etsUserLoginBO.setCurrentPassword(rs
						.getString("CURRENT_PASSWORD"));
			}

		} catch (Exception e) {
			throw new BaajaException("error while getUserIdByUSer from server "
					+ e.getMessage());
		} finally {
			sqlCleanUp(conn, stmt, rs);
		}
		return etsUserLoginBO;
	}
}
	
public UserBo authenticateEtsUser(String username, String password,
		String remoteIP, UserBo userBo) throws BaajaException {

	userBo.setUserName(username);

	EtsUserLogin etsUser = new EtsUserLogin();
	EtsBaseUserLoginHistory etsBaseUserLoginHistory = new EtsBaseUserLoginHistory();

	etsBaseUserLoginHistory.setRecDate(new Date());
	etsBaseUserLoginHistory.setSuccessfullLogin(false);
	etsBaseUserLoginHistory.setIpAddress(remoteIP);
	boolean authorized = false;
	try {

		if (username != null && password != null) {
			etsUser = getUserIdByUSer(username.trim());

			if (etsUser != null) {
				etsBaseUserLoginHistory.setEtsUserLogin(etsUser);
			}
			if (etsUser.getUserId() != null && etsUser.getEnabled()) {
				String pass = null;
				EncryptionPolicy ep = 
					EncryptionPolicyFactory.createEncryptionPolicy
					(AppConstants.PASS_INFO_ENC,AppConstants.ENC_PARA_KEY);
				
				pass = ep.encrypt(password);

				if (etsUser.getCurrentPassword() != null
						&& etsUser.getCurrentPassword().equals(pass)) {
					authorized = true;
				}

				if (!authorized) {
					throw new BaajaException(
							"An invalid user id / password combination was entered");
				} else {
					userBo = getEtsRole(userBo);
				}


			} else {
				if (etsUser != null && etsUser.getEnabled() == false) {
					throw new BaajaException(
							"This account is disabled,Please contact your ETS administrator");
				} else {
					throw new BaajaException(
							"Requested user id not found in the ETS application, please contact the administrator");
				}
			}
		}

		else {
			
				throw new BaajaException(
						"An invalid user id / password combination was entered");

		}
		if (authorized) {
			if (etsUser != null) {
				etsBaseUserLoginHistory.setSuccessfullLogin(true);
				saveEtsLoginHistory(etsBaseUserLoginHistory);
				userBo.setUserId(etsUser.getUserId());
			}
		}else {
			if (etsUser != null) {
				etsBaseUserLoginHistory.setSuccessfullLogin(false);
				saveEtsLoginHistory(etsBaseUserLoginHistory);
			}
		}
	} catch (Exception e) {

		if (etsUser != null) {
			etsBaseUserLoginHistory.setSuccessfullLogin(false);
			saveEtsLoginHistory(etsBaseUserLoginHistory);//
		}
		throw new BaajaException("Please Enter Correct Username/Password");

	}

	try {
		Integer lastInsertedSessionId = getLastInsertedSessionId();
		userBo.setSessionId(lastInsertedSessionId);
	} catch (Exception e) {
		throw new BaajaException("Error in fetching session Id:"
				+ e.getMessage());
	}
	
	
	try{
				
		
		if(userBo!=null && userBo.getRoleName()!=null){
			Integer loginEmpI=getLoginEmpID(userBo);
			userBo.setLoginEmpID(loginEmpI);
			userBo.setLoginEmployeeName(getLoggedInUserName(loginEmpI));
			userBo.setLoginEmployeedesignation(getDesignation(loginEmpI));
			userBo.setTaskPrev(getTaskPrev(userBo));   // To Set task Creation Privilege in Session
			
			HashMap<Integer, String> department = new HashMap<Integer, String>();
			department=getDepartmentList(loginEmpI);
			String departments="";
			for (Iterator iter = department.entrySet().iterator(); iter.hasNext();) {
				Map.Entry pairs = (Map.Entry) iter.next();
				departments=departments+(String)pairs.getValue()+" / ";
			}
			userBo.setDepartments(departments.substring(0, departments.length()-2));
		}
	}catch(Exception e){
		throw new BaajaException("Error in fetching Emp Id:"+ e.getMessage());
	}

	return userBo;
}

	private EtsUserLogin getUserIdByUSer(String userLoginId)
			throws BaajaException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = " SELECT * FROM ets_user_login where user_login_id='"
				+ userLoginId + "'";

		EtsUserLogin etsUserLoginBO = new EtsUserLogin();

		try {
			conn = getDbConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				etsUserLoginBO.setUserId(rs.getInt("user_id"));
				etsUserLoginBO.setUserLoginId(rs.getString("user_login_id"));
				etsUserLoginBO.setEnabled(rs.getBoolean("enabled"));
				etsUserLoginBO.setCurrentPassword(rs
						.getString("CURRENT_PASSWORD"));
			}

		} catch (Exception e) {
			throw new BaajaException("error while getUserIdByUSer from server "
					+ e.getMessage());
		} finally {
			sqlCleanUp(conn, stmt, rs);
		}
		return etsUserLoginBO;
	}

	public UserBo getEtsRole(UserBo userBo) throws BaajaException {
		
		String Q = "SELECT er.*  FROM ets_role er , ets_user_login_role eulr,ets_user_login eu"
				+ " where eu.user_login_id='"
				+ userBo.getUserName()
				+ "' and er.role_id=eulr.role_id and eulr.user_id = eu.user_id and er.role_id=eulr.role_id";

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = getDbConnection();
			ps = conn.prepareStatement(Q);
			rs = ps.executeQuery();

			if (rs.next()) {
				userBo.setRoleId(rs.getInt("ROLE_ID"));
				userBo.setRoleName(rs.getString("DESCRIPTION"));// getting role
																// name of the
																// logged user
			}
		} catch (Exception e) {
			throw new BaajaException("Error while getting ETSRole  :: "
					+ e.getMessage(), e);
		} finally {
			sqlCleanUp(conn, ps, rs);
		}
		return userBo;

	}

	private String saveEtsLoginHistory(EtsBaseUserLoginHistory etsLoginHistory)
			throws BaajaException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "INSERT INTO ETS_BASE_USER_LOGIN_HISTORY ( USER_ID,IP_ADDRESS, SUCCESSFULL_LOGIN, REC_DATE) VALUES(?,?,?,?)";

		String status = "failed";
		try {
			conn = getDbConnection();
			System.out
					.println("Connection Created in saveEcmLoginHistory method");
			Integer userId = null;
			pstmt = conn.prepareStatement(sql);
			if (etsLoginHistory.getEtsUserLogin().getUserId() != null) {
				userId = etsLoginHistory.getEtsUserLogin().getUserId();
				pstmt.setInt(1, userId);
			} else {
				pstmt.setNull(1, java.sql.Types.INTEGER);
			}
			pstmt.setString(2, etsLoginHistory.getIpAddress());
			pstmt.setBoolean(3, etsLoginHistory.getSuccessfullLogin());
			pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

			pstmt.execute();
			status = "success";
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaajaException(
					"error while getting saveLoginHistory list from server "
							+ e);
		} finally {
			sqlCleanUp(conn, pstmt, rs);
		}

		return status;

	}

	public Integer getLastInsertedSessionId() throws BaajaException {
		Connection conn = null;
		Statement stmt = null;
		String lastSessionIdQueryString = null;
		ResultSet resultSet = null;
		Integer lastSessionId = null;
		try {
			lastSessionIdQueryString = "SELECT MAX(SESSION_ID) AS LASTSESSIONID FROM  ETS_BASE_USER_LOGIN_HISTORY ";
			conn = getDbConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(lastSessionIdQueryString);
			while (resultSet.next()) {
				lastSessionId = resultSet.getInt("lastSessionId");
			}

		} catch (Exception e) {
			throw new BaajaException("error while  fetching session id :: "
					+ e.getMessage(), e);
		} finally {
			sqlCleanUp(conn, stmt, resultSet);
		}
		return lastSessionId;
	}
	public ArrayList<EtsRole> getUserRole() throws BaajaException
	{
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		String sql = "SELECT * FROM ETS_ROLE";
		ArrayList<EtsRole> userbaserolelist = new ArrayList<EtsRole>();
		Integer size=0;
		try
		{
 			conn = getDbConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            EtsRole userrole=null;
            
            while(rs.next()){
            	userrole=new EtsRole();
            	         
            	userrole.setRoleId(rs.getInt("ROLE_ID"));
            	userrole.setDescription(rs.getString("DESCRIPTION"));
            	userbaserolelist.add(userrole);
            	
            }
            
		}
		catch(Exception e)
		{
			log.info("Exception during fetching roles from server::"+e);
			throw new BaajaException("error while fetching roles from server ::" + e.getMessage());
		}
		finally
		{
			sqlCleanUp(conn, stmt, rs);
			}
		return userbaserolelist;
	}
	public int setContactInfo(String firstName,String midName,String lastName,String Salutation) throws BaajaException{
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		PreparedStatement pstm=null;
		int lastId=0;
		
		try
		{
			conn = getDbConnection();
			String query = "insert into ets_base_contact(first_name,middle_name,last_name,salutation)values('"+firstName+"','"+midName+"','"+lastName+"','"+Salutation+"')";
            pstm = conn.prepareStatement(query);
            pstm.executeUpdate();
            rs = pstm.executeQuery("select last_insert_id() as last_id");
            if(rs.next()){
            lastId = rs.getInt("last_id");
            }
		}
		catch(Exception e)
		{
			log.info("Exception during fetching roles from server::"+e);
			throw new BaajaException("error while fetching roles from server ::" + e.getMessage());
		}
		finally
		{
			sqlCleanUp(conn, stmt, rs);
			}
		return lastId;
		
	}

	public int setUserCredentials(Integer employeeId, int baseContId,
			String encryptedPass, Boolean enabledcheck,Boolean enable_prev) throws BaajaException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstm = null;
		int lastId = 0;
		
		try {
			conn = getDbConnection();
			String query = "insert into ets_user_login (user_login_id,contact_id,current_password,enabled,emp_id,prev_enable) " +
					"SELECT  LOWER(SUBSTRING_INDEX(Emp_Name, ' ', 1))  as Emp_Name,'"+baseContId+ "','"+ encryptedPass + "'," + enabledcheck+","+employeeId+","+enable_prev+" FROM ets_emp_sw  WHERE Emp_ID="+employeeId;
			pstm = conn.prepareStatement(query);
			pstm.executeUpdate();
			rs = pstm.executeQuery("select last_insert_id() as last_id");
			if (rs.next()) {
				lastId = rs.getInt("last_id");
			}
		} catch (Exception e) {
			log.info("Exception during inserting data into ets_user_login::"
					+ e);
			throw new BaajaException(
					"error while inserting data into ets_user_login ::"
							+ e.getMessage());
		} finally {
			sqlCleanUp(conn, stmt, rs);
		}
		return lastId;
	}
	
	public void setUserLoginRole(int userId,int roleid) throws BaajaException{
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		PreparedStatement pstm=null;
		
		try
		{
			conn = getDbConnection();
			String query = "INSERT INTO ETS_USER_LOGIN_ROLE VALUES("+userId +", "+roleid+")";
            pstm = conn.prepareStatement(query);
            pstm.executeUpdate();
        }
		catch(Exception e)
		{
			log.info("Exception during inserting data into ets_user_login::"+e);
			throw new BaajaException("error while inserting data into ets_user_login ::" + e.getMessage());
		}
		finally
		{
			sqlCleanUp(conn, stmt, rs);
			}
	}
	public int getRoleId(String getRoleId) throws BaajaException
	{
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		int rollId=0;
		String sql = "SELECT ROLE_ID FROM ETS_ROLE WHERE DESCRIPTION='"+getRoleId+"'";
		try
		{
 			conn = getDbConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            	rollId=rs.getInt("ROLE_ID");
            }
        }
		catch(Exception e)
		{
			log.info("Exception during fetching Role Id from server::"+e);
			throw new BaajaException("error during fetching Role Id from server ::" + e.getMessage());
		}
		finally
		{
			sqlCleanUp(conn, stmt, rs);
			}
		return rollId;
		
	}
	public ArrayList<UserListVo> getUserList() throws BaajaException {
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		String sql = "SELECT concat(ebc.first_name, '  ',ebc.last_name) as name, ebc.first_name,ebc.last_name,ebc.middle_name," +
				"eul.current_password,ebc.salutation, eul.user_id,eul.user_login_id ,er.description," +
				"eul.enabled,eulr.role_id,eul.prev_enable as prev_enable FROM ets_user_login eul left join ets_base_contact ebc on eul.contact_id=ebc.contact_id left join " +
				"ets_user_login_role eulr on eul.user_id=eulr.user_id left join ets_role er on eulr.role_id=er.role_id group by eul.user_login_id asc  ;";
		
		ArrayList<UserListVo> userlistVo = new ArrayList<UserListVo>();
		
		try
		{
 			conn = getDbConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            UserListVo userList=null;
            
            while(rs.next()){
            	userList=new UserListVo();
            	userList.setName(rs.getString("name"));
            	userList.setRole(rs.getString("description"));
            	userList.setEnabled(rs.getBoolean("enabled"));
            	userList.setUserLoginId(rs.getString("user_login_id"));
            	userList.setUserid(rs.getInt("user_id"));
            	userList.setFirstName(rs.getString("first_name"));
            	userList.setMiddleName(rs.getString("middle_name"));
            	userList.setCurrentPassword(rs.getString("current_password"));
            	userList.setLastName(rs.getString("last_name"));
            	userList.setRoleId(rs.getInt("role_id"));
            	userList.setSalutation(rs.getString("salutation"));
            	userList.setPrev_enable(rs.getBoolean("prev_enable"));
            	userlistVo.add(userList);
            }
            
		}
		catch(Exception e)
		{
			log.info("Exception during fetching roles from server::"+e);
			throw new BaajaException("error while fetching roles from server ::" + e.getMessage());
		}
		finally
		{
			sqlCleanUp(conn, stmt, rs);
			}
		return userlistVo;
	}
	public String getRoleDescription(String loggedName){
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs = null ;
		String desc=null;
		String sql = "select description from ets_role where role_id in(select role_id from ets_user_login_role where user_id in (select user_id from ets_user_login where user_login_id='"+loggedName+"'))";

	try
	{
		conn = getDbConnection();
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(sql);
	    if(rs.next()){
	    	desc=rs.getString("description");	
	    }
	    
	}
		catch(Exception e)
		{
			log.info("Exception during fetching description from DB::"+e);
			
		}
		finally
		{
			sqlCleanUp(conn, stmt, rs);
			}
		return desc;
			}

	
	
	public void updateUserRole(Integer userId,Integer newRoleId) throws BaajaException{
		Connection conn = null ;
		Statement stmt = null ;
		
		String updteQuery = "update ets_user_login_role set role_id = "+newRoleId+" where user_id ="+userId;
		try
		{
 			conn = getDbConnection();
            stmt = conn.createStatement();
            stmt.execute(updteQuery);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sqlCleanUp(conn, stmt, null);
			}
	}	
	
	public ArrayList<Integer> updateUserCredentials(String userLoginId,String encryptedPass,Boolean enabledcheck,Boolean prev_enable){
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs=null;
		ArrayList<Integer> userInfoList=new ArrayList<Integer>();
		String updateQuery=null;
		if(encryptedPass.trim().isEmpty()){
			updateQuery="update ets_user_login set  enabled ="+enabledcheck+", prev_enable="+prev_enable+" where user_login_id='"+userLoginId+"';";
		}else{
			updateQuery="update ets_user_login set  current_password='"+encryptedPass+"', enabled ="+enabledcheck+", prev_enable ="+prev_enable+" where user_login_id='"+userLoginId+"';";
		}
		try
		{
 			conn = getDbConnection();
            stmt = conn.createStatement();
            stmt.execute(updateQuery);
            rs=stmt.executeQuery("select contact_id,user_id from ets.ets_user_login where user_login_id='"+userLoginId+"';");
            if(rs.next()){
            userInfoList.add(rs.getInt("contact_id"));
            userInfoList.add(rs.getInt("user_id"));
            }
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sqlCleanUp(conn, stmt, null);
			}
		return userInfoList;
		
	}	
	public void updateUserContact(String firstName,String midName,String lastName,String Salutation,Integer contactId){
		Connection conn = null ;
		Statement stmt = null ;
		String updateQuery=("update ets_base_contact set first_name='"+firstName+"',last_name='"+lastName+"', middle_name='"+midName+"',salutation='"+Salutation+"' where contact_id="+contactId+";");
	
		try
		{
 			conn = getDbConnection();
            stmt = conn.createStatement();
            stmt.execute(updateQuery);
            
            
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sqlCleanUp(conn, stmt, null);
			}

		
	}
	 public ArrayList<EtsEmployeeVo> getEmpList() throws BaajaException {
			
			
			Connection conn = null ;
			Statement stmt = null ;
			ResultSet rs = null ;
			String sql = "select * from ets_emp_sw";
			
			ArrayList<EtsEmployeeVo> userlistVo = new ArrayList<EtsEmployeeVo>();
			
			try
			{
	 			conn = getDbConnection();
	            stmt = conn.createStatement();
	            rs = stmt.executeQuery(sql);
	            EtsEmployeeVo empList=null;
	            
	            while(rs.next()){
	            	empList=new EtsEmployeeVo();
	            	empList.setEmployeeID(rs.getInt("Emp_ID"));
	            	empList.setEmployeeName(rs.getString("Emp_Name"));
	            	empList.setDesignation(rs.getString("Designation"));
	            	empList.setEmpCode(rs.getString("Emp_Code"));
	            	empList.setEmpEmailID(rs.getString("Emp_Email"));
	            	empList.setEmpDateofJoining(rs.getString("Emp_DOJ"));
	            	empList.setEmpPhoneNo(rs.getString("Emp_PhoneNumber"));
	            	empList.setEmpAddress(rs.getString("Emp_Address"));
	            	empList.setExperience(rs.getString("Experience"));
	            	empList.setPreviousEmployeer(rs.getString("Previous_employeer"));
	            	
	            	userlistVo.add(empList);
	            }
	            
			}
			catch(Exception e)
			{
				log.info("Exception during fetching roles from server::"+e);
				throw new BaajaException("error while fetching roles from server ::" + e.getMessage());
			}
			finally
			{
				sqlCleanUp(conn, stmt, rs);
				}
			return userlistVo;
		}
	 
	 public void addNewEmployee(String empName,String empDesignation,String empCode,String empEmail,Date doj,String empPhoneNo,String empAdd,Integer[] departments,String experience, String PrevEmployeer) throws BaajaException{
			Connection conn=null;
			ResultSet rs=null;
			PreparedStatement psmt=null;
			Statement stmt = null ;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String emp_doj=sdf.format(doj);
			String insertQueryToCreateNewEmployee = "insert into ets_emp_sw (emp_name,designation,emp_code,emp_email,emp_doj,emp_phoneNumber,emp_address,Experience,Previous_employeer)" +
													"values" +
													"('"+empName+"','"+empDesignation+"','"+empCode+"','"+empEmail+"','"+emp_doj+"','"+empPhoneNo+"','"+empAdd+"','"+experience+"','"+PrevEmployeer+"')";
			Integer employeeId=null;
			StringBuffer insertQuerySpecifyEmployeeDepartment=new StringBuffer("insert into ets_emp_sw_department (Emp_ID,DEPARTMENT_ID) values ");
														
			
			
			try
			{
				
				conn = getDbConnection();
				psmt = conn.prepareStatement(insertQueryToCreateNewEmployee, Statement.RETURN_GENERATED_KEYS);
				psmt.executeUpdate();
				rs=psmt.getGeneratedKeys();
				if (rs.next()){
					employeeId=rs.getInt(1);  //get Auto Generated EmployeeID
		        }
				
				for(Integer department:departments){
					insertQuerySpecifyEmployeeDepartment.append("("+employeeId+","+department+"),");
				}
				
				psmt = conn.prepareStatement(insertQuerySpecifyEmployeeDepartment.substring(0, insertQuerySpecifyEmployeeDepartment.toString().length()-1));
				
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
	public void updateEmployee(int empId,String employeeName,String empDesignation,String empCode,String empEmail,Date empDOJ,String empContactNo,String empAdd,String experience,String previousEmployeer){
		Connection conn = null ;
		Statement stmt = null ;
		ResultSet rs=null;
		Integer contactId = null;
		String updateQuery=null;
		String date;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		date=sdf.format(empDOJ);
		updateQuery="update ets_emp_sw " +
				"set  " +
				"emp_name ='"+employeeName+"'," +
				"designation='"+empDesignation+"'," +
				"emp_code='"+empCode+"'," +
				"emp_email='"+empEmail+"'," +
				"emp_doj='"+date+"'," +
				"emp_phoneNumber='"+empContactNo+"'," +
				"emp_address='"+empAdd+"' ," +
				
				"Experience='"+experience+"'," +
				"Previous_employeer='"+previousEmployeer+"' " +
				"where emp_id="+empId+";";
		
		try
		{
 			conn = getDbConnection();
            stmt = conn.createStatement();
            stmt.execute(updateQuery);
            
            
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sqlCleanUp(conn, stmt, null);
			}
			
	}
	private Integer getLoginEmpID(UserBo userBo) throws BaajaException {
		Connection conn = null;
		Statement stmt = null;
		String selectQuery = null;
		ResultSet rs = null;
		Integer loginEmpID = null;
		try {
			selectQuery = "SELECT EMP_ID FROM ETS_USER_LOGIN WHERE USER_ID="+userBo.getUserId();
			conn = getDbConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectQuery);
			while (rs.next()) {
				loginEmpID = rs.getInt("EMP_ID");
			}

		} catch (Exception e) {
			throw new BaajaException("error while  fetching emp id "+ e.getMessage(), e);
		} finally {
			sqlCleanUp(conn, stmt, rs);
		}
		return loginEmpID;
	}
	
	private Boolean getTaskPrev(UserBo userBo) throws BaajaException {
		Connection conn = null;
		Statement stmt = null;
		String selectQuery = null;
		ResultSet rs = null;
		Boolean taskPrev = false;
		try {
			selectQuery = "SELECT prev_enable FROM ETS_USER_LOGIN WHERE USER_ID="+userBo.getUserId();
			conn = getDbConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(selectQuery);
			while (rs.next()) {
				taskPrev = rs.getBoolean("prev_enable");
			}

		} catch (Exception e) {
			throw new BaajaException("error while  fetching prev_enable "+ e.getMessage(), e);
		} finally {
			sqlCleanUp(conn, stmt, rs);
		}
		return taskPrev;
	}
	

	public HashMap<Integer, String> getEmployeeList() {
		HashMap<Integer, String> employeeList = new HashMap<Integer, String>();
		Connection conn = null;
		String query = "SELECT * FROM ets_emp_sw order by Emp_Name asc";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				employeeList.put(rs.getInt("Emp_ID"),rs.getString("Emp_Name").split("\\s")[0].toLowerCase());
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
	

	public String getLoggedInUserName(Integer empID) {
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
				designation=rs.getString("Emp_Name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return designation;

	}

	public boolean validateUserExistence(Integer employeeId) {
		Connection conn = null;
		String query = "SELECT * FROM ets_user_login where emp_id="+employeeId;
		ResultSet rs = null;
		Boolean result=false;
		PreparedStatement pstmt = null;
		try {
			conn = getDbConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				result=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			sqlCleanUp(conn, pstmt, rs);
		}
		return result;

	}
	
	public String checkCurrentPassword(String userName) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String currentPass = null;
		String sql = "select current_password from ets_user_login where user_login_id='"
				+ userName + "'";
		try {
			conn = getDbConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				currentPass = rs.getString("current_password");

			}

		} catch (Exception e) {
			log.error("Exception during fetching Current Password from server::"
					+ e);

		} finally {
			sqlCleanUp(conn, stmt, rs);
		}
		return currentPass;
	}
	
	public void updatePassword(int userId, String newPass)
			throws BaajaException {

		Connection conn = null;
		Statement stmt = null;
		String updateQuery = null;

		updateQuery = "Update  ets_user_login set current_password ='"
				+ newPass + "' where user_id=" + userId + "";
		try {

			conn = getDbConnection();
			stmt = conn.createStatement();
			stmt.execute(updateQuery);

		}

		catch (Exception e) {

			throw new BaajaException(
					"error while  Updating Password  User roles :: "
							+ e.getMessage(), e);

		}

	}
	public HashMap<Integer, String> getDepartmentList() {
		
		HashMap<Integer, String> departmentList = new HashMap<Integer, String>();
		Connection conn = null;
		
		String query = "SELECT DEPARTMENT_ID,DEPARTMENT_NAME FROM ets_department " ;
						
		
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
	
	
}