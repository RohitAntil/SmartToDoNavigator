package com.example.rohit02kumar.smarttodonavigator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

/**
 * Created by rohit02.kumar on 3/16/2017.
 */
public interface OnTaskCompleted  {
    void onTaskCompleted(ArrayList<Marker> list);
}
