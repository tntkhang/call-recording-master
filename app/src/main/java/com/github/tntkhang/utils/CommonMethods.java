package com.github.tntkhang.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

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

    public static String getContactName(final String number, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        if (contactName != null && !contactName.equals(""))
            return contactName;
        else
            return "";
    }

    public static void writeToFile(String data) {
       /* File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/ANLShipper");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "nlshipperLog.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("\n\n" + data + "\n\n");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Log.e("com/github/tntkhang", "writeToFile: " + data);
    }

    public static String readFromFile() {
        String ret = "";

      /*  File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/ANLShipper");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "nlshipperLog.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            fis.close();
            ret = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e("tntkhang", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("tntkhang", "Can not read file: " + e.toString());
        }*/

        return ret;
    }

    public static String removeSpaceInPhoneNo(String phoneNo) {
        return phoneNo.replace(" ", "");
    }

    public static void getCallLog(Context context, String phoneNo) {
      /*  ContentResolver cr = context.getContentResolver();
        String strOrder = android.provider.CallLog.Calls.DEFAULT_SORT_ORDER;
        Uri callUri = Uri.parse("content://call_log/calls");
        Cursor curLog = cr.query(callUri, null, "NUMBER = '" + phoneNo + "'", null, strOrder + " LIMIT = 1");
        while (curLog.moveToNext()) {
            String callType = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.TYPE));
            if (String.valueOf(OUTGOING_TYPE).equalsIgnoreCase(callType)) {
                String callNumber = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                String callName = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                if (callName == null) {
                    callName = "Unknown";
                }
                String callDate = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.DATE));
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                String dateString = formatter.format(new Date(Long.parseLong(callDate)));

                String duration = curLog.getString(curLog.getColumnIndex(android.provider.CallLog.Calls.DURATION));

                Log.wtf("CALLLOG", callName + " - " + callNumber + " - " + duration + " - " + callType + " - " + dateString);
            }
        }*/
    }
}
