package com.example.myplugindemo;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Package com.hc.studyCoordinatorLayout
 */
public class MyBehavior extends CoordinatorLayout.Behavior<View> {
    private int width;

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        width = display.widthPixels;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //如果dependency是TempView的实例，说明它就是我们所需要的Dependency
        return dependency instanceof MyView;
    }

    //每次dependency位置发生变化，都会执行onDependentViewChanged方法
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View btn, View dependency) {

        //根据dependency的位置，设置Button的位置

        float top = dependency.getX();
        float left = dependency.getY();

        float x = width - left - btn.getWidth();
        float y = top;

        setPosition(btn, x, y);
        return true;
    }

    private void setPosition(View v, float x, float y) {
        CoordinatorLayout.MarginLayoutParams layoutParams = (CoordinatorLayout.MarginLayoutParams) v.getLayoutParams();
        layoutParams.leftMargin = (int)x;
        layoutParams.topMargin = (int)y;
        v.setLayoutParams(layoutParams);
    }


}