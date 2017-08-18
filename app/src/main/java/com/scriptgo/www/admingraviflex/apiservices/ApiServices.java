package com.scriptgo.www.admingraviflex.apiservices;

import com.scriptgo.www.admingraviflex.constans.ConstansHelps;
import com.scriptgo.www.admingraviflex.responses.LoginResponse;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import java.util.Date;

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
    Call<ObrasResponse> processAddObra(@Field("edt_nombre") String namework);

    @FormUrlEncoded
    @POST("work/sync")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processSyncObra(@Field("edt_id") String id,
                                        @Field("edt_idlocal") int idlocal,
                                        @Field("edt_nombre") String namework,
                                        @Field("createdAtLocalDB") Date datecreatelocal,
                                        @Field("iduser") String iduser);

}
