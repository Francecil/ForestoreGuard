package com.france.forestoreguard.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.france.forestoreguard.R;
import com.france.forestoreguard.adapter.VideoAdapter;
import com.france.forestoreguard.model.Video;
import com.france.forestoreguard.ui.BaseActivity;
import com.france.forestoreguard.ui.customView.MyVisualizerView;
import com.france.forestoreguard.util.MyPlayer;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends BaseActivity {
    List<Video> videoList;
    ListView videoListView;
    VideoAdapter videoAdapter;
    // 定义播放声音的MediaPlayer
    private MyPlayer mPlayer;
    private SeekBar musicProgress;
    // 定义系统的示波器
    private Visualizer mVisualizer;
    // 创建MyVisualizerView组件，用于显示波形图
    private MyVisualizerView mVisualizerView;
    private ImageView playButton,stopButton;
    RelativeLayout waveLayout;
    Thread playThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置控制音乐声音
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_video);
        initUI();
        initList();
        initListener();
        // 初始化示波器
        setupVisualizer();
    }
    private void startOnlinePlay(final String url){
        playThread=new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayer.playUrl(url);
            }
        });
        playThread.start();
    }
    private void clearPlayer(){
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer = null;
        }
        clearThread();
        musicProgress.setProgress(0);
    }
    private void clearThread(){
        if(playThread!=null){
            playThread.interrupt();
            playThread=null;
        }
    }
    private void initUI(){
        playButton=(ImageView)findViewById(R.id.playButton);
        stopButton=(ImageView)findViewById(R.id.stopButton);
        videoListView=(ListView)findViewById(R.id.videoListView);
        waveLayout=(RelativeLayout)findViewById(R.id.waveLayout);
        musicProgress = (SeekBar) findViewById(R.id.music_progress);
        mPlayer = new MyPlayer(musicProgress);
        mVisualizerView =new MyVisualizerView(this);
    }
    private void initList(){
        videoList=new ArrayList<>();
        videoList.add(new Video("aaa","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.01.01"));
        videoList.add(new Video("bbb","http://abv.cn/music/光辉岁月.mp3","2016.02.02"));
        videoList.add(new Video("ccc","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.03.03"));
        videoList.add(new Video("ddd","http://abv.cn/music/光辉岁月.mp3","2016.04.04"));
        videoList.add(new Video("bbb","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.02.02"));
        videoList.add(new Video("ccc","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.03.03"));
        videoList.add(new Video("ddd","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.04.04"));
        videoList.add(new Video("bbb","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.02.02"));
        videoList.add(new Video("ccc","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.03.03"));
        videoList.add(new Video("ddd","http://120.24.251.94/3w/jalan/forestStore/resource/voice/dbz.mp3","2016.04.04"));
        videoAdapter=new VideoAdapter(VideoActivity.this,videoList);
        videoListView.setAdapter(videoAdapter);
    }
    private void initListener(){
        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                ShowLog("你点击了ListView条目" + index);//在LogCat中输出信息
                ShowLog("VideoID:" + ((Video) videoListView.getItemAtPosition(index)).getForestID());
                clearThread();
                startOnlinePlay(((Video)videoListView.getItemAtPosition(index)).getVoiceUrl());
            }
        });
    }
    //示波器的显示
    private void setupVisualizer()
    {

        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        // 将MyVisualizerView组件添加到layout容器中
        waveLayout.addView(mVisualizerView);
        // 以MediaPlayer的AudioSessionId创建Visualizer
        // 相当于设置Visualizer负责显示该MediaPlayer的音频数据
        mVisualizer = new Visualizer(mPlayer.mediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 为mVisualizer设置监听器
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    @Override
                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] fft, int samplingRate) {
                        //不采用fft（快速傅立叶）
                    }

                    @Override
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] waveform, int samplingRate) {
                        // 用waveform波形数据更新mVisualizerView组件
                        mVisualizerView.updateVisualizer(waveform);
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
    }
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * mPlayer.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            mPlayer.mediaPlayer.seekTo(progress);
        }

    }
    public void onVideoStatusChange(View view){
        switch (view.getId()){
            case R.id.playButton:mPlayer.rePlay();break;
            case R.id.stopButton:mPlayer.pause();break;
        }
    }
    @Override
    protected void onDestroy() {
        clearPlayer();
        mVisualizer.release();
        //注意销毁顺序
        super.onDestroy();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
    }
}
