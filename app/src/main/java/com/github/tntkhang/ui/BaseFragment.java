package com.github.tntkhang.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.tntkhang.di.AppComponent;
import com.github.tntkhang.BaseApplication;

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public AppComponent getComponent() {
        return BaseApplication.getApplication().getAppComponent();
    }


    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
