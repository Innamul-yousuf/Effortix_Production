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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.effortix.backend.models.Employee;
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
public class GenerateSmartSplitSubTasks {
	
	@Value("${api.key}")
	private String apiKey;
    
	
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

	@Autowired
	EmployeeService employeeService;
	@Autowired
	ProjectService projectService;
	public List<Ticket> generateSub_Tasks(String ParentTaskDescription, String EmployeeWorks, Employee Fromemployee,Ticket UIticket ) {
		try {
			// Escape the EmployeeSkillsTable JSON string
		

			// Define the API URL
			String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="+apiKey;

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
			String systemInstruction = "You are an AI assistanat reponsible for splitting a task into valid technical sub-tasks and align a perfect resource for each sub-tasks. Split sub-tasks into maximum 5 only. You will ge provided with task description and you must split it into some sub-tasks and assign it to employee ids given to you along with there previous skill and works."
					+"Here is the task description: "+ ParentTaskDescription + "here is the json of employee details"+ EmployeeWorks+" Give the responce only in json format with array of subtasks with values taskName, taskDescription(should be  and taskToEid";

			String jsonInputString = "{"
			    + "\"contents\": [{"
			    + "  \"role\": \"user\","
			    + "  \"parts\": [{"
			    + "    \"text\": \"" + systemInstruction.replace("\"", "\\\"") + "\""
			    + "  }]"
			    + "}],"
			    + "\"generationConfig\": {"
			    + "  \"temperature\": 1,"
			    + "  \"topK\": 40,"
			    + "  \"topP\": 0.95,"
			    + "  \"maxOutputTokens\": 8192"
			    + "}"
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
					System.out.println("============AI Generated Fun Friday Tasks===========");
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
			        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			        // Iterate through the tasksArray and map to Ticket objects
			        for (int i = 0; i < tasksArray.length(); i++) {
			            JSONObject taskJson = tasksArray.getJSONObject(i);
			            
			            // Create a new Ticket object and set fields
			            Ticket ticket = new Ticket();
			            ticket.setTName(taskJson.getString("taskName"));
			            ticket.setTDescription(taskJson.getString("taskDescription"));
			            int toEID=taskJson.getInt("taskToEid");
			            Employee ToEmployee=employeeService.getEmployeeById( (long)toEID).get();
			            // Assuming you have some logic to set tFlag, tType, etc.
			            ticket.setTFlag(1); // Example value, adjust as needed
			            ticket.setTType("Splitted Task"); // Example value, adjust as needed
			            ticket.setTStatus("Open"); // Example value, adjust as needed
			           
			            
			            
			         // Get the current date
			            LocalDate today = LocalDate.now();

			       

			            // Get the Friday of the current week
			            LocalDate friday = today;
			            
			         
			            Date fridayDate = Date.from(friday.atStartOfDay(ZoneId.systemDefault()).toInstant());
			            
			            // Format dates to "yyyy-MM-dd"
			            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			          
			            String fridayFormatted = sdf.format(fridayDate);
			            
			            
			            String deadlineString = fridayFormatted;
			            
			               
			                ticket.setDeadline(UIticket.getDeadline());
			         
			            
			            
			            Date createDate = new Date();  // Current system date
			            ticket.setCreatedDate(createDate);
			            // Add the ticket to the list
			            ticket.setToEmployee(ToEmployee);
			            ticket.setFromEmployee(Fromemployee);
			            ticket.setProject(projectService.getProjectById(5L).get());
			           // ticketList.add(ticket);
			            
			            ticketList.add(ticket);
			        }
					// Print the result
					System.out.println("The Tickets: "+ticketList);
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