package com.bursys.baaja.basis.constants;

import java.util.Calendar;
import java.util.Date;

/**
 * This is the misc. constants defined such as name of the Castor parameters
 * being read from the web.xml in the startup servlet
 * 
 *
 @author JosephBrian

 */
public interface MiscConstants {
	public static final String BAAJA_VERSION_NUM = "5.0.1";

	public static final String USER_KEY = "user";

	public static final String UNAUTHORIZED_PAGE_KEY = "unauthorized.url";

	public static final String LOGON_PAGE_KEY = "logon.url";

	public static final String APPLICATION_KEY = "current.app.id";

	public static final String JDO_NAME = "JDO";

	public static final String AUTHENTICATOR_KEY = "baaja.authenticator";

	/**
	 * This is the key for storing the current menu a user is on inside of the
	 * user session.
	 */
	public static final String SESSION_CURRENT_MENU_ID_KEY = "currMenuId";

	public static final String REQUEST_UNAUTHORIZED = "request.unauthorized";

	/**
	 * these property files can be placed anywhere in the classpath, preferrably
	 * in the WEB-INF/classes directory.
	 */
	public static final String APPLICATION_PROPERTIES_FILE = "application.properties";

	public static final String BAAJA_PROPERTIES_FILE = "baaja.properties";

	public static final String BAAJA_PROPERTIES_DB_DS = "baaja.properties.db.ds";

	public static final String BAAJA_PROPERTIES_DB_SQL = "baaja.properties.db.sql";

	/**
	 * this is the key for loading config for roles that can be added
	 * automatically for a user upon logon
	 */

	public static final String AUTH_AUTO_USER_ROLES = "auth.auto.user.roles";

	/**
	 * these are constants for the remote look up for ejb container context
	 */
	public static final String EJB_CONTEXT_PROVIDER_URL = "ejb.context.provider.url";

	public static final String EJB_CONTEXT_FACTORY = "ejb.context.factory";

	public static final String EJB_CONTEXT_USER = "ejb.context.user";

	public static final String EJB_CONTEXT_PWD = "ejb.context.pwd";

	public static final int EJB_CONTEXT = 1;

	/**
	 * these are constants for the remote look up for ejb container context
	 */
	public static final String JNDI_CONTEXT_PROVIDER_URL = "jndi.context.provider.url";

	public static final String JNDI_CONTEXT_FACTORY = "jndi.context.factory";

	public static final String JNDI_CONTEXT_USER = "jndi.context.user";

	public static final String JNDI_CONTEXT_PWD = "jndi.context.pwd";

	public static final int JNDI_CONTEXT = 1;

	/**
	 * constants for remote look up for the web container context
	 */
	public static final String WEB_CONTEXT_PROVIDER_URL = "web.context.provider.url";

	public static final String WEB_CONTEXT_FACTORY = "web.context.factory";

	public static final String WEB_CONTEXT_USER = "web.context.user";

	public static final String WEB_CONTEXT_PWD = "web.context.pwd";

	public static final int WEB_CONTEXT = 0;

	public static final String EMAIL_DOMAIN = "email.domain";

	//
	public static final String DATE_FORMAT = "date.format";

	public static final String TIMESTAMP_FORMAT = "timestamp.format";

	public static final String MONEY_FORMAT = "money.format";

	public static final String DECIMAL_FORMAT = "decimal.format";

	public static final String MONEY_FORMAT_3DIGIT = "money.format.3digit";

	public static final String MONEY_FORM = "money.form";

	public static final String DATA_SOURCE_NAME = "data.source.name";

	public static final String ENC_PARA_KEY = "enc.paraphrase.key";

	public static final String CACHE_TIMEOUT = "cache.timeout";

	public static final String PASS_INFO_ENC = "pass.info.enc";

	public static final String PASS_INFO_ENC_DES = "com.bursys.baaja.basis.encryption";
	
	public static final String JASPER_REPORT_DIR = "jasper.report.dir";

	public static final String JASPER_REPORT_DIR_ABS = "jasper.report.dir.abs";

	public static final String JASPER_VIRTUAL_DIR = "jasper.virtual.dir";

	public static final String REPORT_DATASOURCE = "report.datasource";

	public static final String JASPER_VIRTUAL_MAX_SIZE = "jasper.virtual.max.size";

	public static final String BLANK = "";

	public static final Double DOUBLE = new Double(0.0000);

	public static final Integer INTEGER_ZERO = new Integer(0);

	public static final Integer INTEGER_NON_ZERO = new Integer(1);

	public static final Boolean BOOLEAN_FALSE = Boolean.FALSE;

	public static final Boolean BOOLEAN_TRUE = Boolean.TRUE;

	// Pagination Constants
	public static final int PAGE_SIZE = 100;

	public static final Integer TRANSFERID_DEFAULT = new Integer(0);

	// Batch size constant

	public static final String BATCH_SIZE = "Batch_Size";

	// BAAJA application event LOGGER
	public static final Integer BAAJA_LOG_WARN = 1;

	public static final Integer BAAJA_LOG_STATUS = 2;

	public static final Integer BAAJA_LOG_ERROR = 3;

	public static final Integer BAAJA_LOG_INFO = 4;

	public static final Integer BAAJA_LOG_AUDIT = 5;

	public static final Integer BAAJA_LOG_ALL = 6;

	//
	public static final Integer BAAJA_LOG_AREA_TRND = 1;

	//Constants for Base Config Mail Notification System
	
	public static final String MAIL_TRANS_PROTOCOL = "mail.transport.protocol";
	
	public static final String MAIL_TRANS_HOST = "mail.smtp.host";
	
	public static final String MAIL_TRANS_PORT = "mail.smtp.port";
	
	public static final String MAIL_TRANS_USER = "mail.user";
	
	public static final String MAIL_TRANS_PASSWORD = "mail.password";
	
	public static final String MAIL_SMTP = "SMTP";
	
	public static final String MAILDAT_CUSTOMER_DIRECTORY = "mailDat.customer.directory";
	
	public static final String TIMESTAMP_FORMAT_DIRECTORY = "timestamp.directory.format";
	
    public static Boolean isDSTOn = Calendar.getInstance().getTimeZone().inDaylightTime(new Date());
    
    public static final Integer DOC_ECR_TYPE_ID = 10;
    
    public static final Integer DOC_ECO_TYPE_ID = 20;
    
    public static final Integer DOC_CREATE_ACTION_ID = 1;
    
    public static final Integer DOC_REVIEW_ACTION_ID = 4;
    
    public static final Integer DOC_APPROVE_ACTION_ID = 5;
    
    public static final Integer APPROVER_ROLE_ID = 10;
    
    public static final Integer REVIEWER_ROLE_ID = 20;
    
    public static final Integer PRILIMINARY = 3;
    
    public static final Integer AUDIT_CREATE_ID = 1;
    
    public static final Integer DOC_PART_TYPE_ID = 40;
    
    public static final Integer DOC_UPDATE_ACTION_ID = 2;
    
    public static final String ECM_ECR_W = "W";
    public static final String ECM_ECO_W = "W";
    
    

	public static final Integer ECR_STATUS_PROCESSED = 1;
	public static final Integer ECR_STATUS_ACTIVE = 2;
	public static final Integer ECR_STATUS_PRELIMINARY = 3;
	public static final Integer ECR_STATUS_CANCELLED = 4;

	public static final Integer PART_STATUS_RELEASED = 1;
	public static final Integer PART_STATUS_PRELIMINARY = 2;
	public static final Integer PART_STATUS_ACTIVE = 3;
	public static final Integer PART_STATUS_OBSOLETE = 4;
	public static final Integer PART_STATUS_NOT_AVAILABLE = 5;
	
	
    
}