package com.bursys.baaja.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bursys.baaja.basis.exception.BaajaException;

public class ServiceLookUp {
    private static final Log log = LogFactory.getLog(ServiceLookUp.class);	

    public static Object lookupService(String jndiName) throws NamingException {
        Object svc = null;
        InitialContext ctx = new InitialContext();
        svc = ctx.lookup(jndiName);
        return svc;
    }

    public static Object getLSBByName(String ejbName) throws BaajaException {
    	String str = "ecmsserver/" + ejbName + "/local";
    	Object obj = null;
    	try {
    		obj = lookupService(str);
        } catch (NamingException e){
            log.error("Naming exception occured while looking for an object from JNDI :: " + e.getMessage());
            throw new BaajaException("Naming exception occured while looking for an object from JNDI :: " + e.getMessage());
        }
    	return obj;
    }
    
}
