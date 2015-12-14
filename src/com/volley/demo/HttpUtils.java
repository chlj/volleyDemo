package com.volley.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtils {
	private  static String URL_PATH = "";// ��������ͼƬ��·��

	public HttpUtils() {

	}

	
	public static InputStream getImageViewInputStream(String path) throws IOException {
		InputStream inputStream = null;
		URL url = new URL(path);
		if (url != null) {
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

			httpURLConnection.setConnectTimeout(3000); // �������ӳ�ʱ��ʱ��

			httpURLConnection.setRequestMethod("GET"); // ���󷽷�

			httpURLConnection.setDoInput(true);// ��������

			int response_code = httpURLConnection.getResponseCode();

			if (response_code == 200) {
				inputStream = httpURLConnection.getInputStream();
			}
		}
		return inputStream;
	}

	
}
