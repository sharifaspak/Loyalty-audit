package com.loyalty.marketplace.voucher.outbound.database.repository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.loyalty.marketplace.voucher.outbound.database.entity.VoucherAction;

@SpringBootTest(classes = VoucherActionRepository.class)
@ActiveProfiles("unittest")
public class VoucherActionRepositoryTest {
	
	private VoucherActionRepository voucherActionRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		voucherActionRepository = mock(VoucherActionRepository.class);
	}

	@Test
	public void test() {
		VoucherAction voucherAction = new VoucherAction();
		when(voucherActionRepository.findByRedemptionMethod("")).thenReturn(Optional.of(voucherAction));
	}

}