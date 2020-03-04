package com.mytest.mytestdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends Binder {

        public static final int binderCode = 111111;

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case binderCode:
                    int readdata = data.readInt();
                    reply.writeInt(readdata +1);
                    Log.e("MyBinder",data.readString());
                    break;
            }
            return true;
        }

    }

}
