package com.effortix.backend.testers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.effortix.backend.models.ProjectEmployee;
import com.effortix.backend.services.ProjectEmployeeService;

@Component
public class ProjectEmployeeTester {

    @Autowired
    private ProjectEmployeeService projectEmployeeService;

    // Method to print all ProjectEmployee records to the console
    public void printAllProjectEmployees() {
        List<ProjectEmployee> projectEmployees = projectEmployeeService.getAllProjectEmployees();

        if (projectEmployees.isEmpty()) {
            System.out.println("No ProjectEmployee records found.");
        } else {
            System.out.println("ProjectEmployee Records:");
            for (ProjectEmployee projectEmployee : projectEmployees) {
                System.out.println("ProjectEmployee ID: " + projectEmployee.getPeID());
                System.out.println("Project Name: " + projectEmployee.getProject().getpName());
                System.out.println("Employee Name: " + projectEmployee.getEmployee().geteName());
                System.out.println("--------------------------------------------------");
            }
        }
    }

    // Method to print a single ProjectEmployee record by ID
    public void printProjectEmployeeById(Long peID) {
        Optional<ProjectEmployee> projectEmployee = projectEmployeeService.getProjectEmployeeById(peID);
        if (projectEmployee.isPresent()) {
            ProjectEmployee pe = projectEmployee.get();
            System.out.println("ProjectEmployee ID: " + pe.getPeID());
            System.out.println("Project Name: " + pe.getProject().getpName());
            System.out.println("Employee Name: " + pe.getEmployee().geteName());
        } else {
            System.out.println("ProjectEmployee with ID " + peID + " not found.");
        }
    }
}

