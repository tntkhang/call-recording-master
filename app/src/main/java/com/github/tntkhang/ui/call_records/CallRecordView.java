package com.github.tntkhang.ui.call_records;

import java.util.List;

import com.github.tntkhang.models.database.entitiy.CallDetailEntity;
import com.github.tntkhang.ui.BaseView;

public interface CallRecordView extends BaseView {

    void onGetAllEntities(List<CallDetailEntity> callDetailEntity);

    void onGetAllError(Throwable throwable);
}
