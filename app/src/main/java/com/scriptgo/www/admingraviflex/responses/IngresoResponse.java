package com.scriptgo.www.admingraviflex.responses;

import com.google.gson.annotations.SerializedName;
import com.scriptgo.www.admingraviflex.models.Ingreso;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class IngresoResponse {
    @SerializedName(value="data")
    public RealmList<Ingreso> ingreso;
    public int error;
    public String message;
}
