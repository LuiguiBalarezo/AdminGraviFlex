package com.scriptgo.www.admingraviflex.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class Usuario extends RealmObject {
    @PrimaryKey
    public String id;
    public String nombre;
    public String apellidopaterno;
    public String apellidomaterno;
    public String usuario;
    public int edad;
    public String clave;
    public String correo;
    public String createdAt;
    public String updatedAt;

    public Usuario() {
    }
}
