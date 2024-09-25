package com.effortix.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class PrimePicksController {

    @Autowired
    private PrimePicksService primePicksService;

	/*
	 * // Endpoint to display all active PrimePicks
	 * 
	 * @GetMapping("/primepicks/active") public String getActivePrimePicks(Model
	 * model) { model.addAttribute("primePicksList",
	 * primePicksService.getActivePrimePicks()); return "primepicks/active"; }
	 */

    // Endpoint to show the creation form
    @GetMapping("/primepicks/create")
    public String showCreateForm(Model model) {
        PrimePicks primePicks = new PrimePicks();
        model.addAttribute("primePicks", primePicks);
        return "primePicksUI/create";  // Render the Thymeleaf form
    }

	/*
	 * // Endpoint to handle form submission
	 * 
	 * @PostMapping("/primepicks/create") public String
	 * createPrimePicks(@ModelAttribute("primePicks") PrimePicks primePicks) {
	 * primePicksService.createPrimePicks(primePicks); return "primePicksUI/create";
	 * // Redirect to active PrimePicks list }
	 */
    
    


    @Autowired
    private TicketService ticketService;

	/*
	 * // Get active prime picks
	 * 
	 * @GetMapping("/primepicks/active") public List<PrimePicks>
	 * getActivePrimePicks(Model model) { List<PrimePicks> primePicksList =
	 * primePicksService.getActivePrimePicks(); model.addAttribute("primePicksList",
	 * primePicksList); return primePicksList; // Send data as JSON to the frontend
	 * }
	 */
    
    @GetMapping("/primepicks/active")
    public String getActivePrimePicks(Model model) {
        List<PrimePicks> primePicksList = primePicksService.getActivePrimePicks();
        model.addAttribute("primePicksList", primePicksList);
        return "primePicksUI/PickTasks"; // This corresponds to active.html
    }
    

    // Save the claimed task as a ticket
    @PostMapping("/save")
    public String saveTicket2(@RequestBody Ticket ticket) {
        System.out.println("Ticket: " + ticket.getTName());
        System.out.println("Assigned to: " + ticket.getToEmployee().geteEmail());

        // Save the ticket
        Ticket savedTicket = ticketService.saveOrUpdateTicket(ticket);

        // Trigger an email or any additional functionality you need
      //EMAILLLLLLLL

        System.out.println("Ticket saved.");
        return "redirect:/tickets"; // Redirect to the tickets page
    }
}