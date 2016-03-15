package com.france.forestoreguard.model;

/**
 * Created by Administrator on 2016/3/13.
 * 返回数据说明：
 返回值字段 字段类型 字段说明
 mic_sta boolean 麦克风状态
 cam_sta boolean 相机状态
 com_sta boolean 通讯状态
 */
public class DeviceStatus {
    private boolean mic_sta;
    private boolean cam_sta;
    private boolean com_sta;
    public DeviceStatus(boolean mic_sta, boolean cam_sta, boolean com_sta) {
        this.mic_sta = mic_sta;
        this.cam_sta = cam_sta;
        this.com_sta = com_sta;
    }

    public boolean isMic_sta() {
        return mic_sta;
    }

    public void setMic_sta(boolean mic_sta) {
        this.mic_sta = mic_sta;
    }

    public boolean isCam_sta() {
        return cam_sta;
    }

    public void setCam_sta(boolean cam_sta) {
        this.cam_sta = cam_sta;
    }

    public boolean isCom_sta() {
        return com_sta;
    }

    public void setCom_sta(boolean com_sta) {
        this.com_sta = com_sta;
    }
}
