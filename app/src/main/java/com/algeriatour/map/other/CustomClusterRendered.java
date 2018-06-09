package com.algeriatour.map.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.algeriatour.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class CustomClusterRendered extends DefaultClusterRenderer<MapClusterMarkerItem> {
    private Context mContext;
    public CustomClusterRendered(Context context, GoogleMap map, ClusterManager<MapClusterMarkerItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MapClusterMarkerItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
/*

        int height = 85;
        int width = 55;

        BitmapDrawable bitmapdraw = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable
                .ic_marker_port);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
*/
        Bitmap image = MapUtils.getIcon(mContext, item.getType());

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(image));
    }
}
