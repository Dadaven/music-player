package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class HttpUtils {

	/** 根据地址获取RSS数据源
	 * @param url
	 * @return
	 */
	//private static String strr="123456";
	private static InputStream in = null;
	public static InputStream doGet(String url){
		try {
			URL str = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) str.openConnection();
			if(conn.getResponseCode()==200){
				in = conn.getInputStream();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
	}
	
}
