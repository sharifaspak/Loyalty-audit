package com.loyalty.marketplace.voucher.inbound.dto;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.outbound.dto.EmailRequestDto;

@SpringBootTest(classes = EmailRequestDto.class)
@ActiveProfiles("unittest")
public class EmailRequestDtoTest {

	private EmailRequestDto emailRequestDto;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		emailRequestDto = new EmailRequestDto();
		emailRequestDto.setAccountNumber("");
		emailRequestDto.setAdditionalParameters(new HashMap<String, String>());
		emailRequestDto.setEmailId("");
		emailRequestDto.setLanguage("");
		emailRequestDto.setNotificationCode("");
		emailRequestDto.setNotificationId("");
		emailRequestDto.setTemplateId("");
		emailRequestDto.setTransactionId("");
	}

	@Test
	public void testGetters() {
		assertNotNull(emailRequestDto.getAccountNumber());
		assertNotNull(emailRequestDto.getAdditionalParameters());
		assertNotNull(emailRequestDto.getEmailId());
		assertNotNull(emailRequestDto.getLanguage());
		assertNotNull(emailRequestDto.getNotificationCode());
		assertNotNull(emailRequestDto.getNotificationId());
		assertNotNull(emailRequestDto.getTemplateId());
		assertNotNull(emailRequestDto.getTransactionId());
	}
	
	@Test
	public void testToString() {
		assertNotNull(emailRequestDto.toString());
	}
	
	@Test
	public void testGetSerialversionuid() {
		assertNotNull(EmailRequestDto.getSerialversionuid());
	}

}