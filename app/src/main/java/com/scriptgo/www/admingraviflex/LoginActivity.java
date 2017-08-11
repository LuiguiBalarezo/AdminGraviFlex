package com.scriptgo.www.admingraviflex;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scriptgo.www.admingraviflex.fragments.LoginFragment;
import com.scriptgo.www.admingraviflex.interfaces.LoginFragmentToActivity;

public class LoginActivity extends AppCompatActivity implements LoginFragmentToActivity{

    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = LoginFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_framelayout_login, fragment, fragment.getClass().getSimpleName()).commit();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v = view;
                LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
                loginFragment.proccessSignIn();

            }
        });
    }

    @Override
    public void finishValidateUser(String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
    }
}
