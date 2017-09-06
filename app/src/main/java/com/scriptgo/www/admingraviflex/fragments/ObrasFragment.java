package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.scriptgo.www.admingraviflex.adapters.RecyclerObraAdapter;
import com.scriptgo.www.admingraviflex.bases.BaseFragments;
import com.scriptgo.www.admingraviflex.compound.ProgressCircularText;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessObraApi;
import com.scriptgo.www.admingraviflex.interfaces.ObrasClickRecyclerView;
import com.scriptgo.www.admingraviflex.interfaces.ObrasFragmentToActivity;
import com.scriptgo.www.admingraviflex.models.Obra;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;
import com.scriptgo.www.admingraviflex.services.ObraServiceAPI;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;


public class ObrasFragment extends BaseFragments {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ObrasFragmentToActivity interfaceObras;

    // RESPONSE
    Call<ObrasResponse> obraservicecreate = null;

    // VARS
    boolean listobrasAPIempty = false;
    boolean listobrasDBempty = false;
    String TAG = this.getClass().getSimpleName();

    // UI
    MaterialDialog materialDialogAdd = null,
            materialDialogEdit = null,
            materialDialog = null,
            materialDialogIndeterminate = null;
    EditText edt_nombre_obra;
    RecyclerView recycler_obras;
    TextView txt_vacio;
    ProgressCircularText progressCircularText;


    // REALM
    RealmAsyncTask realmAsyncTask = null;
    RealmChangeListener realmChangeListenerObras = null;
    RealmList<Obra> obrasList = null;
    // ADAPTER
    private RecyclerObraAdapter recyclerObraAdapter;

    /* SERVICES */
    ObraServiceAPI obraServiceAPI = null;

    public ObrasFragment() {

    }

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        initServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_obras, container, false);
        initUI();
        return view;
    }

    void visibleViewContent(String viewcontent) {
        progressCircularText.setVisibility(View.GONE);
        recycler_obras.setVisibility(View.GONE);
        txt_vacio.setVisibility(View.GONE);
        if (viewcontent != null) {
            switch (viewcontent) {
                case "progress":
                    progressCircularText.setVisibility(View.VISIBLE);
                    break;
                case "recycler":
                    recycler_obras.setVisibility(View.VISIBLE);
                    break;
                case "empty":
                    txt_vacio.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            progressCircularText.setVisibility(View.GONE);
            recycler_obras.setVisibility(View.GONE);
            txt_vacio.setVisibility(View.GONE);
        }
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
        Log.d(TAG, "onResume");
        super.onResume();

        apigetallobras();
        interfaceObras.dismissSnackBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        interfaceObras.dismissSnackBar();
        obraServiceAPI.cancelServices();

        if (obraservicecreate != null) {
            obraservicecreate.cancel();
        }
        dismissDialogIndeterminate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        obraServiceAPI.cancelServices();

        if (obraservicecreate != null) {
            obraservicecreate.cancel();
        }

        dismissDialogIndeterminate();
    }

    /* METHOD */
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
        recyclerObraAdapter = new RecyclerObraAdapter(getActivity(), obrasList, new ObrasClickRecyclerView() {
            @Override
            public void onClickSync(View view, int position) {
                int id = obras.get(position).id;
                int idlocal = obras.get(position).idlocal;
                String nombre = obras.get(position).name;
                Date datecreatelocal = obras.get(position).createdAtLocalDB;
                Toast.makeText(getActivity(), id + " " + idlocal + " " + nombre, Toast.LENGTH_SHORT).show();
                apisync(id, idlocal, nombre, datecreatelocal);
            }

            @Override
            public void onClickViewDetail(View view, int position) {
                Toast.makeText(view.getContext(), "Mostrar Detalle ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickOptions(View view, int position) {
                Toast.makeText(view.getContext(), "Editar", Toast.LENGTH_SHORT).show();
            }
        });
        recycler_obras.setAdapter(recyclerObraAdapter);
        recyclerObraAdapter.notifyDataSetChanged();
    }


    void apigetallobras() {

        visibleViewContent("progress");
        progressCircularText.setTextLoad("Cargando...");

        obraServiceAPI.getAllActive(new CallBackProcessObraApi() {
            @Override
            public void connect(RealmList<Obra> obraAPI) {

                listobrasAPIempty = (obraAPI.size() == 0) ? true : false;
                final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();
                listobrasDBempty = (obrasDB.size() == 0) ? true : false;

                if (listobrasAPIempty && listobrasDBempty) {
                    visibleViewContent("empty");
                } else if (listobrasAPIempty && !listobrasDBempty) {
                    visibleViewContent("recycler");
                    setAddAdapter(obrasDB);
                } else if (!listobrasAPIempty && listobrasDBempty) {
                    visibleViewContent("recycler");
                    saveIntDataBase(obraAPI, true);
                } else if (!listobrasAPIempty && !listobrasDBempty) {
                    visibleViewContent("recycler");
                    saveIntDataBase(obraAPI, true);
                }

            }

            @Override
            public void disconnect() {
                interfaceObras.showSnackBar("Sin Conexion", "info");
                final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();
                if (obrasDB.size() == 0) {
                    visibleViewContent("empty");
                } else {
                    visibleViewContent("recycler");
                    setAddAdapter(obrasDB);
                }
            }
        });
    }

    void apicreateaobra(final String nombre) {
        int id = 0;
        int idlocal = getMaxIdObra();
        Date createdAtLocalDB = getDateTime();
        obraServiceAPI.create(id, idlocal, nombre, createdAtLocalDB, new CallBackProcessObraApi() {
            @Override
            public void connect(RealmList<Obra> obraAPI) {
                final RealmList<Obra> obras = obraAPI;
                saveIntDataBase(obras, false);
                interfaceObras.showSnackBar("Sincronizado", "success");
            }

            @Override
            public void disconnect() {
                visibleViewContent("progress");
                dismissDialogIndeterminate();
                interfaceObras.showSnackBar("Sin Conexion / Guardado en Local", "info");

                final Obra obra = new Obra();
                obra.idlocal = getMaxIdObra();
                obra.name = nombre;
                obra.createdAt = null;
                obra.updatedAt = null;
                obra.createdAtLocalDB = getDateTime();
                obra.status = 1;
                obra.sync = 0;
                obra.iduser = iduser;

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(obra);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        visibleViewContent("recycler");
                        checkObras();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        visibleViewContent(null);
                        interfaceObras.showSnackBar("Realm : erronea al Agregar Obra!", "error");
                    }
                });
            }
        });
    }

    void apisync(int id, int idlocal, String nombre, Date datecreatelocal) {
        openDialogIndeterminate("Sincronizando");
        obraServiceAPI.sync(id, idlocal, nombre, datecreatelocal, new CallBackProcessObraApi() {
            @Override
            public void connect(RealmList<Obra> obraAPI) {
                dismissDialogIndeterminate();
                saveIntDataBase(obraAPI, false);

                interfaceObras.showSnackBar("Sincronizado", "success");
            }
            @Override
            public void disconnect() {
                dismissDialogIndeterminate();
                visibleViewContent("recycler");
                interfaceObras.showSnackBar("Sin Conexion / No sincronizado", "info");
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

    private void saveIntDataBase(final RealmList<Obra> obraslist, boolean deleteallsync) {


        int nextId = getMaxIdObra();
        int countobrapi = obraslist.size();

        if (deleteallsync) {
            final RealmResults<Obra> obrasstatusresult = realm.where(Obra.class)
                    .equalTo("sync", 1)
                    .findAll();
            realm.beginTransaction();
            obrasstatusresult.deleteAllFromRealm();
            realm.commitTransaction();
        }


        for (int i = 0; i < countobrapi; i++) {

            int id = obraslist.get(i).id;
            int idlocal = obraslist.get(i).idlocal;
            int status = obraslist.get(i).status;
            String name = obraslist.get(i).name;
            Date createdAt = obraslist.get(i).createdAt;
            Date updatedAt = obraslist.get(i).updatedAt;
            int sync = obraslist.get(i).sync;
            int iduser = obraslist.get(i).iduser;

            if (id == 0) {
                obraslist.get(i).idlocal = nextId;
                nextId++;
            } else if (idlocal == 0) {
                obraslist.get(i).idlocal = nextId;
                nextId++;
            }

        }

        realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(obraslist);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Toast.makeText(getActivity(), "saveIntDataBase onSuccess", Toast.LENGTH_SHORT).show();
                checkObras();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
//                Toast.makeText(getActivity(), "saveIntDataBase onError", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initOpenDialogAdd() {
        openDialogAdd("Nueva Obra", R.layout.dialog_obra, "Crear", "Salir", singleButtonCallback, singleButtonCallback);
    }


    MaterialDialog.SingleButtonCallback singleButtonCallback = new MaterialDialog.SingleButtonCallback() {
        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            switch (which.name()) {
                case "POSITIVE":
                    positiveadd(dialog);
                    break;
                case "NEGATIVE":
                    negativeadd();
                    break;
            }
        }
    };

    void positiveadd(MaterialDialog dialog) {
        edt_nombre_obra = (EditText) dialog.findViewById(R.id.edt_nombre_obra);
        String nombreobra = edt_nombre_obra.getText().toString();
        edt_nombre_obra.setText(null);
        apicreateaobra(nombreobra);
        dismissDialogAdd();
    }

    void negativeadd() {
        dismissDialogAdd();
    }

    @Override
    protected void initUI() {
        super.initUI();
        progressCircularText = (ProgressCircularText) view.findViewById(R.id.pgrs_obra);
        recycler_obras = (RecyclerView) view.findViewById(R.id.recyclerview_obras);
        txt_vacio = (TextView) view.findViewById(R.id.txt_vacio);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recycler_obras.setLayoutManager(layoutManager);
        visibleViewContent(null);
    }

    @Override
    protected void initServices() {
        super.initServices();
        obraServiceAPI = new ObraServiceAPI(iduser);
    }

}
