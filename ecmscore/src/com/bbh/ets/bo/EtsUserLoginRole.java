package com.bbh.ets.bo;

import java.io.Serializable;
import javax.persistence.*;

 
/**
 * The persistent class for the ets_user_login_role database table.
 * 
 */
@Entity
@Table(name="ets_user_login_role")
public class EtsUserLoginRole implements Serializable {
	private static final long serialVersionUID = 1L;
	private EtsUserLoginRoleId id;

    public EtsUserLoginRole() {
    }


	@EmbeddedId
	public EtsUserLoginRoleId getId() {
		return this.id;
	}

	public void setId(EtsUserLoginRoleId id) {
		this.id = id;
	}
	
}