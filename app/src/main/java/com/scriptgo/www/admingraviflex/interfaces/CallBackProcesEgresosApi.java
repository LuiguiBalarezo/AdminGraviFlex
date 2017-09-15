package com.scriptgo.www.admingraviflex.interfaces;

import com.scriptgo.www.admingraviflex.models.Egreso;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public interface CallBackProcesEgresosApi {
    void connect(RealmList<Egreso> obraAPI);
    void disconnect();
}
