package com.loyalty.marketplace.voucher.outbound.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.offers.member.management.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.offers.outbound.service.helper.ServiceHelper;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.member.management.inbound.dto.MemberDetailRequestDto;
import com.loyalty.marketplace.voucher.utils.VoucherManagementException;
import com.loyalty.marketplace.service.ExceptionLogsService;
import com.loyalty.marketplace.service.RetryLogsService;

@Service
@RefreshScope
public class VoucherPointsBankService {

	@Value("${points.bank.base.uri}")
	private String pointsBankBaseUri;

	@Value("${points.bank.financial.path}")
	private String pointsBankFinancialPath;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("getTemplateBean")
	private RestTemplate getRestTemplate;

	@Autowired
	private ServiceHelper serviceHelper;

	@Autowired
	private RetryTemplate retryTemplate;

	@Autowired
	RetryLogsService retryLogsService;

	@Autowired
	ExceptionLogsService exceptionLogsService;

	private static final Logger LOG = LoggerFactory.getLogger(VoucherPointsBankService.class);

	public boolean checkIfInvoiced(String voucherCode, Headers header) throws VoucherManagementException {

		String url = pointsBankBaseUri + pointsBankFinancialPath + voucherCode;

		LOG.info("Pointsbank URL to check if voucher code is invoiced : {} ", url);
		try {
			HttpEntity<?> entity = new HttpEntity<>(serviceHelper.getHeader(header));
			ResponseEntity<CommonApiStatus> body = retryCallForCheckIfInvoiced(url, entity, header);
			// ResponseEntity<CommonApiStatus> body = restTemplate.getForEntity(url,
			// CommonApiStatus.class);
			Integer result = null;
			CommonApiStatus responseBody = body.getBody();
			LOG.info("Response from financial management for voucher code  {} : {} ", voucherCode, body);
			if (responseBody != null) {
				result = responseBody.getApiStatus().getStatusCode();
				if (result == 0) {
					return true;
				} else {
					return false;

				}
			} else {
				throw new VoucherManagementException(this.getClass().toString(),
						VoucherConstants.CHECK_VOUCHER_INVOICED,
						this.getClass() + VoucherManagementCode.VOUCHER_INVOICED_CHECK_REST_CLIENT_EXCEPTION.getMsg(),
						VoucherManagementCode.VOUCHER_INVOICED_CHECK_REST_CLIENT_EXCEPTION);

			}

		} catch (RestClientException e) {
			throw new VoucherManagementException(this.getClass().toString(), VoucherConstants.CHECK_VOUCHER_INVOICED,
					e.getClass() + e.getMessage(), VoucherManagementCode.VOUCHER_INVOICED_CHECK_REST_CLIENT_EXCEPTION);

		} catch (Exception e) {
			throw new VoucherManagementException(this.getClass().toString(), VoucherConstants.CHECK_VOUCHER_INVOICED,
					e.getClass() + e.getMessage(), VoucherManagementCode.VOUCHER_INVOICED_CHECK_RUNTIME_EXCEPTION);

		}
	}

	private ResponseEntity<CommonApiStatus> retryCallForCheckIfInvoiced(String url, HttpEntity<?> entity,
			Headers header) {
		try {
			LOG.info("inside Retry block using retryTemplate of retryCallForCheckIfInvoiced method of class {}",
					this.getClass().getName());
			return retryTemplate.execute(context -> {
				retryLogsService.saveRestRetrytoRetryLogs(url.toString(), header.getExternalTransactionId(),
						entity.toString(), header.getUserName());
				return getRestTemplate.exchange(url, HttpMethod.GET, entity, CommonApiStatus.class);
			});
		} catch (Exception e) {
			exceptionLogsService.saveExceptionsToExceptionLogs(e, header.getExternalTransactionId(), null,
					header.getUserName(), url.toString());
		}
		return null;
	}

}
