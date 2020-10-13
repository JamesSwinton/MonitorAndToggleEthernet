package com.zebra.jamesswinton.monitorandtogglenetwork;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.zebra.jamesswinton.monitorandtogglenetwork.profilemanager.MXProfile;
import com.zebra.jamesswinton.monitorandtogglenetwork.profilemanager.ProcessProfile;
import com.zebra.jamesswinton.monitorandtogglenetwork.profilemanager.XML;

import static com.zebra.jamesswinton.monitorandtogglenetwork.profilemanager.XML.ENABLE_ETHERNET_PROFILE_NAME;

public class MXManager implements EMDKManager.EMDKListener, ProcessProfile.OnProfileProcessed {

    // Debugging
    private static final String TAG = "MXConnectionToggler";

    // Callback
    private OnConnectionStateChangedListener mOnConnectionStateChangedListener;

    // MX
    private Context mCx = null;
    private MXProfile mMXProfile = null;
    private EMDKManager mEmdkManager = null;

    public MXManager(Context cx, OnConnectionStateChangedListener onConnectionStateChangedListener) {
        this.mCx = cx;
        this.mOnConnectionStateChangedListener = onConnectionStateChangedListener;
    }

    public void enableEthernetAndDisableWifi() {
        mMXProfile = XML.getEnableEthernetDisableWifiProfile();
        initEMDK();
    }

    public void disableEthernetAndEnableWifi() {
        mMXProfile = XML.getDisableEthernetEnableWifiXML();
        initEMDK();
    }

    /**
     * EMDK
     */

    private void initEMDK() {
        // Init EMDK
        EMDKResults emdkManagerResults = EMDKManager.getEMDKManager(mCx, this);
        if (emdkManagerResults == null || emdkManagerResults.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            // Log Error
            Log.e(TAG, "onCreate: Failed to get EMDK Manager -> " +
                    (emdkManagerResults == null ? "No Results Returned" : emdkManagerResults.statusCode));
            Toast.makeText(mCx, "Failed to get EMDK Manager!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        // Get Profile & Version Manager Instances
        mEmdkManager = emdkManager;
        ProfileManager mProfileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);

        // Apply Profile
        if (mProfileManager != null) {
            new ProcessProfile(mCx, mMXProfile.getProfileName(), mProfileManager, this)
                    .execute(mMXProfile.getProfileXml());
        } else {
            Log.e(TAG, "Error Obtaining ProfileManager!");
            Toast.makeText(mCx, "Error Obtaining ProfileManager!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onClosed() {
        // Release EMDK Manager Instance
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    /**
     * Profile Callback
     */

    @Override
    public void profileApplied(String statusCode, String extendedStatusCode) {
        mEmdkManager.release();
        if (mMXProfile.getProfileName().equals(ENABLE_ETHERNET_PROFILE_NAME)) {
            mOnConnectionStateChangedListener.onEnabledEthernet();
        } else {
            mOnConnectionStateChangedListener.onEnabledWifi();
        }
    }

    @Override
    public void profileError(String statusCode, String extendedStatusCode) {
        mEmdkManager.release();
    }

    /**
     * Callback
     */

    public interface OnConnectionStateChangedListener {
        void onEnabledWifi();
        void onEnabledEthernet();
        void onError(String statusCode, String extendedStatusCode);
    }

}
