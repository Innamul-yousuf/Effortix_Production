package com.effortix.backend.AIServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class GenerateEmployeeCredits {

	
	@Autowired
	private EmployeeSkillsService employeeSkillsService;

	
	
		public String calculateCredits(String ticketDetails, String theUpdates) {
			Map<String, String> skillAndWorkExp = new HashMap();
			try {
				// Escape the EmployeeSkillsTable JSON string
				String escapedTicketDetails = ticketDetails.replace("\"", "\\\"");
				String escapedUpdate = theUpdates.replace("\"", "\\\"");
				System.out.println("kkkkk: "+escapedTicketDetails);
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
				String systemInstruction="You are an AI assistant responsible for calculating credits for the employees for successfully compeleting the ticket. We will provide the task details and comparing the deadline and the current date time you must give points along with the issue he faced and the solved and how he solved. Maximum is 5 points you should rate it below that. You can also give it very accurately in decimals also . Give it in plain text without any explanation of yours. Give only the credits as responce";
				// JSON request payload
				String jsonInputString = "{"
					    + "\"contents\": ["
					    + "    {"
					    + "        \"parts\": ["
					    + "            {\"text\": \"" + systemInstruction + " Ticket Details: " + escapedTicketDetails + "\"},"
					    + "            {\"text\": \"The Updates from Employee in the ticket are: " + escapedUpdate +"Give it as plain text dont format it"+ "\"}"
					    + "        ]"
					    + "    }"
					    + "]"
					    + "}";
				// Write JSON input string to the request body
				try (OutputStream os = connection.getOutputStream()) {
					byte[] input = jsonInputString.getBytes("utf-8");
					os.write(input, 0, input.length);
				}

				// Get the response code
				int responseCode = connection.getResponseCode();
				System.out.println("Response Code for Timesheet entry: " + responseCode);

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
						System.out.println("============AI Calculated Values==========");
						System.out.println(response.toString());

						JSONObject jsonObject = new JSONObject(response.toString());
				        JSONArray candidates = jsonObject.getJSONArray("candidates");
				        
				        // Extract the "text" part from the first candidate
				        String text = candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");

				        // Remove the leading and trailing backticks and "json" identifier from the text
				        String jsonText = text.replace("```json", "").replace("```", "").trim();
				     
				        
						System.out.println();
						return jsonText;
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
			return null;
		}

}