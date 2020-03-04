package  com.example.commonlib.util;

/**
 * Created by test on 2017/10/20.
 */

public class ClickUtils {

        private static long lastClickTime;
        public static boolean isFastDoubleClick() {
            long time = System.currentTimeMillis();
            long timeD = time - lastClickTime;
            if ( 0 < timeD && timeD < 1100) {       //500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
                return true;
            }
            lastClickTime = time;
            return false;
        }

}
