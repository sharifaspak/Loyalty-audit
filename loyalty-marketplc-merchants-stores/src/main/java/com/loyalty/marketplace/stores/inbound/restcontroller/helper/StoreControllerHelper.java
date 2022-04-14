package com.loyalty.marketplace.stores.inbound.restcontroller.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.merchants.outbound.database.entity.Merchant;
import com.loyalty.marketplace.merchants.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.stores.constants.StoreCodes;
import com.loyalty.marketplace.stores.constants.StoreConstants;
import com.loyalty.marketplace.stores.domain.model.StoreContactPersonDomain;
import com.loyalty.marketplace.stores.domain.model.StoreDomain;
import com.loyalty.marketplace.stores.inbound.dto.StoreDto;
import com.loyalty.marketplace.stores.outbound.database.entity.ContactPerson;
import com.loyalty.marketplace.stores.outbound.database.entity.Store;

@Component
public class StoreControllerHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(StoreDomain.class);

	@Autowired
	StoreDomain storeDomain;
	
	@Autowired
	MerchantRepository merchantRepository;
	
	public List<StoreContactPersonDomain> prepareContactPersonDomainList(StoreDto storeDto, String storeCode, ResultResponse resultResponse,
			Store storeDetails) {
		LOG.info("Inside prepareContactPersonDomainList starts");
		List<StoreContactPersonDomain> contactPersonDomainToUpdate = new ArrayList<>();
		for (final ContactPerson contactPerson : storeDetails.getContactPersons()) {

			boolean exists = false;

			for (final ContactPersonDto contactPersonDto : storeDto.getContactPersons()) {

				if (contactPerson.getUserName().equals(contactPersonDto.getUserName())
						&& storeDomain.checkEmailExists(storeCode, storeDto.getMerchantCode(), contactPersonDto.getEmailId(),
								contactPersonDto.getUserName(), resultResponse)) {
					exists = true;
					contactPersonDomainToUpdate.add(new StoreContactPersonDomain.ContactPersonBuilder(
							contactPersonDto.getEmailId(),
							contactPersonDto.getMobileNumber(),
							contactPersonDto.getFirstName(),
							contactPersonDto.getLastName())
									.faxNumber(contactPersonDto.getFaxNumber())
									.userName(contactPersonDto.getUserName())
									.pin(contactPerson.getPin()).build());
				}

			}

			if (!exists) {
				contactPersonDomainToUpdate.add(
						new StoreContactPersonDomain.ContactPersonBuilder(
								contactPerson.getEmailId(),
								contactPerson.getMobileNumber(),
								contactPerson.getFirstName(), 
								contactPerson.getLastName()).faxNumber(contactPerson.getFaxNumber())
										.userName(contactPerson.getUserName())
										.pin(contactPerson.getPin()).build());
			}

		}
		LOG.info("Inside prepareContactPersonDomainList ends");
		return contactPersonDomainToUpdate;
	}
	
	public void validateUsernameExists(StoreDto storeDto, ResultResponse resultResponse, Store storeDetails) {
		LOG.info("Inside validateUsernameExists starts");
		for (final ContactPersonDto contactPersonDto : storeDto.getContactPersons()) {
			boolean isExists = false;
			if (StringUtils.isEmpty(contactPersonDto.getUserName())) {
				resultResponse.addErrorAPIResponse(StoreCodes.INVALID_USER_NAME.getIntId(),
						StoreCodes.INVALID_USER_NAME.getMsg());
			} else {
				for (final ContactPerson contactPerson : storeDetails.getContactPersons()) {
					if (contactPersonDto.getUserName().equals(contactPerson.getUserName())) {
						isExists = true;
					}
				}
				if (!isExists) {
					resultResponse.addErrorAPIResponse(StoreCodes.USERNAME_NOT_FOUND.getIntId(),
							contactPersonDto.getUserName() + StoreConstants.SEMI_COLON.get() + StoreCodes.USERNAME_NOT_FOUND.getMsg());
				}
			}

		}
		
		LOG.info("Inside validateUsernameExists ends");
	}
	
	public List<Merchant> getMerchants(List<Store> stores, String program) {
		
		List<String> merchantCodesList = new ArrayList<>();
		Set<String> uniqueMerchantCodes = stores.stream().map(store -> store.getMerchantCode()).collect(Collectors.toCollection(HashSet::new));
		uniqueMerchantCodes.forEach(i -> merchantCodesList.add(i)); 
		//return merchantRepository.findByMerchantCodeList(merchantCodesList);
		return merchantRepository.findByMerchantCodeAndProgramCodeListIgnoreCase(merchantCodesList, program);
		
	}
	
}
