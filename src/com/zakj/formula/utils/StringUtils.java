package com.zakj.formula.utils;

import org.junit.Test;

public class StringUtils {
	
	//
	public static boolean isEmpty(String text, boolean isTrim){
		if (text == null || "".equals(isTrim ? text.trim() : text))
			return true;
		return false;
		
	}
	
	@Test
	public void test(){
		System.out.println(StringUtils.isEmpty(" ", false));
	}
}
