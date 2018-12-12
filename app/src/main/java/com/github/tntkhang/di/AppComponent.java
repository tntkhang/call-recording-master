package com.github.tntkhang.di;

import com.github.tntkhang.ui.call_records.CallRecordFragment;

import javax.inject.Singleton;

import dagger.Component;
import com.github.tntkhang.callrecorder.PhoneStateReceiver;

@Singleton
@Component(modules = {DatabaseModule.class})
public interface AppComponent {
    void inject(CallRecordFragment callRecordFragment);
    void inject(PhoneStateReceiver receiver);
}
