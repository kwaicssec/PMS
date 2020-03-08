package com.rzb.pms.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;

import com.rzb.pms.dto.DrugType;
import com.rzb.pms.dto.ExpireStatus;
import com.rzb.pms.dto.ReferenceType;
import com.rzb.pms.exception.CustomException;
import com.rzb.pms.rsql.SearchCriteria;
import com.rzb.pms.rsql.SearchKey;
import com.rzb.pms.rsql.SearchOperators;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseUtil {

	/**
	 * This method will take the sort criteria and process it accordingly Only
	 * Supported sort criteria are expiryDate, unitPrice, mrp and sort orders are
	 * ASC(Ascending) or DSC(Descending)
	 * 
	 * @param sort
	 * @return sortObj
	 */
	public static Sort getSortObject(String sort) {
		Sort sortObj = null;
		String[] sorts = sort.split(",");
		for (String s : sorts) {
			if (s.split(":").length != 2) {
				log.error("Please give a proper sort argument", HttpStatus.NOT_FOUND);
				throw new CustomException("Please give a proper sort argument", HttpStatus.NOT_FOUND);
			}
			String sortBy = s.split(":")[0];
			String sortOrder = s.split(":")[1];

			if (sortBy.equalsIgnoreCase("expiryDate") || sortBy.equalsIgnoreCase("unitPrice")
					|| sortBy.equalsIgnoreCase("mrp")) {
				if ("ASC".equals(sortOrder) || "DESC".equals(sortOrder)) {
					if (sortObj == null) {
						sortObj = Sort.by(Direction.fromString(sortOrder), sortBy);
					} else {
						sortObj = sortObj.and(Sort.by(Direction.fromString(sortOrder), sortBy));
					}
				} else {
					log.error("Please give a proper sort Order like ASC(Ascending) or DSC(Descending) ",
							HttpStatus.NOT_FOUND);
					throw new CustomException("Please give a proper sort Order like ASC(Ascending) or DSC(Descending) ",
							HttpStatus.NOT_FOUND);

				}
			} else if (sortBy.equalsIgnoreCase("createdDate") || sortBy.equalsIgnoreCase("updatedDate")) {

				if ("ASC".equals(sortOrder) || "DESC".equals(sortOrder)) {
					if (sortObj == null) {
						sortObj = Sort.by(Direction.fromString(sortOrder), sortBy);
					} else {
						sortObj = sortObj.and(Sort.by(Direction.fromString(sortOrder), sortBy));
					}
				} else {
					log.error("Please give a proper sort Order like ASC(Ascending) or DSC(Descending) ",
							HttpStatus.NOT_FOUND);
					throw new CustomException("Please give a proper sort Order like ASC(Ascending) or DSC(Descending) ",
							HttpStatus.NOT_FOUND);

				}

			} else {
				log.error("Please give a proper sort argument Like expiryDate, unitPrice, mrp",
						HttpStatus.NOT_FOUND);
				throw new CustomException("Please give a proper sort argument Like expiryDate, unitPrice, mrp",
						HttpStatus.NOT_FOUND);
			}
		}
		return sortObj;
	}

	/*
	 * This method will take custom queryParam of the form (Key Operator Value) eg:
	 * company==cipla . queryParamwill be processed into SearchCriteria members like
	 * key(company), Operation(==), Value(cipla) based on the Search Key and
	 * Operator. Currently supported Search keys are genericanme, brandName,
	 * company, composition, location and Supported Search operators are ==, >=,=lk=
	 * ,<
	 */
	// "id=bt=(2,4)";// id>=2 && id<=4 //between
	public static SearchCriteria getCriteria(String queryParam) {
		Object value = null;
		String key, operator = null;
		String[] o = queryParam.split("==|>=|=lk=|<|=bt=");
		if (o.length < 2) {
			log.error("Please provide proper search Criteria, Supported operators are ==, >=,=lk= ,< ",
					HttpStatus.BAD_REQUEST);
			throw new CustomException("Please provide proper search Criteria, Supported operators are ==, >=,=lk= ,< ",
					HttpStatus.BAD_REQUEST);
		}
		operator = queryParam.substring(o[0].length(), queryParam.length() - o[1].length()).replaceAll("\\s+", "");
		if (operator.equalsIgnoreCase("=bt=")) {

			value = null;
		}
		value = o[1].trim().replaceAll("\\s+", " ");
		key = o[0].replaceAll("\\s+", "");

		switch (SearchKey.getKeyData(key)) {

		case DRUG_NAME: {
			if (!(SearchOperators.LIKE.getName().equals(operator)
					|| SearchOperators.EQUALITY.getName().equals(operator))) {
				log.error("Please provide proper search params, brandName will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
				throw new CustomException("Please provide proper search params, brandName will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
			} else {
				return new SearchCriteria(key, operator, value);
			}
		}

		case GENERIC_NAME: {
			if (!(SearchOperators.LIKE.getName().equals(operator)
					|| SearchOperators.EQUALITY.getName().equals(operator))) {
				log.error("Please provide proper search params, genericName will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
				throw new CustomException(
						"Please provide proper search params, genericName will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
			} else {
				return new SearchCriteria(key, operator, value);
			}
		}

		case COMPANY: {
			if (!(SearchOperators.LIKE.getName().equals(operator)
					|| SearchOperators.EQUALITY.getName().equals(operator))) {
				log.error("Please provide proper search params, company will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
				throw new CustomException("Please provide proper search params, company will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
			} else {
				return new SearchCriteria(key, operator, value);
			}
		}
		case COMPOSITION: {
			if (!(SearchOperators.LIKE.getName().equals(operator)
					|| SearchOperators.EQUALITY.getName().equals(operator))) {
				log.error("Please provide proper search params, composition will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
				throw new CustomException(
						"Please provide proper search params, composition will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
			} else {
				return new SearchCriteria(key, operator, value);
			}
		}

		case LOCATION: {
			if (!(SearchOperators.LIKE.getName().equals(operator)
					|| SearchOperators.EQUALITY.getName().equals(operator))) {
				log.error("Please provide proper search params, location will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
				throw new CustomException("Please provide proper search params, location will only support =lk= OR ==",
						HttpStatus.BAD_REQUEST);
			} else {
				return new SearchCriteria(key, operator, value);
			}

		}
		default:
			log.error("Please Provide right search parameter . Like genericanme, brandName, company, composition, "
					+ "location, cretedBy, updatedBy, createdDate, updatedDate", HttpStatus.BAD_REQUEST);
			throw new CustomException(
					"Please Provide right search parameter . Like genericanme, brandName, company, composition, "
							+ "location, cretedBy, updatedBy, createdDate, updatedDate",
					HttpStatus.BAD_REQUEST);
		}
	}

	public static float calculatePriceAfterDiscount(float mrp, float discount, float itemSellPriceBeforeDiscount) {

		return ((100 - discount) * itemSellPriceBeforeDiscount) / 100;
	}

	public static String stripTrailingZero(String s) {

		return s.replaceAll("()\\.0+$|(\\..+?)0+$", "$2");

	}

	public static String getRandomReference(String type) {

		if (ReferenceType.PO.toString().equalsIgnoreCase(type)) {
			return "PO-" + RandomStringUtils.randomAlphabetic(4);
		} else if (ReferenceType.SELL.toString().equalsIgnoreCase(type)) {
			return "SL-" + RandomStringUtils.randomAlphabetic(4);
		} else {
			return "";
		}

	}

	public static String findQntyInWord(Double avlQntyInWhole, String drugForm) {

		String param = String.valueOf(avlQntyInWhole);
		String LHS = param.split("\\.")[0];
		String RHS = param.split("\\.")[1];
		String lhsDrugForm = null;

		if (drugForm.equalsIgnoreCase(DrugType.CAPSULE.toString())
				|| drugForm.equalsIgnoreCase(DrugType.TABLET.toString())) {

			lhsDrugForm = DrugType.STRIP.toString();
			if (Integer.valueOf(LHS) == 0) {
				String result = RHS + " " + getSuffix(drugForm);
				return result;
			} else {
				String result = LHS + " " + lhsDrugForm + " " + RHS + " " + getSuffix(drugForm);
				return result;
			}

		} else {
			lhsDrugForm = drugForm;
			String result = LHS + " " + lhsDrugForm;
			return result;
		}

	}

	public static String getSuffix(String drugForm) {

		if (drugForm.equalsIgnoreCase(DrugType.TABLET.toString())) {

			return DrugType.TABLET.toString();

		} else if (drugForm.equalsIgnoreCase(DrugType.CAPSULE.toString())) {

			return DrugType.CAPSULE.toString();
		} else {
			return drugForm;
		}

	}

	public static String remainingExpireTime(LocalDate expiryDate) {

		LocalDate expDate = LocalDate.of(expiryDate.getYear(), expiryDate.getMonthValue(), expiryDate.getDayOfMonth());
		LocalDate now = LocalDate.now();
		Period diff = Period.between(expDate, now);

		return "Expiring in" + ": " + diff.getYears() + " " + "Years, " + diff.getMonths() + ", Months, "
				+ diff.getDays();

	}

	public static String getExpireStatus(LocalDate expiryDate) {

		String status = null;
		if (expiryDate.isBefore(LocalDate.now())) {
			status = ExpireStatus.EXPIRED.toString();
		} else if (expiryDate.isAfter(LocalDate.now())) {
			status = ExpireStatus.ABOUT_TO_EXPIRE.toString();
		}
		return status;
	}

	public static boolean isNullOrZero(final Object obj) {

		if (null == obj)
			return true;

		if (obj instanceof Integer) {
			Integer i = (Integer) obj;
			return (i == 0);
		}

		if (obj instanceof Long) {
			Long l = (Long) obj;
			return (l == 0);
		}
		if (obj instanceof Float) {
			Float l = (Float) obj;
			return (l == 0);
		}

		if (obj instanceof String) {
			String str = (String) obj;

			return (str == null && str.isEmpty());

		}
		if (obj instanceof Date) {
			Date date = (Date) obj;

			return (date == null);
		}
		return false;

	}

	public static Float calculateGSTammount(Float originalCost, Float percentage) {

		return (originalCost * percentage) / 100;

	}

	public static Float calculateGSTpercentage(Float gstAmmount, Float originalCost) {

		return (gstAmmount * 100) / originalCost;

	}

	public static Float calculateNetPriceAfterGST(Float originalCost, Float percentage) {

		return originalCost + calculateGSTammount(originalCost, percentage);
	}

	public static LocalDate convertToLocalDateTimeFromDate(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
		return java.sql.Date.valueOf(dateToConvert);
	}
}
