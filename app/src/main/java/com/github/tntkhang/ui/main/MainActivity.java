package com.github.tntkhang.ui.main;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.github.tntkhang.callrecorder.CallRecorderService;
import com.github.tntkhang.callrecorder.MyAccessibilityService;
import com.github.tntkhang.ui.BaseActivity;
import com.github.tntkhang.ui.call_records.CallRecordFragment;
import com.github.tntkhang.utils.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.ButterKnife;
import vn.nextlogix.tntkhang.R;

public class MainActivity extends BaseActivity {
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 2323;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermissions();

        Fragment recordFragment = CallRecordFragment.newInstance();

        replaceFragment(recordFragment);

//
//        Intent accessService = new Intent(this, MyAccessibilityService.class);
//
//        startService(accessService);
    }

    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
            }
        })
        .check();
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
