package com.cde.twitterapp;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.UiThread;

import java.util.List;

/**
 * Created by dello on 12/01/15.
 */
@EActivity(R.layout.activity_map)
public class MapActivity extends Activity implements OnMapReadyCallback{
    @FragmentById(R.id.map)
    MapFragment mapFragment;

    private boolean extras = false;

    GoogleMap map = null;

    Double minLat = 90.0;
    Double maxLat = -90.0;
    Double minLng = 180.0;
    Double maxLng = -180.0;

    @Extra
    List<MarkerOptions> markers;

    @AfterViews
    @UiThread
    void setMapCallback(){
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(extras) showMarkers();
    }

    @AfterExtras
    public void showMarkers(){
        extras = true;
        if(map != null) {
            for (MarkerOptions marker : markers) {
                map.addMarker(marker);
            }
            setBorders();
            drawPath();
        }
    }

    private void setBorders(){
        if(map != null && markers != null) {
            for (MarkerOptions marker : markers) {
                minLat=marker.getPosition().latitude<minLat?marker.getPosition().latitude:minLat;
                maxLat=marker.getPosition().latitude>maxLat?marker.getPosition().latitude:maxLat;
                minLng=marker.getPosition().longitude<minLng?marker.getPosition().longitude:minLng;
                maxLng=marker.getPosition().longitude>maxLng?marker.getPosition().longitude:maxLng;
            }
            double latBorder = 0.25*(maxLat - minLat);
            double lngBorder = 0.25*(maxLng - minLng);
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(minLat - latBorder, minLng - lngBorder), new LatLng(maxLat + latBorder, maxLng + lngBorder)), 1));
        }
    }

    private void drawPath(){
        PolylineOptions polylineOptions = new PolylineOptions();
        for(MarkerOptions marker : markers){
            polylineOptions.add(marker.getPosition());
        }
        map.addPolyline(polylineOptions);
    }
}
