package com.loyalty.marketplace.offers.constants;

import com.loyalty.marketplace.constants.IMarketPlaceCode;

/**
 * 
 * @author jaya.shukla
 *
 */
public enum OfferErrorCodes implements IMarketPlaceCode{
	
	// API Status Codes
	STATUS_FAILURE(1, "Failed"),
		
	// Invalid parameters Codes
	INVALID_PARAMETERS(23500, "Invalid input parameters."),
	
	// Offer Creation
	OFFER_CREATION_FAILED(23501, "Failed to create Offer"),
	OFFER_EXISTS(23502, "Offer already exists"),
	INVALID_OFFER_CODE(23503,"Invalid offer code. Offer code can contain only 20 or less characters, with no whitespaces and special characters."),
	INVALID_OFFER_LABEL_EN(23504,"Offer label english must not be empty"),
	INVALID_OFFER_LABEL_AR(23505,"Offer label arabic must not be empty"),
	INVALID_OFFER_STARTDATE(23506,"Invalid offer start date. Should be in yyyy-MM-dd HH:mm:ss format"),
	INVALID_OFFER_ENDDATE(23507,"Invalid offer end date. Should be in yyyy-MM-dd HH:mm:ss format"),
	INVALID_VOUCHER_EXPIRYDATE(23508,"Invalid voucher expiry date. Should be in yyyy-MM-dd HH:mm:ss format"),
	INVALID_DYNAMIC_DENOMINATION(23509,"Invalid dynamic denomination"),
	INVALID_MERCHANT_CODE(23510,"This merchant is not available"),
    INVALID_STORE_CODE(23511,"This store is not available under this merchant"),
    INVALID_PROVISIONING_CHANNEL(23512, "Provisioning channel is mandatory for creating etisalat offer"),
	INVALID_RATE_PLAN_CODE(23513, "Rate plan Code is mandatory for COMS provisioning channel"),
	INVALID_PRODUCT_TYPE(23514, "Product type is mandatory for RTF provisioning channel"),
	INVALID_PRODUCT_CODE(23515, "Product code is mandatory for RTF provisioning channel"),
	INVALID_VASCODE(23516, "VAS code is mandatory for EMCXAIS provisioning channel"),
	INVALID_VASACTION(23517, "VAS action id is mandatory for EMCXAIS provisioning channel"),
	INVALID_PROMOTIONAL_PERIOD(23769, "Promotional period is mandatory for PHONEYTUNES provisioning channel"),
	INVALID_FEATURE(23770, "Feature is mandatory for RBT provisioning channel"),
	INVALID_SERVICE_ID(23771, "Service Id is mandatory for RBT provisioning channel"),
	INVALID_ACTIVITY_ID(23772, "Activity Id is mandatory for RBT provisioning channel"),
	INVALID_PACK_NAME(23773, "Pack name is mandatory for RBT provisioning channel"),
	INVALID_UPDATE_ACTION(23518, "Invalid action. Action can be update, activate or deactivate."),
	INVALID_ACTION(23519, "Action cannot be blank or null"),
	BLANK_MIN_DYNAMIC_DENOMINATION(23520,"The min denomination must be set as dynamic denomination is set"),
    BLANK_MAX_DYNAMIC_DENOMINATION(23521,"The max denomination must be set as dynamic denomination is set"),
    MIN_GREATER_THAN_MAX(23522,"The max denomination must be greater than min denomination"),
    BLANK_INCREMENTAL_VALUE(23524,"The incremental value cannot be blank as dynamic denomination is set"),
    INVALID_INCREMENTAL_VALUE(23525,"The incremental value must be set greater than 0"),
    INVALID_MIN_DYNAMIC_DENOMINATION(23526,"The min denomination must be greater than 0"),
    INVALID_MAX_DYNAMIC_DENOMINATION(23527,"The max denomination must be greater than 0"),
    STORE_VALUE_BLANK(23529,"Store code value is blank"),
    START_DATE_GREATER_THAN_END_DATE(23530,"Offer start date is later than offer end date"),
    START_DATE_GREATER_THAN_EXPIRY_DATE(23531,"Offer start date is later than voucher expiry date"),
    START_DATE_EQUAL_TO_END_DATE(23532,"Offer start date is same as offer end date"),
    START_DATE_EQUAL_TO_EXPIRY_DATE(23533,"Offer start date is equal to voucher expiry date"),
    START_DATE_EARLIER_THAN_CURRENT_DATE(23534,"Offer start date is earlier than today"),
    END_DATE_EARLIER_THAN_CURRENT_DATE(23535,"Offer end date is earlier than today"),
    EXPIRY_DATE_EARLIER_THAN_CURRENT_DATE(23536,"Voucher expiry date is earlier than today"),
    START_DATE_SAME_AS_CURRENT_DATE(23537,"Offer start date is same as today"),
    END_DATE_SAME_AS_CURRENT_DATE(23538,"Offer end date is same as today"),
    EXPIRY_DATE_SAME_AS_CURRENT_DATE(23539,"Voucher expiry date is same as today"),
    INVALID_COST_VALUE(23540, "Cost must more  must not be less than  0"),
    INVALID_ESTIMATED_SAVINGS_VALUE(23541, "Estimaed savings must more than 0"),
    INVALID_EARN_MULTIPLIER_VALUE(23542, "Earn multiplier must not be less than  0"),
    INVALID_VAT_PERCENTAGE_VALUE(23543, "Vat percentage  must not be less than  0"),
    INVALID_SUBOFFER_NEW_COST(23544, "New cost for suboffer must not be less than 0 "),
    INVALID_SUBOFFER_OLD_COST(23545, "Old cost for suboffer must not be less than 0 "),
    NO_DENOMINATION_CONFIGURED(23546, "Denomination limit cannot be set as no denomination is being configured for offer"),
    DENOMINATION_DOES_NOT_MATCH_OFFER_LIMIT(23547, "Denomination in offer denomination limit is not being configured for this offer "),
    DENOMINATION_DOES_NOT_MATCH_ACCOUNT_LIMIT(23548, "Denomination in account offer denomination limit is not being configured for this offer "),
    DENOMINATION_DOES_NOT_MATCH_MEMBER_LIMIT(23549, "Denomination in member offer denomination limit is not being configured for this offer "),
    INVALID_IS_DOD(23550,"Invalid value for isDod flag"), 
    INVALID_IS_FEATURED(23551,"Invalid value for isFeatured flag"), 
    INVALID_IS_GIFT(23552,"Invalid value for isGiftd flag"), 
    INVALID_NEW_OFFER(23553,"Invalid value for newOffer flag"), 
    INVALID_GROUPED_FLAG(23554,"Invalid value for grouped flag"), 
    INVALID_SHARING(23555,"Invalid value for sharing flag"),
    
	// Sub offers Validations
	INVALID_VAT_PERCENATEG(23556,"Vat percentage should be greater than zero."),
	INVALID_SUBOFFER_TITLEEN(23557,"Sub offer title english must not be empty."),
	INVALID_SUBOFFER_OLDDIRHAM_VALUE(23559,"Sub offer old cost value must not be empty."),
	INVALID_SUBOFFER_NEWDIRHAM_VALUE(23561,"Sub offer new cost value must not be empty."),
	
	// Offer Status
	OFFER_ACTIVATED_ALREADY(23563, "Offer has already been activated"),
	OFFER_DEACTIVATED_ALREADY(23564, "Offer has already been deactivated"),
	INVALID_OFFER_STATUS(23565, "Offer status can be ACTIVE or INACTIVE"),
	OFFER_STATUS_UPDATE_FAILED(23566, "Status update failed."),
	
	//Eligible payment methods
	LISTING_PAYMENT_METHODS_FAILED(23568, "Listing the eligible payment methods failed."),
	PURCHASE_ITEM_PAYMENT_METHOD_NOT_SET(23569, "No payment methods have been set for this purchase service"),
	NO_PAYMENT_METHODS_TO_DISPLAY(23570, "No eligible payment methods"),
	CUSTOMER_PAYMENT_METHOD_NOT_SET(23571, "No payment methods have been set for the customer type"),
			
	//Offer Updation	
	OFFER_UPDATION_FAILED(23572, "Offer updation failed"),
	OFFER_NOT_AVAIABLE_UPDATE(23573, "Offer not available to update"),
	OFFER_CODE_CANNOT_BE_CHANGED(23574,"Offer code cannot be updated"),	
	OFFER_TYPE_CANNOT_BE_CHANGED(23575,"Offer type cannot be updated"),
	UNAUTHORIZED_ROLE(23577,"You are not allowed to perform the operation based on your role"),
	
	// Listing Offers
	NO_OFFERS_TO_DISPLAY(23578, "No offers to display."),
	NO_OFFERS_FOR_PARTNER_TO_DISPLAY(23579, "No offers to display for the partner."),
	INVALID_PARTNER_CODE(23580,"This partner is not available"),
	NO_OFFERS_FOR_MERCHANT_TO_DISPLAY(23581, "No offers to display for the merchant."),
	LISTING_OFFERS_PORTAL_FAILED(23582, "Listing the offer or offers failed."),
	NO_OFFERS_FOR_MEMBER_TO_DISPLAY(23583, "No offers filtered to display."),
	LISTING_OFFERS_MEMBER_FAILED(23584, "Listing the offer(s) for member failed."),
	OFFER_NOT_AVAILABLE(23585, "Offer with entered offerId not available."),
	NO_OFFERS_FOR_CATEGORY_TO_DISPLAY(23586, "No offers to display under the category."),
	NO_OFFERS_FOR_SUBCATEGORY_TO_DISPLAY(23587, "No offers to display under the sub-category."),
	NO_NEW_OFFERS_TO_DISPLAY(23588, "No new offers to display."),
	NO_OFFERS_TO_DISPLAY_FOR_KEYWORD(23589, "No offers to display for the given keyword."),
	NO_OFFERS_TO_DISPLAY_FOR_GROUPED(23590, "No grouped offers to display."),
	LISTING_OFFERS_ELIGIBLE_FAILED(23592, "Listing the eligible offer(s) failed."),
	
	// Static tables
	PAYMENTMETHOD_ADDITION_FAILED(23594, "Payment method addition failed."),
	DUPLICATE_PAYMENTMETHOD(23595, "Payment method already exists."),
	
	OFFERTYPE_ADDITION_FAILED(23596, "Offer type addition failed."),
	DUPLICATE_OFFERTYPE(23598, "Offer type already exists."),
	
	CATEGORY_ADDITION_FAILED(23599, "Category addition failed."),
	DUPLICATE_CATEGORY(23601, "Category already exists."),
	
	SUBCATEGORY_ADDITION_FAILED(23602, "Subcategory addition failed."),
	DUPLICATE_SUBCATEGORY(23604, "Subcategory already exists"),
	
	DENOMINATION_ADDITION_FAILED(23605, "Denomination addition failed."),
	DUPLICATE_DENOMINATION(23607, "Denomination already exists"),
	
	INVALID_PAYMENTMETHOD(23613,"PaymentMethod entered does not exist for the offer type"),
	
	//Offer eligibility
	OFFER_INACTIVE(23614, "The current offer is not active"),
	MERCHANT_INACTIVE(23616, "The associated merchant is not active"),
	STORE_INACTIVE(23617, "No active stores linked"),
	CUSTOMER_TYPE_INELIGIBLE(23621,"You are not not eligible to view the offer based on your customer type"),
	NOT_AVAILABLE_IN_PORTAL(23622,"Offer not available in the specified portal"),
	
	//EligibleOffersRequest FilterFlag validation
	INVALID_FILTER_FLAG(23623,"Filter Flag is not valid"),
	CATEGORYID_NOT_AVAILABLE(23624,"CategoryId must not be empty"),
	SUBCATEGORYID_NOT_AVAILABLE(23625,"SubCategoryId must not be empty"),
	KEYWORDS_NOT_AVAILABLE(23626,"Keywords must not be empty"),
	INVALID_FILTER_FLAG_VALUE(23627,"Invalid value for filter flag."),
	MERCHANT_CODE_NOT_AVAILABLE(23628,"Merchant code must not be empty."),
		
	// Fetching Static Table Data 
    NO_PAYMENT_METHOD_TO_DISPLAY(23630, "No payment method to display"),
    PAYMENT_METHOD_LISTING_FAILED(23631,"Listing Payment methods failed"),
    
	// Fetching Static OfferType table 
	NO_OFFER_TYPE_TO_DISPLAY(23632, "No offer type  to display"),
	OFFER_TYPE_LISTING_FAILED(23633,"Listing offer type failed"),
	
	// Fetching Static Denomination table 
	NO_DENOMINATION_TO_DISPLAY(23634, "No denominations to display"),
	DENOMINATION_LISTING_FAILED(23635,"Listing denominations failed"),
	
	// Fetching Static Category table
	NO_CATEGORY_TO_DISPLAY(23636, "No category to display"),
	CATEGORY_LISTING_FAILED(23637,"Listing categories failed"),
	
	// Fetching Static SubCategory table
	NO_SUBCATEGORY_TO_DISPLAY(23638, "No Subcategory to display"),
	SUBCATEGORY_LISTING_FAILED(23639,"Listing Subcategories failed"),
	OFFERTYPE_NOT_EXISTING(23640, "This offerType is not available"),
    CATEGORY_NOT_EXISTING(23641, "This category is not available"),
    SUBCATEGORY_NOT_EXISTING(23642, "This subcategory does not exist"),
    SUBCATEGORY_NOT_UNDER_CATEGORY(23643, "This subcategory is not available under this category"),
    DENOMINATION_NOT_EXISTING(23644, "The denomination does not exist"),
    SUBCATEGORY_NOT_VALID(23645,"Not a valid subcategory"),
    PARENT_CATEGORY_NOT_EXISTING(23646, "This parent category is not available"),
		
	//Validation issue when value not from List of Values allowed
	PROVISIONING_CHANNEL_NOT_ALLOWED(23647, "Provisioning channel entered is not valid."),
	VOUCHER_ACTION_NOT_ALLOWED(23648, "Voucher action entered is not valid."), 
	
	//Static table OfferType updation
    OFFERTYPE_UPDATION_FAILED(23650, "Offer type updation failed"),
    OFFERTYPE_NOT_AVAILABLE_TO_UPDATE(23651, "This offer type does not exist"),
    OFFERTYPE_ID_CANNOT_BE_CHANGED(23652, "OfferType Id cannot be changed"),
    OFFERTYPE_DESC_EN_CANNOT_BE_CHANGED(23653, "Offer type description(English) cannot be changed"),
    OFFERTYPE_DESC_AR_CANNOT_BE_CHANGED(23654, "Offer type description(Arabic) cannot be changed"),
    PAYMENT_METHOD_NOT_EXISTING(23655, "PaymentMethod entered does not exists"),
    
  //Offer Purchase creation
    OFFER_PURCHASE_FAILED(23656,"Offer purchase failed"),
	OFFERID_CANNOT_BE_EMPTY(23657,"Offer Id cannot be empty"),
	ACCOUNT_NOT_AVAILABLE(23658,"Account does not exist"),
	MEMBER_NOT_AVAILABLE(23659,"Member does not exist"),
	PAYMENT_FAILED(23660,"Payment failed"),
    DENOMINATION_NOT_EXISTING_FOR_OFFER(23661,"Denomination entered is not valid for selected offer"),
    PAYMENT_METHOD_NOT_ELIGIBLE(23662,"Not an eligible payment method"),
    SUB_OFFER_NOT_EXISTING_FOR_OFFER(23663,"Suboffer entered is not valid for selected offer"),
    BILLING_FAILED(23664,"Bill payment Failed"),
    RECHARGE_FAILED(23665,"Recharge Failed"),
    COUPON_QUANTITY_EMPTY(23666,"Coupon Quantity cannot be empty"),
    PURCHASE_ITEM_FAILED(23667,"Purchase Failed"),
    PURCHASE_ITEM_INVALID(23669,"Purchase item is not valid"),
    CARD_DETAILS_EMPTY(23670,"Card details cannot be empty"),
    POINTS_TO_REDEEM_EMPTY(23671,"Points Value cannot be empty"),
    PAYMENT_TYPE_EMPTY(23672,"Payment Type cannot be empty for DCB payment option"),
    VOUCHER_DENOMINATION_EMPTY(23673,"Denomination cannot be empty"),
    OFFER_COUPON_LIMIT_EXCEEDED(23674,"Offer coupon limit exceeded"),
    OFFER_DAILY_LIMIT_EXCEEDED(23675,"Offer daily limit exceeded"),
    OFFER_WEEKLY_LIMIT_EXCEEDED(23676,"Offer weekly limit exceeded"),
    OFFER_MONTHLY_LIMIT_EXCEEDED(23677,"Offer monthly limit exceeded"),
    OFFER_ANNUAL_LIMIT_EXCEEDED(23678,"Offer annual limit exceeded"),
    OFFER_TOTAL_LIMIT_EXCEEDED(23679,"Offer total limit exceeded"),
    ACCOUNT_OFFER_DAILY_LIMIT_EXCEEDED(23680,"Offer daily limit exceeded for this account number"),
    ACCOUNT_OFFER_WEEKLY_LIMIT_EXCEEDED(23681,"Offer weekly limit exceeded for this account number"),
    ACCOUNT_OFFER_MONTHLY_LIMIT_EXCEEDED(23682,"Offer monthly limit exceeded for this account number"),
    ACCOUNT_OFFER_ANNUAL_LIMIT_EXCEEDED(23683,"Offer annual limit exceeded for this account number"),
    ACCOUNT_OFFER_TOTAL_LIMIT_EXCEEDED(23684,"Offer total limit exceeded for this account number"),
    MEMBER_OFFER_DAILY_LIMIT_EXCEEDED(23685,"Offer daily limit exceeded for this membership code"),
    MEMBER_OFFER_WEEKLY_LIMIT_EXCEEDED(23686,"Offer weekly limit exceeded for this membership code"),
    MEMBER_OFFER_MONTHLY_LIMIT_EXCEEDED(23687,"Offer monthly limit exceeded for this membership code"),
    MEMBER_OFFER_ANNUAL_LIMIT_EXCEEDED(23688,"Offer annual limit exceeded for this membership code"),
    MEMBER_OFFER_TOTAL_LIMIT_EXCEEDED(23689,"Offer total limit exceeded for this membership code"),
    ACTIVITY_CODE_NOT_PRESENT(23691,"Activity code is not present"),
    COST_EMPTY(23693,"Cost cannot be empty"),
    OFFER_TITLE_EMPTY(23694,"Offer title cannot be empty"),
    INVALID_OFFER_TITLE(23695,"Offer title entered not matching with selected offer's title"),
    NOT_PREPAID_CUSTOMER(23699,"Member is not eligible for recharge."),
    NOT_POSTPAID_CUSTOMER(23700,"Member is not eligible for bill payment."),
    NOT_ELIGIBLE_BY_RULES(23701,"Member is not eligible to purchase offer as per rules configured for offer"),
    
   
    //Activity Code Creation
    PARTNER_ACTIVITY_NOT_CREATED(23703, "Unable to create activity code in member activity"),
    PROGRAM_ACTIVITY_NOT_CONFIGURED(23705, "Program Activity not configured in member activity"),
	
	//ETISALAT ADD-ON VALIDATIONS
	INVALID_VAT_PERCENTAGE(23706,"Vat percentage should be greater than zero."),
	NULL_VAT_PERCENTAGE(23707,"Vat percentage should not be null."),
	
	CHANNEL_NOT_ALLOWED(23708,"This channel Id is not allowed for member"), 
	LESS_CREDIT_CARD_AMOUNT_PAID(23710, "The amount paid by customer using credit card is less than the required amount to be paid"),
	LESS_SMILES_POINTS_AMOUNT_DISCREPANCY(23711, "The payment made using smiles points is less than the required amount to be paid"),
	LESS_CARD_POINTS_AMOUNT_DISCREPANCY(23712, "The payment made using smiles points and credit card is less than than the required amount to be paid"),
	LESS_ADD_TO_BILL_AMOUNT_DISCREPANCY(23713, "The amount to be added to bill is less than the required amount to be paid"),
	LESS_DEDUCT_FROM_BALANCE_AMOUNT_DISCREPANCY(23714, "The payment amount to be deducted from balance is less than the required amount to be paid"),
	
	PORTAL_VALUE_BLANK(23715,"Blank value entered in available in portals"),
	RULE_VALUE_BLANK(23717,"Blank value entered in rules"), 
	
	
	NO_POINTS_RECEIVED(23718,"No points received from member activity."), 
	NO_ACTIVITY_CODE_RECEIVED(23719,"No activity code received from member activity."), 
	CUSTOMER_TYPE_LIST_EMPTY(23720, "Customer type list rerieved is empty."),
	
	INVALID_CUSTOMER_TYPE(23722,"Not a valid customer type "), 
	INVALID_EXCLUSION_TYPE(23723,"Cannot be present in exclusion list, parent tye not in eligible list "), 
	
	RULE_NOT_ALLOWED(23724, "Rule has not been configured in decision manager "), 
	EXCLUSION_VALUE_BLANK(23725, "The field is blank in exclusion types"),  
	
	SUBSCRIPTION_PURCHASE_FAILED(23726,"Subscription Purchase Failed"), 
	INAVLID_CASH_VOUCHER_ID(23727, "Not a valid cash voucher offer id"),
	INAVLID_DISCOUNT_VOUCHER_ID(23728, "Not a valid discount voucher offer id"),
	INAVLID_DEAL_VOUCHER_ID(23729, "Not a valid deal voucher offer id"),
	INAVLID_ETISALAT_ADDON_ID(23730, "Not a valid Etisalat add on offer id"), 
	PAYMENT_METHOD_INVALID(23731,"Invalid payment method"), 
	NO_OFFERS_TO_DISPLAY_FOR_MERCHANT(23732, "No offers configured for the merchant"), 
	
	FETCHING_DETAILED_OFFER_MEMBER_FAILED(23733, "Fetching detailed offer for the member failed"), 
	FETCHING_DETAILED_ELIGIBLE_OFFER_FAILED(23734,"Fetching detailed offer failed"), 
	PURCHASE_PAYMENT_METHOD_RECORD_UNAVAILABLE(23735, "The payment method detail is not available for this purchase item"),
	PURCHASE_PAYMENT_METHOD_ADDITION_FAILED(23736, "Purchase payment method addition failed"),
    PURCHASE_PAYMENT_METHODS_LISTING_FAILED(23737, "Purchase payment method listing failed"),
    PURCHASE_PAYMENT_METHOD_UPDATION_FAILED(23738, "Purchase payment method updation failed"),
    NO_PURCHASE_PAYMENT_METHOD_AVAILABLE(23739, "Purchase payment method not available for listing"),
    DUPLICATE_PURCHASE_ITEM(23740,"Duplicate purchase item"),
    
    PURCHASE_ITEM_NOT_AVAILABLE_TO_UPDATE(23741,"This purchase item is not available to update"),
    PURCHASE_ITEM_CANNOT_BE_CHANGED(23742,"Purchase item cannot be modified"),
    
    MORE_CREDIT_CARD_AMOUNT_PAID(23743, "The amount paid by customer using credit card is more than the required amount to be paid"),
    MORE_SMILES_POINTS_AMOUNT_DISCREPANCY(23744, "The payment made using smiles points is more than the required amount to be paid"),
    MORE_CARD_POINTS_AMOUNT_DISCREPANCY(23745, "The payment made using smiles points and credit card is more than than the required amount to be paid"),
    MORE_ADD_TO_BILL_AMOUNT_DISCREPANCY(23746, "The amount to be added to bill is more than the required amount to be paid"),
    MORE_DEDUCT_FROM_BALANCE_AMOUNT_DISCREPANCY(23747, "The payment amount to be deducted from balance is more than the required amount to be paid"), 
    
    FETCHING_PURCHASE_TRANSACTONS_FAILED(23748, "Fetching the marketplace purchase transactions failed."), 
    NO_TRANSACTION_MATCHES_CRITERIA(23749, "No transactions fetched based on your inputs"), 
    FAILED_TO_GET_ELIGIBLE_PAYMENT_METHODS(23750, "Failed to get response from eligble payment methods service "),
    
    POINTS_BANK_RESPONSE_NOT_RECEIVED(23751, "Proper response not received from points bank"),
    MEMBER_MANAGEMENT_RESPONSE_NOT_RECEIVED(23752, "Proper response not received from member management"),
    MEMBER_ACTIVITY_RESPONSE_NOT_RECEIVED(23753, "Proper response not received from member activity"),
    DECISION_MANAGER_RESPONSE_NOT_RECEIVED(23754, "Proper response not received from decision manager"),
    PARTNER_MANAGEMENT_RESPONSE_NOT_RECEIVED(23755, "Proper response not received from partner management"),
    
    NO_PAYMENT_RESPONSE(23756,"Failed to get payment response"), 
     
    
    MEMBERSHIP_CODE_WITH_ACCOUNT_NUMBER(23759, "Membership code mandatory with account number, cannot be null or empty"),
    EMPTY_ACTION(23760, "Action field cannot be empty"), 
    ELIGIBLE_FEATURES_NOT_SET_FOR_CUSTOMER_TYPE(23761, "The eligibility features have not been set for the customer type"),
    
    //Offer Rating Codes
    OFFER_RATING_FAILED(23762, "Failed to rate offer"),
    OFFER_RATING_MEMBER_UNAVAILABLE(23763, "Member not found to rate offer"),
    OFFER_RATING_OFFER_UNAVAILABLE(23764, "Offer not found for offer id: "), 
    
    NOT_IN_AGE_LIMIT(23765, "The age criteria is not met"),
	
	//BirthdayGift Offer Codes
    BIRTHDAY_GIFT_AVAILED(23766,"Birthday Offer has already been availed"),
	
	SUB_OFFERID_CANNOT_BE_EMPTY(23767,"SubOffer Id cannot be empty for purchasing deal voucher"),
	
	BIRTHDAY_CRITERIA_CHECKED_FAILED(23768,"Birthday criteria not fulfilled for the offer"), 
	ACCOUNT_NUMBER_REQUIRED(23769,"Account Number is Required"),
    LISTING_BIRTHDAYGIFT_OFFERS_ACCOUNT_FAILED(23770, "Listing the birthday gift offer(s) for account failed."),
	DENOMINATION_NOT_PRESENT(23778,"Denomination are mandatory for this offer type"), 
	
	DYNAMIC_DENOMINATION_CONFIGURED(23778,"Cannot set denomination limit as dynamic denomination configured"), 
	OFFER_EXPIRED(23779, "Offer has expired"), 
	
	GOLD_CERTIFICATE_PURCHASE_FAILED(23779,"Gold certificate purchase failed"),
    
	DENOMINATION_CANNOT_BE_EMPTY(23780,"Denomination cannot be empty for purchasing this offer"), 
	
	MEMBERSHIPCODE_DOES_NOT_MATCH(23781, "The membership code in request and details fetched for member do not match"), 
	
	INAVLID_GOLD_CERTIFICATE_ID(23782, "Not a valid gold certificate id"), 
	
	OFFER_DENOMINATION_DAILY_LIMIT_EXCEEDED(23783, "Daily limit exceeded for offer denomination"),
	OFFER_DENOMINATION_WEEKLY_LIMIT_EXCEEDED(23784, "Weekly limit exceeded for offer denomination"),
	OFFER_DENOMINATION_MONTHLY_LIMIT_EXCEEDED(23785, "Monthly limit exceeded for offer denomination"),
	OFFER_DENOMINATION_ANNUAL_LIMIT_EXCEEDED(23786, "Annual limit exceeded for offer denomination"),
	OFFER_DENOMINATION_TOTAL_LIMIT_EXCEEDED(23787, "Total limit exceeded for offer denomination"),
	ACCOUNT_OFFER_DENOMINATION_DAILY_LIMIT_EXCEEDED(23788, "Daily limit exceeded for account offer denomination"),
	ACCOUNT_OFFER_DENOMINATION_WEEKLY_LIMIT_EXCEEDED(23789, "Weekly limit exceeded for account offer denomination"),
	ACCOUNT_OFFER_DENOMINATION_MONTHLY_LIMIT_EXCEEDED(23790, "Monthly limit exceeded for account offer denomination"),
	ACCOUNT_OFFER_DENOMINATION_ANNUAL_LIMIT_EXCEEDED(23791, "Annual limit exceeded for account offer denomination"),
	ACCOUNT_OFFER_DENOMINATION_TOTAL_LIMIT_EXCEEDED(23792, "Total limit exceeded for account offer denomination"),
	MEMBER_OFFER_DENOMINATION_DAILY_LIMIT_EXCEEDED(23793, "Daily limit exceeded for member offer denomination"),
	MEMBER_OFFER_DENOMINATION_WEEKLY_LIMIT_EXCEEDED(23794, "Weekly limit exceeded for member offer denomination"),
	MEMBER_OFFER_DENOMINATION_MONTHLY_LIMIT_EXCEEDED(23795, "Monthly limit exceeded for member offer denomination"),
	MEMBER_OFFER_DENOMINATION_ANNUAL_LIMIT_EXCEEDED(23797, "Annual limit exceeded for member offer denomination"),
	MEMBER_OFFER_DENOMINATION_TOTAL_LIMIT_EXCEEDED(23797, "Total limit exceeded for member offer denomination"), 
	
	NO_GIFT_OFFERS(23798, "No gift offer filtered"), 
	MORE_THAN_ONE_GIFT_OFFER(23799, "More than one offer matches this request"),
	
	INVALID_VOUCHER_AMOUNT(23832, "The voucher amount cannot be less than 0"), 
	NO_CUSTOMER_SEGMENTS_PRESENT(23833, "No customer segments present in DB"),
	INVALID_CUSTOMER_SEGMENT(23834 ,"The customer segment does not exist"), 
	
	INVALID_CUSTOMER_SEGMENT_IN_LIMIT(23836, "The customer segment mentioned in limit does not exist"), 
	CUSTOMER_SEGMENT_LIMIT_NOT_DEFINED(23837,"Customer segment limit not defined "), 
	CUSTOMER_SEGMENT_IN_LIMIT_NOT_ELIGIBLE(23838, "Limit cannot be set for customer segment as not in eligible customer segment "), 
	NO_DENOMINATIONS_PRESENT(23839, "No denomination present in db"), 
	
	DENOMINATION_LIMIT_NOT_SET(23840, "Denomination limit not set for configured denomination "),
	DENOMINATION_NOT_CONFIGURED(23841, "Denomination limit set but denomination not configured"), 
	
	NOT_IN_ELIGIBLE_CUSTOMER_SEGMENT(23842, "Not in eligible customer segments configured for the offer"), 
	
	CUSTOMER_SEGMENT_EXISTS(23483, "Customer segment with this name already exists"), 
	CUSTOMER_SEGMENT_CREATION_FAILED(23844, "Customer segment creation failed"), 
	
	RATE_NOT_SET_FOR_PARTNER(23845, "The conversion rate is not set for the partner/product type"), 
	
	RATE_NOT_DEFINED_FOR_THIS_RANGE(23846, "Conversion rate not defined for this range"), 
	
	INVALID_OPERATION(23847, "Invalid operation for customer segment"), 
	HIGH_VALUE_NOT_PRESENT(23848, "Range high value not present for"),
	LOW_VALUE_NOT_PRESENT(23849, "Range low value not present for"),
	EXACT_VALUE_NOT_PRESENT(23850, "Comparison value not present for"),
	INVALID_UNIT(23851, "Invalid unit"), 
	STRING_VALUE_BLANK(23852, "One or more list value is blank"),
	
	VOUCHER_RESPONSE_NOT_RECEIVED(23853, "Proper response not received from vouchers"),
	
	//Wishlist Error Codes
	WISHLIST_INVALID_PARAMETERS(2720, "Invalid input parameters."), 
	ADDING_TO_WISHLIST_FAILED(2721, "Adding to wishlist failed"),
	REMOVING_FROM_WISHLIST_FAILED(2722, "Removing from wishlist failed"),
	GETTING_FROM_WISHLIST_FAILED(2723, "Displaying offers from wishlist failed"), 
	OFFER_NOT_PRESENT(2724, "The offer is not available"),
	OFFER_WITH_ID_NOT_PRESENT(2725, "The offer with following id is not available"),
	NO_OFFER_IN_WISHLIST(2726, "There are no offers present in your wishlist."), 
	NO_WISHLIST_FOR_ACCOUNT(2727, "Wishlist does not exist for this account number"),
	NO_ACTIVE_OFFER_IN_WISHLIST(2728, "There are no active offers present in your wishlist."), 
	OFFER_NOT_IN_WISHLIST(2729, "The offer is not present in your wishlist "), 
	OFFER_ALREADY_IN_WISHLIST(2730, "The offer is already present in your wishlist"), 
	UPDATING_WISHLIST_FAILED(2731,"Updating the wishlist failed"), 
	NOT_A_VALID_WISHLIST_ACTION(2732,"The action entered is not valid"), 
	NO_OFFERS_IN_DATABASE(2733, "There are no offers present in the database"),
	WISHLIST_CONFIGURATION_FAILED(2734,"Adding/Updating to wishlist failed"), 
	
	CONFIGURED_STORES_NOT_FOUND(23854, "None of stores configured exist"), 
	
	BALANCE_EMPTY(23855, "Balance cannot be empty for amount/points conversion for gold certificate"),
	
	CONFIGURING_BIRTHDAY_INFO_FAILED(23856, "Failed to configure birthday information"), 
	BIRTHDAY_NOT_SET(23857, "The birthday is not present in member deatils"), 
	BIRTHDAY_INFO_NOT_SET(23858, "Birthday information has not been configured"),
	RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ACCOUNT(23859, "Failed to retrieve birthday information for account"), 
	NO_PAYMENT_NEEDED_FOR_FREE_OFFER(23860, "No payment in amount/points required for free offer"), 
	NO_BIRTHDAY_OFFERS_FILTERED(23861, "No birthday offers filtered"), 
	
	ELIGIBLE_VALUE_BLANK(23862, "The field is blank in eligible types"),
	ELIGIBLE_SEGMENT_BLANK(23862, "The field is blank in eligible customer segment"),
	EXCLUSION_SEGMENT_BLANK(23862, "The field is blank in exclusion customer segments"), 
	PAGE_LIMIT_NUMBER_TOGETHER(23863, "Page number/ Page limit cannot be sent alone"), 
	LATITUDE_LONGITUDE_TOGETHER(23864, "Latitude/ Longitude cannot be sent alone"),
	
	RETREIVING_BIRTHDAY_INFO_FAILED_FOR_ADMIN(23867, "Failed to retrieve birthday information for admin"), 
	BIRTHDAY_OFFER_PURCHASE_LIMIT_CROSSED(23868, "Purchase limit for birthday offers has been reached"), 
	NOT_A_VALID_ORDER(23869, "Price order can be 1/ -1"), 
	NOT_A_VALID_OFFER_TYPE_FOR_PRFERENCE(23870, "The offer type mentioned in offerTypePreference is not valid"), 
	INVALID_PAGE_NUMBER(23871, "Page number cannot be less than 0"),
	INVALID_PAGE_LIMIT(23872, "Page limit cannot be less than 1"), 
	
	INVALID_OFFER_TYPE(23874, "Offer type in input is not valid"),
	INVALID_STATUS(23875, "Offer status in input is not valid"),
	
	STANDARD_GLOBAL_LIMIT_EXCEEDED(23876, "The cinema offer global limit for standard customer segment reached/ exceeded"),
	SUBSCRIBER_GLOBAL_LIMIT_EXCEEDED(23877, "The cinema offer global limit for subscriber customer segment reached/ exceeded"),
	SPECIAL_GLOBAL_LIMIT_EXCEEDED(23878, "The cinema offer global limit for special customer segment reached/ exceeded"),
	
	STANDARD_MEMBER_LIMIT_EXCEEDED(23879, "The cinema offer member limit for standard customer segment reached/ exceeded"),
	SUBSCRIBER_MEMBER_LIMIT_EXCEEDED(23880, "The cinema offer member limit for subscriber customer segment reached/ exceeded"),
	SPECIAL_MEMBER_LIMIT_EXCEEDED(23881, "The cinema offer member limit for special customer segment reached/ exceeded"),
	
	STANDARD_ACCOUNT_LIMIT_EXCEEDED(23882, "The cinema offer account limit for standard customer segment reached/ exceeded"),
	SUBSCRIBER_ACCOUNT_LIMIT_EXCEEDED(23883, "The cinema offer account limit for subscriber customer segment reached/ exceeded"),
	SPECIAL_ACCOUNT_LIMIT_EXCEEDED(23884, "The cinema offer account limit for special customer segment reached/ exceeded"),

	MEMBER_NOT_ELIGIBLE_ON_DAY(23885, "Member is not eligible to view offer today"), 
	ELIGIBLE_OFFERS_CONFIGURATION_FAILED(23886, "Failed to configure eligible offers"), 
	SUBOFFER_ID_NOT_UNIQUE(23887, "SubOffer Id is not unique "), 
	
	INVALID_OFFER_CODE_SIZE(23013, "Offer Code size must not exceed 20 characters"), 
	ESTIMATED_SAVINGS_REQUIRED(23888, "Estimated savings is mandatory for discount voucher"), 
	SINGLETON_RECORDS_RETRIEVAL_FAILED(23889, "Failed to retrieve eligible offers from servelet context "), 
	CANNOT_CREATE_OFFER_WITH_OFFER_TYPE(23890, "Offer with input offer type cannot be created"), 
	BLANK_VOUCHER_REDEEM_TYPE(23891, "The voucher redeem type cannot be empty for this offer type"),
	INVALID_VOUCHER_REDEEM_TYPE(23892, "The voucher redeem type is not valid"), 
	NON_EMPTY_PARTNER_REDEEM_URL(23893, "The partner redeem url should not be set for this voucher redeem type"),
	NON_EMPTY_TITLE_REDEEM_INSTRUCTION_EN(23894, "The instruction to redeem title English should not be set for this voucher redeem type"),
	NON_EMPTY_TITLE_REDEEM_INSTRUCTION_AR(23895, "The instruction to redeem title Arabic should not be set for this voucher redeem type"),
	NON_EMPTY_REDEEM_INSTRUCTION_EN(23896, "The instruction to redeem English should not be set for this voucher redeem type"),
	NON_EMPTY_REDEEM_INSTRUCTION_AR(23897, "The instruction to redeem Arabic should not be set for this voucher redeem type"),
	EMPTY_PARTNER_REDEEM_URL(23893, "The partner redeem url should not be empty for this voucher redeem type"),
	EMPTY_TITLE_REDEEM_INSTRUCTION_EN(23894, "The instruction to redeem title English should not be empty for this voucher redeem type"),
	EMPTY_TITLE_REDEEM_INSTRUCTION_AR(23895, "The instruction to redeem title Arabic should not be empty for this voucher redeem type"),
	EMPTY_REDEEM_INSTRUCTION_EN(23896, "The instruction to redeem English should not be empty for this voucher redeem type"),
	EMPTY_REDEEM_INSTRUCTION_AR(23897, "The instruction to redeem Arabic should not be empty for this voucher redeem type"), 
	PROMOTIONAL_GIFT_FAILED(23898, "Gifting promotional gift failed"),
	PROMOTIONAL_BOTH_GIFT_FAILED(23899, "Both subscription and voucher gifting failed"),
	PROMOTIONAL_GIFT_SUBSCRIPTION_FAILED(23900, "Failed to gift subscription"),
	PROMOTIONAL_VOUCHER_GIFT_FAILED(23901, "Failed to gift voucher"),
PROMOTIONAL_GIFT_DM_CHECK_FAILED(23902, "DM check failed "),
	SUBSCRIPTION_CATALOG_NOT_FOUND(23903,"No Subscription Catalog Found"),
	PROPER_RESPONSE_NOT_RECIEVED_AFTER_VOUCHER_GIFTING(23904, "Proper response not recieved after voucher gifting"),
	PROPER_RESPONSE_NOT_RECIEVED_AFTER_SUBSCRUPTION_GIFTING(23904, "Proper response not recieved after subscription gifting"),
	EITHER_CATEGORY_ID_OR_CATEGORY_ID_LIST_IS_MANDATORY(23905,"Either category Id or Category Id List is mandatory"),
	LESS_APPLE_PAY_AMOUNT_PAID(23906, "The amount paid by customer using apple pay is less than the required amount to be paid"),
	LESS_SAMSUNG_PAY_AMOUNT_PAID(23907, "The amount paid by customer using samsung pay is less than the required amount to be paid"),
	MORE_APPLE_PAY_AMOUNT_PAID(23908, "The amount paid by customer using apple pay is more than the required amount to be paid"),
	MORE_SAMSUNG_PAY_AMOUNT_PAID(23909, "The amount paid by customer using samsung pay is more than the required amount to be paid"),
	NOT_A_VALID_OFFER_TYPE_ITEM(23910, "The offer type is not valid"),
	
	PROMOTIONAL_GIFT_SUBSCRIPTION_EXISTS_VOUCHER_GIFTING_FAILED(24111, "Voucher gifting failed and account already subscribed"),
	RESET_ALL_COUNTER_FAILURE(24114, "Reset All Counter Failure"),
	GENERIC_RUNTIME_EXCEPTION(24115, "Runtime Exception occured. Please refer logs "),

        FILE_IS_EMPTY(24116, "File is empty"),
	NO_DATA_TO_UPDATE(24117, "Data is not available to update"),
	EXTERNAL_REFERENCE_NUMBER_EMPTY(24118, "External Reference Number Cannot be Empty"),
	EXTERNAL_REFERENCE_NUMBER_DUPLICATE(24118, "Duplicate External Reference Number"),

	FETCHING_REFUND_TRANSACTONS_FAILED(24120, "Failed to fetch refund transactions."),
	
	CONFIGURED_PAYMENT_METHODS_NOT_FOUND(23854, "None of payment methods configured exist"),
	LISTING_RESTAURANTS_FAILED(23870, "Restaurant listing failed."),

	;
	
	private final int id;

	private final String msg;

	OfferErrorCodes(int id, String msg) {
		this.id = id;
		this.msg = msg;
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

}

