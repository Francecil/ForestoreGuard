package com.france.forestoreguard.model;

/**
 * Created by Administrator on 2016/3/16.
 */
public class Video {
    private String forestID;
    private String voiceUrl;
    //录音时间
    private String voiceTime;

    public Video(String forestID, String voiceUrl, String voiceTime) {
        this.forestID = forestID;
        this.voiceUrl = voiceUrl;
        this.voiceTime = voiceTime;
    }

    public String getForestID() {
        return forestID;
    }

    public void setForestID(String forestID) {
        this.forestID = forestID;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }


    public String getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(String voiceTime) {
        this.voiceTime = voiceTime;
    }
}
