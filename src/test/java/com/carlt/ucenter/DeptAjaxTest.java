package com.carlt.ucenter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.carlt.ucenter.util.HttpUtils;

public class DeptAjaxTest {

	// static String BASE_URI = "http://127.0.0.1:9000/local/api/";
	static String BASE_URI = "http://127.0.0.1:9000/local/api/";

	public static void main(String[] args) throws Exception {

		// add("user/add");
		// get("login?userCode=1002&password=88888888");
		// get("user/2");
		// get("/logout");
		// get("json");
		// get("hello");
//		modify("dept/update");
		modify2("http://127.0.0.1:9000/local/api/dept/update");
		// delete("user/delete/11");
	}

	private static void get(String path) throws Exception {
		String urlStr = BASE_URI + path;
		URL url = new URL(urlStr);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Cookie", "SESSION=e88ce191-f172-4b7e-b0d6-1eae2a8c03d3");
		connection.connect();

		DataInputStream in = new DataInputStream(connection.getInputStream());

		int len = in.available();
		byte[] b = new byte[len];
		in.readFully(b, 0, len);

		System.out.println(new String(b));

		in.close();
	}

	private static void delete(String path) throws Exception {
		String urlStr = BASE_URI + path;
		URL url = new URL(urlStr);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.connect();

		DataInputStream in = new DataInputStream(connection.getInputStream());

		int len = in.available();
		byte[] b = new byte[len];
		in.readFully(b, 0, len);

		System.out.println(new String(b));

		in.close();
	}

	private static void modify2(String path) throws Exception {

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", "" + 4);
		paramMap.put("deptCode", "0104");
		paramMap.put("parDeptCode", "01");
		paramMap.put("deptName", "测试名称更新");
		paramMap.put("mark", "测试update");
		paramMap.put("updateTime", "" + System.currentTimeMillis());
		paramMap.put("createTime", "" + System.currentTimeMillis());

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Cookie", "SESSION=df1bac16-533c-404b-bc0c-88abb0efafeb");

		Object ret = HttpUtils.doPost(path, headers, paramMap);

		System.out.println(ret);

	}

	private static void modify(String path) throws Exception {
		String urlStr = BASE_URI + path;
		URL url = new URL(urlStr);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Cookie", "SESSION=df1bac16-533c-404b-bc0c-88abb0efafeb");
		connection.connect();

		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		JSONObject obj = new JSONObject();
		obj.put("id", 4);
		obj.put("deptCode", "0104");
		obj.put("parDeptCode", "01");
		obj.put("deptName", "测试名称更新");
		obj.put("mark", "测试update");
		obj.put("updateTime", System.currentTimeMillis());
		obj.put("createTime", System.currentTimeMillis());
		System.out.println(obj.toString());
		out.write(obj.toString().getBytes());
		out.flush();
		out.close();

		DataInputStream in = new DataInputStream(connection.getInputStream());

		int len = in.available();
		byte[] b = new byte[len];
		in.readFully(b, 0, len);

		System.out.println(new String(b));

		in.close();
	}

	private static void add(String path) throws Exception {
		String urlStr = BASE_URI + path;
		URL url = new URL(urlStr);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.connect();

		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		JSONObject obj = new JSONObject();
		obj.put("userName", "测试资源更新");
		obj.put("userCode", "1014");
		obj.put("password", "2");
		obj.put("deptCode", "2");
		obj.put("mark", "测试update");
		obj.put("updateTime", System.currentTimeMillis());
		obj.put("createTime", System.currentTimeMillis());
		System.out.println(obj.toString());
		out.write(obj.toString().getBytes());
		out.flush();
		out.close();

		DataInputStream in = new DataInputStream(connection.getInputStream());

		int len = in.available();
		byte[] b = new byte[len];
		in.readFully(b, 0, len);

		System.out.println(new String(b));

		in.close();
	}
}
