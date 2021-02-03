package com.reactnative.samsunghealth;

import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.facebook.react.bridge.Callback;

import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.facebook.react.bridge.Promise;

public class ConnectionListener implements HealthDataStore.ConnectionListener {
    private Callback mSuccessCallback;
    private Callback mErrorCallback;
    private Promise mPromise;
    private SamsungHealthModule mModule;
    private HealthConnectionErrorResult mConnError;

    private static final String REACT_MODULE = "RNSamsungHealth";

    public Set<PermissionKey> mKeySet;

    public ConnectionListener(SamsungHealthModule module, Promise promise) {
        mModule = module;
        mPromise = promise;
        mKeySet = new HashSet<PermissionKey>();
    }

    public void addReadPermission(String name) {
        mKeySet.add(new PermissionKey(name, PermissionType.READ));
    }

    @Override
    public void onConnected() {
        if (mKeySet.isEmpty()) {
            Log.e(REACT_MODULE, "Permission is empty");
            mPromise.reject("Permission is empty");
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
                        .setResultListener(new PermissionListener(mModule, mPromise));
            } else {
                // Get the current step count and display it
                Log.d(REACT_MODULE, "COUNT THE STEPS!");
                mPromise.resolve(true);
            }
        } catch (Exception e) {
            Log.e(REACT_MODULE, "CHECK");
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            mPromise.reject("Permission setting fails");
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