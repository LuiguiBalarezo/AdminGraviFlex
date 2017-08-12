package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.adapters.ObraAdapter;
import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.ObrasFragmentToActivity;
import com.scriptgo.www.admingraviflex.models.Obra;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ObrasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ObrasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObrasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ObrasFragmentToActivity interfaceObras;
    private OnFragmentInteractionListener mListener;

    // UI
    View view = null;
    //TextView txt_prueba = null;
    MaterialDialog materialDialogAddOrEdit = null, materialDialog = null;
    EditText edt_nombre_obra;

    // REALM
    // REALM
    Realm realm = null;
    RealmAsyncTask realmAsyncTask = null;


    // ADAPTER
    private RecyclerView listaobras;
    private ObraAdapter obraAdapter;

    public ObrasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ObrasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ObrasFragment newInstance(String param1, String param2) {
        ObrasFragment fragment = new ObrasFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_obras, container, false);
        listaobras = (RecyclerView)view.findViewById(R.id.recyclerview_obras);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listaobras.setLayoutManager(layoutManager);
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
            interfaceObras = (ObrasFragmentToActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        interfaceObras = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        processGetListObras();
    }

    // METHOD PUBLICS

    private void proccessAddObra(String nombreobra) {

    }

    private void processGetListObras() {

        openDialog("Sincronizando Obras");
        final Call<ObrasResponse> obra = ApiAdapter.getApiService().processGetAllObra();
        obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                // materialDialogHelp.showOrDismissIndeterninate(null, null);

                if (response.isSuccessful()) {
                    ObrasResponse obraResponse = response.body();
                    if (obraResponse.error) {
                        openDialog(null);
                    } else {
                        final RealmList<Obra> obras = obraResponse.obra;
                        Toast.makeText(getActivity(), "GET OBRAS : " + obras, Toast.LENGTH_SHORT).show();
                        openDialog(null);
                        saveIntDataBase(obras);

                    }
                } else {
                    //FALLA TRANSFORMACION DE GSON
                    openDialog(null);
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                // FALLO CONEXION CON LA API
                openDialog(null);
                openDialog("Listando desde la Base de Datos");
                interfaceObras.shoSnackBar("No se pudo sincronizar / Datos sin conexion.");
                realm = Realm.getDefaultInstance();
                RealmResults<Obra> obras = realm.where(Obra.class).findAllAsync();
                obras.addChangeListener(new RealmChangeListener<RealmResults<Obra>>() {
                    @Override
                    public void onChange(RealmResults<Obra> obras) {
                        RealmList<Obra> obrasList = new RealmList<Obra>();
                        obrasList.addAll(obras.subList(0, obras.size()));
                        loadRecyclerAdapter(obrasList);
                    }
                });
            }
        });
    }

    private void processAddObra(String nombreobra){

        openDialog("Sincronisando con la API");
        Call<ObrasResponse> obra = ApiAdapter.getApiService().processAddObra(nombreobra);
        obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                openDialog(null);
                if (response.isSuccessful()) {
                    ObrasResponse obraResponse = response.body();
                    if (obraResponse.error) {

                        openDialog(null);
                    } else {
                        final RealmList<Obra> obras = obraResponse.obra;
                        openDialog("Guardando en la BD");
                        saveIntDataBase(obras);
                    }
                } else {
                    Toast.makeText(getActivity(), "ERROR EN FORMATO DE RESPUESTA", Toast.LENGTH_SHORT).show();
                    realm.close();
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                //Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                openDialog(null);
            }
        });
    }

    private void saveIntDataBase(final RealmList<Obra> obrasapi) {
        openDialog("Guardando en Local");
        realm = Realm.getDefaultInstance();
        final RealmResults<Obra> obras = realm.where(Obra.class).findAll();
        Toast.makeText(getActivity(), "Total de obras en DB : " + obras.size(), Toast.LENGTH_SHORT).show();
        realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(obrasapi);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "Obras almacenadas en DB : " + obras.size(), Toast.LENGTH_SHORT).show();
                openDialog(null);
                openDialog("Cargando Listado");
                loadRecyclerAdapter(obrasapi);
                realm.close();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getActivity(), "Error al almacenar Obras", Toast.LENGTH_SHORT).show();
                openDialog(null);
                realm.close();
            }
        });
    }

    private void loadRecyclerAdapter(final RealmList<Obra> obras) {
        obraAdapter = new ObraAdapter(obras);
        listaobras.setAdapter(obraAdapter);
        obraAdapter.notifyDataSetChanged();
        openDialog(null);
    }

    public void openDialogAddOrEdit() {
        if (materialDialogAddOrEdit == null) {
            materialDialogAddOrEdit = new MaterialDialog.Builder(getActivity()).autoDismiss(false)
                    .title("Nueva Obra")
                    .customView(R.layout.dialog_obra, true)
                    .positiveText("Crear")
                    .negativeText("Salir")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            edt_nombre_obra = (EditText) dialog.findViewById(R.id.edt_nombre_obra);
                            Toast.makeText(getActivity(), "" + edt_nombre_obra.getText().toString(), Toast.LENGTH_SHORT).show();
                            processAddObra(edt_nombre_obra.getText().toString());
                            materialDialogAddOrEdit.dismiss();

                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            materialDialogAddOrEdit.dismiss();
                        }
                    })
                    .build();
            materialDialogAddOrEdit.show();
        } else {
            materialDialogAddOrEdit.show();
        }
    }

    public void openDialog(String msg) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(getActivity())
                    .autoDismiss(false)
                    .cancelable(false)
                    .title(null)
                    .content(msg)
                    .progress(true, 0)
                    .progressIndeterminateStyle(true).build();
            materialDialog.show();
        } else {
            materialDialog.dismiss();
            materialDialog = null;
        }
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
