package com.carlt.ucenter;

import java.util.HashMap;
import java.util.Map;

import com.carlt.ucenter.util.HttpUtils;

public class HttpUtilsTester {

	public static void main(String[] args) throws Exception {

		System.out.println(HttpUtils.doGet("http://localhost:8081/user/add1?tel=1234567&password=8888"));

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("tel", "0103");
		paramMap.put("password", "01");
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		headerMap.put("Cookie", "SESSION=df1bac16-533c-404b-bc0c-88abb0efafeb");
		HttpUtils.doPost("http://localhost:8081/user/add1", headerMap, paramMap);
	}
}
