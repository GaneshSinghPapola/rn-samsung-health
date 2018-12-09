package com.reactnative.samsunghealth;

import android.database.Cursor;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray;
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

/**
 * Created by firodj on 5/2/17.
 */

@ReactModule(name = "RNSamsungHealth")
public class SamsungHealthModule extends ReactContextBaseJavaModule implements
        LifecycleEventListener {

    private static final String REACT_MODULE = "RNSamsungHealth";
    public static final String STEP_DAILY_TREND_TYPE = "com.samsung.shealth.step_daily_trend";
    public static final String DAY_TIME = "day_time";

    private HealthDataStore mStore;

    public SamsungHealthModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_MODULE;
    }

    @Override
    public void initialize() {
        super.initialize();

        getReactApplicationContext().addLifecycleEventListener(this);
        initSamsungHealth();
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("STEP_COUNT", HealthConstants.StepCount.HEALTH_DATA_TYPE);
        constants.put("WEIGHT", HealthConstants.Weight.HEALTH_DATA_TYPE);
        constants.put("STEP_DAILY_TREND", SamsungHealthModule.STEP_DAILY_TREND_TYPE);



        return constants;
    }

    @Override
    public void onHostResume() {
    }

    @Override
    public void onHostPause() {
    }

    @Override
    public void onHostDestroy() {
    }

    private void sendEvent(String eventName,
                           @Nullable WritableMap params) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }


    public void initSamsungHealth() {
        Log.d(REACT_MODULE, "initialize Samsung Health...");
        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(getReactApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HealthDataStore getStore()
    {
        return mStore;
    }

    public ReactContext getContext()
    {
        return getReactApplicationContext();
    }

    @ReactMethod
    public void connect(ReadableArray permissions, Callback error, Callback success)
    {
        ConnectionListener listener = new ConnectionListener(this, error, success);
        for (int i = 0; i < permissions.size(); i++) {
            listener.addReadPermission(permissions.getString(i));
        }

        mStore = new HealthDataStore(getReactApplicationContext(), listener);
        mStore.connectService();
    }

    @ReactMethod
    public void disconnect()
    {
        if (mStore != null) {
            Log.d(REACT_MODULE, "disconnectService");
            mStore.disconnectService();
            mStore = null;
        }
    }

    /*
    private final HealthDataObserver mObserver = new HealthDataObserver(null) {
        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d(REACT_MODULE, "Observer receives a data changed event");
            readStepCount();
        }
    };
    private void start() {
        // Register an observer to listen changes of step count and get today step count
        // HealthDataObserver.addObserver(mStore, HealthConstants.StepCount.HEALTH_DATA_TYPE, mObserver);
        readStepCount();
    }
     */

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }

    // Read the today's step count on demand
    @ReactMethod
    public void readStepCount(double startDate, double endDate, Callback error, Callback success) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        Filter filter = Filter.and(
            Filter.greaterThanEquals(SamsungHealthModule.DAY_TIME, (long)startDate),
            Filter.lessThanEquals(SamsungHealthModule.DAY_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(SamsungHealthModule.STEP_DAILY_TREND_TYPE)
                .setProperties(new String[]{
                        HealthConstants.StepCount.COUNT,
                        HealthConstants.StepCount.DISTANCE,
                        SamsungHealthModule.DAY_TIME,
                        HealthConstants.StepCount.CALORIE,
                        HealthConstants.StepCount.DEVICE_UUID 
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting step count fails.");
            error.invoke("Getting step count fails.");
        }


    }


    @ReactMethod
    public void readHeight(double startDate, double endDate, Callback error, Callback success) {
     HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.and(
            Filter.greaterThanEquals(HealthConstants.Height.START_TIME, (long)startDate),
            Filter.lessThanEquals(HealthConstants.Height.START_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.Height.HEALTH_DATA_TYPE)
                .setProperties(new String[]{
                        HealthConstants.Height.HEIGHT,
                        HealthConstants.Height.START_TIME,  
                        HealthConstants.Height.TIME_OFFSET,
                        HealthConstants.Height.DEVICE_UUID 
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting Height fails.");
            error.invoke("Getting Height fails.");
        }
}


   @ReactMethod
    public void readBloodPressure(double startDate, double endDate, Callback error, Callback success) {
    HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.and(
            Filter.greaterThanEquals(HealthConstants.BloodPressure.START_TIME, (long)startDate),
            Filter.lessThanEquals(HealthConstants.BloodPressure.START_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.BloodPressure.HEALTH_DATA_TYPE)
                .setProperties(new String[]{
                        HealthConstants.BloodPressure.DIASTOLIC,
                        HealthConstants.BloodPressure.MEAN,
                        HealthConstants.BloodPressure.PULSE,
                        HealthConstants.BloodPressure.START_TIME,
                        HealthConstants.BloodPressure.SYSTOLIC,
                        HealthConstants.BloodPressure.TIME_OFFSET, 
                        HealthConstants.BloodPressure.DEVICE_UUID 
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting BloodPressure fails.");
            error.invoke("Getting BloodPressure fails.");
        }
}



  @ReactMethod
    public void readBodyTemprature(double startDate, double endDate, Callback error, Callback success) {
    HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.and(
            Filter.greaterThanEquals(HealthConstants.BodyTemperature.START_TIME, (long)startDate),
            Filter.lessThanEquals(HealthConstants.BodyTemperature.START_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.BodyTemperature.HEALTH_DATA_TYPE) 
                .setProperties(new String[]{
                        HealthConstants.BodyTemperature.START_TIME,
                        // HealthConstants.BodyTemperature.END_TIME,
                        HealthConstants.BodyTemperature.TEMPERATURE,
                        HealthConstants.BodyTemperature.TIME_OFFSET,
                        HealthConstants.BodyTemperature.DEVICE_UUID 
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting BodyTemperature fails.");
            error.invoke("Getting BodyTemperature fails.");
        }
}





@ReactMethod
    public void readHeartRate(double startDate, double endDate, Callback error, Callback success) {
    HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.and(
            Filter.greaterThanEquals(HealthConstants.HeartRate.START_TIME, (long)startDate),
            Filter.lessThanEquals(HealthConstants.HeartRate.END_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.HeartRate.HEALTH_DATA_TYPE) 
                .setProperties(new String[]{
                        HealthConstants.HeartRate.START_TIME,
                        HealthConstants.HeartRate.END_TIME,
                        HealthConstants.HeartRate.HEART_RATE,
                        HealthConstants.HeartRate.TIME_OFFSET,
                        HealthConstants.HeartRate.DEVICE_UUID 
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting HeartRate fails.");
            error.invoke("Getting HeartRate fails.");
        }
}


@ReactMethod
    public void readSleep(double startDate, double endDate, Callback error, Callback success) {
    HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.and(
            Filter.greaterThanEquals(HealthConstants.Sleep.START_TIME, (long)startDate),
            Filter.lessThanEquals(HealthConstants.Sleep.END_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.Sleep.HEALTH_DATA_TYPE) 
                .setProperties(new String[]{
                        HealthConstants.Sleep.START_TIME,
                        HealthConstants.Sleep.END_TIME,
                        HealthConstants.Sleep.TIME_OFFSET,
                        HealthConstants.Sleep.DEVICE_UUID
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting Sleep fails.");
            error.invoke("Getting Sleep fails.");
        }
}


    @ReactMethod
    public void readWeight(double startDate, double endDate, Callback error, Callback success) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.and(
            Filter.greaterThanEquals(HealthConstants.Weight.START_TIME, (long)startDate),
            Filter.lessThanEquals(HealthConstants.Weight.START_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.Weight.HEALTH_DATA_TYPE) 
                .setProperties(new String[]{
                        HealthConstants.Weight.WEIGHT,
                        // HealthConstants.Weight.END_TIME,
                        HealthConstants.Weight.START_TIME,
                        HealthConstants.Weight.TIME_OFFSET, 
                        HealthConstants.Weight.DEVICE_UUID  
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting weight fails.");
            error.invoke("Getting weight fails.");
        }
    }


    @ReactMethod
    public void readCholesterol(double startDate, double endDate, Callback error, Callback success) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);
        Filter filter = Filter.and(
            Filter.greaterThanEquals(HealthConstants.TotalCholesterol.START_TIME, (long)startDate),
            Filter.lessThanEquals(HealthConstants.TotalCholesterol.START_TIME, (long)endDate)
        );
        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.TotalCholesterol.HEALTH_DATA_TYPE)
                .setProperties(new String[]{
                        HealthConstants.TotalCholesterol.TOTAL_CHOLESTEROL,
                        HealthConstants.TotalCholesterol.START_TIME,
                        HealthConstants.TotalCholesterol.TIME_OFFSET, 
                        HealthConstants.TotalCholesterol.DEVICE_UUID
                })
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(new HealthDataResultListener(this, error, success));
        } catch (Exception e) {
            Log.e(REACT_MODULE, e.getClass().getName() + " - " + e.getMessage());
            Log.e(REACT_MODULE, "Getting TotalCholesterol fails.");
            error.invoke("Getting TotalCholesterol fails.");
        }
    }

}