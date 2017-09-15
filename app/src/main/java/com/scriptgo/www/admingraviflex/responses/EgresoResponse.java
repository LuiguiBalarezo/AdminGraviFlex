package com.scriptgo.www.admingraviflex.responses;

import com.google.gson.annotations.SerializedName;
import com.scriptgo.www.admingraviflex.models.Egreso;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class EgresoResponse {
    @SerializedName(value="data")
    public RealmList<Egreso> egreso;
    public int error;
    public String message;
}
