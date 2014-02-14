package com.azilen.firstandroidndkproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {
	static {
		System.loadLibrary("FirstAndroidNDKProject");
	}
	
	public native int addTwoNumbers(int x,int y);
	public native int divideByZero(float z, int y, Class<?> classObj) throws MyException;
	public native String getStringFromJNI(String str);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String str = null;
		
		//Log.e("dhara"," getStringFromJNI: " +  getStringFromJNI(str));
		
		try {
			getStringFromJNI(str);
		}catch(Exception e) {
			e.printStackTrace();
			Log.e("dhara", "e : " + e.getMessage());
		}
		
		int x= addTwoNumbers(10,20);
		Log.e("dhara"," value of x : " + x);

		try {
			float y = divideByZero(100, 0, MyException.class);
			Log.e("dhara", "value of y inside try " + y);
		}catch(final MyException e) {
			Handler handler = new Handler();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	static class MyException extends Exception {
		private String message;
		
		public String getMessage() {
			return message;
		}
		
		public MyException(String msg) {
			message = msg;
		}
	}
}
