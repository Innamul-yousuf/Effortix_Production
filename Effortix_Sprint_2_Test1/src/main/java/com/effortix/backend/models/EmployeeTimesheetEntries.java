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
@Table(name = "employee_timesheet_entries")
public class EmployeeTimesheetEntries {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "etId", nullable = false, unique = true)
    private Long etId; // Primary Key

    // Relation to Employee
    @ManyToOne
    @JoinColumn(name = "etEid", nullable = false, referencedColumnName = "eId") // FK to Employee
    private Employee employee;

    // Relation to Ticket
    @ManyToOne
    @JoinColumn(name = "etTid", nullable = true, referencedColumnName = "tId") // FK to Ticket (can be nullable)
    private Ticket ticket;

    // Relation to Project
    @ManyToOne
    @JoinColumn(name = "etPid", nullable = true, referencedColumnName = "pId") // FK to Project (can be nullable)
    private Project project;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date; // Date of the entry

    @Column(name = "etActivity", length = 6000)
    private String etActivity; // Description of the activity

    // Getters and Setters

    public Long getEtId() {
        return etId;
    }

    public void setEtId(Long etId) {
        this.etId = etId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEtActivity() {
        return etActivity;
    }

    public void setEtActivity(String etActivity) {
        this.etActivity = etActivity;
    }

	public EmployeeTimesheetEntries() {
		
	}
    
}