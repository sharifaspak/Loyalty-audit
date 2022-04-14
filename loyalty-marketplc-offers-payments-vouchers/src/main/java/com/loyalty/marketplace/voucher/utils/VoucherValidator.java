package com.loyalty.marketplace.voucher.utils;

import com.loyalty.marketplace.outbound.dto.ResultResponse;
import com.loyalty.marketplace.voucher.constants.VoucherCode;
import com.loyalty.marketplace.voucher.constants.VoucherConstants;
import com.loyalty.marketplace.voucher.constants.VoucherManagementCode;
import com.loyalty.marketplace.voucher.outbound.dto.Errors;
import com.loyalty.marketplace.voucher.outbound.dto.VoucherResultResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class VoucherValidator {
	
	private VoucherValidator() {}
	
	
	public static boolean validateDto(Object marketPlaceDto, Validator validator,
			ResultResponse resultResponse) {
		Set<ConstraintViolation<Object>> violations = validator.validate(marketPlaceDto);
		if (violations.isEmpty()) {
			return true;	
		} else {
			for (ConstraintViolation<Object> violation : violations) {
				if (violation.getMessage() != null) {
					resultResponse.addErrorAPIResponse(
							Integer.parseInt(
									(violation.getMessage().split(VoucherCode.VALIDATOR_DELIMITOR.getConstant(), 2)[0])),
							(violation.getMessage().split(VoucherCode.VALIDATOR_DELIMITOR.getConstant(), 2)[1]));
				} else {
					resultResponse.addErrorAPIResponse(VoucherCode.INVALID_PARAMETER.getIntId(),
							VoucherCode.INVALID_PARAMETER.getMsg());
				}
			}

			return false;
		}
	}


	public static boolean validateVoucherDto(Object voucherDto, Validator validator,
			VoucherResultResponse resultResponse) {
		Set<ConstraintViolation<Object>> violations = validator.validate(voucherDto);
		if (violations.isEmpty()) {
			return true;	
		} else {
			for (ConstraintViolation<Object> violation : violations) {
				if (violation.getMessage() != null) {
					resultResponse.addErrorAPIResponse(
							Integer.parseInt(
									(violation.getMessage().split(VoucherCode.VALIDATOR_DELIMITOR.getConstant(), 2)[0])),
							(violation.getMessage().split(VoucherCode.VALIDATOR_DELIMITOR.getConstant(), 2)[1]));
				} else {
					resultResponse.addErrorAPIResponse(VoucherCode.INVALID_PARAMETER.getIntId(),
							VoucherCode.INVALID_PARAMETER.getMsg());
				}
			}

			return false;
		}
	}	
	
	public static Object validateEntity(Object object,Validator validator) throws Exception{
        Set<ConstraintViolation<Object>> violations =  validator.validate(object);
        if(violations.isEmpty()) { 
        return object;
        }else {
               List<Errors> errorList=new ArrayList<>();
               
               for (ConstraintViolation<Object> violation : violations) {
                      Errors error=new Errors();
                      if (violation.getMessage()!=null && !((violation.getMessage().split(VoucherConstants.VALIDATOR_DELIMITOR)).length>1)){
                            error.setCode(Integer.parseInt(
                                       (violation.getMessage().split(VoucherConstants.VALIDATOR_DELIMITOR, 2)[0])));
                      error.setMessage((violation.getMessage().split(VoucherConstants.VALIDATOR_DELIMITOR,2)[1]));
                 errorList.add(error);   
                      }else {
                            error.setCode(VoucherManagementCode.INVALID_PARAMETERS.getIntId());
                        error.setMessage(violation.getPropertyPath() + " " +violation.getMessage());
                        errorList.add(error);
                      }
               }
               if (!errorList.isEmpty()) {
                      throw new Exception(errorList.get(0).getMessage()+" "+object.toString());
               }
        
        return object;
        }
  }      


}
