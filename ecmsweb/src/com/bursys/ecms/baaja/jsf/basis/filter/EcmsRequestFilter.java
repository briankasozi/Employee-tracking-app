package com.bursys.ecms.baaja.jsf.basis.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bbh.ets.bo.UserBo;

public class EcmsRequestFilter implements Filter{
	
	private static Log log = LogFactory.getLog(EcmsRequestFilter.class);
	
	private static final String LOGIN_PATH = "/logOn/logOn.jsf";
	FilterConfig fc;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		fc = filterConfig;
		
	}
	
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse; 
		
		HttpSession session = request.getSession(false);
		
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		if((!uri.contains("logOn.jsf")) && (session == null || session.getAttribute("loggedEtsUser")==null )){
			log.info("Authentication required on ES Server......");
			response.sendRedirect(contextPath+LOGIN_PATH);//Redirect to login page if user try to access private ETS pages except login  page
		}else if(session != null && session.getAttribute("loggedEtsUser")!=null ){
		
			UserBo userBo=(UserBo)session.getAttribute("loggedEtsUser");
			if(
					!userBo.getRoleName().equalsIgnoreCase("Administrator") 
					
					&& 
					
					(uri.contains("showAllEmployee.jsf")  
					|| uri.contains("addEmployee.jsf") 
					|| uri.contains("updateEmployee.jsf") 
					|| uri.contains("showAllEmployee.jsf") 
					|| uri.contains("userList.jsf") 
					|| uri.contains("userCreation.jsf")  
					|| uri.contains("userEdition.jsf") 
					|| uri.contains("project.jsf") 
					|| uri.contains("projectHome.jsf"))
					){
				
				session.invalidate();
				log.info("only Administrator Access......");
				response.sendRedirect(contextPath+LOGIN_PATH);
			}else{
				filterChain.doFilter(request, response);
			}
		}else{
			filterChain.doFilter(request, response);
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	

	
	
	

}
