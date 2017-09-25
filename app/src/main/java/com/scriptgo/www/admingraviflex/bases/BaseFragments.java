package com.scriptgo.www.admingraviflex.bases;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.compound.ProgressCircularText;
import com.scriptgo.www.admingraviflex.interfaces.ObrasFragmentToActivity;
import com.scriptgo.www.admingraviflex.models.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public class BaseFragments extends Fragment {

    /* UI */
    protected View view;
    public MaterialDialog materialDialogAdd = null,
            materialDialogEdit = null,
            materialDialogIndeterminate = null;
    protected TextView txt_vacio;
    protected ProgressCircularText progressCircularText;
    protected RecyclerView recycler_view;
    /* REALM */
    protected Realm realm = null;
    protected Menu menu = null;

    /* VARS */
    protected String TAG = this.getClass().getSimpleName();
    protected int iduser = 0;
    protected boolean listobrasAPIempty = false;
    protected boolean listobrasDBempty = false;
    protected boolean listegresosAPIempty = false;
    protected boolean listegresosDBempty = false;
    protected boolean listingresosAPIempty = false;
    protected boolean listingresosDBempty = false;
    protected int diainit, mesinit, anioinit, dia, mes, anio;

    Calendar calendar = null;
    /* MODELS */
    protected Usuario m_usuario = null;

    /* CALLBACKS */
    private ObrasFragmentToActivity activityactions;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        realm = Realm.getDefaultInstance();
        iduser = getIdUser();
        calendar = Calendar.getInstance();

        diainit = calendar.get(Calendar.DAY_OF_MONTH);
        mesinit = calendar.get(Calendar.MONTH);
        anioinit = calendar.get(Calendar.YEAR);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ObrasFragmentToActivity) {
            activityactions = (ObrasFragmentToActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityactions = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activityactions != null) {
            activityactions.dismissSnackBar();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (activityactions != null) {
            activityactions.dismissSnackBar();
        }
    }

    /* METHODS */
    private int getIdUser() {
        m_usuario = realm.where(Usuario.class).findFirst();
        return iduser = m_usuario.id;
    }

    protected Dialog openDialogDatePicker(DatePickerDialog.OnDateSetListener onDateSetListener) {
        return new DatePickerDialog(getActivity(), onDateSetListener, anioinit, mesinit, diainit);
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

    protected void showSnackbar(String msg, String type) {

    }

    protected void visibleViewContent(String viewcontent) {
        progressCircularText.setVisibility(View.GONE);
        recycler_view.setVisibility(View.GONE);
        txt_vacio.setVisibility(View.GONE);
        if (viewcontent != null) {
            switch (viewcontent) {
                case "progress":
                    progressCircularText.setVisibility(View.VISIBLE);
                    break;
                case "recycler":
                    recycler_view.setVisibility(View.VISIBLE);
                    break;
                case "empty":
                    txt_vacio.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            progressCircularText.setVisibility(View.GONE);
            recycler_view.setVisibility(View.GONE);
            txt_vacio.setVisibility(View.GONE);
        }
    }

    protected Date getDateTime() {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        return new Date(date);
    }
}
