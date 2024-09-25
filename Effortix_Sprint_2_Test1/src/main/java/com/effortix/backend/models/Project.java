package com.effortix.backend.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Project") // Maps to the 'Project' table in the database
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "pId", nullable = false, unique = true) // Defining column attributes
	private Long pId;
	@Column(name = "pName", nullable = false, length = 200)
	private String pName;
	@Column(name = "pDetails", length = 5000)
	private String pDetails;

	private int pFlag;
	private String pPriority;
	@Temporal(TemporalType.DATE)
	@Column(name = "pStartDate")
	private Date pStartDate;
	@Temporal(TemporalType.DATE)
	@Column(name = "pEndDate")
	private Date pEndDate;
	private Long pteamId;
	@Column(name = "pSkills", length = 5000)
	private String pSkills;
	@Column(name = "pConcepts", length = 5000)
	private String pConcepts;

	public Project() {
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public String getpDetails() {
		return pDetails;
	}

	public void setpDetails(String pDetails) {
		this.pDetails = pDetails;
	}

	public int getpFlag() {
		return pFlag;
	}

	public void setpFlag(int pFlag) {
		this.pFlag = pFlag;
	}

	public String getpPriority() {
		return pPriority;
	}

	public void setpPriority(String pPriority) {
		this.pPriority = pPriority;
	}

	public Date getpStartDate() {
		return pStartDate;
	}

	public void setpStartDate(Date pStartDate) {
		this.pStartDate = pStartDate;
	}

	public Date getpEndDate() {
		return pEndDate;
	}

	public void setpEndDate(Date pEndDate) {
		this.pEndDate = pEndDate;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public Long getPteamId() {
		return pteamId;
	}

	public void setPteamId(Long pteamId) {
		this.pteamId = pteamId;
	}

	public String getpSkills() {
		return pSkills;
	}

	public void setpSkills(String pSkills) {
		this.pSkills = pSkills;
	}

	public String getpConcepts() {
		return pConcepts;
	}

	public void setpConcepts(String pConcepts) {
		this.pConcepts = pConcepts;
	}

}
