package com.loyalty.marketplace.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.loyalty.marketplace.database.entity.ExceptionLogs;
import com.loyalty.marketplace.database.repository.ExceptionLogsRepository;

@RefreshScope
@Service
public class ExceptionLogsService {

	@Autowired
	ExceptionLogsRepository exceptionLogsRepository;
	
	private static final Logger log = LoggerFactory.getLogger(ExceptionLogsService.class);
	
	/** method to store exception to ExceptionLog collection.
	 * @param e
	 * @param transactionId
	 * @param UserName
	 */
	public String saveExceptionsToExceptionLogs(Exception e, String transactionId, String accountNumber, String UserName, String url) {
		log.info("Inside ExceptionLogsService.saveExceptionsToDB()");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw); 
		e.printStackTrace(pw);
		ExceptionLogs exceptionLogs = new ExceptionLogs();
		exceptionLogs.setExceptionDetails(sw.toString());
		exceptionLogs.setAccountNumber(accountNumber);
		exceptionLogs.setTransactionId(transactionId);
		exceptionLogs.setUrl(url);
		Date date = new Date();
		exceptionLogs.setCreatedDate(date);
		exceptionLogs.setCreatedUser(UserName);
		exceptionLogsRepository.save(exceptionLogs);
		log.info("Inside ExceptionLogsService.saveExceptionsToDB() Saved the exception to collection");
		return exceptionLogs.getId();		
	}
	
	public String saveExceptionsToExceptionLogs(Exception e, String transactionId, String accountNumber, String UserName) {
		log.info("Inside ExceptionLogsService.saveExceptionsToDB()");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw); 
		e.printStackTrace(pw);
		ExceptionLogs exceptionLogs = new ExceptionLogs();
		exceptionLogs.setExceptionDetails(sw.toString());
		exceptionLogs.setAccountNumber(accountNumber);
		exceptionLogs.setTransactionId(transactionId);
		Date date = new Date();
		exceptionLogs.setCreatedDate(date);
		exceptionLogs.setCreatedUser(UserName);
		exceptionLogsRepository.save(exceptionLogs);
		log.info("Inside ExceptionLogsService.saveExceptionsToDB() Saved the exception to collection");
		return exceptionLogs.getId();		
	}
}
