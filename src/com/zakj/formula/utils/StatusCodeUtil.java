package com.zakj.formula.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StatusCodeUtil {
	
	public static String getJsonStr(int code, Object obj){
		JsonObject object = new JsonObject();
		object.add("code", new JsonParser().parse(new Gson().toJson(code)));
		object.add("data", new JsonParser().parse(new Gson().toJson(obj)));
		return new Gson().toJson(object);
	}
}
