package com.github.tntkhang.ui.call_records;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import com.github.tntkhang.models.database.entitiy.CallDetailEntity;
import com.github.tntkhang.models.database.repository.CallDetailRepository;
import com.github.tntkhang.ui.BasePresenter;

public class CallRecordPresenter extends BasePresenter {
    private CallDetailRepository callDetailRepository;
    private CallRecordView mView;

    public CallRecordPresenter(CallDetailRepository callDetailRepository, CallRecordView view) {
        this.callDetailRepository = callDetailRepository;
        this.mView = view;
    }

    public void getAll() {
        Disposable disposable = callDetailRepository.getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextGetAllNotifications, this::onErrorGetAllNotifications);
        disposeBag.add(disposable);
    }



    private void onNextGetAllNotifications(List<CallDetailEntity> callDetailEntities) {
        mView.onGetAllEntities(callDetailEntities);
    }

    private void onErrorGetAllNotifications(Throwable throwable) {
        mView.onGetAllError(throwable);
    }

}
