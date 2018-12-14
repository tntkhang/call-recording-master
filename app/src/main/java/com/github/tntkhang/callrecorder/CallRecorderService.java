package com.github.tntkhang.callrecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.github.tntkhang.utils.Constants;

import khangtran.preferenceshelper.PrefHelper;

public class CallRecorderService extends Service {

    MediaRecorder recorder;
    static final String TAGS = "tntkhang";

    private boolean isStartRecordSuccess = true;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        recorder = new MediaRecorder();
        recorder.reset();

        String phoneNumber = intent.getStringExtra(Constants.PHONE_CALL_NUMBER);
        String outputPath = intent.getStringExtra(Constants.CALL_RECORD_PATH);
        Log.d(TAGS, "Phone number in service: " + phoneNumber);

        String recordType = "";
        int recordValue = PrefHelper.getIntVal(Constants.RECORD_TYPE, 0);
        switch(recordValue){
            case 0:
                recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                recordType = "DEFAULT";
                break;
            case 1:
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recordType = "MIC";
                break;
            case 2:
                recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                recordType = "VOICE_CALL";
                break;
            case 3:
                recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                recordType = "VOICE_COMMUNICATION";
                break;
        }

        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncodingBitRate(96000);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recorder.setOutputFile(outputPath);

        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            isStartRecordSuccess = false;
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        if (isStartRecordSuccess) {
            try {
                if (recorder != null) {
                    recorder.stop();
                    recorder.reset();
                    recorder.release();
                    recorder = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Stop recording audio", Toast.LENGTH_SHORT).show();
            Log.d(TAGS, "onDestroy: " + "Recording stopped");
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("com/github/tntkhang", "onTaskRemoved: ");
    }
}
