
  package com.effortix.backend.services;
  
  import org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.stereotype.Service;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeTimesheetEntries;
import com.effortix.backend.models.Project;
import com.effortix.backend.models.ProjectEmployee;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.models.TicketUpdates;
import com.effortix.backend.repository.EmployeeRepository;
import com.effortix.backend.repository.ProjectEmployeeRepository;
import com.effortix.backend.repository.TicketRepository;
import com.effortix.backend.repository.TicketUpdatesRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List; import java.util.Optional;
  
@Service
public class TicketUpdatesService{

    @Autowired
    private TicketUpdatesRepository ticketUpdatesRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private EmployeeTimesheetEntriesService timesheetEntriesService;
    
   
    
    public List<TicketUpdates> getTicketUpdatesByEmployeeAndDateRange(
            Long employeeId, Date currentDate) {
        return ticketUpdatesRepository.findByEmployeeAndDateInRange(employeeId, currentDate);
    }
    
   
    public TicketUpdates saveOrUpdateTicketUpdates(TicketUpdates ticketUpdates) {
        // Validate input
     
     
       System.out.println("TU___---______:"+ticketUpdates.getTuId()+ ticketUpdates.gettUpdate());
       TicketUpdates savedTicketUpdates =ticketUpdatesRepository.save(ticketUpdates);
       Date currentDate = new Date();
       if (savedTicketUpdates != null) {
           // Fetch existing timesheet entries for the employee on the given date
          // Date entryDate2 = ; // Assuming this is the date for the timesheet entry
        		   
        		   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

           // Get the current date
           String currentDateString = dateFormat.format(new Date());

           // Convert the string to a Date object
           try {
               currentDate = dateFormat.parse(currentDateString);
               System.out.println("Current Date: " + currentDate);
           } catch (ParseException e) {
               e.printStackTrace();
           }
           Date entryDate = currentDate; // Assuming this is the date for the timesheet entry

           Long employeeId = savedTicketUpdates.getEmployee().geteId(); // Get the employee ID
           
           List<EmployeeTimesheetEntries> existingEntries = timesheetEntriesService.findByEmployeeIdAndDate(employeeId, entryDate);
           
           EmployeeTimesheetEntries timesheetEntry = new EmployeeTimesheetEntries();
           timesheetEntry.setEmployee(savedTicketUpdates.getEmployee());
           timesheetEntry.setDate(entryDate);
           timesheetEntry.setTicket(savedTicketUpdates.getTicket());
           timesheetEntry.setProject(savedTicketUpdates.getProject());
           
           if (!existingEntries.isEmpty()) {
               // Update existing entry
               EmployeeTimesheetEntries existingEntry = existingEntries.get(0); // Assuming only one entry per day
               String updatedActivity = existingEntry.getEtActivity() + ", " + savedTicketUpdates.gettUpdate() + " FROM AI";
               existingEntry.setEtActivity(updatedActivity);
               
               // Save the updated entry
               timesheetEntriesService.createTimesheetEntry(existingEntry);
           } else {
               // No existing entry, create a new one
               timesheetEntry.setEtActivity(savedTicketUpdates.gettUpdate() + " FROM AI");
               timesheetEntriesService.createTimesheetEntry(timesheetEntry);
           }
       }
       
       
       // Create a new timesheet entry
//       public EmployeeTimesheetEntries createTimesheetEntry(EmployeeTimesheetEntries timesheetEntry) {
//           return employeeTimesheetEntriesRepository.save(timesheetEntry);
//       }
       
		/*
		 * if(ticketUpdates2 != null) {
		 * 
		 * timesheetEntries.setEmployee(ticketUpdates2.getEmployee());
		 * timesheetEntries.setDate(ticketUpdates2.getDateTime());
		 * timesheetEntries.setEtActivity(ticketUpdates2.gettUpdate()+" FROM AI");
		 * 
		 * timesheetEntries.setTicket(ticketUpdates2.getTicket());
		 * timesheetEntries.setProject(ticketUpdates2.getProject());
		 * 
		 * timesheetEntriesService.createTimesheetEntry(timesheetEntries); }
		 */
        return savedTicketUpdates;
    }

    
    public Optional<TicketUpdates> getTicketUpdatesById(Long tuId) {
        return ticketUpdatesRepository.findById(tuId);
    }

 
    public List<TicketUpdates> getAllTicketUpdates() {
        return ticketUpdatesRepository.findAll();
    }

  
    public void deleteTicketUpdatesById(Long tuId) {
        ticketUpdatesRepository.deleteById(tuId);
    }
    
    
        // Fetch updates by ticketId, employeeId, and projectId
        public List<TicketUpdates> getUpdatesByTicketEmployeeProject(Long ticketId, Long employeeId, Long projectId) {
            return ticketUpdatesRepository.findByTicketIdAndEmployeeIdAndProjectId(ticketId, employeeId, projectId);
        }
    

    
}