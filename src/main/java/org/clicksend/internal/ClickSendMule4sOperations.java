package org.clicksend.internal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClickSendMule4sOperations {

	private static final String APPLICATION_JSON_UTF_8 = "application/json; utf-8";
	private static final String AUTHORIZATION = "Authorization";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String ERROR_WHILE_PREPARING_REQUEST_PAYLOAD = "Error While Preparing Request Payload.";
	private static final String APPLICATION_JSON = "application/json";
	private static final String ACCEPT = "Accept";
	private static final String UTF_8 = "utf-8";
	private static final String BASIC = "Basic ";
	private static final Logger LOGGER = LoggerFactory.getLogger(ClickSendMule4sOperations.class);

	@MediaType(value = MediaType.APPLICATION_JSON, strict = false)
	@Alias("SendSMS")
	@DisplayName("Send SMS")
	@Summary("Send SMS to a number")
	public String sendSMS(@Config ClickSendMule4sConfiguration configuration,
			@Connection ClickSendMule4sConnection connection,
			@ParameterGroup(name = "SMS Parameters") SMSParameters smsParams) throws IOException {

		String username = configuration.getUserId();
		String password = configuration.getPassword();

		String auth = BASIC + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		HttpURLConnection conn = connection.GetConnection("/sms/send");
		conn.setRequestMethod("POST");
		conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON_UTF_8);
		conn.setRequestProperty(ACCEPT, APPLICATION_JSON);
		conn.setRequestProperty(AUTHORIZATION, auth);
		conn.setDoOutput(true);

		JSONObject root = null;
		try {
			root = new JSONObject();
			JSONArray arr = new JSONArray();
			JSONObject messageObj = new JSONObject();
			messageObj.put("to", smsParams.To);
			messageObj.put("source", "mulesoft");
			messageObj.put("body", smsParams.Message);

			if (smsParams.CustomString != null && !smsParams.CustomString.isEmpty()) {
				messageObj.put("custom_string", smsParams.CustomString);
			}

			arr.put(messageObj);
			root.put("messages", arr);
		} catch (JSONException e) {
			LOGGER.error(ERROR_WHILE_PREPARING_REQUEST_PAYLOAD);
			e.printStackTrace();
		}

		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = root.toString().getBytes(UTF_8);
			os.write(input, 0, input.length);
		} catch (Exception e) {
			LOGGER.error("Error While Writing Request Payload to Connection:");
			e.printStackTrace();
			throw e;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}

			return response.toString();
		} catch (Exception e) {
			LOGGER.error("Error While Reading Response Payload.");
			e.printStackTrace();
			throw e;
		}
	}

	@MediaType(value = MediaType.APPLICATION_JSON, strict = false)
	@Alias("SendMMS")
	@DisplayName("Send MMS")
	@Summary("Send MMS to a number")
	public String sendMMS(@Config ClickSendMule4sConfiguration configuration,
			@Connection ClickSendMule4sConnection connection,
			@ParameterGroup(name = "MMS Parameters") MMSParameters mmsParams,
			@ParameterGroup(name = "MMS Media Parameters") MMSMediaParameters mmsMediaParams) throws Exception {
		String username = configuration.getUserId();
		String password = configuration.getPassword();

		String url = mmsMediaParams.FileURL;

		if (mmsMediaParams.FilePath != null && !mmsMediaParams.FilePath.isEmpty()) {
			url = UploadFile(connection, mmsMediaParams.FilePath, username, password);
			if (url == null) {
				throw new FileNotFoundException("Failed to Upload File.");
			}
		}

		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("Either File Path or File URL parameter must be provided.");
		}

		String auth = BASIC + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		HttpURLConnection conn = connection.GetConnection("/mms/send");
		conn.setRequestMethod("POST");
		conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON_UTF_8);
		conn.setRequestProperty(ACCEPT, APPLICATION_JSON);
		conn.setRequestProperty(AUTHORIZATION, auth);
		conn.setDoOutput(true);

		JSONObject root = null;
		try {
			root = new JSONObject();
			JSONArray arr = new JSONArray();
			JSONObject messageObj = new JSONObject();
			messageObj.put("to", mmsParams.To);
			messageObj.put("source", "mulesoft");
			messageObj.put("subject", mmsParams.Subject);

			if (mmsParams.From != null && !mmsParams.From.isEmpty()) {
				messageObj.put("from", mmsParams.From);
			}

			messageObj.put("body", mmsParams.Message);

			if (mmsParams.CustomString != null && !mmsParams.CustomString.isEmpty()) {
				messageObj.put("custom_string", mmsParams.CustomString);
			}

			arr.put(messageObj);
			root.put("messages", arr);
			root.put("media_file", url);

		} catch (JSONException e) {
			LOGGER.error(ERROR_WHILE_PREPARING_REQUEST_PAYLOAD);
			e.printStackTrace();
			throw e;
		}

		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = root.toString().getBytes(UTF_8);
			os.write(input, 0, input.length);
		} catch (Exception e) {
			LOGGER.error("Error While Writing Request Payload to Connection.");
			e.printStackTrace();
			throw e;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		} catch (FileNotFoundException e) {
			LOGGER.error("Error While Reading Payload From Response.");
			e.printStackTrace();
			throw e;
		}
	}

	private String UploadFile(ClickSendMule4sConnection connection, String filePath, String username, String password)
			throws UnsupportedEncodingException, IOException, JSONException {
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Paths.get(filePath));
		} catch (Exception e) {
			LOGGER.error("Failed to Read File.");
			e.printStackTrace();
			return null;
		}

		byte[] encoded = Base64.getEncoder().encode(bytes);

		String auth = BASIC + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		HttpURLConnection conn = connection.GetConnection("/uploads?convert=mms");
		conn.setRequestMethod("POST");
		conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON_UTF_8);
		conn.setRequestProperty(ACCEPT, APPLICATION_JSON);
		conn.setRequestProperty(AUTHORIZATION, auth);
		conn.setDoOutput(true);

		JSONObject root = null;
		try {
			root = new JSONObject();
			root.put("content", new String(encoded));
		} catch (JSONException e) {
			LOGGER.error(ERROR_WHILE_PREPARING_REQUEST_PAYLOAD);
			e.printStackTrace();
			throw e;
		}

		try (OutputStream os = conn.getOutputStream()) {
			byte[] input = root.toString().getBytes(UTF_8);
			os.write(input, 0, input.length);
		} catch (Exception e) {
			LOGGER.error("Error While Writing Request Payload to Connection.");
			e.printStackTrace();
			return null;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			JSONObject resObj = new JSONObject(response.toString());
			String url = resObj.getJSONObject("data").getString("_url");
			return url;
		} catch (FileNotFoundException e) {
			LOGGER.error("Error While Reading Payload From Response:");
			e.printStackTrace();
			throw e;
		}
	}

}
