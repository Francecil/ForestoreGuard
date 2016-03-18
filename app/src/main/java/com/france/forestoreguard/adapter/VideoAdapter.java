package com.france.forestoreguard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.france.forestoreguard.R;
import com.france.forestoreguard.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class VideoAdapter extends BaseAdapter {
    List<Video> videoList= new ArrayList();
    Context ct;
    private LayoutInflater inflater;
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
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        final Video video=videoList.get(i);
        if(view==null){
            view=inflater.inflate(R.layout.view_item_video,viewGroup,false);
            viewHolder=new ViewHolder();
            //find hold
            viewHolder.videoTime=(TextView)view.findViewById(R.id.video_time);
            viewHolder.layout_download=(RelativeLayout)view.findViewById(R.id.layout_download);
            viewHolder.layout_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("zjx","cllllllllllll:"+view.getTag());
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

    public VideoAdapter(Context ct, List<Video> videoList) {
        this.ct = ct;
        this.videoList = videoList;
        inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
