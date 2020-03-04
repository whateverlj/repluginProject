package com.example.myplugindemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

public class MyView1 extends ViewGroup {


    private int mWidth;
    private int mHeight;
    private int childHalfWidth;
    private int childWidth;
    private int halfWidth;
    private Scroller mScroller;
    Bitmap bufferBitmap;
    Canvas bufferCanvas;
    boolean isFirst = true;
    float moveX = 0;
    float movedistance;
    boolean isAdding = false;

    public MyView1(Context context) {
        super(context);
        init(context);
    }

    public MyView1(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context, new LinearInterpolator());
    }

    ;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        childWidth = mWidth / 3;
        childHalfWidth = childWidth / 2;
        halfWidth = mWidth / 2;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout((i - getChildCount() / 2 + 1) * childWidth , 0, (i - getChildCount() / 2 + 2) * childWidth , mHeight);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        if(isFirst){
//            bufferBitmap = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_8888);//创建内存位图
//            bufferCanvas = new Canvas(bufferBitmap);//创建绘图画布
//            isFirst = false;
//        }
        Log.e("isAdding2", isAdding + "");

        if (!isAdding) {
            float scrollX = getScrollX();
            Log.e("scrollBy",getScrollX()+"");
            if (Math.abs(scrollX) > childWidth) {
                if (scrollX < 0) {
                    scrollX = -childWidth;
                } else {
                    scrollX = childWidth;
                }
            }
            for (int i = 0; i < getChildCount() / 2; i++) {
                canvas.save();
                float rate = 0;
                float sumWidth = 0;
                sumWidth = childWidth * getChildCount() / 2;
                float distance = 0;
                if (getChildAt(i).getX() < halfWidth) {
                    if (getChildAt(i).getX() < 0) {
                        distance = Math.abs(getChildAt(i).getX()) - childHalfWidth + halfWidth + scrollX;
                    } else {
                        distance = halfWidth - getChildAt(i).getX() - childHalfWidth + scrollX;
                    }
                } else {
                    distance = getChildAt(i).getX() - halfWidth + childHalfWidth + scrollX;
                }
                rate = distance / sumWidth;
                float scale = 1.0f - (1 - 0.5f) * rate;
                canvas.scale(scale, scale, mWidth / 2, mHeight / 2);
                drawChild(canvas, getChildAt(i), getDrawingTime());
                canvas.restore();
            }
            for (int i = getChildCount() - 1; i > ((getChildCount() - 1) / 2); i--) {
                canvas.save();
                float rate = 0;
                float sumWidth = 0;
                sumWidth = childWidth * getChildCount() / 2;
                float distance = 0;
                distance = getChildAt(i).getX() - halfWidth + childHalfWidth - scrollX;
                rate = distance / sumWidth;
                float scale = 1.0f - (1 - 0.5f) * rate;
                canvas.scale(scale, scale, mWidth / 2, mHeight / 2);
                drawChild(canvas, getChildAt(i), getDrawingTime());
                canvas.restore();
            }
            for (int i = 0; i < getChildCount() - 1; i++) {
                canvas.save();
                float rate = 0;
                float sumWidth = 0;
                if (i == getChildCount() / 2) {
                    float distance = 0;
                    distance = (float) Math.abs(scrollX);
                    sumWidth = childWidth * getChildCount() / 2;
                    rate = distance / sumWidth;
                    float scale = 1.0f - (1 - 0.5f) * rate;
                    if (scrollX > 0) {
                        canvas.scale(scale, scale, mWidth / 2 + childHalfWidth, mHeight / 2);
                    } else {
                        canvas.scale(scale, scale, mWidth / 2 - childHalfWidth, mHeight / 2);
                    }
                    drawChild(canvas, getChildAt(i), getDrawingTime());
                }
                canvas.restore();
            }
        }
        else {
            isAdding = false;
        }
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }

    }

    private void addNextView() {
        int count = this.getChildCount();
        View child = getChildAt(0);
        removeView(getChildAt(0));
        addView(child, count-1);
    }

    private void addNextView1() {
        View child = getChildAt(getChildCount() - 1);
        removeView(getChildAt(getChildCount() - 1));
        addView(child, 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                moveX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                movedistance = moveX - event.getX();
                scrollTo((int) movedistance, 0);
                if (Math.abs(movedistance) > childWidth) {
                    moveX = event.getX();
                    isAdding = true;
                    if (movedistance > 0) {
                        addNextView();
                    } else if (movedistance < 0) {
                        addNextView1();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                int delta = 0;
                int startX = 0;
                if (Math.abs(movedistance) > childWidth) {
                    if (movedistance > 0) {
                        addNextView();
                        startX = getScrollX();
                        delta = -getScrollX();
                    } else if (movedistance < 0) {
                        addNextView1();
                        startX = getScrollX();
                        delta = -getScrollX();
                    }
                } else {
                    startX = getScrollX();
                    delta = -getScrollX();
                }
                mScroller.startScroll(startX, 0, delta, 0);
                invalidate();
                break;
        }
        return true;
    }
}
