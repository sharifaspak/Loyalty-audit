package com.loyalty.marketplace.constants;

public enum ImageCodes {
	
	VALIDATOR_DELIMITOR(" "),
	STATUS_SUCCESS(0, "Success"),
	STATUS_FAILURE(1, "Failed"),
	
	//GENERAL CODES
	INVALID_PARAMETER(8100, "Invalid input parameters"),
	GENERIC_RUNTIME_EXCEPTION(8101, "Runtime Exception occured. Please refer logs "),
	IMAGE_FILE_WRITE_EXCEPTION(8102, "Marketplace image file write to server exception. Please refer logs."),
	IMAGE_FILE_READ_EXCEPTION(8103, "Marketplace image file read exception. Please refer logs."),
	
	SERVER_IMAGE_UPLOAD_SUCCESS(8104, "Image uploaded to server successfully."),
	SERVER_IMAGE_UPDATE_SUCCESS(8105, "Image updated on server successfully."),
	SERVER_IMAGE_UPLOAD_FAILED(8106, "Image upload to server failed."),
	FILE_ATTRIBUTE_MANDATORY(8107, "Input file attribute is a mandatory field."),
	PATH_ATTRIBUTE_MANDATORY(8108, "Input path attribute is a mandatory field."),
	DIRECTORY_DOES_NOT_EXIST(8109, "Directory does not exist: "),
	META_DATA_GENERATION_SUCCESS(8109, "Meta data generated successfully."),
	META_DATA_GENERATION_FAILED(8110, "Meta data generation failed."),
	IMAGE_DELETE_SUCCESS(8111, "Image deleted successfully."),
	IMAGE_DELETE_FAILED(8112, "Image delete failed."),
	IMAGE_ALREADY_EXISTS(8113, "Image already exists with the file name: ");
	
	private int id;
	private String msg;
	private String constant;
	
	ImageCodes(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}

	ImageCodes(String constant) {
		this.constant = constant;
	}

	public int getIntId() {
		return this.id;
	}

	public String getId() {
		return Integer.toString(this.id);
	}

	public String getMsg() {
		return this.msg;
	}

	public String getConstant() {
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

}
