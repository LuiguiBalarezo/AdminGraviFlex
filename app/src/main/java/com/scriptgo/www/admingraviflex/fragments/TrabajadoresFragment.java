package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.bases.BaseFragments;

public class TrabajadoresFragment extends BaseFragments {

    /* UI */
    EditText edt_nombre,
            edt_apellidopaterno,
            edt_apellidomaterno,
            edt_dni, edt_sueldo;

    public TrabajadoresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /* METHODS */
    public void initOpenDialogAdd() {
        openDialogAdd("Nuevo Trabajador", R.layout.dialog_trabajadores, "Crear", "Salir", singleButtonCallback, singleButtonCallback);
    }

    /* METHOD DIALOG*/

    @Override
    protected void positiveadd(MaterialDialog dialog) {
        super.positiveadd(dialog);
        edt_nombre = (EditText) dialog.findViewById(R.id.edt_nombre);
        edt_apellidopaterno = (EditText) dialog.findViewById(R.id.edt_apellidopaterno);
        edt_apellidomaterno = (EditText) dialog.findViewById(R.id.edt_apellidomaterno);
        edt_dni = (EditText) dialog.findViewById(R.id.edt_dni);
        edt_sueldo = (EditText) dialog.findViewById(R.id.edt_sueldo);
        dismissDialogAdd();
    }

    @Override
    protected void negativeadd() {
        super.negativeadd();
        dismissDialogAdd();
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    /* API SERVICE*/

}
