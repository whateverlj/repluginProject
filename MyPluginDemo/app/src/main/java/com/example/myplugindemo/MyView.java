package com.example.myplugindemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {
    private float moveX;
    private float moveY;
    private float moveDistanceX;
    private float moveDistanceY;
    public MyView(Context context){
        super(context);
    }
    public MyView(Context context, AttributeSet set){
        super(context,set);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
           break;
            case MotionEvent.ACTION_MOVE:
               moveDistanceX = event.getX() - moveX;
               moveDistanceY = event.getY() - moveY;
               setX(getX() + moveDistanceX);
                setY(getY()+ moveDistanceY);
                break;
        };
        return true;
    }
}
