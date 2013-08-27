package com.pengjun.ka.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtils {

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable
						.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap bitmapDrawable2Bitmap(BitmapDrawable drawable) {
		return drawable.getBitmap();
	}

	public static Drawable bitmap2Drawable(Bitmap bmp) {
		return new BitmapDrawable(bmp);
	}

	public static byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		return out.toByteArray();
	}

	public static Bitmap byte2Bitmap(byte[] buffer) {
		return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
	}

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

	public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
	}

	public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
		float[] floats = null;
		switch (flag) {
		// horizontal
		case 0:
			floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
			break;
		// vertical
		case 1:
			floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
			break;
		}

		if (floats != null) {
			Matrix matrix = new Matrix();
			matrix.setValues(floats);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		}

		return null;
	}

	public static Bitmap scaleBitmap(Bitmap source, int targetWidth, int targetHeight) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();
		float scaleWidth = ((float) targetWidth) / sourceWidth;
		float scaleHeight = ((float) targetHeight) / sourceHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap result = Bitmap.createBitmap(source, 0, 0, sourceWidth, sourceHeight, matrix, true);
		source.recycle();
		source = null;
		return result;
	}

	public static void saveImageToSdcard(String path, Bitmap bitmap) {

		File file = new File(path);
		FileOutputStream out = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
