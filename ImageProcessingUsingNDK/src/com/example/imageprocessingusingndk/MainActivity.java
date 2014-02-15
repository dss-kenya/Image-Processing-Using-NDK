package com.example.imageprocessingusingndk;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.imageprocessingusingndk.exception.MyException;

public class MainActivity extends Activity {
	private ImageView mImgView;
	private Bitmap mBmp;
	private Drawable mImgDrawable;
	private RelativeLayout mRelLayout;
	private int width, height;
	
	static {
		System.loadLibrary("ImageProcessingUsingNDK");
	}
	
	public native Bitmap convertToGray(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap convertToRed(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap warmifyImage(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap convertToSepia(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap increaseBrightness(Bitmap bitmapIn,int direction);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImgView = (ImageView)findViewById(R.id.imgView);
		mRelLayout = (RelativeLayout)findViewById(R.id.relLayout);
		
		mImgDrawable = mImgView.getDrawable();
	    mBmp= ((BitmapDrawable)mImgDrawable).getBitmap();
	    
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    // Make sure it is 24 bit color as our image processing algorithm
	    // expects this format
	    options.inPreferredConfig = Config.ARGB_8888;
	    mBmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.bg,options);
	    
	    if(mBmp != null) {
	    	mImgView.setImageBitmap(mBmp);
	    }
	    
	   width = mBmp.getWidth();
	   height = mBmp.getHeight();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_red:
			convertImageToRed();
			break;
			
		case R.id.action_blue:
			
			break;
			
		case R.id.action_bright:
			increaseBrightness();
			break;
			
		case R.id.action_dull:
			
			break;
			
		case R.id.action_gray:
			convertImageToGray();
			break;
			
		case R.id.action_warmify:
			warmifyImage();
			break;
			
		case R.id.action_green:
		
			break;
			
		case R.id.action_reset:
			resetImage();
			break;
			
		case R.id.action_sepia:
			convertImageToSepia();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void increaseBrightness() {
		Bitmap bitmapWip = 
				Bitmap.createBitmap(width,height, mBmp.getConfig());
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		increaseBrightness(bitmapWip,1);
		mImgView.setImageBitmap(bitmapWip);
	}
	
	private void convertImageToGray() {
		Bitmap bitmapWip = 
				Bitmap.createBitmap(width,height, Bitmap.Config.ALPHA_8);
		
		try {
			mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
			convertToGray(mBmp, bitmapWip, MyException.class);
			mImgView.setImageBitmap(bitmapWip);
		}catch(final MyException e) {
			Handler handler = new Handler();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(MainActivity.this,e.getMessage() , Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	private void convertImageToRed() {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		try {
			Bitmap bitmapWipOther = 
					Bitmap.createBitmap(width,height, 
							mBmp.getConfig());
			
			convertToRed(mBmp, bitmapWipOther, MyException.class);
			
			mImgView.setImageBitmap(bitmapWipOther);
		}catch(final MyException e) {
			Handler handler = new Handler();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(MainActivity.this,e.getMessage() , Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	private void warmifyImage() {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		try {
			Bitmap bitmapWipOther = 
					Bitmap.createBitmap(width,height, 
							mBmp.getConfig());
			
			warmifyImage(mBmp, bitmapWipOther, MyException.class);
			
			mImgView.setImageBitmap(bitmapWipOther);
		}catch(final MyException e) {
			Handler handler = new Handler();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(MainActivity.this,e.getMessage() , Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	private void convertImageToSepia() {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		try {
			Bitmap bitmapWipOther = 
					Bitmap.createBitmap(width,height, 
							mBmp.getConfig());
			
			convertToSepia(mBmp, bitmapWipOther, MyException.class);
			
			mImgView.setImageBitmap(bitmapWipOther);
		}catch(final MyException e) {
			Handler handler = new Handler();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(MainActivity.this,e.getMessage() , Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	
	private void resetImage() {
		mImgView.setImageBitmap(mBmp);
	}
}
