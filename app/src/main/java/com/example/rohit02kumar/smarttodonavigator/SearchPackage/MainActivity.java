package com.example.rohit02kumar.smarttodonavigator.SearchPackage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnSuggestionListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit02kumar.smarttodonavigator.MapsActivity;
import com.example.rohit02kumar.smarttodonavigator.R;
import com.example.rohit02kumar.smarttodonavigator.database.Event;
import com.example.rohit02kumar.smarttodonavigator.database.EventDataSource;
import com.example.rohit02kumar.smarttodonavigator.database.EventsDatabaseHelper;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
     FloatingActionButton fab;
    SearchView search;
    Spinner spinner;
    EditText eventName;
    TextView fromDate;
    TextView toDate;
    Button addEvent;
    DatePickerDialog dialog;
    DatePickerDialog dialog1;
    List<Event> eventsList;
    final Calendar myCalendar = Calendar.getInstance();
    private static final String[] suggestions={"Bellandur","HSR","Silkboard","Whitefield"};
    private static String[] landmarks={"School","Medical Stores and Hospitals", "Restraunts"};
 static final int PLACE_PICKER_REQUEST=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        search =(SearchView)findViewById(R.id.search);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        spinner = (Spinner) findViewById(R.id.event_type);
        eventName = (EditText) findViewById(R.id.event_text);
        fromDate = (TextView) findViewById(R.id.from_date_text);
        toDate = (TextView) findViewById(R.id.to_date_text);
        addEvent = (Button) findViewById(R.id.add_event);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, landmarks); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        updateLabel();

        final DatePickerDialog.OnDateSetListener date_picker_from = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel_from();
            }

        };
        final DatePickerDialog.OnDateSetListener date_picker_to = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel_to();
                view.setMinDate(System.currentTimeMillis()-10000);
            }

        };
        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
             dialog=new DatePickerDialog(MainActivity.this, date_picker_from, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dialog.show();
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog1= new DatePickerDialog(MainActivity.this, date_picker_to, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog1.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                dialog1.show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.list);
                ListView lv = (ListView) dialog.findViewById(R.id.lv);
                dialog.setCancelable(true);
                dialog.setTitle("ListView");
                dialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        eventsList= new EventDataSource(MainActivity.this).getAllEvents();
                       Event event=eventsList.get(position);

                        Snackbar.make(view, event.getmEventName()+"\n"+event.getmEvenType()+" Date : "+event.getmFromDate(), Snackbar.LENGTH_LONG)
                                .setAction("No action", null).show();
                    }
                });
//                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                startActivity(intent);
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             String name = eventName.getText().toString();
                if(name==null||name.length()<=2)
                {
                    Toast.makeText(MainActivity.this,"Enter valid event Name",Toast.LENGTH_SHORT).show();
                    return ;
                }
                 String type=spinner.getSelectedItem().toString();
                String from=fromDate.getText().toString();
                String to=toDate.getText().toString();
                try {
                    Date fromDate=new SimpleDateFormat("dd/MM/yyyy").parse(from);
                    Date toDate=new SimpleDateFormat("dd/MM/yyyy").parse(to);
                    Date date =new Date();
                    if(fromDate.compareTo(toDate)>=0)
                    {
                        Toast.makeText(MainActivity.this,"End Date must be greater than Start Date .",Toast.LENGTH_SHORT).show();
                        return ;
                    }

                    EventDataSource dataSource=new EventDataSource(MainActivity.this);
                    dataSource.createEvent(name,type,from,to,0);
                    eventName.setText("");
                    spinner.setSelection(0);
                     dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                    dialog1.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        fromDate.setText(sdf.format(myCalendar.getTime()));
        toDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel_from() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        fromDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel_to() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        toDate.setText(sdf.format(myCalendar.getTime()));
    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place selectedPlace = PlacePicker.getPlace(data, this);
//                // Do something with the place
//            }
//        }
//    }
//            search.setOnSuggestionListener(new OnSuggestionListener(){
//
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                return false;
//            }
//
//            @Override
//            public boolean onSuggestionClick(int position) {
//                return false;
//            }
//        });
//
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                populateAdapter(newText);
//                return false;
//            }
//        });

}
