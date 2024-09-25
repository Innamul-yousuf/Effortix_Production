package com.effortix.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.TicketAssignment;
import com.effortix.backend.repository.EmployeeRepository;
import com.effortix.backend.repository.EmployeeSkillsRepository;
import com.effortix.backend.repository.TicketAssignmentRepository;

@Service
public class EmployeeSkillsService {

    @Autowired
    private EmployeeSkillsRepository employeeSkillsRepository;

    // Create or update employee skills
    public EmployeeSkills saveOrUpdateEmployeeSkills(EmployeeSkills employeeSkills) {
        return employeeSkillsRepository.save(employeeSkills);
    }

    // Get all employee skills
    public List<EmployeeSkills> getAllEmployeeSkills() {
        return employeeSkillsRepository.findAll();
    }

    // Get employee skills by ID
    public Optional<EmployeeSkills> getEmployeeSkillsById(Long esID) {
        return employeeSkillsRepository.findById(esID);
    }

    // Delete employee skills by ID
    public void deleteEmployeeSkillsById(Long esID) {
        employeeSkillsRepository.deleteById(esID);
    }

    // Get employee skills by Employee ID
    public List<EmployeeSkills> getEmployeeSkillsByEId(Long EId) {
        return employeeSkillsRepository.findByEId(EId);
    }
}
