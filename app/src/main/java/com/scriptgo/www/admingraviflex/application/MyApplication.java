package com.scriptgo.www.admingraviflex.application;

import android.app.Application;
import io.realm.Realm;

/**
 * Created by BALAREZO on 29/07/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
