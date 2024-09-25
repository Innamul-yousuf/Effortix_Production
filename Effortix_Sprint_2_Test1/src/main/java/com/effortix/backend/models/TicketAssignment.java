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
@Table(name = "TicketAssignments")
public class TicketAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TicketAssignedID", nullable = false, unique = true)
    private Long ticketAssignedId; // Primary key

    @ManyToOne
    @JoinColumn(name = "TId", nullable = false, referencedColumnName = "tId") // FK to Ticket
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "AssignedTo", nullable = false, referencedColumnName = "eId") // FK to Employee
    private Employee employee;

    @Column(name = "AssignmentStatus", nullable = false)
    private String assignmentStatus; // e.g., "Assigned", "Accepted", "Completed", etc.

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AcceptDate")
    private Date acceptDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CompletedDate")
    private Date completedDate;

    @Column(name = "Flag", nullable = false)
    private Boolean flag = false;

    @Column(name = "RejectReason", columnDefinition = "TEXT")
    private String rejectReason;

    // Getters and Setters

    public Long getTicketAssignedId() {
        return ticketAssignedId;
    }

    public void setTicketAssignedId(Long ticketAssignedId) {
        this.ticketAssignedId = ticketAssignedId;
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

    public String getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}