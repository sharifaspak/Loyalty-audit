package com.loyalty.marketplace.stores.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.loyalty.marketplace.audit.AuditService;
import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.stores.constants.DBConstants;
import com.loyalty.marketplace.stores.constants.StoreCodes;
import com.loyalty.marketplace.stores.constants.StoreRequestMappingConstants;
import com.loyalty.marketplace.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.utils.Utils;
import com.mongodb.MongoWriteException;

@Component
public class StoreDomain {

	private static final Logger LOG = LoggerFactory.getLogger(StoreDomain.class);

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	StoreContactPersonDomain contactPersonDomain;
	
	@Autowired
	AuditService auditService;
	
	private String id;
	
	private String programCode;

	private String storeCode;

	private StoreDescriptionDomain description;
	
	private String merchantCode;

	private String status;

	private StoreAddressDomain address;
	
	private String longitude;
	
	private String latitude;

	private String usrCreated;

	private String usrUpdated;

	private Date dtCreated;

	public StoreContactPersonDomain getContactPersonDomain() {
		return contactPersonDomain;
	}
	

	public String getId() {
		return id;
	}


	public String getProgramCode() {
		return programCode;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public StoreDescriptionDomain getDescription() {
		return description;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public String getStatus() {
		return status;
	}

	public StoreAddressDomain getAddress() {
		return address;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getUsrCreated() {
		return usrCreated;
	}

	public String getUsrUpdated() {
		return usrUpdated;
	}

	public Date getDtCreated() {
		return dtCreated;
	}

	public Date getDtUpdated() {
		return dtUpdated;
	}

	public List<StoreContactPersonDomain> getContactPersons() {
		return contactPersons;
	}

	private Date dtUpdated;

	private List<StoreContactPersonDomain> contactPersons;	

	
	public StoreDomain() {

	}

	public StoreDomain(StoreBuilder storeBuilder) {
		super();
		this.programCode = storeBuilder.programCode;
		this.merchantCode = storeBuilder.merchantCode;
		this.storeCode = storeBuilder.storeCode;
		this.latitude = storeBuilder.latitude;
		this.longitude = storeBuilder.longitude;
		this.address = storeBuilder.address;
		this.description = storeBuilder.description;
		this.status = storeBuilder.status;
		this.dtCreated = storeBuilder.dtCreated;
		this.dtUpdated = storeBuilder.dtUpdated;
		this.usrCreated = storeBuilder.usrCreated;
		this.usrUpdated = storeBuilder.usrUpdated;
		this.contactPersons = storeBuilder.contactPersons;
		this.id = storeBuilder.id;
	
	}

	public static class StoreBuilder {

		private String id;

		private String programCode;

		private String storeCode;

		private StoreDescriptionDomain description;
		
		private String merchantCode;

		private String status;

		private StoreAddressDomain address;
		
		private String longitude;
		
		private String latitude;

		private String usrCreated;

		private String usrUpdated;

		private Date dtCreated;

		private Date dtUpdated;
	
		private List<StoreContactPersonDomain> contactPersons;	
		
		public StoreBuilder(String storeCode, StoreDescriptionDomain description, String merchantCode,
				List<StoreContactPersonDomain> contactPersons, String status) {
			super();
			this.storeCode = storeCode;
			this.description = description;
			this.merchantCode = merchantCode;
			this.contactPersons = contactPersons;
			this.status = status;
			
		}
		
		public StoreBuilder id(String id) {
			this.id = id;
			return this;
		}

		public StoreBuilder programCode(String programCode) {
			this.programCode = programCode;
			return this;
		}

		public StoreBuilder status(String status) {
			this.status = status;
			return this;
		}

		public StoreBuilder address(StoreAddressDomain address) {
			this.address = address;
			return this;
		}

	
		public StoreBuilder longitude(String longitude) {
			this.longitude = longitude;
			return this;
		}
		
		public StoreBuilder latitude(String latitude) {
			this.latitude = latitude;
			return this;
		}

	
		public StoreBuilder dtUpdated(Date dtUpdated) {
			this.dtUpdated = dtUpdated;
			return this;
		}

		public StoreBuilder dtCreated(Date dtCreated) {
			this.dtCreated = dtCreated;
			return this;
		}
		
		public StoreBuilder usrCreated(String usrCreated) {
			this.usrCreated = usrCreated;
			return this;
		}

		public StoreBuilder usrUpdated(String usrUpdated) {
			this.usrUpdated = usrUpdated;
			return this;
		}
		
		public StoreDomain build() {
			return new StoreDomain(this);
		}

	}
	
	@Override
	public String toString() {
		return "StoreDomain [modelMapper=" + modelMapper + ", storeRepository=" + storeRepository
				+ ", contactPersonDomain=" + contactPersonDomain + ", id=" + id + ", programCode=" + programCode
				+ ", storeCode=" + storeCode + ", description=" + description + ", merchantCode=" + merchantCode
				+ ", status=" + status + ", address=" + address + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", usrCreated=" + usrCreated + ", usrUpdated=" + usrUpdated + ", dtCreated=" + dtCreated
				+ ", dtUpdated=" + dtUpdated + ", contactPersons=" + contactPersons + "]";
	}
	
	private void sendCredentialsToLdap(StoreDomain store) {
		for (StoreContactPersonDomain contactPerson : store.getContactPersons()) {
			Utils.sendCredentailsToLdap(contactPerson.getUserName(), contactPerson.getPassword(),
					contactPerson.getEmailId());
		}
	}

	public void sendSmsToContactPersons(StoreDomain store) {
		for (StoreContactPersonDomain contactPerson : store.getContactPersons()) {
			Utils.sendMail(contactPerson.getUserName(), contactPerson.getPassword(), contactPerson.getEmailId());
		}
	}

	public List<StoreContactPersonDomain> createContactPersonDomain(List<ContactPersonDto> contactPersons,
			String storeCode, String merchantCode, boolean optInorOut, ResultResponse resultResponse) {
		return contactPersonDomain.createContactPersons(contactPersons, storeCode, merchantCode, optInorOut,
				resultResponse);
	}

	public void saveStore(StoreDomain storeDomain,String transactionId) throws MarketplaceException {
		LOG.info("Domain Object To Be Persisted: {}", storeDomain);
		try {
			Store store = modelMapper.map(storeDomain, Store.class);
			this.storeRepository.insert(store);
//			auditService.insertDataAudit(DBConstants.STORE, store,StoreRequestMappingConstants.CONFIGURE_STORE,transactionId,storeDomain.getUsrCreated());

			sendCredentialsToLdap(storeDomain);
			sendSmsToContactPersons(storeDomain);

			LOG.info("Persisted Object: {}", store);
		} catch (MongoWriteException mongoException) {

			LOG.error("MongoDB persist exception occured while saving Store to database.");
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_STORE.getConstant(),
					mongoException.getClass() + mongoException.getMessage(), StoreCodes.STORE_CREATION_FAILED);

		}

		catch (Exception e) {

			LOG.error("Runtime Exception exception occured while saving Store to database.");
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_STORE.getConstant(), e.getClass() + e.getMessage(),
					StoreCodes.STORE_CREATION_FAILED);

		}

	}

	public void configureContactPerson(Store store, List<StoreContactPersonDomain> contactPersonDomain,
			String userName, String program, String configureConatactPerson,String transactionId) {

		List<ContactPerson> contactPersonList = new ArrayList<>();
		
	    Gson gson = new Gson();
		Store oldStore = gson.fromJson(gson.toJson(store), Store.class);
		
		LOG.info("Existing store object : {}", oldStore);
		for (final StoreContactPersonDomain contactDomain : contactPersonDomain) {
			contactPersonList.add(modelMapper.map(contactDomain, ContactPerson.class));
		}

		store.getContactPersons().addAll(contactPersonList);
		store.setDtUpdated(new Date());
		store.setProgramCode(program);
		store.setUsrUpdated(userName);

		this.storeRepository.save(store);
		LOG.info("After updating store object : {}", store);
		auditService.updateDataAudit(DBConstants.STORE, store,configureConatactPerson,oldStore,transactionId,userName);


		for (StoreContactPersonDomain contactPerson : contactPersonDomain) {
			Utils.sendCredentailsToLdap(contactPerson.getUserName(), contactPerson.getPassword(),
					contactPerson.getEmailId());
			Utils.sendMail(contactPerson.getUserName(), contactPerson.getPassword(), contactPerson.getEmailId());
		}

	}
	


	public void updateStore(StoreDomain storeDomainToUpdate, Store existingStore,String transactionId) throws MarketplaceException {
		LOG.info("Inside updateStore starts");
		try {
			Store store = modelMapper.map(storeDomainToUpdate, Store.class);
			this.storeRepository.save(store);
			auditService.updateDataAudit(DBConstants.STORE, store, StoreRequestMappingConstants.UPDATE_STORE, existingStore,transactionId,storeDomainToUpdate.getUsrUpdated());
		} catch (MongoWriteException mongoException) {

			LOG.error("MongoDB persist exception occured while Updating Store");
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_STORE.getConstant(),
					mongoException.getClass() + mongoException.getMessage(), StoreCodes.STORE_UPDATION_FAILED);

		}

		catch (Exception e) {

			LOG.error("Runtime Exception exception occured while Updating Store.");
			throw new MarketplaceException(this.getClass().toString(), MarketPlaceCode.SAVE_STORE.getConstant(), e.getClass() + e.getMessage(),
					StoreCodes.STORE_UPDATION_FAILED);

		}
		LOG.info("Inside updateStore ends");
	}


	public boolean checkEmailExists(String storeCode, String merchantCode, String emailId, String userName, ResultResponse resultResponse) {
		Optional<Store> store = storeRepository.findByStoreCodeAndMerchantCodeAndEmailId(storeCode, merchantCode, emailId);
		//List<Store> store = storeRepository.findByStoreCodeAndMerchantCodeAndEmailIdList(storeCode, merchantCode, emailId);

		if(store.isPresent() && null != store.get()) {
			for(final ContactPerson contactperson: store.get().getContactPersons()) {
				
				if(contactperson.getEmailId().equals(emailId) && !contactperson.getUserName().equals(userName)) {
					resultResponse.addErrorAPIResponse(StoreCodes.CONTACT_PERSON_EXISTS.getIntId(),emailId+ " : "+  StoreCodes.CONTACT_PERSON_EXISTS.getMsg());

					return false;
				}
			}
			
		}
	
		return true;
	}
}
