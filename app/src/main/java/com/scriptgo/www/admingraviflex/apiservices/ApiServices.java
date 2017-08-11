package com.scriptgo.www.admingraviflex.apiservices;

import com.scriptgo.www.admingraviflex.constans.ConstansHelps;
import com.scriptgo.www.admingraviflex.responses.LoginResponse;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by BALAREZO on 10/08/2017.
 */

public interface ApiServices {

    /*
   * LOGIN
   * */
    @FormUrlEncoded
    @POST("signin")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<LoginResponse> processLogin(@Field("edt_usuario") String usuario, @Field("edt_clave") String clave);

//    @FormUrlEncoded
//    @POST("logout")
//    @Headers(ConstantsHelp.ACCESS_TOKEN)
//    Call<LoginResponse> processLogout(@Field("edt_id") String id);
//
//
//    /*
//    * USUARIOS
//    * */
//    @GET("users")
//    @Headers(ConstantsHelp.ACCESS_TOKEN)
//    Call<UsuariosResponse> getAllUsuarios();
//
//
//
    /*
    *  OBRAS
    **/
    @GET("works")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processGetAllObra();

    @FormUrlEncoded
    @POST("work/add")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processAddObra(@Field("edt_nombre_obra") String namework);

}
