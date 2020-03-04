/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Administrator\\Desktop\\MyTestDemo\\app\\src\\main\\aidl\\com\\mytest\\mytestdemo\\BeanControllerAidl.aidl
 */
package com.mytest.mytestdemo;
public interface BeanControllerAidl extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.mytest.mytestdemo.BeanControllerAidl
{
private static final java.lang.String DESCRIPTOR = "com.mytest.mytestdemo.BeanControllerAidl";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.mytest.mytestdemo.BeanControllerAidl interface,
 * generating a proxy if needed.
 */
public static com.mytest.mytestdemo.BeanControllerAidl asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.mytest.mytestdemo.BeanControllerAidl))) {
return ((com.mytest.mytestdemo.BeanControllerAidl)iin);
}
return new com.mytest.mytestdemo.BeanControllerAidl.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
java.lang.String descriptor = DESCRIPTOR;
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(descriptor);
return true;
}
case TRANSACTION_getBeanList:
{
data.enforceInterface(descriptor);
java.util.List<com.mytest.mytestdemo.Bean> _result = this.getBeanList();
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_addBeanInOut:
{
data.enforceInterface(descriptor);
com.mytest.mytestdemo.Bean _arg0;
if ((0!=data.readInt())) {
_arg0 = com.mytest.mytestdemo.Bean.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.addBeanInOut(_arg0);
reply.writeNoException();
if ((_arg0!=null)) {
reply.writeInt(1);
_arg0.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
default:
{
return super.onTransact(code, data, reply, flags);
}
}
}
private static class Proxy implements com.mytest.mytestdemo.BeanControllerAidl
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.util.List<com.mytest.mytestdemo.Bean> getBeanList() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<com.mytest.mytestdemo.Bean> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getBeanList, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(com.mytest.mytestdemo.Bean.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void addBeanInOut(com.mytest.mytestdemo.Bean bean) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((bean!=null)) {
_data.writeInt(1);
bean.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_addBeanInOut, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
bean.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getBeanList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_addBeanInOut = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public java.util.List<com.mytest.mytestdemo.Bean> getBeanList() throws android.os.RemoteException;
public void addBeanInOut(com.mytest.mytestdemo.Bean bean) throws android.os.RemoteException;
}
