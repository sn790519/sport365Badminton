package com.sport365.badminton.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.sport365.badminton.R;
import com.sport365.badminton.base.BizException;

public class FileTools {

	public static String installPath;// 在sdcard

	private static BitmapDrawable get(Activity activity,
			InputStream inputStream, String price, float density) {
		Paint paintText = new Paint();
		paintText.setColor(Color.WHITE);
		paintText.setTextSize(13 * density);
		paintText.setStyle(Paint.Style.STROKE);
		// ANTI_ALIAS
		paintText.setFlags(Paint.ANTI_ALIAS_FLAG);
		String displayText = "¥" + price.split("\\.")[0] + "起";
		// Measure the width of the text string.
		float textWidth = paintText.measureText(displayText);// 文本宽度
		Bitmap bbbm = BitmapFactory.decodeStream(inputStream);
		int addWidth = (int) (18 * density);
		int addHeight = (int) (10 * density);
		int height = bbbm.getHeight();
		int allwidth = (int) (textWidth + addWidth);
		int allHeight = height + addHeight;
		Bitmap bitmapReal = Bitmap.createBitmap(allwidth, allHeight,
				Config.ARGB_8888);

		Canvas canvasRef = new Canvas(bitmapReal);
		Rect dst = new Rect();// 屏幕裁剪区域
		dst.left = 0;
		dst.top = 0;
		dst.right = allwidth;
		dst.bottom = allHeight;

		canvasRef.drawBitmap(bbbm, null, dst, null);

		// 绘制文本
		canvasRef.drawText(displayText, addWidth / 2, allHeight / 2 - 5,
				paintText);

		BitmapDrawable bitmapDrawable = new BitmapDrawable(
				activity.getResources(), bitmapReal);
		// bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(),
		// bitmapDrawable.getIntrinsicHeight());
		return bitmapDrawable;
	}

	public static Bitmap getStreet(Activity activity, int resId, String title,
			float density) {
		Paint paintText = new Paint();
		paintText.setColor(Color.WHITE);
		paintText.setTextSize(13 * density);
		paintText.setStyle(Paint.Style.STROKE);
		// ANTI_ALIAS
		paintText.setFlags(Paint.ANTI_ALIAS_FLAG);
		// Measure the width of the text string.
		float textWidth = paintText.measureText(title);// 文本宽度
		InputStream is = activity.getResources().openRawResource(resId);
		Bitmap bbbm = BitmapFactory.decodeStream(is);
		int addWidth = (int) (15 * density);
		int addHeight = (int) (12 * density);
		int height = bbbm.getHeight();
		int allwidth = (int) (textWidth + addWidth);
		int allHeight = height + addHeight;
		Bitmap bitmapReal = Bitmap.createBitmap(allwidth, allHeight,
				Config.ARGB_8888);

		Canvas canvasRef = new Canvas(bitmapReal);
		Rect dst = new Rect();// 屏幕裁剪区域
		dst.left = 0;
		dst.top = 0;
		dst.right = allwidth;
		dst.bottom = allHeight;

		canvasRef.drawBitmap(bbbm, null, dst, null);

		// 绘制文本
		canvasRef.drawText(title, addWidth / 2, allHeight / 2 + 5, paintText);

		return bitmapReal;
	}

	/**
	 * 得到普通背景图片
	 * 
	 * @param activity
	 * @param price
	 * @return
	 */

	public static Drawable getDrawable(Activity activity, String price,
			float density) {
		InputStream is = activity.getResources().openRawResource(
				R.drawable.bg_hotelmap_price);
		return get(activity, is, price, density);
	}

	/**
	 * 得到普通背景图片(满房)
	 * 
	 * @param activity
	 * @param price
	 * @return
	 */
	public static Drawable getDrawableFullRoom(Activity activity, String price,
			float density) {
		InputStream is = activity.getResources().openRawResource(
				R.drawable.bg_hotelmap_full);
		return get(activity, is, price, density);
	}

	/**
	 * 得到被选中的背景图片（黄色）
	 * 
	 * @param activity
	 * @param price
	 * @return
	 */
	public static Drawable getDrawablePressed(Activity activity, String price,
			float density) {
		InputStream is = activity.getResources().openRawResource(
				R.drawable.bg_hotelmapdown_price);
		return get(activity, is, price, density);
	}

	// save drawable to file
	public static void saveDrawableToFile(Drawable drawable, String bitName) {
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		try {
			saveMyBitmap(bitmap, getFileNameWithEx(bitName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 将bitmap保存到file
	public static void saveMyBitmap(Bitmap bm, String bitName)
			throws IOException {
		File f = new File(Utilities.FILE_ROOT + bitName);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			String fileExtension = getExtensionName(bitName);

			if (fileExtension.equals("png"))
				bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			else
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Java文件操作 获取文件扩展名
	 * 
	 * Created on: 2011-8-2
	 */
	public static String getExtensionName(String filename) {
		if (filename != null && filename.length() > 0) {
			int dot = filename.lastIndexOf('.');
			if (dot > -1 && dot < (filename.length() - 1)) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * Java文件操作 获取带扩展名的文件名
	 * 
	 * @param urlName
	 * @return filename
	 */
	public static String getFileNameWithEx(String urlName) {
		if ((urlName != null) && (urlName.length() > 0)) {
			int dot = urlName.lastIndexOf('/');
			if ((dot > -1) && (dot + 1 < (urlName.length()))) {
				return urlName.substring(dot + 1);
			}
		}
		return urlName;
	}

	public static boolean saveToSD(String dirName, String fileName,
			String content) throws BizException {
		File dirFile = new File(dirName);// 判断文件夹是否存在
		if (!dirFile.exists()) {
			return false;
		}
		File f = new File(dirName, fileName);
		FileOutputStream out2 = null;
		try {
			out2 = new FileOutputStream(f);
			out2.write(content.getBytes());
			out2.flush();
			return true;
		} catch (Exception e) {
			throw new BizException("往SD卡中保存数据失败", e);
		} finally {
			if (out2 != null) {
				try {
					out2.close();
				} catch (Exception e2) {
				}
			}
		}
	}

	public static void saveToSD(String fileName, String content)
			throws BizException {
		saveToSD(Utilities.JSON_FILE_ROOT, fileName, content);
	}

	public static String readFile(String fileName) throws BizException {
		return readFile(Utilities.JSON_FILE_ROOT, fileName);
	}

	public static String readFile(String dirName, String fileName)
			throws BizException {
		File readFile = new File(dirName, fileName);
		if (readFile.exists() && readFile.canRead()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(readFile);
				return inputStream2String(fis);
			} catch (IOException e) {
				throw new BizException("从SD卡读取数据失败", e);
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return "";
	}

	public static String inputStream2String(InputStream in) throws IOException {
		BufferedReader inReader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = inReader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	/**
	 * 2张图片合成
	 * 
	 * @param src1
	 * @param src2
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap src1, Bitmap src2) {
		if (src1 == null || src2 == null)
			return null;

		int w = src1.getWidth();
		int h = src1.getHeight();
		int ww = src2.getWidth();
		int wh = src2.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h + 5 + wh, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src1, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(src2, (w - ww) / 2, h + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param dirName
	 * @param fileName
	 */
	public static void deleteFile(String dirName, String fileName) {
		File f = new File(dirName, fileName);
		if (f != null && f.exists()) {
			f.delete();
		}
	}

	/**
	 * 重命名文件
	 * 
	 * @return 重命名之后的文件路径
	 * @author Ruyan.Zhao 6045
	 * @since tongcheng_client_6.0.1 2014-2-28 下午1:33:35
	 */
	public static String renameFolder(String folderPath) {
		File file = new File(folderPath);
		final File to = new File(file.getAbsolutePath()
				+ System.currentTimeMillis());
		file.renameTo(to);
		return to.getAbsolutePath();
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 删除文件夹（带返回值）
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static boolean deleteFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static void delAllFile(String path) {

		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		if (file.list() == null) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {

			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				if (path.contains(Utilities.JSON_FILE_ROOT)) {
					if (!fileCache(temp)) {
						temp.delete();
					}
				} else {
					temp.delete();
				}

			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 判断是否要删除
	 */
	public static boolean fileCache(File file) {

		if (file.exists()) {
			long expiredTime = System.currentTimeMillis() - file.lastModified();

			if (expiredTime > ConfigCache.CONFIG_CACHE_MOBILE_TIMEOUT) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static File createDir(String dirName) {
		File dir = new File(installPath + dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	public static boolean createFile(String fileName) {
		File file = new File(installPath + fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public static synchronized boolean writeObjectToFile(Object object,
			String dirName, String fileName) {
		createDir(dirName);
		File file = new File(installPath + dirName + File.separator + fileName);
		if (!file.exists()) {
			createFile(dirName + File.separator + fileName);
		}
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void delOneDir(String dirName) {
		File file = new File(installPath + dirName);
		delAllFile(file.getAbsolutePath());
	}

	public static synchronized Object readObjectFromFile(String dirName,
			String fileName) {
		File file = new File(installPath + dirName + File.separator + fileName);
		if (!file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			Object object = ois.readObject();
			return object;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
