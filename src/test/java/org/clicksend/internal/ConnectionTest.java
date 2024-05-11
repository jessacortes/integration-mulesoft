package org.clicksend.internal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;
import org.mule.functional.junit4.FunctionalTestCase;

public class ConnectionTest {

	@Test
    public void connectionTest()
    {
		ClickSendMule4sConnection a = new ClickSendMule4sConnection();
		HttpURLConnection r = a.GetConnection("/sms/send");
		System.out.println(r.toString());  
		Assert.assertNotNull(r);
		
    }
	
	@Test
    public void invalidateTest()
    {
		ClickSendMule4sConnection a = new ClickSendMule4sConnection();
		HttpURLConnection r = a.GetConnection("/sms/send");
		//Assert.assertNotNull(r);
		a.invalidate();
    }
	
//	@Test(expected = MalformedURLException.class)
//    public void getConnectionMalformedURLExceptionTest()
//    {
//		ClickSendMule4sConnection a = new ClickSendMule4sConnection();
//		HttpURLConnection r = a.GetConnection("*");
//		//Assert.assertEquals("Either File Path or File URL parameter must be provided.", e.getMessage());
//    }
}
