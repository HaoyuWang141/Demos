package com.example.myapplication;

import android.location.Location;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;

public class MyLocationSource implements LocationSource {
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private LocationSource.OnLocationChangedListener locationChangedListener;





    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}
