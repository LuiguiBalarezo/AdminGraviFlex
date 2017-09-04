package com.scriptgo.www.admingraviflex.bases;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.ProcessObraApi;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public class BaseFragments extends Fragment {

    ProcessObraApi processObraApi = null;

    /* UI */
    protected View view;

    /* REALM */
    protected Realm realm = null;

    /* VARS */
    protected  String TAG = this.getClass().getSimpleName();
    protected int iduser = 0;

    /* MODELS */
    protected Usuario m_usuario= null;

    /* RESPONSE */
    protected ObrasResponse r_obras = null;

    /* SERVICES */
    protected Call<ObrasResponse> s_obrasrecycler = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        iduser = getIdUser();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelServices();
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelServices();
    }

    /* METHODS */
    private int getIdUser() {
        m_usuario = realm.where(Usuario.class).findFirst();
        return iduser = m_usuario.id;
    }

    /* CALL API */
    protected void getServiceObrasAllActive(final ProcessObraApi callback){
        processObraApi = callback;
        s_obrasrecycler = ApiAdapter.getApiService().processGetAllObra(iduser);
        s_obrasrecycler.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                if (response.isSuccessful()) {
                    r_obras = response.body();
                    if (r_obras.error == 1) {
                        Log.d(TAG, "ERROR + MENSAJE");
                    } else {
                        processObraApi.processDataAPI(r_obras.obra);
                    }
                }else {
                    Log.d(TAG, "ERROR SERVICES OBRASRECYCLER");
                }
            }
            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                processObraApi.processDataLocal();
            }
        });
    }

    protected void createServiceObra(){

    }

    void cancelServices(){
        if(s_obrasrecycler != null){
            s_obrasrecycler.cancel();
        }
    }

}
