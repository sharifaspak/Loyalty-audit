package com.loyalty.marketplace.stores.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.stores.constants.StoreCodes;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;
import com.loyalty.marketplace.stores.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.utils.Utils;

@Component
public class StoreContactPersonDomain {

	@Autowired
	StoreRepository storeRepository;
	
	@Value("${def.credential}")
	private String CREDENTIAL;

	private static final Logger LOG = LoggerFactory.getLogger(StoreContactPersonDomain.class);

	public StoreRepository getStoreRepository() {
		return storeRepository;
	}

	private String emailId;

	private String mobileNumber;

	private String firstName;

	private String lastName;

	private String faxNumber;

	private String userName;

	private String password;

	private Integer pin;

	public StoreContactPersonDomain() {

	}

	public StoreContactPersonDomain(ContactPersonBuilder contactPerson) {
		super();
		this.emailId = contactPerson.emailId;
		this.mobileNumber = contactPerson.mobileNumber;
		this.firstName = contactPerson.firstName;
		this.lastName = contactPerson.lastName;
		this.faxNumber = contactPerson.faxNumber;
		this.userName = contactPerson.userName;
		this.password = contactPerson.password;
		this.pin = contactPerson.pin;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public Integer getPin() {
		return pin;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public static class ContactPersonBuilder {

		private String emailId;

		private String mobileNumber;

		private String firstName;

		private String lastName;

		private String faxNumber;

		private String userName;

		private String password;

		private Integer pin;

		public ContactPersonBuilder(String emailId, String mobileNumber, String firstName, String lastName) {
			super();
			this.emailId = emailId;
			this.mobileNumber = mobileNumber;
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public ContactPersonBuilder faxNumber(String faxNumber) {
			this.faxNumber = faxNumber;
			return this;

		}

		public ContactPersonBuilder userName(String userName) {
			this.userName = userName;
			return this;

		}

		public ContactPersonBuilder password(String password) {
			this.password = password;
			return this;
		}

		public ContactPersonBuilder pin(Integer pin) {
			this.pin = pin;
			return this;
		}

		public StoreContactPersonDomain build() {
			return new StoreContactPersonDomain(this);
		}
	}

	@Override
	public String toString() {
		return "ContactPersonDomain [emailId=" + emailId + ", mobileNumber=" + mobileNumber + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", faxNumber=" + faxNumber + ", userName=" + userName + ", password="
				+ password + "]";
	}

	public List<StoreContactPersonDomain> createContactPersons(List<ContactPersonDto> contactPersonDtoList,
			String storeCode, String merchantCode, boolean optInorOut, ResultResponse resultResponse) {

		List<StoreContactPersonDomain> contactPersonList = new ArrayList<>();

		Optional<Store> store = Optional.empty();

		for (final ContactPersonDto contactPerson : contactPersonDtoList) {

			boolean exists = false;
			if (!optInorOut) {
				store = storeRepository.findByStoreCodeAndMerchantCodeAndEmailId(storeCode, merchantCode,
						contactPerson.getEmailId());

				if (store.isPresent()) {
					exists = true;
					resultResponse.addErrorAPIResponse(StoreCodes.CONTACT_PERSON_EXISTS.getIntId(),
							contactPerson.getEmailId() + ":" + StoreCodes.CONTACT_PERSON_EXISTS.getMsg());
				} else {

					for (final StoreContactPersonDomain contactPersonDomain : contactPersonList) {
						if (contactPerson.getEmailId().contentEquals(contactPersonDomain.getEmailId())) {
							exists = true;
							resultResponse.addErrorAPIResponse(StoreCodes.CONTACT_PERSON_EXISTS.getIntId(),
									contactPerson.getEmailId() + " : " + StoreCodes.CONTACT_PERSON_EXISTS.getMsg());
						}
					}

				}
				if (!exists) {
					String username = contactPerson.getEmailId().split("@")[0] + "_" + merchantCode + storeCode;
					String credential = Utils.generatePassword();
					Integer storePin = generatePin(merchantCode);
					contactPersonList.add(new StoreContactPersonDomain.ContactPersonBuilder(contactPerson.getEmailId(),
							contactPerson.getMobileNumber(), contactPerson.getFirstName(), contactPerson.getLastName())
									.faxNumber(contactPerson.getFaxNumber()).userName(username).password(credential)
									.pin(storePin).build());
				}
			} else {

				store = storeRepository.findByStoreCodeAndMerchantCodeAndUserName(storeCode, merchantCode,
						contactPerson.getUserName());

				if (store.isPresent()) {
					exists = true;
					resultResponse.addErrorAPIResponse(StoreCodes.CONTACT_PERSON_EXISTS.getIntId(),
							contactPerson.getEmailId() + ":" + StoreCodes.CONTACT_PERSON_EXISTS.getMsg());
				} else {

					for (final StoreContactPersonDomain contactPersonDomain : contactPersonList) {
						if (contactPerson.getUserName().contentEquals(contactPersonDomain.getUserName())) {
							exists = true;
							resultResponse.addErrorAPIResponse(StoreCodes.CONTACT_PERSON_EXISTS.getIntId(),
									contactPerson.getEmailId() + " : " + StoreCodes.CONTACT_PERSON_EXISTS.getMsg());
						}
					}

				}
				if (!exists) {
					
					String username = contactPerson.getUserName();
					String credential = CREDENTIAL;
					Integer storePin = generatePin(merchantCode);
					contactPersonList.add(new StoreContactPersonDomain.ContactPersonBuilder(contactPerson.getEmailId(),
							contactPerson.getMobileNumber(), contactPerson.getFirstName(), contactPerson.getLastName())
									.faxNumber(contactPerson.getFaxNumber()).userName(username).password(credential)
									.pin(storePin).build());
				}

			}
		}

		LOG.info("Persisted Object: ");

		return contactPersonList;
	}

	private Integer generatePin(String merchantCode) {

		Integer storePin = Utils.generateRandomNumber(5);

		Optional<Store> store = storeRepository.findByMerchantCodeAndPin(merchantCode, storePin);
	
		while (store.isPresent()) {
			storePin = Utils.generateRandomNumber(5);
			store = storeRepository.findByMerchantCodeAndPin(merchantCode, storePin);
		}
		
		return storePin;

	}
}
