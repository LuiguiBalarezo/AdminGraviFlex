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
import retrofit2.http.Path;

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
    @GET("works/{edt_iduser}/")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processGetAllObra(@Path("edt_iduser") int iduser);

    @GET("works/{edt_iduser}/getall_id_name")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processGetAllObraActive_ID_NAME(@Path("edt_iduser") int iduser);

    @FormUrlEncoded
    @POST("work/create")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processCreateObra(@Field("edt_id") Integer id,
                                       @Field("edt_idlocal") Integer idlocal,
                                       @Field("edt_nombre") String namework,
                                       @Field("createdAtLocalDB") Date datecreatelocal,
                                       @Field("edt_iduser") Integer iduser);

    @FormUrlEncoded
    @POST("work/update")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processUpdateObra(@Field("edt_id") Integer id,
                                       @Field("edt_nombre") String namework,
                                       @Field("updatedAtLocalDB") Date dateupdatelocal,
                                       @Field("edt_iduser") Integer iduser);

    @FormUrlEncoded
    @POST("work/sync")
    @Headers(ConstansHelps.ACCESS_TOKEN)
    Call<ObrasResponse> processSyncObra(@Field("edt_id") Integer id,
                                        @Field("edt_idlocal") Integer idlocal,
                                        @Field("edt_nombre") String namework,
                                        @Field("createdAtLocalDB") Date datecreatelocal,
                                        @Field("updatedAtLocalDB") Date dateupdatelocal,
                                        @Field("edt_iduser") Integer iduser);

}
