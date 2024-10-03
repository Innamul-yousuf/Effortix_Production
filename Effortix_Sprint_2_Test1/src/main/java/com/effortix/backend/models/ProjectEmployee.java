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
@Table(name = "project_employee")
public class ProjectEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "peID", nullable = false, unique = true)
    private Long peID; // Primary Key

    @ManyToOne
    @JoinColumn(name = "pID", nullable = false)
    private Project project; // Foreign Key to Project entity

    @ManyToOne
    @JoinColumn(name = "eID", nullable = false)
    private Employee employee; // Foreign Key to Employee entity

    public ProjectEmployee() {}

	public Long getPeID() {
		return peID;
	}

	public void setPeID(Long peID) {
		this.peID = peID;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

   
}