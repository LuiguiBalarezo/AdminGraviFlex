package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.adapters.ObraAdapter;
import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.ObrasClickRecyclerView;
import com.scriptgo.www.admingraviflex.interfaces.ObrasFragmentToActivity;
import com.scriptgo.www.admingraviflex.models.Obra;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ObrasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ObrasFragmentToActivity interfaceObras;

    // VARS
    boolean listobrasAPIempty = false;
    boolean listobrasDBempty = false;
    private String iduser = "";
    // UI
    View view = null;
    //TextView txt_prueba = null;
    MaterialDialog materialDialogAddOrEdit = null, materialDialog = null;
    EditText edt_nombre_obra;
    RecyclerView recycler_obras;
    TextView txt_vacio;

    // REALM
    // REALM
    Realm realm = null;
    RealmAsyncTask realmAsyncTask = null;
    RealmChangeListener realmChangeListenerObras = null;
    RealmList<Obra> obrasList = null;
    // ADAPTER
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

        realm = Realm.getDefaultInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_obras, container, false);
        recycler_obras = (RecyclerView) view.findViewById(R.id.recyclerview_obras);
        txt_vacio = (TextView) view.findViewById(R.id.txt_vacio);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_obras.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ObrasFragmentToActivity) {
            interfaceObras = (ObrasFragmentToActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interfaceObras = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        interfaceObras.dismissSnackBar();
        processGetListObras();
    }

    // METHOD
    private void processGetListObras() {
        dismissDialog();
        openDialog("Extrayendo Obras del A.P.I");
        final Call<ObrasResponse> obra = ApiAdapter.getApiService().processGetAllObra();
        obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                dismissDialog();
                openDialog("Procesando resultados del A.P.I");
                if (response.isSuccessful()) {
                    ObrasResponse obraResponse = response.body();
                    if (obraResponse.error) {

                        dismissDialog();
                        interfaceObras.showSnackBar("Error en la peticion A.P.I, " + obraResponse.message);

                    } else {

//                        realm = Realm.getDefaultInstance();
                        final RealmList<Obra> obrasAPI = obraResponse.obra;
                        final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();

                        listobrasAPIempty = (obrasAPI.size() == 0) ? true : false;
                        listobrasDBempty = (obrasDB.size() == 0) ? true : false;

                        if (listobrasAPIempty) {
                            if (listobrasDBempty) {
                                showTextEmptyHiddeRecycler();
                            } else {
                                showRecyclerHiddeTextEmpty();
                                setAddAdapter(obrasDB);
                            }
                        } else {
                            showRecyclerHiddeTextEmpty();
                            saveIntDataBase(obrasAPI);
                        }

                    }
                } else {
                    interfaceObras.showSnackBar("Error : Falló la transformación a GSON de Retrofit");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                interfaceObras.showSnackBar("Sin Conexion con el A.P.I / Obras de DB Local");
                dismissDialog();
                openDialog("Extrayendo obras de BD Local");

//                realm = Realm.getDefaultInstance();
                final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();

                if (obrasDB.size() == 0) {
                    showTextEmptyHiddeRecycler();
                } else {
                    showRecyclerHiddeTextEmpty();
                    setAddAdapter(obrasDB);
                }

            }
        });
    }

    private void checkObras() {
        RealmResults<Obra> obrasdb = realm.where(Obra.class).findAllAsync();
        obrasdb.addChangeListener(new RealmChangeListener<RealmResults<Obra>>() {
            @Override
            public void onChange(RealmResults<Obra> obras) {
                if (obras.size() == 0) {
                    showTextEmptyHiddeRecycler();
                } else {
                    showRecyclerHiddeTextEmpty();
                    setAddAdapter(obras);
                }
            }
        });
    }

    private void showRecyclerHiddeTextEmpty() {
        recycler_obras.setVisibility(View.VISIBLE);
        txt_vacio.setVisibility(View.GONE);
    }

    private void showTextEmptyHiddeRecycler() {
        recycler_obras.setVisibility(View.GONE);
        txt_vacio.setVisibility(View.VISIBLE);
    }

    private void setAddAdapter(final RealmResults<Obra> obras) {
        obrasList = new RealmList<Obra>();
        obrasList.addAll(obras.subList(0, obras.size()));
        obraAdapter = new ObraAdapter(getActivity(), obrasList, new ObrasClickRecyclerView() {
            @Override
            public void onClickSync(View view, int position) {
                Toast.makeText(getActivity(), "hola " + obrasList.get(position).nombre    , Toast.LENGTH_SHORT).show();
            }
        });
        recycler_obras.setAdapter(obraAdapter);
        obraAdapter.notifyDataSetChanged();
    }

    private void processAddObra(final String nombreobra) {
        dismissDialog();
        openDialog("Enviando Obra al A.P.I");
        Usuario usuario = realm.where(Usuario.class).findFirst();
        iduser = usuario.id;
        Call<ObrasResponse> obra = ApiAdapter.getApiService().processAddObra(nombreobra);
        obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                if (response.isSuccessful()) {
                    ObrasResponse obraResponse = response.body();
                    if (obraResponse.error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                    } else {
                        final RealmList<Obra> obras = obraResponse.obra;
                        saveIntDataBase(obras);
                    }
                } else {
//                    realm.close();
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {

                interfaceObras.showSnackBar("Sin Conexion con el A.P.I / Guardado en Local");

                final Obra obra = new Obra();
                obra.idlocal = getMaxIdObra();
                obra.nombre = nombreobra;
                obra.createdAt = null;
                obra.updatedAt = null;
                obra.createdAtLocalDB = getDateTime();
                obra.iduser = iduser;

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(obra);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        checkObras();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getActivity(), "processAddObra onError", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private int getMaxIdObra() {
        Number currentIdNum = realm.where(Obra.class).max("idlocal");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    private Date getDateTime() {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        return new Date(date);
    }

    private void saveIntDataBase(final RealmList<Obra> obrasapi) {
        final RealmResults<Obra> obras = realm.where(Obra.class).findAll();

        int nextId = getMaxIdObra();

        for (int i = 0; i < obrasapi.size(); i++) {
            if (obrasapi.get(i).sync == 0) {
                obrasapi.get(i).idlocal = nextId;
                nextId++;
            }
        }

        Log.d("OBRAS API ", "TERMINO");

        realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(obrasapi);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "saveIntDataBase onSuccess", Toast.LENGTH_SHORT).show();
                checkObras();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getActivity(), "saveIntDataBase onError", Toast.LENGTH_SHORT).show();
            }
        });

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
                            final String nombreobra = edt_nombre_obra.getText().toString();
                            edt_nombre_obra.setText("");
                            materialDialogAddOrEdit.dismiss();
                            processAddObra(nombreobra);
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
        }
    }

    public void dismissDialog() {
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
    }

}
