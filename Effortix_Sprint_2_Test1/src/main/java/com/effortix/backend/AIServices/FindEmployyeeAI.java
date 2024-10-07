package com.effortix.backend.AIServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.effortix.backend.models.Employee;
import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.EmployeeSkillsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

@Component
public class FindEmployyeeAI {

    private static final String API_KEY = "AIzaSyCvv0Y7PzJrGCDfsAgBfSmncW8JW91vUpc";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyC0yDq9TByoPnCIwe8huyIaHnZNOOErTAU";
    
    @Autowired
    private EmployeeSkillsService employeeSkillsService;
    public void callerMethod() {
    	 Gson gson = new Gson();
     	List<EmployeeSkills> employeeSkills= employeeSkillsService.getAllEmployeeSkills();
     	String json = gson.toJson(employeeSkills);
         System.out.println("JSON output: " + json);
        // json="Find the employee who worked on or has similar work or previous worked with Impact Analysis from the json "+json;
         System.out.println("JSON output: " + json);
         ExecutorService executorService = Executors.newSingleThreadExecutor();

         // Submit the task for asynchronous execution
         executorService.submit(() -> {
        	 findEmployeeWithSkill("You are an AI assistant responsible for finding the Eid of employees who match specific skills or work experiences. The data may contain repetitive entries where the same employee Eid appears multiple times with different skills or work details. The term skill refers to a specific work area or task the employee has previously worked on. Only return the `Eid` of the matching employee. Do not include the skill or work detail in your response. If there are many employees with particular skill give only the top 3 employees suitable for the skill mentioned. Find the employee who worked on or has accurate and prisice work or previous worked with Teamcenter data model from", json);
        	    
         });
         
         System.out.println("Continuing with other operations while finding employees with skill...");

         // Shutdown the executor service after your tasks are complete
         executorService.shutdown();
         
          }
    
    @Autowired
    EmployeeService employeeService;
    public List<Employee> findEmployeeWithSkill(String ticketDescription, String EmployeeSkillsTable) {
        try {
            // Escape the EmployeeSkillsTable JSON string
            String escapedEmployeeSkillsTable = EmployeeSkillsTable.replace("\"", "\\\"");

            // Define the API URL
            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyCObxtlyDEDrzupiBXBcGZKz7u2az8zX_M";

            // Create URL object
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            connection.setRequestMethod("POST");

            // Set the request headers
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            // Allow sending the request body
            connection.setDoOutput(true);
            String SystemInstruciton= "You are an AI assistant responsible for finding the Eid of employees who match specific skills or work experiences."
            		+ "The data may contain repetitive entries where the same employee Eid appears multiple times with different skills or work details. "
            		+ "The term skill refers to a specific work area or task the employee has previously worked on. Only return the `Eid` of the matching employee. "
            		+ "Do not include the skill or work detail in your response. If there are many employees with particular skill give only the top 3 employees suitable for the skill mentioned in description of the ticket. "
            		+ "Find the employee who worked on or has accurate and prisice work or previous worked with the description of the ticket. The desciprion from the ticket is: "+ticketDescription +" from ";
            // JSON request payload
            String PostInstruction="Only give top 3 employees";
            String jsonInputString = "{ \"contents\": [{ \"parts\": [{ \"text\": \""+SystemInstruciton+ "\" }, { \"text\": \"" + escapedEmployeeSkillsTable  +"\" }] }] }";

            // Write JSON input string to the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Check if the response is successful
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response Body: " + response.toString());
                    System.out.println("============AI Sugested Employees===========");
                    System.out.println(response.toString());
                   
                    
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray candidates = jsonObject.getJSONArray("candidates");
                    
                    // Extract the "text" part
                    String text = candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");

                    // Use regex to find numbers
                    String[] numbers =text.split("[,\n]+");
                    List<Employee> AISuggestedEmployee=new ArrayList<>();
                    // Print each number
                    System.out.println("Only Id:");
                    for (String number : numbers) {
                        number = number.trim(); // Trim whitespace
                        if (number.matches("\\d+")) { // Check if it is a digit
                            System.out.println(number);
                            Optional<Employee> employee=employeeService.getEmployeeById(Long.parseLong(number));
                            if(employee.isPresent()) {
                                AISuggestedEmployee.add(employee.get());

                            }else {
                            	System.out.println("Employee Not Present...");
                            }
                        
                        }
                    }
                  
                    // Print the result
                    System.out.println();
                    return AISuggestedEmployee;
                }
            } else {
                // Handle non-2xx response codes (error response)
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    System.out.println("Error Response Body: " + errorResponse.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}

	


