
  package com.effortix.backend.services;
  
  import org.springframework.beans.factory.annotation.Autowired; import
  org.springframework.stereotype.Service;

import com.effortix.backend.AIServices.GenerateEmployeeSkillsAI;
import com.effortix.backend.AIServices.GenerateTimeSheetsEntriesAI;
import com.effortix.backend.EmailsUps.EmailService;
import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.EmployeeTimesheetEntries;
import com.effortix.backend.models.Project;
import com.effortix.backend.models.ProjectEmployee;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.models.TicketUpdates;
import com.effortix.backend.repository.EmployeeRepository;
import com.effortix.backend.repository.ProjectEmployeeRepository;
import com.effortix.backend.repository.TicketRepository;
import com.effortix.backend.repository.TicketUpdatesRepository;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
  
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
    
    @Autowired
    EmailService emailService;
   
	/*
	 * public TicketUpdates saveOrUpdateTicketUpdates(TicketUpdates ticketUpdates) {
	 * // Validate input
	 * 
	 * 
	 * System.out.println("TU___---______:"+ticketUpdates.getTuId()+
	 * ticketUpdates.gettUpdate()); TicketUpdates savedTicketUpdates
	 * =ticketUpdatesRepository.save(ticketUpdates); Date currentDate = new Date();
	 * if (savedTicketUpdates != null) { // Fetch existing timesheet entries for the
	 * employee on the given date // Date entryDate2 = ; // Assuming this is the
	 * date for the timesheet entry
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * // Get the current date String currentDateString = dateFormat.format(new
	 * Date());
	 * 
	 * // Convert the string to a Date object try { currentDate =
	 * dateFormat.parse(currentDateString); System.out.println("Current Date: " +
	 * currentDate); } catch (ParseException e) { e.printStackTrace(); } Date
	 * entryDate = currentDate; // Assuming this is the date for the timesheet entry
	 * 
	 * Long employeeId = savedTicketUpdates.getEmployee().geteId(); // Get the
	 * employee ID
	 * 
	 * List<EmployeeTimesheetEntries> existingEntries =
	 * timesheetEntriesService.findByEmployeeIdAndDate(employeeId, entryDate);
	 * 
	 * EmployeeTimesheetEntries timesheetEntry = new EmployeeTimesheetEntries();
	 * timesheetEntry.setEmployee(savedTicketUpdates.getEmployee());
	 * timesheetEntry.setDate(entryDate);
	 * timesheetEntry.setTicket(savedTicketUpdates.getTicket());
	 * timesheetEntry.setProject(savedTicketUpdates.getProject());
	 * 
	 * if (!existingEntries.isEmpty()) { // Update existing entry
	 * EmployeeTimesheetEntries existingEntry = existingEntries.get(0); // Assuming
	 * only one entry per day String
	 * AIEntry=callTimeSheetAI(savedTicketUpdates.getTicket().getTId(),
	 * savedTicketUpdates); String updatedActivity = existingEntry.getEtActivity() +
	 * ", " + AIEntry; existingEntry.setEtActivity(updatedActivity);
	 * 
	 * // Save the updated entry
	 * 
	 * timesheetEntriesService.createTimesheetEntry(existingEntry);
	 * 
	 * 
	 * } else { // No existing entry, create a new one
	 * timesheetEntry.setEtActivity(savedTicketUpdates.gettUpdate() + " FROM AI");
	 * timesheetEntriesService.createTimesheetEntry(timesheetEntry);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * callReplyEmailMethod(savedTicketUpdates);
	 * 
	 * ExecutorService executorService = Executors.newSingleThreadExecutor();
	 * 
	 * // Submit the task for asynchronous execution executorService.submit(() -> {
	 * callEmployeeSkillsAI(savedTicketUpdates.getTicket().getTId(),
	 * savedTicketUpdates); });
	 * 
	 * 
	 * }
	 * 
	 * 
	 * return savedTicketUpdates; }
	 */
    
    
    public TicketUpdates saveOrUpdateTicketUpdates(TicketUpdates ticketUpdates) {
        // Validate input
        System.out.println("TU___---______:" + ticketUpdates.getTuId() + ticketUpdates.gettUpdate());
        
        TicketUpdates savedTicketUpdates = ticketUpdatesRepository.save(ticketUpdates);
        Date currentDate = new Date();

        if (savedTicketUpdates != null) {
            // Convert the current date to string and parse it
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateString = dateFormat.format(new Date());
            try {
                currentDate = dateFormat.parse(currentDateString);
                System.out.println("Current Date: " + currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            Date entryDate = currentDate; // Assuming this is the date for the timesheet entry
            Long employeeId = savedTicketUpdates.getEmployee().geteId(); // Get the employee ID
            
            // Use ExecutorService to parallelize tasks
            ExecutorService executorService = Executors.newFixedThreadPool(4); // Using a thread pool for efficiency

            // Task 1: Fetch existing timesheet entries (this can be done concurrently)
            executorService.submit(() -> {
                List<EmployeeTimesheetEntries> existingEntries = timesheetEntriesService.findByEmployeeIdAndDate(employeeId, entryDate);
                
                EmployeeTimesheetEntries timesheetEntry = new EmployeeTimesheetEntries();
                timesheetEntry.setEmployee(savedTicketUpdates.getEmployee());
                timesheetEntry.setDate(entryDate);
                timesheetEntry.setTicket(savedTicketUpdates.getTicket());
                timesheetEntry.setProject(savedTicketUpdates.getProject());
                
                if (!existingEntries.isEmpty()) {
                    // Update existing entry
                    EmployeeTimesheetEntries existingEntry = existingEntries.get(0); // Assuming only one entry per day
                    String AIEntry = callTimeSheetAI(savedTicketUpdates.getTicket().getTId(), savedTicketUpdates);
                    String updatedActivity = existingEntry.getEtActivity() + ", " + AIEntry;
                    existingEntry.setEtActivity(updatedActivity);
                    
                    // Save the updated entry
                    timesheetEntriesService.createTimesheetEntry(existingEntry);
                } else {
                    // No existing entry, create a new one
                	String AIEntry = callTimeSheetAI(savedTicketUpdates.getTicket().getTId(), savedTicketUpdates);
                    timesheetEntry.setEtActivity(AIEntry);
                    timesheetEntriesService.createTimesheetEntry(timesheetEntry);
                }
            });

            // Task 2: Call Reply Email Method (asynchronous, independent of other tasks)
            executorService.submit(() -> {
                callReplyEmailMethod(savedTicketUpdates);
            });
            
            // Task 3: Call Employee Skills AI (asynchronous, independent of other tasks)
            executorService.submit(() -> {
                callEmployeeSkillsAI(savedTicketUpdates.getTicket().getTId(), savedTicketUpdates);
            });

            // Shut down the executor after submitting tasks
            executorService.shutdown();
        }

        return savedTicketUpdates;
    }
    
    @Autowired
    TicketService ticketService;
    @Autowired
    EmployeeSkillsService employeeSkillsService;
    @Autowired
    GenerateEmployeeSkillsAI employeeSkillaI; // Assuming this class handles the API call

    public void callEmployeeSkillsAI(Long ticketID, TicketUpdates ticketUpdates) {
        Optional<Ticket> ticketOptional = ticketService.getTicketById(ticketID);
        if (!ticketOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }
        
        Ticket ticket = ticketOptional.get();
        String ticketDetailJson = new Gson().toJson(ticket);
        String theUpdate = ticketUpdates.gettUpdate();
        
        // Call AI to get new skills and work experience
        System.out.println("Before AI: "+theUpdate+" ticketDetailJson "+ ticketDetailJson);
        
        Map<String, String> skillAndWorkExp = employeeSkillaI.generateSkillsAndUpdatePreviousWorks(ticketDetailJson, theUpdate);

        // Fetch the employee skills for the current employee
        List<EmployeeSkills> employeeSkillsList = employeeSkillsService.getEmployeeSkillsByEId(ticketUpdates.getEmployee().geteId());
        
        // Update each EmployeeSkills entry
        for (EmployeeSkills employeeSkill : employeeSkillsList) {
            // Append new skills and work experience to existing entries
            String previousSkills = employeeSkill.getSkills();
            String previousWorks = employeeSkill.getSkills_detail();
            
            // Append AI-generated skills and work experience
            String newSkills = skillAndWorkExp.get("skills");
            String newWorks = skillAndWorkExp.get("work_experience");
            System.out.println("newSkills: "+newSkills+" newWorks: "+newWorks);
            
            employeeSkill.setSkills(previousSkills + ", " + newSkills);
            employeeSkill.setSkills_detail(previousWorks + ", " + newWorks);
            
            // Save updated employee skills
            employeeSkillsService.saveOrUpdateEmployeeSkills(employeeSkill);
            System.out.println("ESID: "+employeeSkill.getEsID());
        }
     
        if(employeeSkillsList.size()==0) {
        	   EmployeeSkills employeeSkills =new EmployeeSkills();
        	 String newSkills = skillAndWorkExp.get("skills");
             String newWorks = skillAndWorkExp.get("work_experience");
            
            
             employeeSkills.setSkills( newSkills);
             employeeSkills.setSkills_detail(newWorks);
             employeeSkills.setEId(ticket.getToEmployee().geteId());
             employeeSkillsService.saveOrUpdateEmployeeSkills(employeeSkills);
             System.out.println("ESID: "+employeeSkills.getEsID());

        }
    }

    @Autowired
    GenerateTimeSheetsEntriesAI sheetsEntriesAI;
    
    public String callTimeSheetAI(Long ticketID, TicketUpdates ticketUpdates) {
        Optional<Ticket> ticketOptional = ticketService.getTicketById(ticketID);
        if (!ticketOptional.isPresent()) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }
        
        Ticket ticket = ticketOptional.get();
        String ticketDetailJson = new Gson().toJson(ticket);
        String theUpdate = ticketUpdates.gettUpdate();
        
        // Call AI to get new skills and work experience
        System.out.println("Before AI: "+theUpdate+" ticketDetailJson "+ ticketDetailJson);
      String AIResponce= sheetsEntriesAI.generateTimeSheetEntries(ticketDetailJson, theUpdate);

       return AIResponce;
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
    
        @Autowired
        EmployeeService employeeService;
        public void callReplyEmailMethod(TicketUpdates ticketUpdates) {
        	 Optional<Ticket> ticketOptional = ticketService.getTicketById(ticketUpdates.getTicket().getTId());
             if (ticketOptional.isPresent()) {
          	 Long lFromEid=  ticketOptional.get().getFromEmployee().geteId();
          	 Optional<Employee> fromEmployee = employeeService.getEmployeeById(lFromEid);
          	Long lToEid=  ticketOptional.get().getToEmployee().geteId();
         	 Optional<Employee> toEmployee = employeeService.getEmployeeById(lToEid);
         	 
          	 String sNewTicketUpdate="New Update in Ticket #"+ticketOptional.get().getTId();
          	 String Content="Hello " + fromEmployee.get().geteName() + ",\n\n" +
                     "Good day! We would like to inform you about a new update on your ticket:\n\n" +
                     "Ticket ID: " + ticketOptional.get().getTId() + "\n" +
                     "Ticket Name: " + ticketOptional.get().getTName() + "\n\n" +
                     "New Update: " + ticketUpdates.gettUpdate() + "\n\n" +
                     "Please review the details at your earliest convenience.\n\n" +
                     "Best regards,\n" +
                     "Your Support Team";
          	   emailService.sendSimpleMessage(fromEmployee.get().geteEmail(), sNewTicketUpdate, Content);
             }
        }

    
}