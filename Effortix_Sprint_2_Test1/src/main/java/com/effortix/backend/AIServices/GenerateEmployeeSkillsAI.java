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
public class GenerateEmployeeSkillsAI {

	
	@Autowired
	private EmployeeSkillsService employeeSkillsService;

	

	public Map<String, String> generateSkillsAndUpdatePreviousWorks(String ticketDetails, String theUpdate) {
		
		
		
		try { 
			Map<String, String> skillAndWorkExp = new HashMap();
			// Escape the EmployeeSkillsTable JSON string
			String escapedTicketDetails = ticketDetails.replace("\"", "\\\"");
			String escapedUpdate = theUpdate.replace("\"", "\\\"");
			System.out.println("kkkkk: "+escapedTicketDetails);
			// Define the API URL
			//String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyCObxtlyDEDrzupiBXBcGZKz7u2az8zX_M";
			String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyBpwx92JlgpI9IApoF7iU8Kwihf36JbvQ4";
 
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
			String systemInstruction="You are an AI assistant responsible for generating a short Skill(very short) to be added in work experience columns in an employee's record by analyzing the updates provided in a ticket. "
					+ "You will receive a new ticket update, which includes the details of the task completed by the employee. "
					+ "Your task is to read the update, identify any new skills or work experiences, and generate a brief, concise description of the skills or tasks learned from the ticket update. Give it as plain text and don't inculde other detalils in your responce. "
					+ "Give it in json format skills in Skills and previous works in Previous Works: Try to find the skill from the ticket update. Give it as plain text. If no skill or previous work is gained then just give the responce with a blank space. Note: Give the contnt in short. Skills with maximum 3 word and Previous work with short understandable content maximum 9 words.";
			// JSON request payload
			String jsonInputString = "{"
				    + "\"contents\": ["
				    + "    {"
				    + "        \"parts\": ["
				    + "            {\"text\": \"" + systemInstruction + " Ticket Details: " + escapedTicketDetails + "\"},"
				    + "            {\"text\": \"The Update from Employee in the ticket is: " + escapedUpdate +"Give it as plain text dont bold it"+ "\"}"
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
					System.out.println("============AI Sugested SKILLS===========");
					System.out.println(response.toString());

					JSONObject jsonObject = new JSONObject(response.toString());
			        JSONArray candidates = jsonObject.getJSONArray("candidates");
			        
			        // Extract the "text" part from the first candidate
			        String text = candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");

			        // Remove the leading and trailing backticks and "json" identifier from the text
			        String jsonText = text.replace("```json", "").replace("```", "").trim();
			     // Parse the cleaned-up JSON string
			        JSONObject parsedJson = new JSONObject(jsonText);
			        String skillsSection = parsedJson.optString("Skills", "").trim();
			        String workExperienceSection = parsedJson.optString("Previous Works", "").trim();
			        
			        skillsSection = skillsSection.replaceAll("[^a-zA-Z0-9\\s,]", ""); // Keeps commas and alphanumeric characters
			        workExperienceSection = workExperienceSection.replaceAll("[^a-zA-Z0-9\\s,]", ""); // Same for previous works

			        
			        /*
					 * String[] sections = text.split("## Previous Works:"); String skillsSection =
					 * sections[0].replace("## Skills:\n\n", "").trim(); skillsSection =
					 * skillsSection.replaceAll("[^a-zA-Z0-9\\s]", "");;
					 */

			        //String workExperienceSection = (sections.length > 1) ? sections[1].trim() : "";
			        //workExperienceSection= workExperienceSection.replaceAll("[^a-zA-Z0-9\\s]", "");
			        System.out.println("Before MAP: "+skillsSection);
			        System.out.println("Before MAP2: "+workExperienceSection);
			        skillAndWorkExp.clear();  
			        skillAndWorkExp.put("skills", skillsSection);
			        skillAndWorkExp.put("work_experience", workExperienceSection);
			        
					System.out.println("skillAndWorkExp: "+skillAndWorkExp.toString());
					return skillAndWorkExp;
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
