package com.parse.weaver.classes;

import com.parse.ParseUser;

import java.io.Serializable;

/**
 * Created by madog on 23/03/16.
 */
public class freeTimeItem implements Serializable
{

    private int from;
    private int from_minutes;
    private int to;
    private int to_minutes;
    private String days;
    private String where;

    public freeTimeItem()
    {
        super();
    }

    public freeTimeItem(int from, int from_minutes, int to, int to_minutes, String days, String where)
    {
        this.from = from;
        this.from_minutes = from_minutes;
        this.to = to;
        this.to_minutes = to_minutes;
        this.days = days;
        this.where = where;
    }

    public int getFrom() {
        return from;
    }

    public int getFrom_minutes() {
        return from_minutes;
    }

    public int getTo() {
        return to;
    }

    public int getTo_minutes() {
        return to_minutes;
    }

    public String getDays() {
        return days;
    }

    public String getWhere() {
        return where;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setFrom_minutes(int from_minutes) {
        this.from_minutes = from_minutes;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void setTo_minutes(int to_minutes) {
        this.to_minutes = to_minutes;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        freeTimeItem other = (freeTimeItem) obj;

        if (from != other.from)
            return false;

        return true;
    }

    @Override
    public String toString()
    {
        return "FreetimeItem: "+" - From:"+from+" - To:"+to+" - Days:"+days+" - Where:"+where;
    }
}
