package com.effortix.backend.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


//EmployeeLeads Entity
@Entity
@Table(name = "employee_lead")
public class EmployeeLeads {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 @Column(name = "el_ID", nullable = false, unique = true)
 private Long id; // Primary Key

 @OneToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "employee_id", referencedColumnName = "eId", nullable = false, unique = true)
 private Employee employee; // Reference to Employee entity

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "lead_id", referencedColumnName = "eId", nullable = false)
 private Employee lead; // Reference to Employee entity as a lead

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Employee getEmployee() {
	return employee;
}

public void setEmployee(Employee employee) {
	this.employee = employee;
}

public Employee getLead() {
	return lead;
}

public void setLead(Employee lead) {
	this.lead = lead;
}

 // other fields, if needed

 // Getters and Setters
}