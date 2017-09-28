package com.scriptgo.www.admingraviflex.services;

import android.util.Log;

import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.bases.BaseServiceAPI;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessObraApi;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BALAREZO on 04/09/2017.
 */

public class ObraServiceAPI extends BaseServiceAPI {

    public ObraServiceAPI(int _iduser) {
        iduser = _iduser;
    }

    public void getAllActive(final CallBackProcessObraApi callback) {
        callBackProcessObraApi = callback;
        s_getall_obra = ApiAdapter.getApiService().processGetAllObra(iduser);
        s_getall_obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                if (response.isSuccessful()) {
                    r_obras = response.body();
                    if (r_obras.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessObraApi.connect(r_obras.obra);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }
            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                callBackProcessObraApi.disconnect();
            }
        });
    }

    public void getallactive_id_name(final CallBackProcessObraApi callback) {
        callBackProcessObraApi = callback;
        s_getall_obra_active_id_name = ApiAdapter.getApiService().processGetAllObraActive_ID_NAME(iduser);
        s_getall_obra_active_id_name.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                if (response.isSuccessful()) {
                    r_obras = response.body();
                    if (r_obras.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessObraApi.connect(r_obras.obra);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                callBackProcessObraApi.disconnect();
            }
        });
    }

    public void create(int idserver, int idlocal, String nombre, Date datecreate, final CallBackProcessObraApi callback) {
        callBackProcessObraApi = callback;
        s_create_obra = ApiAdapter.getApiService().processCreateObra(idserver, idlocal, nombre, datecreate, iduser);
        s_create_obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                if (response.isSuccessful()) {
                    r_obras = response.body();
                    if (r_obras.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessObraApi.connect(r_obras.obra);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                callBackProcessObraApi.disconnect();
            }
        });
    }

    public void sync(int id, int idlocal, String nombre, Date createloacl, final CallBackProcessObraApi callback) {
        callBackProcessObraApi = callback;
        s_sync_obra = ApiAdapter.getApiService().processSyncObra(id, idlocal, nombre, createloacl, null, iduser);
        s_sync_obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                if (response.isSuccessful()) {
                    r_obras = response.body();
                    if (r_obras.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessObraApi.connect(r_obras.obra);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                callBackProcessObraApi.disconnect();
            }
        });
    }

    @Override
    public void cancelServices() {
        super.cancelServices();
        Log.d(TAG, "all services cancel");
        if (s_getall_obra != null) { s_getall_obra.cancel(); }
        if (s_getall_obra_active_id_name != null) { s_getall_obra_active_id_name.cancel(); }
        if (s_create_obra != null) { s_create_obra.cancel(); }
        if (s_sync_obra != null) { s_sync_obra.cancel(); }
    }

}
