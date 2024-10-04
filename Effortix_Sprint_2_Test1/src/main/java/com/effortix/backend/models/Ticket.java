package com.effortix.backend.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "Ticket") // Maps to the 'tickets' table in the database
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tId", nullable = false, unique = true) // Ticket primary key
    private Long tId;

    @Column(name = "tName", nullable = false, length = 100)
    private String tName;

    @Column(name = "tDescription", length = 600000)
    private String tDescription;

    @Column(name = "tFileLink")
    private String tFileLink;

    @Column(name = "LocationOfFile")
    private String locationOfFile;

    @Column(name = "tFlag")
    private int tFlag;

    @Temporal(TemporalType.DATE)
    @Column(name = "Deadline")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "tType")
    private String tType;

    @Column(name = "tStatus")
    private String tStatus;

    @Column(name = "eDesignation")
    private String eDesignation;

    // Relationship with Employee: many tickets can be assigned to one employee
    @ManyToOne
    @JoinColumn(name = "tFromEID", referencedColumnName = "EId") // FK to Employee (from)
    private Employee fromEmployee;

    @ManyToOne
    @JoinColumn(name = "tToEID", referencedColumnName = "EId") // FK to Employee (to)
    private Employee toEmployee;

    // Relationship with Project: many tickets can be related to one project
    @ManyToOne
    @JoinColumn(name = "tPID", referencedColumnName = "pId") // FK to Project
    private Project project;

    // Constructors, Getters, and Setters

    public Ticket() {}

    public Long getTId() {
        return tId;
    }

    public void setTId(Long tId) {
        this.tId = tId;
    }

    public String getTName() {
        return tName;
    }

    public void setTName(String tName) {
        this.tName = tName;
    }

    public String getTDescription() {
        return tDescription;
    }

    public void setTDescription(String tDescription) {
        this.tDescription = tDescription;
    }

    public String getTFileLink() {
        return tFileLink;
    }

    public void setTFileLink(String tFileLink) {
        this.tFileLink = tFileLink;
    }

    public String getLocationOfFile() {
        return locationOfFile;
    }

    public void setLocationOfFile(String locationOfFile) {
        this.locationOfFile = locationOfFile;
    }

    public int getTFlag() {
        return tFlag;
    }

    public void setTFlag(int tFlag) {
        this.tFlag = tFlag;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getTType() {
        return tType;
    }

    public void setTType(String tType) {
        this.tType = tType;
    }

    public String getTStatus() {
        return tStatus;
    }

    public void setTStatus(String tStatus) {
        this.tStatus = tStatus;
    }

    public String getEDesignation() {
        return eDesignation;
    }

    public void setEDesignation(String eDesignation) {
        this.eDesignation = eDesignation;
    }

    public Employee getFromEmployee() {
        return fromEmployee;
    }

    public void setFromEmployee(Employee fromEmployee) {
        this.fromEmployee = fromEmployee;
    }

    public Employee getToEmployee() {
        return toEmployee;
    }

    public void setToEmployee(Employee toEmployee) {
        this.toEmployee = toEmployee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
