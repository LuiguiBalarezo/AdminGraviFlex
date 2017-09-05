package com.scriptgo.www.admingraviflex.interfaces;

import com.scriptgo.www.admingraviflex.models.Obra;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public interface CallBackProcessObraApi {
    void success(RealmList<Obra> obraAPI);
    void fail();
}
