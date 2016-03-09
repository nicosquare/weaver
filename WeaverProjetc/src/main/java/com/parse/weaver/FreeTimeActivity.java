package com.parse.weaver;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FreeTimeActivity extends AppCompatActivity {

    private Spinner fromSpinner;
    private Spinner toSpinner;
    private CheckBox cbMonday;
    private CheckBox cbTuesday;
    private CheckBox cbWednesday;
    private CheckBox cbThursday;
    private CheckBox cbFriday;
    private CheckBox cbSaturday;
    private CheckBox cbSunday;
    private Button buttonAdd;
    private ListView freeTimeList;
    private ArrayList<String> freeSpotsArray;
    private ArrayAdapter<String> freeSpotsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_time);

        fromSpinner = (Spinner) findViewById(R.id.spinnerFrom);
        toSpinner = (Spinner) findViewById(R.id.spinnerTo);
        cbMonday = (CheckBox) findViewById(R.id.checkBoxL);
        cbMonday = (CheckBox) findViewById(R.id.checkBoxL);
        cbTuesday = (CheckBox) findViewById(R.id.checkBoxM);
        cbWednesday = (CheckBox) findViewById(R.id.checkBoxMc);
        cbThursday = (CheckBox) findViewById(R.id.checkBoxJ);
        cbFriday = (CheckBox) findViewById(R.id.checkBoxV);
        cbSaturday = (CheckBox) findViewById(R.id.checkBoxS);
        cbSunday = (CheckBox) findViewById(R.id.checkBoxD);
        freeTimeList = (ListView) findViewById(R.id.listView_items);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        freeSpotsArray = new ArrayList<String>();
        freeSpotsArray.clear();

        ParseQuery query = new ParseQuery("FreeTime");

        query.whereEqualTo("username", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>(){

            public void done(List<ParseObject> objects, ParseException e)
            {
                int index = 0;
                String listText = "";

                if (e == null)
                {
                    for (ParseObject freeTimeObject : objects)
                    {
                        listText = "Desde: ";
                        listText += get12HourNotation(freeTimeObject.getNumber("from").intValue());
                        listText += " hasta: ";
                        listText += get12HourNotation(freeTimeObject.getNumber("to").intValue());
                        listText += " Dias: ";
                        listText += freeTimeObject.getString("days");


                        freeSpotsArray.add(index,listText);
                    }
                }
                else
                {
                    Log.d("E. FreeTime", "Error: " + e.getMessage());
                }
            }
        });

        freeSpotsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,freeSpotsArray);
        freeTimeList.setAdapter(freeSpotsAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putFreeTime();

            }
        });


    }

    private String get12HourNotation(int hour)
    {
        String transformedHour = "";

        switch (hour)
        {
            case 1: transformedHour = "1:00 AM";
                break;
            case 2: transformedHour = "2:00 AM";
                break;
            case 3: transformedHour = "3:00 AM";
                break;
            case 4: transformedHour = "4:00 AM";
                break;
            case 5: transformedHour = "5:00 AM";
                break;
            case 6: transformedHour = "6:00 AM";
                break;
            case 7: transformedHour = "7:00 AM";
                break;
            case 8: transformedHour = "8:00 AM";
                break;
            case 9: transformedHour = "9:00 AM";
                break;
            case 10: transformedHour = "10:00 AM";
                break;
            case 11: transformedHour = "11:00 AM";
                break;
            case 12: transformedHour = "12:00 M";
                break;
            case 13: transformedHour = "1:00 PM";
                break;
            case 14: transformedHour = "2:00 PM";
                break;
            case 15: transformedHour = "3:00 PM";
                break;
            case 16: transformedHour = "4:00 PM";
                break;
            case 17: transformedHour = "5:00 PM";
                break;
            case 18: transformedHour = "6:00 PM";
                break;
            case 19: transformedHour = "7:00 PM";
                break;
            case 20: transformedHour = "8:00 PM";
                break;
            case 21: transformedHour = "9:00 PM";
                break;
            case 22: transformedHour = "10:00 PM";
                break;
            case 23: transformedHour = "11:00 PM";
                break;
            case 24: transformedHour = "11:00 PM";
                break;

        }

        return transformedHour;
    }

    private void putFreeTime()
    {
        int fromTime = fromSpinner.getSelectedItemPosition();
        int toTime = toSpinner.getSelectedItemPosition();
        String days = "";

        if(cbMonday.isChecked()) days += "L,";
        if(cbTuesday.isChecked()) days += "M,";
        if(cbWednesday.isChecked()) days += "MC,";
        if(cbThursday.isChecked()) days += "J,";
        if(cbFriday.isChecked()) days += "V,";
        if(cbSaturday.isChecked()) days += "S,";
        if(cbSunday.isChecked()) days += "D,";

        days = days.substring(0,days.length()-1);

        ParseObject freeTimeObject = new ParseObject("FreeTime");

        freeTimeObject.put("username", ParseUser.getCurrentUser());
        freeTimeObject.put("from",fromTime);
        freeTimeObject.put("to",toTime);
        freeTimeObject.put("days",days);

        freeTimeObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null)
                {
                    Toast.makeText(FreeTimeActivity.this, "Se guardo franja de disponibilidad", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(FreeTimeActivity.this, FreeTimeActivity.class));
                }
                else
                {
                    Toast.makeText(FreeTimeActivity.this, "No se guardo franja de disponibilidad", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
