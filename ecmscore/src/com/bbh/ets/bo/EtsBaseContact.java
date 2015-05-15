package com.bbh.ets.bo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the ets_base_contact database table.
 * 
 */
@Entity
@Table(name="ets_base_contact")
public class EtsBaseContact implements Serializable {
	private static final long serialVersionUID = 1L;
	private int contactId;
	private String firstName;
	private String lastName;
	private String middleName;
	private String salutation;
	private Set<EtsUserLogin> etsUserLogins;

    public EtsBaseContact() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CONTACT_ID")
	public int getContactId() {
		return this.contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}


	@Column(name="FIRST_NAME")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	@Column(name="LAST_NAME")
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	@Column(name="MIDDLE_NAME")
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
 

	@Column(name="SALUTATION")
	public String getSalutation() {
		return this.salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}


	//bi-directional many-to-one association to EtsUserLogin
	@OneToMany(mappedBy="etsBaseContact")
	public Set<EtsUserLogin> getEtsUserLogins() {
		return this.etsUserLogins;
	}

	public void setEtsUserLogins(Set<EtsUserLogin> etsUserLogins) {
		this.etsUserLogins = etsUserLogins;
	}
	
}