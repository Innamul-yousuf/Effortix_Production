
  package com.effortix.backend.services;
  

import com.effortix.backend.models.EmployeeTimesheetEntries;
import com.effortix.backend.repository.EmployeeTimesheetEntriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeTimesheetEntriesService {

    @Autowired
    private EmployeeTimesheetEntriesRepository employeeTimesheetEntriesRepository;

    // Get all timesheet entries for a specific employee by ID
    public List<EmployeeTimesheetEntries> getTimesheetEntriesByEmployeeId(Long employeeId) {
        return employeeTimesheetEntriesRepository.findAllByEmployeeId(employeeId);
    }

    // Get all timesheet entries for a specific employee within a date range
    public List<EmployeeTimesheetEntries> getTimesheetEntriesByEmployeeIdAndDate(Long employeeId, Date fromDate, Date toDate) {
        return employeeTimesheetEntriesRepository.findAllByEmployeeIdAndDateBetween(employeeId, fromDate, toDate);
    }

    // Create a new timesheet entry
    public EmployeeTimesheetEntries createTimesheetEntry(EmployeeTimesheetEntries timesheetEntry) {
        return employeeTimesheetEntriesRepository.save(timesheetEntry);
    }

    // Update an existing timesheet entry
    public Optional<EmployeeTimesheetEntries> updateTimesheetEntry(Long etId, EmployeeTimesheetEntries updatedEntry) {
        Optional<EmployeeTimesheetEntries> existingEntry = employeeTimesheetEntriesRepository.findById(etId);
        if (existingEntry.isPresent()) {
            EmployeeTimesheetEntries entry = existingEntry.get();
            entry.setEmployee(updatedEntry.getEmployee());
            entry.setTicket(updatedEntry.getTicket());
            entry.setProject(updatedEntry.getProject());
            entry.setDate(updatedEntry.getDate());
            entry.setEtActivity(updatedEntry.getEtActivity());
            return Optional.of(employeeTimesheetEntriesRepository.save(entry));
        }
        return Optional.empty();
    }

    // Delete a timesheet entry by ID
    public void deleteTimesheetEntry(Long etId) {
        employeeTimesheetEntriesRepository.deleteById(etId);
    }
    
    
    public List<EmployeeTimesheetEntries> findByEmployeeIdAndDate(Long employeeId, Date date) {
        return employeeTimesheetEntriesRepository.findByEmployeeIdAndDate(employeeId, date);
    }
    
    
    //---------updated
//    @Query("SELECT ete FROM EmployeeTimesheetEntries ete WHERE ete.employee.eId = :employeeId AND MONTH(ete.date) = :month")
//    List<EmployeeTimesheetEntries> findByEmployeeIdAndMonth(@Param("employeeId") Long employeeId, @Param("month") int month);
}


 


