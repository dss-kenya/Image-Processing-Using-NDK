#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <coffeecatch.h>

#define APP_NAME 	"ImageProcessingUsingNDK"
#define LOG_E(...) 	__android_log_print(ANDROID_LOG_ERROR,APP_NAME,__VA_ARGS__)

/**
 * structure defined for the colors (ARGB)
 */
typedef struct {
	uint8_t alpha;
	uint8_t red;
	uint8_t green;
	uint8_t blue;
}argb;

extern "C" {


/**
 * Function that converts the image to grayscale image
 */
JNIEXPORT void JNICALL Java_com_example_imageprocessingusingndk_MainActivity_convertToGray(JNIEnv * env,
		jobject jobj, jobject bitmapIn, jobject bitmapOut, jclass cls) {

	// the variables needed declared here
	AndroidBitmapInfo 	infoColor;
	void*				pixelsColor;
	AndroidBitmapInfo	grayImageInfo;
	void*				pixelsGrayColor;
	int					ret;
	int 				x,y;

	if ((ret = AndroidBitmap_getInfo(env, bitmapIn, &infoColor)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapIn failed !");
		return;
	}

	if ((ret = AndroidBitmap_getInfo(env, bitmapOut, &grayImageInfo)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapOut failed !");
		return;
	}

	if (infoColor.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
		LOG_E("Bitmap format is not RGBA_8888 !");
		env->ThrowNew(cls,"Bitmap format is not RGBA_8888 !");
		return;
	}


	if (grayImageInfo.format != ANDROID_BITMAP_FORMAT_A_8) {
		LOG_E("Bitmap format is not A_8 !");
		env->ThrowNew(cls,"Bitmap format is not A_8 !");
		return;
	}

	//COFFEE_TRY() {
	if ((ret = AndroidBitmap_lockPixels(env, bitmapIn, &pixelsColor)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	if ((ret = AndroidBitmap_lockPixels(env, bitmapOut, &pixelsGrayColor)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	for (y=0;y<infoColor.height;y++) {
		argb * line = (argb *) pixelsColor;
		uint8_t * grayline = (uint8_t *) pixelsGrayColor;

		for (x=0;x<infoColor.width;x++) {
			//grayline[x] = 0.21 * line[x].red + 0.71 * line[x].green + 0.07 * line[x].blue;
			grayline[x] = 0.3 * line[x].red + 0.59 * line[x].green + 0.11*line[x].blue;
		}

		pixelsColor = (char *)pixelsColor + infoColor.stride;
		pixelsGrayColor = (char *) pixelsGrayColor + grayImageInfo.stride;
	}

	AndroidBitmap_unlockPixels(env,bitmapIn);
	AndroidBitmap_unlockPixels(env, bitmapOut);

	//}COFFEE_CATCH() {
	//	__android_log_print(ANDROID_LOG_ERROR,APP_NAME,"error : %s ", coffeecatch_get_message());
	/*jclass clss = env->FindClass("java/lang/RuntimeException");
			env->ThrowNew(clss,coffeecatch_get_message());*/
	//}COFFEE_END();
}

JNIEXPORT void JNICALL Java_com_example_imageprocessingusingndk_MainActivity_convertToRed(JNIEnv * env,
		jobject jobj, jobject bitmapIn, jobject bitmapOut, jclass cls) {

	// the variables needed declared here
	AndroidBitmapInfo 	infoColor;
	void*				pixelsColor;
	AndroidBitmapInfo	redImageInfo;
	void*				pixelsRedColor;
	int					ret;
	int 				x,y;

	if ((ret = AndroidBitmap_getInfo(env, bitmapIn, &infoColor)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapIn failed !");
		return;
	}

	if ((ret = AndroidBitmap_getInfo(env, bitmapOut, &redImageInfo)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapOut failed !");
		return;
	}

	if ((ret = AndroidBitmap_lockPixels(env, bitmapIn, &pixelsColor)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	if ((ret = AndroidBitmap_lockPixels(env, bitmapOut, &pixelsRedColor)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	COFFEE_TRY() {
		for (y=0;y<infoColor.height;y++) {
			argb * greyline = (argb *) pixelsColor;
			argb * sepialine = (argb *) pixelsRedColor;

			for (x=0;x<infoColor.width;x++) {
				sepialine[x].red =  0.1 *greyline[x].red;
				sepialine[x].green = 0.9;
				sepialine[x].blue = 0;
				sepialine[x].alpha = greyline[x].alpha;
			}

			pixelsColor = (char *)pixelsColor + infoColor.stride;
			pixelsRedColor = (char *) pixelsRedColor + redImageInfo.stride;
		}

		AndroidBitmap_unlockPixels(env,bitmapIn);
		AndroidBitmap_unlockPixels(env, bitmapOut);

		LOG_E("infoColor.format : %d", redImageInfo.format);
	}COFFEE_CATCH() {
		LOG_E("error : %s", coffeecatch_get_message());
	}COFFEE_END();
}

/**
 * Converts the bitmap to green color
 */
JNIEXPORT void JNICALL Java_com_example_imageprocessingusingndk_MainActivity_convertToGreen(JNIEnv * env,
		jobject jobj, jobject bitmapIn, jobject bitmapOut, jclass cls) {

	AndroidBitmapInfo 	infoColor;
	void*				pixelsColor;
	AndroidBitmapInfo	greenImageColor;
	void*				pixelsGreenColor;
	int					ret;
	int 				x,y;

	if ((ret = AndroidBitmap_getInfo(env, bitmapIn, &infoColor)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapIn failed !");
		return;
	}

	if ((ret = AndroidBitmap_getInfo(env, bitmapOut, &greenImageColor)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapOut failed !");
		return;
	}

	if ((ret = AndroidBitmap_lockPixels(env, bitmapIn, &pixelsColor)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	if ((ret = AndroidBitmap_lockPixels(env, bitmapOut, &pixelsGreenColor)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	COFFEE_TRY() {
		for (y=0;y<infoColor.height;y++) {
			argb * line = (argb *) pixelsColor;
			argb * greenline = (argb *) pixelsGreenColor;

			/* greenline[x].red = 0 * line[x].red + 0.5* line[x].green + 0 * line[x].blue;
			   greenline[x].green = 0.803 * line[x].green + 0.08 * line[x].blue; //0.009 * line[x].red + 0.803 * line[x].green + 0.008 * line[x].blue;
			   greenline[x].blue =  0.0;
			   greenline[x].alpha = line[x].alpha;

			   greenline[x].red = 0.5* line[x].red;// 0 * line[x].red + 0.02* line[x].green + 0 * line[x].blue;
				greenline[x].green = line[x].green;//0 * line[x].red + 1.0 * line[x].green + 0.0 * line[x].blue; //0.009 * line[x].red + 0.803 * line[x].green + 0.008 * line[x].blue;
				greenline[x].blue =0* line[x].blue;
				greenline[x].alpha = line[x].alpha;
							*/
			for (x=0;x<infoColor.width;x++) {
				greenline[x].red = 0.65 * line[x].red;// 0 * line[x].red + 0.02* line[x].green + 0 * line[x].blue;
				greenline[x].green = 1.098 * line[x].green;//0 * line[x].red + 1.0 * line[x].green + 0.0 * line[x].blue; //0.009 * line[x].red + 0.803 * line[x].green + 0.008 * line[x].blue;
				greenline[x].blue =0;//1.1 * line[x].blue;
				greenline[x].alpha = line[x].alpha;
			}

			pixelsColor = (char *)pixelsColor + infoColor.stride;
			pixelsGreenColor = (char *) pixelsGreenColor + greenImageColor.stride;
		}

		AndroidBitmap_unlockPixels(env,bitmapIn);
		AndroidBitmap_unlockPixels(env, bitmapOut);

		LOG_E("infoColor.format : %d", greenImageColor.format);
	}COFFEE_CATCH() {
		LOG_E("error : %s", coffeecatch_get_message());
	}COFFEE_END();
}

JNIEXPORT void JNICALL Java_com_example_imageprocessingusingndk_MainActivity_convertToSepia(JNIEnv * env,
		jobject jobj, jobject bitmapIn, jobject bitmapOut, jclass cls) {

	AndroidBitmapInfo 	infoColor;
	void*				pixelsColor;
	AndroidBitmapInfo	sepiaImageColor;
	void*				pixelsSepia;
	int					ret;
	int 				x,y;

	if ((ret = AndroidBitmap_getInfo(env, bitmapIn, &infoColor)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapIn failed !");
		return;
	}

	if ((ret = AndroidBitmap_getInfo(env, bitmapOut, &sepiaImageColor)) < 0) {
		LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
		env->ThrowNew(cls,"AndroidBitmap_getInfo() bitmapOut failed !");
		return;
	}

	if ((ret = AndroidBitmap_lockPixels(env, bitmapIn, &pixelsColor)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	if ((ret = AndroidBitmap_lockPixels(env, bitmapOut, &pixelsSepia)) < 0) {
		LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	/* =================================== */
	/* For refrerence
	   R' = (R × 0.393 + G × 0.769 + B × 0.189);
	   G' = (R × 0.349 + G × 0.686 + B × 0.168);
	   B' = (R × 0.272 + G × 0.534 + B × 0.131);
	/* =================================== */

	COFFEE_TRY() {
		for (y=0;y<infoColor.height;y++) {
			argb * line = (argb *) pixelsColor;
			argb * sepialine = (argb *) pixelsSepia;

			/* sepialine[x].red = 0.293 * line[x].red + 0.169* line[x].green + 0.489 * line[x].blue;
			sepialine[x].green = 0.349 * line[x].red + 0.483 * line[x].green + 0.168 * line[x].blue;
			sepialine[x].blue =  0.292 * line[x].red + 0.554 * line[x].green + 0.154 * line[x].blue;
			sepialine[x].alpha = line[x].alpha; */

			for (x=0;x<infoColor.width;x++) {
				// 0.393 * line[x].red + 0.369* line[x].green + 0.189 * line[x].blue;
				/*sepialine[x].red = 0.003 * line[x].red + 0.549* line[x].green + 0.449 * line[x].blue;
				sepialine[x].green = 0.009 * line[x].red + 0.903 * line[x].green + 0.008 * line[x].blue;
				sepialine[x].blue =  0.004 * line[x].red + 0.254 * line[x].green + 0.150 * line[x].blue;
				sepialine[x].alpha = line[x].alpha;*/

				sepialine[x].red = 0.393 * line[x].red + 0.369* line[x].green + 0.189 * line[x].blue;
				sepialine[x].green = 0.349 * line[x].red + 0.483 * line[x].green + 0.168 * line[x].blue;
				sepialine[x].blue =  0.292 * line[x].red + 0.554 * line[x].green + 0.154 * line[x].blue;
				sepialine[x].alpha = line[x].alpha;
			}

			pixelsColor = (char *)pixelsColor + infoColor.stride;
			pixelsSepia = (char *) pixelsSepia + sepiaImageColor.stride;
		}

		AndroidBitmap_unlockPixels(env,bitmapIn);
		AndroidBitmap_unlockPixels(env, bitmapOut);

		LOG_E("infoColor.format : %d", sepiaImageColor.format);
	}COFFEE_CATCH() {
		LOG_E("error : %s", coffeecatch_get_message());
	}COFFEE_END();
}

jint truncate(int value) {
	if(value < 0) {
		return 0;
	}else if(value > 255) {
		return 255;
	}else {
		return value;
	}
}

/*JNIEXPORT void JNICALL Java_com_example_imageprocessingusingndk_MainActivity_increaseBrightness(JNIEnv * env,
		jobject jobj, jobject bitmapIn, int direction) {

		AndroidBitmapInfo  	infogray;
	    void*              	pixelsgray;
	    int                	ret;
	    int 				y;
	    int             	x;
	    uint8_t 			save;

	    //http://www.easyrgb.com/index.php?X=MATH

	    if ((ret = AndroidBitmap_getInfo(env, bitmapIn, &infogray)) < 0) {
	    	LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
	    	return;
	    }

	    if ((ret = AndroidBitmap_lockPixels(env, bitmapIn, &pixelsgray)) < 0) {
	    	LOG_E("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	    }

	    for (y=0;y<infogray.height;y++) {
	    	//uint8_t * grayline = (uint8_t *) pixelsgray;
	    	argb * line = (argb *)line;
	    	int v;
	    	for (x=0;x<infogray.width;x++) {
	    		line[x].red = truncate(line[x].red + 5);
	    		line[x].green=truncate( line[x].green + 5);
	    		line[x].blue = truncate(line[x].blue + 5);

	    		//v = (int) grayline[x];

	    		if (direction == 1)
	    			v -=5;
	    		else
	    			v += 5;
	    		if (v >= 255) {
	    			grayline[x] = 255;
	    		} else if (v <= 0) {
	    			grayline[x] = 0;
	    		} else {
	    			grayline[x] = (uint8_t) v;
	    		}
	    	}

	    	pixelsgray = (char *) pixelsgray + infogray.stride;
	    }

	    AndroidBitmap_unlockPixels(env, bitmapIn);

}*/

}
