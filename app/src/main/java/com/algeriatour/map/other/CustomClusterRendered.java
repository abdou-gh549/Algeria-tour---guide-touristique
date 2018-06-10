package com.algeriatour.map.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.algeriatour.map.activity.MapPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRendered extends DefaultClusterRenderer<MapClusterMarkerItem> {
    private Context mContext;
    private MapPresenter mapPresenter;
    public CustomClusterRendered(Context context, GoogleMap map,
                                 ClusterManager<MapClusterMarkerItem> clusterManager,
                                 MapPresenter mapPresenter) {
        super(context, map, clusterManager);
        mContext = context;
        this.mapPresenter = mapPresenter;
    }

    @Override
    protected void onBeforeClusterItemRendered(MapClusterMarkerItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        Bitmap image = MapUtils.getIcon(mContext, item.getType());

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(image));
    }

    @Override
    protected void onClusterItemRendered(MapClusterMarkerItem clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);

        if(mapPresenter != null){
            Log.d("tixx", "onClusterRendered: befor if ");
            if( mapPresenter.isSelectedMarker(marker)){
                Log.d("tixx", "onClusterRendered: in if ");
                marker.setIcon( BitmapDescriptorFactory.fromBitmap(MapUtils.getIcon(mContext, "selected")));
            }
        }
    }


}
