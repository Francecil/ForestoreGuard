package com.france.forestoreguard.ui.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.france.forestoreguard.R;

/**
 * Created by Administrator on 2016/3/13.
 */
public class ZoomControlView extends RelativeLayout implements View.OnClickListener {
    private Button mButtonZoomin;
    private Button mButtonZoomout;
    private MapView mapView;
    private int maxZoomLevel;
    private int minZoomLevel;

    public ZoomControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_layout_zoom, null);
        mButtonZoomin = (Button) view.findViewById(R.id.zoom_in);
        mButtonZoomout = (Button) view.findViewById(R.id.zoom_out);
        mButtonZoomin.setOnClickListener(this);
        mButtonZoomout.setOnClickListener(this);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        if(mapView == null){
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        BaiduMap mBaiduMap = mapView.getMap();
        switch (v.getId()) {

            case R.id.zoom_in:{
                float zoomLevel = mBaiduMap.getMapStatus().zoom;
                if(zoomLevel<=20){
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                    mButtonZoomout.setEnabled(true);
                }else{
                    mButtonZoomin.setEnabled(false);
                }
                break;
            }
            case R.id.zoom_out:{
                float zoomLevel = mBaiduMap.getMapStatus().zoom;
                if(zoomLevel>4){
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                    mButtonZoomin.setEnabled(true);
                }else{
                    mButtonZoomout.setEnabled(false);
                }
                break;
            }
        }
    }

    /**
     * 与MapView设置关联
     * @param mapView
     */
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
        // 获取最大的缩放级别
        maxZoomLevel = (int)mapView.getMap().getMaxZoomLevel();
        // 获取最小的缩放级别
        minZoomLevel = (int)mapView.getMap().getMinZoomLevel();
    }


    /**
     * 根据MapView的缩放级别更新缩放按钮的状态，当达到最大缩放级别，设置mButtonZoomin
     * 为不能点击，反之设置mButtonZoomout
     * @param level
     */
    public void refreshZoomButtonStatus(int level){
        if(mapView == null){
            throw new NullPointerException("you can call setMapView(MapView mapView) at first");
        }
        if(level > minZoomLevel && level < maxZoomLevel){
            if(!mButtonZoomout.isEnabled()){
                mButtonZoomout.setEnabled(true);
            }
            if(!mButtonZoomin.isEnabled()){
                mButtonZoomin.setEnabled(true);
            }
        }
        else if(level == minZoomLevel ){
            mButtonZoomout.setEnabled(false);
        }
        else if(level == maxZoomLevel){
            mButtonZoomin.setEnabled(false);
        }
    }

}
