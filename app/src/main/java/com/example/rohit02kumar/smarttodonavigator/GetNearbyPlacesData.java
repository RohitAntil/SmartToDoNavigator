package com.example.rohit02kumar.smarttodonavigator;

/**
 * Created by rohit02.kumar on 3/9/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    String type;
    LatLng start;
    Context context;

   OnTaskCompleted task;
    ArrayList<Marker> mMarkerPoints ;
    GetNearbyPlacesData(Context context , OnTaskCompleted task, String type, LatLng start)
    {   this.context=context;
        this.task=task;
        mMarkerPoints=new ArrayList<Marker>();
        this.type=type;
        this.start=start;
    }
    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        NearbyDataParser dataParser = new NearbyDataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        task.onTaskCompleted(mMarkerPoints);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        Log.d("onPostExecute","Entered into showing locations");
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName);

            Location location1 = new Location("");
            location1.setLatitude(start.latitude);
            location1.setLongitude(start.longitude);

            Location location2 = new Location("");
            location2.setLatitude(lat);
            location2.setLongitude(lng);

            float distanceInMeters = location1.distanceTo(location2);
            markerOptions.snippet("Dist : "+ (int)distanceInMeters+"m");

            Marker marker=mMap.addMarker(markerOptions);

            mMarkerPoints.add(marker);
            marker.showInfoWindow();

//            move map camera
           mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

    }
}