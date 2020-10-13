package com.zebra.jamesswinton.monitorandtogglenetwork;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zebra.jamesswinton.monitorandtogglenetwork.utilities.NotificationHelper;
import com.zebra.jamesswinton.monitorandtogglenetwork.MXManager.OnConnectionStateChangedListener;

import static com.zebra.jamesswinton.monitorandtogglenetwork.utilities.NotificationHelper.NOTIFICATION_ID;

public class NetworkMonitoringService extends Service implements OnConnectionStateChangedListener {

    // Debugging
    private static final String TAG = "NetworkMonitoringService";

    // Managers
    private ConnectivityManager mConnectivityManager = null;
    private MXManager mMXManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Start Service
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        startForeground(NOTIFICATION_ID, NotificationHelper.createNotification(this));

        // Init Managers
        this.mMXManager = new MXManager(this, this);
        this.mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Set Default State
        enableEthernetAndDisableWifi();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterEthernetNetworkCallback();
    }

    /**
     * MX XML Methods
     */

    private void enableEthernetAndDisableWifi() {
        Toast.makeText(this, "Enabling Ethernet", Toast.LENGTH_SHORT).show();
        mMXManager.enableEthernetAndDisableWifi();
    }

    private void disableEthernetAndEnableWifi() {
        Toast.makeText(this, "Enabling WIFI", Toast.LENGTH_SHORT).show();
        mMXManager.disableEthernetAndEnableWifi();
    }

    /**
     * MX Callbacks
     */

    @Override
    public void onEnabledWifi() {
        // Validate Wifi Connection (We allow is connecting because we'll assume it'll complete)
        if (isConnectedOrConnectingWifi()) {
            long delay = 30000;
            Toast.makeText(this, "WIFI Connected - reverting to Ethernet in: " + delay + "ms", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(this::enableEthernetAndDisableWifi, delay);
        } else {
            Toast.makeText(this, "WIFI Not Connected - reverting to Ethernet", Toast.LENGTH_SHORT).show();
            enableEthernetAndDisableWifi();
        }
    }

    @Override
    public void onEnabledEthernet() {
        // Validate Connection Manually
        if (isConnectedEthernet()) {
            // We've got a connection, lets listen for any drops -> results are handled in the callback
            Toast.makeText(this, "Ethernet connected - monitoring connection", Toast.LENGTH_SHORT).show();
            registerEthernetNetworkCallback();
        } else {
            // No connection on Ethernet, fall back to Wifi
            Toast.makeText(this, "Ethernet not connected - reveting to WIFI", Toast.LENGTH_SHORT).show();
            disableEthernetAndEnableWifi();
        }
    }

    @Override
    public void onError(String statusCode, String extendedStatusCode) {
        // Oh no...
        Log.e(TAG, "Error: " + statusCode + " " + extendedStatusCode);
        Toast.makeText(this, "Profile Error: " + statusCode, Toast.LENGTH_SHORT).show();
    }

    /**
     * Network Monitoring
     */

    public void registerEthernetNetworkCallback() {
        if (mConnectivityManager != null) {
            this.mConnectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(),
                    mEthernetNetworkCallback);
        }
    }

    public void unRegisterEthernetNetworkCallback() {
        if (mConnectivityManager != null) {
            this.mConnectivityManager.unregisterNetworkCallback(mEthernetNetworkCallback);
        }
    }

    /**
     * Network Callback (Android)
     */

    private ConnectivityManager.NetworkCallback mEthernetNetworkCallback = new ConnectivityManager.NetworkCallback(){
        @Override
        public void onAvailable(@NonNull Network network) {
            super.onAvailable(network);
            Log.i(TAG, "Ethernet Available");
            Toast.makeText(NetworkMonitoringService.this, "Ethernet Available", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLost(@NonNull Network network) {
            super.onLost(network);
            Log.i(TAG, "Ethernet Lost");
            Toast.makeText(NetworkMonitoringService.this, "Ethernet Lost", Toast.LENGTH_SHORT).show();
            disableEthernetAndEnableWifi();
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            Log.i(TAG, "Ethernet Unavailable");
            Toast.makeText(NetworkMonitoringService.this, "Ethernet Unavailable", Toast.LENGTH_SHORT).show();
            disableEthernetAndEnableWifi();
        }
    };

    /**
     * Utility Methods
     */

    public boolean isConnectedOrConnectingWifi(){
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnectedOrConnecting() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean isConnectedEthernet(){
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_ETHERNET);
    }

    /**
     *
     * Unused Service Methods
     *
     */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Binding not supported");
    }
}
