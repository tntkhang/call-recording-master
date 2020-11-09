package com.github.tntkhang.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.github.tntkhang.ui.BaseActivity;
import com.github.tntkhang.ui.call_records.CallRecordFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.ButterKnife;
import vn.nextlogix.tntkhang.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermissions();

        Fragment recordFragment = CallRecordFragment.newInstance();

        replaceFragment(recordFragment);
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
