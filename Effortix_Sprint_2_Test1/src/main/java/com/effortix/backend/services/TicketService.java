package com.effortix.backend.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.effortix.backend.AIServices.GenerateEmployeeCredits;
import com.effortix.backend.EmailsUps.EmailService;
import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeCredits;
import com.effortix.backend.models.EmployeeLeads;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.models.TicketUpdates;
import com.effortix.backend.repository.TicketRepository;
import com.google.gson.Gson;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EmployeeService employeeService;
    // Create or update a ticket
    public Ticket saveOrUpdateTicket(Ticket ticket) {
    	Ticket existingTicket = ticketRepository.findById(ticket.getTId()).orElse(null);
    	
        Ticket savedTicket= ticketRepository.save(ticket);
       
     // Check if the ticket status is updated to "Closed"
        if ( ticket.getTStatus().equals("Completed")) {
        	calculateCredits(savedTicket); // Call the method when the status is closed
        }
        
        sendEmailToResponsible(ticket);
        return savedTicket;
    }

    // Get a ticket by its ID
    public Optional<Ticket> getTicketById(Long tId) {
        return ticketRepository.findById(tId);
    }

    // Get all tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // Delete a ticket by its ID
//    public void deleteTicketById(Long tId) {
//        ticketRepository.deleteById(tId);
//    }
    
    public boolean deleteTicketById(Long tId) {
        if (ticketRepository.existsById(tId)) {
            ticketRepository.deleteById(tId);
            return !ticketRepository.existsById(tId); // Confirm if the ticket is deleted
        } else {
            return false; // Return false if the ticket ID doesn't exist
        }
    }


  

    // Get all tickets assigned to a specific employee (From Employee)
    public List<Ticket> getTicketsByFromEmployeeId(Long employeeId) {
        return ticketRepository.findByFromEmployeeEId(employeeId);
    }

    // Get all tickets assigned to a specific employee (To Employee)
    public List<Ticket> getTicketsByToEmployeeId(Long employeeId) {
        return ticketRepository.findByFromEmployeeEId(employeeId);
    }

    // Get all tickets related to a specific project
    public List<Ticket> getTicketsByProjectId(Long projectId) {
        return ticketRepository.findByProjectPId(projectId);
    }

    // Get all tickets by status
    public List<Ticket> getTicketsByStatus(String status) {
        return ticketRepository.findBytStatus(status);
    }

    // Get all tickets by type
    public List<Ticket> getTicketsByType(String type) {
        return ticketRepository.findBytType(type);
    }
    public List<Ticket> getTicketsByTypeAndFlag(String type, int flag) {
        return ticketRepository.findByTypeAndTFlag(type, flag);
    }

    // Update ticket status
    public Ticket updateTicketStatus(Long tId, String newStatus) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(tId);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            ticket.setTStatus(newStatus);
            return ticketRepository.save(ticket);
        }
        return null; // Or throw a custom exception
    }

    // Update ticket deadline
    public Ticket updateTicketDeadline(Long tId, Date newDeadline) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(tId);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            ticket.setDeadline(newDeadline);
            return ticketRepository.save(ticket);
        }
        return null; // Or throw a custom exception
    }

    // Get tickets by flag
    public List<Ticket> getTicketsByFlag(int tFlag) {
        return ticketRepository.findBytFlag(tFlag);
    }
    
    
    @Autowired
   	private EmailService emailService;
    
    @Autowired
    private EmployeeLeadsService employeeLeadsService;
       //Email
       private void sendEmailToResponsible(Ticket savedTicket) {
    	   long Eid = savedTicket.getToEmployee().geteId();
           System.out.println("tttttt: "+Eid);
           Optional<Employee> toEmployee=employeeService.getEmployeeById(Eid);
           
           // Retrieve the assigned employee's email
           String assignedEmployeeEmail = toEmployee.get().geteEmail(); 
           Optional<EmployeeLeads> LeadEmployee=employeeLeadsService.findEmployeeLeadsByEmployee(toEmployee.get());
           String LeadEmailID=LeadEmployee.get().getLead().geteEmail();
           
           System.out.println("assignedEmployeeEmail: "+assignedEmployeeEmail+ "Lead Email: "+ LeadEmailID);
           // Prepare email details
           String subject = "New Ticket Assigned: " + toEmployee.get().geteName();
           String text = "Dear " + toEmployee.get().geteName() + ",\n\n"
                   + "A new ticket has been assigned to you. Please check the details below:\n"
                   + "Ticket ID: " + savedTicket.getTId() + "\n"
                   + "Title: " + savedTicket.getTName() + "\n"
                   + "Description: " + savedTicket.getTDescription() + "\n\n"
                   + "Best regards,\nEffortix Team";

           // Send email notification to the assigned employee
           emailService.sendSimpleMessage(LeadEmailID, subject, text);

       }
       
       @Autowired
       GenerateEmployeeCredits credits;

       @Autowired
       @Lazy
       TicketUpdatesService ticketUpdatesService;

       @Autowired
       EmployeeCreditsService creditsService;

       public void calculateCredits(Ticket ticket) {
       System.out.println("Processing ticket for credit calculation...");

       // Convert the ticket details to JSON
       String ticketDetailJson = new Gson().toJson(ticket);

       // Fetch ticket updates related to the employee and project
       List<TicketUpdates> ticketUpdates = ticketUpdatesService.getUpdatesByTicketEmployeeProject(
           ticket.getTId(), 
           ticket.getToEmployee().geteId(), 
           ticket.getProject().getpId()
       );

       // Convert ticket updates to JSON
       String ticketUpdatesJson = new Gson().toJson(ticketUpdates);

       // Calculate the credits based on ticket details and updates
       String sCalculatedValues = credits.calculateCredits(ticketDetailJson, ticketUpdatesJson);

       // Fetch the previous credits for the employee
       List<EmployeeCredits> previousCreditList = creditsService.getEmployeeCreditsByEId(ticket.getToEmployee().geteId());
       Double totalCredits = 0D;

       // Sum up the previous credits if any
       for (EmployeeCredits empCredits : previousCreditList) {
           totalCredits += empCredits.getCredits();
       }

       // Parse the newly calculated credits
       Double calculatedValueD = Double.parseDouble(sCalculatedValues);

       // Add the new calculated value to total credits
       totalCredits += calculatedValueD;

       System.out.println("Calculated Value = " + calculatedValueD);
       
       
       // If no previous credits exist, create a new EmployeeCredits entry
       
           EmployeeCredits newCredits = new EmployeeCredits();
           newCredits.setEId(ticket.getToEmployee().geteId());
           newCredits.setCredits(calculatedValueD);  // Set the new calculated credits
           newCredits.setCreditType(ticket.getTType());  // Set appropriate credit type (adjust as needed)
           newCredits.setDate(new Date());
           // Save the new credits in the database
           creditsService.saveOrUpdateEmployeeCredits(newCredits);
           System.out.println("New credits created and saved for the employee.");

      

       }
       
       public List<Ticket> getTicketsByEmployeeIdAndFlag(Long employeeId) {
           return ticketRepository.findByEmployeeIdAndFlag(employeeId, 0);
       }
       
      
}