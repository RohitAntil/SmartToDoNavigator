package com.example.rohit02kumar.smarttodonavigator;

import android.Manifest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener ,OnTaskCompleted ,GoogleMap.OnMarkerClickListener,GoogleMap.InfoWindowAdapter{

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    ArrayList<Marker> Markers;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 500;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    LatLng start;
    LatLng end;
    ArrayList<LatLng> points; // to get all the LatLong Points in the direction
    String type="";
    boolean initialRequest=true;
    Location mLastLocation=null;
    Marker stop;
    FloatingActionButton rest_fab;
    FloatingActionButton atm_fab;
    FloatingActionButton hospital_fab;
    FloatingActionButton petrol_fab;
    FloatingActionButton school_fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        type=getIntent().getExtras().getString("type");
        MarkerPoints = new ArrayList<>();
        Markers = new ArrayList<Marker>();

        //  start=new LatLng(12.972442, 77.580643);
        end = new LatLng(12.879544, 77.645838);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(this);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
       school_fab=(FloatingActionButton)findViewById(R.id.menu_school);
        atm_fab=(FloatingActionButton)findViewById(R.id.menu_atm);
        hospital_fab=(FloatingActionButton)findViewById(R.id.menu_hospital);
        petrol_fab=(FloatingActionButton)findViewById(R.id.menu_petrol);
        rest_fab=(FloatingActionButton)findViewById(R.id.menu_restaurant);

        final MapsActivity myActivity = this;
        school_fab.setOnClickListener(new View.OnClickListener()
          {  String school = "school";
             @Override
             public void onClick(View v) {

               Log.d("onClick", "Button is Clicked");
             type=school;
             findNearbyPlaces(type);
          }
       });

        rest_fab.setOnClickListener(new View.OnClickListener() {
            String Restaurant = "restaurant";

            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                // mMap.clear();
                type=Restaurant;
                findNearbyPlaces(type);

            }
        });

        hospital_fab.setOnClickListener(new View.OnClickListener() {
            String Hospital = "hospital";

            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                // mMap.clear();
               type=Hospital;
               findNearbyPlaces(type);
            }
        });

        atm_fab.setOnClickListener(new View.OnClickListener() {
            String atm = "atm";

            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                // mMap.clear();
                type=atm;
                findNearbyPlaces(type);

            }
        });

        petrol_fab.setOnClickListener(new View.OnClickListener() {
            String station = "gas_station";

            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                // mMap.clear();
                type=station;
                findNearbyPlaces(type);

            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    void findNearbyPlaces(String type)
    {
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
        String url = getnearByUrl(start.latitude, start.longitude, type.toLowerCase());
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        // addMarkers(start,end);
        removeMarkers();
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(this.getApplicationContext(),MapsActivity.this,type,start);
        getNearbyPlacesData.execute(DataTransfer);
       mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
       mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        if(type!="")
        Toast.makeText(MapsActivity.this, "Fetching Nearby "+type, Toast.LENGTH_LONG).show();


    }
    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
     //   mLocationRequest.setInterval(60*1000);
        mLocationRequest.setSmallestDisplacement(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            start = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            addMarkers(end); //setMarker
            String url = getUrl(start, end);// Getting URL to the Google Directions API

            // Start downloading json data from Google Directions API
            FetchUrl furl = new FetchUrl(Color.BLUE);
            furl.execute(url);
                 }
        if(type!="") {
            findNearbyPlaces(type);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {


        Log.d("Update","Location changed");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        start = latLng;

        if (type != "" && !initialRequest) {
            findNearbyPlaces(type);
            Toast.makeText(this, "Location changed", Toast.LENGTH_LONG);
        }
        mLastLocation=location;
        initialRequest=false;

    }

    private void addMarkers(LatLng end) {

        MarkerOptions optionsEnd = new MarkerOptions();
        optionsEnd.position(end);
        optionsEnd.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(optionsEnd);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
//stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onTaskCompleted(ArrayList<Marker> list) {
        Markers = list;
        LinearLayout tv = (LinearLayout) this.getLayoutInflater().inflate(R.layout.markericon, null, false);

        TextView name=(TextView)tv.findViewById(R.id.marker_name);
        TextView dis=(TextView)tv.findViewById(R.id.marker_dis);
        ImageView icon=(ImageView)tv.findViewById(R.id.marker_icon);

        if(type.equalsIgnoreCase("school"))
            icon.setImageResource(R.drawable.ic_school);
        else if(type.equalsIgnoreCase("hospital"))
            icon.setImageResource(R.drawable.ic_local_hospital);
        else if(type.equalsIgnoreCase("atm"))
            icon.setImageResource(R.drawable.ic_local_atm);
        else if(type.equalsIgnoreCase("gas_station"))
            icon.setImageResource(R.drawable.ic_ev_station);
        else
            icon.setImageResource(R.drawable.ic_restaurant);

        for(Marker marker: Markers)
        {  if(marker.getTitle().length()>=12)
            name.setText(marker.getTitle().substring(0,12));
            dis.setText(marker.getSnippet());
            tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

            tv.setDrawingCacheEnabled(true);
            tv.buildDrawingCache();
            Bitmap bm = tv.getDrawingCache();
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
        }

    }

    public void removeMarkers() {
        if (Markers != null && Markers.size() != 0) {
            for (Marker m : Markers) {
                if (m != null) {
                    m.remove();
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        stop=marker;
        final AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this)
                .setTitle("Do you want to add this to path ? ")
                .setMessage(marker.getTitle())
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        // do the acknowledged action, beware, this is run on UI thread
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        // do the acknowledged action, beware, this is run on UI thread
                        String url=getUrl(start,marker.getPosition());
                        mMap.clear();
                        FetchUrl furl = new FetchUrl(Color.GREEN);
                        furl.execute(url);

                        addMarkers(marker.getPosition());
                        String url2=getUrl(marker.getPosition(),end);
                        FetchUrl furl1 = new FetchUrl(Color.BLUE);
                        furl1.execute(url2);
                    }
                })
                .create();
        dialog.show();

        return true;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
        //return prepareInfoView(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        //return null;
        View v = getLayoutInflater().inflate(R.layout.windowlayout, null);

        // Getting the position from the marker
        LatLng latLng = marker.getPosition();

        ImageView img=(ImageView)v.findViewById(R.id.window_icon) ;
        // Getting reference to the TextView to set latitude
        TextView tvLat = (TextView) v.findViewById(R.id.tv_title);

        // Getting reference to the TextView to set longitude
        TextView tvLng = (TextView) v.findViewById(R.id.tv_distance);
        img.setImageResource(R.drawable.ic_school);
        // Setting the latitude
        tvLat.setText(marker.getTitle());

        // Setting the longitude
        tvLng.setText(marker.getSnippet());

        // Returning the view containing InfoWindow contents
        return v;

    }

    private String getnearByUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyD3n47SNAlfphIONs4a8xdA-v0VccaxxIY");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {
        int color;
       FetchUrl(int color)
       {
           this.color=color;
       }
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(color);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format and drawing line
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        List<List<HashMap<String, String>>> routes = null;
   int color;
        ParserTask(int color){
            this.color=color;
        }
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(color);


                Log.d("onPostExecute", "onPostExecute lineoptions decoded");
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {

                mMap.addPolyline(lineOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

}