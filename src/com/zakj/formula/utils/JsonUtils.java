package com.zakj.formula.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.zakj.formula.exception.CustomException;

public class JsonUtils {

	/**
	 * 解析json数据并封装到实体类中
	 * 
	 * @param clazz 实体类的class
	 * @param request 请求对象
	 * @param response  响应对象
	 * @param isShowJsonStr 是否打印json串
	 * @return
	 * @throws CustomException
	 */
	public static <T> T toObject(Class<T> clazz, HttpServletRequest request) throws CustomException {
		T t = null;
		try {
			Gson gson = new Gson();
			StringBuffer json = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			System.out.println(json);
			t = gson.fromJson(json.toString(), clazz);
			if (json.toString().length() != 0 && t == null) {
				throw new CustomException(CustomException.SQL_EXCEPTION_EXCEL_JSON_PARSE_FAILURE);
			}
			if(t == null) t = clazz.newInstance();
			System.out.println(t);
		} catch (JsonSyntaxException | JsonIOException e) {
			e.printStackTrace();
			throw new CustomException(
					CustomException.SQL_EXCEPTION_EXCEL_JSON_PARSE_FAILURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T toObject(Reader reader,Type type) throws CustomException {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(reader, type);
			if(t == null) t = (T) type.getClass().newInstance();
			System.out.println(t);
		} catch (JsonSyntaxException | JsonIOException e) {
			e.printStackTrace();
			throw new CustomException(
					CustomException.SQL_EXCEPTION_EXCEL_JSON_PARSE_FAILURE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
}
