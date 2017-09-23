package com.scriptgo.www.admingraviflex.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.adapters.RecyclerEgresosAdapter;
import com.scriptgo.www.admingraviflex.adapters.SpinnerObraAdapter;
import com.scriptgo.www.admingraviflex.bases.BaseFragments;
import com.scriptgo.www.admingraviflex.compound.ProgressCircularText;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessEgresosApi;
import com.scriptgo.www.admingraviflex.interfaces.CallBackProcessObraApi;
import com.scriptgo.www.admingraviflex.interfaces.EgresosClickRecyclerView;
import com.scriptgo.www.admingraviflex.models.Egreso;
import com.scriptgo.www.admingraviflex.models.Obra;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;
import com.scriptgo.www.admingraviflex.services.EgresoServiceAPI;
import com.scriptgo.www.admingraviflex.services.ObraServiceAPI;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;

public class EgresosFragment extends BaseFragments {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //    /* UI */
    AppCompatSpinner spinner = null;
    EditText edt_fecha_emision = null, edt_serie = null, edt_monto = null;
    ImageView imageView = null;
    // RESPONSE
    Call<ObrasResponse> obraservicegetlistactive_id_name = null;

    // ADAPTER
    private SpinnerObraAdapter spinnerObraAdapter;
    private RecyclerEgresosAdapter recyclerEgresosAdapter;

    //RESPPONSE
    ObrasResponse obraResponse = null;

    /* SERVICES */
    ObraServiceAPI obraServiceAPI = null;
    EgresoServiceAPI egresoServiceAPI = null;

    /* REALM */
    // REALM
    RealmAsyncTask realmAsyncTask = null;
    RealmList<Obra> obrasList = null;
    RealmList<Egreso> egresosList = null;

    /* VARS*/
    Date fecha_emision = null;
    int serie = 0;
    float monto = 0;
    int idobra = 0;
    String image = null;
    Integer positionitemspinner = null;
    Calendar calendar = Calendar.getInstance();
    String IDOBRA = "IDOBRA",
            POSITIONSPINNER = "POSITIONSPINNER";

    Bitmap imageBitmap = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EgresosFragment() {
        // Required empty public constructor
    }

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

        if (savedInstanceState != null) {
            idobra = savedInstanceState.getInt(IDOBRA);
            positionitemspinner = savedInstanceState.getInt(POSITIONSPINNER);
        }

        initServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_egresos, container, false);
        initUI();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    void apigetallobras_id_name() {
        obraServiceAPI.getAllActive(new CallBackProcessObraApi() {
            @Override
            public void connect(RealmList<Obra> obraAPI) {

                listobrasAPIempty = (obraAPI.size() == 0) ? true : false;
                final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();
                listobrasDBempty = (obrasDB.size() == 0) ? true : false;

//                if (listobrasAPIempty && listobrasDBempty) {
////                    visibleViewContent("empty");
//                } else if (listobrasAPIempty && !listobrasDBempty) {
//                    visibleViewContent("recycler");
                setSpinnerAddAdapter(obrasDB);
//                } else if (!listobrasAPIempty && listobrasDBempty) {
////                    visibleViewContent("recycler");
////                    saveIntDataBase(obraAPI, true);
//                } else if (!listobrasAPIempty && !listobrasDBempty) {
////                    visibleViewContent("recycler");
////                    saveIntDataBase(obraAPI, true);
//                }

            }

            @Override
            public void disconnect() {
                Toast.makeText(getActivity(), "disconnect", Toast.LENGTH_SHORT).show();
                final RealmResults<Obra> obrasDB = realm.where(Obra.class).findAll();

            }
        });
    }


    void apigetallegresosbyobra(int idobra) {
        egresoServiceAPI.getAllEgresosByObra(idobra, new CallBackProcessEgresosApi() {
            @Override
            public void connect(RealmList<Egreso> egresosAPI) {
                listegresosAPIempty = (egresosAPI.size() == 0) ? true : false;
                saveIntDataBase(egresosAPI, true);

//                final RealmResults<Egreso> egresoDB = realm.where(Egreso.class).findAll();
//                listegresosDBempty = (egresoDB.size() == 0) ? true : false;
//
//                if (listegresosAPIempty && listegresosDBempty) {
//                    visibleViewContent("empty");
//                } else if (listegresosAPIempty && !listegresosDBempty) {
//                    visibleViewContent("recycler");
//                    setRecyclerAddAdapter(egresoDB);
//                } else if (!listegresosAPIempty && listegresosDBempty) {
//                    visibleViewContent("recycler");
//                    saveIntDataBase(egresosAPI, true);
//                } else if (!listegresosAPIempty && !listegresosDBempty) {
//                    visibleViewContent("recycler");
//                    saveIntDataBase(egresosAPI, true);
//                }

            }

            @Override
            public void disconnect() {
                Toast.makeText(getActivity(), "disconnect", Toast.LENGTH_SHORT).show();
                //final RealmResults<Egreso> egresoDB = realm.where(Egreso.class).findAll();
            }
        });
    }


    private void saveIntDataBase(final RealmList<Egreso> egresosList, boolean deleteallsync) {

        int nextId = getMaxId();
        int countapi = egresosList.size();

        if (deleteallsync) {
            final RealmResults<Egreso> egresoRealmResults = realm.where(Egreso.class)
                    .equalTo("sync", 1)
                    .findAll();
            realm.beginTransaction();
            egresoRealmResults.deleteAllFromRealm();
            realm.commitTransaction();
        }

        for (int i = 0; i < countapi; i++) {
            int id = egresosList.get(i).id;
            int idlocal = egresosList.get(i).idlocal;
            int status = egresosList.get(i).status;
//            String name = egresosList.get(i).name;
            Date createdAt = egresosList.get(i).createdAt;
            Date updatedAt = egresosList.get(i).updatedAt;
            int sync = egresosList.get(i).sync;
            int iduser = egresosList.get(i).iduser;

            if (id == 0) {
                egresosList.get(i).idlocal = nextId;
                nextId++;
            } else if (idlocal == 0) {
                egresosList.get(i).idlocal = nextId;
                nextId++;
            }
        }

        realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(egresosList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
//                Toast.makeText(getActivity(), "saveIntDataBase onSuccess", Toast.LENGTH_SHORT).show();
                check();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
//                Toast.makeText(getActivity(), "saveIntDataBase onError", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* METHOD */
    private void check() {
        RealmResults<Egreso> egresoRealmResults = realm.where(Egreso.class).equalTo("idwork", idobra).findAllAsync();
        egresoRealmResults.addChangeListener(new RealmChangeListener<RealmResults<Egreso>>() {
            @Override
            public void onChange(RealmResults<Egreso> egresos) {
                if (egresos.size() == 0) {
                    visibleViewContent("empty");
                } else {
                    visibleViewContent("recycler");
                    setRecyclerAddAdapter(egresos);
                }
            }
        });
    }

    private int getMaxId() {
        Number currentIdNum = realm.where(Egreso.class).max("idlocal");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }

    private void setSpinnerAddAdapter(final RealmResults<Obra> obras) {
        obrasList = new RealmList<Obra>();
        obrasList.addAll(obras.subList(0, obras.size()));
        spinnerObraAdapter = new SpinnerObraAdapter(getContext(), R.layout.obras_item_spinner, obrasList);
        spinner.setAdapter(spinnerObraAdapter);
        spinnerObraAdapter.notifyDataSetChanged();
        if(positionitemspinner !=null){
            spinner.setSelection(positionitemspinner);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + obrasList.get(position).name, Toast.LENGTH_SHORT).show();
                idobra = obrasList.get(position).id;
                positionitemspinner = position;
                apigetallegresosbyobra(idobra);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setRecyclerAddAdapter(final RealmResults<Egreso> egresos) {
        egresosList = new RealmList<Egreso>();
        egresosList.addAll(egresos.subList(0, egresos.size()));
        recyclerEgresosAdapter = new RecyclerEgresosAdapter(getActivity(), egresosList, new EgresosClickRecyclerView() {
            @Override
            public void onClickSync(View view, int position) {

            }

            @Override
            public void onClickViewDetail(View view, int position) {

            }

            @Override
            public void onLongClickOptions(View view, int position) {

            }
        });
        recycler_view.setAdapter(recyclerEgresosAdapter);
        recyclerEgresosAdapter.notifyDataSetChanged();
    }


    /* */
    public void initOpenDialogAdd() {
        openDialogAdd("Nuevo Egreso", R.layout.dialog_egresos, "Crear", "Salir", singleButtonCallback, singleButtonCallback);
        edt_fecha_emision = (EditText) materialDialogAdd.findViewById(R.id.edt_fecha_emision);
        edt_fecha_emision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDatePicker().show();
            }
        });
        edt_serie = (EditText) materialDialogAdd.findViewById(R.id.edt_serie);
        edt_monto = (EditText) materialDialogAdd.findViewById(R.id.edt_monto);
    }

    public void setImageViewInDialog(Bitmap imgbmp) {
        imageView = (ImageView) materialDialogAdd.findViewById(R.id.img_egreso_dialog);
        imageBitmap = imgbmp;
        imageView.setImageBitmap(imageBitmap);
    }


    private String imagetToString(Bitmap bmp){
        ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
        byte[] imgByte = byteArrayInputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fecha_emision = format.parse(edt_fecha_emision.getText().toString());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        serie = Integer.parseInt(edt_serie.getText().toString());
        monto = Float.parseFloat(edt_monto.getText().toString());
        image = imagetToString(imageBitmap);
        apicreateaEgreso(fecha_emision, serie, monto, image);
        dismissDialogAdd();
    }

    void negativeadd() {
        dismissDialogAdd();
    }

    void apicreateaEgreso(final Date fecha, final int serie, final float monto, String image) {

        int id = 0;
        int idlocal = getMaxId();
        Date createdAtLocalDB = getDateTime();

        egresoServiceAPI.create(id, idlocal, idobra, fecha, serie, monto, image, createdAtLocalDB, new CallBackProcessEgresosApi() {
            @Override
            public void connect(RealmList<Egreso> egresoAPI) {
                final RealmList<Egreso> egresos = egresoAPI;
                saveIntDataBase(egresos, false);
                showSnackbar("Sincronizado", "success");
            }

            @Override
            public void disconnect() {
                visibleViewContent("progress");
                dismissDialogIndeterminate();

                showSnackbar("Sin Conexion / Guardado en Local", "info");

                final Egreso egreso = new Egreso();
                egreso.idlocal = getMaxId();
                egreso.idwork = idobra;
                egreso.date = fecha;
                egreso.number = serie;
                egreso.amount = monto;
                egreso.createdAt = null;
                egreso.updatedAt = null;
                egreso.createdAtLocalDB = getDateTime();
                egreso.status = 1;
                egreso.sync = 0;
                egreso.iduser = iduser;

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(egreso);
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
                        showSnackbar("Realm : erronea al Agregar Obra!", "error");

                    }
                });
            }
        });
    }

    /* METHOD */
    private void checkObras() {
        RealmResults<Egreso> obrasdb = realm.where(Egreso.class).findAllAsync();
        obrasdb.addChangeListener(new RealmChangeListener<RealmResults<Egreso>>() {
            @Override
            public void onChange(RealmResults<Egreso> egresos) {
                if (egresos.size() == 0) {
                    visibleViewContent("empty");
                } else {
                    visibleViewContent("recycler");
                    setRecyclerAddAdapter(egresos);
                }
            }
        });
    }

    protected Dialog openDialogDatePicker() {
        return new DatePickerDialog(getActivity(), onDateSetListener, anioinit, mesinit, diainit);
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dia = dayOfMonth;
            mes = month;
            anio = year;
            edt_fecha_emision.setText(dia + "-" + mes + "-" + anio);
        }
    };

    @Override
    protected void initUI() {
        super.initUI();
        spinner = (AppCompatSpinner) view.findViewById(R.id.spn_obras);
        progressCircularText = (ProgressCircularText) view.findViewById(R.id.pgrs_egresos);
        recycler_view = (RecyclerView) view.findViewById(R.id.rcv_egresos);
        txt_vacio = (TextView) view.findViewById(R.id.txt_vacio);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(false);
        recycler_view.setLayoutManager(layoutManager);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(IDOBRA, idobra);
        outState.putInt(POSITIONSPINNER, positionitemspinner);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initServices() {
        super.initServices();
        obraServiceAPI = new ObraServiceAPI(iduser);
        egresoServiceAPI = new EgresoServiceAPI(iduser);
    }

}
