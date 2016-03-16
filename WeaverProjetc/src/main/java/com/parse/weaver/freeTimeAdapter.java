package com.parse.weaver;

/**
 * Created by madog on 9/03/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class freeTimeAdapter extends BaseAdapter
{
    private ArrayList<Integer> from;
    private ArrayList<Integer> to;
    private ArrayList<String> days;
    private static LayoutInflater inflater = null;
    private static Context context;

    public freeTimeAdapter(Context context, ArrayList<Integer> from, ArrayList<Integer> to, ArrayList<String> days)
    {
        this.context = context;
        this.from = from;
        this.to = to;
        this.days = days;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return from.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final View freeItemList = inflater.inflate(R.layout.freetime_item, null);
        Holder holder = new Holder();

        holder.deleteIcon = (ImageView) freeItemList.findViewById(R.id.deleteIcon);
        holder.fromValue = (TextView) freeItemList.findViewById(R.id.fromValue);
        holder.toValue = (TextView) freeItemList.findViewById(R.id.toValue);
        holder.daysValue = (TextView) freeItemList.findViewById(R.id.daysValue);
        holder.hiddenFrom = (TextView) freeItemList.findViewById(R.id.hiddenFrom);
        holder.hiddenTo = (TextView) freeItemList.findViewById(R.id.hiddenTo);
        holder.hiddenDays = (TextView) freeItemList.findViewById(R.id.hiddenDays);

        holder.fromValue.setText(get12HourNotation(from.get(position)));
        holder.toValue.setText(get12HourNotation(to.get(position)));
        holder.daysValue.setText(days.get(position));
        holder.hiddenFrom.setText(""+from.get(position));
        holder.hiddenTo.setText(""+to.get(position));
        holder.hiddenDays.setText("" + days.get(position));

        final int fromToDelete = from.get(position);
        final int toToDelete = to.get(position);
        final String daysToDelete = days.get(position);

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
        TextView fromValue;
        TextView toValue;
        TextView daysValue;
        TextView hiddenFrom;
        TextView hiddenTo;
        TextView hiddenDays;
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

