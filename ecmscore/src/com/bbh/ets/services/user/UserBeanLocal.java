package com.bbh.ets.services.user;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import com.bursys.baaja.basis.exception.BaajaException;
/******
*************
@JosephBrian
*************
*/
@Local
public interface UserBeanLocal {
	public List getAllObjectFrom(String tableName) throws BaajaException;
	public String passwordEncrypt(String password) throws BaajaException;
	public ArrayList getResultsetList(String customQuery) throws BaajaException;
	

}
