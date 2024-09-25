package com.effortix.backend.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.effortix.backend.EmailsUps.EmailService;
import com.effortix.backend.models.Employee;
import com.effortix.backend.models.Project;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.ProjectService;
import com.effortix.backend.services.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private ProjectService projectService;
    
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    
    // List all tickets
    @GetMapping
    public String listTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "ticketUI/ticket_list"; // Returns 'ticket_list.html'
    }

    // Show form to create a new ticket
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticketUI/ticket_form"; // Returns 'ticket_form.html'
    }

	
	  // Save a new ticket
	  
	  @PostMapping("/save") public String saveTicket2(@ModelAttribute("ticket")Ticket ticket) {
		  System.out.println("Ticket"+ticket.getTName()+" ");
		  System.out.println("Ticket"+ticket.getToEmployee().geteEmail()+" ");
		  
		  Ticket savedTicket=ticketService.saveOrUpdateTicket(ticket);
		  sendEmailToResponsible(ticket);
	  //sendEmailToResponsible(ticket);
		 

	  System.out.println("saveTicket2"); 
	  return "redirect:/tickets"; // Redirects//to the ticket list }
	  }

    // View details of a single ticket
    @GetMapping("/{id}")
    public String viewTicket(@PathVariable("id") Long id, Model model) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            return "ticketUI/ticket_detail"; // Returns 'ticket_detail.html'
        } else {
            model.addAttribute("errorMessage", "Ticket not found");
            return "error"; // Returns 'error.html'
        }
    }
    
    // Show form to edit an existing ticket
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            return "ticketUI/ticket_form"; // Returns 'ticket_form.html'
        } else {
            model.addAttribute("errorMessage", "Ticket not found");
            return "error"; // Returns 'error.html'
        }
    }

    // Delete a ticket
    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable("id") Long id) {
        boolean deleted = ticketService.deleteTicketById(id);
        return "redirect:/tickets"; // Redirects to the ticket list
    }
    
    
    @GetMapping("/tickets/edit/{tId}")
    public String editTicket(@PathVariable("tId") Long tId, Model model) {
        Optional<Ticket> ticket = ticketService.getTicketById(tId);
        List<Employee> employees = employeeService.getAllEmployees(); // Fetch all employees
        List<Project> projects = projectService.getAllProjects(); // Fetch all projects

        model.addAttribute("ticket", ticket);
        model.addAttribute("employees", employees);
        model.addAttribute("projects", projects);
        
        return "ticket-form"; // Thymeleaf template name
    }

    
	
	  @Autowired ProjectService projectService2;
	  
	  @GetMapping("/tickets/new") public String createNewTicket(Model model) {
	  Ticket ticket=new Ticket(); List<Employee> employees =
	  employeeService.getAllEmployees();
	  System.out.println("Employees For Form:"+employees.size()+employees+employees
	  .get(0).geteId()); for(int i=0; i <employees.size();i++) {
	  System.out.println("Employee name: "+employees.get(i).geteName()); }
	  List<Project> projects = projectService2.getAllProjects(); for(int i=0; i
	  <projects.size();i++) {
	  System.out.println("projects name: "+projects.get(i).getpName()); }
	  
	  
	  model.addAttribute("ticket", ticket); model.addAttribute("employees",
	  employees); model.addAttribute("projects", projects);
	  
	  return "ticketUI/ticket_form"; }
	 

	
	  @PostMapping("/tickets/save") public String saveTicket(Ticket ticket) {
	  
	  ticketService.saveOrUpdateTicket(ticket);
	  System.out.println("ticket"+ticket.getTId()); sendEmailToResponsible(ticket);
	  System.out.println("saveTicket"); return "redirect:/tickets"; 
	  
	  }
	  
	 
    
    @Autowired
   	private EmailService emailService;
       //Email
       private void sendEmailToResponsible(Ticket savedTicket) {
           
           // Retrieve the assigned employee's email
           String assignedEmployeeEmail = savedTicket.getToEmployee().geteEmail(); 
           System.out.println("assignedEmployeeEmail: "+assignedEmployeeEmail);
           // Prepare email details
           String subject = "New Ticket Assigned: " + savedTicket.getTName();
           String text = "Dear " + savedTicket.getToEmployee().geteName() + ",\n\n"
                   + "A new ticket has been assigned to you. Please check the details below:\n"
                   + "Ticket ID: " + savedTicket.getTId() + "\n"
                   + "Title: " + savedTicket.getTName() + "\n"
                   + "Description: " + savedTicket.getTDescription() + "\n\n"
                   + "Best regards,\nEffortix Team";

           // Send email notification to the assigned employee
           emailService.sendSimpleMessage(assignedEmployeeEmail, subject, text);

       }
    
    
    
       
       
       /////////////////AI Tasks
    // Show the ticket creation form, pre-filled with values from the PrimePicks
		/*
		 * @GetMapping("/tickets/tickets/new") public String showNewTicketForm(
		 * 
		 * @RequestParam(name = "topicName", required = false) String topicName,
		 * 
		 * @RequestParam(name = "activity", required = false) String activity, Model
		 * model) {
		 * 
		 * // Create a new empty ticket object Ticket ticket = new Ticket();
		 * 
		 * // Pre-fill ticket fields with PrimePicks data if available
		 * 
		 * if (topicName != null) { ticket.setTName(topicName); } if (activity != null)
		 * { ticket.setTDescription(activity); }
		 * 
		 * // Get employees and projects for the form List<Employee> employees =
		 * employeeService.getAllEmployees(); List<Project> projects =
		 * projectService.getAllProjects();
		 * 
		 * // Add attributes to the model model.addAttribute("ticket", ticket);
		 * model.addAttribute("employees", employees); model.addAttribute("projects",
		 * projects);
		 * 
		 * return "ticketUI/ticket_form"; // Return the Thymeleaf template for the form
		 * }
		 */
       
       
       @GetMapping("/tickets/ticketsAI/new")
       public String showNewTicketForm(
               @RequestParam(name = "ppID", required = false) Long ppID,
               @RequestParam(name = "topicName", required = false) String topicName,
               @RequestParam(name = "activity", required = false) String activity,
               Model model) {

           // Create a new empty ticket object
           Ticket ticket = new Ticket();

           // Pre-fill ticket fields with PrimePicks data if available
           if (ppID != null) {
              System.out.println("ppID: "+ppID); //) there is a field for PrimePicks ID
           }
           if (topicName != null) {
               ticket.setTName(topicName);
           }
           if (activity != null) {
               ticket.setTDescription(activity);
           }
           ticket.setTType("AI Generated");
           Optional<Employee> fromEmployee=employeeService.getEmployeeById(152L);
           ticket.setFromEmployee(fromEmployee.get());
           ticket.setTStatus("Assigned");
           
           // Get employees and projects for the form
           List<Employee> employees = employeeService.getAllEmployees();
           List<Project> projects = projectService2.getAllProjects();

           // Add attributes to the model
           model.addAttribute("ticket", ticket);
           model.addAttribute("employees", employees);
           model.addAttribute("projects", projects);
           
           return "ticketUI/ticket_form";  // Return the Thymeleaf template for the form
       }
       
    
}