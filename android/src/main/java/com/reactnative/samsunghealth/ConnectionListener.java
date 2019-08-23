package com.reactnative.samsunghealth;

import android.database.Cursor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.LifecycleEventListener;

import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.Filter;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthDevice;
import com.samsung.android.sdk.healthdata.HealthDeviceManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionResult;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConnectionListener implements HealthDataStore.ConnectionListener {
    private Callback mSuccessCallback;
    private Callback mErrorCallback;
    private SamsungHealthModule mModule;
    private HealthConnectionErrorResult mConnError;

    private static final String REACT_MODULE = "RNSamsungHealth";

    public Set<PermissionKey> mKeySet;

    public ConnectionListener(SamsungHealthModule module, Callback error, Callback success) {
        mModule = module;
        mErrorCallback = error;
        mSuccessCallback = success;
        mKeySet = new HashSet<PermissionKey>();
    }

    public void addReadPermission(String name) {
        // mKeySet.add(new PermissionKey(name, PermissionType.READ));
        // mKeySet.add(new PermissionKey(SamsungHealthModule.STEP_DAILY_TREND_TYPE,
        // PermissionType.READ));

        mKeySet.add(new PermissionKey("com.samsung.shealth.step_daily_trend", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.weight", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.height", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.heart_rate", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.blood_pressure", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.sleep", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.body_temperature", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.total_cholesterol", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.water_intake", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.nutrition", PermissionType.READ));
        mKeySet.add(new PermissionKey("com.samsung.health.step_count", PermissionType.READ));

    }

    @Override
    public void onConnected() {
        if (mKeySet.isEmpty()) {
            Log.e(REACT_MODULE, "Permission is empty");
            mErrorCallback.invoke("Permission is empty");
            return;
        }

        Log.d(REACT_MODULE, "Health data service is connected.");
        HealthPermissionManager pmsManager = new HealthPermissionManager(mModule.getStore());

        try {
            // Check whether the permissions that this application needs are acquired
            Map<PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(mKeySet);

            if (resultMap.containsValue(Boolean.FALSE)) {
                // Request the permission for reading step counts if it is not acquired
                pmsManager.requestPermissions(mKeySet, mModule.getContext().getCurrentActivity())
                        .setResultListener(new PermissionListener(mModule, mErrorCallback, mSuccessCallback));
            } else {
                // Get the current step count and display it
                Log.d(REACT_MODULE, "COUNT THE STEPS!");
                mSuccessCallback.invoke(true);
            }
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            mErrorCallback.invoke("Permission setting fails");
        }
    }

    @Override
    public void onConnectionFailed(HealthConnectionErrorResult error) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mModule.getContext().getCurrentActivity());
        mConnError = error;
        String message = "Connection with Samsung Health is not available";

        if (error.hasResolution()) {
            switch (error.getErrorCode()) {
            case HealthConnectionErrorResult.PLATFORM_NOT_INSTALLED:
                message = "Please install Samsung Health";
                break;
            case HealthConnectionErrorResult.OLD_VERSION_PLATFORM:
                message = "Please upgrade Samsung Health";
                break;
            case HealthConnectionErrorResult.PLATFORM_DISABLED:
                message = "Please enable Samsung Health";
                break;
            case HealthConnectionErrorResult.USER_AGREEMENT_NEEDED:
                message = "Please agree with Samsung Health policy";
                break;
            default:
                message = "Please make Samsung Health available";
                break;
            }
        }

        alert.setMessage(message);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mConnError.hasResolution()) {
                    mConnError.resolve(mModule.getContext().getCurrentActivity());
                }
            }
        });

        if (error.hasResolution()) {
            alert.setNegativeButton("Cancel", null);
        }

        alert.show();
        // mErrorCallback.invoke(message);
    }

    @Override
    public void onDisconnected() {
        Log.d(REACT_MODULE, "Health data service is disconnected.");
        // mErrorCallback.invoke("Health data service is disconnected.");
    }
};