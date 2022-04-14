package com.loyalty.marketplace.merchants.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.inbound.restcontroller.MerchantController;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.utils.Utils;

@Component
public class MerchantContactPersonDomain {
	
	private static final Logger LOG = LoggerFactory.getLogger(MerchantContactPersonDomain.class);

	@Value("${def.credential}")
	private String CREDENTIAL;

	@Autowired
	MerchantRepository merchantRepository;
	
	private String emailId;

	private String firstName;

	private String lastName;

	private String mobileNumber;

	private String faxNumber;

	private String userName;

	private String password;

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public MerchantContactPersonDomain() {
		
	}

	public MerchantContactPersonDomain(ContactPersonBuilder contactPerson) {
		super();
		this.emailId = contactPerson.emailId;
		this.firstName = contactPerson.firstName;
		this.lastName = contactPerson.lastName;
		this.mobileNumber = contactPerson.mobileNumber;
		this.faxNumber = contactPerson.faxNumber;
		this.userName = contactPerson.userName;
		this.password = contactPerson.password;
	}

	public static class ContactPersonBuilder {

		private String emailId;

		private String firstName;

		private String lastName;

		private String mobileNumber;

		private String faxNumber;

		private String userName;

		private String password;

		public ContactPersonBuilder(String emailId, String firstName, String lastName, String mobileNumber) {
			super();
			this.emailId = emailId;
			this.firstName = firstName;
			this.lastName = lastName;
			this.mobileNumber = mobileNumber;

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

		public MerchantContactPersonDomain build() {
			return new MerchantContactPersonDomain(this);
		}
	}

	public List<MerchantContactPersonDomain> createContactPersons(List<ContactPersonDto> contactPersonDtoList,
			List<ContactPersonDto> invalidContactPersons, boolean optInorOut, String merchantCode) {

		List<MerchantContactPersonDomain> contactPersonList = new ArrayList<>();
		Optional<Merchant> merchant = Optional.empty();

		for (final ContactPersonDto contactPerson : contactPersonDtoList) {
			
			boolean exists = false;
			LOG.info("Inside createContactPersons :optInorOut :: {}" , optInorOut);
			if(!optInorOut) {
			 merchant = merchantRepository.findByMerchantCodeAndEmailId(merchantCode, contactPerson.getEmailId());
			
			if(merchant.isPresent()) {
				exists = true;
				invalidContactPersons.add(contactPerson);
			} else {

				for (final MerchantContactPersonDomain contactPersonDomain : contactPersonList) {
					if (contactPerson.getEmailId().contentEquals(contactPersonDomain.getEmailId())) {
						exists = true;
						invalidContactPersons.add(contactPerson);
					}
				}

			}
			if (!exists) {
				String name = merchantCode+contactPerson.getEmailId().split("@")[0];
				String credential = Utils.generatePassword();
				contactPersonList.add(new MerchantContactPersonDomain.ContactPersonBuilder(contactPerson.getEmailId(),
						contactPerson.getFirstName(), contactPerson.getLastName(), contactPerson.getMobileNumber())
								.faxNumber(contactPerson.getFaxNumber()).userName(name).password(credential).build());
			}
			}else {
				merchant = merchantRepository.findByMerchantCodeAndUserName(merchantCode, contactPerson.getUserName());
				
				if(merchant.isPresent()) {
					exists = true;
					invalidContactPersons.add(contactPerson);
				} else {

					for (final MerchantContactPersonDomain contactPersonDomain : contactPersonList) {
						if (contactPerson.getUserName().contentEquals(contactPersonDomain.getUserName())) {
							exists = true;
							invalidContactPersons.add(contactPerson);
						}
					}

				}
				if (!exists) {
					String name = contactPerson.getUserName();
					String credential = CREDENTIAL;
					contactPersonList.add(new MerchantContactPersonDomain.ContactPersonBuilder(contactPerson.getEmailId(),
							contactPerson.getFirstName(), contactPerson.getLastName(), contactPerson.getMobileNumber())
									.faxNumber(contactPerson.getFaxNumber()).userName(name).password(credential).build());
				}

			}
		}
		return contactPersonList;
	}
	
}
