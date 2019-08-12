package com.zakj.formula.exception;

import java.util.HashMap;

public class CustomException extends Exception {

	private static final long serialVersionUID = 2L;
	
	HashMap<Integer, String> msgMap = null;
	{
		msgMap = new HashMap<Integer, String>();
		msgMap.put(SQL_EXCEPTION_UNKNOWN, "未知错误！");
		msgMap.put(SQL_EXCEPTION_ADD, "添加失败，请重试！");
		msgMap.put(SQL_EXCEPTION_UPDATE, "更新失败，请重试！");
		msgMap.put(SQL_EXCEPTION_DELETE, "删除失败，请重试！");
		msgMap.put(SQL_EXCEPTION_SHOW, "获取数据失败，请重试！");
		
		msgMap.put(SQL_EXCEPTION_EXCEL_PARSE_FAILURE, "excel解析失败！");
		msgMap.put(SQL_EXCEPTION_EXCEL_IINPUT_DB_FAILURE, "excel数据存入数据库失败！");
		
		msgMap.put(SQL_EXCEPTION_EXCEL_JSON_PARSE_FAILURE, "json格式错误！");
		msgMap.put(SQL_EXCEPTION_EXCEL_JSON_NULL, "json串为空！");
	}
	
	public static int SQL_EXCEPTION_UNKNOWN = 1001;
	public static int SQL_EXCEPTION_ADD = 1002;
	public static int SQL_EXCEPTION_UPDATE = 1004;
	public static int SQL_EXCEPTION_DELETE = 1008;
	public static int SQL_EXCEPTION_SHOW = 1032;
	public static int SQL_EXCEPTION_ROLLBACK = 1064;
	public static int SQL_EXCEPTION_EXCEL_PARSE_FAILURE = 1128;
	public static int SQL_EXCEPTION_EXCEL_IINPUT_DB_FAILURE = 1256;
	public static int SQL_EXCEPTION_EXCEL_JSON_PARSE_FAILURE = 1512;
	public static int SQL_EXCEPTION_EXCEL_JSON_NULL = 2024;
	
	
	private int code = 201;
	private String msg;
	
	public CustomException(int code) {
		this.code = code;
	}
	
	public CustomException(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		if(msg == null){
			return msgMap.get(code);
		} else {
			return msg;
		}
	}
	
	public int getCode(){
		return code;
	}
}
