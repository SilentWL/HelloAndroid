package com.example.administrator.baidumap;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;


public class MainActivity extends AppCompatActivity {
    private TextureMapView mBaiduMapView = null;
    BaiduMap mBaiduMap = null;
    LocationClient locationClient = null;
    private static int LOCATION_COUTNS = 0;
    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mBaiduMapView = (TextureMapView)findViewById(R.id.bmapView);
        mBaiduMap = mBaiduMapView.getMap();

        locationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd0911");
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setProdName("LocationDemo");
        option.setScanSpan(1000);
        locationClient.setLocOption(option);



        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null)
                    return;
                StringBuffer sb = new StringBuffer(256);
                sb.append("Time:");
                sb.append(bdLocation.getTime());
                sb.append("\n Error code:");
                sb.append(bdLocation.getLocType());
                sb.append("\nLatitude:");
                sb.append(bdLocation.getLatitude());
                sb.append("\nLongitude:");
                sb.append(bdLocation.getLongitude());
                sb.append("\nRadius:");
                sb.append(bdLocation.getRadius());

                if (bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                    sb.append("\nSpeed:");
                    sb.append(bdLocation.getSpeed());
                    sb.append("\nSatellite:");
                    sb.append(bdLocation.getSatelliteNumber());
                }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                    sb.append("\nAddress:");
                    sb.append(bdLocation.getAddrStr());
                }
                LOCATION_COUTNS++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));
                Log.w("onReceiveLocation",sb.toString());

                // map view 销毁后不在处理新接收的位置
                if (bdLocation == null || mBaiduMapView == null) {
                    return;

                }
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(ll).zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }
        });
        locationClient.start();
        locationClient.requestLocation();
    }
}
