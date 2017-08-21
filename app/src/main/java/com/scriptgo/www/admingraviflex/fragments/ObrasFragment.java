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
    private Integer iduser = 0;
    String TAG = this.getClass().getSimpleName();
    // UI
    View view = null;
    //TextView txt_prueba = null;
    MaterialDialog materialDialogAdd = null,
            materialDialogEdit = null,
            materialDialog = null,
            materialDialogIndeterminate = null;

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
        iduser = getIdUser();
        final Call<ObrasResponse> obra = ApiAdapter.getApiService().processGetAllObra(iduser);
        obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                dismissDialog();
                openDialog("Procesando resultados del A.P.I");
                if (response.isSuccessful()) {
                    ObrasResponse obraResponse = response.body();
                    if (obraResponse.error) {

                        dismissDialog();
                        interfaceObras.showSnackBar("Error : Listar Obras");

                    } else {

                        final RealmList<Obra> obrasAPI = obraResponse.obra;
                        final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();

                        listobrasAPIempty = (obrasAPI.size() == 0) ? true : false;
                        listobrasDBempty = (obrasDB.size() == 0) ? true : false;

                        if (listobrasAPIempty && listobrasDBempty) {
                            Log.d(TAG, "api = 0 / db = 0");
                            showTextEmptyHiddeRecycler();
                        } else if (listobrasAPIempty && !listobrasDBempty) {
                            Log.d(TAG, "api = 0 / db = full");
                            setAddAdapter(obrasDB);
                            showRecyclerHiddeTextEmpty();
                        } else if (!listobrasAPIempty && listobrasDBempty) {
                            Log.d(TAG, "api = full / db = 0");
                            saveIntDataBase(obrasAPI);
                            showRecyclerHiddeTextEmpty();
                        } else if (!listobrasAPIempty && !listobrasDBempty) {
                            Log.d(TAG, "api = full / db = full");
                            saveIntDataBase(obrasAPI);
                        }


                    }
                } else {
                    interfaceObras.showSnackBar("Respuesta erronea al Obtener Obras!");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                interfaceObras.showSnackBar("Sin Conexion con el A.P.I / Obras de DB Local");
                dismissDialog();
                openDialog("Extrayendo obras de BD Local");

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

    private Integer getIdUser() {
        Usuario usuario = realm.where(Usuario.class).findFirst();
        return iduser = usuario.id;
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

                Integer id = obras.get(position).id;
                int idlocal = obras.get(position).idlocal;
                String nombre = obras.get(position).name;
                Date datecreatelocal = obras.get(position).createdAtLocalDB;
                int iduser = obras.get(position).iduser;

                processSyncObra(id, idlocal, nombre, datecreatelocal, iduser);

            }

            @Override
            public void onClickViewDetail(View view, int position) {
                Toast.makeText(view.getContext(), "View Detail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickOptions(View view, int position) {
                Integer id = obras.get(position).id,
                        idlocal = obras.get(position).idlocal;
                String name = obras.get(position).name;

                Toast.makeText(view.getContext(), "Long Update " + id + " " + idlocal + " " + name, Toast.LENGTH_SHORT).show();
                openDialogEdit(id, idlocal, name);

            }
        });
        recycler_obras.setAdapter(obraAdapter);
        obraAdapter.notifyDataSetChanged();
    }

    private void processCreateObra(final String nombreobra) {
        dismissDialog();
        openDialog("Enviando Obra al A.P.I");

        //Usuario usuario = realm.where(Usuario.class).findFirst();
        Integer id = null;
        Integer idlocal = getMaxIdObra();
        Date createdAtLocalDB = getDateTime();
        iduser = getIdUser();

        Call<ObrasResponse> obra = ApiAdapter.getApiService().processCreateObra(id, idlocal, nombreobra, createdAtLocalDB, iduser);
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
                        interfaceObras.showSnackBar("Sincronizado");
                    }
                } else {
                    interfaceObras.showSnackBar("Respuesta erronea al Agregar Obra!");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {

                interfaceObras.showSnackBar("Sin Conexion con el A.P.I / Guardado en Local");

                final Obra obra = new Obra();
                obra.idlocal = getMaxIdObra();
                obra.name = nombreobra;
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

    private void processUpdateObra(Integer id, Integer idlocal, final String nombreobra) {
        dismissDialog();
        openDialog("Actualizando");
        Date updatedAtLocalDB = getDateTime();
        iduser = getIdUser();

        Call<ObrasResponse> obra = ApiAdapter.getApiService().processUpdateObra(id, nombreobra, updatedAtLocalDB, iduser);
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
                        interfaceObras.showSnackBar("Sincronizado");
                    }
                } else {
                    interfaceObras.showSnackBar("Respuesta erronea al Agregar Obra!");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {

                interfaceObras.showSnackBar("Sin Conexion con el A.P.I / Guardado en Local");

//                final Obra obra = new Obra();
//                obra.idlocal = getMaxIdObra();
//                obra.name = nombreobra;
//                obra.createdAt = null;
//                obra.updatedAt = null;
//                obra.createdAtLocalDB = getDateTime();
//                obra.iduser = iduser;
//
//                realm.executeTransactionAsync(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.copyToRealmOrUpdate(obra);
//                    }
//                }, new Realm.Transaction.OnSuccess() {
//                    @Override
//                    public void onSuccess() {
//                        checkObras();
//                    }
//                }, new Realm.Transaction.OnError() {
//                    @Override
//                    public void onError(Throwable error) {
//                        Toast.makeText(getActivity(), "processAddObra onError", Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        });
    }

    private void processSyncObra(Integer id, int idlocal, String nombre, Date createloacl, int iduser) {
        openDialogIndeterminate("Sincronizando");
        Call<ObrasResponse> obra = ApiAdapter.getApiService().processSyncObra(id, idlocal, nombre, createloacl, null, iduser);
        obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                if (response.isSuccessful()) {
                    dismissDialogIndeterminate();
                    ObrasResponse obraResponse = response.body();

                    if (obraResponse.error) {
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                    } else {
                        final RealmList<Obra> obras = obraResponse.obra;
                        saveIntDataBase(obras);
                        interfaceObras.showSnackBar("Sincronizado");
                    }
                } else {
                    interfaceObras.showSnackBar("Respuesta erronea al Sincronizar Obra!");
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                dismissDialogIndeterminate();
                interfaceObras.showSnackBar("Sin Conexion con el A.P.I / No sincronizado");
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
        int nextId = getMaxIdObra();
        for (int i = 0; i < obrasapi.size(); i++) {
            Integer id = obrasapi.get(i).id;
            int idlocal = obrasapi.get(i).idlocal;
            int status = obrasapi.get(i).status;
            String name = obrasapi.get(i).name;
            if (obrasapi.get(i).id == null) {
                obrasapi.get(i).idlocal = nextId;
                nextId++;
            } else if (obrasapi.get(i).idlocal == 0) {
                obrasapi.get(i).idlocal = nextId;
                nextId++;
            }
        }
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

    public void openDialogAdd() {
        if (materialDialogAdd == null) {
            materialDialogAdd = new MaterialDialog.Builder(getActivity()).autoDismiss(false)
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
                            materialDialogAdd.dismiss();

                            processCreateObra(nombreobra);

                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            materialDialogAdd.dismiss();
                        }
                    })
                    .build();
            materialDialogAdd.show();
        } else {
            materialDialogAdd.show();
        }
    }

    public void openDialogEdit(Integer _id, Integer _idlocal, String _name){
        final Integer id = _id;
        final Integer idlocal = _idlocal;


        if (materialDialogEdit == null) {
            materialDialogEdit = new MaterialDialog.Builder(getActivity()).autoDismiss(false)
                    .title("Nueva Obra")
                    .customView(R.layout.dialog_obra, true)
                    .positiveText("Editar")
                    .negativeText("Salir")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            final String nombreobra = edt_nombre_obra.getText().toString();
                            edt_nombre_obra.setText("");
                            materialDialogEdit.dismiss();
                            processUpdateObra(id, idlocal, nombreobra);
                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            materialDialogEdit.dismiss();
                        }
                    })
                    .build();
            edt_nombre_obra = (EditText) materialDialogEdit.findViewById(R.id.edt_nombre_obra);
            edt_nombre_obra.setText(_name);
            materialDialogEdit.show();
        } else {
            edt_nombre_obra.setText(_name);
            materialDialogEdit.show();
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
