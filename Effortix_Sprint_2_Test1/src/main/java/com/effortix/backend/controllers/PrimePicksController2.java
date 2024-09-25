package com.effortix.backend.controllers;

import java.security.Principal;
import java.util.List;

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

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.PrimePicks;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.PrimePicksService;
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
@RequestMapping("/primepicks")
public class PrimePicksController2 {
	
	@Autowired
    private PrimePicksService primePicksService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private EmployeeService employeeService;

    
   
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
}