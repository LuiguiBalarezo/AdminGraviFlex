package com.scriptgo.www.admingraviflex.interfaces;

import com.scriptgo.www.admingraviflex.models.Dinero;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public interface CallBackProcessDineroApi {
    void connect(RealmList<Dinero> dineroAPI);
    void disconnect();
}
