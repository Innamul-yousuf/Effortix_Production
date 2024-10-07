package com.effortix.backend.AIServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.services.EmployeeSkillsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

@Component
public class GenerateHotTasks {

	
	@Autowired
	private EmployeeSkillsService employeeSkillsService;

	public void callerMethod() {
		Gson gson = new Gson();
		List<EmployeeSkills> employeeSkills = employeeSkillsService.getAllEmployeeSkills();
		String json = gson.toJson(employeeSkills);
		System.out.println("JSON output: " + json);
		
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		// Submit the task for asynchronous execution
		executorService.submit(() -> {
			
		});

		System.out.println("Continuing with other operations while finding employees with skill...");

		executorService.shutdown();

	}

	public List<Ticket> generateSuitableTasks(String employeeSkills, String primePicks) {
		try {
			// Escape the EmployeeSkillsTable JSON string
			String escapedEmployeeSkills = employeeSkills.replace("\"", "\\\"");
			String escapedPrimePicks = primePicks.replace("\"", "\\\"");

			// Define the API URL
			String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyC0yDq9TByoPnCIwe8huyIaHnZNOOErTAU";

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
			String systemInstruction="You are an AI assistant responsible for generating suitable tasks for a bench resource based on his previous skills and comparing it with the primepicks list which are give by the Delevery groups for the booming technologies or upcoming projects requirements. you match the employee skills and the prime picks suitable for him and generate a maximum 5 task with a breaf description about the task, task name, links if any and online reference files if any. Please only give the details of the task in json format and not any other instruction about the your respose. Also don't give user skill in responce only the tasks. The Json must have taskName, description, link(Empty), referenceFile(Empty) ";
			// JSON request payload
			String jsonInputString = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" +systemInstruction+" "+ escapedEmployeeSkills
					+ "\" }, { \"text\": \"" + escapedPrimePicks + "\" }] }] }";

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
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(connection.getInputStream(), "utf-8"))) {
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
			        
			        // Extract the "text" part from the first candidate
			        String text = candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");

			        // Remove the leading and trailing backticks and "json" identifier from the text
			        String jsonText = text.replace("```json", "").replace("```", "").trim();
			        
			        // Now parse the inner JSON (array of tasks)
			        JSONArray tasksArray = new JSONArray(jsonText);

			        // List to hold Ticket objects
			        List<Ticket> ticketList = new ArrayList<>();
			        
			        // Iterate through the tasksArray and map to Ticket objects
			        for (int i = 0; i < tasksArray.length(); i++) {
			            JSONObject taskJson = tasksArray.getJSONObject(i);
			            
			            // Create a new Ticket object and set fields
			            Ticket ticket = new Ticket();
			            ticket.setTName(taskJson.getString("taskName"));
			            ticket.setTDescription(taskJson.getString("description"));
			            ticket.setTFileLink(taskJson.getString("link"));
			            ticket.setLocationOfFile(taskJson.getString("referenceFile"));
			            // Assuming you have some logic to set tFlag, tType, etc.
			            ticket.setTFlag(1); // Example value, adjust as needed
			            ticket.setTType("AI Generated"); // Example value, adjust as needed
			            ticket.setTStatus("Open"); // Example value, adjust as needed

			            // Add the ticket to the list
			            ticketList.add(ticket);
			        }
					// Print the result
					System.out.println();
					return ticketList;
				}
			} else {
				// Handle non-2xx response codes (error response)
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
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
		return new ArrayList<>();
	}

}
