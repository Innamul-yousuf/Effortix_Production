package com.effortix.backend.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Employee") // Maps to the 'employees' table in the database
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "eId", nullable = false, unique = true) // Defining column attributes
	private Long eId;
	
	@Column(name = "eName", nullable = false, length = 100)
	private String eName;
	
	@Column(name = "ePassword", nullable = false)
	private String ePassword;
	
	@Column(name = "eEmail", nullable = false)
	private String eEmail;
	
	private int eFlag;
	private String eRole;
	
	 @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date eJoinDate;
	private String eContact;
	@Column(name = "eDesignation")
	private String eDesignation;
	
	private Boolean eInBench;
	private Boolean eISLead;
	
	
	
	public Boolean geteInBench() {
		return eInBench;
	}



	public void seteInBench(Boolean eInBench) {
		this.eInBench = eInBench;
	}



	public Boolean geteISLead() {
		return eISLead;
	}



	public void seteISLead(Boolean eISLead) {
		this.eISLead = eISLead;
	}



	public Employee() {
	}

	

	public Long geteId() {
		return eId;
	}



	public void seteId(Long eId) {
		this.eId = eId;
	}



	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String getePassword() {
		return ePassword;
	}

	public void setePassword(String ePassword) {
		this.ePassword = ePassword;
	}

	public String geteEmail() {
		return eEmail;
	}

	public void seteEmail(String eEmail) {
		this.eEmail = eEmail;
	}

	public int geteFlag() {
		return eFlag;
	}

	public void seteFlag(int eFlag) {
		this.eFlag = eFlag;
	}

	public String geteRole() {
		return eRole;
	}

	public void seteRole(String eRole) {
		this.eRole = eRole;
	}

	public Date geteJoinDate() {
		return eJoinDate;
	}

	public void seteJoinDate(Date eJoinDate) {
		this.eJoinDate = eJoinDate;
	}

	public String geteContact() {
		return eContact;
	}

	public void seteContact(String eContact) {
		this.eContact = eContact;
	}

	public String geteDesignation() {
		return eDesignation;
	}

	public void seteDesignation(String eDesignation) {
		this.eDesignation = eDesignation;
	}



	


	

	

}
