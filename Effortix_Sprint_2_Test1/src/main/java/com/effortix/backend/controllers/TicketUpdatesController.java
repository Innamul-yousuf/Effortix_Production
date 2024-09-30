 package com.effortix.backend.controllers;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.Project;
import com.effortix.backend.models.ProjectEmployee;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.models.TicketUpdates;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.ProjectEmployeeService;
import com.effortix.backend.services.ProjectService;
import com.effortix.backend.services.TicketService;
import com.effortix.backend.services.TicketUpdatesService;

@Controller
@RequestMapping("/ticketUpdates")
public class TicketUpdatesController {

	@Autowired
	private TicketUpdatesService ticketUpdatesService;



	@GetMapping("/byDate")
	public List<TicketUpdates> getTicketUpdatesByEmployeeAndDate(@RequestParam Long employeeId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
		return ticketUpdatesService.getTicketUpdatesByEmployeeAndDateRange(employeeId, date);
	}

	
	  @PostMapping public ResponseEntity<TicketUpdates>
	  createOrUpdateTicketUpdates(@RequestBody TicketUpdates ticketUpdates) {
	  TicketUpdates savedTicketUpdates =
	  ticketUpdatesService.saveOrUpdateTicketUpdates(ticketUpdates); return
	  ResponseEntity.ok(savedTicketUpdates); }
	 

	@Autowired
	private TicketService ticketService;

	@Autowired
	EmployeeService employeeService;
	@Autowired
	ProjectService projectService;

	@PostMapping("/{createByticketId}")
	public String createOrUpdateTicketUpdates(@RequestParam Long ticketId, @RequestParam Long employeeId,
			@RequestParam Long projectId, @RequestParam String tUpdate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date dateTime) {

		// Create new TicketUpdates object
		Optional<Ticket> ticket = ticketService.getTicketById(ticketId);
		Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
		Optional<Project> project = projectService.getProjectById(projectId);

		TicketUpdates ticketUpdates = new TicketUpdates();
		ticketUpdates.setTicket(ticket.get());
		ticketUpdates.setEmployee(employee.get());
		ticketUpdates.setProject(project.get());
		ticketUpdates.setDateTime(dateTime);
		ticketUpdates.settUpdate(tUpdate);

		// Save the ticket update
		ticketUpdatesService.saveOrUpdateTicketUpdates(ticketUpdates);
		
		return  "ticketUI/ticket_detail";
	}

	@GetMapping("/{id}")
	public ResponseEntity<TicketUpdates> getTicketUpdatesById(@PathVariable Long id) {
		Optional<TicketUpdates> ticketUpdates = ticketUpdatesService.getTicketUpdatesById(id);
		return ticketUpdates.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/*
	 * @GetMapping("/{ticketId}") public String getTicketDetails(@PathVariable Long
	 * ticketId, Model model) { Optional<Ticket> ticket =
	 * ticketService.getTicketById(ticketId);
	 * 
	 * // Fetch previous updates for this ticket Optional<TicketUpdates>
	 * previousUpdates = ticketUpdatesService.getTicketUpdatesById(ticketId);
	 * 
	 * model.addAttribute("ticket", ticket); model.addAttribute("previousUpdates",
	 * previousUpdates);
	 * 
	 * return "ticketUI/ticket_detail"; }
	 */
	
	/*
	 * @GetMapping("/{ticketId}") public String getTicketDetails(@PathVariable Long
	 * ticketId, Model model) { Optional<Ticket> ticket =
	 * ticketService.getTicketById(ticketId);
	 * 
	 * // Assuming you already have the employeeId and projectId from the ticket
	 * Long employeeId = ticket.get().getToEmployee().geteId(); Long projectId =
	 * ticket.get().getProject().getpId();
	 * 
	 * // Fetch matching updates List<TicketUpdates> previousUpdates =
	 * ticketUpdatesService.getUpdatesByTicketEmployeeProject(ticketId, employeeId,
	 * projectId); System.out.println("count of previous updates:"
	 * +previousUpdates.size()); model.addAttribute("ticket", ticket.orElse(null));
	 * model.addAttribute("previousUpdates", previousUpdates); // Add updates to the
	 * model
	 * 
	 * return "ticketUI/ticket_detail"; }
	 */
	
	

	
	@GetMapping("/ticket/{ticketId}")
	public String getTicketDetails(@PathVariable Long ticketId, Model model) {
	    // Fetch the ticket by ID
	    Optional<Ticket> ticket = ticketService.getTicketById(ticketId);

	    if (ticket.isPresent()) {
	        // Fetch the previous updates related to this ticket
	        List<TicketUpdates> previousUpdates = ticketUpdatesService.getUpdatesByTicketEmployeeProject(
	                ticketId, ticket.get().getToEmployee().geteId(), ticket.get().getProject().getpId());
	        
	        // Add the ticket and previous updates to the model
	        model.addAttribute("ticket", ticket.get());
	        model.addAttribute("previousUpdates", previousUpdates);
	    } else {
	        model.addAttribute("error", "Ticket not found.");
	    }

	    return "ticketUI/ticket_detail";
	
	}

	
	

	@GetMapping("/{getTicketUpdate}")
	public ResponseEntity<List<TicketUpdates>> getAllTicketUpdates() {
		List<TicketUpdates> ticketUpdatesList = ticketUpdatesService.getAllTicketUpdates();
		return ResponseEntity.ok(ticketUpdatesList);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTicketUpdatesById(@PathVariable Long id) {
		ticketUpdatesService.deleteTicketUpdatesById(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/ticketUpdates")
	public ResponseEntity<List<TicketUpdates>>  submitUpdate(@RequestParam("ticketId") Long ticketId, @RequestParam("employeeId") Long employeeId,
			@RequestParam("projectId") Long projectId,
			@RequestParam("dateTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTime,
			@RequestParam("tUpdate") String updateText, Model model) {

		// Create a new TicketUpdates object and set values from form inputs
		TicketUpdates newUpdate = new TicketUpdates();
	        newUpdate.setTicket(ticketService.getTicketById(ticketId).get());
	        Employee temp=new Employee();
	        Project p=new Project();
	        
	        newUpdate.setEmployee(employeeService.getEmployeeById(employeeId).get()); // Assuming Employee entity
	       newUpdate.setProject(projectService.getProjectById(projectId).get()); // Assuming Project entity
	       newUpdate.setDateTime(dateTime);
	       newUpdate.settUpdate(updateText);

		// Save the new update
		ticketUpdatesService.saveOrUpdateTicketUpdates(newUpdate);

		// Redirect back to the ticket details page after submission
		//return "ticketUI/ticket_detail";
		return ResponseEntity.noContent().build();
	}
	
	//Failed
	@PostMapping("/ticketUpdates/dynamic")
	public ResponseEntity<List<TicketUpdates>> submitUpdate(
	        @RequestParam("ticketId") Long ticketId,
	        @RequestParam("employeeId") Long employeeId,
	        @RequestParam("projectId") Long projectId,
	        @RequestParam("dateTime") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTime,
	        @RequestParam("tUpdate") String updateText) {

	    // Create a new TicketUpdates object and set values from form inputs
	    TicketUpdates newUpdate = new TicketUpdates();
	    newUpdate.setTicket(ticketService.getTicketById(ticketId).get());
	    newUpdate.setEmployee(employeeService.getEmployeeById(employeeId).get());
	    newUpdate.setProject(projectService.getProjectById(projectId).get());
	    newUpdate.setDateTime(dateTime);
	    newUpdate.settUpdate(updateText);

	    // Save the new update
	    ticketUpdatesService.saveOrUpdateTicketUpdates(newUpdate);

	    // Get updated list of previous updates for the ticket
	    Optional<Ticket> ticket = ticketService.getTicketById(ticketId);

	    List<TicketUpdates> previousUpdates = ticketUpdatesService.getUpdatesByTicketEmployeeProject(ticketId, ticket.get().getToEmployee().geteId(), ticket.get().getProject().getpId());

	    // Return the updated list as JSON
	    return ResponseEntity.ok(previousUpdates);
	}
	
	/*
	 * //@GetMapping("/byTicketEmployeeProject")
	 * 
	 * @GetMapping("/ticket/{ticketId}") public ResponseEntity<List<TicketUpdates>>
	 * getUpdatesByTicketEmployeeProject(
	 * 
	 * @RequestParam Long ticketId,
	 * 
	 * @RequestParam Long employeeId,
	 * 
	 * @RequestParam Long projectId) {
	 * 
	 * // Call the repository method to get the filtered ticket updates
	 * List<TicketUpdates> updates =
	 * ticketUpdatesService.getUpdatesByTicketEmployeeProject(ticketId, employeeId,
	 * projectId);
	 * 
	 * // Return the updates as the response body return ResponseEntity.ok(updates);
	 * }
	 */

	
	
	
}


/*
 * package com.effortix.backend.controllers;
 * 
 * import java.util.Date; import java.util.List; import java.util.Optional;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.format.annotation.DateTimeFormat; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.DeleteMapping; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.ModelAttribute; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RequestParam; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import com.effortix.backend.models.ProjectEmployee; import
 * com.effortix.backend.models.TicketUpdates; import
 * com.effortix.backend.services.ProjectEmployeeService; import
 * com.effortix.backend.services.TicketUpdatesService;
 * 
 * @RestController
 * 
 * @RequestMapping("/ticketupdates") public class TicketUpdatesController {
 * 
 * @Autowired private TicketUpdatesService ticketUpdatesService;
 * 
 * 
 * @GetMapping("/all") public List<TicketUpdates>
 * getTicketUpdatesByEmployeeAndDateRange(
 * 
 * @RequestParam Long employeeId,
 * 
 * @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
 * 
 * @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
 * return
 * ticketUpdatesService.getTicketUpdatesByEmployeeAndDateRange(employeeId,
 * startDate, endDate); }
 * 
 * 
 * 
 * @GetMapping("/byDate") public List<TicketUpdates>
 * getTicketUpdatesByEmployeeAndDate(
 * 
 * @RequestParam Long employeeId,
 * 
 * @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
 * return
 * ticketUpdatesService.getTicketUpdatesByEmployeeAndDateRange(employeeId,
 * date); }
 * 
 * 
 * @PostMapping public ResponseEntity<TicketUpdates>
 * createOrUpdateTicketUpdates(@RequestBody TicketUpdates ticketUpdates) {
 * TicketUpdates savedTicketUpdates =
 * ticketUpdatesService.saveOrUpdateTicketUpdates(ticketUpdates); return
 * ResponseEntity.ok(savedTicketUpdates); }
 * 
 * @GetMapping("/{id}") public ResponseEntity<TicketUpdates>
 * getTicketUpdatesById(@PathVariable Long id) { Optional<TicketUpdates>
 * ticketUpdates = ticketUpdatesService.getTicketUpdatesById(id); return
 * ticketUpdates.map(ResponseEntity::ok).orElseGet(() ->
 * ResponseEntity.notFound().build()); }
 * 
 * @GetMapping public ResponseEntity<List<TicketUpdates>> getAllTicketUpdates()
 * { List<TicketUpdates> ticketUpdatesList =
 * ticketUpdatesService.getAllTicketUpdates(); return
 * ResponseEntity.ok(ticketUpdatesList); }
 * 
 * @DeleteMapping("/{id}") public ResponseEntity<Void>
 * deleteTicketUpdatesById(@PathVariable Long id) {
 * ticketUpdatesService.deleteTicketUpdatesById(id); return
 * ResponseEntity.noContent().build(); }
 * 
 * }
 */