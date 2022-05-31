package org.clicksend.internal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class ClickSendMule4sConnection {

  private final String BASE_URL = "https://rest.clicksend.com/v3";
  private HttpURLConnection connection = null;
  private Logger LOGGER = LoggerFactory.getLogger(ClickSendMule4sConnection.class);
  
  public ClickSendMule4sConnection() {
  }

  public HttpURLConnection GetConnection(String endpoint) {
	  String uri = BASE_URL+endpoint;
	  LOGGER.info(String.format("Creating Connection to %s",uri));
	  URL url;
	  try {
		  url = new URL(uri);
		  connection =  (HttpURLConnection) url.openConnection();
		  LOGGER.info(String.format("Connection successfully to %s",uri));
		  return connection;
	  } catch (MalformedURLException e) {
		  // TODO Auto-generated catch block
		  LOGGER.error(String.format("Invalid URL %s",uri));
		  e.printStackTrace();
		  return null;
	  } catch (IOException e) {
		  // TODO Auto-generated catch block
		  LOGGER.error(String.format("IO Exception, unable to open connection to %s", uri));
		  e.printStackTrace();
		  return null;
	  }
	  
	  
  }

  public void invalidate() {
	  if (connection !=null) {
		  LOGGER.info("Connection Closed to ClickSend Services.");
		  connection.disconnect();
	  }
  }
}
