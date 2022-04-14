package com.loyalty.marketplace.voucher.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.inbound.dto.VoucherUploadDto;

public class VoucherCsvGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(VoucherCsvGenerator.class);

	public CSVPrinter generateCsv(List<VoucherUploadDto> res, String fileLocation) throws IOException {

		String[] HEADERS = VoucherConstants.HEADERS;
		String fileName = VoucherConstants.HANDBACK_CSV + (new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))
				+ ".csv";

		File newFile = new File(fileLocation + fileName);
		newFile.createNewFile();

		FileWriter out = new FileWriter(newFile);

		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
			res.stream().forEach(e -> {
			
					try {
						printer.printRecord(e.getVoucherCode(), formatDate(e.getUploadDate()), e.getDenomination(),
								e.getMerchantCode(), formatDate(e.getExpiryDate()), e.getOfferId(),
								formatDate(e.getStartDate()), formatDate(e.getEndDate()), e.getSubOfferId(), e.getStatus(),
								e.getError());
					} catch (IOException e1) {
					LOG.error("Exception occured while generating CSV : {} ",e1.getMessage());
					}
				
			});
			return printer;
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
		finally {
			out.close();
		}
	}

	private String formatDate(Date d1) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (d1 == null) {
			return null;
		} else {
			return formatter.format(d1);
		}
	}
}
