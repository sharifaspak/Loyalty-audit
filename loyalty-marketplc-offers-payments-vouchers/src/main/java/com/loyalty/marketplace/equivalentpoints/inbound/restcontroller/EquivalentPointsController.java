package com.loyalty.marketplace.equivalentpoints.inbound.restcontroller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.marketplace.constants.RequestMappingConstants;
import com.loyalty.marketplace.equivalentpoints.constant.ApplicationConstants;
import com.loyalty.marketplace.equivalentpoints.domain.EquivalentPointsDomain;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.ConversionRateDto;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.EquivalentPointsDto;
import com.loyalty.marketplace.equivalentpoints.inbound.dto.event.HeaderDto;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.CommonApiStatus;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.ListRedemptionRate;
import com.loyalty.marketplace.equivalentpoints.outbound.dto.RedemptionRate;
import com.loyalty.marketplace.equivalentpoints.utils.AbstractController;
import com.loyalty.marketplace.equivalentpoints.utils.EquivalentPointsException;
import com.loyalty.marketplace.equivalentpoints.utils.ErrorCodes;
import com.loyalty.marketplace.equivalentpoints.utils.ErrorMessages;
import com.loyalty.marketplace.offers.constants.OffersRequestMappingConstants;
import com.loyalty.marketplace.offers.helper.dto.Headers;
import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.opencsv.CSVWriter;

import io.swagger.annotations.Api;

@RestController
@Api(value = OffersRequestMappingConstants.MARKETPLACE)
@RequestMapping(OffersRequestMappingConstants.MARKETPLACE_BASE)
public class EquivalentPointsController extends ErrorMessages implements AbstractController {

	private static final Logger log = LoggerFactory.getLogger(EquivalentPointsController.class);

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private EquivalentPointsDomain equivalentPointsDomain;

	@Autowired
	Validator validator;
	
	public EquivalentPointsController(EquivalentPointsDomain equivalentPointsDomain, Validator validator) {
		super();
		this.equivalentPointsDomain = equivalentPointsDomain;
		this.validator = validator;
	}

	@PostMapping(value = "/conversionRate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonApiStatus> insertConversionRate(@RequestBody ConversionRateDto conversionRateDto,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program) {
		ResponseEntity<CommonApiStatus> apiStatus = null;
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			equivalentPointsDomain.insertConversionRate(conversionRateDto, userName, program, token,
					externalTransactionId);
			apiStatus = getSuccessStatus(externalTransactionId, conversionRateCreated, conversionRateInserted);
			log.info("API Status : {}", apiStatus);
		} catch (EquivalentPointsException e) {
			log.error("inside insertConversionRate method of class {} got Exception={} ", this.getClass().getName(), e);
			apiStatus = getErrorStatus(externalTransactionId, e.getErrorMsg(), e.getErrorCode(), null);
		} catch (Exception e) {
			log.error("inside insertConversionRate method of class {} got Exception={} ", this.getClass().getName(), e);
			apiStatus = getErrorStatus(externalTransactionId, e.getMessage(),
					ErrorCodes.GENERIC_RUNTIME_EXCEPTION.getCode(), null);
		}
		return apiStatus;
	}

	@GetMapping(value = "/equivalentPoints")
	public ResponseEntity<CommonApiStatus> getEquivalentPoints(
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program) {
		ResponseEntity<CommonApiStatus> apiStatus = null;
		List<ListRedemptionRate> result = new ArrayList<>();
		ListRedemptionRate listRedemptionRate;
		try {
			//Changes for loyalty as a service.
			if (null == program) program = defaultProgramCode;
			
			listRedemptionRate = equivalentPointsDomain.getAllEquivalentPoints();
			if (listRedemptionRate != null) {
				result.add(listRedemptionRate);
				apiStatus = getSuccessStatus(externalTransactionId, calculatedEquivalentPoints, result);
			}
		} catch (EquivalentPointsException me) {
			log.error("inside getAllConversionRate method of class {} got Exception={} ", this.getClass().getName(),
					me);
			apiStatus = getErrorStatus(externalTransactionId, me.getErrorMsg(), me.getErrorCode(), null);
		} catch (Exception e) {
			log.error("inside getAllConversionRate method of class {} got Exception={} ", this.getClass().getName(), e);
			apiStatus = getErrorStatus(externalTransactionId, e.getMessage(),
					ErrorCodes.GENERIC_RUNTIME_EXCEPTION.getCode(), null);
		}

		return apiStatus;
	}

	@PostMapping(value = "/equivalentPoints", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonApiStatus> equivalentPoints(@RequestBody EquivalentPointsDto equivalentPointsDto,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program) {
		ResponseEntity<CommonApiStatus> apiStatus = null;
		CommonApiStatus commonApiStatus = new CommonApiStatus(externalTransactionId);
		
		//Changes for loyalty as a service.
		if (null == program) program = defaultProgramCode;
		
		HeaderDto headerDto = new HeaderDto(program, channelId, userName, "role", "Smiles", externalTransactionId,
				token);
		List<ListRedemptionRate> result = new ArrayList<>();
		if (equivalentPointsDto != null && validateDto(equivalentPointsDto, validator, commonApiStatus)) {
			try {
				if (equivalentPointsDto.getAmount() == null && equivalentPointsDto.getPoint() == null) {
					ListRedemptionRate listRedemptionRate = equivalentPointsDomain
							.getConversionRates(equivalentPointsDto, userName, token);
					if (listRedemptionRate != null) {
						result.add(listRedemptionRate);
						apiStatus = getSuccessStatus(externalTransactionId, calculatedEquivalentPoints, result);
					}
				} else {
					apiStatus = equivalentPointsAndAmount(equivalentPointsDto, userName, token, externalTransactionId,
							apiStatus, headerDto, result);
				}
				log.info("API Status: {}", apiStatus);
			} catch (EquivalentPointsException e) {
				log.error("inside equivalentPoints method of class {} got Exception={} ", this.getClass().getName(), e);
				apiStatus = getErrorStatus(externalTransactionId, e.getErrorMsg(), e.getErrorCode(), null);
			} catch (Exception e) {
				log.error("inside equivalentPoints method of class {} got Exception={} ", this.getClass().getName(), e);
				apiStatus = getErrorStatus(externalTransactionId, e.getMessage(),
						ErrorCodes.GENERIC_RUNTIME_EXCEPTION.getCode(), null);
			}
		} else {
			log.info(invalidInput);
			commonApiStatus.setResult(null);
			apiStatus = ResponseEntity.ok(commonApiStatus);
		}
		return apiStatus;
	}

	/**
	 * @param equivalentPointsDto
	 * @param userName
	 * @param token
	 * @param externalTransactionId
	 * @param apiStatus
	 * @param headerDto
	 * @param result
	 * @return
	 * @throws EquivalentPointsException
	 */
	private ResponseEntity<CommonApiStatus> equivalentPointsAndAmount(EquivalentPointsDto equivalentPointsDto,
			String userName, String token, String externalTransactionId, ResponseEntity<CommonApiStatus> apiStatus,
			HeaderDto headerDto, List<ListRedemptionRate> result) throws EquivalentPointsException {
		if (equivalentPointsDto.getAmount() != null) {
			apiStatus = equivalentAmount(equivalentPointsDto, userName, token, externalTransactionId, apiStatus,
					headerDto, result);
		} else {
			apiStatus = equivalentPoints(equivalentPointsDto, externalTransactionId, apiStatus, result);
		}
		return apiStatus;
	}

	/**
	 * @param equivalentPointsDto
	 * @param externalTransactionId
	 * @param apiStatus
	 * @param result
	 * @return
	 * @throws EquivalentPointsException
	 */
	private ResponseEntity<CommonApiStatus> equivalentPoints(EquivalentPointsDto equivalentPointsDto,
			String externalTransactionId, ResponseEntity<CommonApiStatus> apiStatus, List<ListRedemptionRate> result)
			throws EquivalentPointsException {
		List<RedemptionRate> redemptionRateList = equivalentPointsDomain
				.calculateEquivalentAmount(equivalentPointsDto);
		if (redemptionRateList != null) {
			for (RedemptionRate r : redemptionRateList) {
				RedemptionRate redemptionRate = new RedemptionRate(r.getEquivalentPoint(), r.getRate());
				if (equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_EARNING)) {
					result.add(new ListRedemptionRate(null, redemptionRate, null, null));
				} else {
					result.add(new ListRedemptionRate(redemptionRate, null, null, null));
				}
				apiStatus = getSuccessStatus(externalTransactionId, calculatedEquivalentPoints, result);
			}
		}
		return apiStatus;
	}

	/**
	 * @param equivalentPointsDto
	 * @param userName
	 * @param token
	 * @param externalTransactionId
	 * @param apiStatus
	 * @param headerDto
	 * @param result
	 * @return
	 * @throws EquivalentPointsException
	 */
	private ResponseEntity<CommonApiStatus> equivalentAmount(EquivalentPointsDto equivalentPointsDto, String userName,
			String token, String externalTransactionId, ResponseEntity<CommonApiStatus> apiStatus, HeaderDto headerDto,
			List<ListRedemptionRate> result) throws EquivalentPointsException {
		List<RedemptionRate> redemptionRateList = equivalentPointsDomain
				.calculateEquivalentPoints(equivalentPointsDto, headerDto, userName, token);
		if (redemptionRateList != null) {
			for (RedemptionRate r : redemptionRateList) {
				RedemptionRate v = new RedemptionRate(r.getEquivalentPoint(), r.getRate());
				if (equivalentPointsDto.getOperationType()
						.equalsIgnoreCase(ApplicationConstants.ACTIVITY_TYPE_EARNING)) {
					result.add(new ListRedemptionRate(null, v, null, null));
				} else {
					result.add(new ListRedemptionRate(v, null, null, null));
				}
				apiStatus = getSuccessStatus(externalTransactionId, calculatedEquivalentPoints, result);
			}
		}
		return apiStatus;
	}
	
	@GetMapping(value = "/purchaseAmountUpdate")
	public ResponseEntity<CommonApiStatus> purchaseAmountUpdate(@RequestParam("file") MultipartFile file,
			@RequestHeader(value = RequestMappingConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = RequestMappingConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = RequestMappingConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = RequestMappingConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = RequestMappingConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = RequestMappingConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = RequestMappingConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = RequestMappingConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = RequestMappingConstants.TOKEN, required = false) String token,
			@RequestHeader(value = RequestMappingConstants.TRANSACTION_ID, required = false) String transactionId,
			HttpServletResponse httpServletResponse) throws IOException {
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		ResponseEntity<CommonApiStatus> apiStatus = null;
		Headers headers = new Headers(program, authorization, externalTransactionId, userName, sessionId, userPrev,
				channelId, systemId, systemPassword, token, transactionId);
		try {
		if (file != null && !file.isEmpty() && file.getContentType() != null) 
		{
			log.info("================Started purchaseAmountUpdate=============");
			readCSVfileandProcess(file,httpServletResponse,headers);
			apiStatus = getSuccessStatus(externalTransactionId, "Success", "Successfully generated");
		}
		}
		catch (Exception e) {
			log.error("inside equivalentPoints method of class {} got Exception={} ", this.getClass().getName(), e);
			apiStatus = getErrorStatus(externalTransactionId, e.getMessage(),
					ErrorCodes.GENERIC_RUNTIME_EXCEPTION.getCode(), null);
		}
		log.info("================End purchaseAmountUpdate=============");
		return apiStatus;
		
	
	}

	@SuppressWarnings("unchecked")
	public void readCSVfileandProcess(MultipartFile file, HttpServletResponse httpServletResponse, Headers headers) throws Exception
	{
		
		List<String[]> rows = new ArrayList<>();
		String[] header = { "_id", "AccountNumber", "PurchaseItem", "PointsTransactionId",
				"PaymentMethod", "SpentPoints","SpentAmount", "PurchaseAmount","CreatedDate","UpdatedPurchaseAmount"};
		rows.add(header);
		log.info("========== Inside readCSVfileandProcess method =========");
			BufferedReader br;
			List<String> fileContent = new ArrayList<>();
			String line;
			
			
			InputStream is = file.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				if (!StringUtils.isEmpty(line)) {
					fileContent.add(line);
				}
			}
			for (int i = 1; i < fileContent.size(); i++) {
				Double rate=0.0;
				String[] data;
				data = fileContent.get(i).split(",");
				List<String> asList = Arrays.asList(data);
				
				EquivalentPointsDto equivalentPointsDto = getEquivalentPointsDto(data);
				log.info("=========Calling equivalentPoint===========   {} "+equivalentPointsDto);
				ResponseEntity<CommonApiStatus> equivalentPointsResponse = equivalentPoints(equivalentPointsDto,headers.getUserName(),headers.getSessionId(),headers.getUserPrev(),headers.getChannelId(),
						headers.getSystemId(),headers.getSystemPassword(),headers.getToken(),headers.getExternalTransactionId(),headers.getProgram());
					
						
				log.info("response from  equivalentPoint.. {}"+equivalentPointsResponse);
				
				if(equivalentPointsResponse.getBody()!= null && equivalentPointsResponse.getBody().getApiStatus()!= null
						)
				{
					
					
					if(equivalentPointsResponse.getBody().getApiStatus().getStatusCode()==0)
					{
						Object result = equivalentPointsResponse.getBody().getResult();
						List<ListRedemptionRate> list=(List<ListRedemptionRate>)convertValue( equivalentPointsResponse.getBody().getResult(),
								new TypeReference<List<ListRedemptionRate>>() {
								});
						 rate = list.get(0).getRedemptionCalculatedValue().getEquivalentPoint();
					}
				}
				
				log.info("rate:  {}"+rate);	
			
			
			
			List<String> row = new ArrayList<>();
			row.add(data[0]);
			row.add(data[1]);
			row.add(data[2]);
			row.add(data[3]);
			row.add(data[4]);
			row.add(data[5]);
			row.add(data[6]);
			row.add(data[7]);
			row.add(data[8]);
			Double d=0.0;
			if(rate!=0.0)
			 d= (rate*-1);
			row.add(d.toString());
			rows.add(row.toArray(new String[header.length]));
			
		}
			long date = Instant.now().toEpochMilli();
			String filename = "PurchaseAmount_" + date;
			 String systemReportFileLocation="/loyalty_revamp_shared/UAT/reports/systemBalance/";
			String fileLocation = systemReportFileLocation + filename + ".csv";

			// long millis = Instant.now().toEpochMilli();
			// writeCSV(rows, "BillingFile_" + millis + ".csv", response);
			writeCSVtoLocation(rows, fileLocation);
			//writeCSV(rows,"PurchaseAmount"+Instant.now().toEpochMilli()+".csv",httpServletResponse)	;
	}
	
	
	private void writeCSVtoLocation(List<String[]> rows, String excelFilePath) throws IOException {
		log.info("File Location: " + excelFilePath);
		File file = new File(excelFilePath);

		FileWriter outputfile = new FileWriter(file);

		CSVWriter writer = new CSVWriter(outputfile);

		writer.writeAll(rows);

		log.info("Completed writing to csv file....");
		writer.close();
	}
	
	private void writeCSV(List<String[]> rows, String excelFilePath, HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		log.info("Started writing to csv file....");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + excelFilePath + "\"");
		CSVWriter writer;
		writer = new CSVWriter(response.getWriter(), ',', CSVWriter.NO_QUOTE_CHARACTER,
				CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
		for (String[] row : rows) {
			writer.writeNext(row);
		}
		log.info("Completed writing to csv file....");
		writer.close();
	}
	
	public Object convertValue(Object result, TypeReference<?> typeReference) {
		log.info("inside convertToObject method of class ObjectMapperUtil");
		try {
			return mapper.convertValue(result, typeReference);
		} catch (Exception ex) {
			log.error("inside convertToObject method ObjectMapperUtil  got exception", ex);

		}
		return null;
	}

	private EquivalentPointsDto getEquivalentPointsDto(String[] data) {
		EquivalentPointsDto equivalentPointsDto=new EquivalentPointsDto();
		
		equivalentPointsDto.setOperationType("redeeming");
		equivalentPointsDto.setOfferType("billRecharge");
		equivalentPointsDto.setPartnerCode("ES");
		equivalentPointsDto.setPoint(Double.parseDouble(data[5]));
		equivalentPointsDto.setChannel("SAPP");
		return equivalentPointsDto;
	}
	
}
