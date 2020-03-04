// BeanControllerAidl.aidl
package com.mytest.mytestdemo;

// Declare any non-default types here with import statements
import com.mytest.mytestdemo.Bean;
interface BeanControllerAidl {
 List<Bean> getBeanList();
    void addBeanInOut(inout Bean bean);
}
