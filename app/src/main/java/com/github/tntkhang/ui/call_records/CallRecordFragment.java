package com.github.tntkhang.ui.call_records;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.github.tntkhang.models.database.entitiy.CallDetailEntity;
import com.github.tntkhang.models.database.repository.CallDetailRepository;
import com.github.tntkhang.ui.BaseFragment;
import com.github.tntkhang.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import khangtran.preferenceshelper.PrefHelper;
import vn.nextlogix.tntkhang.R;

public class CallRecordFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, CallRecordView {

    @Inject
    CallDetailRepository callDetailRepository;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.phone_no)
    EditText phoneNo;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R.id.rdDefault)
    RadioButton rdDefault;

    @BindView(R.id.rdMic)
    RadioButton rdMic;

    @BindView(R.id.rdVoiceCall)
    RadioButton rdVoiceCall;

    @BindView(R.id.rdVoiceCom)
    RadioButton rdVoiceComm;

    List<CallDetailEntity> callDetailEntities;
    CallRecordAdapter mAdapter;

    CallRecordPresenter callRecordPresenter;

    public static CallRecordFragment newInstance() {
        return new CallRecordFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callDetailEntities = new ArrayList<>();
        callRecordPresenter = new CallRecordPresenter(callDetailRepository, this);
    }

    @Override
    public void onRefresh() {
        callRecordPresenter.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_recoder_list, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new CallRecordAdapter(callDetailRepository, callDetailEntities);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        callRecordPresenter.getAll();

        radioGroup.setOnCheckedChangeListener((radioGroup1, i) ->  {
            int recordValue = 0;
            switch(i){
                case R.id.rdDefault:
                    recordValue = 0;
                    break;
                case R.id.rdMic:
                    recordValue = 1;
                    break;
                case R.id.rdVoiceCall:
                    recordValue = 2;
                    break;
                case R.id.rdVoiceCom:
                    recordValue = 3;
                    break;
            }

            PrefHelper.setVal(Constants.RECORD_TYPE, recordValue);
        });

        return view;
    }

    @Override
    public void onGetAllEntities(List<CallDetailEntity> callDetailEntity) {
        callDetailEntities.clear();
        callDetailEntities.addAll(callDetailEntity);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAllError(Throwable throwable) {
        showToast(getContext(), throwable.getMessage());
    }

    @OnClick(R.id.check_record_support)
    void onCheck() {
        rdDefault.setEnabled(false);
        rdDefault.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        rdMic.setEnabled(false);
        rdMic.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        rdVoiceCall.setEnabled(false);
        rdVoiceCall.setTextColor(ContextCompat.getColor(getContext(),  android.R.color.black));
        rdVoiceComm.setEnabled(false);
        rdVoiceComm.setTextColor(ContextCompat.getColor(getContext(),  android.R.color.black));

        checkRecord(MediaRecorder.AudioSource.DEFAULT);
    }

    private void checkRecord(int method) {
        CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {

            boolean canRecord = true;
            MediaRecorder recorder;

            @Override
            public void onTick(long l) {
                recorder = new MediaRecorder();
                recorder.reset();
                recorder.setAudioSource(method);
                recorder.setAudioSamplingRate(44100);
                recorder.setAudioEncodingBitRate(96000);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                String outputPath = getContext().getCacheDir().getPath() + "/recording_temp.mp3";
                recorder.setOutputFile(outputPath);

                try {
                    recorder.prepare();
                    recorder.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    canRecord = false;
                }

                Log.i("tntkhang", "Mothod: " + method);
            }

            @Override
            public void onFinish() {
                Log.i("tntkhang", "==FNISH: " + method);
                if (canRecord) {
                    canRecord = stopChecking(recorder);
                }

                switch (method) {
                    case MediaRecorder.AudioSource.DEFAULT:
                        rdDefault.setEnabled(canRecord);
                        rdDefault.setTextColor(ContextCompat.getColor(getContext(), canRecord ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
                        break;
                    case MediaRecorder.AudioSource.MIC:
                        rdMic.setEnabled(canRecord);
                        rdMic.setTextColor(ContextCompat.getColor(getContext(), canRecord ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
                        break;
                    case MediaRecorder.AudioSource.VOICE_CALL:
                        rdVoiceCall.setEnabled(canRecord);
                        rdVoiceCall.setTextColor(ContextCompat.getColor(getContext(), canRecord ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
                        break;
                    case MediaRecorder.AudioSource.VOICE_COMMUNICATION:
                        rdVoiceComm.setEnabled(canRecord);
                        rdVoiceComm.setTextColor(ContextCompat.getColor(getContext(), canRecord ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
                        break;
                }

                checkNext(method);
            }
        };
        countDownTimer.start();
    }

    private void checkNext(int method) {
        switch (method) {
            case MediaRecorder.AudioSource.DEFAULT:
                checkRecord(MediaRecorder.AudioSource.MIC);
                break;
            case MediaRecorder.AudioSource.MIC:
                checkRecord(MediaRecorder.AudioSource.VOICE_CALL);
                break;
            case MediaRecorder.AudioSource.VOICE_CALL:
                checkRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
                break;
            case MediaRecorder.AudioSource.VOICE_COMMUNICATION:
                Toast.makeText(getContext(), "Check Done", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private boolean stopChecking(MediaRecorder recoder) {
        boolean result = true;
        if (recoder != null) {
            try {
                recoder.stop();
                recoder.reset();
                recoder.release();
                recoder = null;

                result = true;
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }


    @OnClick(R.id.btn_call)
    void onCallClick() {
        PrefHelper.setVal(Constants.PHONE_NUMBER_TO_RECORD, phoneNo.getText().toString());
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNo.getText().toString()));
        startActivity(intent);
    }

    @Override
    public void showProgressDialog(Context context) {

    }

    @Override
    public void hideProgressDialog() {

    }
}
