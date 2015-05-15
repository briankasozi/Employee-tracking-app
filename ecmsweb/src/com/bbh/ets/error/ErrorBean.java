package com.bbh.ets.error;

import java.util.Map;  

import javax.enterprise.context.RequestScoped;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;  

import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;  
  
/** 
* 
* @author jospehbrian
*/  
@ManagedBean(name="errorBean")  
@RequestScoped
public class ErrorBean  
{  
   private static final String BR = "n";  
   
   private String stackTrace; 
   
   public ErrorBean(){}
   

public String getStackTrace() {
	
	FacesContext context = FacesContext.getCurrentInstance();  
    Map<String,Object> map = context.getExternalContext().getRequestMap();  
    Throwable throwable = (Throwable) map.get("javax.servlet.error.exception");  
    StringBuilder builder = new StringBuilder();  
    builder.append(throwable.getMessage()).append(BR);  

    for (StackTraceElement element : throwable.getStackTrace())  
    {  
      builder.append(element).append(BR);  
    }  
 
    stackTrace=builder.toString();
    return stackTrace ;  
	
}

public void setStackTrace(String stackTrace) {
	this.stackTrace = stackTrace;
}
  


  
  
}  
