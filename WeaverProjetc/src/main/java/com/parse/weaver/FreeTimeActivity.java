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
import android.widget.BaseAdapter;
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
    private ArrayList<Integer> from;
    private ArrayList<Integer> to;
    private ArrayList<String>  days;
    private BaseAdapter freeSpotsAdapter;


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

        from = new ArrayList<Integer>();
        from.clear();
        to = new ArrayList<Integer>();
        to.clear();
        days = new ArrayList<String>();
        days.clear();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putFreeTime();

            }
        });

        ParseQuery query = new ParseQuery("FreeTime");

        query.whereEqualTo("username", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> objects, ParseException e) {

                int index = 0;

                if (e == null) {
                    for (ParseObject freeTimeObject : objects) {
                        from.add(index, freeTimeObject.getNumber("from").intValue());
                        to.add(index, freeTimeObject.getNumber("to").intValue());
                        days.add(index, freeTimeObject.getString("days"));
                    }
                } else {
                    Log.d("E. FreeTime", "Error: " + e.getMessage());
                }
            }
        });

        freeSpotsAdapter = new freeTimeAdapter(this, from, to, days);
        freeTimeList.setAdapter(freeSpotsAdapter);

        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                freeSpotsAdapter.notifyDataSetChanged();
            }
        });

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
