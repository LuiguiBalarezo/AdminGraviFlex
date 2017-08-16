package com.scriptgo.www.admingraviflex.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class Obra extends RealmObject {

    public String id;
    @PrimaryKey
    public int idlocal;
    public String nombre;
    public Date createdAt;
    public Date updatedAt;
    public Date createdAtLocalDB;
    public Date updatedAtLocalDB;
    public Integer sync;
    public Obra() {
    }
}
