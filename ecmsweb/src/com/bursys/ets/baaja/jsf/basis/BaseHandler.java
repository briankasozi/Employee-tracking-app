package com.bursys.ets.baaja.jsf.basis;

//import org.apache.myfaces.shared_tomahawk.util.MessageUtils;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.shared_tomahawk.util.MessageUtils;

// import org.apache.myfaces.shared_impl.util.MessageUtils;

/*import com.bursys.baaja.basis.constants.MiscConstants;
import com.bursys.baaja.common.bo.BaseUserLogin;*/

public class BaseHandler implements java.io.Serializable {
	private Long defLongValue = 0L;

	public Long getDefLongValue() {
		return defLongValue;
	}

	public void setDefLongValue(Long defLongValue) {
		this.defLongValue = defLongValue;
	}

	/**
	 * Get servlet context.
	 * 
	 * @return the servlet context
	 */
	public ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
	}

	/**
	 * Store the managed bean inside the session scope.
	 * 
	 * @param beanName
	 *            the name of the managed bean to be stored
	 * @param managedBean
	 *            the managed bean to be stored
	 */
	public void setManagedBeanInSession(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put(beanName, managedBean);
	}

	/**
	 * Get parameter value from request scope.
	 * 
	 * @param name
	 *            the name of the parameter
	 * @return the parameter value
	 */
	public String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(name);
	}

	/**
	 * Get parameter value from request scope.
	 * 
	 * @param name
	 *            the name of the parameter
	 * @return the parameter value
	 */
	public void setRequestParameter(String name, String value) {
		Map hm = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();
		hm.put(name, value);
	}

	/**
	 * Add information message.
	 * 
	 */
	public void addResourceMessage(FacesMessage.Severity sev, String msgId,
			Object[] args) {
		MessageUtils.addMessage(sev, msgId, args);
	}

	public void addResourceInfoMessage(String msgId, Object[] args) {
		MessageUtils.addMessage(FacesMessage.SEVERITY_INFO, msgId, args);
	}

	public void addResourceErrorMessage(String msgId, Object[] args) {
		FacesMessage msg = MessageUtils.getMessage(FacesMessage.SEVERITY_ERROR,
				msgId, args);
		MessageUtils.addMessage(FacesMessage.SEVERITY_ERROR, msgId, args);
	}

	/**
	 * Add information message.
	 * 
	 * @param msg
	 *            the information message
	 */
	public void addInfoMessage(String msg) {
		addInfoMessage(null, msg);
	}

	/**
	 * Add information message to a sepcific client.
	 * 
	 * @param clientId
	 *            the client id
	 * @param msg
	 *            the information message
	 */
	public void addInfoMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_INFO, msg, ""));
	}

	/**
	 * Add information message.
	 * 
	 * @param msg
	 *            the information message
	 */
	public void addWarnMessage(String msg) {
		addWarnMessage(null, msg);
	}

	/**
	 * Add information message to a sepcific client.
	 * 
	 * @param clientId
	 *            the client id
	 * @param msg
	 *            the information message
	 */
	public void addWarnMessage(String clientId, String msg) {

		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_WARN, msg, ""));
	}

	/**
	 * Add error message.
	 * 
	 * @param msg
	 *            the error message
	 */
	public void addErrorMessage(String msg) {
		addErrorMessage(null, msg);
	}

	/**
	 * Add error message to a sepcific client.
	 * 
	 * @param clientId
	 *            the client id
	 * @param msg
	 *            the error message
	 */
	public void addErrorMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
	}

	public Application getApplication() {
		ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		return appFactory.getApplication();
	}

	public HttpServletRequest getServletRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}

	public HttpServletResponse getServletResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance()
				.getExternalContext().getResponse();
	}

	public HttpSession getHttpSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(Boolean.FALSE);
	}

	public Object getObjectFromMap(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestMap().get(key);
	}

/*	public BaseUserLogin getUserFromSession() {
		HttpSession session = getHttpSession();
		if (session.getAttribute(MiscConstants.USER_KEY) != null) {
			return (BaseUserLogin) session.getAttribute(MiscConstants.USER_KEY);
		}
		return null;
	}*/

	public void addErrorMessageForClient(String clientId, String msgId,
			Object[] args) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = MessageUtils.getMessage(context, msgId, args);
		FacesContext.getCurrentInstance().addMessage(
				clientId,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message
						.getSummary(), message.getDetail()));
	}

	/**
	 * @param sourceComponent -
	 *            component to search for parent component.
	 * @param parentComponentId -
	 *            name of the parent component whose child list needs to be
	 *            cleared.
	 * 
	 * Uses the source UIComponent to search for the parent component whose ID
	 * matches the provided ID. If the parent component is found, it's child
	 * list is cleared. This is useful when canceling out of forms and their
	 * component tree needs to be clearned to ensure that they get fresh data
	 * the next time the form is loaded.
	 */
	protected void clearComponentTree(UIComponent sourceComponent,
			String parentComponentId) {
		if (sourceComponent == null || parentComponentId == null
				|| parentComponentId.length() <= 0) {
			return;
		}

		// try to find the parent component, and if found, clear its children.
		UIComponent parentComponent = sourceComponent
				.findComponent(parentComponentId);
		if (parentComponent != null) {
			parentComponent.getChildren().clear();
		}
	}

	/**
	 * @param parentComponentId -
	 *            id of component to clear children from.
	 * 
	 * looks for the component with the provided id in the UIViewRoot and if
	 * found, clears its children.
	 */
	protected void clearComponentTree(String parentComponentId) {
		if (parentComponentId == null || parentComponentId.length() < 1) {
			return;
		}

		FacesContext f = FacesContext.getCurrentInstance();
		UIComponent parentComponent = findComponent(f.getViewRoot(),
				parentComponentId);
		if (parentComponent != null) {
			parentComponent.getChildren().clear();
		}
	}

	/**
	 * @param sectionId -
	 *            ID of the section to check authorization for
	 * @param user -
	 *            user to check authorization on.
	 * @param authCode -
	 *            authority code (B, I, U, D)
	 * @return - true if authorized, false if not.
	 * 
	 * Convenience method for checking user authorization in handlers for action
	 * methods. If the user is not authorized, a message indicating this to them
	 * is added to the error message queue. The calling action handler method is
	 * responsible for ensuring the message is displayed properly on the view
	 * page.
	 */
/*	protected boolean checkUserSectionAuth(BaseUserLogin user,
			String sectionId, String authCode) {
		if (sectionId == null || sectionId.length() < 1) {
			throw new IllegalArgumentException(
					"Null or empty sectionId provided.");
		} else if (user == null) {
			throw new IllegalArgumentException("Null user provided");
		} else if (user.getBaseRoles() == null) {
			throw new IllegalArgumentException(
					"user provided had null role collection");
		} else if (authCode == null || authCode.length() < 1) {
			throw new IllegalArgumentException(
					"null or empty authCode provided.");
		}

//		boolean authorized = Utils.checkUserSectionAuth(user, Long
//				.valueOf(Utils.getSectionId(sectionId)), authCode);
        boolean authorized = true;
		if (!authorized) {
			this
					.addErrorMessage("You are not authorized to perform this action.");
		}

		return authorized;
	}*/

	private UIComponent findComponent(UIComponent comp, String id) {
		UIComponent foundComponent = null;
		if (id == null || id.length() < 1) {
			throw new IllegalArgumentException(
					"parameter \"id\" was null or empty");
		}

		if (comp == null) {
			return null;
		} else if (comp.getId().equals(id)) {
			return comp;
		} else if (comp.getChildCount() < 1) {
			return null;
		} else {
			for (Iterator i = comp.getChildren().iterator(); i.hasNext();) {
				UIComponent child = (UIComponent) i.next();
				foundComponent = findComponent(child, id);
				if (foundComponent != null) {
					break;
				}
			}
		}

		return foundComponent;
	}

	protected boolean checkSectionPresence(String sectionKey) {
		return this.getServletRequest().getParameter(sectionKey) != null;
	}

	/**
	 * 
	 * @param request
	 *         
	 * @return
	 */
/*	protected BaseUserLogin getCurrentUser(HttpServletRequest request) {
		HttpSession session = getHttpSession();
		return (BaseUserLogin) session.getAttribute(MiscConstants.USER_KEY);
	}*/
}
