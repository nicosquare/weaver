package com.parse.weaver.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreferenceFreeTime
{

    public static final String PREFS_NAME = "FREETIME_WEAVER";
    public static final String FREEGAPS = "Freetime_gaps";

    public SharedPreferenceFreeTime() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFreeTime(Context context, List<freeTimeItem> gapList)
    {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();

        String jsonFreetimeList = gson.toJson(gapList);

        editor.putString(FREEGAPS, jsonFreetimeList);

        editor.commit();
    }

    public void addFreeTime(Context context, freeTimeItem gap)
    {
        List<freeTimeItem> freeTimeList = getFreeTimeList(context);

        if (freeTimeList == null)
            freeTimeList = new ArrayList<freeTimeItem>();

        freeTimeList.add(gap);

        saveFreeTime(context, freeTimeList);
    }

    public void removeFreeTime(Context context, freeTimeItem gap)
    {
        ArrayList<freeTimeItem> freeTimeList = getFreeTimeList(context);

        if (freeTimeList != null)
        {
            freeTimeList.remove(gap);
            saveFreeTime(context, freeTimeList);
        }
    }

    public ArrayList<freeTimeItem> getFreeTimeList(Context context)
    {
        SharedPreferences settings;
        List<freeTimeItem> freeTimeList;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(FREEGAPS))
        {
            String jsonFreeTime = settings.getString(FREEGAPS, null);

            Gson gson = new Gson();

            freeTimeItem[] freeTimeItems = gson.fromJson(jsonFreeTime, freeTimeItem[].class);

            freeTimeList = Arrays.asList(freeTimeItems);
            freeTimeList = new ArrayList<freeTimeItem>(freeTimeList);
        }
        else
            return null;

        return (ArrayList<freeTimeItem>) freeTimeList;
    }
}