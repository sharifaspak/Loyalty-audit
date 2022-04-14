package com.loyalty.marketplace.stores.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


import com.loyalty.marketplace.utils.Utils;
import com.loyalty.marketplace.inbound.dto.ContactPersonDto;
import com.loyalty.marketplace.stores.inbound.dto.StoreDto;

public class StoreUtils extends Utils{

	public static boolean merchantInputValidations(String action, String barcodeType, String category,
			List<ContactPersonDto> contactPersons, String earnMultiplier, String merchantCode, String merchantNameAr,
			String merchantNameEn, String partnerCode, String userName, String phoneNumber) {
		return false;
}

	public static StoreDto removeSpecialCharsMerchantInput(StoreDto storeInput) {
		StoreDto storeOutput = new StoreDto();
		List<ContactPersonDto> contactPersonDtoList = new ArrayList<ContactPersonDto>();
		
		for (final ContactPersonDto contactPersonDto : storeInput.getContactPersons()) {
			ContactPersonDto contactPerson = new ContactPersonDto();
			contactPerson.setEmailId(removeSpecialChars(contactPersonDto.getEmailId()));
			contactPerson.setFaxNumber(removeSpecialChars(contactPersonDto.getFaxNumber()));
			contactPerson.setFirstName(removeSpecialChars(contactPersonDto.getFirstName()));
			contactPerson.setMobileNumber(removeSpecialChars(contactPersonDto.getMobileNumber()));
			contactPersonDtoList.add(contactPerson);
		}
		storeOutput.setContactPersons(contactPersonDtoList);
		storeOutput.setStoreCode(removeSpecialChars(storeInput.getStoreCode()));
		storeOutput.setDescriptionAr(removeSpecialChars(storeInput.getDescriptionAr()));
		storeOutput.setDescriptionEn(removeSpecialChars(storeInput.getDescription()));
		storeOutput.setMerchantCode(removeSpecialChars(storeInput.getMerchantCode()));
		
		return storeOutput;
	}

	

}
