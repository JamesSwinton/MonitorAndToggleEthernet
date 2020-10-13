package com.zebra.jamesswinton.monitorandtogglenetwork.profilemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;
import com.symbol.emdk.ProfileManager.PROFILE_FLAG;
import com.zebra.jamesswinton.monitorandtogglenetwork.utilities.CustomDialog;

import java.lang.ref.WeakReference;

import static com.symbol.emdk.EMDKResults.STATUS_CODE.CHECK_XML;
import static com.symbol.emdk.EMDKResults.STATUS_CODE.FAILURE;
import static com.symbol.emdk.EMDKResults.STATUS_CODE.SUCCESS;

public class ProcessProfile extends AsyncTask<String, Void, EMDKResults> {

  // Debugging
  private static final String TAG = "ProcessProfile";

  // Profile Variables
  private String mProfileName;
  private ProfileManager mProfileManager;
  private OnProfileProcessed mOnProfileProcessed;
  private WeakReference<Context> mContextWekRef = null;

  public ProcessProfile(Context cx, String profileName, ProfileManager profileManager, OnProfileProcessed onProfileProcessed) {
    this.mProfileName = profileName;
    this.mProfileManager = profileManager;
    this.mOnProfileProcessed = onProfileProcessed;
    this.mContextWekRef = new WeakReference<>(cx);
  }

  @Override
  protected EMDKResults doInBackground(String... params) {
    return mProfileManager.processProfile(mProfileName, PROFILE_FLAG.SET, params);
  }

  @Override
  protected void onPostExecute(EMDKResults results) {
    super.onPostExecute(results);
    Log.i(TAG, "Profile Manager Result: " + results.statusCode + " | " + results.extendedStatusCode);

    // Notify Class
    if (results.statusCode.equals(CHECK_XML) || results.statusCode.equals(SUCCESS)) {
      Log.i(TAG, "XML: " + results.getStatusString());
      mOnProfileProcessed.profileApplied(results.statusCode.toString(),
              results.extendedStatusCode.toString());
    } else if (results.statusCode.equals(FAILURE)) {
      mOnProfileProcessed.profileError(results.statusCode.toString(),
              results.extendedStatusCode.toString());
    }
  }

  public interface OnProfileProcessed {
    void profileApplied(String statusCode, String extendedStatusCode);
    void profileError(String statusCode, String extendedStatusCode);
  }
}
