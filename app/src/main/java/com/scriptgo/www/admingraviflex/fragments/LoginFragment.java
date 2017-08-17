package com.scriptgo.www.admingraviflex.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scriptgo.www.admingraviflex.MainActivity;
import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.apiadapter.ApiAdapter;
import com.scriptgo.www.admingraviflex.interfaces.LoginFragmentToActivity;
import com.scriptgo.www.admingraviflex.models.Usuario;
import com.scriptgo.www.admingraviflex.responses.LoginResponse;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {


    // UI
    View view;
    MaterialDialog materialDialog = null;
    EditText  edt_usuario , edt_clave;
    Button btn_entrar;

    // VARS
    String usuario = null, clave = null;
    Intent intent = null;
    // REALM
    Realm realm = null;
    RealmAsyncTask realmAsyncTask = null;

    LoginFragmentToActivity loginActivityFragment;

    public LoginFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentToActivity) {
            loginActivityFragment = (LoginFragmentToActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(isUserLogin()){
            startMainActivity();
        }

        view = inflater.inflate(R.layout.fragment_login, container, false);
        initUI();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginActivityFragment = null;
    }

    // METHOD
    private void initUI(){
        edt_usuario = (EditText)view.findViewById(R.id.edt_usuario);
        edt_clave = (EditText)view.findViewById(R.id.edt_clave);
    }

    public void proccessSignIn(){

        usuario = edt_usuario.getText().toString();
        clave = edt_clave.getText().toString();
        openDialog();
        Call<LoginResponse> login = ApiAdapter.getApiService().processLogin(usuario, clave);
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.error) {
                        loginActivityFragment.finishValidateUser("Usuario o Clave Incorrectos");
                        openDialog();
                    } else {
                        final Usuario usuario = loginResponse.usuario;
                        Log.d(this.getClass().getSimpleName(), "OK");
                        saveInDataBase(usuario);
                    }
                } else {
                    loginActivityFragment.finishValidateUser("Error al Formatear Resulados del Login");
                    openDialog();
                    realm.close();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginActivityFragment.finishValidateUser("No se pudo conectar con el API");
                openDialog();
            }
        });


    }

    private void saveInDataBase(final Usuario usuario){
        realm = Realm.getDefaultInstance();
        realmAsyncTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(usuario);
            }
        },  new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                openDialog();
                startMainActivity();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                realm.close();
                openDialog();
                loginActivityFragment.finishValidateUser("Error al Guardar Usuario en DB");
            }
        });
    }

    private void startMainActivity(){
        intent = new Intent(getActivity(), MainActivity.class);
        getActivity().finishAffinity();
        getActivity().startActivity(intent);
    }

    private boolean isUserLogin(){
        realm = Realm.getDefaultInstance();
        final RealmResults<Usuario> results = realm.where(Usuario.class).findAll();
        if(results.size() != 0){
            realm.close();
            return true;
        }else {
            realm.close();
            return false;
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

}
