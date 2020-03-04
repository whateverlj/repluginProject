package com.example.myplugindemo;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class StereoView extends ViewGroup {

    //可对外进行设置的参数
    private int mStartScreen = 1;//开始时的item位置
    private float resistance = 1f;//滑动阻力
    private Scroller mScroller;

    private Context mContext;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int mChildWidth;//子view容器的宽度
    private int mHeight;//容器的高度
    private static final int standerSpeed = 2000;
    private static final int flingSpeed = 800;
    private int addCount;//记录手离开屏幕后，需要新增的页面次数
    private int alreadyAdd = 0;//对滑动多页时的已经新增页面次数的记录
    private boolean isAdding = false;//fling时正在添加新页面，在绘制时不需要开启camera绘制效果，否则页面会有闪动
    private int mCurScreen = 1;//记录当前item
    private float mDownX, mTempY;
    private boolean isSliding = true;

    private State mState = State.Normal;

    public StereoView(Context context) {
        this(context, null);
    }

    public StereoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StereoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(mContext);
    }

    /**
     * 初始化数据
     */
    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        if (mScroller == null) {
            mScroller = new Scroller(context);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mChildWidth = getMeasuredWidth()/3;
        mHeight = getMeasuredHeight();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout((i - getChildCount() / 2 + 1) * mChildWidth, 0, (i - getChildCount() / 2 + 2) * mChildWidth, mHeight);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        float x = ev.getX();
//        float y = ev.getY();
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isSliding = false;
//                mDownX = x;
//                mTempY = mDownY = y;
//                if (!mScroller.isFinished()) {
//                    //当上一次滑动没有结束时，再次点击，强制滑动在点击位置结束
//                    mScroller.setFinalX(mScroller.getCurrX());
//                    mScroller.abortAnimation();
//                    scrollTo(getScrollX(), 0);
//                    isSliding = true;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (!isSliding) {
//                    isSliding = isCanSliding(ev);
//                }
//                break;
//            default:
//                break;
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        float y = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                    int realDelta = (int) (mDownX - y);
                    Log.e("realDelta",realDelta+"");
                   mDownX = y;
                    if (mScroller.isFinished()) {
                        //因为要循环滚动
                        recycleMove(realDelta);
                    }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float yVelocity = mVelocityTracker.getXVelocity();
                    //滑动的速度大于规定的速度，或者向上滑动时，上一页页面展现出的高度超过1/2。则设定状态为State.ToPre
                    if (yVelocity > standerSpeed || ((getScrollX() + mChildWidth / 2) / mChildWidth < mStartScreen)) {
                        mState = State.ToPre;
                    } else if (yVelocity < -standerSpeed || ((getScrollX() + mChildWidth / 2) / mChildWidth > mStartScreen)) {
                        //滑动的速度大于规定的速度，或者向下滑动时，下一页页面展现出的高度超过1/2。则设定状态为State.ToNext
                        mState = State.ToNext;
                    } else {
                        mState = State.Normal;
                    }
                    //根据mState进行相应的变化
                    changeByState(yVelocity);
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private void changeByState(float yVelocity) {
        alreadyAdd = 0;//重置滑动多页时的计数
        if (getScrollX() != mChildWidth) {
            switch (mState) {
                case Normal:
                    toNormalAction();
                    break;
                case ToPre:
                    toPreAction(yVelocity);
                    break;
                case ToNext:
                    toNextAction(yVelocity);
                    break;
            }
            invalidate();
        }
    }

    /**
     * mState = State.Normal 时进行的动作
     */
    private void toNormalAction() {
        int startY;
        int delta;
        int duration;
        mState = State.Normal;
        addCount = 0;
        startY = getScrollX();
        delta = mChildWidth * mStartScreen - getScrollX();
        duration = (Math.abs(delta)) * 4;
        mScroller.startScroll(startY, 0, delta, 0, duration);
    }

    /**
     * mState = State.ToPre 时进行的动作
     *
     * @param yVelocity 竖直方向的速度
     */
    private void toPreAction(float yVelocity) {
        int startY;
        int delta;
        int duration;
        mState = State.ToPre;
        addPre();//增加新的页面
        //计算松手后滑动的item个数
        int flingSpeedCount = (yVelocity - standerSpeed) > 0 ? (int) (yVelocity - standerSpeed) : 0;
        addCount = flingSpeedCount / flingSpeed + 1;
        //mScroller开始的坐标
        startY = getScrollX() + mChildWidth;
        setScrollX(startY);
        //mScroller移动的距离
        delta = -(startY - mStartScreen * mChildWidth) - (addCount - 1) * mChildWidth;
        duration = (Math.abs(delta)) * 3;
        mScroller.startScroll(startY, 0, delta, 0, duration);
        addCount--;
    }

    /**
     * mState = State.ToNext 时进行的动作
     *
     * @param yVelocity 竖直方向的速度
     */
    private void toNextAction(float yVelocity) {
        int startY;
        int delta;
        int duration;
        mState = State.ToNext;
        addNext();
        int flingSpeedCount = (Math.abs(yVelocity) - standerSpeed) > 0 ? (int) (Math.abs(yVelocity) - standerSpeed) : 0;
        addCount = flingSpeedCount / flingSpeed + 1;
        startY = getScrollX() - mChildWidth;
        setScrollX(startY);
        delta = mChildWidth * mStartScreen - startY + (addCount - 1) * mChildWidth;
        Log.e("","多后一页startY " + startY + " yVelocity " + yVelocity + " delta " + delta + "  getScrollY() " + getScrollY() + " addCount " + addCount);
        duration = (Math.abs(delta)) * 3;
        mScroller.startScroll(startY, 0, delta, 0, duration);
        addCount--;
    }


    @Override
    public void computeScroll() {
        //滑动没有结束时，进行的操作
        if (mScroller.computeScrollOffset()) {
            if (mState == State.ToPre) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY() + mChildWidth * alreadyAdd);
                if (getScrollX() < (mChildWidth + 2) && addCount > 0) {
                    isAdding = true;
                    addPre();
                    alreadyAdd++;
                    addCount--;
                }
            } else if (mState == State.ToNext) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY() - mChildWidth * alreadyAdd);
                if (getScrollX() > (mChildWidth) && addCount > 0) {
                    isAdding = true;
                    addNext();
                    addCount--;
                    alreadyAdd++;
                }
            } else {
                //mState == State.Normal状态
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            postInvalidate();
        }
        //滑动结束时相关用于计数变量复位
        if (mScroller.isFinished()) {
            alreadyAdd = 0;
            addCount = 0;
        }
    }

    /**
     * 把第一个item移动到最后一个item位置
     */
    private void addNext() {
        mCurScreen = (mCurScreen + 1) % getChildCount();
        int childCount = getChildCount();
        View view = getChildAt(0);
        removeViewAt(0);
        addView(view, childCount - 1);
    }

    /**
     * 把最后一个item移动到第一个item位置
     */
    private void addPre() {
        mCurScreen = ((mCurScreen - 1) + getChildCount()) % getChildCount();
        int childCount = getChildCount();
        View view = getChildAt(childCount - 1);
        removeViewAt(childCount - 1);
        addView(view, 0);
    }

    private void recycleMove(int delta) {
        delta = delta % mChildWidth;
        delta = (int) (delta / resistance);
        if (Math.abs(delta) > mChildWidth / 4) {
            return;
        }
        scrollBy(delta, 0);
        if (getScrollX() < 5 && mStartScreen != 0) {
            addPre();
//            scrollBy(mChildWidth, 0);
        } else if (getScrollX() > (getChildCount() - 1) * mChildWidth - 5) {
            addNext();
//            scrollBy(-mChildWidth, 0);
        }

    }

    public enum State {
        Normal, ToPre, ToNext
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (!isAdding) {
            Log.e("scrollBy",getScrollX()+"");
            super.dispatchDraw(canvas);
//            float scrollX = (float)getScrollX();
//            Log.e("scrollX",scrollX +"");
//            if (Math.abs(scrollX) > mChildWidth) {
//                if (scrollX < 0) {
//                    scrollX = -mChildWidth;
//                } else {
//                    scrollX = mChildWidth % mChildWidth;
//                }
//            }
//            for (int i = 0; i < getChildCount() / 2; i++) {
//                canvas.save();
//                float rate = 0;
//                float sumWidth = 0;
//                sumWidth = mChildWidth * getChildCount() / 2;
//                float distance = 0;
//                if (getChildAt(i).getX() < getWidth()/2) {
//                    if (getChildAt(i).getX() < 0) {
//                        distance = Math.abs(getChildAt(i).getX()) - mChildWidth/2 + getWidth()/2 + scrollX;
//                    } else {
//                        distance = getWidth()/2 - getChildAt(i).getX() - mChildWidth/2  + scrollX;
//                    }
//                } else {
//                    distance = getChildAt(i).getX() - getWidth()/2 + mChildWidth/2  + scrollX;
//                }
//                rate = distance / sumWidth;
//                float scale = 1.0f - (1 - 0.5f) * rate;
//                canvas.scale(scale, scale, getWidth() / 2, mHeight / 2);
//                drawChild(canvas, getChildAt(i), getDrawingTime());
//                canvas.restore();
//            }
//            for (int i = getChildCount()-1 ; i > ((getChildCount() - 1) / 2); i--) {
//                canvas.save();
//                float rate = 0;
//                float sumWidth = 0;
//                sumWidth = mChildWidth * getChildCount() / 2;
//                float distance = 0;
//                distance = getChildAt(i).getX() - getWidth()/2 + mChildWidth/2  - scrollX;
//                rate = distance / sumWidth;
//                float scale = 1.0f - (1 - 0.5f) * rate;
//                canvas.scale(scale, scale, getWidth() / 2, mHeight / 2);
//                drawChild(canvas, getChildAt(i), getDrawingTime());
//                canvas.restore();
//            }
//            for (int i = 0; i < getChildCount() - 1; i++) {
//                canvas.save();
//                float rate = 0;
//                float sumWidth = 0;
//                if (i == getChildCount() / 2) {
//                    float distance = 0;
//                    distance = (float) Math.abs(scrollX);
//                    sumWidth = mChildWidth * getChildCount() / 2;
//                    rate = distance / sumWidth;
//                    float scale = 1.0f - (1 - 0.5f) * rate;
//                    if (scrollX > 0) {
//                        canvas.scale(scale, scale, getWidth() / 2 + mChildWidth/2 , mHeight / 2);
//                    } else {
//                        canvas.scale(scale, scale, getWidth() / 2 - mChildWidth/2 , mHeight / 2);
//                    }
//                    drawChild(canvas, getChildAt(i), getDrawingTime());
//                }
//                canvas.restore();
//            }
        } else {
            isAdding = false;
            super.dispatchDraw(canvas);
        }
    }
}