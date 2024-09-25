
  package com.effortix.backend.services;
  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effortix.backend.models.EmployeeCredits;
import com.effortix.backend.repository.EmployeeCreditsRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeCreditsService {

    @Autowired
    private EmployeeCreditsRepository employeeCreditsRepository;

    // Save or update EmployeeCredits
    public EmployeeCredits saveOrUpdateEmployeeCredits(EmployeeCredits employeeCredits) {
        return employeeCreditsRepository.save(employeeCredits);
    }

    // Find all EmployeeCredits
    public List<EmployeeCredits> getAllEmployeeCredits() {
        return employeeCreditsRepository.findAll();
    }

    // Find EmployeeCredits by ID
    public Optional<EmployeeCredits> getEmployeeCreditsById(Long id) {
        return employeeCreditsRepository.findById(id);
    }

    // Find EmployeeCredits by Employee ID
    
    // Find EmployeeCredits by Employee ID and date range
    public List<EmployeeCredits> getEmployeeCreditsByEmployeeIdAndDateRange(Long eId, Date startDate, Date endDate) {
        return employeeCreditsRepository.findByEmployeeAndDateBetween(eId, startDate, endDate);
    }

    // Delete EmployeeCredits by ID
    public void deleteEmployeeCreditsById(Long id) {
        employeeCreditsRepository.deleteById(id);
    }
}