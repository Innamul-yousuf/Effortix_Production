package com.effortix.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effortix.backend.models.Employee;
import com.effortix.backend.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> getEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}

	public Employee saveOrUpdateEmployee(Employee employee) {
		System.out.println("Employee Name: "+ employee.geteName());
		return employeeRepository.save(employee);
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}
	
	public Employee findByIdAndPassword(Long eId, String ePassword) {
        return employeeRepository.findByEIdAndEPassword(eId, ePassword);
    }
	
	 public List<Employee> getAllLeads() {
	        return employeeRepository.findAllLeads();
	    }
	 
	 
	 // Get the count of employees where eInBench is true
	    public long getBenchEmployeeCount() {
	        return employeeRepository.countByEInBenchTrue();
	    }

	    // Get the count of employees where eInBench is false
	    public long getNonBenchEmployeeCount() {
	        return employeeRepository.countByEInBenchFalse();
	    }

	    // Get all employees where eInBench is true
	    public List<Employee> getBenchEmployees() {
	        return employeeRepository.findByEInBenchTrue();
	    }

	    // Get all employees where eInBench is false
	    public List<Employee> getNonBenchEmployees() {
	        return employeeRepository.findByEInBenchFalse();
	    }
	    
	    public Employee authenticateEmployee(Long eId, String ePassword) {
	        Optional<Employee> employeeOptional = getEmployeeById(eId);
	        if (employeeOptional.isPresent()) {
	            Employee employee = employeeOptional.get();
	            
	            // Compare the provided password with the stored password using BCrypt
	            if (employee != null && employee.getePassword().equals(ePassword)) {
	                return employee; // Return employee if authentication is successful
	            }
	        }
	       
	        return null; // Return null if authentication fails
	    }
}
