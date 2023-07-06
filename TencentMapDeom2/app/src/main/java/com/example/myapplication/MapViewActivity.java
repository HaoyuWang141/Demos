package com.example.myapplication;

import android.Manifest;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationStyle;

public class MapViewActivity extends AppCompatActivity implements TencentLocationListener {

    /**
     * 由于SDK并没有提供用于MapView管理地图生命周期的Activity
     * 因此需要用户继承Activity后管理地图的生命周期，防止内存泄露
     */

    private TextView mStatus;
    private MapView mMapView;
    private TencentMap mTencentMap;
    private Marker mLocationMarker;
    private Circle mAccuracyCircle;

    private TencentLocation mLocation;
    private TencentLocationManager mLocationManager;

    // 用于记录定位参数, 以显示到 UI
    private String mRequestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TencentMapInitializer.setAgreePrivacy(true);
        TencentLocationManager.setUserAgreePrivacy(true);
        setContentView(R.layout.activity_map_view);

        mStatus = (TextView) findViewById(R.id.status);
        mStatus.setTextColor(Color.RED);

        initMapView();
        initLocation();

    }

    /**
     * 初始化地图
     */
    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.mapview);
        mTencentMap = mMapView.getMap();
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(9);
        mTencentMap.moveCamera(cameraUpdate);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
        mLocationManager = TencentLocationManager.getInstance(this);
        //创建定位请求
//        locationRequest = TencentLocationRequest.create();
        //设置定位周期（位置监听器回调周期）为3s
//        locationRequest.setInterval(3000);
    }

    /**
     * mapview的生命周期管理
     */
    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        stopLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMapView.onRestart();
    }

    /**
     * 腾讯定位SDK位置变化回调
     */
    @Override
    public void onLocationChanged(TencentLocation location, int error,
                                  String reason) {
        System.out.println(reason);
        Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
//        switch (error) {
//            case 0:
//                Toast.makeText(this, "定位成功", Toast.LENGTH_SHORT).show();
//            case 1:
//                Toast.makeText(this, "网络问题引起的定位失败", Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                Toast.makeText(this, "GPS, Wi-Fi 或基站错误引起的定位失败", Toast.LENGTH_SHORT).show();
//                break;
//            case 4:
//                Toast.makeText(this, "无法将WGS84坐标转换成GCJ-02坐标时的定位失败", Toast.LENGTH_SHORT).show();
//                break;
//            case 404:
//                Toast.makeText(this, "未知原因引起的定位失败", Toast.LENGTH_SHORT).show();
//                break;
//            default:
//                break;
//        }

        if (error == TencentLocation.ERROR_OK) {
            mLocation = location;

            // 定位成功
            StringBuilder sb = new StringBuilder();
            sb.append("定位参数=").append(mRequestParams).append("\n");
            sb.append("(纬度=").append(location.getLatitude()).append(",经度=")
                    .append(location.getLongitude()).append(",精度=")
                    .append(location.getAccuracy()).append("), 来源=")
                    .append(location.getProvider()).append(", 地址=")
                    .append(location.getAddress());
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // 更新 status
            mStatus.setText(sb.toString());

            // 更新 location Marker
            if (mLocationMarker == null) {
                mLocationMarker =
                        mTencentMap.addMarker(new MarkerOptions().
                                position(latLngLocation).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_location)));
            } else {
                mLocationMarker.setPosition(latLngLocation);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLngLocation);
                mTencentMap.moveCamera(cameraUpdate);
            }

            if (mAccuracyCircle == null) {
                mAccuracyCircle = mTencentMap.addCircle(new CircleOptions().
                        center(latLngLocation).
                        radius(location.getAccuracy()).
                        fillColor(0x884433ff).
                        strokeColor(0xaa1122ee).
                        strokeWidth(1));
            } else {
                mAccuracyCircle.setCenter(latLngLocation);
                mAccuracyCircle.setRadius(location.getAccuracy());
            }
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        // ignore
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(3000);
        mLocationManager.requestLocationUpdates(request, this);
        mRequestParams = request + ", 坐标系="
                + DemoUtils.toString(mLocationManager.getCoordinateType());
    }

    private void stopLocation() {
        mLocationManager.removeUpdates(this);
    }
}
