package org.clicksend.internal;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(value = BlockJUnit4ClassRunner.class)
public class OperationTest {

	@Test
    public void smsTest() throws IOException
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations();
		ClickSendMule4sConfiguration configuration = new ClickSendMule4sConfiguration();
		ClickSendMule4sConnection connection = new ClickSendMule4sConnection();
		SMSParameters smsParams = new SMSParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		smsParams.setFrom("+61411111111");
		smsParams.setTo("+61411111111");
		smsParams.setMessage("test message, please ignore");
		smsParams.setCustomString("test message, please ignore");
		
		String r = op.sendSMS(configuration, connection, smsParams);
		System.out.println(r.toString());  
		Assert.assertNotNull(r);
    }
	
	@Test
    public void mmsWithFilePathTest() throws Exception
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations();
		ClickSendMule4sConfiguration configuration = new ClickSendMule4sConfiguration();
		ClickSendMule4sConnection connection = new ClickSendMule4sConnection();
		MMSParameters mmsParams = new MMSParameters();
		MMSMediaParameters mmsMediaParams = new MMSMediaParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		mmsParams.setFrom("+61411111111");
		mmsParams.setTo("+61411111111");
		mmsParams.setMessage("test message, please ignore");
		mmsParams.setCustomString("test message, please ignore");
		mmsParams.setSubject("test message");
		mmsMediaParams.setFilePath("src/test/resources/Mercedes.jpg");
		
		String r = op.sendMMS(configuration, connection, mmsParams, mmsMediaParams);
		System.out.println(r.toString());  
		Assert.assertNotNull(r);
		
    }
	
	@Test
    public void mmsWithoutFilePathTest() 
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations();
		ClickSendMule4sConfiguration configuration = new ClickSendMule4sConfiguration();
		ClickSendMule4sConnection connection = new ClickSendMule4sConnection();
		MMSParameters mmsParams = new MMSParameters();
		MMSMediaParameters mmsMediaParams = new MMSMediaParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		mmsParams.setFrom("+61411111111");
		mmsParams.setTo("+61411111111");
		mmsParams.setMessage("test message, please ignore");
		mmsParams.setCustomString("test message, please ignore");
		mmsParams.setSubject("test message");
		mmsMediaParams.setFilePath("src/test/resources/Mercedes1.jpg");
		
		String r = null;
		try {
			r = op.sendMMS(configuration, connection, mmsParams, mmsMediaParams);
		} catch (Exception e) {
			
			Assert.assertEquals("Failed to Upload File.", e.getMessage());
			//assertThrows(IllegalArgumentException.class, null);
		}
		
    }
	
	@Test
    public void mmsWithoutFileURLTest() 
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations();
		ClickSendMule4sConfiguration configuration = new ClickSendMule4sConfiguration();
		ClickSendMule4sConnection connection = new ClickSendMule4sConnection();
		MMSParameters mmsParams = new MMSParameters();
		MMSMediaParameters mmsMediaParams = new MMSMediaParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		mmsParams.setFrom("+61411111111");
		mmsParams.setTo("+61411111111");
		mmsParams.setMessage("test message, please ignore");
		mmsParams.setCustomString("test message, please ignore");
		mmsParams.setSubject("test message");
		mmsMediaParams.setFileURL(null);
		
		String r = null;
		try {
			r = op.sendMMS(configuration, connection, mmsParams, mmsMediaParams);
		} catch (Exception e) {
			
			Assert.assertEquals("Either File Path or File URL parameter must be provided.", e.getMessage());
			//assertThrows(IllegalArgumentException.class, null);
		}
		
    }
}
