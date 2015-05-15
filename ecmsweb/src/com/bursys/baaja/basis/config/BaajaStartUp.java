package com.bursys.baaja.basis.config;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bursys.baaja.basis.constants.AppConstants;
import com.bursys.baaja.basis.constants.MiscConstants;
import com.bursys.baaja.basis.utils.BaajaConfig;
import com.bursys.baaja.basis.utils.PropertyFileLoader;


/**
 * This is a startup servlet that gets loaded at startup in the web.xml
 * Any parameters loaded will be setup in the servlet context as well as the
 * System properties space - can be easily accessed by your application from either
 * the Servlet context space if you happen to be running in the web container, or
 * access the properties through the system properties (System.getProperty()).
 * 
 *
 */
public class BaajaStartUp extends HttpServlet {
    private static Log logger = LogFactory.getLog(BaajaStartUp.class);


    public void destroy() {
        logger.debug("Finalizing");
    }

    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        logger.info("******************************************************************************************");
        logger.info("*  Welcome to next generation EMPLOYEE TRACKING SYSTEM    *");
        logger.info("*                                                                                        *");
        logger.info("*     Server Software Version: " + AppConstants.APP_VERSION_BUILD_DATE + 
        		"                                            *");
        logger.info("*                                                                                        *");
        logger.info("******************************************************************************************");
        
        String paramName = null;
        String param = null;

        Enumeration parms = config.getInitParameterNames();
        while (parms.hasMoreElements()) {
            paramName = (String) parms.nextElement();
            param = config.getInitParameter(paramName);
            logger.debug(paramName + "=" + param);

            getServletContext().setAttribute(paramName, param);

        }
     
        BaajaConfig conf = BaajaConfig.getInstance();
        PropertyFileLoader pfl = new PropertyFileLoader();
        pfl.loadProperties(MiscConstants.BAAJA_PROPERTIES_FILE, getServletContext());
        
        logger.info(" ");
        logger.info("******************************************************************************************");        
        logger.info("*           Loading......................................................                *");
        logger.info("*              ........................................                                  *");
        logger.info("*                Version Number: " + MiscConstants.BAAJA_VERSION_NUM + 
        		"                                                   *");
        logger.info("******************************************************************************************");
        logger.info("");
    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
/*        StartupRequestProcessor proc = new StartupRequestProcessor();
        proc.handleAndProcessRequest(request,response);
*/    }
}
