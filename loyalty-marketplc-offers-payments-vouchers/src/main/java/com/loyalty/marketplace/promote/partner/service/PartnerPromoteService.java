package com.loyalty.marketplace.promote.partner.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.loyalty.marketplace.constants.MarketPlaceCode;
import com.loyalty.marketplace.promote.partner.inbound.dto.AddPartnerPromote;
import com.loyalty.marketplace.promote.partner.outbound.dto.PartnerPromoteDetails;
import com.loyalty.marketplace.promote.partner.outbound.dto.PartnerPromoteResponse;
import com.loyalty.marketplace.promote.partner.outbound.database.entity.PartnerPromoteEntity;
import com.loyalty.marketplace.promote.partner.outbound.database.repository.PartnerPromoteRepository;
import com.loyalty.marketplace.utils.MarketplaceException;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.opencsv.CSVWriter;

@Service
public class PartnerPromoteService {

	@Autowired
	private PartnerPromoteRepository partnerPromoteRepository;

	private static final Logger LOG = LoggerFactory.getLogger(PartnerPromoteService.class);

	public PartnerPromoteResponse insertPatnerPromote(AddPartnerPromote patnerPromote, String program) throws MarketplaceException {
		try {
			PartnerPromoteResponse partnerPromoteResponse = null;
			boolean isAccountNumberAndPatnerCodeExits = isAccountNumberAndPatnerCodeExists(
					patnerPromote.getAccountNumber(), patnerPromote.getPartnerCode());
			if (isAccountNumberAndPatnerCodeExits) {
				PartnerPromoteEntity fetchpartnerPromoteEntity = partnerPromoteRepository
						.findByAccountNumberAndPatnerCode(patnerPromote.getAccountNumber(),
								patnerPromote.getPartnerCode());
				fetchpartnerPromoteEntity.setPatnerCode(patnerPromote.getPartnerCode());
				fetchpartnerPromoteEntity.setProgramCode(program);
				fetchpartnerPromoteEntity.setAccountNumber(patnerPromote.getAccountNumber());
				fetchpartnerPromoteEntity.setContactName(patnerPromote.getContactName());
				fetchpartnerPromoteEntity.setContactNumber(patnerPromote.getContactNumber());
				fetchpartnerPromoteEntity.setContactEmail(patnerPromote.getContactEmail());
				fetchpartnerPromoteEntity.setDob(patnerPromote.getDob());
				fetchpartnerPromoteEntity.setNationality(patnerPromote.getNationality());
				fetchpartnerPromoteEntity.setLanguage(patnerPromote.getLanguage());
				fetchpartnerPromoteEntity.setUpdatedDate(new Date());
				PartnerPromoteEntity upadtedpartnerPromoteDbEntity = partnerPromoteRepository
						.save(fetchpartnerPromoteEntity);
				partnerPromoteResponse = new PartnerPromoteResponse(upadtedpartnerPromoteDbEntity.getAccountNumber(),
						upadtedpartnerPromoteDbEntity.getPatnerCode(), upadtedpartnerPromoteDbEntity.getContactNumber(),
						upadtedpartnerPromoteDbEntity.getContactEmail(), "Record updated succesfully",upadtedpartnerPromoteDbEntity.getDob(),upadtedpartnerPromoteDbEntity.getNationality(),upadtedpartnerPromoteDbEntity.getLanguage());
			} else {
				PartnerPromoteEntity partnerPromoteEntity = new PartnerPromoteEntity();
				partnerPromoteEntity.setPatnerCode(patnerPromote.getPartnerCode());
				partnerPromoteEntity.setProgramCode(program);
				partnerPromoteEntity.setAccountNumber(patnerPromote.getAccountNumber());
				partnerPromoteEntity.setContactName(patnerPromote.getContactName());
				partnerPromoteEntity.setContactNumber(patnerPromote.getContactNumber());
				partnerPromoteEntity.setContactEmail(patnerPromote.getContactEmail());
				partnerPromoteEntity.setDob(patnerPromote.getDob());
				partnerPromoteEntity.setNationality(patnerPromote.getNationality());
				partnerPromoteEntity.setLanguage(patnerPromote.getLanguage());
				partnerPromoteEntity.setCreatedDate(new Date());
				partnerPromoteEntity.setUpdatedDate(new Date());
				PartnerPromoteEntity partnerPromoteDbEntity = partnerPromoteRepository.insert(partnerPromoteEntity);
				partnerPromoteResponse = new PartnerPromoteResponse(partnerPromoteDbEntity.getAccountNumber(),
						partnerPromoteDbEntity.getPatnerCode(), partnerPromoteDbEntity.getContactNumber(),
						partnerPromoteDbEntity.getContactEmail(), "inserted succesfully",partnerPromoteDbEntity.getDob(),partnerPromoteDbEntity.getNationality(),partnerPromoteDbEntity.getLanguage());
			}
			return partnerPromoteResponse;
		} catch (Exception e) {
			throw new MarketplaceException(this.getClass().toString(), "insertPatnerPromote",
					e.getClass() + e.getMessage(), MarketPlaceCode.PARTNER_PROMOTE_INSERTION_FAILED);
		}
	}

	private boolean isAccountNumberAndPatnerCodeExists(String accountNumber, String patnerCode) {
		return partnerPromoteRepository.existsByAccountNumberAndPatnerCode(accountNumber, patnerCode);
	}

	public String getPatnerContactDetails(HttpServletResponse httpServletResponse,String startDate,String endDate, String program)
			throws MarketplaceException, ParseException {
		
		Date dbStartDate = null;
		Date dbEndDate = null;
		if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			dbStartDate = changeStringToDate(startDate.concat(" 00:00:00"), "dd-MM-yyyy HH:mm:ss");
			dbEndDate = changeStringToDate(endDate.concat(" 23:59:59"), "dd-MM-yyyy HH:mm:ss");
		}
		List<PartnerPromoteEntity> PartnerPromoteEntityList = partnerPromoteRepository.findByProgramCodeIgnoreCaseAndCreatedDateBetween(program, dbStartDate, dbEndDate);		
		
		if (!PartnerPromoteEntityList.isEmpty()) {
			try {
				WriteExcelData(PartnerPromoteEntityList, httpServletResponse);
			} catch (Exception e) {
				throw new MarketplaceException(this.getClass().toString(), "getPatnerContactDetails",
						e.getClass() + e.getMessage(), MarketPlaceCode.PARTNER_PROMOTE_REPORT_FAILED);
			}
		} else {
			return "RECORD_NOT_FOUND";
		}
		return "Success";
	}
	
	private Date changeStringToDate(String sdate,String format) throws ParseException
	{
		
		return new SimpleDateFormat(format).parse(sdate);
	}
	
	
	public PartnerPromoteDetails getPartnerContactAccount(String accountNumber, String program) {
		AddPartnerPromote partnerPromote = new AddPartnerPromote();
		PartnerPromoteDetails partnerPromoteResponse = new PartnerPromoteDetails();
		List<PartnerPromoteEntity> PartnerPromoteEntityList = partnerPromoteRepository
				.findByAccountNumberAndProgramCodeIgnoreCase(accountNumber,program);
		if (!PartnerPromoteEntityList.isEmpty()) {
			PartnerPromoteEntity partnerPromoteEntity = PartnerPromoteEntityList.get(0);
			partnerPromote.setAccountNumber(accountNumber);
			partnerPromote.setPartnerCode(partnerPromoteEntity.getPatnerCode());
			partnerPromote.setContactEmail(partnerPromoteEntity.getContactEmail());
			partnerPromote.setContactName(partnerPromoteEntity.getContactName());
			partnerPromote.setContactNumber(partnerPromoteEntity.getContactNumber());
			partnerPromote.setNationality(partnerPromoteEntity.getNationality());
			partnerPromote.setDob(partnerPromoteEntity.getDob());
			partnerPromote.setLanguage(partnerPromoteEntity.getLanguage());
			partnerPromoteResponse = new PartnerPromoteDetails(accountNumber, partnerPromote.getPartnerCode(),partnerPromote.getContactNumber(), partnerPromote.getContactEmail(), partnerPromote.getContactName(),partnerPromote.getDob(),partnerPromote.getNationality(),partnerPromote.getLanguage());
			return partnerPromoteResponse;
		}
		return null;
	}

	private void WriteExcelData(List<PartnerPromoteEntity> partnerPromoteEntityList,
			HttpServletResponse httpServletResponse) throws IOException {
	
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "PartnerContactDetails.csv" + "\"");

		CSVWriter writer = new CSVWriter(httpServletResponse.getWriter());
		String[] HEADERS = {"PatnerCode", "AccountNumber", "ContactName", "ContactNumber", "ContactEmail" , "DOB" , "Nationality" , "Language"};
		List<String[]> fileContentList = new ArrayList<>();
		writer.writeNext(HEADERS);
		for(PartnerPromoteEntity e:partnerPromoteEntityList) {
			String[] data= {e.getPatnerCode(),e.getAccountNumber(),e.getContactName(),e.getContactNumber(),e.getContactEmail(),e.getDob(),e.getNationality(),e.getLanguage()};	
			fileContentList.add(data);
		}
		writer.writeAll(fileContentList);
		writer.close();
	}
	private String formatdate(Date d)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(d);
	}
}
