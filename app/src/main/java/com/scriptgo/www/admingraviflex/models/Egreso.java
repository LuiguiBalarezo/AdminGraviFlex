package com.scriptgo.www.admingraviflex.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class Egreso extends RealmObject {

    public int id;
    @PrimaryKey
    public int idlocal;
    public int idwork;
    public Date date;
    public int number;
    public float amount;
    public String image;
    public Date createdAt;
    public Date updatedAt;
    public Date createdAtLocalDB;
    public Date updatedAtLocalDB;
    public int sync;
    public int status;
    public int iduser;
    public Egreso() {
    }
}
