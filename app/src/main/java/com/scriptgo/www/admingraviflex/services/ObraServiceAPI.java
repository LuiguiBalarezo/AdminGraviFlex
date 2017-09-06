package com.scriptgo.www.admingraviflex.services;

import android.util.Log;

import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessObraApi;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BALAREZO on 04/09/2017.
 */

public class ObraServiceAPI {

    private int iduser = 0;

    /* VARS */
    private String TAG = this.getClass().getSimpleName();

    /* SERVICES */
    private Call<ObrasResponse> s_getallactive = null, s_create = null, s_sync = null;
    /* CALLBACK */
    private CallBackProcessObraApi callBackProcessObraApi = null;
    /* MODELS */
    protected Usuario m_usuario = null;
    /* RESPONSE */
    protected ObrasResponse r_obras = null;

    public ObraServiceAPI(int _iduser) {
        iduser = _iduser;
    }

    public void getAllActive(final CallBackProcessObraApi callback) {
        callBackProcessObraApi = callback;
        s_getallactive = ApiAdapter.getApiService().processGetAllObra(iduser);
        s_getallactive.enqueue(new Callback<ObrasResponse>() {
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
        s_create = ApiAdapter.getApiService().processCreateObra(idserver, idlocal, nombre, datecreate, iduser);
        s_create.enqueue(new Callback<ObrasResponse>() {
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
        s_sync = ApiAdapter.getApiService().processSyncObra(id, idlocal, nombre, createloacl, null, iduser);
        s_sync.enqueue(new Callback<ObrasResponse>() {
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

    public void cancelServices() {
        if (s_getallactive != null) {
            s_getallactive.cancel();
        }
    }


}
