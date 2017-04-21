package com.example.rohit02kumar.smarttodonavigator;

/**
 * Created by rohit02.kumar on 3/9/2017.
 */
import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

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
   OnTaskCompleted task;
    ArrayList<Marker> mMarkerPoints ;
    GetNearbyPlacesData(OnTaskCompleted task,String type,LatLng start)
    {
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
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
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
            markerOptions.snippet("Distance : "+ distanceInMeters+"m");

            if(type.equalsIgnoreCase("school"))
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school));
            else if(type.equalsIgnoreCase("hospital"))
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_hospital));
            else
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant));

            Marker marker=mMap.addMarker(markerOptions);
            marker.showInfoWindow();
            mMarkerPoints.add(marker);

//            move map camera
           mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

    }
}