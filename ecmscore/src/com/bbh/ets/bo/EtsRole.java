package com.bbh.ets.bo;

import java.io.Serializable;
import javax.persistence.*; 
import java.util.Set;


/**
 * The persistent class for the ets_role database table.
 * 
 */
@Entity
@Table(name="ets_role")
public class EtsRole implements Serializable {
	private static final long serialVersionUID = 1L;
	
//	private Set<BaseAcl> baseAcls;
	private int roleId;
	private String description;
	private Boolean checked=false;
	

	//bi-directional many-to-one association to EcmDocRole
	
	//private Set<EcmDocRole> ecmDocRoles;

    public EtsRole() {
    }

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ROLE_ID")
	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Column(name="DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*@OneToMany(mappedBy="ecmRole")
	public Set<EcmDocRole> getEcmDocRoles() {
		return this.ecmDocRoles;
	}

	public void setEcmDocRoles(Set<EcmDocRole> ecmDocRoles) {
		this.ecmDocRoles = ecmDocRoles;
	}*/

	/*@Transient
	public Set<BaseAcl> getBaseAcls() {
		return baseAcls;
	}

	public void setBaseAcls(Set<BaseAcl> baseAcls) {
		this.baseAcls = baseAcls;
	}*/

	@Transient
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
}