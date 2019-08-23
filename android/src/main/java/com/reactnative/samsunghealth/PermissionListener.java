package com.reactnative.samsunghealth;

import android.database.Cursor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Toast;

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

public class PermissionListener implements
    HealthResultHolder.ResultListener<PermissionResult>
{
    private SamsungHealthModule mModule;
    private Callback mSuccessCallback;
    private Callback mErrorCallback;

    private static final String REACT_MODULE = "RNSamsungHealth";

    public PermissionListener(SamsungHealthModule module, Callback error, Callback success)
    {
        mModule = module;
        mSuccessCallback = success;
        mErrorCallback = error;
    }

    @Override
    public void onResult(PermissionResult result) {
        Log.d(REACT_MODULE, "Permission callback is received.");
        Map<PermissionKey, Boolean> resultMap = result.getResultMap();

        if (resultMap.containsValue(Boolean.FALSE)) {
            Log.e(REACT_MODULE, "NOT CONNECTED YET");
            mErrorCallback.invoke("Permisson canceled");
        } else {
            Log.d(REACT_MODULE, "COUNT THE STEPS!");
            mSuccessCallback.invoke(true);
        }
    }
};
