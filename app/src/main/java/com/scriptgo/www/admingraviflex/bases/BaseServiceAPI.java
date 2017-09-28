package com.scriptgo.www.admingraviflex.bases;

import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessDineroApi;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessEgresosApi;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessObraApi;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.DineroResponse;
import com.scriptgo.www.admingraviflex.responses.EgresoResponse;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import retrofit2.Call;

/**
 * Created by BALAREZO on 27/09/2017.
 */

public class BaseServiceAPI {

    protected int iduser = 0;

    /* VARS */
    protected String TAG = this.getClass().getSimpleName();

    /* SERVICES */
    protected Call<ObrasResponse> s_getall_obra = null, s_create_obra = null, s_sync_obra = null, s_getall_obra_active_id_name = null;
    protected Call<EgresoResponse> s_getall_egreso = null, s_create_egreso = null, s_sync_egreso = null, s_getall_egreso_active_id_name = null;
    protected Call<DineroResponse> s_getall_dinero = null;

    /* CALLBACKS */
    protected CallBackProcessObraApi callBackProcessObraApi = null;
    protected CallBackProcessEgresosApi callBackProcessEgresosApi = null;
    protected CallBackProcessDineroApi callBackProcessDineroApi = null;

    /* MODELS */
    protected Usuario m_usuario = null;

    /* RESPONSES */
    protected ObrasResponse r_obras = null;
    protected EgresoResponse r_egresos = null;
    protected DineroResponse r_dineros = null;

    public void cancelServices() {

    }

}
