package com.zakj.formula.utils;

import java.math.BigDecimal;

public class UnitHandler {
	
	public final static String GRAM = "g";
	public final static String KILOGRAM = "kg";
	public final static String TON = "t";
	
	public static BigDecimal translate(String orign, String final1, BigDecimal orignPrice){
		switch (orign) {
		case GRAM:
			return final1.equals(GRAM) ? orignPrice : (final1.equals(KILOGRAM) ? orignPrice.multiply(new BigDecimal("1000")) : orignPrice.multiply(new BigDecimal("1000000")));
		case KILOGRAM:
			return final1.equals(GRAM) ? orignPrice.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_EVEN) : (final1.equals(KILOGRAM) ? orignPrice : orignPrice.multiply(new BigDecimal("1000")));
		case TON:
			return final1.equals(GRAM) ? orignPrice.divide(new BigDecimal("1000000"),2, BigDecimal.ROUND_HALF_EVEN) : (final1.equals(KILOGRAM) ? orignPrice.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_EVEN) : orignPrice);
		}
		return new BigDecimal("0");
	}
}
