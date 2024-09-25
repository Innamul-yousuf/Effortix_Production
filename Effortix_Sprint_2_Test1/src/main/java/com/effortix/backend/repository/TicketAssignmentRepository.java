package com.effortix.backend.repository;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeTimesheetEntries;
import com.effortix.backend.models.TicketAssignment;

@Repository
public interface TicketAssignmentRepository extends JpaRepository<TicketAssignment, Long> {
	  
}