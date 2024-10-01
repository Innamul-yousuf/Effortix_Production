package com.effortix.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.effortix.backend.models.Employee;
import com.effortix.backend.services.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @GetMapping
    public String listEmployees(Model model) {
    	System.out.println("Helllo");
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employeeUI/employee_list"; // Thymeleaf template name (employee_list.html)
    }

  

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.saveOrUpdateEmployee(employee);
        return "redirect:/employees";
    }
    
    
    
    @GetMapping("/new")
    public String showEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());

        // Assuming there's a method to fetch all leads (employees who are leads)
        List<Employee> leads = employeeService.getAllLeads(); 
        model.addAttribute("leads", leads); // Adding the list of leads to the model

        return "employeeUI/employee_form"; // Thymeleaf template name (employee_form.html)
    }
    
    @GetMapping
    @RequestMapping("/charts")
    public String listEmployeesForChart(Model model) {
        // Get the list of all employees
        List<Employee> employees = employeeService.getAllEmployees();

        // Calculate the number of employees on bench and not on bench
        long benchCount = employeeService.getBenchEmployeeCount();
        long nonBenchCount = employeeService.getNonBenchEmployeeCount();

        // Add the data to the model
        model.addAttribute("benchCount", benchCount);
        model.addAttribute("nonBenchCount", nonBenchCount);
        model.addAttribute("employees", employees);

        // Return the Thymeleaf template name (employee_list.html)
        return "pages/employeeChart";
    }
}

