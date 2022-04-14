package com.loyalty.marketplace.constants;

public class ImageConfigurationConstants {

	private ImageConfigurationConstants() {}

	//HEADERS
	public static final String PROGRAM = "program";
	public static final String AUTHORIZATION = "Authorization";
	public static final String EXTERNAL_TRANSACTION_ID = "externalTransactionId";
	public static final String USER_NAME = "userName";
	public static final String SESSION_ID = "sessionId";
	public static final String USER_PREV = "userPrev";
	public static final String CHANNEL_ID = "channelId";
	public static final String SYSTEM_ID = "systemId";
	public static final String SYSTEM_CREDENTIAL = "systemPassword";
	public static final String TOKEN = "token";
	public static final String TRANSACTION_ID = "transactionId";
	public static final String FILE = "file";
	public static final String PATH = "path";
	public static final String ACTION = "action";
	public static final String EXISTING_PATH = "existingPath";
	
	//ENDPOINTS
	public static final String IMAGE_BASE = "/image";
	public static final String UPLOAD_IMAGE = "/upload";
	public static final String RETRIEVE_IMAGE_DATA = "/data";
	public static final String DELETE_IMAGE = "/delete";

	//METHODS
	public static final String CONTROLLER_IMAGE_UPLOAD = "imageUpload";
	public static final String CONFIG_CREATE_DIRECTORIES = "createImagesDirectories";
	public static final String CONTROLLER_GENERATE_META_DATA = "generateMetaData";
	public static final String CONTROLLER_DELETE_IMAGE = "deleteImage";
	
}
