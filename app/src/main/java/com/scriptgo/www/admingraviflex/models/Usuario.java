package com.scriptgo.www.admingraviflex.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class Usuario extends RealmObject {
    @PrimaryKey
    public int id;
    public String name;
    public String firstlastname;
    public String secondlastname;
    public String user;
    public int gear;
    public String password;
    public String email;
    public String createdAt;
    public String updatedAt;
    public int status;
    public int type;

    public Usuario() {
    }
}
