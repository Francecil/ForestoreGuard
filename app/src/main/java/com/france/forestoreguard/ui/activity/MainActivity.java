package com.france.forestoreguard.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MonitorMarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.france.forestoreguard.R;
import com.france.forestoreguard.config.MonitorConfig;
import com.france.forestoreguard.model.DeviceStatus;
import com.france.forestoreguard.model.Monitor;
import com.france.forestoreguard.ui.BaseActivity;
import com.france.forestoreguard.ui.customView.ZoomControlView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends BaseActivity {

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    BitmapDescriptor mCurrentMarker;
    //设置定位图标旁边圆圈,如下设置为空，默认设置是浅蓝色
    private static final int accuracyCircleFillColor = 0x00000000;
    private static final int accuracyCircleStrokeColor = 0x00000000;
    //UI
    private Button[] buttonTab;
    private int index;
    private int currentTabIndex;//当前所处Tap
    //自定义缩放控件
    private ZoomControlView mZoomControlView;
    MapView mMapView;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    ArrayList<Monitor> monitors = new ArrayList<>();
    //    latitude: 26.057144, longitude: 119.199095
    //监测点点击详情
    HashMap<String, Marker> indexDetailOptions = new HashMap<String, Marker>();
    HashMap<String, Marker> fireDetailOptions = new HashMap<String, Marker>();
    HashMap<String, Marker> fellDetailOptions = new HashMap<String, Marker>();
    HashMap<String,Marker> naviDetailOptions=new HashMap<>();
    HashMap<String, List<Marker>> indexMarkers = new HashMap<>();
    HashMap<String, List<Marker>> fireMarkers = new HashMap<>();
    HashMap<String, List<Marker>> fellMarkers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initMap();
        initClickListener();
        initMonitors();//耗时操作
    }

    private void showIndex() {
        Iterator iterator = monitors.iterator();
        while (iterator.hasNext()) {
            Monitor monitor = (Monitor) iterator.next();
            if (monitor.isFired()) {
                List<Marker> markers = fireMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(false);
                    markers.get(1).setVisible(false);
                }
            }
            if (monitor.isFelled()) {
                List<Marker> markers = fellMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(false);
                    markers.get(1).setVisible(false);
                }
            }
            List<Marker> markers = indexMarkers.get(monitor.getForest_id());
            markers.get(0).setVisible(true);
            markers.get(1).setVisible(true);
            markers.get(1).setToTop();
            markers.get(0).setToTop();
        }
    }

    private void showFire() {
        Iterator iterator = monitors.iterator();
        int monitorIndex = 0;
        while (iterator.hasNext()) {
            Monitor monitor = (Monitor) iterator.next();
            if (monitor.isFelled()) {
                List<Marker> markers = fellMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(false);
                    markers.get(1).setVisible(false);
                }
            }
            if (monitor.isFired()) {
                List<Marker> markers0 = indexMarkers.get(monitor.getForest_id());
                markers0.get(0).setVisible(false);
                markers0.get(1).setVisible(false);
                List<Marker> markers = fireMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(true);
                    markers.get(1).setVisible(true);
                    markers.get(1).setToTop();
                    markers.get(0).setToTop();
                } else {
                    BitmapDescriptor map_fire_fire = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fire_fire);
                    BitmapDescriptor map_fire_circle = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fire_circle);
                    List<Marker> markers2 = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    bundle.putInt(MonitorConfig.MONITOR_ID, monitorIndex);
                    bundle.putString(MonitorConfig.MONITOR_INFO, MonitorConfig.MONITOR_FIRE);
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fire_fire).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fire_circle).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    fireMarkers.put(monitor.getForest_id(), markers2);
                }
            } else {
                List<Marker> markers0 = indexMarkers.get(monitor.getForest_id());
                markers0.get(0).setVisible(true);
                markers0.get(1).setVisible(true);
            }
            monitorIndex++;
        }
    }

    private void showFell() {
        Iterator iterator = monitors.iterator();
        int monitorIndex = 0;
        while (iterator.hasNext()) {
            Monitor monitor = (Monitor) iterator.next();
            if (monitor.isFired()) {
                List<Marker> markers = fireMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(false);
                    markers.get(1).setVisible(false);
                }
            }
            if (monitor.isFelled()) {
                List<Marker> markers0 = indexMarkers.get(monitor.getForest_id());
                markers0.get(0).setVisible(false);
                markers0.get(1).setVisible(false);
                List<Marker> markers = fellMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(true);
                    markers.get(1).setVisible(true);
                    markers.get(1).setToTop();
                    markers.get(0).setToTop();
                } else {
                    BitmapDescriptor map_fell_fell = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fell_fell);
                    BitmapDescriptor map_fell_circle = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fell_circle);
                    List<Marker> markers2 = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    bundle.putInt(MonitorConfig.MONITOR_ID, monitorIndex);
                    bundle.putString(MonitorConfig.MONITOR_INFO, MonitorConfig.MONITOR_FELL);
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fell_fell).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fell_circle).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    fellMarkers.put(monitor.getForest_id(), markers2);
                }
            } else {
                List<Marker> markers0 = indexMarkers.get(monitor.getForest_id());
                markers0.get(0).setVisible(true);
                markers0.get(1).setVisible(true);
            }
            monitorIndex++;
        }
    }

    private void showNavi() {
        Iterator iterator = monitors.iterator();
        int monitorIndex = 0;
        boolean hasMarker=naviDetailOptions!=null&&naviDetailOptions.size()>0;
        while (iterator.hasNext()) {
            Monitor monitor = (Monitor) iterator.next();
            if (monitor.isFelled()) {
                List<Marker> markers0 = indexMarkers.get(monitor.getForest_id());
                markers0.get(0).setVisible(false);
                markers0.get(1).setVisible(false);
                List<Marker> markers = fellMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(true);
                    markers.get(1).setVisible(true);
                    markers.get(1).setToTop();
                    markers.get(0).setToTop();
                } else {
                    BitmapDescriptor map_fell_fell = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fell_fell);
                    BitmapDescriptor map_fell_circle = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fell_circle);
                    List<Marker> markers2 = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    bundle.putInt(MonitorConfig.MONITOR_ID, monitorIndex);
                    bundle.putString(MonitorConfig.MONITOR_INFO, MonitorConfig.MONITOR_FELL);
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fell_fell).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fell_circle).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    fellMarkers.put(monitor.getForest_id(), markers2);
                }
            } else if (monitor.isFired()){
                List<Marker> markers0 = indexMarkers.get(monitor.getForest_id());
                markers0.get(0).setVisible(false);
                markers0.get(1).setVisible(false);
                List<Marker> markers = fireMarkers.get(monitor.getForest_id());
                if (markers != null && markers.size() > 0) {
                    markers.get(0).setVisible(true);
                    markers.get(1).setVisible(true);
                    markers.get(1).setToTop();
                    markers.get(0).setToTop();
                } else {
                    BitmapDescriptor map_fire_fire = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fire_fire);
                    BitmapDescriptor map_fire_circle = BitmapDescriptorFactory
                            .fromResource(R.drawable.map_fire_circle);
                    List<Marker> markers2 = new ArrayList<>();
                    Bundle bundle = new Bundle();
                    bundle.putInt(MonitorConfig.MONITOR_ID, monitorIndex);
                    bundle.putString(MonitorConfig.MONITOR_INFO, MonitorConfig.MONITOR_FIRE);
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fire_fire).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    markers2.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(map_fire_circle).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                    fireMarkers.put(monitor.getForest_id(), markers2);
                }
            }else{
                List<Marker> markers0 = indexMarkers.get(monitor.getForest_id());
                markers0.get(0).setVisible(true);
                markers0.get(1).setVisible(true);
            }
            //设置巡察时间;
            if(!hasMarker) {
                Bundle bundle = new Bundle();
                bundle.putInt(MonitorConfig.MONITOR_ID, monitorIndex);
                bundle.putString(MonitorConfig.MONITOR_INFO, MonitorConfig.MONITOR_NAVI);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_marker_monitor_navitime, null);
                TextView textView=(TextView)view.findViewById(R.id.time_search);
                textView.setText(monitor.getSearchTime());
                BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view));
                naviDetailOptions.put(monitor.getForest_id(), (Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(markerIcon).anchor(0.5f, 0f).setMonitor(monitor).extraInfo(bundle)));
            }
            Marker marker=naviDetailOptions.get(monitor.getForest_id());
            marker.setVisible(true);
            marker.setToTop();
            monitorIndex++;
        }
    }

    /*
    清除index详细，在进行其他页面or新点击marker都要进行此操作
     */
    private void clearIndexDetailMarkers() {
        Iterator iterator = indexDetailOptions.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry map = (HashMap.Entry) iterator.next();
            Marker marker = (Marker) map.getValue();
            marker.setVisible(false);
        }
    }

    private void clearFireDetailMarkers() {
        Iterator iterator = fireDetailOptions.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry map = (HashMap.Entry) iterator.next();
            Marker marker = (Marker) map.getValue();
            marker.setVisible(false);
        }
    }

    private void clearFellDetailMarkers() {
        Iterator iterator = fellDetailOptions.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry map = (HashMap.Entry) iterator.next();
            Marker marker = (Marker) map.getValue();
            marker.setVisible(false);
        }
    }
    private void clearNaviDetailMarkers(){
        Iterator iterator = naviDetailOptions.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry map = (HashMap.Entry) iterator.next();
            Marker marker = (Marker) map.getValue();
            marker.setVisible(false);
        }
    }
    //    private void clearIndex(){}
//    private void clearFire(){}
//    private void clearFell(){}
//    private void clearNavi(){}
    private void initUI() {
        buttonTab = new Button[5];
        currentTabIndex = -1;
        buttonTab[0] = (Button) findViewById(R.id.map_button_fire);
        buttonTab[1] = (Button) findViewById(R.id.map_button_fell);
        buttonTab[2] = (Button) findViewById(R.id.map_button_navi);
        buttonTab[3] = (Button) findViewById(R.id.map_button_video);
        buttonTab[4] = (Button) findViewById(R.id.map_other2index);
    }

    private void initButtonClickListener() {
    }

    private void initMapClickListener() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             * @param point 点击的地理坐标
             */
            public void onMapClick(LatLng point) {
                clearIndexDetailMarkers();
//                clearFireDetailMarkers();
//                clearFellDetailMarkers();
                mBaiduMap.hideInfoWindow();
            }

            /**
             * 地图内 Poi 单击事件回调函数
             * @param poi 点击的 poi 信息
             */
            public boolean onMapPoiClick(MapPoi poi) {
                return true;
            }
        });
        //对 marker 添加点击相应事件
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Bundle bundle = arg0.getExtraInfo();
                mBaiduMap.hideInfoWindow();
                if (bundle != null) {
                    Monitor monitor = monitors.get(bundle.getInt(MonitorConfig.MONITOR_ID));
                    //开启or关闭info
                    ShowLog("MONITOR_ID:" + bundle.getString(MonitorConfig.MONITOR_INFO));
                    switch (bundle.getString(MonitorConfig.MONITOR_INFO)) {
                        case MonitorConfig.MONITOR_INDEX:{
                            if(currentTabIndex!=-1)return false;
                            generateMarker(monitor);
                            break;
                        }
                        case MonitorConfig.MONITOR_FIRE: {
                            if(currentTabIndex!=0)return false;//可能是在fell页点击的时候 fire处于隐藏,重复了
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_marker_monitor_time, null);
                            TextView time_day=(TextView)view.findViewById(R.id.time_day);
                            TextView time_second=(TextView)view.findViewById(R.id.time_second);
                            SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
                            time_day.setText(df.format(monitor.getFireTime()));
                            SimpleDateFormat df2=new SimpleDateFormat("hh:mm:ss");
                            time_second.setText(df2.format(monitor.getFireTime()));
                            InfoWindow infoWindownew = new InfoWindow(view, monitor.getLatLng(), 110);
                            mBaiduMap.showInfoWindow(infoWindownew);
                            break;
                        }
                        case MonitorConfig.MONITOR_FELL: {
                            if(currentTabIndex!=1)return false;//可能是在fell页点击的时候 fire处于隐藏,重复了
                            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_marker_monitor_time, null);
                            TextView time_day=(TextView)view.findViewById(R.id.time_day);
                            TextView time_second=(TextView)view.findViewById(R.id.time_second);
                            SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
                            time_day.setText(df.format(monitor.getFellTime()));
                            SimpleDateFormat df2=new SimpleDateFormat("hh:mm:ss");
                            time_second.setText(df2.format(monitor.getFellTime()));
                            InfoWindow infoWindownew = new InfoWindow(view, monitor.getLatLng(), 110);
                            mBaiduMap.showInfoWindow(infoWindownew);
                            break;
                        }
                    }
                    return true;
                }
                return false;//由于我们点击其中的一个marker(中心眼)不一定就处理 所以需要继续
            }
        });
    }

    private void initClickListener() {
        initButtonClickListener();
        initMapClickListener();
    }

    private void generateMarker(Monitor monitor) {
        clearIndexDetailMarkers();
        if (!indexDetailOptions.containsKey(monitor.getForest_id())) {
            ShowLog("未找到" + monitor.getForest_id() + "的infoWindow");
            //这边为去后台找数据的过程
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_marker_monitor_status, null);
            if (!monitor.isForest_status()) {
                ImageView status_video = (ImageView) view.findViewById(R.id.status_video);
                ImageView status_net = (ImageView) view.findViewById(R.id.status_net);
                ImageView status_camera = (ImageView) view.findViewById(R.id.status_camera);
                if (!monitor.getmDeviceStatus().isCam_sta()) {
                    status_camera.setImageResource(R.drawable.map_index_status_no);
                }
                if (!monitor.getmDeviceStatus().isCom_sta()) {
                    status_net.setImageResource(R.drawable.map_index_status_no);
                }
                if (!monitor.getmDeviceStatus().isMic_sta()) {
                    status_video.setImageResource(R.drawable.map_index_status_no);
                }
            }
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromBitmap(getViewBitmap(view));
            MonitorMarkerOptions oo = new MonitorMarkerOptions().position(new LatLng(monitor.getLatLng().latitude, monitor.getLatLng().longitude + 0.0005)).icon(markerIcon).zIndex(19).anchor(0, 0.3f);
            Marker marker = (Marker) mBaiduMap.addOverlay(oo);
            marker.setToTop();
            indexDetailOptions.put(monitor.getForest_id(), marker);
        } else {
            ShowLog("对marker的引用，并不产生新对象");
            Marker marker = indexDetailOptions.get(monitor.getForest_id());//对marker的引用，并不产生新对象
            marker.setVisible(true);
            marker.setToTop();
        }

    }

    private void initMonitors() {
        monitors.add(new Monitor("aaa", 26.067144, 119.209095, false, false, false).setmDeviceStatus(new DeviceStatus(false, true, true)).setSearchTime("13:20 ~ 14.20"));
        monitors.add(new Monitor("bbb", 26.047144, 119.189095, true, false, true).setmDeviceStatus(new DeviceStatus(true, true, true)).setFellTime(new Date("2010/04/04 04:04:04")).setSearchTime("14:50 ~ 15.20"));
        monitors.add(new Monitor("ccc", 26.047144, 119.209095, false, true, false).setmDeviceStatus(new DeviceStatus(false, false, true)).setFireTime(new Date("2010/01/01 01:01:01")).setSearchTime("16:20 ~ 17.20"));
        monitors.add(new Monitor("ddd", 26.067144, 119.189095, false, true, false).setmDeviceStatus(new DeviceStatus(true, true, false)).setFireTime(new Date("2010/03/03 03:03:03")).setSearchTime("09:20 ~ 11.20"));

        //构建Marker图标 相同图案的 icon 的 Marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间
        BitmapDescriptor status_green_eye = BitmapDescriptorFactory
                .fromResource(R.drawable.map_index_status_green);
        BitmapDescriptor status_green_circle = BitmapDescriptorFactory
                .fromResource(R.drawable.map_index_status_green_circle);
        ArrayList<BitmapDescriptor> status_green = new ArrayList<BitmapDescriptor>();
        status_green.add(status_green_circle);
        status_green.add(status_green_eye);
        BitmapDescriptor status_red_eye = BitmapDescriptorFactory
                .fromResource(R.drawable.map_index_status_red);
        BitmapDescriptor status_red_circle = BitmapDescriptorFactory
                .fromResource(R.drawable.map_index_status_red_circle);
        Iterator<Monitor> iterator = monitors.iterator();
        int monitorIndex = 0;
        while (iterator.hasNext()) {
            Bundle bundle = new Bundle();
            bundle.putInt(MonitorConfig.MONITOR_ID, monitorIndex++);
            bundle.putString(MonitorConfig.MONITOR_INFO, MonitorConfig.MONITOR_INDEX);
            Monitor monitor = iterator.next();
            List<Marker> markers = new ArrayList<>();
            if (monitor.isForest_status()) {
                markers.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(status_green_eye).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                markers.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(status_green_circle).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
            } else {
                markers.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(status_red_eye).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
                markers.add((Marker) mBaiduMap.addOverlay(new MonitorMarkerOptions().position(monitor.getLatLng()).zIndex(19).icon(status_red_circle).anchor(0.5f, 0.5f).setMonitor(monitor).extraInfo(bundle)));
            }
            indexMarkers.put(monitor.getForest_id(), markers);
        }
    }

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //卫星地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        //隐藏自带的地图缩放控件
        mMapView.showZoomControls(false);
        mZoomControlView = (ZoomControlView) findViewById(R.id.zoomControlView);
        mZoomControlView.setMapView(mMapView);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    //view 转Bitmap
    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0, addViewContent.getMeasuredWidth(), addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }
    public void cleadAllMarkers(){
        clearIndexDetailMarkers();
        clearFireDetailMarkers();
        clearFellDetailMarkers();
        clearNaviDetailMarkers();
        mBaiduMap.hideInfoWindow();
    }
    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.map_button_fire:
                index = 0;
                break;
            case R.id.map_button_fell:
                index = 1;
                break;
            case R.id.map_button_navi:
                index = 2;
                break;
            case R.id.map_button_video:
                startAnimActivity(VideoActivity.class);
                return;
            case R.id.map_other2index:
                index = -1;
                break;
            default:
                break;
        }
        cleadAllMarkers();
        if (currentTabIndex != -1) {
            buttonTab[currentTabIndex].setSelected(false);
        }
        currentTabIndex = index;
        if (index != -1) {
            //把当前tab设为选中状态
            buttonTab[4].setVisibility(View.VISIBLE);
            buttonTab[index].setSelected(true);
            //进入状态index 耗时操作..
            switch (index) {
                case 0:
                    showFire();
                    break;
                case 1:
                    showFell();
                    break;
                case 2:
                    showNavi();
                    break;
            }
        } else {
            buttonTab[4].setVisibility(View.GONE);
            showIndex();
            //进入初始状态-1..
        }

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.map_location);
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker,
                    accuracyCircleFillColor, accuracyCircleStrokeColor));
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                ShowLog(ll.toString());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);//1cm:50m

                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        super.onPause();
    }
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if(currentTabIndex<=2&&currentTabIndex>=0){
            buttonTab[currentTabIndex].setSelected(false);
            buttonTab[4].setVisibility(View.GONE);
            currentTabIndex=index=-1;
            showIndex();
            return;
        }
        if (doubleBackToExitPressedOnce) {//按两次
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
//		Toast.makeText(this,R.string.doubleclick, Toast.LENGTH_SHORT).show();
        ShowToast(R.string.doubleBackToExitPressedOnce);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
