package com.bawei.test.houyongliang20161121;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;
    private TextView tv_address;
    private Button bt_tiaozhuan;
    LocationClient mLocClient;
    /*位置监听*/
    public MyLocationListenner myListener = new MyLocationListenner();
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    String address="";
    String addpath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        /*初始化视图*/
        initView();
        /*定位*/
        dingWei();
    }
    /*展示定位信息*/
    public void show(View v){
        tv_address.setText(address);
        Toast.makeText(this, addpath, Toast.LENGTH_SHORT).show();
    }
    /*跳转*/
    public void jump(View v){
        startActivity(new Intent(MainActivity.this,RecyclerViewActivity.class));
    }
    private void dingWei() {
        /*6.0 再次注册权限*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            initMap();
        }else {
            initMap();
        }


    }
/* 地图层获取 并展示数据*/
    private void initMap() {
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        /*注册监听*/
        mLocClient.registerLocationListener(myListener);
        /*参数设置*/
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);/*设置定位时间间隔*/
        mLocClient.setLocOption(option);/*参数进行设置*/
        /*开启定位*/
        mLocClient.start();

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
/*数据从 location 取出 */
        @Override
        public void onReceiveLocation(BDLocation location) {


            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            StringBuffer sb = new StringBuffer(256);
           sb.append("Time : ");
            sb.append(location.getTime());
            sb.append("\nError code : ");
            sb.append(location.getLocType());
            sb.append("\nLatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nLontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nRadius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nSpeed : ");
                sb.append(location.getSpeed());
                sb.append("\nSatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\nAddress : ");
                sb.append(location.getAddrStr());
            }
            tv_address.setText(sb.toString());
            double weidu=location.getAltitude();
            double jingdu=location.getLatitude();
//            tv_address.setText("位置"+location.getAddrStr()+"经纬度"+jingdu+"___"+weidu);
            addpath="经纬度"+jingdu+"___"+weidu;
            address="位置"+location.getAddrStr()+"经纬度"+jingdu+"___"+weidu;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    private void initView() {
        mMapView= (MapView) findViewById(R.id.bmapView);
        tv_address= (TextView) findViewById(R.id.tv_address);
        bt_tiaozhuan= (Button) findViewById(R.id.bt_tiaozhuan);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView=null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
