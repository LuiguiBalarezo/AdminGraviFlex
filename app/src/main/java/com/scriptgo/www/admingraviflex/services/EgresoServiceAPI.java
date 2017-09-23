package com.scriptgo.www.admingraviflex.services;

import android.util.Log;

import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessEgresosApi;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.EgresoResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BALAREZO on 04/09/2017.
 */

public class EgresoServiceAPI {

    private int iduser = 0;

    /* VARS */
    private String TAG = this.getClass().getSimpleName();

    /* SERVICES */
    private Call<EgresoResponse> s_getallactive = null, s_create = null, s_sync = null, s_getallactive_id_name = null;
    /* CALLBACK */
    private CallBackProcessEgresosApi callBackProcessEgresosApi = null;
    /* MODELS */
    protected Usuario m_usuario = null;
    /* RESPONSE */
    protected EgresoResponse r_egresos = null;

    public EgresoServiceAPI(int _iduser) {
        iduser = _iduser;
    }

    public void getAllEgresosByObra(int idobra, final CallBackProcessEgresosApi callback) {
        callBackProcessEgresosApi = callback;
        s_getallactive = ApiAdapter.getApiService().processGetAllEgresoByObra(iduser, idobra);
        s_getallactive.enqueue(new Callback<EgresoResponse>() {
            @Override
            public void onResponse(Call<EgresoResponse> call, Response<EgresoResponse> response) {
                if (response.isSuccessful()) {
                    r_egresos = response.body();
                    if (r_egresos.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessEgresosApi.connect(r_egresos.egreso);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }
            @Override
            public void onFailure(Call<EgresoResponse> call, Throwable t) {
                callBackProcessEgresosApi.disconnect();
            }
        });
    }

    public void create(int idserver, int idlocal, int idobra, Date fecha, int serie, float monto, String image, Date datecreate, final CallBackProcessEgresosApi callback) {
        callBackProcessEgresosApi = callback;
        s_create = ApiAdapter.getApiService().processCreateEgreso(idserver, idlocal, idobra,fecha , serie, monto , image, datecreate, iduser);
        s_create.enqueue(new Callback<EgresoResponse>() {
            @Override
            public void onResponse(Call<EgresoResponse> call, Response<EgresoResponse> response) {
                if (response.isSuccessful()) {
                    r_egresos = response.body();
                    if (r_egresos.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessEgresosApi.connect(r_egresos.egreso);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }

            @Override
            public void onFailure(Call<EgresoResponse> call, Throwable t) {
                callBackProcessEgresosApi.disconnect();
            }
        });
    }

    public void cancelServices() {
        if (s_getallactive != null) {
            s_getallactive.cancel();
        }
    }


}
