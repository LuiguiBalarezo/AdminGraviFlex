package com.scriptgo.www.admingraviflex.responses;

import com.google.gson.annotations.SerializedName;
import com.scriptgo.www.admingraviflex.models.Trabajador;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class TrabajadoresResponse {
    @SerializedName(value="data")
    public RealmList<Trabajador> dineros;
    public int error;
    public String message;
}
