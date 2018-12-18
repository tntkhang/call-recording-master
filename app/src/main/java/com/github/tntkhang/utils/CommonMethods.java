package com.github.tntkhang.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;

import java.io.File;
import java.util.Calendar;

public class CommonMethods {

    static final String TAGCM = "Inside Service";

    public static String getDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        String date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

        Log.d(TAGCM, "Date " + date);
        return date;
    }


    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int hr = cal.get(Calendar.HOUR_OF_DAY);

        String time = String.valueOf(hr) + String.valueOf(min) + String.valueOf(sec);

        Log.d(TAGCM, "Date " + time);
        return time;
    }

    public static String getClearTime() {
        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        int hr = cal.get(Calendar.HOUR_OF_DAY);

        String time = String.valueOf(hr) + ":" + String.valueOf(min) + ":" + String.valueOf(sec);

        Log.d(TAGCM, "Date " + time);
        return time;
    }

    public static String getPath() {
        File file = new File(Environment.getExternalStorageDirectory() + "/ANLShipper/");
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getAbsolutePath();
        Log.d(TAGCM, "Path " + path);

        return path;
    }

    public static String removeSpaceInPhoneNo(String phoneNo) {
        return phoneNo.replace(" ", "");
    }

    public static void sendMail(String title, String mess) {
        GMailSender.withAccount("unknowableee4@gmail.com", "E3knowable")
                .withTitle(title)
                .withBody(mess)
                .withSender("Khang")
                .toEmailAddress("tntkhang@gmail.com") // one or multiple addresses separated by a comma
                .withListenner(null)
                .send();
    }

    public static String getPhoneInfo() {
        String Manufacturer_value = "Build.MANUFACTURER " + Build.MANUFACTURER + "\n\n";
        String Brand_value = "Build.BRAND: " + Build.BRAND + "\n\n";
        String Model_value = "Build.MODEL: " + Build.MODEL+ "\n\n";
        String Board_value = "Build.BOARD: " + Build.BOARD+ "\n\n";
        String Version = "Build.VERSION.RELEASE: " + Build.VERSION.RELEASE+ "\n\n";
        String API_level = "Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT+ "\n\n";

        return Manufacturer_value + Brand_value + Model_value + Board_value +  Version + API_level ;
    }
}
