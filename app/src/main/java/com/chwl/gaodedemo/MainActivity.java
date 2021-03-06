package com.chwl.gaodedemo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private AMapLocationClient mLocationClient = null;
    private double lon, lat;
    private AMap aMap;
    //声明mLocationOption对象  
    public AMapLocationClientOption mLocationOption = null;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);
        mButton = (Button) findViewById(R.id.btn_dao);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化定位  
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听  
        mLocationClient.setLocationListener(mLocationListener);
        init();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AmapNaviActivity.class));
            }
        });
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setUpMap();
    }

    private void setUpMap() {
        //初始化定位参数  
        mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式  
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）  
        mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false  
        mLocationOption.setOnceLocation(false);
//设置是否强制刷新WIFI，默认为强制刷新  
        mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置  
        mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms  
        mLocationOption.setInterval(2000);
//给定位客户端对象设置定位参数  
        mLocationClient.setLocationOption(mLocationOption);
//启动定位  
        mLocationClient.startLocation();
    }

    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
//定位成功回调信息，设置相关消息  
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表  
                amapLocation.getLatitude();//获取纬度  
                amapLocation.getLongitude();//获取经度  
                amapLocation.getAccuracy();//获取精度信息  
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间  
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。  
                amapLocation.getCountry();//国家信息  
                amapLocation.getProvince();//省信息  
                amapLocation.getCity();//城市信息  
                amapLocation.getDistrict();//城区信息  
                amapLocation.getStreet();//街道信息  
                amapLocation.getStreetNum();//街道门牌号信息  
                amapLocation.getCityCode();//城市编码  
                amapLocation.getAdCode();//地区编码  
                amapLocation.getAoiName();//获取当前定位点的AOI信息  
                lat = amapLocation.getLatitude();
                lon = amapLocation.getLongitude();
                Log.v("pcw", "Country : " + amapLocation.getCountry() +
                        " province : " + amapLocation.getProvince() + " City : " + amapLocation.getCity() + " District : "
                        + amapLocation.getDistrict());


                // 设置当前地图显示为当前位置  
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 19));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(lat, lon));
                markerOptions.title("当前位置");
                markerOptions.visible(true);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                markerOptions.icon(bitmapDescriptor);
                aMap.addMarker(markerOptions);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。  
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        mLocationClient.onDestroy();
    }
}
