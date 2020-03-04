package com.example.myplugindemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    MyFragment myFragment;
    OthrerFragment othrerFragment;
    ThreeFragment threeFragment;
    private Fragment[] fragments;
    ConstraintLayout container;
    BottomNavigationView navigation;
    private int lastfragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView() {
        othrerFragment = new OthrerFragment();
        myFragment = new MyFragment();
        threeFragment = new ThreeFragment();
        fragments = new Fragment[]{myFragment, othrerFragment, threeFragment};
        container = (ConstraintLayout) findViewById(R.id.container);
        //设置fragment到布局
        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).show(myFragment).commit();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //这里是bottomnavigationview的点击事件
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        final TextView tv = (TextView) findViewById(R.id.btn);
        new  LooperThread().start();
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PhotoActivity.class));
//                Message msg =new Message();
//                msg.what=0;
//                mHandler.sendMessage(msg);
            }
        });

    }
    public static Handler mHandler;
    public static class LooperThread extends Thread {

        public static Looper childLooper;
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            childLooper = Looper.myLooper();
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.e("111111111111111",msg.what+"");
                }
            };
            Looper.loop();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //这里因为需要对3个fragment进行切换
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    //end
                    //如果只是想测试按钮点击，不管fragment的切换，可以把start到end里面的内容去掉
                    return true;
                case R.id.navigation_dashboard:
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    return true;
                case R.id.navigation_notifications:
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    /**
     * 切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.container, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
