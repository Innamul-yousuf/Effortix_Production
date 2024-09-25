package com.effortix.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.TicketAssignment;
import com.effortix.backend.repository.EmployeeRepository;
import com.effortix.backend.repository.TicketAssignmentRepository;

@Service
public class TicketAssignmentService {

    @Autowired
    private TicketAssignmentRepository ticketAssignmentRepository;

    // Save or update a TicketAssignment
    public TicketAssignment saveOrUpdateTicketAssignment(TicketAssignment ticketAssignment) {
        return ticketAssignmentRepository.save(ticketAssignment);
    }

    // Find all TicketAssignments
    public List<TicketAssignment> getAllTicketAssignments() {
        return ticketAssignmentRepository.findAll();
    }

    // Find TicketAssignment by ID
    public Optional<TicketAssignment> getTicketAssignmentById(Long id) {
        return ticketAssignmentRepository.findById(id);
    }



    // Delete TicketAssignment by ID
    public void deleteTicketAssignmentById(Long id) {
        ticketAssignmentRepository.deleteById(id);
    }

}
