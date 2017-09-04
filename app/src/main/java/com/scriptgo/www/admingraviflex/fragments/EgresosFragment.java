package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.MainActivity;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.adapters.SpinnerObraAdapter;
import com.scriptgo.www.admingraviflex.bases.BaseFragments;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EgresosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EgresosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EgresosFragment extends BaseFragments {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    /* UI */
//    View view;
    Spinner spinner = null;

    /* VARS */
//    int iduser = getIdUser();

    // RESPONSE
    Call<ObrasResponse> obraservicegetlistactive_id_name = null;

//    REALM
//    Realm realm = null;
//    RealmAsyncTask realmAsyncTask = null;
//    RealmChangeListener realmChangeListenerObras = null;
//    RealmList<Obra> obrasList = null;

    // ADAPTER
    private SpinnerObraAdapter recyclerObraAdapter;

    //RESPPONSE
    ObrasResponse obraResponse = null;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EgresosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EgresosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EgresosFragment newInstance(String param1, String param2) {
        EgresosFragment fragment = new EgresosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        realm = Realm.getDefaultInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_egresos, container, false);
        spinner = (Spinner)view.findViewById(R.id.spn_obras);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.obras_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /* METHOD */
//    void processGetObras(){
//
//        obraservicegetlistactive_id_name = ApiAdapter.getApiService().processGetAllObraActive_ID_NAME(iduser);
//        obraservicegetlistactive_id_name.enqueue(new Callback<ObrasResponse>() {
//            @Override
//            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
//                if (response.isSuccessful()) {
//                    obraResponse = response.body();
//                    if (obraResponse.error == 1) {
//
//                    }else{
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ObrasResponse> call, Throwable t) {
//
//            }
//        });
//    }

//    private Integer getIdUser() {
//        Usuario usuario = realm.where(Usuario.class).findFirst();
//        return iduser = usuario.id;
//    }

    // METHOD PUBLICS
    public void openDialogEgresos(MainActivity context, MaterialDialog materialDialog){
        materialDialog = new MaterialDialog.Builder(context).autoDismiss(false)
                .title("Nuevo Egreso")
                .customView(R.layout.dialog_egresos, true)
                .positiveText("Crear")
                .negativeText("Salir")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .build();
        materialDialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
