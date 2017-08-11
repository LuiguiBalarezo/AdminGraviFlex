package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.ObrasFragmentToActivity;
import com.scriptgo.www.admingraviflex.models.Obra;
import com.scriptgo.www.admingraviflex.responses.ObrasResponse;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
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
    TextView txt_prueba = null;
    MaterialDialog materialDialogAddOrEdit = null, materialDialog = null;
    EditText edt_nombre_obra;

    // REALM
    // REALM
    Realm realm = null;
    RealmAsyncTask realmAsyncTask = null;

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
        view =  inflater.inflate(R.layout.fragment_obras, container, false);

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

    private void proccessAddObra(String nombreobra){

    }

    private void processGetListObras(){

        openDialog();
        final Call<ObrasResponse> obra = ApiAdapter.getApiService().processGetAllObra();
        obra.enqueue(new Callback<ObrasResponse>() {
            @Override
            public void onResponse(Call<ObrasResponse> call, Response<ObrasResponse> response) {
                // materialDialogHelp.showOrDismissIndeterninate(null, null);
                if (response.isSuccessful()) {
                    ObrasResponse obraResponse = response.body();
                    if (obraResponse.error) {
                        openDialog();
                    } else {
                        final RealmList<Obra> obra = obraResponse.obra;
                        Toast.makeText(getActivity(), "" + obra, Toast.LENGTH_SHORT).show();
                        openDialog();
                    }
                } else {
                    openDialog();
                }
            }

            @Override
            public void onFailure(Call<ObrasResponse> call, Throwable t) {
                openDialog();

            }
        });
    }

    private void saveIntDataBase(){

    }

    public void openDialogAddOrEdit(){
        if(materialDialogAddOrEdit == null){
            materialDialogAddOrEdit = new MaterialDialog.Builder(getActivity()).autoDismiss(false)
                    .title("Nueva Obra")
                    .customView(R.layout.dialog_obra, true)
                    .positiveText("Crear")
                    .negativeText("Salir")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            edt_nombre_obra = (EditText)dialog.findViewById(R.id.edt_nombre_obra);
                            Toast.makeText(getActivity(), ""+   edt_nombre_obra.getText().toString(), Toast.LENGTH_SHORT).show();
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
        }else{
            materialDialogAddOrEdit.show();
        }
    }

    public void openDialog(){
        if(materialDialog == null){
            materialDialog = new MaterialDialog.Builder(getActivity())
                    .autoDismiss(false)
                    .cancelable(false)
                    .title(null)
                    .content("Validando Usuario")
                    .progress(true, 0)
                    .progressIndeterminateStyle(true).build();
            materialDialog.show();
        }else{
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
