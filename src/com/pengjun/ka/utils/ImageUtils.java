package com.pengjun.ka.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;

public class ImageUtils {

	public static Bitmap createReflectedImage(Bitmap oriBmp, float ref2OriRadio) {

		if (ref2OriRadio < 0) {
			return null;
		}

		int width = oriBmp.getWidth();
		int height = oriBmp.getHeight();

		Bitmap dstBmp = Bitmap.createBitmap(width, (int) (height * (1 + ref2OriRadio)), Config.ARGB_8888);
		Canvas canvas = new Canvas(dstBmp);
		canvas.drawBitmap(oriBmp, 0, 0, null);

		// create reflect bmp
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionBmp = Bitmap.createBitmap(oriBmp, 0, (int) (height * (1 - ref2OriRadio)), width,
				(int) (height * ref2OriRadio), matrix, false);
		canvas.drawBitmap(reflectionBmp, 0, height, null);
		reflectionBmp.recycle();

		// make the gradual change effect
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, oriBmp.getHeight(), 0, dstBmp.getHeight(), 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, dstBmp.getHeight(), paint);

		return dstBmp;
	}

	public static Bitmap combineImageForeFirst(Bitmap backgoundBmp, Bitmap foregoundBmp, Rect bRect,
			Rect fRect) {

		Bitmap combineImage = Bitmap.createBitmap(backgoundBmp.getWidth(), backgoundBmp.getHeight(),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(combineImage);
		canvas.drawBitmap(foregoundBmp, fRect, bRect, null);
		canvas.drawBitmap(backgoundBmp, 0, 0, null);

		return combineImage;
	}

	public static Bitmap combineImageBackFirst(Bitmap backgoundBmp, Bitmap foregoundBmp, Rect bRect,
			Rect fRect) {

		Bitmap combineImage = Bitmap.createBitmap(backgoundBmp.getWidth(), backgoundBmp.getHeight(),
				Config.ARGB_8888);

		Canvas canvas = new Canvas(combineImage);
		canvas.drawBitmap(backgoundBmp, 0, 0, null);
		canvas.drawBitmap(foregoundBmp, fRect, bRect, null);

		return combineImage;
	}
}
