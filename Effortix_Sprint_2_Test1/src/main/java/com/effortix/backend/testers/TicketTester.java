package com.effortix.backend.testers;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.Project;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.models.TicketUpdates;
import com.effortix.backend.repository.TicketUpdatesRepository;
import com.effortix.backend.services.TicketService;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.ProjectService;

@Component
public class TicketTester {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private TicketUpdatesRepository ticketUpdatesRepository;

    // Method to print all Ticket records to the console
    public  void printAllTicketMade(){
    	
        List<Ticket> tickets = ticketService.getAllTickets();

        if (tickets.isEmpty()) {
            System.out.println("No Ticket records found.");
        } else {
            System.out.println("Ticket Records:");
            for (Ticket ticket : tickets) {
                printTicketDetails(ticket);
                System.out.println("--------------------------------------------------");
            }
        }
    }

    // Method to print a single Ticket record by ID
    public void printTicketById(Long tId) {
        Optional<Ticket> ticket = ticketService.getTicketById(tId);
        if (ticket.isPresent()) {
            printTicketDetails(ticket.get());
        } else {
            System.out.println("Ticket with ID " + tId + " not found.");
        }
    }

    // Method to print Ticket details
    private void printTicketDetails(Ticket ticket) {
        System.out.println("Ticket ID: " + ticket.getTId());
        System.out.println("Ticket Name: " + ticket.getTName());
        System.out.println("Ticket Description: " + ticket.getTDescription());
        System.out.println("Ticket File Link: " + ticket.getTFileLink());
        System.out.println("Ticket Location of File: " + ticket.getLocationOfFile());
        System.out.println("Ticket Flag: " + ticket.getTFlag());
        System.out.println("Ticket Deadline: " + ticket.getDeadline());
        System.out.println("Ticket Created Date: " + ticket.getCreatedDate());
        System.out.println("Ticket Type: " + ticket.getTType());
        System.out.println("Ticket Status: " + ticket.getTStatus());
        System.out.println("Ticket Employee Designation: " + ticket.getEDesignation());

        if (ticket.getFromEmployee() != null) {
            System.out.println("Ticket From Employee: " + ticket.getFromEmployee().geteName());
        }
        if (ticket.getToEmployee() != null) {
            System.out.println("Ticket To Employee: " + ticket.getToEmployee().geteName());
        }
        if (ticket.getProject() != null) {
            System.out.println("Ticket Project: " + ticket.getProject().getpName());
        }
    }

    // Method to create a new Ticket
    public void createTicket(Long fromEmployeeId, Long toEmployeeId, Long projectId) {
        Optional<Employee> fromEmployee = employeeService.getEmployeeById(fromEmployeeId);
        Optional<Employee> toEmployee = employeeService.getEmployeeById(toEmployeeId);
        Optional<Project> project = projectService.getProjectById(projectId);

        if (fromEmployee.isPresent() && toEmployee.isPresent() && project.isPresent()) {
            Ticket newTicket = new Ticket();
            newTicket.setTName("New Feature Request");
            newTicket.setTDescription("Implement a new feature in the system.");
            newTicket.setTFileLink("/path/to/file");
            newTicket.setLocationOfFile("/path/to/location");
            newTicket.setTFlag(1);
            newTicket.setDeadline(new Date());
            newTicket.setCreatedDate(new Date());
            newTicket.setTType("Feature");
            newTicket.setTStatus("Open");
            newTicket.setEDesignation("Developer");

            newTicket.setFromEmployee(fromEmployee.get());
            newTicket.setToEmployee(toEmployee.get());
            newTicket.setProject(project.get());

            Ticket savedTicket = ticketService.saveOrUpdateTicket(newTicket);
            System.out.println("Created new Ticket with ID: " + savedTicket.getTId());
        } else {
            System.out.println("From Employee, To Employee, or Project not found.");
        }
    }

    // Method to update an existing Ticket
    public void updateTicket(Long tId, String newStatus, String newDescription) {
        Optional<Ticket> existingTicket = ticketService.getTicketById(tId);

        if (existingTicket.isPresent()) {
            Ticket ticket = existingTicket.get();
            ticket.setTStatus(newStatus);
            ticket.setTDescription(newDescription);

            Ticket updatedTicket = ticketService.saveOrUpdateTicket(ticket);
            System.out.println("Updated Ticket with ID: " + updatedTicket.getTId());
        } else {
            System.out.println("Ticket with ID " + tId + " not found.");
        }
    }

    // Method to delete a Ticket by ID
    public void deleteTicket(Long tId) {
        boolean isDeleted = ticketService.deleteTicketById(tId);
        if (isDeleted) {
            System.out.println("Deleted Ticket with ID: " + tId);
        } else {
            System.out.println("Ticket with ID " + tId + " not found.");
        }
    }

    // Test all service methods
    public void testAllMethods() {
        // Print all tickets
        //printAllTickets();
    	
    	 List<TicketUpdates> ticketupdates = ticketUpdatesRepository.findByEmployeeEIdMade(2L);
    	 if(ticketupdates!=null) {
    		 for(TicketUpdates update : ticketupdates) {
    			 System.out.println("Ticket updates::::");
    			 System.out.println(" ID: " +  update.getTuId()  );
        	 }
    	 }
    }
    }

    	

        // Print ticket by ID (assuming 1L is a valid ID)
        //printTicketById(1L);

        // Create a new ticket (assuming valid employee and project IDs)
        //createTicket(52L, 203L, 2L);

        // Update ticket status and description (assuming valid ticket ID)
       // updateTicket(1L, "Closed", "Feature implemented successfully");

        // Delete a ticket (assuming 1L is a valid ID)
        //deleteTicket(1L);
    //}

