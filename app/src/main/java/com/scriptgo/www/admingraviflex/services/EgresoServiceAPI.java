package com.scriptgo.www.admingraviflex.services;

import android.util.Log;

import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcesEgresosApi;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.EgresoResponse;

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
    private CallBackProcesEgresosApi callBackProcesEgresosApi = null;
    /* MODELS */
    protected Usuario m_usuario = null;
    /* RESPONSE */
    protected EgresoResponse r_egresos = null;

    public EgresoServiceAPI(int _iduser) {
        iduser = _iduser;
    }

    public void getAllEgresosByObra(int idobra, final CallBackProcesEgresosApi callback) {
        callBackProcesEgresosApi = callback;
        s_getallactive = ApiAdapter.getApiService().processGetAllEgresoByObra(iduser, idobra);
        s_getallactive.enqueue(new Callback<EgresoResponse>() {
            @Override
            public void onResponse(Call<EgresoResponse> call, Response<EgresoResponse> response) {
                if (response.isSuccessful()) {
                    r_egresos = response.body();
                    if (r_egresos.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        callBackProcesEgresosApi.connect(r_egresos.egreso);
                    }
                } else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }
            @Override
            public void onFailure(Call<EgresoResponse> call, Throwable t) {
                callBackProcesEgresosApi.disconnect();
            }
        });
    }


//    public void getallactive_id_name(final CallBackProcessObraApi callback){
//        callBackProcesEgresosApi = callback;
//        s_getallactive_id_name = ApiAdapter.getApiService().processGetAllObraActive_ID_NAME(iduser);
//        s_getallactive_id_name.enqueue(new Callback<ObrasResponse>() {
//            @Override
//            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
//                if (response.isSuccessful()) {
//                    r_obras = response.body();
//                    if (r_obras.error == 1) {
//                        Log.d(TAG, "ERROR + MENSAJE");
//                    } else {
//                        callBackProcessObraApi.connect(r_obras.obra);
//                    }
//                } else {
//                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ObrasResponse> call, Throwable t) {
//                callBackProcessObraApi.disconnect();
//            }
//        });
//    }

//    public void create(int idserver, int idlocal, String nombre, Date datecreate, final CallBackProcessObraApi callback) {
//        callBackProcessObraApi = callback;
//        s_create = ApiAdapter.getApiService().processCreateObra(idserver, idlocal, nombre, datecreate, iduser);
//        s_create.enqueue(new Callback<ObrasResponse>() {
//            @Override
//            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
//                if (response.isSuccessful()) {
//                    r_obras = response.body();
//                    if (r_obras.error == 1) {
//                        Log.d(TAG, "ERROR + MENSAJE");
//                    } else {
//                        callBackProcessObraApi.connect(r_obras.obra);
//                    }
//                } else {
//                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ObrasResponse> call, Throwable t) {
//                callBackProcessObraApi.disconnect();
//            }
//        });
//    }

//    public void sync(int id, int idlocal, String nombre, Date createloacl, final CallBackProcessObraApi callback) {
//        callBackProcessObraApi = callback;
//        s_sync = ApiAdapter.getApiService().processSyncObra(id, idlocal, nombre, createloacl, null, iduser);
//        s_sync.enqueue(new Callback<ObrasResponse>() {
//            @Override
//            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
//                if (response.isSuccessful()) {
//                    r_obras = response.body();
//                    if (r_obras.error == 1) {
//                        Log.d(TAG, "ERROR + MENSAJE");
//                    } else {
//                        callBackProcessObraApi.connect(r_obras.obra);
//                    }
//                } else {
//                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
//                }
//            }
//            @Override
//            public void onFailure(Call<ObrasResponse> call, Throwable t) {
//                callBackProcessObraApi.disconnect();
//            }
//        });
//    }

    public void cancelServices() {
        if (s_getallactive != null) {
            s_getallactive.cancel();
        }
    }


}
