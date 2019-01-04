package com.daphino.bukutamu;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    public String getToday(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String time = dateFormat.format(calendar.getTime());
        String date_now = getDay(day) + " " + date + " " + getMonth(month) + " " + year + " " + time;
        return date_now;
    }
    public String getDay(int i){
        String[] arr_day = {
                "",
                "Minggu",
                "Senin",
                "Selasa",
                "Rabu",
                "Kamis",
                "Jum'at",
                "Sabtu"
        };
        return arr_day[i];
    }
    public String getMonth(int i){
        String[] arr_month = {
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "Mei",
                "Jun",
                "Jul",
                "Agu",
                "Sep",
                "Okt",
                "Nov",
                "Des"
        };
        return arr_month[i];
    }
}
