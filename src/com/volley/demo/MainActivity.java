package com.volley.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn1, btn2, btn3, btn4;
	private String title="";
	private String url="";
	private String content="";
	

	private EditText edt_title,edt_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
	}

	public void findView() {
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);

		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);

		
		edt_title =(EditText) findViewById(R.id.title);
		edt_content =(EditText) findViewById(R.id.content);
		
		edt_title.setText(R.string.defatlut_title);
		edt_content.setText(R.string.defatlut_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn1:
			shareS(getString(R.string.share_url1));
			break;
		case R.id.btn2:
			shareS(getString(R.string.share_url2));
			break;
		case R.id.btn3:
			shareS(getString(R.string.share_url3));
			break;
		case R.id.btn4:
			shareS(getString(R.string.share_url4));
			break;

		default:
			break;
		}

	}
	
	public void shareS(String str_url){
		title=edt_title.getText().toString().trim();
		content=edt_content.getText().toString().trim();
		url=str_url;
		
		
		if("".equals(title)){
			title=getString(R.string.defatlut_title);
		}
		if("".equals(content)){
			content=getString(R.string.defatlut_content);
		}
		share(title, "", str_url, "");
	}
	/**
	 * 
	 * @param title
	 * @param pic
	 * @param url
	 * @param content
	 */
	
	
	private void share(final String title, String pic, final String url, final String content) {

		if ("".equals(pic)) {
			pic = "http://3d414.com/Tpl/default/images/view/logo.png";

		}

		final String pics = pic;

		ImageView img = new ImageView(MainActivity.this);
		
		new MyTask2().execute(new Object[] { pic, img });
		
	}
	
	
	public class MyTask2 extends AsyncTask<Object, Integer, Void> {
		@Override
		protected Void doInBackground(Object... params) {
			InputStream inputStream;
			try {
				inputStream = HttpUtils.getImageViewInputStream(String.valueOf(params[0]));
				Bitmap drawable = BitmapFactory.decodeStream(inputStream);

				Message msg = Message.obtain();
				msg.what = 1;
				msg.obj = new Object[] { params[1], drawable };
				mHandler.sendMessage(msg);

				if (drawable == null) {
					Log.i("xx", "==========is null");
				} else {
					Log.i("xx", "==========is not null");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}


	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				Object[] obj = (Object[]) msg.obj;
				ImageView img = (ImageView) obj[0];
				Bitmap bmp = (Bitmap) obj[1];

				
				saveBitmap (bmp,"test.png","cl");
				
				String local_imgPath=Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator +"cl/test.png";


				try {
					OnekeyShare oks = new OnekeyShare();
					oks.setAddress("");
					oks.setTitle(title);
					oks.setTitleUrl(url);
					oks.setText(content);
					oks.setImagePath(local_imgPath);
					oks.setImageUrl(local_imgPath);
					oks.setUrl(url);
					oks.setSite(getString(R.string.app_name));
					oks.setSiteUrl(url);
					oks.setSilent(true);

					oks.setCallback(new PlatformActionListener() {

						@Override
						public void onError(Platform arg0, int arg1, Throwable arg2) {

							 Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_LONG).show();

						}

						@Override
						public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                               Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_LONG).show();
						}

						@Override
						public void onCancel(Platform arg0, int arg1) {

						}
					});
					oks.show(MainActivity.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			

			}
		}

	};
	
	
	
	/**
	 * 判断SD卡是否存在，并且是否具有读写权限
	 * 
	 * @return 存在返回true，不存在返回false
	 */
	public static Boolean IsExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;

		}

	}
	
	
	/***
	 * 将BitMap转换为图片保存到SD卡中去
	 * 
	 * @param bmp
	 * @param fileName
	 * @param rootdir
	 */
	public Boolean saveBitmap(Bitmap bmp, String fileName, String rootdir) {

		Boolean bool = false;
		if (IsExist()) {
			if (bmp != null) {
				File dir = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + File.separator + rootdir);
				if (!dir.exists())
					dir.mkdir();
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(new File(dir, fileName));
					bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
					out.flush();
					out.close();
					bool = true;

				} catch (FileNotFoundException e) {

				} catch (IOException e) {

				}
			}
		}
		return bool;
	}
	
	

	
}
