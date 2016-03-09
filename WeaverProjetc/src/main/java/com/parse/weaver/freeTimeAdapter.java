package com.parse.weaver;

/**
 * Created by madog on 9/03/16.
 */

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.util.ArrayList;

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
            View freeItemList = inflater.inflate(R.layout.freetime_item, null);
            Holder holder = new Holder();

            holder.hoursInfo = (TextView) freeItemList.findViewById(R.id.hoursInfo);
            holder.daysInfo = (TextView) freeItemList.findViewById(R.id.daysInfo);
            holder.hiddenFrom = (TextView) freeItemList.findViewById(R.id.hiddenFrom);
            holder.hiddenTo = (TextView) freeItemList.findViewById(R.id.hiddenTo);
            holder.hiddenDays = (TextView) freeItemList.findViewById(R.id.hiddenDays);

            //holder.hoursInfo.setText("Desde: "+get12HourNotation(from.get(position))+"  hasta:  "+get12HourNotation(to.get(position)));
            holder.hoursInfo.setText("Desde: Test");
            holder.daysInfo.setText("Días:  "+days.get(position));
            holder.hiddenFrom.setText(""+from.get(position));
            holder.hiddenTo.setText(""+to.get(position));
            holder.hiddenDays.setText(""+days.get(position));

            freeItemList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "From " +from.get(position)+" to "+to.get(position)+" days "+days.get(position), Toast.LENGTH_LONG).show();
                }
            });

            return freeItemList;
        }

        public class Holder
        {
            TextView hoursInfo;
            TextView daysInfo;
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

