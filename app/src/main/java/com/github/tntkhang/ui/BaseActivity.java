package com.github.tntkhang.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.tntkhang.BaseApplication;
import com.github.tntkhang.di.AppComponent;

public class BaseActivity extends AppCompatActivity {

    public AppComponent getComponent() {
        return BaseApplication.getApplication().getAppComponent();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Context currentActivity, Class nextActivity) {
        Intent i = new Intent(currentActivity, nextActivity);
        startActivity(i);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
    }

    public void finishWithoutAnim() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
    }
}
