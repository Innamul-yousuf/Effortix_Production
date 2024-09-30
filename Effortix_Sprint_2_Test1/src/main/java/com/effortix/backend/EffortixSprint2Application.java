package com.effortix.backend;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.effortix.backend.AIServices.FindEmployyeeAI;
import com.effortix.backend.EmailsUps.EmailService;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.services.EmployeeSkillsService;
import com.effortix.backend.testers.GeminiTesterClass;
import com.effortix.backend.testers.ProjectEmployeeTester;
import com.effortix.backend.testers.TicketTester;

@SpringBootApplication
@EnableScheduling
public class EffortixSprint2Application {
	@Autowired
	private ProjectEmployeeTester projectEmployeeTester;

	public static void main(String[] args) {
		SpringApplication.run(EffortixSprint2Application.class, args);
		System.out.println("		Hello, World!     ");
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			// Print all ProjectEmployee records to the console
			projectEmployeeTester.printAllProjectEmployees();

			// You can also print a single ProjectEmployee by ID
			projectEmployeeTester.printProjectEmployeeById(1L); // Replace with actual ID
		};
	}

	@Autowired
	private TicketTester ticketTester;

	@Bean
	public CommandLineRunner demo1() {
		return (args) -> {
			System.out.println("________STARTED TICKETTESTER________");
			// Test all service methods in ProjectEmployeeTester
			ticketTester.testAllMethods();
		};
	}

	@Autowired
	private GeminiTesterClass geminiTesterClass;

	@Bean
	public CommandLineRunner callGemini() {
		return (args) -> {
			System.out.println("________STARTED Gemini API________");
			// Test all service methods in ProjectEmployeeTester
			// geminiTesterClass.runAPI();
		};
	}

	@Autowired
	private EmailService emailService;

	@Bean
	public CommandLineRunner sendEmail() {
		return (args) -> {
			System.out.println("________Started Sending Email________");
			
			/*
			 * mohammed.m@intelizign.com mohammedayub.a@intelizign.com
			 * faraazahmed.c@intelizign.com
			 
			  emailService.sendSimpleMessage("innamulyousufzath.r@intelizign.com",
			  "Hello, World!", "Hey Team Effortix!\n"+ "\n" +
			  "We’re excited to share that our Sprint 2 is going exceptionally well! Your hard work and dedication are truly making a difference, and it’s inspiring to see everyone’s contributions come together so seamlessly.\n"
			  + "\n" +
			  "Let’s keep the momentum going! Your involvement is key to our success, and every effort counts. Together, we’re not just completing tasks; we’re building something amazing!\n"
			  + "\n" +
			  "Keep up the fantastic work, and let’s make this sprint our best one yet!\n"
			  + "\n" + "Cheers,\n" + "Hello, World!"); };
			 */

		};

	}

	@Autowired
	private EmployeeSkillsService employeeSkillsService;

	@Bean
	public CommandLineRunner callTest() {
		return (args) -> {
			System.out.println("________STARTED Testing EmployeeSkills API________");

			/*
			 * // 1. Test create or update employee skills EmployeeSkills newSkill = new
			 * EmployeeSkills(); newSkill.setEId(101L);
			 * newSkill.setSkills("Java, Spring Boot");
			 * newSkill.setSkills_detail("Backend Development");
			 * 
			 * EmployeeSkills savedSkill =
			 * employeeSkillsService.saveOrUpdateEmployeeSkills(newSkill);
			 * System.out.println("Saved EmployeeSkills: " + savedSkill.getEsID());
			 * 
			 * // 2. Test retrieving employee skills by ID Optional<EmployeeSkills>
			 * retrievedSkill =
			 * employeeSkillsService.getEmployeeSkillsById(savedSkill.getEsID()); if
			 * (retrievedSkill.isPresent()) {
			 * System.out.println("Retrieved EmployeeSkills by ID: " +
			 * retrievedSkill.get().getSkills()); } else {
			 * System.out.println("EmployeeSkills not found."); }
			 */

		};
	}

	@Bean
	public CommandLineRunner receiveEmail() {
		return (args) -> {
			System.out.println("________Started Sending Email________");

			// emailService.readEmails();
			System.out.println("Mail reading complete!");

		};
	}
	
	@Autowired
	private FindEmployyeeAI employyeeAI;
	@Bean
	public CommandLineRunner callFindResourceCode() {
		return (args) -> {
			System.out.println("________Started Finding Resource________");

			employyeeAI.callerMethod();
			

		};
	}
	
}
