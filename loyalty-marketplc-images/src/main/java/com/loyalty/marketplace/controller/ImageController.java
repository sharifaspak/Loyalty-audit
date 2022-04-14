package com.loyalty.marketplace.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.loyalty.marketplace.constants.ImageCodes;
import com.loyalty.marketplace.constants.ImageConfigurationConstants;
import com.loyalty.marketplace.constants.ImageConstants;
import com.loyalty.marketplace.dto.ImageDataResponse;
import com.loyalty.marketplace.dto.ImageDataResult;
import com.loyalty.marketplace.dto.ResultResponse;

@EnableScheduling
@RestController
@RequestMapping(ImageConfigurationConstants.IMAGE_BASE)
public class ImageController {

	private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);
	
	/**
	 * API to upload image to Nginx file system.
	 * @param program
	 * @param authorization
	 * @param externalTransactionId
	 * @param userName
	 * @param sessionId
	 * @param userPrev
	 * @param channelId
	 * @param systemId
	 * @param systemPassword
	 * @param token
	 * @param transactionId
	 * @param path
	 * @param file
	 * @return
	 */
    @PostMapping(value = ImageConfigurationConstants.UPLOAD_IMAGE, consumes = MediaType.ALL_VALUE)
	public ResultResponse imageUpload(
			@RequestHeader(value = ImageConfigurationConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = ImageConfigurationConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = ImageConfigurationConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = ImageConfigurationConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = ImageConfigurationConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = ImageConfigurationConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = ImageConfigurationConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = ImageConfigurationConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = ImageConfigurationConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = ImageConfigurationConstants.TOKEN, required = false) String token,
			@RequestHeader(value = ImageConfigurationConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestParam(value = ImageConfigurationConstants.PATH, required = false) String path,
			@RequestParam(value = ImageConfigurationConstants.ACTION, required = false) String action,
			@RequestParam(value = ImageConfigurationConstants.EXISTING_PATH, required = false) String existingPath,
			@RequestParam(value = ImageConfigurationConstants.FILE, required = false) MultipartFile file) {

    	if(action.equalsIgnoreCase(ImageConstants.ACTION_INSERT)) {
    		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.IMAGE_UPLOAD_API_REQUEST_PARAMS,
    				this.getClass(), ImageConfigurationConstants.CONTROLLER_IMAGE_UPLOAD, path, action);
    	} else {
    		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.IMAGE_UPDATE_API_REQUEST_PARAMS,
    				this.getClass(), ImageConfigurationConstants.CONTROLLER_IMAGE_UPLOAD, path, existingPath, action);
    	}
		
		ResultResponse resultResponse = new ResultResponse(externalTransactionId);
		
		try {

			if(file.isEmpty()) {
				resultResponse.addErrorAPIResponse(ImageCodes.FILE_ATTRIBUTE_MANDATORY.getIntId(),
						ImageCodes.FILE_ATTRIBUTE_MANDATORY.getMsg());
			}
			
			if(null == path) {
				resultResponse.addErrorAPIResponse(ImageCodes.PATH_ATTRIBUTE_MANDATORY.getIntId(),
						ImageCodes.PATH_ATTRIBUTE_MANDATORY.getMsg());
			}
			
			if(!resultResponse.getApiStatus().getErrors().isEmpty()) {
				resultResponse.setResult(ImageCodes.SERVER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.SERVER_IMAGE_UPLOAD_FAILED.getMsg());
				return resultResponse;
			}

			Path imagePath = Paths.get(path);
			if(!imagePath.toFile().isDirectory()) Files.createDirectories(imagePath.getParent());
			
			byte[] bytes = file.getBytes();
			Files.write(imagePath, bytes);

			LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.SAVED_TO_PATH,
					this.getClass(), ImageConfigurationConstants.CONTROLLER_IMAGE_UPLOAD, imagePath);

			if(action.equalsIgnoreCase(ImageConstants.ACTION_INSERT)) {
				resultResponse.setResult(ImageCodes.SERVER_IMAGE_UPLOAD_SUCCESS.getId(),
						ImageCodes.SERVER_IMAGE_UPLOAD_SUCCESS.getMsg());
			} else {
				resultResponse.setResult(ImageCodes.SERVER_IMAGE_UPDATE_SUCCESS.getId(),
						ImageCodes.SERVER_IMAGE_UPDATE_SUCCESS.getMsg());
			}
			
		} catch (IOException e) {
			resultResponse.addErrorAPIResponse(ImageCodes.IMAGE_FILE_WRITE_EXCEPTION.getIntId(), e.getMessage());

			if(action.equalsIgnoreCase(ImageConstants.ACTION_INSERT)) {
				resultResponse.setResult(ImageCodes.SERVER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.SERVER_IMAGE_UPLOAD_FAILED.getMsg());
			} else {
				resultResponse.setResult(ImageCodes.SERVER_IMAGE_UPLOAD_FAILED.getId(),
						ImageCodes.SERVER_IMAGE_UPLOAD_FAILED.getMsg());
			}
			
			LOG.error(e.getMessage());
		}

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.RESPONSE_PARAMS,
				this.getClass(), ImageConfigurationConstants.CONTROLLER_IMAGE_UPLOAD, resultResponse);

		return resultResponse;
		
	}
    
    /**
     * This API is used to generate meta data for the images in the provided path.
     * @param program
     * @param authorization
     * @param externalTransactionId
     * @param userName
     * @param sessionId
     * @param userPrev
     * @param channelId
     * @param systemId
     * @param systemPassword
     * @param token
     * @param transactionId
     * @param path
     * @return
     */
    @PostMapping(value = ImageConfigurationConstants.RETRIEVE_IMAGE_DATA, consumes = MediaType.ALL_VALUE)
	public ImageDataResponse generateMetaData(
			@RequestHeader(value = ImageConfigurationConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = ImageConfigurationConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = ImageConfigurationConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = ImageConfigurationConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = ImageConfigurationConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = ImageConfigurationConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = ImageConfigurationConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = ImageConfigurationConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = ImageConfigurationConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = ImageConfigurationConstants.TOKEN, required = false) String token,
			@RequestHeader(value = ImageConfigurationConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = ImageConfigurationConstants.PATH, required = false) String path) {

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.META_DATA_API_REQUEST_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_GENERATE_META_DATA, path);

		ImageDataResponse resultResponse = new ImageDataResponse(externalTransactionId);

		if (null == path) {
			resultResponse.addErrorAPIResponse(ImageCodes.PATH_ATTRIBUTE_MANDATORY.getIntId(),
					ImageCodes.PATH_ATTRIBUTE_MANDATORY.getMsg());
			resultResponse.setResult(ImageCodes.META_DATA_GENERATION_FAILED.getId(),
					ImageCodes.META_DATA_GENERATION_FAILED.getMsg());
			return resultResponse;
		}

		File directoryPath = new File(path);
		String regularExpression = Pattern.quote(ImageConstants.PERIOD);
		String[] filesList = directoryPath.list();

		List<String> listOfFileNames = Arrays.asList(filesList);
		List<ImageDataResult> listImageData = new ArrayList<>();

		for (String file : filesList) {

			if (null == file)
				continue;
			if (!new File(path + ImageConstants.FORWARD_SLASH + file).exists())
				continue;
			if (new File(path + ImageConstants.FORWARD_SLASH + file).isDirectory())
				continue;
			if (!ImageConstants.ALOWED_IMG_FORMATS.contains(FilenameUtils.getExtension(file)))
				continue;

			ImageDataResult imageDataResult = new ImageDataResult();

			String filePath = path + ImageConstants.FORWARD_SLASH + file;
			Path imagePath = Paths.get(filePath);

			BufferedImage bufferedImage = null;
			byte[] fileArray = null;
			String domainId = file.split(regularExpression)[0];

			try {
				fileArray = Files.readAllBytes(imagePath);
				InputStream in = new ByteArrayInputStream(fileArray);
				bufferedImage = ImageIO.read(in);
			} catch (IOException e) {
				LOG.error(e.getMessage());
				continue;
			}

			if (null != bufferedImage) {
				int width = bufferedImage.getWidth();
				int height = bufferedImage.getHeight();
				imageDataResult.setHeight(height);
				imageDataResult.setWidth(width);
			}

			if (null != fileArray) {
				imageDataResult.setByteArray(fileArray);
				imageDataResult.setImageSize(fileArray.length);
			}

			imageDataResult.setDomainId(domainId);
			imageDataResult.setImageName(file);
			imageDataResult.setPath(filePath);

			listImageData.add(imageDataResult);

		}

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.META_DATA_LIST_FILES,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_GENERATE_META_DATA,
				listOfFileNames.size());

		resultResponse.setImagesList(listOfFileNames);
		resultResponse.setTotalFileCount(listOfFileNames.size());
		resultResponse.setDataGeneratedCount(listImageData.size());
		resultResponse.setImageData(listImageData);
		resultResponse.setResult(ImageCodes.META_DATA_GENERATION_SUCCESS.getId(),
				ImageCodes.META_DATA_GENERATION_SUCCESS.getMsg());

		return resultResponse;

	}
    
    /**
     * This API is used to delete image - used for error handling in offers-payments-voucher microservice.
     * @param program
     * @param authorization
     * @param externalTransactionId
     * @param userName
     * @param sessionId
     * @param userPrev
     * @param channelId
     * @param systemId
     * @param systemPassword
     * @param token
     * @param transactionId
     * @param path
     * @return
     */
    @PostMapping(value = ImageConfigurationConstants.DELETE_IMAGE, consumes = MediaType.ALL_VALUE)
	public ResultResponse deleteImage(
			@RequestHeader(value = ImageConfigurationConstants.PROGRAM, required = false) String program,
			@RequestHeader(value = ImageConfigurationConstants.AUTHORIZATION, required = false) String authorization,
			@RequestHeader(value = ImageConfigurationConstants.EXTERNAL_TRANSACTION_ID, required = false) String externalTransactionId,
			@RequestHeader(value = ImageConfigurationConstants.USER_NAME, required = false) String userName,
			@RequestHeader(value = ImageConfigurationConstants.SESSION_ID, required = false) String sessionId,
			@RequestHeader(value = ImageConfigurationConstants.USER_PREV, required = false) String userPrev,
			@RequestHeader(value = ImageConfigurationConstants.CHANNEL_ID, required = false) String channelId,
			@RequestHeader(value = ImageConfigurationConstants.SYSTEM_ID, required = false) String systemId,
			@RequestHeader(value = ImageConfigurationConstants.SYSTEM_CREDENTIAL, required = false) String systemPassword,
			@RequestHeader(value = ImageConfigurationConstants.TOKEN, required = false) String token,
			@RequestHeader(value = ImageConfigurationConstants.TRANSACTION_ID, required = false) String transactionId,
			@RequestHeader(value = ImageConfigurationConstants.PATH, required = false) String path) {

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.DELETE_IMAGE_REQUEST_PARAMS,
				this.getClass().getSimpleName(), ImageConfigurationConstants.CONTROLLER_DELETE_IMAGE, path);

		ResultResponse resultResponse = new ResultResponse(externalTransactionId);

		if (null == path) {
			resultResponse.addErrorAPIResponse(ImageCodes.PATH_ATTRIBUTE_MANDATORY.getIntId(),
					ImageCodes.PATH_ATTRIBUTE_MANDATORY.getMsg());
			resultResponse.setResult(ImageCodes.IMAGE_DELETE_FAILED.getId(),
					ImageCodes.IMAGE_DELETE_FAILED.getMsg());
			return resultResponse;
		}

		try {
			
			Files.deleteIfExists(Paths.get(path));
			
			resultResponse.setResult(ImageCodes.IMAGE_DELETE_SUCCESS.getId(),
					ImageCodes.IMAGE_DELETE_SUCCESS.getMsg());
		
		} catch (IOException e) {
			
			resultResponse.addErrorAPIResponse(ImageCodes.IMAGE_FILE_READ_EXCEPTION.getIntId(), e.getMessage());
			resultResponse.setResult(ImageCodes.IMAGE_DELETE_FAILED.getId(),
					ImageCodes.IMAGE_DELETE_FAILED.getMsg());
			LOG.error(e.getMessage());
			
		}

		LOG.info(ImageConstants.CLASS_NAME + ImageConstants.METHOD_NAME + ImageConstants.RESPONSE_PARAMS,
				this.getClass(), ImageConfigurationConstants.CONTROLLER_DELETE_IMAGE, resultResponse);
		
		return resultResponse;

	}
    
    /**
     * A scheduled method to trigger a LOG statement at the start of the day (12AM)
     * for creating a new LOG file for LOG rotation.
     */
    @Scheduled(cron = "${schedule.logs.start}")
    public void startLogs() {
    	
    	Calendar todayDate = Calendar.getInstance();
        todayDate.setTime(new Date());
    	SimpleDateFormat formatter = new SimpleDateFormat(ImageConstants.LOG_DATE_FORMAT);
    	String currentDate = formatter.format(todayDate.getTime());
    	
    	LOG.info(ImageConstants.SCHEDULED_LOG_START, currentDate);
    
    }
    
    /**
     * A scheduled method to trigger a LOG statement at the end of the day (11:59PM).
     */
    @Scheduled(cron = "${schedule.logs.end}")
    public void endLogs() {
    	
    	Calendar todayDate = Calendar.getInstance();
        todayDate.setTime(new Date());
    	SimpleDateFormat formatter = new SimpleDateFormat(ImageConstants.LOG_DATE_FORMAT);
    	String currentDate = formatter.format(todayDate.getTime());
    	
    	LOG.info(ImageConstants.SCHEDULED_LOG_END, currentDate);
    
    }
    
    /**
     * A helper API to check nginx server connectivity.
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "Welcome to Nginx Server! PWD: " + Paths.get("").toAbsolutePath().toString();
    }

}