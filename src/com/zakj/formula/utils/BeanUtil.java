package com.zakj.formula.utils;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class BeanUtil {
	
	//将数据封装成bean返回一个bean
	public static <T> T populate(Class<T> clazz, Map map){
		T instance = null;
		try {
			instance = clazz.newInstance();
			BeanUtils.populate(instance, map);
			System.out.println(instance);
		} catch (Exception e) {
			System.out.println("bean转换异常:"+e.getMessage());
		}
		return instance;
	}
}
