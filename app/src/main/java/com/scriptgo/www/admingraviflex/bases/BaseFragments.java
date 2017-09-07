package com.scriptgo.www.admingraviflex.bases;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.models.Usuario;

import io.realm.Realm;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public class BaseFragments extends Fragment {

    /* UI */
    protected View view;
    protected MaterialDialog materialDialogAdd = null,
            materialDialogEdit = null,
            materialDialogIndeterminate = null;

    /* REALM */
    protected Realm realm = null;

    /* VARS */
    protected String TAG = this.getClass().getSimpleName();
    protected int iduser = 0;
    protected boolean listobrasAPIempty = false;
    protected boolean listobrasDBempty = false;

    /* MODELS */
    protected Usuario m_usuario = null;

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

    /* INICIALIZADOR DE VIEWS*/
    protected void initUI() {

    }

    /* INICIALIZADOR DE SERVICESAPI*/
    protected void initServices() {

    }

    /* DIALOG */
    protected void openDialogAdd(String title, int layout, String positivetext,
                              String negativetext, MaterialDialog.SingleButtonCallback positivecallback,
                              MaterialDialog.SingleButtonCallback negativecallback) {
        if (materialDialogAdd == null) {
            materialDialogAdd = new MaterialDialog.Builder(getActivity()).autoDismiss(false)
                    .title(title)
                    .customView(layout, true)
                    .positiveText(positivetext)
                    .negativeText(negativetext)
                    .onPositive(positivecallback).onNegative(negativecallback)
                    .build();
            materialDialogAdd.show();
        } else {
            materialDialogAdd.show();
        }
    }

    public void dismissDialogAdd() {
        if (materialDialogAdd != null) {
            materialDialogAdd.dismiss();
        }
    }

    public void openDialogEdit() {

    }

    public void openDialogIndeterminate(String textcontent) {
        if (materialDialogIndeterminate == null) {
            materialDialogIndeterminate = new MaterialDialog.Builder(getActivity()).autoDismiss(false)
                    .content(textcontent)
                    .cancelable(false)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true)
                    .build();
            materialDialogIndeterminate.show();
        } else {
            materialDialogIndeterminate.show();
        }
    }

    public void dismissDialogIndeterminate() {
        if (materialDialogIndeterminate != null) {
            materialDialogIndeterminate.dismiss();
        }
    }
}
