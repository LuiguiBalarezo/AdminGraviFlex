package com.scriptgo.www.admingraviflex.bases;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.scriptgo.www.admingraviflex.models.Usuario;

import io.realm.Realm;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public class BaseFragments extends Fragment {

    /* UI */
    protected View view;

    /* REALM */
    protected Realm realm = null;

    /* VARS */
    protected  String TAG = this.getClass().getSimpleName();
    protected int iduser = 0;

    /* MODELS */
    protected Usuario m_usuario= null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        iduser = getIdUser();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /* METHODS */
    private int getIdUser() {
        m_usuario = realm.where(Usuario.class).findFirst();
        return iduser = m_usuario.id;
    }

    protected void initUI(){

    }
}
