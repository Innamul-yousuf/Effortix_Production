package com.effortix.backend.models;

import java.util.Date;

import org.antlr.v4.runtime.misc.NotNull;
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
@Table(name = "ticket_updates")
public class TicketUpdates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tuId", nullable = false, unique = true)
    private Long tuId; // Primary Key

    @ManyToOne
    @JoinColumn(name = "TId", nullable = false, referencedColumnName = "tId") // FK to Ticket
    @NotNull
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "EId", nullable = false, referencedColumnName = "eId") // FK to Employee
    @NotNull
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "PId", nullable = true, referencedColumnName = "pId") // FK to Project (optional)
    @NotNull
    private Project project;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateTime;

    @Column(name = "tUpdate",  length = 6000)
    private String tUpdate;

    @Column(name = "FileLink")
    private String fileLink;

    // Constructors
    public TicketUpdates() {}

    // Getters and Setters
    public Long getTuId() {
        return tuId;
    }

    public void setTuId(Long tuId) {
        this.tuId = tuId;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

   
   

	public String gettUpdate() {
		return tUpdate;
	}

	public void settUpdate(String tUpdate) {
		this.tUpdate = tUpdate;
	}

	public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }
}