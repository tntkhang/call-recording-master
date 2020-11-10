package com.github.tntkhang.callrecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.github.tntkhang.utils.Constants;

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

        String phoneNumber = intent.getStringExtra(Constants.Prefs.PHONE_CALL_NUMBER);
        String outputPath = intent.getStringExtra(Constants.Prefs.CALL_RECORD_PATH);
        Log.d(TAGS, "Phone number in service: " + phoneNumber);

//            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
//            } else if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            } else {
//                recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
//            }

        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncodingBitRate(96000);


        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

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
            Log.d(TAGS, "onDestroy: " + "Recording stopped");
        }
    }
}
