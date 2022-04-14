package com.loyalty.marketplace.stores.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.entity.StoreAddress;
import com.loyalty.marketplace.stores.outbound.database.entity.StoreDescription;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.mongodb.MongoWriteException;

@SpringBootTest(classes = StoreDomain.class)
@ActiveProfiles("unittest")
public class StoreDomainTest {

	@Mock
	ModelMapper modelMapper;

	@Mock
	Validator validator;

	@Mock
	StoreRepository storeRepository;

	@Mock
	StoreContactPersonDomain contactPersonDomain;

	@InjectMocks
	StoreDomain storeDomain = new StoreDomain();

	private String id;
	private String programCode;
	private String storeCode;
	private String merchantCode;
	private String status;
	private String longitude;
	private String latitude;
	private String userName;
	private String emailId;
	private String usrCreated;
	private String usrUpdated;
	private Date dtCreated;
	private Date dtUpdated;
	
	private StoreDescriptionDomain storeDescriptionDomain;
	private StoreAddressDomain storeAddressDomain;
	private List<StoreContactPersonDomain> storeContactPersons;
	private StoreContactPersonDomain storeContactPersonDomain;
	private StoreDomain storeD;
	
	private String externalTransactionId;
	ResultResponse resultResponse;
	Store store;

	@Before
	public void setUp() {

		externalTransactionId = "123";
		MockitoAnnotations.initMocks(this);
		resultResponse = new ResultResponse(externalTransactionId);
		store = new Store();

		store.setProgramCode("Smiles");
		store.setMerchantCode("Mer01");
		store.setStoreCode("Sto01");

		StoreDescription description = new StoreDescription();
		description.setDescription("Store Description English");
		description.setDescriptionAr("Store Description Arabic");
		store.setDescription(description);

		StoreAddress address = new StoreAddress();
		address.setAddress("Store Address English");
		address.setAddressAr("Store Address Arabic");
		store.setAddress(address);

		store.setLatitude("Store Latitude");
		store.setLongitude("Store Longitude");
		store.setStatus("Active");

		List<ContactPerson> contactPersons = new ArrayList<ContactPerson>();
		ContactPerson contactPerson = new ContactPerson();
		contactPerson.setEmailId("Email ID");
		contactPerson.setFaxNumber("Fax Number");
		contactPerson.setFirstName("First Name");
		contactPerson.setLastName("Last Name");
		contactPersons.add(contactPerson);
		store.setContactPersons(contactPersons);

		store.setDtCreated(new Date());
		store.setDtUpdated(new Date());
		store.setUsrCreated("User Created");
		store.setUsrUpdated("User Updated");

		id = "ID";
		programCode = "Smiles";
		storeCode = "Sto01";
		merchantCode = "Mer01";
		status = "Active";
		longitude = "Longitude";
		latitude = "Latitude";
		userName = "Username";
		emailId = "Email ID";
		usrCreated = "Created User";
		usrUpdated = "Updated User";
		dtCreated = new Date();
		dtUpdated = new Date();
		
		storeAddressDomain = new StoreAddressDomain.StoreAddressBuilder("Store Address English", "Store Address Arabic")
				.build();
		storeDescriptionDomain = new StoreDescriptionDomain.StoreDescriptionBuilder("Store Description English",
				"Store Description Arabic").build();

		storeContactPersonDomain = new StoreContactPersonDomain.ContactPersonBuilder("Email ID", "Mobile Number",
				"First Name", "Last Name").build();
		storeContactPersons = new ArrayList<StoreContactPersonDomain>();
		storeContactPersons.add(storeContactPersonDomain);

		storeD = new StoreDomain.StoreBuilder(storeCode, storeDescriptionDomain, merchantCode, storeContactPersons,
				status).id(id).programCode(programCode).address(storeAddressDomain).latitude(latitude)
						.longitude(longitude).usrCreated(usrCreated).usrUpdated(usrUpdated)
						.dtCreated(dtCreated).dtUpdated(dtUpdated).status(status).build();

	}

	@Test
	public void testSaveStoreSuccess() throws MarketplaceException {
		when(modelMapper.map(storeD, Store.class)).thenReturn(store);
		when(this.storeRepository.insert(store)).thenReturn(store);
		storeDomain.saveStore(storeD,externalTransactionId);
		assertNotNull(store);
	}

	@Test(expected = MarketplaceException.class)
	public void testSaveStoreMongoException() throws MarketplaceException {
		when(modelMapper.map(storeD, Store.class)).thenReturn(store);
		when(this.storeRepository.insert(store)).thenThrow(MongoWriteException.class);
		storeDomain.saveStore(storeD,externalTransactionId);
		assertNotNull(store);
	}

	@Test(expected = MarketplaceException.class)
	public void testSaveStoreException() throws MarketplaceException {
		when(modelMapper.map(storeD, Store.class)).thenThrow(NullPointerException.class);
		when(this.storeRepository.insert(store)).thenReturn(store);
		storeDomain.saveStore(storeD,externalTransactionId);
		assertNotNull(store);
	}

	@Test
	public void testConfigureContactPersonSuccess() {
		Store storeSaved = new Store();
		when(storeRepository.save(store)).thenReturn(storeSaved);
		storeDomain.configureContactPerson(store, storeContactPersons, userName, programCode, RequestMappingConstants.CONFIGURE_CONATACT_PERSON,externalTransactionId);
		assertNotNull(storeSaved);
	}

	@Test
	public void testUpdateStoreSuccess() throws MarketplaceException {
		Store storeUpdated = new Store();
		StoreDomain storeD = new StoreDomain();
		when(modelMapper.map(storeDomain, Store.class)).thenReturn(store);
		when(storeRepository.save(store)).thenReturn(storeUpdated);
		storeDomain.updateStore(storeD, storeUpdated,externalTransactionId);
		assertNotNull(storeUpdated);
	}

	@Test(expected = MarketplaceException.class)
	public void testUpdateStoreMongoException() throws MarketplaceException {
		when(modelMapper.map(storeD, Store.class)).thenReturn(store);
		when(this.storeRepository.save(store)).thenThrow(MongoWriteException.class);
		storeDomain.updateStore(storeD, store,externalTransactionId);
		assertNotNull(store);
	}

	@Test(expected = MarketplaceException.class)
	public void testUpdateStoreException() throws MarketplaceException {
		when(modelMapper.map(storeD, Store.class)).thenThrow(NullPointerException.class);
		when(this.storeRepository.save(store)).thenReturn(store);
		storeDomain.updateStore(storeD, store,externalTransactionId);
		assertNotNull(store);
	}

	@Test
	public void testCheckEmailExistsSuccess() throws MarketplaceException {
		Optional<Store> storeCheck = Optional.of(new Store());
		storeCheck.get().setStoreCode(storeCode);
		storeCheck.get().setMerchantCode(merchantCode);

		List<ContactPerson> contacts = new ArrayList<ContactPerson>();
		ContactPerson contactPerson = new ContactPerson();
		contactPerson.setEmailId(emailId);
		contactPerson.setFirstName("Name");
		contactPerson.setLastName("Name");
		contactPerson.setMobileNumber("Mobile");
		contactPerson.setUserName(userName);
		storeCheck.get().setContactPersons(contacts);
		contacts.add(contactPerson);
		when(storeRepository.findByStoreCodeAndEmail(storeCode, emailId)).thenReturn(storeCheck);
		boolean chk = storeDomain.checkEmailExists(storeCode, merchantCode, emailId, userName, resultResponse);
		assertTrue(chk);
	}

	@Test
	public void testCheckEmailExistsFailure() throws MarketplaceException {
		Optional<Store> storeCheck = Optional.of(new Store());
		storeCheck.get().setStoreCode(storeCode);
		storeCheck.get().setMerchantCode(merchantCode);

		List<ContactPerson> contacts = new ArrayList<ContactPerson>();
		ContactPerson contactPerson = new ContactPerson();
		contactPerson.setEmailId("Email ID");
		contactPerson.setFirstName("Name");
		contactPerson.setLastName("Name");
		contactPerson.setMobileNumber("Mobile");
		contactPerson.setUserName("Different Username");
		storeCheck.get().setContactPersons(contacts);
		contacts.add(contactPerson);
		when(storeRepository.findByStoreCodeAndEmail(storeCode, emailId)).thenReturn(storeCheck);
		boolean chk = storeDomain.checkEmailExists(storeCode, merchantCode, emailId, userName, resultResponse);
		assertFalse(chk);
	}

	@Test
	public void testCreateContactPersonDomain() {
		List<ContactPersonDto> contacts = new ArrayList<ContactPersonDto>();
		ContactPersonDto contactPerson = new ContactPersonDto();
		contactPerson.setEmailId("Email ID");
		contactPerson.setFirstName("Name");
		contactPerson.setLastName("Name");
		contactPerson.setMobileNumber("Mobile");
		contactPerson.setUserName("Different Username");
		contacts.add(contactPerson);
		when(contactPersonDomain.createContactPersons(contacts, storeCode, merchantCode, true, resultResponse)).thenReturn(storeContactPersons);
		List<StoreContactPersonDomain> contact = storeDomain.createContactPersonDomain(contacts, storeCode, merchantCode, true, resultResponse);
		assertNotNull(contact);
	}
	
	@Test
	public void testGetStoreCode()
	{
		assertEquals(storeD.getStoreCode(), storeCode);
	}
	
	@Test
	public void testGetContactPersonDomain()
	{
		storeD.getContactPersonDomain();
		assertNotNull(storeD);
	}
	
	@Test
	public void testGetId()
	{
		assertEquals(storeD.getId(), id);
	}
	
	@Test
	public void testGetProgramCode()
	{
		assertEquals(storeD.getProgramCode(), programCode);
	}
	
	@Test
	public void testGetDescription()
	{
		assertEquals(storeD.getDescription(), storeDescriptionDomain);
	}
	
	@Test
	public void testGetMerchantCode()
	{
		assertEquals(storeD.getMerchantCode(), merchantCode);
	}
	
	@Test
	public void testGetStatus()
	{
		assertEquals(storeD.getStatus(), status);
	}
	
	@Test
	public void testGetAddress()
	{
		assertEquals(storeD.getAddress(), storeAddressDomain);
	}
	
	@Test
	public void testetLongitude()
	{
		assertEquals(storeD.getLongitude(), longitude);
	}
	
	@Test
	public void testGetLatitude()
	{
		assertEquals(storeD.getLatitude(), latitude);
	}

	@Test
	public void testGetUsrCreated()
	{
		assertEquals(storeD.getUsrCreated(), usrCreated);
	}
	
	@Test
	public void testGetUsrUpdated()
	{
		assertEquals(storeD.getUsrUpdated(), usrUpdated);
	}
	
	@Test
	public void testGetDtUpdated()
	{
		assertEquals(storeD.getDtUpdated(), dtUpdated);
	}
	
	@Test
	public void testGetDtCreated()
	{
		assertEquals(storeD.getDtCreated(), dtCreated);
	}
}
