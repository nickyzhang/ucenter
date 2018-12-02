package com.carlt.ucenter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.carlt.ucenter.util.HttpUtils;

public class DownloadTest {

	public static void main(String[] args) throws Exception {

		get();
		
		post();
	}

	private static void get() throws Exception {
		String uri = "http://162.16.109.102:9000/backbond/api/v1/rpt/risk/exportQuarter3Report?startDt=20180101&endDt=20180131";
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Access-Token", "token#1#78221278318592");
		 ByteArrayOutputStream bio = (ByteArrayOutputStream)
		 HttpUtils.doGetForStream(uri, headerMap);
		 System.out.println(new String(bio.toByteArray(), "utf-8"));
		bio.close();
	}

	private static void post() throws Exception {
		
		String uri = "http://162.16.109.102:9000/backbond/api/v1/rpt/risk/exportQuarter3Report";

		Map<String, String> headerMap = new HashMap<>();
		Map<String, String> paramMap = new HashMap<>();

		headerMap.put("Access-Token", "token#1#78221278318592");
		paramMap.put("startDt", "20180101");
		paramMap.put("endDt", "20180831");

		ByteArrayOutputStream bio = (ByteArrayOutputStream) HttpUtils.doPost(uri, headerMap, paramMap);
		FileOutputStream fos = new FileOutputStream("test.xlsx");
		fos.write(bio.toByteArray());
		fos.flush();
		fos.close();

		bio.close();
	}
}
