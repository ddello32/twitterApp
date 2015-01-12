package com.cde.twitterapp;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

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
        }
    }
}
