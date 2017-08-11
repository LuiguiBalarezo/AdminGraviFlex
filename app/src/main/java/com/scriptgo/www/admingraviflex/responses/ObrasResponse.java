package com.scriptgo.www.admingraviflex.responses;

import com.google.gson.annotations.SerializedName;
import com.scriptgo.www.admingraviflex.models.Obra;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class ObrasResponse {
    @SerializedName(value="data")
    public RealmList<Obra> obra;
    public boolean error;
    public String message;
}
