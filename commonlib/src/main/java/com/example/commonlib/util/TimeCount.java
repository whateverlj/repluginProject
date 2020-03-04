package  com.example.commonlib.util;

import android.os.CountDownTimer;

/**
 * 倒计时，可用于验证码两次发送时间间距的倒计时
 * new TimeCount(6000, 1000, listener).start()
 *
 * @author Like
 * @version 1.0.0
 */
public class TimeCount extends CountDownTimer {

    private TimeListener mListener;

    /**
     * @param millisInFuture    倒计时的总时长（单位：毫秒）
     * @param countDownInterval 倒计时的间隔时差（单位：毫秒）
     * @param listener          倒计时回调函数
     */
    public TimeCount(long millisInFuture, long countDownInterval,
                     TimeListener listener) {
        super(millisInFuture, countDownInterval);
        this.mListener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mListener.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        mListener.onFinish();
    }

    public interface TimeListener {
        /**
         * 倒计时回调，每个tick调用一次
         *
         * @param millisUntilFinished 距离倒计时结束时间
         */
        void onTick(long millisUntilFinished);

        /**
         * 倒计时结束回调
         */
        void onFinish();
    }

}
