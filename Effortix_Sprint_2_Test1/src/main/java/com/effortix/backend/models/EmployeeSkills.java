package com.effortix.backend.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "employee_skills")
public class EmployeeSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "esID")
    private Long esID;

    @Column(name = "EId", nullable = false)
    private Long EId;

   
    public EmployeeSkills() {
	}

	@Column(name = "skills", length = 600000)
    private String skills;

    @Column(name = "skillsDetail", length = 600000)
    private String skills_detail;

	public Long getEsID() {
		return esID;
	}

	public void setEsID(Long esID) {
		this.esID = esID;
	}

	public Long getEId() {
		return EId;
	}

	public void setEId(Long eId) {
		EId = eId;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getSkills_detail() {
		return skills_detail;
	}

	public void setSkills_detail(String skills_detail) {
		this.skills_detail = skills_detail;
	}


    
}