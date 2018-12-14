package com.github.tntkhang.ui.call_records;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
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
import android.widget.RadioGroup;

import com.github.tntkhang.models.database.entitiy.CallDetailEntity;
import com.github.tntkhang.models.database.repository.CallDetailRepository;
import com.github.tntkhang.ui.BaseFragment;
import com.github.tntkhang.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import khangtran.preferenceshelper.PreferencesHelper;
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

            PreferencesHelper.getInstance().setValue(Constants.RECORD_TYPE, recordValue);
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
        String recordType = "";
        int recordValue = PreferencesHelper.getInstance().getIntValue(Constants.RECORD_TYPE, 0);
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

        for (int i = 0; i < 4; i++) {

        }
    }

    private MediaRecorder checkRecord(int method) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.reset();

        String outputPath = getContext().getCacheDir().getPath();

        String recordType = "";
        int recordValue = PreferencesHelper.getInstance().getIntValue(Constants.RECORD_TYPE, 0);
        switch(recordValue){
            case 0:
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

        recorder.setAudioSource(method);
        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncodingBitRate(96000);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recorder.setOutputFile(outputPath);

        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            isCanRecord = false;
            e.printStackTrace();
        }

        return recorder;
    }

    private MediaRecorder startChecking(int method) {
        CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {

            MediaRecorder recorder;

            @Override
            public void onTick(long l) {
                recorder = checkRecord(method);
            }

            @Override
            public void onFinish() {
                stopChecking(recorder);
            }
        }
        return checkRecord(method);
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
        PreferencesHelper.getInstance().setValue(Constants.PHONE_NUMBER_TO_RECORD, phoneNo.getText().toString());
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
