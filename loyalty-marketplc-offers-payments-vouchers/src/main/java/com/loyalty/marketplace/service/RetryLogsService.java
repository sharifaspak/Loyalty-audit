package com.loyalty.marketplace.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loyalty.marketplace.outbound.database.entity.RetryLogs;
import com.loyalty.marketplace.outbound.database.repository.RetryLogsRepository;

@Component
public class RetryLogsService {

	@Autowired
	RetryLogsRepository retryLogsRepository;
	
	private static final Logger log = LoggerFactory.getLogger(RetryLogsService.class);
	
	/** method to store exception to ExceptionLog collection.
	 * @param e
	 * @param transactionId
	 * @param UserName
	 */
	public void saveRestRetrytoRetryLogs(String url, String transactionId, String request, String userName) {
		log.info("Inside RetryLogsService.saveRestRetrytoRetryLogs()");
		RetryLogs retryLogs = new RetryLogs();
		retryLogs.setRequest(request);
		retryLogs.setTransactionId(transactionId);
		retryLogs.setUrl(url);
		Date date = new Date();
		retryLogs.setCreatedDate(date);
		retryLogs.setCreatedUser(userName);
		retryLogsRepository.save(retryLogs);
		log.info("Inside RetryLogsService.saveRestRetrytoRetryLogs() Saved the retry logs to collection");
				
	}
}
