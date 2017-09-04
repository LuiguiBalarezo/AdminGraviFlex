package com.scriptgo.www.admingraviflex.responses;

import com.google.gson.annotations.SerializedName;
import com.scriptgo.www.admingraviflex.models.Usuario;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public class LoginResponse {
    /*@SerializedName: Si el dato retornado desde el api tiene un nombre distinto a nusetra variables, podemos zerializar
    * @data: es el Objeto retornado del api, y sera entregado al objeto Usuario*/
    @SerializedName(value="data")
    public Usuario usuario;
    public int error;
    public String message;
}
