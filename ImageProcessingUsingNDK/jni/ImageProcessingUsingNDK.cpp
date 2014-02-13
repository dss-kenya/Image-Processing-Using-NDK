#include <jni.h>
#include <stdlib.h>
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
			char* message = "AndroidBitmap_getInfo() failed ! error=%d", ret;
			env->ThrowNew(cls,message);
			return;
		}

		if ((ret = AndroidBitmap_getInfo(env, bitmapOut, &grayImageInfo)) < 0) {
			LOG_E("AndroidBitmap_getInfo() failed ! error=%d", ret);
			char* message = "AndroidBitmap_getInfo() failed ! error=%d", ret;
			env->ThrowNew(cls,message);
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
}
