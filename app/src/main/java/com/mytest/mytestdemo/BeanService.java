package com.mytest.mytestdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BeanService extends Service {
    private final String TAG = "Server";

    private List<Bean> BeanList;

    public BeanService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BeanList = new ArrayList<>();
        initData();
    }

    private void initData() {
        Bean Bean1 = new Bean("活着");
        Bean Bean2 = new Bean("或者");
        Bean Bean3 = new Bean("叶应是叶");
        Bean Bean4 = new Bean("https://github.com/leavesC");
        Bean Bean5 = new Bean("http://www.jianshu.com/u/9df45b87cfdf");
        Bean Bean6 = new Bean("http://blog.csdn.net/new_one_object");
        BeanList.add(Bean1);
        BeanList.add(Bean2);
        BeanList.add(Bean3);
        BeanList.add(Bean4);
        BeanList.add(Bean5);
        BeanList.add(Bean6);
    }
    private final BeanControllerAidl.Stub stub = new BeanControllerAidl.Stub() {

        @Override
        public List<Bean> getBeanList() throws RemoteException {
            return BeanList;
        }

        @Override
        public void addBeanInOut(Bean bean) throws RemoteException {
            if (bean != null) {
                bean.setName("服务器改了新书的名字 InOut");
                BeanList.add(bean);
            } else {
                Log.e(TAG, "接收到了一个空对象 InOut");
            }
        }

    };
    @Override
    public IBinder onBind(Intent intent) {
        return  stub;
    }
}
