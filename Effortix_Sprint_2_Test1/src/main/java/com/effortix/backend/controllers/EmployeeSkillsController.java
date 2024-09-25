package com.effortix.backend.controllers;

import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.services.EmployeeSkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmployeeSkillsController {

    @Autowired
    private EmployeeSkillsService employeeSkillsService;

    // Controller method to display all EmployeeSkills entries
    @GetMapping("/employeeSkills")
    public String getAllEmployeeSkills(Model model) {
        List<EmployeeSkills> skillsList = employeeSkillsService.getAllEmployeeSkills();
        model.addAttribute("skillsList", skillsList);
        return "skills/employeeSkills"; // Return Thymeleaf template name
    }
}