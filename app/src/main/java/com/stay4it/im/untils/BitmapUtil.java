package com.stay4it.im.untils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Stay
 * @version create time：Feb 1, 2013 12:33:51 PM
 */
public class BitmapUtil {
	public static final int DEFAULT_WIDTH = 480;
	public static final int DEFAULT_HEIGHT = 640;

	/**
	 * 保持长宽比缩小Bitmap
	 * 
	 * @param bitmap
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();
		// no need to resize
		if (originWidth < maxWidth && originHeight < maxHeight) {
			return bitmap;
		}
		int width = originWidth;
		int height = originHeight;
		if (originWidth > maxWidth) {
			width = maxWidth;
			double i = originWidth * 1.0 / maxWidth;
			height = (int) Math.floor(originHeight / i);
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
		}
		if (height > maxHeight) {
			height = maxHeight;
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		}
		return bitmap;
	}

	public static Bitmap loadBitmap(String path, int mWidth, int mHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = null;
		int be = 1;
		try {
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
			be = (int) Math.max(options.outWidth / mWidth, options.outHeight
					/ mHeight);
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
		} catch (OutOfMemoryError e) {
			Trace.d(e.toString());
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			options.inSampleSize = be * 2;
			try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
						null, options);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			Trace.e(e.toString());
		}
		return bitmap;
	}
	
	public static BitmapDrawable loadDrawable(String path){
		return new BitmapDrawable(BitmapFactory.decodeFile(path));
	}

	public static Bitmap loadBitmap(String path, int mWidth, int mHeight,
			boolean isNeedToRotate) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = null;
		int be = 1;
		int needToRotate = 0;
		try {
			if (isNeedToRotate) {
				ExifInterface exif = new ExifInterface(path);
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, -1);
				if (orientation != -1) {
					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						needToRotate = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						needToRotate = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						needToRotate = 270;
						break;
					}
				}
			}
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
			be = (int) Math.max(options.outWidth / mWidth, options.outHeight
					/ mHeight);
			if (be <= 0) {
				be = 1;
			}
			options.inSampleSize = be;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
					null, options);
		} catch (OutOfMemoryError e) {
			Trace.d(e.toString());
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			options.inSampleSize = be * 2;
			try {
				bitmap = BitmapFactory.decodeStream(new FileInputStream(path),
						null, options);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			Trace.e(e.toString());
		}
		if (bitmap != null && isNeedToRotate && needToRotate != 0) {
			Matrix tempMatrix = new Matrix();
			tempMatrix.postRotate(needToRotate);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), tempMatrix, false);
		}
		return bitmap;
	}

	public static void compressBitmap(String path, Bitmap bitmap, int quality, long attachmentMaxSize) {
		BufferedOutputStream bos = null;
		File mFile = new File(path);
		try {
			if (mFile.exists()) {
				mFile.delete();
				mFile.createNewFile();
			}
			bos = new BufferedOutputStream(new FileOutputStream(path));
			if (bitmap != null && bos != null) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.flush();
					bos.close();
				}
				if (mFile.exists() && mFile.length() > attachmentMaxSize) {
					compressBitmap(path, bitmap, quality/2, attachmentMaxSize);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static int[] getBitmapScale(String path) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(path), null,
					options);
			return new int[] { options.outWidth, options.outHeight };
		} catch (FileNotFoundException e) {
			return new int[]{0,0};
		}
	}
	
	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	} 
}
