package com.zebra.jamesswinton.monitorandtogglenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.zebra.jamesswinton.monitorandtogglenetwork.utilities.PermissionsHelper;

public class MainActivity extends AppCompatActivity {

    // Permissions Helper
    private PermissionsHelper mPermissionsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionsHelper = new PermissionsHelper(this, () -> {
            startService(new Intent(this, NetworkMonitoringService.class));
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionsHelper.onRequestPermissionsResult();
    }
}