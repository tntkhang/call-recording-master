package com.github.tntkhang.callrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.github.tntkhang.BaseApplication;
import com.github.tntkhang.models.database.entitiy.CallDetailEntity;
import com.github.tntkhang.models.database.repository.CallDetailRepository;
import com.github.tntkhang.utils.CommonMethods;
import com.github.tntkhang.utils.Constants;

import javax.inject.Inject;

import khangtran.preferenceshelper.PrefHelper;

public class PhoneStateReceiver extends BroadcastReceiver {

    @Inject
    CallDetailRepository callDetailRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        BaseApplication.getApplication().getAppComponent().inject(this);
        try {
            Bundle extras = intent.getExtras();
            String state = extras.getString(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                String recPath = startRecording(context, phoneNumber);

                PrefHelper.setVal(Constants.Prefs.CALL_RECORD_STARTED, !recPath.isEmpty());
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (PrefHelper.getBooleanVal(Constants.Prefs.CALL_RECORD_STARTED, false)) {
                    String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    stopRecording(context, phoneNumber);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String startRecording(Context context, String number) {
        String trimNumber = CommonMethods.removeSpaceInPhoneNo(number);

        String time = CommonMethods.getClearTime();
        String date = CommonMethods.getDate();
        String path = CommonMethods.getPath();
        String outputPath = path + "/" + trimNumber + "_" + time + ".mp3";

        Intent recordService = new Intent(context, CallRecorderService.class);
        recordService.putExtra(Constants.PHONE_CALL_NUMBER, trimNumber);
        recordService.putExtra(Constants.CALL_RECORD_PATH, outputPath);

        context.startService(recordService);

        String name = "";
        int recordValue = PrefHelper.getIntVal(Constants.RECORD_TYPE, 0);
        switch(recordValue){
            case 0:
                name = "AudioSource.DEFAULT";
                break;
            case 1:
                name = "AudioSource.MIC";
                break;
            case 2:
                name = "AudioSource.VOICE_CALL";
                break;
            case 3:
                name = "AudioSource.VOICE_COMMUNICATION";
                break;
        }
        CallDetailEntity callDetailEntity = new CallDetailEntity(trimNumber, name, time, date, outputPath);
        callDetailRepository.insert(callDetailEntity);
        return outputPath;
    }

    private void stopRecording(Context context, String phoneNo) {
        context.stopService(new Intent(context, CallRecorderService.class));
    }
}
