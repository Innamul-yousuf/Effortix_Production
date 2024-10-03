package com.effortix.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeLeads;
import com.effortix.backend.repository.EmployeeLeadsRepository;
import com.effortix.backend.repository.EmployeeRepository;
@Service
public class EmployeeLeadsService {

    @Autowired
    private EmployeeLeadsRepository employeeLeadsRepository;

    public EmployeeLeads saveEmployeeLead(EmployeeLeads employeeLead) {
        return employeeLeadsRepository.save(employeeLead);
    }

    public List<EmployeeLeads> findAllEmployeeLeads() {
        return employeeLeadsRepository.findAll();
    }

    public Optional<EmployeeLeads> findEmployeeLeadsByEmployee(Employee employee) {
        return employeeLeadsRepository.findByEmployee(employee);
    }

    
}