package com.france.forestoreguard;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2016/3/12.
 */
public class MyApplication extends Application {

    public static MyApplication mInstance;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mInstance = this;
        init();
    }
    public static MyApplication getInstance() {
        return mInstance;
    }
    private void init(){
        initMap();
    }
    private void initMap(){
        //baiduMap 初始化sdk
        SDKInitializer.initialize(mInstance);
    }


}