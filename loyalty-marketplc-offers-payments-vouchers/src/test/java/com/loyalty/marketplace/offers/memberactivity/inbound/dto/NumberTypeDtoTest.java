package com.loyalty.marketplace.offers.memberactivity.inbound.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = NumberTypeDto.class)
@ActiveProfiles("unittest")
public class NumberTypeDtoTest {

	private NumberTypeDto numberTypeDto;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		numberTypeDto = new NumberTypeDto();
		numberTypeDto.setName("");
		numberTypeDto.setDescription("");
		NumberTypeDto numberType = new NumberTypeDto("","");
		numberType.setName("");
	}

	@Test
	public void testGetters() {
		assertNotNull(numberTypeDto.getName());
		assertNotNull(numberTypeDto.getDescription());
	}
	
	@Test
	public void testToString() {
		assertNotNull(numberTypeDto.toString());
	}
	
}