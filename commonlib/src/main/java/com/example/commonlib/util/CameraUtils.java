package  com.example.commonlib.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class CameraUtils {
	
	/**
	 * 保存图片
	 * @param path 图片绝对路径
	 * @param bitmap
	 */
	public static void saveBitmap2Path(String path, Bitmap bitmap) {
		CompressFormat format = CompressFormat.PNG;
		File file = new File(path);
		if (!file.exists()) { // 若该文件存在
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			int lastIndexOf = path.lastIndexOf(".");
			String key = path.substring(lastIndexOf + 1, path.length()).toUpperCase();
			switch (key) {
			case "JPEG":
				format = CompressFormat.JPEG;
				break;
			case "WEBP":
				//format = CompressFormat.WEBP;
				break;
			}
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(format, 100, fos);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
