package com.example.a10234_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity {
    private static final double BATTERY_LEVEL_REQUIRE = 0.90;
    ShapeableImageView image1;
    ShapeableImageView image2;
    ShapeableImageView image3;
    AppCompatButton checkingButton;
    AppCompatButton successButton;
    AppCompatTextView wifiTextView;
    AppCompatTextView nfcTextView;
    AppCompatTextView batteryTextView;
    private int currentImageIndex;
    private int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void hideImages() {
        switch (currentImageIndex) {
            case 0:
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null && wifiManager.isWifiEnabled()) {
                    // WiFi is enabled
                    image1.setImageResource(R.drawable.wifi);
                    wifiTextView.setText("WIFI is enabled");
                    counter++;
                } else {
                    // WiFi is not enabled
                    image1.setImageResource(R.drawable.no_wifi);
                    wifiTextView.setText("");
                    Toast.makeText(this, "Hint - WIFI", Toast.LENGTH_SHORT).show();
                }
                continueChecking();
                break;
            case 1:
                NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
                if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                    // NFC is enabled
                    image2.setImageResource(R.drawable.nfc);
                    nfcTextView.setText("NFC is enabled");
                    counter++;
                } else {
                    // NFC is not enabled
                    image2.setImageResource(R.drawable.no_nfc);
                    nfcTextView.setText("");
                    Toast.makeText(this, "Hint - NFC", Toast.LENGTH_SHORT).show();
                }
                continueChecking();
                break;
            case 2:
                if (checkingIfDeviceIsCharging(this) || checkingDeviceButteryLevel(this, BATTERY_LEVEL_REQUIRE)) {
                    // Battery is charging or more than 90%
                    image3.setImageResource(R.drawable.battery);
                    batteryTextView.setText("Battery is charging or more than 90% ");
                    counter++;
                } else {
                    // Battery is not charging and less than 90%
                    image3.setImageResource(R.drawable.no_battery);
                    batteryTextView.setText("");
                    Toast.makeText(this, "Hint - Battery", Toast.LENGTH_SHORT).show();
                }
                continueChecking();
                break;
            default:
                Toast.makeText(this, "All images are visible", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void continueChecking() {
        currentImageIndex++;
        if (counter == 3) {
            checkingButton.setVisibility(View.INVISIBLE);
            successButton.setVisibility(View.VISIBLE);
        } else if (currentImageIndex < 3) {
            hideImages();
        }
    }

    private void initViews() {
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);

        wifiTextView = findViewById(R.id.wifiTextView);
        nfcTextView = findViewById(R.id.nfcTextView);
        batteryTextView = findViewById(R.id.batteryTextView);

        checkingButton = findViewById(R.id.checkingButton);

        checkingButton.setOnClickListener(v -> {
            counter = 0;
            currentImageIndex = 0;
            hideImages();
        });

        successButton = findViewById(R.id.successButton);
        successButton.setVisibility(View.INVISIBLE);
    }


    public boolean checkingIfDeviceIsCharging(Context context) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        assert batteryStatus != null;
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        return status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
    }

    public boolean checkingDeviceButteryLevel(Context context, double pct) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, intentFilter);
        assert batteryStatus != null;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float) scale;
        return batteryPct > pct;
    }
}