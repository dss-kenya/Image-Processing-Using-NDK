package com.example.imageprocessingusingndk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.imageprocessingusingndk.exception.MyException;

public class MainActivity extends Activity {
	private ImageView mImgView;
	private Bitmap mBmp;
	private Drawable mImgDrawable;
	private RelativeLayout mRelLayout;
	private int width, height;
	private float brightnessValue;
	
	static {
		System.loadLibrary("ImageProcessingUsingNDK");
	}
	
	public native Bitmap convertToGray(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap convertToRed(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap warmifyImage(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap convertToSepia(Bitmap bitmapIn, Bitmap bitmapOut, Class<?> classObj) throws MyException;
	public native Bitmap increaseBrightness(Bitmap bitmapIn, float brightnessValue);
	public native Bitmap reduceBrightness(Bitmap bitmapIn, float brightnessValue);
	public native Bitmap convertToBlue(Bitmap bitmapIn, Bitmap bitmapOut);
	public native Bitmap convertToGreen(Bitmap bitmapIn, Bitmap bitmapOut);
	public native Bitmap invertImage(Bitmap bitmapIn);
	
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
			convertImageToBlue();
			break;
			
		case R.id.action_bright:
			showDialog();
			break;
			
		case R.id.action_dull:
			reduceBrightness(2.5f);
			break;
			
		case R.id.action_gray:
			convertImageToGray();
			break;
			
		case R.id.action_warmify:
			warmifyImage();
			break;
			
		case R.id.action_green:
			convertImageToGreen();
			break;
			
		case R.id.action_reset:
			resetImage();
			break;
			
		case R.id.action_sepia:
			convertImageToSepia();
			break;
			
		case R.id.action_invert:
			invertImage();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Inverts the current image
	 */
	private void invertImage() {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		Bitmap bitmapWip = mBmp.copy(Config.ARGB_8888, true);
		invertImage(bitmapWip);
		mImgView.setImageBitmap(bitmapWip);
	}
	
	private void convertImageToBlue() {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		Bitmap bitmapWip = Bitmap.createBitmap(width, height, mBmp.getConfig());
		convertToBlue(mBmp, bitmapWip);
		mImgView.setImageBitmap(bitmapWip);
	}
	
	private void convertImageToGreen() {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		Bitmap bitmapWip = Bitmap.createBitmap(width, height, mBmp.getConfig());
		convertToGreen(mBmp, bitmapWip);
		mImgView.setImageBitmap(bitmapWip);
	}
	
	/**
	 * Increasing brightness of the image
	 */
	private void increaseBrightness(float value) {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		Bitmap bitmapWip = mBmp.copy(Config.ARGB_8888, true);
		increaseBrightness(bitmapWip, value);
		mImgView.setImageBitmap(bitmapWip);
	}
	
	private void reduceBrightness(float value) {
		mRelLayout.setBackgroundColor(getResources().getColor(android.R.color.black));
		Bitmap bitmapWip = mBmp.copy(Config.ARGB_8888, true);
		reduceBrightness(bitmapWip, value);
		mImgView.setImageBitmap(bitmapWip);
	}
	
	private void showDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
		.setPositiveButton(getString(R.string.set), null)
		.setNegativeButton(getString(R.string.cancel), null)
		.create();

		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.brightness_view, null);
		dialog.setView(view);

		final SeekBar brightnessSeekBar = (SeekBar)view.findViewById(R.id.brightnessSeekBar);

		dialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogObj) {
				Button btnSet = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				btnSet.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						brightnessValue = brightnessSeekBar.getProgress() / 10.0f;

						if(brightnessValue <= 0) {
							brightnessValue = 1.0f;
						}
						dialog.dismiss();
						increaseBrightness(brightnessValue);
					}
				});

				Button btnCancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
				btnCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
		});
		
		dialog.show();
	}
	
	/**
	 * Converts image to gray
	 */
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
	
	/**
	 * Converts image to red scale
	 */
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
	
	/**
	 * Adds warmth to the image
	 */
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
	
	/**
	 * Converts image to sepia looks
	 */
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
	
	/**
	 * Sets image back to the original image
	 */
	private void resetImage() {
		mImgView.setImageBitmap(mBmp);
	}
}
