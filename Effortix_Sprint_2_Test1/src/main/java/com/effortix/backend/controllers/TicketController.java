package com.effortix.backend.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.effortix.backend.AIServices.FindEmployyeeAI;
import com.effortix.backend.EmailsUps.EmailService;
import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.Project;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.repository.TicketRepository;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.EmployeeSkillsService;
import com.effortix.backend.services.ProjectService;
import com.effortix.backend.services.TicketService;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpSession;

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
    
    @GetMapping("/IndividualTickets")
    public String listTicketsIndividual(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "ticketUI/ticket_list_Individual"; // Returns 'ticket_list.html'
    }

    // Show form to create a new ticket
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticketUI/ticket_form"; // Returns 'ticket_form.html'
    }

	
	  // Save a new ticket
	  @Autowired
	  TicketRepository Ticrepository;
    @PostMapping("/save") public String saveTicket2(@ModelAttribute("ticket")Ticket ticket) {
		  System.out.println("Ticket"+ticket.getTName()+" ");
		  System.out.println("Ticket"+ticket.getToEmployee().geteEmail()+" ");
		  
		  if (ticket.getTId() == null) {
		        System.out.println("Ticket ID is null, indicating a new ticket");
		        // Handle creating a new ticket if necessary
		    } else {
		        System.out.println("Updating existing ticket with ID: " + ticket.getTId());
		    }
		
	  //sendEmailToResponsible(ticket);
	        //Ticket savedTicket=ticketService.saveOrUpdateTicket(ticket);
		  	ticketService.calculateCredits(ticket);
	        Ticket savedTicket=Ticrepository.save(ticket);
	        sendEmailToResponsible(ticket);
	  System.out.println("saveTicket2"); 
	  return "redirect:/tickets"; // Redirects//to the ticket list }
	  }
   

    
    
    	
    @PostMapping("/saveByAI")
    public String saveTicketByAI(@ModelAttribute("ticket") Ticket ticket) { 
    	System.out.println("ticket id: "+ticket);
        System.out.println("Ticket Name: " + ticket.getTName());
        System.out.println("Assigned Employee: " + ticket.getToEmployee().geteEmail());

        // Set the createdDate to the current date
        ticket.setCreatedDate(new Date());

        // Set the ticket status to "In Progress"
        ticket.setTStatus("In Progress");

        // Set default project and fromEmployee if not provided
        // Assuming you have predefined values in your database for AI project and AI-assigned employee
        Optional<Project> defaultProject = projectService.getProjectById(3L);
        if (defaultProject.isPresent()) {
            ticket.setProject(defaultProject.get());
        } else {
            throw new RuntimeException("Default AI Project not found");
        }

        Optional<Employee> aiAssignedEmployee = employeeService.getEmployeeById(152L);
        if (aiAssignedEmployee.isPresent()) {
            ticket.setFromEmployee(aiAssignedEmployee.get());
        } else {
            throw new RuntimeException("AI Assigned Employee not found");
        }

        // Save the ticket with all details
        Ticket savedTicket = ticketService.saveOrUpdateTicket(ticket);

        // Optionally send email notification to the responsible employee
        sendEmailToResponsible(savedTicket);

        System.out.println("Ticket saved successfully with Name: " + savedTicket.getTName());
        return "redirect:/tickets"; // Redirects to the ticket list page or another view
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
        Long fromEmployee=ticket.get().getFromEmployee().geteId();
        Optional<Employee> FromEmployeeObj=employeeService.getEmployeeById(fromEmployee);
        Long ToEmployee=ticket.get().getToEmployee().geteId();
        Optional<Employee> ToEmployeeObj=employeeService.getEmployeeById(ToEmployee);

        if (ticket.isPresent()) {
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("er", FromEmployeeObj.get());
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
    	Optional<Ticket> ticketOptional = ticketService.getTicketById(tId);
    	
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            model.addAttribute("ticket", ticket);
            // Populate employees and projects for dropdowns
            List<Employee> employees = employeeService.getAllEmployees();
            List<Project> projects = projectService.getAllProjects();
          
                model.addAttribute("tId", tId);
           
            model.addAttribute("employees", employees);
            model.addAttribute("projects", projects);
            return "ticketUI/ticket_form"; // Thymeleaf template name
        } else {
            // Handle the case where the ticket is not found
            return "redirect:/tickets"; // or show an error message
        }
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
	  
	  return "ticketUI/ticket_form"; 
	  
	  
	  }
	 

	
	  @PostMapping("/tickets/save") public String saveTicket(Ticket ticket) {
	  
	  ticketService.saveOrUpdateTicket(ticket);
	  System.out.println("ticket"+ticket.getTId()); 
	  sendEmailToResponsible(ticket);
	  System.out.println("saveTicket"); 
	  return "redirect:/tickets";
	  
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
           String text = "Hi " + savedTicket.getToEmployee().geteName() + ",\n\n"
        		   +"Good Day!"
                   + "A new ticket has been assigned to you. Please check the details below:\n"
                   + "Ticket ID: " + savedTicket.getTId() + "\n"
                   + "Title: " + savedTicket.getTName() + "\n"
                   + "Description: " + savedTicket.getTDescription() + "\n\n"
                   + "Best regards,\nEffortix Team";

           // Send email notification to the assigned employee
           emailService.sendSimpleMessage(assignedEmployeeEmail, subject, text);

       }
    
    
    
       
    
       
       
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
       
       

       @Autowired
       FindEmployyeeAI findEmployyeeAI;
       @Autowired
       EmployeeSkillsService employeeSkillsService;
       //AI Suugestion Employees
       @PostMapping("/tickets/getAiSuggestedEmployees")
       @ResponseBody
       public List<Employee> getAiSuggestedEmployees(@RequestBody Map<String, String> requestBody) {
           String description = requestBody.get("description");
          
           Gson gson = new Gson();
        	List<EmployeeSkills> employeeSkills= employeeSkillsService.getAllEmployeeSkills();
        	String EmployeeSkillsJSON = gson.toJson(employeeSkills);
            System.out.println("JSON output: " + EmployeeSkillsJSON);
           // ExecutorService executorService = Executors.newSingleThreadExecutor();

            // Submit the task for asynchronous execution
			/*
			 * executorService.submit(() -> {
			 * findEmployyeeAI.findEmployeeWithSkill(description, EmployeeSkillsJSON); });
			 */
            System.out.println("Continuing with other operations while finding employees with skill...");

            // Shutdown the executor service after your tasks are complete
            //executorService.shutdown();
            
            
          
           
         
           
           // Call the method to get AI-suggested employees based on the description
           List<Employee> suggestedEmployees = findEmployyeeAI.findEmployeeWithSkill(description, EmployeeSkillsJSON);
           
           // Filter out Optional.empty() and return the list of employees
           return suggestedEmployees;
       }

       
      
       @PostMapping("/tickets/edit/getAiSuggestedEmployees")
       @ResponseBody
       public List<Employee> getAiSuggestedEmployeesForFormEdit(@RequestBody Map<String, String> requestBody) {
           String description = requestBody.get("description");
          
           Gson gson = new Gson();
        	List<EmployeeSkills> employeeSkills= employeeSkillsService.getAllEmployeeSkills();
        	String EmployeeSkillsJSON = gson.toJson(employeeSkills);
            System.out.println("JSON output: " + EmployeeSkillsJSON);
          
            System.out.println("Continuing with other operations while finding employees with skill...");

            
           List<Employee> suggestedEmployees = findEmployyeeAI.findEmployeeWithSkill(description, EmployeeSkillsJSON);
           
          
           return suggestedEmployees;
       }
    
       @GetMapping("/dashboard")
       public String showDashboard(Model model) {
           model.addAttribute("ticket", new Ticket());
           return "pages/dashboard"; // Thymeleaf will look for dashboard.html
       }
       
       @GetMapping("/sharePoint/{id}")
      
       public String showSharePoint(@PathVariable("id") Long id, Model model) {
    	   Optional<Ticket> ticket = ticketService.getTicketById(id);
           if (ticket.isPresent()) {
               model.addAttribute("ticket", ticket.get());
               return "pages/sharePointIntegrations";  // Returns 'ticket_detail.html'
           } else {
               model.addAttribute("errorMessage", "Ticket not found");
               return "error"; // Returns 'error.html'
           }
       }
       
       
       @PostMapping("tickets/save/{id}")
       public String saveTicket2(@PathVariable("id") Long id, @ModelAttribute("ticket") Ticket ticket) {
           // Retrieve the existing ticket if it's an update
           if (ticket.getTId() == null) {
               // Fetch the ticket from the service if it's not already set
               Optional<Ticket> existingTicket = ticketService.getTicketById(id);
               if (existingTicket != null) {
                   // Update the ticket with the existing ticket data
                   ticket.setTId(existingTicket.get().getTId()); // Ensure ticket ID is set
                   ticket.setTName(existingTicket.get().getTName());
                   ticket.setToEmployee(existingTicket.get().getToEmployee());
                   // Add any other fields as necessary
               } else {
                   System.out.println("No existing ticket found for ID: " + id);
                   return "redirect:/tickets"; // Redirect if the ticket does not exist
               }
           }

           // Print ticket details for debugging
           System.out.println("Ticket: " + ticket.getTName());
           System.out.println("To Employee Email: " + ticket.getToEmployee().geteEmail());

           // Handle the ticket saving logic
           Ticket savedTicket = ticketService.saveOrUpdateTicket(ticket);
           sendEmailToResponsible(ticket);
           System.out.println("saveTicket2");
           
           return "redirect:/tickets"; // Redirect to the ticket list
       }
       
       
       @GetMapping("/tickets/claim/{ticketId}")
       public String showTicketForm(@PathVariable Long ticketId, Model model, HttpSession session) {
           // Fetch ticket by ID
           Optional<Ticket> ticket = ticketService.getTicketById(ticketId);
           if (ticket == null) {
               return "redirect:/tickets/issues"; // Redirect to issue list if ticket not found
           }

           // Prefill the ticket form with ticket details
           model.addAttribute("ticket", ticket.get());

           // Fetch employee info from session to prefill employee info
           Employee employee = (Employee) session.getAttribute("employee");
           model.addAttribute("employee", employee);

           return "ticketUI/ticket_form"; // Ticket form view
       }
       
       @GetMapping("/tickets/claim/{id}")
       public String claimTicket(@PathVariable("id") Long ticketId, Model model, HttpSession session) {
           // Fetch the ticket by ID
           Optional<Ticket> ticket = ticketService.getTicketById(ticketId);
           
           // Get the logged-in user from the session
           Employee loggedInEmployee = (Employee) session.getAttribute("loggedInEmployee");
           
           // Prefill the `toEmployee` with the logged-in user
           ticket.get().setToEmployee(loggedInEmployee);
           
           // Add the ticket and required lists (employees, projects) to the model
           model.addAttribute("ticket", ticket);
//           model.addAttribute("employees", employeeService.findAll());
//           model.addAttribute("projects", projectService.findAll());
           
           return "ticketUI/ticket_form";  // Return the Thymeleaf template for ticket form
       }
       
}


@Controller
@RequestMapping("/ticketsREST")
class TicketRESTController { 

    @Autowired
    private TicketService ticketService;

    // Endpoint to fetch tickets for the selected employee and flag = 0
    @GetMapping("/flag")
    @ResponseBody  // Returns the response as JSON
    public List<Ticket> getTicketsByEmployeeAndFlag(@RequestParam("employeeId") Long employeeId) {
        return ticketService.getTicketsByEmployeeIdAndFlag(employeeId);  // Flag = 0 for filtering tickets
    }
    
    @GetMapping("/tickets/issues")
    public ResponseEntity<List<Ticket>> getTicketsByType(HttpSession session) {
        // Fetch all tickets with type "Issue" and t_flag = 0
        List<Ticket> tickets = ticketService.getTicketsByTypeAndFlag("Issue", 0);

        // Return the list of tickets as JSON
        return ResponseEntity.ok(tickets);
    }
    
    
    @GetMapping("/created")
    @ResponseBody  // Returns the response as JSON
    public List<Ticket> getTicketsCreatedByEmployee(@RequestParam("employeeId") Long employeeId) {
        return ticketService.getTicketsByFromEmployeeId(employeeId);
    }
    
    
}