package com.volley.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtils {
	private  static String URL_PATH = "";// 访问网络图片的路径

	public HttpUtils() {

	}

	
	public static InputStream getImageViewInputStream(String path) throws IOException {
		InputStream inputStream = null;
		URL url = new URL(path);
		if (url != null) {
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

			httpURLConnection.setConnectTimeout(3000); // 设置连接超时的时间

			httpURLConnection.setRequestMethod("GET"); // 请求方法

			httpURLConnection.setDoInput(true);// 打开输入流

			int response_code = httpURLConnection.getResponseCode();

			if (response_code == 200) {
				inputStream = httpURLConnection.getInputStream();
			}
		}
		return inputStream;
	}

	
}
