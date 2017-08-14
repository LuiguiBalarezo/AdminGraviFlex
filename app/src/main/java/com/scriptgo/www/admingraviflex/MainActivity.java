package com.scriptgo.www.admingraviflex;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.scriptgo.www.admingraviflex.fragments.EgresosFragment;
import com.scriptgo.www.admingraviflex.fragments.IngresosFragment;
import com.scriptgo.www.admingraviflex.fragments.ObrasFragment;
import com.scriptgo.www.admingraviflex.fragments.ValoracionesFragment;
import com.scriptgo.www.admingraviflex.interfaces.ObrasFragmentToActivity;


public class MainActivity extends AppCompatActivity
        implements
        EgresosFragment.OnFragmentInteractionListener,
        IngresosFragment.OnFragmentInteractionListener,
        ObrasFragmentToActivity,
        ValoracionesFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    // UI
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    Snackbar snackbar =  null;
    View view;
    // TRANSACTION
    FragmentManager fragmentManager;

    ActionBarDrawerToggle toggle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        view = fab;

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
           // toolbar.setNavigationIcon(null);
            //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            changeFragment(R.id.nav_obras);
        }else{
            //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        changeFragment(id);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void changeFragment(int iditemnav){

        Fragment fragment = null;
        Class fragmentClass = null;

        if (iditemnav == R.id.nav_obras) {
            fragmentClass = ObrasFragment.class;
            toolbar.setTitle("Obras");
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObrasFragment obrasFragment = (ObrasFragment) getSupportFragmentManager().findFragmentByTag(ObrasFragment.class.getSimpleName());
                    obrasFragment.openDialogAddOrEdit();
                }
            });
        } else if (iditemnav == R.id.nav_egresos) {
            fragmentClass = EgresosFragment.class;
            toolbar.setTitle("Egresos");
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (iditemnav == R.id.nav_ingresos) {
            fragmentClass = IngresosFragment.class;
            toolbar.setTitle("Ingresos");
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (iditemnav == R.id.nav_valoraciones ){
            fragmentClass = ValoracionesFragment.class;
            toolbar.setTitle("Valoraciones");
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else if (iditemnav == R.id.nav_salir) {

        }else{
            Toast.makeText(this, "NO EXISTE ITEM DE MENU", Toast.LENGTH_SHORT).show();
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_framelayout, fragment, fragment.getClass().getSimpleName()).commit();

        drawer.closeDrawer(GravityCompat.START);
    }

    // INTERFACES

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void showSnackBar(String msg) {
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setAction("Action", null).show();
    }

    @Override
    public void dismissSnackBar() {
        if(snackbar != null){
            snackbar.dismiss();
        }
    }
}
