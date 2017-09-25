package com.scriptgo.www.admingraviflex.responses;

import com.google.gson.annotations.SerializedName;
import com.scriptgo.www.admingraviflex.models.Dinero;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class DineroResponse {
    @SerializedName(value="data")
    public RealmList<Dinero> dineros;
    public int error;
    public String message;
}
