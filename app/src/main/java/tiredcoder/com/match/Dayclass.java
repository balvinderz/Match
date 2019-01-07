package tiredcoder.com.match;

import android.util.Log;

public class Dayclass {
    String day,month,date;
    String fulldate;
    public Dayclass()
    {}

    public Dayclass(String day, String month, String date, String fulldate) {
        this.day = day;
        this.month = month;
        this.date = date;
        this.fulldate = fulldate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFulldate() {
        return fulldate;
    }

    public void setFulldate(String fulldate) {
        this.fulldate = fulldate;
    }
    void display()
    {
        Log.i("displayingdate",date);
        Log.i("displayingday",day);
        Log.i("displayingmonth",month);
        Log.i("displayingfulldate",fulldate);
        Log.i("displayingcorrectdate",myformat());


    }
    String myformat()
    {
        Log.i("sendingdate",fulldate);
        String date="";
        date=fulldate.substring(fulldate.length()-2,fulldate.length());
        date+="/"+fulldate.substring(5,7);
        date+="/"+fulldate.substring(2,4);
        return  date;
    }

}
