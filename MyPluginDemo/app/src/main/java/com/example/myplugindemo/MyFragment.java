package com.example.myplugindemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mytest.mytestdemo.BeanControllerAidl;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class MyFragment extends Fragment {
    private View rootView;//缓存Fragment view
    public static final int binderCode = 111111;
    private BeanControllerAidl bookController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my, container, false);
        } else {
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        final RefreshLayout refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        //触发自动刷新
        refreshLayout.autoRefresh();
        ImageView img  = rootView.findViewById(R.id.img);
        Glide.with(this).load("https://goss.veer.com/creative/vcg/veer/800water/veer-350534476.jpg").into(img);

//        Button btn = (Button) rootView.findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent serviceIntent = new Intent();
//                serviceIntent.setComponent(new ComponentName("com.mytest.mytestdemo", "com.mytest.mytestdemo.MyService"));
//                getActivity().bindService(serviceIntent, new ServiceConnection() {
//                    @Override
//                    public void onServiceConnected(ComponentName name, IBinder service) {
//                        Parcel data0 = Parcel.obtain();//请求参数
//                        Parcel reply0 = Parcel.obtain();//响应参数
//                        try {
//                            data0.writeInt(9999);
//                            data0.writeString("成功通信宿主");
//                            service.transact(binderCode, data0, reply0, 0);
////                            reply0.readException();
//                            Toast.makeText(getActivity(),reply0.readInt()+"",Toast.LENGTH_LONG).show();
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        } finally {
//                            data0.recycle();
//                            reply0.recycle();
//                        }
//                    }
//
//                    @Override
//                    public void onServiceDisconnected(ComponentName name) {
//
//                    }
//                }, BIND_AUTO_CREATE);
//                serviceIntent.setComponent(new ComponentName("com.mytest.mytestdemo", "com.mytest.mytestdemo.BeanService"));
//                getActivity().bindService(serviceIntent, new ServiceConnection() {
//                    @Override
//                    public void onServiceConnected(ComponentName name, IBinder service) {
//                        bookController = BeanControllerAidl.Stub.asInterface(service);
//                        try {
//                            List<Bean> bookList = bookController.getBeanList();
//                            Log.e("BeanService",bookList.toString());
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onServiceDisconnected(ComponentName name) {
//
//                    }
//                }, BIND_AUTO_CREATE);
//            }
//        });
        return rootView;
    }
}
