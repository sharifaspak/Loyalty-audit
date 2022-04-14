package com.loyalty.marketplace.outbound.service.dto;

import java.util.ArrayList;

public class UserManagementResponse {
		 ApiStatus ApiStatusObject;
		 Result ResultObject;


		 // Getter Methods 

		 @Override
		public String toString() {
			return "UserManagementResponse [ApiStatusObject=" + ApiStatusObject + ", ResultObject=" + ResultObject
					+ "]";
		}

		public ApiStatus getApiStatus() {
		  return ApiStatusObject;
		 }

		 public Result getResult() {
		  return ResultObject;
		 }

		 // Setter Methods 

		 public void setApiStatus(ApiStatus apiStatusObject) {
		  this.ApiStatusObject = apiStatusObject;
		 }

		 public void setResult(Result resultObject) {
		  this.ResultObject = resultObject;
		 }
		}
		

