package com.tencent.map.vector.demo.heatoverlay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.tencent.map.sdk.utilities.visualization.glmodel.GLModelOverlay;
import com.tencent.map.sdk.utilities.visualization.glmodel.GLModelOverlayProvider;
import com.tencent.map.vector.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.VectorOverlay;

import java.io.File;

import static com.tencent.map.sdk.utilities.visualization.glmodel.GLModelOverlayProvider.CoordType.PixelType;

public class GLModelActivity extends AppCompatActivity {

    /**
     * 和其他覆盖物相同三维模型图也可控制图层的Level和zIndex
     * 可通过 GLModelOverlayProvider的displayLevel和zIndex进行设置
     * displayLevel - 默认层级为POI之下 OverlayLevel.OverlayLevelAboveBuildings
     * 相同Level内的显示层级关系通过zIndex(int)来控制，zIndex越大越靠上显示。 Level优先级高于zIndex displayLevel必须为如下值之一，否则不生效
     */

    private MapView mapView;
    protected TencentMap tencentMap;
    private String mResourcePath;
    private GLModelOverlay vectorOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_l_model);
        mapView = findViewById(R.id.mapView);
        tencentMap = mapView.getMap();
        init();

    }

    private void init() {
        //gltf格式三维模型文件在assets内
        String file = "BrainStem.gltf";
        String SKELETON_PATH = "gltf/gltf-BrainStem";
        mResourcePath = getExternalFilesDir(null).getAbsolutePath() + File.separator + SKELETON_PATH;
        tencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.987336, 116.370171), 16, 38.254456f, 318.35846f)));
        GLModelOverlayProvider provider = new GLModelOverlayProvider(
                mResourcePath + File.separator + file,
                new LatLng(39.987336, 116.370171)).coordType(PixelType)
                .pixelBounds(800, 2000).rotationX(90);

        provider.setVectorOverlayLoadedListener(
                new VectorOverlay.OnVectorOverlayLoadListener() {
                    @Override
                    public void onVectorOverlayLoaded(boolean success) {
                        vectorOverlay.playSkeletonAnimation(0, 1, true);
                        //vectorOverlay.stopSkeletonAnimation();

                    }
                });
        vectorOverlay = tencentMap.addVectorOverlay(provider);
    }


    /**
     * mapview的生命周期管理
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }
}
