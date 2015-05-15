package com.bbh.ets.services.user;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bursys.baaja.basis.encryption.EncryptionPolicy;
import com.bursys.baaja.basis.encryption.EncryptionPolicyFactory;
import com.bursys.baaja.basis.exception.BaajaException;


/**
 * Session Bean implementation class UserBean
 @JosephBrian
 */
@Stateless
public class UserBean implements UserBeanLocal {

	private static final Log log = LogFactory.getLog(UserBean.class);
    public   String PASS_INFO_ENC="com.bursys.baaja.basis.encryption.MD5EncryptionPolicy";
	public   String ENC_PARA_KEY="TJrdaTestR";
	
	@PersistenceContext(unitName = "ecmscore")
	private EntityManager em;
	
    public UserBean() {
        // TODO Auto-generated constructor stub
    }

    
    public String passwordEncrypt(String password) throws BaajaException{
		String encryptPass ="";
		try{
			EncryptionPolicy ep = EncryptionPolicyFactory.createEncryptionPolicy(PASS_INFO_ENC, ENC_PARA_KEY);
			encryptPass = ep.encrypt(password);
			
		}catch(BaajaException be){
			log.info("BaajaException occured during encrypting password in userBean::"+be);
			throw new BaajaException("Exception occured during encryption of password in userBean:: :: " + be.getMessage(), be);
		}
		return encryptPass;
	}
	
	public List getAllObjectFrom(String tableName) throws BaajaException {

		ArrayList<Serializable> list = new ArrayList<Serializable>();

		try {
			Query q = em.createQuery("SELECT o FROM " + tableName + " o");
			Collection<Serializable> tableListObj = q.getResultList();
			list.addAll(tableListObj);
		} catch (Exception e) {
			log.info("Exception occured while getting resultSet from custom table::"	+ e);
			throw new BaajaException("Exception occured during fetching records from table "+tableName+" in userBean:: :: " + e.getMessage(), e);
		}

		return list;
	}
	
	
	  public ArrayList getResultsetList(String customQuery) throws BaajaException{
	    	
	    	ArrayList<Serializable> resultSetList = new ArrayList<Serializable>();
	    	
	    	try{
	    		Query q = em.createQuery(customQuery);
	           
				Collection<Serializable> reusultColl = q.getResultList(); 
	            reusultColl.size();
	            resultSetList.addAll(reusultColl);
	    	}catch(Exception e){
	    		log.info("Exception occured while getting resultSetLIst from query "+customQuery+ "::"+e);
	    		throw new BaajaException("Exception occured during getting resultset in userBean:: :: " + e.getMessage(), e);
	    	}
	    	 return resultSetList;
	    }
	
	


	

}
