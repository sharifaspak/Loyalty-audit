package com.loyalty.marketplace.offers.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.loyalty.marketplace.offers.constants.OfferConstants;
import com.loyalty.marketplace.offers.constants.OffersConfigurationConstants;

/**
 * 
 * @author jaya.shukla
 *
 */
@SuppressWarnings(OffersConfigurationConstants.UNCHECKED)
public class Utilities {
	
	private static final Logger LOG = LoggerFactory.getLogger(Utilities.class);
	
	Utilities(){
		
	}
	
	/**
	 * 
	 * @param list
	 * @param value
	 * @return status to indicate if value is present in list
	 * 
	 */
	public static boolean presentInList(List<String> list, String value) {
		
		List<String> uppercaseList = null;
		boolean isPresent = false;
		
		if(!CollectionUtils.isEmpty(list)) {
			
			uppercaseList = getUpperCaseList(list);
			isPresent = !ObjectUtils.isEmpty(value) 
					&& !CollectionUtils.isEmpty(uppercaseList)
					&& uppercaseList.contains(value.toUpperCase());
		}
		
		return isPresent;
	}
	
	/**
	 * 
	 * @param originalList
	 * @param valueList
	 * @return status to indicate any value from valueList is present in original list
	 */
    public static boolean anyValuePresentInList(List<String> originalList, List<String> valueList) {
		
		boolean isPresent = false;
		
		if(!CollectionUtils.isEmpty(originalList) && !CollectionUtils.isEmpty(valueList)) {
			
			List<String> valuesPresent = valueList.stream().filter(v->presentInList(originalList, v)).collect(Collectors.toList());
			isPresent = !CollectionUtils.isEmpty(valuesPresent);	
			
		}
		
		return isPresent;
	}
	
	/**
	 * 
	 * @param list
	 * @param value
	 * @return value to indicate if value is present in any of the two list
	 * 
	 */
	public static boolean presentInAnyList(List<String> list1, List<String> list2, String value) {
		
		List<String> uppercaseList1 = null;
		boolean isPresent = false;
		
		if(!CollectionUtils.isEmpty(list1)) {
			
			uppercaseList1 = getUpperCaseList(list1);
			isPresent = !CollectionUtils.isEmpty(uppercaseList1) && uppercaseList1.contains(value.toUpperCase());
		}
		
		if(!isPresent) {
			
			List<String> uppercaseList2 = !CollectionUtils.isEmpty(list2) ? getUpperCaseList(list2) : null;
			isPresent = !CollectionUtils.isEmpty(uppercaseList2) && uppercaseList2.contains(value.toUpperCase());
		}
		
		return isPresent;
		
	}
	
	/**
	 * 
	 * @param valueList
	 * @return list with each element converted to its upper case value
	 * 
	 */
	public static List<String> getUpperCaseList(List<String> valueList) {
		
		return null!=valueList && !valueList.isEmpty() 
				? valueList.stream().map(String::toUpperCase).collect(Collectors.toList())
				: null;
		
	}
	
	/**
	 * 
	 * @param list1
	 * @param list2
	 * @return common values of the two lists
	 */
	public static List<String> intersection(List<String> list1,
			List<String> list2) {
		
		Collection<String> commonStrings = CollectionUtils.isNotEmpty(list1)
			&& CollectionUtils.isNotEmpty(list2)
			 ? CollectionUtils.intersection(list1, list2)
			 : null;
		return CollectionUtils.isNotEmpty(commonStrings)
			 ? commonStrings.stream().collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param list1
	 * @param list2
	 * @return common values of the two integer lists
	 */
	public static List<Integer> intersectionInteger(List<Integer> list1,
			List<Integer> list2) {
		
		Collection<Integer> commonStrings = CollectionUtils.isNotEmpty(list1)
			&& CollectionUtils.isNotEmpty(list2)
			 ? CollectionUtils.intersection(list1, list2)
			 : null;
		return CollectionUtils.isNotEmpty(commonStrings)
			 ? commonStrings.stream().collect(Collectors.toList())
			 : null;
	}
	
	/**
	 * 
	 * @param dateString
	 * @param format
	 * @return string converted to date with the input format 
	 * @throws ParseException
	 */
    public static Date changeStringToDate(String dateString, String format) throws ParseException {
		
		return !StringUtils.isEmpty(dateString) && !StringUtils.isEmpty(format)
			? new SimpleDateFormat(format).parse(dateString)
			: null;
	}
    
    /**
     * 
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static String changeStringDateFormat(String date, String format1, String format2) throws ParseException {
		
    	return !ObjectUtils.isEmpty(date) && !StringUtils.isEmpty(format1) && !StringUtils.isEmpty(format2)
			 ? changeDateToStringWithFormat(changeStringToDate(date, format1), format2)
			 : null;
	}
    
    /**
     * 
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static String changeStringDateFormatWithTimeZone(String date, String format) throws ParseException {
		
    	return !ObjectUtils.isEmpty(date) && !StringUtils.isEmpty(format)
			 ? changeDateToStringWithFormat(changeStringToDateWithTimeZone(date, format), format)
			 : null;
	}
    
    /**
     * 
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static String changeDateToStringWithFormat(Date date, String format) {
		
    	DateFormat df = new SimpleDateFormat(format);
		return !ObjectUtils.isEmpty(date) && !StringUtils.isEmpty(format)
			 ? df.format(date)
			 : null;
	}
    
    /**
     * 
     * @param dateString
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date changeStringToDateWithTimeZone(String dateString, String format) throws ParseException {
		
    	SimpleDateFormat myDate = new SimpleDateFormat(format);
    	myDate.setTimeZone(TimeZone.getTimeZone(OfferConstants.UTC.get()));
    	LOG.info("TimeZone Date: {}", myDate.parse(dateString));
		return myDate.parse(dateString);
	}
    
    /**
     * 
     * @param stringValueList
     * @return list of string converted to list of integer
     */
    public static List<Integer> convertFromStringToIntegerList(List<String> stringValueList) {
		
		List<Integer> integerValueList = null;
		
		if(!CollectionUtils.isEmpty(stringValueList)) {
			
			integerValueList = new ArrayList<>(stringValueList.size());
			for(String s : stringValueList) {
				
				integerValueList.add(Integer.parseInt(s));
			}
			
		}
		
		return integerValueList;
		
   }
   
   /**
    *  
    * @param value1
    * @param value2
    * @return status to indicate two objects are equal
    */
   public static boolean isEqual(Object value1, Object value2) {
    	
    	return !ObjectUtils.isEmpty(value1) 
    		&& !ObjectUtils.isEmpty(value2) 
    		&& value1.equals(value2);
    }
   
   /**
    * 
    * @param setValues
    * @return set converted to list
    */
   public static List<String> convertSetToList(Set<String> setValues) {
		
		return !CollectionUtils.isEmpty(setValues)
			 ? new ArrayList<>(setValues)
			 : null;
	}
   
   /**
    * 
    * @param value
    * @return the value or null 
    */
   public static Double getDoubleValueOrNull(Double value) {
		
		return !ObjectUtils.isEmpty(value) ? value : null;
	}
   
   /**
    * 
    * @param valueList
    * @return status to indicate list has just one element
    */
   public static boolean isListWithOneElement(Collection<?> valueList) {
		
		return !CollectionUtils.isEmpty(valueList) && valueList.size()==1;
	}
   
   /**
    * 
    * @param duration
    * @return date after specific number of days from current date
    */
   public static Date getDate(Integer duration) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, duration);
		return cal.getTime();
	}
   
   /**
    * 
    * @param duration
    * @param date
    * @return date after specific number of days from input date
    */
   public static Date getDateFromSpecificDate(Integer duration, Date date) {
		
	    Date newDate = null;
	    
	    if(!ObjectUtils.isEmpty(date)) {
	    	
	    	Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, duration);
			newDate = cal.getTime();
	    }
		
		return newDate;
	}
   
   /**
    * 
    * @param duration
    * @param date
    * @return date after specific years form input date
    */
   public static Date getDateFromSpecificDateDurationYear(Integer year, Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}
   
   /**
    * 
    * @param value1
    * @param value2
    * @return status to indicate objects are not equal
    */
   public static boolean isNotEqual(Object value1, Object value2) {
   	
   	return !ObjectUtils.isEmpty(value1) 
   		&& !ObjectUtils.isEmpty(value2) 
   		&& !value1.equals(value2);
   }
   
   /**
    * 
    * @param flag
    * @return the value of flag based on input flag
    */
   public static String getFlag(String flag) {
		
		return null!=flag && !StringUtils.isEmpty(flag)
				? flag : OfferConstants.FLAG_NOT_SET.get();
	}
   
   /**
    * 
    * @param value
    * @return string value or null
    */
   public static String getStringValueOrNull(String value) {
		
		return !StringUtils.isEmpty(value) ? value : null;
	}
   
   /**
    * 
    * @param list
    * @param value
    * @return status to indicate if value is present in integer list
    */
   public static boolean presentInIntegerList(List<Integer> list, Integer value) {
		
		return CollectionUtils.isNotEmpty(list) 
			&& !ObjectUtils.isEmpty(value) 
			&& list.contains(value);
	}
   
   /**
    * 
    * @param value
    * @return double value or 0.0 if null
    */
   public static Double getDoubleValue(Double value) {
		
		return !ObjectUtils.isEmpty(value) ? value : OffersConfigurationConstants.ZERO_DOUBLE;
	}
   
   /**
    * 
    * @param value
    * @return integer value or 0 if null
    */
   public static boolean isNullOrZero(Integer value) {
		
		return ObjectUtils.isEmpty(value) || value.equals(OffersConfigurationConstants.ZERO_INTEGER);
	}
   
   /**
    * 
    * @param firstDate
    * @param secondDate
    * @return comparison value of two dates
    */
   public static Integer compareDates(Date firstDate, Date secondDate){
		
		return firstDate.compareTo(secondDate);
		
	}
   
   /**
    * 
    * @param value1
    * @param value2
    * @return status to indicate first double greater than second
    */
   public static boolean firstMoreThanSecond(Double value1, Double value2) {
		
		return !ObjectUtils.isEmpty(value1) && !ObjectUtils.isEmpty(value2) && value1>value2;
	}
   
   /**
    * 
    * @param value1
    * @param value2
    * @return status to indicate first double less than second
    */
   public static boolean firstLessThanSecond(Double value1, Double value2) {
		
		return !ObjectUtils.isEmpty(value1) && !ObjectUtils.isEmpty(value2) && value1<value2;
	}
   
   /**
    * 
    * @param valueList
    * @param presentList
    * @return status to indicate if no value from presentList is present in valueList
    */
   public static boolean matchNoValuePresentInListIfPresent(List<String> valueList, List<String> presentList) {
		
		return CollectionUtils.isEmpty(valueList) || !anyValuePresentInList(valueList, presentList);
	}
   
   /**
    * 
    * @param valueList
    * @param presentList
    * @return status to indicate if any value from presentList is present in valueList
    */
   public static boolean matchAnyValuePresentInListIfPresent(List<String> valueList, List<String> presentList) {
		
		return CollectionUtils.isEmpty(valueList) || anyValuePresentInList(valueList, presentList);
	}
   
   /**
    * 
    * @param value1
    * @param value2
    * @return sum of two integer values
    */
   public static Integer addTwoIntegers(Integer value1, Integer value2) {
		
		return getIntegerValue(value1) + getIntegerValue(value2);
		
	}
   
   /**
    * 
    * @param value
    * @return integer value or 0
    */
   public static Integer getIntegerValue(Integer value) {
		
		return !ObjectUtils.isEmpty(value) ? value : OffersConfigurationConstants.ZERO_INTEGER;
	}
   
   /**
    * 
    * @param valueList
    * @return list or null
    */
   public static List<String> getListOrNull(List<String> valueList) {
		
		return !CollectionUtils.isEmpty(valueList)
			? valueList : null;		
	}
   
   /**
    * 
    * @param value
    * @return integer value or null
    */
   public static Integer getIntegerValueOrNull(Integer value) {
		
		return !ObjectUtils.isEmpty(value) ? value : null;
	}
   
   /**
    * 
    * @param dateString
    * @param hhmmss
    * @return date converted from string with time set
    * @throws ParseException
    */
   public static Date changeStringToDateWithTimeFormat(String dateString, String hhmmss) throws ParseException {
		
		Date date = changeStringToDate(dateString, OfferConstants.TRANSACTIONS_DATE_FORMAT.get());
		return setTimeInDate(date, hhmmss);
	     
   }

   /**
    * 
    * @param dateString
    * @param hhmmss
    * @return date converted to string format with time and timezone set
    * @throws ParseException
    */
   public static Date changeStringToDateWithTimeFormatAndTimeZone(String dateString, String hhmmss) throws ParseException {
		
		Date date = changeStringToDateWithTimeZone(dateString, OfferConstants.TRANSACTIONS_DATE_FORMAT.get());
		return setTimeInDate(date, hhmmss);
	     
   }
   
   /**
    * 
    * @param date
    * @param hhmmss
    * @return date with time values set as in input
    */
	public static Date setTimeInDate(Date date, String hhmmss) {
	 
		Date newDate = null;
		
		if(!ObjectUtils.isEmpty(date)) {
			
			String[] parts = hhmmss.split(OfferConstants.DATE_SEPARATOR.get());
		    Calendar cal = Calendar.getInstance();   
		    cal.setTime(date);
		    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
		    cal.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
		    cal.set(Calendar.SECOND, Integer.parseInt(parts[2]));
		    newDate = cal.getTime();
		}
		
	    return newDate;
	}

	/**
	 * 
	 * @param value
	 * @return negative value of an integer
	 */
	public static Integer getNegativeIntegerValue(Integer value) {
		
		return !ObjectUtils.isEmpty(value)
			&& value>0	
			 ? -1 * value
			 : value;
	}
	
	/**
	 * 
	 * @param value
	 * @return positive value of an integer
	 */
	public static Integer getPositiveIntegerValue(Integer value) {
		
		return !ObjectUtils.isEmpty(value)
			&& value<0	
			 ? -1 * value
			 : value;
	}

	/**
	 * 
	 * @param date
	 * @return date with current year set in the date
	 */
	public static Date setCurrentYear(Date date) {
		
		if(!ObjectUtils.isEmpty(date)) {
			Calendar cal = Calendar.getInstance();
			Integer year = cal.get(Calendar.YEAR);
		    cal.setTime(date);
			cal.set(year, cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			date = cal.getTime();
		}
		
		return date;
	}
	
	/**
	 * 
	 * @param list
	 * @return maximum value in integer list
	 */
	public static Integer getMaxValueInIntegerList(List<Integer> list) {
		
		return CollectionUtils.isNotEmpty(list) ? Collections.max(list) : null;
	}
	
	/**
	 * 
	 * @param originalDate
	 * @return date with UTC time
	 * @throws ParseException
	 */
	public static Date setUtcTimeZone(Date originalDate, String format) {
		
		 Date convertedDate = null;
		 
		 if(!ObjectUtils.isEmpty(originalDate)) {
			 
			 SimpleDateFormat sdf = new SimpleDateFormat(format);
			 sdf.setLenient(false);
	         sdf.setTimeZone(TimeZone.getTimeZone(OfferConstants.UTC_TIME_ZONE.get()));
	         String validDOB = sdf.format(originalDate);
	         try {
				convertedDate = sdf.parse(validDOB);
			} catch (ParseException e) {
				
				LOG.info("Error Occured while setting time zone in date");
				e.printStackTrace();
			}
		 }
		 
		 return convertedDate;
		
	}
	/**
	 * 
	 * @param originalDate
	 * @return date with GST time
	 * @throws ParseException
	 */
	public static Date setGstTimeZone(Date originalDate, String format) throws ParseException {
		
		 Date convertedDate = null;
		 
		 if(!ObjectUtils.isEmpty(originalDate)) {
			 
			 SimpleDateFormat sdf = new SimpleDateFormat(format);
			 sdf.setLenient(false);
	         sdf.setTimeZone(TimeZone.getTimeZone(OfferConstants.GST_TIME_ZONE.get()));
	         String validDOB = sdf.format(originalDate);
	         convertedDate = sdf.parse(validDOB);
		 }
		 
		 return convertedDate;
		
	}
	
	/**
	 * 
	 * @return date with specific time format set
	 */
	public static Date createDateInSpecificFormat(Date date, String format) {
		
		if(ObjectUtils.isEmpty(date)) {
			
			date = Calendar.getInstance().getTime();
		} 
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			
			date = sdf.parse(getDateInStringFormat(date, format));
		
		}catch (ParseException e) {
			e.printStackTrace();
			LOG.info("Error in parsing date : {}", e.getMessage());
		}
		
		return date;
	}
	
	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateInStringFormat(Date date, String format) {
		
		if(ObjectUtils.isEmpty(date)) {
			
			date = Calendar.getInstance().getTime();
		} 

		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
		
	}
	
	/**
	 * 
	 * @return first day of year
	 */
	public static Date getFirstDayOfCurrentYear() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), 1, 1);
		return getDateWithoutTimeStamp(cal.getTime());
	}
	
	/**
	 * 
	 * @return first day of month
	 */
	public static Date getFirstDayOfCurrentMonth() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
		return getDateWithoutTimeStamp(cal.getTime());
	}
	
	/**
	 * 
	 * @return first day of week
	 */
	public static Date getFirstDayOfCurrentWeek() {
		
		Calendar cal = Calendar.getInstance();
	    while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	    	cal.add(Calendar.DATE, -1);
	    }
	    return getDateWithoutTimeStamp(cal.getTime());
	}
	
	/**
	 * 
	 * @return current date without timestamp
	 */
	public static Date getCurrentDateWithoutTimeStamp() {
		
		Calendar cal = Calendar.getInstance();
		return getDateWithoutTimeStamp(cal.getTime());
		
	}
	
	
	/**
	 * 
	 * @param date
	 * @return date without timestamp
	 */
	public static Date getDateWithoutTimeStamp(Date date) {
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(!ObjectUtils.isEmpty(date)
        		? date : null);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
		
	}
	
	/**
	 * 
	 * @param paymentMethodList
	 * @return list with distinct values
	 */
	public static List<String> convertListToDistinctValueList(List<String> list) {
		
		return !CollectionUtils.isEmpty(list) 
				? list.stream().distinct().collect(Collectors.toList())
				: list ;
	}
	
}
