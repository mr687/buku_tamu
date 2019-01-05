package com.daphino.bukutamu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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
    public String getTodays(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String date_now = getDay(day) + " " + date + " " + getMonth(month) + " " + year;
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
    public Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
