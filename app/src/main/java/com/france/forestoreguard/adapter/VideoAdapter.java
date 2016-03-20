package com.france.forestoreguard.adapter;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.france.forestoreguard.R;
import com.france.forestoreguard.listener.DownloadProgressListener;
import com.france.forestoreguard.model.Video;
import com.france.forestoreguard.util.FileDownloader;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class VideoAdapter extends BaseAdapter {
    private static final int PROCESSING = 1;
    private static final int FAILURE = -1;
    List<Video> videoList= new ArrayList();
    Context ct;
    private LayoutInflater inflater;
    private List<ProgressBar> progressBarList;
    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int i) {
        return videoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    static class ViewHolder{
        TextView  videoTime;
        RelativeLayout layout_download;
        ProgressBar progressBar;
        DownloadTask task;//每个view都可能带有一个Task
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final Video video=videoList.get(i);
        if(view==null){
            view=inflater.inflate(R.layout.view_item_video,viewGroup,false);
            viewHolder=new ViewHolder();
            //find hold
            viewHolder.videoTime=(TextView)view.findViewById(R.id.video_time);
            viewHolder.layout_download=(RelativeLayout)view.findViewById(R.id.layout_download);
            viewHolder.progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            progressBarList.add(i,viewHolder.progressBar);//progressBar 不能序列化 故放在List中
            viewHolder.layout_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("zjx", "cllllllllllll:" + view.getTag());
                    Video video1=videoList.get(Integer.valueOf(view.getTag().toString()));
                    String url=video1.getVoiceUrl();
                    String fileName=url.substring(url.lastIndexOf('/') + 1);
                    try {
                        // URL编码（这里是为了将中文进行URL编码）不整个url都编码是防止'/ .'也被编码
                        fileName = URLEncoder.encode(fileName, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    url=url.substring(0,url.lastIndexOf('/')+1)+fileName;
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File savDir = Environment.getExternalStorageDirectory();
                        File saveDir=new File(savDir.getPath()+File.separator+"forestoreGuard");
//                        download(url, savDir,viewHolder.progressBar);
                        //先去文件夹下找有没有存在文件
                        if(existFile(saveDir,fileName)){
                            Toast.makeText(ct,R.string.download_already,Toast.LENGTH_SHORT).show();
                        }else {
                            viewHolder.task = new DownloadTask(url, saveDir, i);
                            viewHolder.progressBar.setVisibility(View.VISIBLE);
                            viewHolder.progressBar.setProgress(0);
                            new Thread(viewHolder.task).start();
                            Log.i("zjx", "fileName:" + fileName + " &url=" + url + " &savDir:" + savDir);
                        }
                    } else {
                        Log.i("zjx","无sd卡");
                    }
                }
            });
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.layout_download.setTag(i);
        viewHolder.videoTime.setText(video.getVoiceTime());
        return view;
    }
    private boolean existFile(File saveDir,String filename){
        if(!saveDir.exists())return false;
        File file=new File(saveDir.getPath()+File.separator+filename);
        if(!file.exists())return false;
        return true;
    }
    private Handler handler = new UIHandler();

    private final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESSING: // 更新进度
                    ProgressBar progressBar=progressBarList.get(msg.getData().getInt("index"));
                    progressBar.setProgress(msg.getData().getInt("size"));
                    if (progressBar.getProgress() == progressBar.getMax()) { // 下载完成
                        Toast.makeText(ct,R.string.download_success,Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    break;
                case FAILURE:Toast.makeText(ct,R.string.download_fail,Toast.LENGTH_SHORT).show();break;
            }
        }
    }
    private final class DownloadTask implements Runnable {
        private String path;
        private File saveDir;
        private FileDownloader loader;
        private int index;
        public DownloadTask(String path, File saveDir,int index) {
            this.path = path;
            this.saveDir = saveDir;
            this.index=index;
        }

        /**
         * 退出下载
         */
        public void exit() {
            if (loader != null)
                loader.exit();
        }
        //写个接口用来回调
        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
            @Override
            public void onDownloadSize(int size) {
                Message msg = new Message();
                msg.what = PROCESSING;
                msg.getData().putInt("size", size);
                msg.getData().putInt("index", index);
                handler.sendMessage(msg);
            }
        };

        public void run() {
            try {
                // 实例化一个文件下载器
                loader = new FileDownloader(ct, path,saveDir, 3);
                // 设置进度条最大值
                progressBarList.get(index).setMax(loader.getFileSize());
                loader.download(downloadProgressListener);
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendMessage(handler.obtainMessage(FAILURE)); // 发送一条空消息对象
            }
        }
    }
    public VideoAdapter(Context ct, List<Video> videoList) {
        this.ct = ct;
        this.videoList = videoList;
        inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        progressBarList=new ArrayList<>();
    }
}
