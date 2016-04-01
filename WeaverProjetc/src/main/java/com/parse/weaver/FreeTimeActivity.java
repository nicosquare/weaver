package com.parse.weaver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.weaver.classes.SharedPreferenceFreeTime;
import com.parse.weaver.classes.freeTimeAdapter;
import com.parse.weaver.classes.freeTimeItem;

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
    private EditText whereText;
    private CheckBox cbSunday;
    private Button buttonAdd;
    private ListView freeTimeList;
    private freeTimeAdapter freeSpotsAdapter;

    private SharedPreferenceFreeTime freeTimePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Preferences code
        freeTimePreferences = new SharedPreferenceFreeTime();

        // Graphic interaction code
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
        whereText = (EditText) findViewById(R.id.whereText);
        freeTimeList = (ListView) findViewById(R.id.listView_items);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        ParseQuery query = new ParseQuery("FreeTime");

        ArrayList<freeTimeItem> freeTimeStored =  freeTimePreferences.getFreeTimeList(getApplicationContext());

        if(freeTimeStored == null)
        {

            final ArrayList<freeTimeItem> freeTimeNotStored = new ArrayList<freeTimeItem>();

            query.whereEqualTo("username", ParseUser.getCurrentUser());
            query.findInBackground(new FindCallback<ParseObject>() {

                public void done(List<ParseObject> objects, ParseException e) {

                    int index = 0;

                    if (objects != null && objects.size() != 0) {
                        for (ParseObject freeTimeObject : objects) {

                            ParseUser user = ParseUser.getCurrentUser();
                            int from = freeTimeObject.getNumber("from") == null ? 0 : freeTimeObject.getNumber("from").intValue();
                            int from_min = freeTimeObject.getNumber("from_min") == null ? 0 : freeTimeObject.getNumber("from_min").intValue();
                            int to = freeTimeObject.getNumber("to") == null ? 0 : freeTimeObject.getNumber("to").intValue();
                            int to_min = freeTimeObject.getNumber("to_min") == null ? 0 : freeTimeObject.getNumber("to_min").intValue();
                            String days = freeTimeObject.getString("days") == null ? "" : freeTimeObject.getString("days");
                            String where = freeTimeObject.getString("where") == null ? "" : freeTimeObject.getString("where");

                            freeTimeItem listItem = new freeTimeItem(from, from_min, to, to_min, days, where);
                            freeTimeNotStored.add(index,listItem);
                        }

                        freeTimePreferences.saveFreeTime(getApplicationContext(),freeTimeNotStored);

                    } else {
                        Log.d("E. FreeTime", "No hay registros");
                    }
                }
            });

            freeSpotsAdapter = new freeTimeAdapter(this, freeTimeNotStored);
        }
        else
        {
            freeSpotsAdapter = new freeTimeAdapter(this, freeTimeStored);
        }

        if(freeSpotsAdapter == null) Log.d("Error","null adapter");
        else Log.d("Error", "null adapter todo guembis");

        freeTimeList.setAdapter(freeSpotsAdapter);
        freeTimeList.deferNotifyDataSetChanged();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                freeSpotsAdapter.notifyDataSetChanged();
            }
        });

        freeTimeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FreeTimeActivity.this);

                alertDialogBuilder
                        .setTitle("¿Quieres borrar este registro?")
                        .setCancelable(false)
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do something with parameter.
                                removeFreetimeGap(position);
                            }
                        })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                putFreeTime();

            }
        });

    }

    private void putFreeTime()
    {
        final int fromTime = fromSpinner.getSelectedItemPosition();
        final int toTime = toSpinner.getSelectedItemPosition();
        final String where = whereText.getText().toString().trim();
        String days = "";

        if(cbMonday.isChecked()) days += "L,";
        if(cbTuesday.isChecked()) days += "M,";
        if(cbWednesday.isChecked()) days += "MC,";
        if(cbThursday.isChecked()) days += "J,";
        if(cbFriday.isChecked()) days += "V,";
        if(cbSaturday.isChecked()) days += "S,";
        if(cbSunday.isChecked()) days += "D,";

        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));

        if (fromTime == 0)
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_from_spinner));
        }

        if (toTime == 0)
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_to_spinner));
        }

        if (toTime <= fromTime)
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_concordance));
        }

        if (days.length() == 0)
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_days));
        }

        if (where.equals(""))
        {
            if (validationError)
            {
                validationErrorMessage.append(getString(R.string.error_join));
            }
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_where));
        }

        validationErrorMessage.append(getString(R.string.error_end));

        if (validationError) {
            Toast.makeText(FreeTimeActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        days = days.substring(0,days.length()-1);
        final String daysAux = days;

        ParseObject freeTimeObject = new ParseObject("FreeTime");

         /*ALERTA: Modificar cuando se ponga DatePicker*/
        freeTimeObject.put("username", ParseUser.getCurrentUser());
        freeTimeObject.put("from",fromTime);
        freeTimeObject.put("to",toTime);
        freeTimeObject.put("days",days);
        freeTimeObject.put("where",where);

        freeTimeObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e == null)
                {
                    ArrayList<freeTimeItem> freeTimeGap =  freeTimePreferences.getFreeTimeList(getApplicationContext());

                    freeTimeGap.add(new freeTimeItem(fromTime, 0, toTime, 0, daysAux, where));
                    freeTimePreferences.saveFreeTime(getApplicationContext(), freeTimeGap);

                    freeSpotsAdapter = new freeTimeAdapter(FreeTimeActivity.this, freeTimeGap);
                    freeTimeList.setAdapter(freeSpotsAdapter);

                    Toast.makeText(FreeTimeActivity.this, "Se guardo franja de disponibilidad", Toast.LENGTH_LONG).show();
                    buttonAdd.setText("Agregar");

                    finish();
                    startActivity(getIntent());
                }
                else
                {
                    Toast.makeText(FreeTimeActivity.this, "No se guardo franja de disponibilidad", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void removeFreetimeGap(final int position)
    {

        Log.d("Delete. FreeTime", "Entro");

        final ArrayList<freeTimeItem> freeTimeGap = freeTimePreferences.getFreeTimeList(getApplicationContext());

        final freeTimeItem selectedItem = freeTimeGap.get(position);

        ParseQuery query = new ParseQuery("FreeTime");

        query.whereEqualTo("username", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            public void done(List<ParseObject> objects, ParseException e) {

                int index = 0;

                Log.d("Delete. FreeTime", "Done");

                if(objects == null) Log.d("Delete. FreeTime", "Objects null");

                if (e == null) {

                    Log.d("Delete. FreeTime", "E null");

                    Log.d("Delete. FreeTime", "From:" + String.valueOf(selectedItem.getFrom()));
                    Log.d("Delete. FreeTime", "To:" + String.valueOf(selectedItem.getTo()));
                    Log.d("Delete. FreeTime", "Where:" + String.valueOf(selectedItem.getWhere()));

                    for (ParseObject freeTimeObject : objects) {

                        if (freeTimeObject.getInt("from") == selectedItem.getFrom()
                                && freeTimeObject.getInt("to") == selectedItem.getTo()
                                && freeTimeObject.getString("days").equals(selectedItem.getDays()))
                        {

                            freeTimeObject.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {

                                        // Delete selectedItem from Dataset
                                        freeTimeGap.remove(selectedItem);

                                        // Update SharedPreferences
                                        freeTimePreferences.saveFreeTime(getApplicationContext(), freeTimeGap);
                                        freeSpotsAdapter = new freeTimeAdapter(FreeTimeActivity.this, freeTimeGap);
                                        freeTimeList.setAdapter(freeSpotsAdapter);

                                        Toast.makeText(FreeTimeActivity.this, "Se borro franja de disponibilidad", Toast.LENGTH_LONG).show();

                                        buttonAdd.setText("Agregar");

                                        finish();
                                        startActivity(getIntent());

                                        Log.d("Delete. FreeTime", "Borro");
                                    } else {
                                        Log.d("Delete. FreeTime", " No Borro");
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }

                    }
                } else {
                    Log.d("E. FreeTime", "Error: " + e.getMessage());
                }
            }
        });

    }

}
