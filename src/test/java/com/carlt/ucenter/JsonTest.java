package com.carlt.ucenter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonTest {
	
	public static void main(String[] args) {
		
		try {
			JSONObject json = new JSONObject("{'a':1}");
			System.out.println(json.toString());
			

			Map<String, Object> ret = new HashMap<>();
			ret.put("code", "1001");
			ret.put("msg", "authinfo is empty");
			
			System.out.println(JSONObject.wrap(ret));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
