package com.effortix.backend.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.effortix.backend.models.EmployeeCredits;
import com.effortix.backend.services.EmployeeCreditsService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employee-credits")
public class EmployeeCreditsController {

    @Autowired
    private EmployeeCreditsService employeeCreditsService;

    // Save or update EmployeeCredits
    @PostMapping
    public EmployeeCredits saveOrUpdateEmployeeCredits(@RequestBody EmployeeCredits employeeCredits) {
        return employeeCreditsService.saveOrUpdateEmployeeCredits(employeeCredits);
    }

    // Get all EmployeeCredits
    @GetMapping
    public List<EmployeeCredits> getAllEmployeeCredits() {
        return employeeCreditsService.getAllEmployeeCredits();
    }

    // Get EmployeeCredits by ID
    @GetMapping("/{id}")
    public Optional<EmployeeCredits> getEmployeeCreditsById(@PathVariable Long id) {
        return employeeCreditsService.getEmployeeCreditsById(id);
    }

   
    // Get EmployeeCredits by Employee ID and date range
    @GetMapping("/employee/{eId}/date-range")
    public List<EmployeeCredits> getEmployeeCreditsByEmployeeIdAndDateRange(
            @PathVariable Long eId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return employeeCreditsService.getEmployeeCreditsByEmployeeIdAndDateRange(eId, startDate, endDate);
    }

    // Delete EmployeeCredits by ID
    @DeleteMapping("/{id}")
    public void deleteEmployeeCreditsById(@PathVariable Long id) {
        employeeCreditsService.deleteEmployeeCreditsById(id);
    }
}