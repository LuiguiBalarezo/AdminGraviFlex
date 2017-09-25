package com.scriptgo.www.admingraviflex.services;

import android.util.Log;

import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessDineroApi;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessIngresosApi;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.DineroResponse;
import com.scriptgo.www.admingraviflex.responses.IngresoResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BALAREZO on 04/09/2017.
 */

public class IngresoServiceAPI {

    private int iduser = 0;

    /* VARS */
    private String TAG = this.getClass().getSimpleName();

    /* SERVICES */
    private Call<IngresoResponse> s_getallactive = null, s_create = null, s_sync = null, s_getallactive_id_name = null;
    private Call<DineroResponse> s_getallmoney = null;
    /* CALLBACK */
    private CallBackProcessIngresosApi callBackProcessIngresosApi = null;
    private CallBackProcessDineroApi callBackProcessDineroApi = null;
    /* MODELS */
    protected Usuario m_usuario = null;
    /* RESPONSE */
    protected IngresoResponse r_ingresos = null;
    protected DineroResponse r_dineros = null;

    public IngresoServiceAPI(int _iduser) {
        iduser = _iduser;
    }

    public void getAllIngresoByObra(int idobra, final CallBackProcessIngresosApi callback) {
        callBackProcessIngresosApi = callback;
        s_getallactive = ApiAdapter.getApiService().processGetAllIngresoByObra(iduser, idobra);
        s_getallactive.enqueue(new Callback<IngresoResponse>() {
            @Override
            public void onResponse(Call<IngresoResponse> call, Response<IngresoResponse> response) {
                if (response.isSuccessful()) {
                    r_ingresos = response.body();
                    if (r_ingresos.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessIngresosApi.connect(r_ingresos.ingreso);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }
            @Override
            public void onFailure(Call<IngresoResponse> call, Throwable t) {
                callBackProcessIngresosApi.disconnect();
            }
        });
    }

    public void create(int idserver, int idlocal, int idobra, Date fecha, int serie, float monto, String image, Date datecreate, final CallBackProcessIngresosApi callback) {
        callBackProcessIngresosApi = callback;
        s_create = ApiAdapter.getApiService().processCreateIngreso(idserver, idlocal, idobra,fecha , serie, monto , image, datecreate, iduser);
        s_create.enqueue(new Callback<IngresoResponse>() {
            @Override
            public void onResponse(Call<IngresoResponse> call, Response<IngresoResponse> response) {
                if (response.isSuccessful()) {
                    r_ingresos = response.body();
                    if (r_ingresos.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessIngresosApi.connect(r_ingresos.ingreso);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }

            @Override
            public void onFailure(Call<IngresoResponse> call, Throwable t) {
                callBackProcessIngresosApi.disconnect();
            }
        });
    }

    public void getAllMoney(int idobra, final CallBackProcessDineroApi callback) {
        callBackProcessDineroApi = callback;
        s_getallmoney = ApiAdapter.getApiService().processGetAllDineroIngresoByObra(iduser,idobra);
        s_getallmoney.enqueue(new Callback<DineroResponse>() {
            @Override
            public void onResponse(Call<DineroResponse> call, Response<DineroResponse> response) {
                if (response.isSuccessful()) {
                    r_dineros = response.body();
                    if (r_dineros.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcessDineroApi.connect(r_dineros.dineros);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES DINERO EGRESO");
                }
            }

            @Override
            public void onFailure(Call<DineroResponse> call, Throwable t) {
                callBackProcessDineroApi.disconnect();
            }
        });
    }

    public void cancelServices() {
        if (s_getallactive != null) {
            s_getallactive.cancel();
        }
    }


}
