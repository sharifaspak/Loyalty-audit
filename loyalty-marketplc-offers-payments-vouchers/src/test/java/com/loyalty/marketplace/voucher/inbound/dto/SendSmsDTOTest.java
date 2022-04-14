package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = SendSmsDTO.class)
@ActiveProfiles("unittest")
public class SendSmsDTOTest {

	private SendSmsDTO sendSmsDTO;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		sendSmsDTO = new SendSmsDTO();
		sendSmsDTO.setScheduledTime(new Date());
		sendSmsDTO.setSmsText("");
		sendSmsDTO.setTemplate(new ArrayList<>());
	}

	@Test
	public void testGetters() {
		assertNotNull(sendSmsDTO.getScheduledTime());
		assertNotNull(sendSmsDTO.getSmsText());
		assertNotNull(sendSmsDTO.getTemplate());
	}
	
	@Test
	public void testToString() {
		assertNotNull(sendSmsDTO.toString());
	}
	
}