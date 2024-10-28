package com.effortix.backend.AIServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.effortix.backend.models.EmployeeSkills;
import com.effortix.backend.models.Ticket;
import com.effortix.backend.services.EmployeeService;
import com.effortix.backend.services.EmployeeSkillsService;
import com.effortix.backend.services.ProjectService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

@Component
public class GeneratePerfectSkillsAndDetails {

	
	@Autowired
	private EmployeeSkillsService employeeSkillsService;

	
	@Autowired
	EmployeeService employeeService;
	@Autowired
	ProjectService projectService;
	
	@Value("${api.key}")
	private String apiKey;
	Map<String, List<String>> skillsMap = new HashMap<>();
	public Map<String, List<String>> GeneratePerfectSkillsAndDetailsAI(String SkillsJson) {
		
		  try {
	            // Define the API URL
	            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
	            System.out.println("apiUrl: " + apiUrl);

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
	            String systemInstruction = "You are an AI assistant responsible for generating all possible skills earned by the employee with ratings from the given details eleminating duplicates. Rating must be calculated maximum of 5 points with decimals allowed. You must only give skill and ratings in JSON format and no other information.";
	            String jsonContent = "Here is the json: " + SkillsJson;

	            String jsonInputString = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + systemInstruction.replace("\"", "\\\"") + "\" }, { \"text\": \"" + jsonContent.replace("\"", "\\\"") + "\" }] }] }";

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
	                    System.out.println("============ AI Modified Skills ===========");

	                    JSONObject jsonObject = new JSONObject(response.toString());
	                    JSONArray candidates = jsonObject.getJSONArray("candidates");

	                    // Extract the "text" part from the first candidate
	                    String text = candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts").getJSONObject(0).getString("text");

	                    // Remove the leading and trailing backticks and "json" identifier from the text
	                    String jsonText = text.replace("```json", "").replace("```", "").trim();

	                    // Parse the JSON response
	                    JSONArray skillsArray = new JSONArray(jsonText); // Assuming the response is now a simple array
	                    for (int i = 0; i < skillsArray.length(); i++) {
	                        JSONObject skillObject = skillsArray.getJSONObject(i);

	                        // Fetch the skills and rating
	                        String skillName = skillObject.getString("skill");
	                        double rating = skillObject.getDouble("rating");

	                        // Store skill and rating
	                        skillsMap.computeIfAbsent(skillName, k -> new ArrayList<>()).add("Rating: " + rating);
	                        System.out.println("Skill: " + skillName + " (Rating: " + rating + ")");
	                    }
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
	        return skillsMap;
	    }


	
}