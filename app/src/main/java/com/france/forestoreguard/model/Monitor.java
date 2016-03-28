package com.france.forestoreguard.model;

import com.baidu.mapapi.model.LatLng;

import java.util.Date;

/**
 * Created by Administrator on 2016/3/13.
 */
public class Monitor {
    private String forest_id;
    private double latitude;
    private double longitude;
    private boolean forest_status;
    private DeviceStatus mDeviceStatus;
    private boolean isFired =false;
    private boolean isFelled=false;
    private String fireTime;
    private String fellTime;
    private String searchTime;
    public Monitor(String forest_id,  double latitude,double longitude, boolean forest_status) {
        this.forest_id = forest_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.forest_status = forest_status;
    }

    public String getForest_id() {
        return forest_id;
    }

    public void setForest_id(String forest_id) {
        this.forest_id = forest_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public boolean isForest_status() {
        return forest_status;
    }

    public void setForest_status(boolean forest_status) {
        this.forest_status = forest_status;
    }

    public DeviceStatus getmDeviceStatus() {
        return mDeviceStatus;
    }

    public Monitor setmDeviceStatus(DeviceStatus mDeviceStatus) {
        this.mDeviceStatus = mDeviceStatus;
        return this;
    }
    public LatLng getLatLng(){
        return new LatLng(latitude,longitude);
    }

    public Monitor(String forest_id, double latitude, double longitude, boolean forest_status, boolean isFired, boolean isFelled) {
        this.forest_id = forest_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.forest_status = forest_status;
        this.isFired = isFired;
        this.isFelled = isFelled;
    }

    public boolean isFired() {
        return isFired;
    }

    public void setIsFired(boolean isFired) {
        this.isFired = isFired;
    }

    public boolean isFelled() {
        return isFelled;
    }

    public void setIsFelled(boolean isFelled) {
        this.isFelled = isFelled;
    }

    public String getFellTime() {
        return fellTime;
    }

    public Monitor setFellTime(String fellTime) {
        this.fellTime = fellTime;
        return this;
    }

    public String getFireTime() {
        return fireTime;
    }

    public Monitor setFireTime(String fireTime) {
        this.fireTime = fireTime;
        return this;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public Monitor setSearchTime(String searchTime) {
        this.searchTime = searchTime;
        return this;
    }
}
