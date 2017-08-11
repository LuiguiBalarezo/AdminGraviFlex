package com.scriptgo.www.admingraviflex.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class Obra extends RealmObject {
    @PrimaryKey
    public String id;
    public String nombre;
    public String createdAt;
    public String updatedAt;
    public Obra() {
    }

}
