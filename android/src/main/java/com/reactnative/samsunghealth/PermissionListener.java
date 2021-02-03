package com.reactnative.samsunghealth;

import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionResult;
import com.samsung.android.sdk.healthdata.HealthResultHolder;
import com.facebook.react.bridge.Promise;

import java.util.Map;

public class PermissionListener implements HealthResultHolder.ResultListener<PermissionResult> {
    private SamsungHealthModule mModule;
    private Promise mPromise;

    private static final String REACT_MODULE = "RNSamsungHealth";

    public PermissionListener(SamsungHealthModule module, Promise promise) {
        mModule = module;
        mPromise = promise;
    }

    @Override
    public void onResult(PermissionResult result) {
        Log.d(REACT_MODULE, "Permission callback is received.");
        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

        if (resultMap.containsValue(Boolean.FALSE)) {
            Log.e(REACT_MODULE, "NOT CONNECTED YET");
            mPromise.reject("Permisson canceled");
        } else {
            Log.d(REACT_MODULE, "COUNT THE STEPS!");
            mPromise.resolve(true);
        }
    }
};
