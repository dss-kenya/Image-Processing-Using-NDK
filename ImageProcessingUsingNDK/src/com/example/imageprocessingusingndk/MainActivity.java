package com.example.imageprocessingusingndk;

import com.example.imageprocessingusingndk.exception.MyException;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView mImgView;
	private Bitmap mBmp;
	private Drawable mImgDrawable;
	
	static {
		System.loadLibrary("ImageProcessingUsingNDK");
	}
	
	public native Bitmap convertToGray(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mImgView = (ImageView)findViewById(R.id.imgView);
		
		mImgDrawable = mImgView.getDrawable();
	    mBmp= ((BitmapDrawable)mImgDrawable).getBitmap();
	    
	    //BitmapFactory.Options options = new BitmapFactory.Options();
	    // Make sure it is 24 bit color as our image processing algorithm
	    // expects this format
	    //options.inPreferredConfig = Config.ARGB_8888;
	    //mBmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.bg,options);
	    
	    //if(mBmp != null) {
	    //	mImgView.setImageBitmap(mBmp);
	    //}
	    
	    mImgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int width = mBmp.getWidth();
				int height = mBmp.getHeight();
				
				Bitmap bitmapWip = 
						Bitmap.createBitmap(width,height, Bitmap.Config.ALPHA_8);
				
				try {
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
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
