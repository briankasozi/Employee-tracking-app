package com.bursys.service;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceLocator {

    private static final Log log = LogFactory.getLog(ServiceLocator.class);	

    public static Object lookupService(String jndiName) throws NamingException {
        Object svc = null;
        InitialContext ctx = new InitialContext();
        svc = ctx.lookup(jndiName);
        return svc;
    }

  /*  public static Object getLSBByName(String ejbName) {
    	String str = "ecmsserver/" + ejbName + "/local";
    	Object obj = null;
    	try {
    		obj = lookupService(str);
        } catch (NamingException e){
            log.error("Naming exception occured while looking for an object from JNDI :: " + e.getMessage());
        }
    	return obj;
    }
    */
}
