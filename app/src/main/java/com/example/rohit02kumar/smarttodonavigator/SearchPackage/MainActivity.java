package com.example.rohit02kumar.smarttodonavigator.SearchPackage;

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

public class MainActivity extends AppCompatActivity {
FloatingActionButton fab;
    SearchView search;
    private static final String[] suggestions={"Bellandur","HSR","Silkboard","Whitefield"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search =(SearchView)findViewById(R.id.search);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              Intent intent =new Intent(getApplicationContext(), MapsActivity.class);
//                startActivity(intent);
         //       https://medium.com/exploring-android/exploring-play-services-place-picker-autocomplete-150809f739fe#.2tsgeu68h

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
    void populateAdapter(String place)
    {

    }
}
