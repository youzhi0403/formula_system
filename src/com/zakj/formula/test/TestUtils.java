package com.zakj.formula.test;

import java.math.BigDecimal;

import org.junit.Test;

import com.zakj.formula.utils.UnitHandler;

public class TestUtils {

	@Test
	public void test(){
		System.out.println(UnitHandler.translate("吨", "千克", new BigDecimal("1000")));
	}
	
}
