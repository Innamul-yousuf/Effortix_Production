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
	@Table(name = "PrimePicks") // This should match your actual table name
	public class PrimePicks {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long ppID;

	    @Column(name = "TopicName")
	    private String topicName;

	    @Column(name = "status")
	    private String status;

	    @Column(name = "Activity", length = 6000)
	    private String activity;

	    // Getters and Setters
	    public Long getPpID() {
	        return ppID;
	    }

	    public void setPpID(Long ppID) {
	        this.ppID = ppID;
	    }

	    public String getTopicName() {
	        return topicName;
	    }

	    public void setTopicName(String topicName) {
	        this.topicName = topicName;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public String getActivity() {
	        return activity;
	    }

	    public void setActivity(String activity) {
	        this.activity = activity;
	    }

	    // toString() (Optional for debugging purposes)
	    @Override
	    public String toString() {
	        return "PrimePicks{" +
	                "ppID=" + ppID +
	                ", topicName='" + topicName + '\'' +
	                ", status='" + status + '\'' +
	                ", activity='" + activity + '\'' +
	                '}';
	    }
	}