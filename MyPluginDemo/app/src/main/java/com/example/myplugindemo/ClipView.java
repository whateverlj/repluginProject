package com.example.myplugindemo;

import android.app.AppComponentFactory;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ClipView extends AppCompatTextView {
    public ClipView(Context context) {
        super(context);
        init();
    }

    public ClipView(Context context, AttributeSet set) {
        super(context, set);
        init();
    }

    Rect mRect = new Rect();
    Paint mPaint = new Paint();
    float moveX = 0;
    float moveDistance = 0;

    private void init() {
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveDistance = Math.abs(event.getX() - moveX);
                mRect.set(0, 0, (int)Math.abs( moveDistance), getHeight());
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (moveDistance > 0) {
            mPaint.setColor(Color.RED);
            canvas.save();
            canvas.clipRect(mRect);
            canvas.drawText(getText().toString(),0,0,mRect.right, getHeight(),mPaint);
            canvas.restore();
        }

    }
}
