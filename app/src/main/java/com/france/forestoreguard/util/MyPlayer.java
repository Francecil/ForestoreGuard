package com.france.forestoreguard.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MyPlayer implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer; // 媒体播放器
    private SeekBar seekBar; // 拖动条
    private TextView leafTime;
    private Timer mTimer = new Timer(); // 计时器
    // 初始化播放器
    public MyPlayer(SeekBar seekBar,TextView leafTime) {
        super();
        this.seekBar = seekBar;
        this.leafTime = leafTime;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 每一秒触发一次
        mTimer.schedule(timerTask, 0, 1000);
    }
    // 计时器
    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && seekBar.isPressed() == false) {
                handler.sendEmptyMessage(0); // 发送消息
            }
        }
    };
    /*
    getCurrentPosition:获取当前播放到的位置 单位ms
    getDuration:获取总时长
    seekBar.getMax() * position / duration; getMax=100 故取值在0-100之间
     */
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            int leaf=(duration-position)/1000;
            int min=leaf/60;String smin=min<10?"0"+min:""+min;
            int sec=leaf%60;String ssec=sec<10?"0"+sec:""+sec;
            leafTime.setText(smin+":"+ssec);
//            Log.i("zjx","time:"+duration+" & position:"+position);
            if (duration > 0) {
                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
//                long pos = seekBar.getMax() * position / duration;
////                Log.i("zjx","pos:"+pos);
//                seekBar.setProgress((int) pos);
                seekBar.setProgress(position/1000);//当前播放位置 (S)
            }
        };
    };
    public void rePlay(){
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
    public void play() {
        mediaPlayer.start();
    }

    /**
     *
     * @param url
     *            url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
            seekBar.setMax(mediaPlayer.getDuration() / 1000);//总长度的设置从100改为真正长度（S）
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 暂停
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.e("mediaPlayer", "onCompletion");
    }

    /**
     * 缓冲更新，具体细节和技术需要研究
     * update:利用OnBufferingUpdateListener实现
     * 默认缓冲完 进度条消失的bug
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //percent 0-100
        seekBar.setSecondaryProgress(seekBar.getMax()*percent/100);
        Log.e("zjx","buffer:"+percent);
//        if(percent==100)seekBar.setSecondaryProgress(99);
//        int currentProgress = seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
//        Log.e(currentProgress + "% play", percent + " buffer");
    }
}
