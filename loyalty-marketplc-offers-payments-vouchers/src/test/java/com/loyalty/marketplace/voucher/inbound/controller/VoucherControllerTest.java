package com.loyalty.marketplace.voucher.inbound.controller;

import static org.junit.Assert.fail;

import java.util.List;

import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.loyalty.marketplace.MarketplaceApplication.ProgramManagement;
import com.loyalty.marketplace.offers.inbound.restcontroller.OffersController;
import com.loyalty.marketplace.offers.outbound.database.repository.OfferRepository;
import com.loyalty.marketplace.offers.outbound.service.MemberManagementService;
import com.loyalty.marketplace.outbound.database.repository.MerchantRepository;
import com.loyalty.marketplace.outbound.database.repository.StoreRepository;
import com.loyalty.marketplace.outbound.events.handler.EventHandler;
import com.loyalty.marketplace.subscription.outbound.database.repository.SubscriptionRepositoryHelper;
import com.loyalty.marketplace.voucher.domain.VoucherActionDomain;
import com.loyalty.marketplace.voucher.domain.VoucherDomain;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherActionRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherRepository;
import com.loyalty.marketplace.voucher.outbound.database.repository.VoucherUploadFileRepository;
import com.loyalty.marketplace.voucher.outbound.service.CarreFourService;
import com.loyalty.marketplace.voucher.outbound.service.MafService;
import com.loyalty.marketplace.voucher.service.VoucherService;

@SpringBootTest(classes=VoucherController.class)
@AutoConfigureMockMvc
@ActiveProfiles("unittest")
@EnableWebMvc
public class VoucherControllerTest {
	@Mock
	Validator validator;

	@Mock
	ModelMapper modelMapper;

	@Mock
	VoucherActionRepository voucherActionRepository;

	@Mock
	VoucherActionDomain voucherActionDomain;

	@Mock
	VoucherDomain voucherDomain;

	@Mock
	VoucherControllerHelper voucherControllerHelper;

	@Mock
	VoucherRepository voucherRepository;

	@Mock
	MerchantRepository merchantRepository;

	@Mock
	OfferRepository offerRepository;	

	@Mock
	MemberManagementService memberManagementService;

	@Mock
	private MafService mafService;
	
	@Mock
	private CarreFourService carreFourService;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	ProgramManagement programManagement;
	
	@Mock
	private VoucherUploadFileRepository voucherUploadFileRepository;
	
	@Mock
	SubscriptionRepositoryHelper subscriptionRepositoryHelper;
	
	@Mock
	private EventHandler eventHandler;
	
	@Mock
	private VoucherService voucherService;
		
	@Mock
	MongoOperations mongoOperations;
	
	@InjectMocks
	VoucherController voucherController;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}
