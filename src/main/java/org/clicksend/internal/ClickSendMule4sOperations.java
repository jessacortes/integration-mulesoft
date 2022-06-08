package org.clicksend.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;

public class ClickSendMule4sOperations {

	private final Logger LOGGER = LoggerFactory.getLogger(ClickSendMule4sOperations.class);
	
  @MediaType(value = MediaType.APPLICATION_JSON, strict = false)
  @Alias("SendSMS")
  public String sendSMS(@Config ClickSendMule4sConfiguration configuration, @Connection ClickSendMule4sConnection connection, @ParameterGroup(name="SMS Parameters") SMSParameters smsParams) throws IOException{
	  
	  LOGGER.info("Initianting SMS Send Operation.");
	  LOGGER.info("Fetching Credentials from Configuration.");
	  String username = configuration.getUserId();
	  String password = configuration.getPassword();
	  
	  LOGGER.info("Encoding Credentials.");
	  String auth = "Basic " + Base64.getEncoder().encodeToString((username+":"+password).getBytes());
	  LOGGER.info("Creating Connection");
	  HttpURLConnection conn = connection.GetConnection("/sms/send");
	  LOGGER.info("Preparing Headers");
	  conn.setRequestMethod("POST");
	  conn.setRequestProperty("Content-Type", "application/json; utf-8");
	  conn.setRequestProperty("Accept", "application/json");
	  conn.setRequestProperty("Authorization", auth);
	  conn.setDoOutput(true);
	  
	  LOGGER.info("Preparing Request Payload.");
	  JSONObject root = null;
	  try {
		  root = new JSONObject();
		  JSONArray arr = new JSONArray();
		  JSONObject messageObj = new JSONObject();
		  messageObj.put("to", smsParams.To);
		  messageObj.put("source", "mulesoft");
		  messageObj.put("body", smsParams.Message);
		  
		  if(smsParams.CustomString != null && !smsParams.CustomString.isEmpty()) {
			  messageObj.put("custom_string", smsParams.CustomString);
		  }
		  
		  arr.put(messageObj);
		  root.put("messages", arr);
	  } catch (JSONException e) {
		  LOGGER.error("Error Encoutered While Preparing Request Payload.");
		  LOGGER.error(e.getMessage());
	  }
	  
	  LOGGER.info("Writing Payload to Request.");
	  try(OutputStream os = conn.getOutputStream()) {
		  	LOGGER.info(root.toString());
		    byte[] input = root.toString().getBytes("utf-8");
		    os.write(input, 0, input.length);
	  } catch (Exception e) {
		  LOGGER.error("Error Encoutered While Writing Request Payload to Connection:");
		  LOGGER.error(e.getMessage());
		  throw e;
	  }
	  
	  LOGGER.info("Reading Response.");
	  try(BufferedReader br = new BufferedReader(
			  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			    StringBuilder response = new StringBuilder();
			    String responseLine = null;
			    while ((responseLine = br.readLine()) != null) {
			        response.append(responseLine.trim());
			    }
		  LOGGER.info("Response Successfully Received. SMS Success.");
		  LOGGER.info("Response: " + response.toString());
		  
		  return response.toString(); 
	  } catch (Exception e) {
		  LOGGER.error("Error Encoutered While Reading Response Payload.");
		  LOGGER.error(e.getMessage());
		  throw e;
	  }
  }
	  
	  @MediaType(value = MediaType.APPLICATION_JSON, strict = false)
	  @Alias("SendMMS")
	  public String sendMMS(@Config ClickSendMule4sConfiguration configuration, @Connection ClickSendMule4sConnection connection, @ParameterGroup(name="MMS Parameters") MMSParameters mmsParams) throws Exception{
		  LOGGER.info("Initiating MMS Send Operation.");
		  LOGGER.info("Fetching Credentials from Configuration.");
		  String username = configuration.getUserId();
		  String password = configuration.getPassword();
		  
		  LOGGER.info("Uploading File and Fetching URL");
		  String url;
		  try {
				url = UploadFile(connection, mmsParams.FilePath, username, password);
				LOGGER.info(url);
			} catch (Exception e) {
				LOGGER.error("Error Encoutered While Uploading the File and Fetching URL.");
				LOGGER.error(e.getMessage());
				throw new Exception("File Failed to Upload");
			}
		  
		  
		  LOGGER.info("Encoding Credentials.");
		  String auth = "Basic " + Base64.getEncoder().encodeToString((username+":"+password).getBytes());
		  LOGGER.info("Creating Connection");
		  HttpURLConnection conn = connection.GetConnection("/mms/send");
		  LOGGER.info("Preparing Headers");
		  conn.setRequestMethod("POST");
		  conn.setRequestProperty("Content-Type", "application/json; utf-8");
		  conn.setRequestProperty("Accept", "application/json");
		  conn.setRequestProperty("Authorization", auth);
		  conn.setDoOutput(true);
		  
		  LOGGER.info("Preparing Request Payload.");
		  JSONObject root = null;
		  try {
			  root = new JSONObject();
			  JSONArray arr = new JSONArray();
			  JSONObject messageObj = new JSONObject();
			  messageObj.put("to", mmsParams.To);
			  messageObj.put("source", "mulesoft");
			  messageObj.put("subject", mmsParams.Subject);
			  
			  if(mmsParams.From != null && !mmsParams.From.isEmpty()) {
				  messageObj.put("from", mmsParams.From);
			  }
			  
			  messageObj.put("body", mmsParams.Message);
			  
			  if(mmsParams.CustomString != null && !mmsParams.CustomString.isEmpty()) {
				  messageObj.put("custom_string", mmsParams.CustomString);
			  }
			  
			  arr.put(messageObj);
			  root.put("messages", arr);
			  root.put("media_file", url);
		  } catch (JSONException e) {
			  LOGGER.error("Error Encoutered While Preparing Request Payload.");
			  LOGGER.error(e.getMessage());
		  }
		  
		  LOGGER.info("Writing Payload to Request.");
		  try(OutputStream os = conn.getOutputStream()) {
			    byte[] input = root.toString().getBytes("utf-8");
			    os.write(input, 0, input.length);
		  } catch (Exception e) {
			  LOGGER.error("Error Encoutered While Writing Request Payload to Connection.");
			  LOGGER.error(e.getMessage());
		  }
		  
		  LOGGER.info("Reading Response.");
		  try(BufferedReader br = new BufferedReader(
				  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				    StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
			  LOGGER.info("Response Successfully Received. MMS Success.");
			  LOGGER.info("Response: " + response.toString());
			  return response.toString();
		  } catch (Exception e) {
			  LOGGER.error("Error Encoutered While Reading Payload From Response.");
			  LOGGER.error(e.getMessage());
			  throw e;
		  }
	  }
	  
	  private String UploadFile(ClickSendMule4sConnection connection, String filePath, String username, String password) throws Exception {
		  LOGGER.info("Initiating Upload.");
		  LOGGER.info("Reading File.");
		  byte[] bytes;
		  try {
			  bytes = Files.readAllBytes(Paths.get(filePath));
		  } catch (Exception e) {
			  LOGGER.error("Failed to Read File.");
			  LOGGER.error(e.getMessage());
			  return null;
		  }
		  
		  byte[] encoded = Base64.getEncoder().encode(bytes);
		  
		  LOGGER.info("Encoding Credentials.");
		  String auth = "Basic " + Base64.getEncoder().encodeToString((username+":"+password).getBytes());
		  LOGGER.info("Creating Connection.");
		  HttpURLConnection conn = connection.GetConnection("/uploads?convert=mms");
		  LOGGER.info("Preparing Headers");
		  conn.setRequestMethod("POST");
		  conn.setRequestProperty("Content-Type", "application/json; utf-8");
		  conn.setRequestProperty("Accept", "application/json");
		  conn.setRequestProperty("Authorization", auth);
		  conn.setDoOutput(true);
		  
		  LOGGER.info("Preparing File Upload Request Payload.");
		  JSONObject root = null;
		  try {
			  root = new JSONObject();
			  root.put("content", new String(encoded));
		  }
		  catch (JSONException e) {
			  LOGGER.error("Error Encoutered While Preparing Request Payload.");
			  LOGGER.error(e.getMessage());
			  throw e;
		  }
		  
		  LOGGER.info("Writing Payload to Request.");
		  try(OutputStream os = conn.getOutputStream()) {
			    byte[] input = root.toString().getBytes("utf-8");
			    os.write(input, 0, input.length);
		  } catch (Exception e) {
			  LOGGER.error("Error Encoutered While Writing Request Payload to Connection.");
			  LOGGER.error(e.getMessage());
			  return null;
		  }
		  
		  LOGGER.info("Reading Response.");
		  try(BufferedReader br = new BufferedReader(
			  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
			  StringBuilder response = new StringBuilder();
				    String responseLine = null;
				    while ((responseLine = br.readLine()) != null) {
				        response.append(responseLine.trim());
				    }
				    JSONObject resObj = new JSONObject(response.toString());
				    String url = resObj.getJSONObject("data").getString("_url");
				    LOGGER.info("Response Successfully Received. File Uploaded Success.");
					LOGGER.info("Response: " + response.toString());
			  return url;
		  } catch (Exception e) {
			  LOGGER.error("Error Encoutered While Reading Payload From Response:");
			  LOGGER.error(e.getMessage());
			  throw e;
		  }
	  }
}
