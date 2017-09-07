package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.MainActivity;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.adapters.SpinnerObraAdapter;
import com.scriptgo.www.admingraviflex.bases.BaseFragments;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessObraApi;
import com.scriptgo.www.admingraviflex.models.Obra;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;
import com.scriptgo.www.admingraviflex.services.ObraServiceAPI;

import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;

public class EgresosFragment extends BaseFragments {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //    /* UI */
    AppCompatSpinner spinner = null;


    // RESPONSE
    Call<ObrasResponse> obraservicegetlistactive_id_name = null;


    // ADAPTER
    private SpinnerObraAdapter recyclerObraAdapter;

    //RESPPONSE
    ObrasResponse obraResponse = null;

    /* SERVICES */
    ObraServiceAPI obraServiceAPI = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

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
        initServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_egresos, container, false);
        spinner = (AppCompatSpinner) view.findViewById(R.id.spn_obras);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.obras_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onResume() {
        super.onResume();
        apigetallobras_id_name();
    }

    void apigetallobras_id_name(){
        obraServiceAPI.getAllActive(new CallBackProcessObraApi() {
            @Override
            public void connect(RealmList<Obra> obraAPI) {

                listobrasAPIempty = (obraAPI.size() == 0) ? true : false;
                final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();
                listobrasDBempty = (obrasDB.size() == 0) ? true : false;

                Toast.makeText(getActivity(), "connect", Toast.LENGTH_SHORT).show();

//                if (listobrasAPIempty && listobrasDBempty) {
//                    visibleViewContent("empty");
//                } else if (listobrasAPIempty && !listobrasDBempty) {
//                    visibleViewContent("recycler");
//                    setAddAdapter(obrasDB);
//                } else if (!listobrasAPIempty && listobrasDBempty) {
//                    visibleViewContent("recycler");
//                    saveIntDataBase(obraAPI, true);
//                } else if (!listobrasAPIempty && !listobrasDBempty) {
//                    visibleViewContent("recycler");
//                    saveIntDataBase(obraAPI, true);
//                }

            }

            @Override
            public void disconnect() {
                Toast.makeText(getActivity(), "disconnect", Toast.LENGTH_SHORT).show();
                //final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();

            }
        });
    }
    // METHOD PUBLICS
    public void openDialogEgresos(MainActivity context, MaterialDialog materialDialog) {
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

    @Override
    protected void initServices() {
        super.initServices();
        obraServiceAPI = new ObraServiceAPI(iduser);
    }

}
