package com.loyalty.marketplace.interest.inbound.restcontroller;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loyalty.marketplace.offers.member.management.inbound.dto.MemberDetailRequestDto;

import io.swagger.annotations.Api;

@Api(value = "marketplace")
@RestController
@RequestMapping("/memberManagement")
public class LoyaltyStubController {
	private static final Logger LOG = LoggerFactory.getLogger(LoyaltyStubController.class);

	@PostMapping(value = "/member")
	public String getMemberCall(@RequestBody MemberDetailRequestDto getMemberRequest) {
		return stubMemberCall();
	}

	private File getMemberFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		URL sizejsonPath = classLoader.getResource("stubjson/getMember.json");
		//URL sizejsonPath = classLoader.getResource("stubjson/getMemberNotFound.json");
		return new File(sizejsonPath.getPath());
	}
	private String stubMemberCall() {
		JSONParser parser = new JSONParser();
		String responseObj = null;
		try {
			Object obj = parser.parse(new FileReader(getMemberFile()));
			JSONObject json = (JSONObject) obj;
		//	Gson gsonObje = new Gson();
			responseObj = json.toString();
			//responseObj.getResult().getAccounts().get(0).setAccountNumber(accountNumber);
		} catch (Exception e) {
			LOG.info("Error during crm stub creation:" + e.getLocalizedMessage());
		}
		return responseObj;
	}
}