package com.effortix.backend.AIServices;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.effortix.backend.EmailsUps.EmailService;
import com.effortix.backend.models.Employee;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.TicketService;

@Component
public class EmailSchedulerCode {

	@Autowired
	TicketService ticketService;
	@Autowired
	EmailService emailService;
	@Autowired
	EmployeeService employeeService;
	@Scheduled(cron = "0 0 15 * * ?")
	 //@Scheduled(cron = "*/20 * * * * ?")
    public void helloWorld() {
		List<Ticket> liveTickets=ticketService.getTicketsByFlag(0);
		for(Ticket liveTicket: liveTickets) {
			System.out.println("Hello!!!");
          	 Long lFromEid=  liveTicket.getFromEmployee().geteId();
          	 Optional<Employee> fromEmployee = employeeService.getEmployeeById(lFromEid);
          	Long lToEid=  liveTicket.getToEmployee().geteId();
         	 Optional<Employee> toEmployee = employeeService.getEmployeeById(lToEid);
         	 
          	 String sNewTicketUpdate="Please Update the Ticket: "+liveTicket.getTId();
          	 String Content="Hi "+fromEmployee.get().geteName()+","
          	 		+ "Please Update the Ticket: "+liveTicket.getTName()+ "from the link: http://localhost:8088/ticketUpdates/ticket/"+liveTicket.getTId();
          	   emailService.sendSimpleMessage(fromEmployee.get().geteEmail(), sNewTicketUpdate, Content);
		}
            
		
    }
	
}
