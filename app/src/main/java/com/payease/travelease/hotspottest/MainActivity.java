package com.payease.travelease.hotspottest;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PASSWORD = "qwer1234";
    private static final String SSID = "FREEDY-Hotspot";

    View btnUpdateHotspotSettings;
    View btnOpenHotspotSettingsScreen;
    TextView txtTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTextView = (TextView) findViewById(R.id.textView);
        btnUpdateHotspotSettings = findViewById(R.id.button_update_hotspot_config);
        btnOpenHotspotSettingsScreen = findViewById(R.id.button_open_hotspot_settings);

        StringBuilder builder = new StringBuilder().append("This will change hotspot config to\n")
                .append("SSID: ")
                .append(SSID)
                .append("\n")
                .append("Password: ")
                .append(PASSWORD)
                .append("\n");
        txtTextView.setText(builder.toString());

        btnUpdateHotspotSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                changeHotspotSettings(SSID, PASSWORD);
                updateHotspotSettings(SSID, PASSWORD);
            }
        });

        btnOpenHotspotSettingsScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHotspotSettingsScreen();
            }
        });
    }

    private void openHotspotSettingsScreen() {
        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }

    private void updateHotspotSettings(String ssid, String password) {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        disableWifi(wm);
        WifiConfiguration wifiCon = new WifiConfiguration();
        wifiCon.preSharedKey = password;
        wifiCon.hiddenSSID = false;
        wifiCon.SSID = ssid;
        wifiCon.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiCon.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiCon.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiCon.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
//        wifiCon.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiCon.allowedKeyManagement.set(4); //4 = WPA2_PSK

        try {
            Method setWifiApMethod = wm.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean accessPointStatus = (Boolean) setWifiApMethod.invoke(wm, wifiCon, true);
            String msg = "Access Point Status: " + accessPointStatus;
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void disableWifi(WifiManager wm) {
        if (wm.isWifiEnabled()) {
            wm.setWifiEnabled(false);
        }
    }

}
