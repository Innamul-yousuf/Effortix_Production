package com.effortix.backend.AIServices;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Component;


public class GeminiTesterClass {
	 public void runAPI() {
	        try {
	            // Define the API URL (Replace YOUR_API_KEY with the actual API key)
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

	            // JSON request payload (modify the content as needed)
	            String jsonInputString = "{ \"contents\": [{ \"parts\": [{ \"text\": \"Explain to make a Paper Rocket\" }]}]}";

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
	    }
}
