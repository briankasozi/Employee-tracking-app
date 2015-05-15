package com.bbh.ets.bo;

import java.io.Serializable;
import javax.persistence.*;
 
import org.hibernate.annotations.Cascade;

/**
 * The primary key class for the ets_user_login_role database table.
 * 
 */
@Embeddable
public class EtsUserLoginRoleId implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	private int userId;
	private int roleId;

    public EtsUserLoginRoleId() {
    }

	@Column(name="USER_ID")
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name="ROLE_ID")
	public int getRoleId() {
		return this.roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EtsUserLoginRoleId)) {
			return false;
		}
		EtsUserLoginRoleId castOther = (EtsUserLoginRoleId)other;
		return 
			(this.userId == castOther.userId)
			&& (this.roleId == castOther.roleId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.roleId;
		
		return hash;
    }
}