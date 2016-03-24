package com.parse.weaver.classes;

/**
 * Created by madog on 9/03/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.weaver.R;

import java.util.ArrayList;
import java.util.List;

public class freeTimeAdapter extends ArrayAdapter<freeTimeItem>
{
    private static LayoutInflater inflater = null;
    private static Context context;

    public freeTimeAdapter(Context context, ArrayList<freeTimeItem> freeTimeStored)
    {
        super(context,0, freeTimeStored);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final View freeItemList = inflater.inflate(R.layout.freetime_item, null);
        Holder holder = new Holder();

        freeTimeItem freeTimeStored = getItem(position);

        /*ALERTA: Modificar cuando se ponga DatePicker*/

        holder.deleteIcon = (ImageView) freeItemList.findViewById(R.id.deleteIcon);
        holder.fromValue = (TextView) freeItemList.findViewById(R.id.fromValue);
        holder.toValue = (TextView) freeItemList.findViewById(R.id.toValue);
        holder.daysValue = (TextView) freeItemList.findViewById(R.id.daysValue);

        if(freeTimeStored == null) Log.d("freeTimeStored","Paila");
        else Log.d("freeTimeStored","No Paila");

        holder.fromValue.setText(get12HourNotation(freeTimeStored.getFrom()));
        holder.toValue.setText(get12HourNotation(freeTimeStored.getTo()));
        holder.daysValue.setText(freeTimeStored.getDays());

        final int fromToDelete = freeTimeStored.getFrom();
        final int toToDelete = freeTimeStored.getTo();
        final String daysToDelete = freeTimeStored.getDays();

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery query = new ParseQuery("FreeTime");

                query.whereEqualTo("username", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {

                    public void done(List<ParseObject> objects, ParseException e) {

                        int index = 0;

                        if (e == null) {

                            for (ParseObject freeTimeObject : objects) {

                                Log.d("Delete. FreeTime", "From:"+freeTimeObject.getInt("from"));
                                Log.d("Delete. FreeTime", "To:"+freeTimeObject.getInt("to"));
                                Log.d("Delete. FreeTime", "Days:"+freeTimeObject.getString("days"));

                                if(freeTimeObject.getInt("from") == fromToDelete
                                        && freeTimeObject.getInt("to") == toToDelete
                                        && freeTimeObject.getString("days").equals(daysToDelete))
                                {

                                    Log.d("Delete. FreeTime", "Borro");
                                    try {
                                        freeTimeObject.delete();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }

                            }
                        } else {
                            Log.d("E. FreeTime", "Error: " + e.getMessage());
                        }
                    }
                });

            }
        });



        return freeItemList;
    }

    public class Holder
    {
        ImageView deleteIcon;
        TextView fromMinValue;
        TextView fromValue;
        TextView toMinValue;
        TextView toValue;
        TextView daysValue;
        TextView whereValue;
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


}

