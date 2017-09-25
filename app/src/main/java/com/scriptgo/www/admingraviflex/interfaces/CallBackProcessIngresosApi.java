package com.scriptgo.www.admingraviflex.interfaces;

import com.scriptgo.www.admingraviflex.models.Ingreso;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public interface CallBackProcessIngresosApi {
    void connect(RealmList<Ingreso> ingresosAPI);
    void disconnect();
}
