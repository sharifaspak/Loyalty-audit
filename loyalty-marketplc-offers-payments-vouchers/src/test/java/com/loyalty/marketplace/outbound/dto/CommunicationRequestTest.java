package com.loyalty.marketplace.outbound.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommunicationRequest.class)
@ActiveProfiles("unittest")
public class CommunicationRequestTest {

	private CommunicationRequest communicationRequest;
	private String testString;
	
	@Before
	public void setUp(){
		
		communicationRequest = new CommunicationRequest("channelId", "systemid", "systempassword", 
				"token", "receivertype", "receiverid", "templateid", "communicationtype", "additionalParameters", 
				"commSubject", "commText", "smilesEmail", "uiLanguage", "attachmentContent", 
				"sticker", "voucherDescription", false, "dbMethod", "dataValidation", 
				"finalText", "template", "subject", "notificationId", "notificationCode", "memCode", 
				"notificationLanguage", new HashMap<String, String>(), new HashMap<String, String>());
		
		testString = "testDate";
	}
	
	@Test
	public void testConstructor()
	{
		assertNotNull(new CommunicationRequest());
	}
	
	@Test
	public void testGetChannelId()
	{
		communicationRequest.setChannelId(testString);
		assertEquals(communicationRequest.getChannelId(), testString);
	}
	
	@Test
	public void testGetSystemId()
	{
		communicationRequest.setSystemid(testString);
		assertEquals(communicationRequest.getSystemid(), testString);
	}
	
	@Test
	public void testGetSystemPassword()
	{ 
		communicationRequest.setSystempassword(testString);
		assertEquals(communicationRequest.getSystempassword(), testString);
	}
	
	@Test
	public void testGetToken()
	{ 
		communicationRequest.setToken(testString);
		assertEquals(communicationRequest.getToken(), testString);
	}
	
	@Test
	public void testGetReceiverType()
	{ 
		communicationRequest.setReceivertype(testString);
		assertEquals(communicationRequest.getReceivertype(), testString);
	}
	
	@Test
	public void testGetReceiverid()
	{ 
		communicationRequest.setReceiverid(testString);
		assertEquals(communicationRequest.getReceiverid(), testString);
	}
	
	@Test
	public void testGetTemplateid()
	{ 
		communicationRequest.setTemplateid(testString);
		assertEquals(communicationRequest.getTemplateid(), testString);
	}
	
	@Test
	public void testGetCommunicationtype()
	{ 
		communicationRequest.setCommunicationtype(testString);
		assertEquals(communicationRequest.getCommunicationtype(), testString);
	}
	
	@Test
	public void testGetAdditionalParameters()
	{ 
		communicationRequest.setAdditionalParameters(testString);
		assertEquals(communicationRequest.getAdditionalParameters(), testString);
	}
	
	@Test
	public void testGetCommSubject()
	{ 
		communicationRequest.setCommSubject(testString);
		assertEquals(communicationRequest.getCommSubject(), testString);
	}
	
	@Test
	public void testGetCommText()
	{ 
		communicationRequest.setCommText(testString);
		assertEquals(communicationRequest.getCommText(), testString);
	}
	
	@Test
	public void testGetSmilesEmail()
	{ 
		communicationRequest.setSmilesEmail(testString);
		assertEquals(communicationRequest.getSmilesEmail(), testString);
	}
	
	@Test
	public void testGetUiLanguage()
	{ 
		communicationRequest.setUiLanguage(testString);
		assertEquals(communicationRequest.getUiLanguage(), testString);
	}
	
	@Test
	public void testGetAttachmentContent()
	{ 
		communicationRequest.setAttachmentContent(testString);
		assertEquals(communicationRequest.getAttachmentContent(), testString);
	}
	
	@Test
	public void testGetSticker()
	{ 
		communicationRequest.setSticker(testString);
		assertEquals(communicationRequest.getSticker(), testString);
	}
	
	@Test
	public void testGetVoucherDescription()
	{ 
		communicationRequest.setVoucherDescription(testString);
		assertEquals(communicationRequest.getVoucherDescription(), testString);
	}
	
	@Test
	public void testGetDbFlag()
	{ 
		boolean dbFlag = true;
		communicationRequest.setDbFlag(dbFlag);
		assertEquals(communicationRequest.isDbFlag(), dbFlag);
	}
		
	@Test
	public void testGetDbMethod()
	{ 
		communicationRequest.setDbMethod(testString);
		assertEquals(communicationRequest.getDbMethod(), testString);
	}
	
	@Test
	public void testGetDataValidation()
	{ 
		communicationRequest.setDataValidation(testString);
		assertEquals(communicationRequest.getDataValidation(), testString);
	}
	
	@Test
	public void testGetFinalText()
	{ 
		communicationRequest.setFinalText(testString);
		assertEquals(communicationRequest.getFinalText(), testString);
	}
		
	@Test
	public void testGetTemplate()
	{ 
		communicationRequest.setTemplate(testString);
		assertEquals(communicationRequest.getTemplate(), testString);
	}
	
	@Test
	public void testGetSubject()
	{ 
		communicationRequest.setSubject(testString);
		assertEquals(communicationRequest.getSubject(), testString);
	}
	
	@Test
	public void testNotificationId()
	{ 
		communicationRequest.setNotificationId(testString);
		assertEquals(communicationRequest.getNotificationId(), testString);
	}
	
	@Test
	public void testGetNotificationCode()
	{ 
		communicationRequest.setNotificationCode(testString);
		assertEquals(communicationRequest.getNotificationCode(), testString);
	}
	
	@Test
	public void testGetMemCode()
	{ 
		communicationRequest.setMemCode(testString);
		assertEquals(communicationRequest.getMemCode(), testString);
	}
	
	@Test
	public void testGetNotificationLanguage()
	{ 
		communicationRequest.setNotificationLanguage(testString);
		assertEquals(communicationRequest.getNotificationLanguage(), testString);
	}
	
	@Test
	public void testGetPlaceholders()
	{ 
		Map<String, String> placeholders = new HashMap<String, String>();
		communicationRequest.setPlaceholders(placeholders);
		assertEquals(communicationRequest.getPlaceholders(), placeholders);
	}
	
	@Test
	public void testGetDynamicParameters()
	{ 
		Map<String, String> dynamicParameters = new HashMap<String, String>();
		communicationRequest.setDynamicParameters(dynamicParameters);
		assertEquals(communicationRequest.getDynamicParameters(), dynamicParameters);
	}
	
	@Test
	public void testToString()
	{
		assertNotNull(communicationRequest.toString());
	}
	
}
