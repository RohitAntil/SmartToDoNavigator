package com.example.rohit02kumar.smarttodonavigator.SearchPackage;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.view.View;
import android.widget.Toast;

import com.example.rohit02kumar.smarttodonavigator.MapsActivity;
import com.example.rohit02kumar.smarttodonavigator.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class MainActivity extends AppCompatActivity {
FloatingActionButton fab;
    SearchView search;
    private static final String[] suggestions={"Bellandur","HSR","Silkboard","Whitefield"};
 static final int PLACE_PICKER_REQUEST=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search =(SearchView)findViewById(R.id.search);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent =new Intent(getApplicationContext(), MapsActivity.class);
              startActivity(intent);
         //       https://medium.com/exploring-android/exploring-play-services-place-picker-autocomplete-150809f739fe#.2tsgeu68h
//                Context context=getApplicationContext();
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                Intent intent = builder.build();
//                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });
          search.setOnSuggestionListener(new OnSuggestionListener(){

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return false;
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                populateAdapter(newText);
                return false;
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, this);
                // Do something with the place
            }
        }
    }
    void populateAdapter(String place)
    {

    }


}
