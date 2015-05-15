package com.bbh.ets.bo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The persistent class for the ecm_user_login database table.
 * 
 */
@Entity
@Table(name = "ets_user_login")
public class EtsUserLogin implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String currentPassword;
	private Boolean enabled;
	private String userLoginId;
	private Set<EtsBaseUserLoginHistory> etsBaseUserLoginHistories;
	private EtsBaseContact etsBaseContact;

	private Set etsRoles = new HashSet(0);

	public EtsUserLogin() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Column(name = "CURRENT_PASSWORD")
	public String getCurrentPassword() {
		return this.currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	@Column(name = "ENABLED")
	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "USER_LOGIN_ID")
	public String getUserLoginId() {
		return this.userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	// bi-directional many-to-one association to EcmBaseUserLoginHistory
	@OneToMany(mappedBy = "etsUserLogin")
	public Set<EtsBaseUserLoginHistory> getEtsBaseUserLoginHistories() {
		return this.etsBaseUserLoginHistories;
	} 

	public void setEtsBaseUserLoginHistories(
			Set<EtsBaseUserLoginHistory> etsBaseUserLoginHistories) {
		this.etsBaseUserLoginHistories = etsBaseUserLoginHistories;
	}

	// bi-directional many-to-one association to EcmBaseContact
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "CONTACT_ID")
	public EtsBaseContact getEtsBaseContact() {
		return this.etsBaseContact;
	}

	public void setEtsBaseContact(EtsBaseContact etsBaseContact) {
		this.etsBaseContact = etsBaseContact;
	}

	@Transient
	public Set getEtsRoles() {
		return etsRoles;
	}

	public void setEtsRoles(Set etsRoles) {
		this.etsRoles = etsRoles;
	}

}