package com.reactnative.samsunghealth;

import android.support.annotation.Nullable;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by firodj on 5/2/17.
 */

public class SamsungHealthPackage implements ReactPackage {

    public static String PACKAGE_NAME;

    public SamsungHealthPackage() {
        if (PACKAGE_NAME == null) {
            this.PACKAGE_NAME = "com.reactnative.samsunghealth";
        }
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new SamsungHealthModule(reactContext));
        return modules;
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

}

/* vim :set ts=4 sw=4 sts=4 et : */