package com.effortix.backend.controllers;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeTimesheetEntries;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.EmployeeTimesheetEntriesService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/timesheet")
public class EmployeeTimesheetEntriesController {

    @Autowired
    private EmployeeTimesheetEntriesService timesheetService;

    // Get all timesheet entries for a specific employee by ID
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<EmployeeTimesheetEntries>> getTimesheetEntriesByEmployeeId(@PathVariable Long employeeId) {
        List<EmployeeTimesheetEntries> entries = timesheetService.getTimesheetEntriesByEmployeeId(employeeId);
        return ResponseEntity.ok(entries);
    }

	/*
	 * // Get all timesheet entries for a specific employee by ID and within a date
	 * range
	 * 
	 * @GetMapping("/employee/{employeeId}/dates") public
	 * ResponseEntity<List<EmployeeTimesheetEntries>>
	 * getTimesheetEntriesByEmployeeIdAndDate(
	 * 
	 * @PathVariable Long employeeId,
	 * 
	 * @RequestParam("fromDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date
	 * fromDate,
	 * 
	 * @RequestParam("toDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
	 * List<EmployeeTimesheetEntries> entries =
	 * timesheetService.getTimesheetEntriesByEmployeeIdAndDate(employeeId, fromDate,
	 * toDate); return ResponseEntity.ok(entries); }
	 */
    // Create a new timesheet entry
    @PostMapping
    public ResponseEntity<EmployeeTimesheetEntries> createTimesheetEntry(@RequestBody EmployeeTimesheetEntries timesheetEntry) {
        EmployeeTimesheetEntries createdEntry = timesheetService.createTimesheetEntry(timesheetEntry);
        return ResponseEntity.ok(createdEntry);
    }

    // Update an existing timesheet entry
    @PutMapping("/{etId}")
    public ResponseEntity<EmployeeTimesheetEntries> updateTimesheetEntry(
            @PathVariable Long etId,
            @RequestBody EmployeeTimesheetEntries updatedEntry) {
        Optional<EmployeeTimesheetEntries> updated = timesheetService.updateTimesheetEntry(etId, updatedEntry);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a timesheet entry by ID
    @DeleteMapping("/{etId}")
    public ResponseEntity<Void> deleteTimesheetEntry(@PathVariable Long etId) {
        timesheetService.deleteTimesheetEntry(etId);
        return ResponseEntity.noContent().build();
    }
    
    
    
    
    //Updated!!!!!!!!
    
   
    
    @Autowired
    private EmployeeService employeeService; // Assuming you have a service to fetch employees

    
    
    @GetMapping("/timesheet")
    public String showTimesheetPage(org.springframework.ui.Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("selectedEmployeeId", null);
        model.addAttribute("selectedMonth", null);
        model.addAttribute("timesheetEntries", new ArrayList<>());
        return "timesheetUI/timesheetUser";
    }

    
    
	/*
	 * @GetMapping("/employee/{employeeId}/dates")
	 * 
	 * @ResponseBody // Ensure this is present if returning JSON public
	 * List<EmployeeTimesheetEntries> getTimesheetEntriesByEmployeeIdAndDate(
	 * 
	 * @PathVariable Long employeeId,
	 * 
	 * @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date
	 * fromDate,
	 * 
	 * @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate)
	 * { // Add error handling try { return
	 * timesheetService.getTimesheetEntriesByEmployeeIdAndDate(employeeId, fromDate,
	 * toDate); } catch (Exception e) { // Log the exception e.printStackTrace();
	 * throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	 * "An error occurred while fetching data."); } }
	 */

    @GetMapping("/employee/{employeeId}/dates")
    @ResponseBody
    public List<EmployeeTimesheetEntries> getTimesheetEntriesByEmployeeIdAndDate(
            @PathVariable Long employeeId,
            @RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        // Error handling and data fetching logic
        try {
            return timesheetService.getTimesheetEntriesByEmployeeIdAndDate(employeeId, fromDate, toDate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching data.");
        }
    }

    
    
    
}