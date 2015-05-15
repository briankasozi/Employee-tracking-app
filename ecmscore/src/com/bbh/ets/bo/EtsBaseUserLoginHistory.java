package com.bbh.ets.bo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

 
/**
 * The persistent class for the ets_base_user_login_history database table.
 * 
 */
@Entity
@Table(name="ets_base_user_login_history")
public class EtsBaseUserLoginHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer sessionId;
	private String ipAddress;
	private Date recDate;
	private Boolean successfullLogin;
	private EtsUserLogin etsUserLogin;

    public EtsBaseUserLoginHistory() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SESSION_ID")
	public Integer getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}


	@Column(name="IP_ADDRESS")
	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="REC_DATE")
	public Date getRecDate() {
		return this.recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}


	@Column(name="SUCCESSFULL_LOGIN")
	public Boolean getSuccessfullLogin() {
		return this.successfullLogin;
	}

	public void setSuccessfullLogin(Boolean successfullLogin) {
		this.successfullLogin = successfullLogin;
	}


	//bi-directional many-to-one association to EtsUserLogin
    @ManyToOne
	@JoinColumn(name="USER_ID")
	public EtsUserLogin getEtsUserLogin() {
		return this.etsUserLogin;
	}

	public void setEtsUserLogin(EtsUserLogin etsUserLogin) {
		this.etsUserLogin = etsUserLogin;
	}
	
}