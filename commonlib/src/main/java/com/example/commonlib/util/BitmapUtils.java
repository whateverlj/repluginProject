package  com.example.commonlib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {

    public static String bitmapToStr(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
        byte[] data = null;
        try {
            data = new byte[is.available()];
            is.read(data);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < data.length; n++) {
            stmp = Integer.toHexString(data[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }

    public static Bitmap toBitmap(File file, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                options);
        Bitmap returnBm = null;
        Matrix m = new Matrix();
        m.postRotate(90);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;

    }

    public static void saveBitmapFile(Bitmap bitmap, File file) {

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            } else {
                file.delete();
            }
            file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static int calSampleSize(long length, long targetLenth) {
        float per = length * 1.0f / targetLenth;
        per /= 2.7;
        if (per <= 1) {
            return 1;
        }
        return (int) Math.ceil(per);
    }

}
