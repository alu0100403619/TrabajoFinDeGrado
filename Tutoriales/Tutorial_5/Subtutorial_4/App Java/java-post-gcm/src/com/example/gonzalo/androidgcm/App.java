package com.example.gonzalo.androidgcm;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.gonzalo.androidgcm.vo.Content;

public class App {
	
	public static void main(String[] args) {
		
	  System.out.println("Mandando Post a GCM");
	  String apiKey = "AIzaSyA9HWNKEz5-JpAoPPScakswhe0HUJFEnpc";
	  Content content = createContent();
	  POST2GCM.post(apiKey, content);
  }
	
	public static Content createContent() {
		Content content = new Content();
		content.addRegId("APA91bF0G1qjRS3qNnsG3YX2B2iw4NmYFRVZgHnUbNkVKIXH9-k2zbH9w6LhXYUtlskibIvELcQGIl76MCIHcEXn3bKbf4u3rm5mxcYuD2I5CKXdf-4yXcZ1MfvM9gfJs10qJdtQCOm_TiYlR3yiOIPoRYJ8vPfq1WE35_yM9LhGkDX6_w25vVs");
		content.createData("Test Title", "Test Message");
		return content;
	}
	
}
