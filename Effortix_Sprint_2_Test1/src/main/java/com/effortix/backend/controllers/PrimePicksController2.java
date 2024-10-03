package com.effortix.backend.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.effortix.backend.AIServices.GenerateHotTasks;
import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.PrimePicks;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.repository.PrimePicksRepository;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.EmployeeSkillsService;
import com.effortix.backend.services.PrimePicksService;
import com.effortix.backend.services.TicketService;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/primepicks")
public class PrimePicksController2 {
	
	@Autowired
    private PrimePicksService primePicksService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmployeeService employeeService;

    
    @Autowired
    private EmployeeSkillsService employeeSkillsService;

    
   
    // Display all active PrimePicks (tasks) on the page
    @GetMapping("/tasks/active")
    public String showActivePrimePicks(Model model) {
        // Fetch all active tasks
        List<PrimePicks> activeTasks = primePicksService.getActivePrimePicks();
        model.addAttribute("activePrimePicks", activeTasks);
        return "primePicksUI/active_hotpicks"; // Thymeleaf view for active tasks
    }

    // This will redirect to the ticket creation page with auto-filled values from PrimePicks
    @PostMapping("/tasks/claim/{taskId}")
    public String claimTask(@PathVariable Long taskId, Model model) {
        PrimePicks primePick = primePicksService.getPrimePicksById(taskId);

        // Pass the PrimePicks data to the ticket creation page
        model.addAttribute("ppID", primePick.getPpID());
        model.addAttribute("topicName", primePick.getTopicName());
        model.addAttribute("activity", primePick.getActivity());

        return "redirect:/tickets/tickets/new";  // Redirect to ticket creation with pre-filled data
    }
    
	/*
	 * @GetMapping("/generate-tasks/{employeeId}") public String
	 * generateTasksForEmployee(@PathVariable Long employeeId, Model model) { //
	 * Fetch employee skills List<EmployeeSkills> employeeSkills =
	 * employeeSkillsService.getEmployeeSkillsByEId(employeeId); // Fetch active
	 * PrimePicks List<PrimePicks> activePrimePicks =
	 * primePicksService.getActivePrimePicks();
	 * 
	 * // Call AI to generate tasks (method can be implemented later) List<Ticket>
	 * ticketList = callAIToGenerateTasks(employeeSkills, activePrimePicks);
	 * 
	 * 
	 * model.addAttribute("employeeSkills", employeeSkills);
	 * model.addAttribute("activePrimePicks", activePrimePicks);
	 * 
	 * return "primePicksUI/taskGenerationResult"; // Name of the view to display
	 * the results }
	 */
    
    @GetMapping("/generate-tasks/{employeeId}")
    public String generateTasksForEmployee(@PathVariable Long employeeId, Model model) {
        // Fetch employee skills
        List<EmployeeSkills> employeeSkills = employeeSkillsService.getEmployeeSkillsByEId(employeeId);
        
        // Fetch active PrimePicks
        List<PrimePicks> activePrimePicks = primePicksService.getActivePrimePicks();

        // Call AI to generate tasks (implement this method)
        List<Ticket> ticketList = callAIToGenerateTasks(employeeSkills, activePrimePicks);
        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);

        // Add data to model
        model.addAttribute("employeeSkills", employeeSkills);
        model.addAttribute("activePrimePicks", activePrimePicks);
        model.addAttribute("ticketList", ticketList);
        
        model.addAttribute("employee", employee.get()); // Add employee details for task assignment

        return "primePicksUI/taskGenerationResult"; // Name of the view to display the results
    }

    
    
    @Autowired
    GenerateHotTasks generateHotTasks;
    private List<Ticket> callAIToGenerateTasks(List<EmployeeSkills> employeeSkills, List<PrimePicks> activePrimePicks) {
       System.out.println("AI Called for Ticket Cretion");
       Gson gson = new Gson();
       String employeeSkill=gson.toJson(employeeSkills);
       System.out.println("Employee Skill: " + employeeSkill);
       
       String primePicks=gson.toJson(activePrimePicks);
       System.out.println("Active Prime Picks: " + primePicks);
       
       List<Ticket> ticketList =generateHotTasks.generateSuitableTasks(employeeSkill, primePicks);
       for(Ticket ticket: ticketList) {
    	   System.out.println(ticket.getTName()+ "  "+ticket.getTDescription());
       }
       return ticketList;
       
    }
    
    @GetMapping("/primepicks/active") public String getActivePrimePicks(Model model) { 
    	model.addAttribute("primePicksList",
    	 primePicksService.getActivePrimePicks()); return "primepicks/active";
    	 
    }
    	

    	    // Endpoint to show the creation form
    	    @GetMapping("/primepicks/create")
    	    public String showCreateForm(Model model) {
    	        PrimePicks primePicks = new PrimePicks();
    	        model.addAttribute("primePicks", primePicks);
    	        return "primePicksUI/create";  // Render the Thymeleaf form
    	    }
    	    
    	    @PostMapping("/createPrimePick")
    	    public String savePrimePick(PrimePicks primePick) {
    	        primePicksRepository.save(primePick);
    	        return "redirect:/activePrimePicks";
    	    }
    	    
    	    @GetMapping("/createPrimePick")
    	    public String createPrimePick(Model model) {
    	        model.addAttribute("primePick", new PrimePicks());
    	        return "primePicksUI/create2";
    	    }
    	    @Autowired
    	    private PrimePicksRepository primePicksRepository;
    	    
    	    @GetMapping("/activePrimePicks")
    	    public String getActivePrimePicks2(Model model) {
    	        List<PrimePicks> activePrimePicks = primePicksRepository.findByStatus("Active");
    	        model.addAttribute("primePicks", activePrimePicks);
    	        return "primePicksUI/primePicksList"; // Corresponds to the Thymeleaf template name
    	    }
}