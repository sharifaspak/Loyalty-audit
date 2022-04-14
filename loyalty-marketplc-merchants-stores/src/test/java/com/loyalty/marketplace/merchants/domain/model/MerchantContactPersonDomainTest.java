package com.loyalty.marketplace.merchants.domain.model;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;

@SpringBootTest(classes = MerchantContactPersonDomain.class)
@ActiveProfiles("unittest")
public class MerchantContactPersonDomainTest {

	@Mock
	MerchantRepository merchantRepository;
	
	@InjectMocks
	MerchantContactPersonDomain contactPersonDomain = new MerchantContactPersonDomain();

	private String MerchantCode;
	
	private MerchantContactPersonDomain cpDom;
	private List<MerchantContactPersonDomain> contactPersonsDomain;
	private ContactPersonDto contactPersonDto;
	private List<ContactPersonDto> contactPersonDtoList;
	
	
	@Before
	public void setUp() throws ParseException {
		MockitoAnnotations.initMocks(this);
		
		MerchantCode = "P101";
		
		cpDom = new MerchantContactPersonDomain.ContactPersonBuilder("test@mail.com", "90023200", "John", "Doe")
				.faxNumber("101010").userName("cp1").password("xxx").build();
		contactPersonsDomain = new ArrayList<MerchantContactPersonDomain>();
		contactPersonsDomain.add(cpDom);
		
		contactPersonDto = new ContactPersonDto();
        contactPersonDto.setEmailId("test@mail.com");
        contactPersonDto.setMobileNumber("987654321");
        contactPersonDto.setFaxNumber("566781");
        contactPersonDto.setFirstName("John");
        contactPersonDto.setLastName("Doe");
        contactPersonDto.setUserName("userName");
        contactPersonDtoList = new ArrayList<ContactPersonDto>();
        contactPersonDtoList.add(contactPersonDto);
		
	}
	
	@Test
	public void testCreateContactPersonsMerchantPresent(){
		
		Merchant merchant = new Merchant();
		Optional<Merchant> MerchantOptional = Optional.of(merchant);
		when(merchantRepository.findByMerchantCodeAndEmailId(Mockito.anyString(), Mockito.anyString())).thenReturn(MerchantOptional);
		
		List<ContactPersonDto> invalidContactPersons = new ArrayList<ContactPersonDto>();
		List<MerchantContactPersonDomain> contactPersonsD = contactPersonDomain.createContactPersons(contactPersonDtoList, invalidContactPersons, MerchantCode);
		assertNotNull(contactPersonsD);
		
	}
	
	@Test
	public void testCreateContactPersonsMerchantNotPresent(){
		 
		List<ContactPersonDto> invalidContactPersons = new ArrayList<ContactPersonDto>();
		List<MerchantContactPersonDomain> contactPersonsD = contactPersonDomain.createContactPersons(contactPersonDtoList, invalidContactPersons, MerchantCode);
		assertNotNull(contactPersonsD);
		
	}
		
}
