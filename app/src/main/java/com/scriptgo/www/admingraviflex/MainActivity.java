package com.scriptgo.www.admingraviflex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scriptgo.www.admingraviflex.fragments.EgresosFragment;
import com.scriptgo.www.admingraviflex.fragments.IngresosFragment;
import com.scriptgo.www.admingraviflex.fragments.ObrasFragment;
import com.scriptgo.www.admingraviflex.fragments.ValoracionesFragment;
import com.scriptgo.www.admingraviflex.interfaces.ObrasFragmentToActivity;


public class MainActivity extends AppCompatActivity
        implements
        IngresosFragment.OnFragmentInteractionListener,
        ObrasFragmentToActivity,
        ValoracionesFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;


    // UI
    View view;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    Snackbar snackbar = null;
    ActionBarDrawerToggle toggle;
    Button btnExpBottomSheet;
    LinearLayout bottomSheet;
    BottomSheetBehavior bsb = null;
    ImageButton imgbtn_opencamera = null, imgbtn_openpickerphoto = null;
    // TRANSACTION
    FragmentManager fragmentManager;

    /* ITEM */
    Intent intentpreferences;

    //FRAGMENTS
    EgresosFragment egresosFragment = null;


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

        bottomSheet = (LinearLayout) findViewById(R.id.bottomSheet);
        imgbtn_opencamera = (ImageButton) bottomSheet.findViewById(R.id.imgbtn_opencamera);
        imgbtn_openpickerphoto = (ImageButton) bottomSheet.findViewById(R.id.imgbtn_openpickerphoto);

        imgbtn_opencamera.setOnClickListener(onClickListener);
        imgbtn_openpickerphoto.setOnClickListener(onClickListener);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // toolbar.setNavigationIcon(null);
            //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            changeFragment(R.id.nav_obras);
        } else {
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


    private void changeFragment(int iditemnav) {

        Fragment fragment = null;
        Class fragmentClass = null;




        switch (iditemnav) {
            case R.id.nav_obras:

                dismissSnackBar();

                fragmentClass = ObrasFragment.class;
                toolbar.setTitle("Obras");
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ObrasFragment obrasFragment = (ObrasFragment) getSupportFragmentManager().findFragmentByTag(ObrasFragment.class.getSimpleName());
                        obrasFragment.initOpenDialogAdd();
                    }
                });
                transactionFragment(fragment, fragmentClass);
                break;
            case R.id.nav_egresos:
                dismissSnackBar();
                fragmentClass = EgresosFragment.class;
                toolbar.setTitle("Egresos");
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        bsb = BottomSheetBehavior.from(bottomSheet);
                        bsb.setState(BottomSheetBehavior.STATE_EXPANDED);

                    }
                });
                transactionFragment(fragment, fragmentClass);
                break;
            case R.id.nav_ingresos:
                dismissSnackBar();
                fragmentClass = IngresosFragment.class;
                toolbar.setTitle("Ingresos");
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                transactionFragment(fragment, fragmentClass);
                break;
            case R.id.nav_valoraciones:
                dismissSnackBar();
                fragmentClass = ValoracionesFragment.class;
                toolbar.setTitle("Valoraciones");
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                transactionFragment(fragment, fragmentClass);
                break;
            case R.id.nav_configuracion:
                dismissSnackBar();
                Toast.makeText(this, "Configuraciones", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_salir:
                dismissSnackBar();
                Toast.makeText(this, "Salir", Toast.LENGTH_SHORT).show();
                break;
            default:
                dismissSnackBar();
                Toast.makeText(this, "NO EXISTE ITEM DE MENU", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            egresosFragment = (EgresosFragment) getSupportFragmentManager().findFragmentByTag(EgresosFragment.class.getSimpleName());
            egresosFragment.initOpenDialogAdd();
            egresosFragment.setImageViewInDialog(imageBitmap);

            Toast.makeText(this, "" + imageBitmap, Toast.LENGTH_SHORT).show();

        }

    }

    // INTERFACES
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void showSnackBar(String msg, String type) {
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        View snackBarView = snackbar.getView();
        snackbar.setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        switch (type) {
            case "log":
                snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_900));
                break;
            case "success":
                snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.green_500));
                break;
            case "error":
                snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.red_500));
                break;
            case "info":
                snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_500));
                break;
        }
        snackbar.setAction("Action", null).show();
    }

    @Override
    public void dismissSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    void transactionFragment(Fragment fragment, Class fragmentClass) {
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_framelayout, fragment, fragment.getClass().getSimpleName()).commit();
    }

    private void startMainActivity(Intent intent, Class ClassActivity) {
        intent = new Intent(this, ClassActivity);
        startActivity(intent);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
            switch (v.getId()) {
                case R.id.imgbtn_opencamera:
                    openCamera();
                    break;
                case R.id.imgbtn_openpickerphoto:
                    Toast.makeText(MainActivity.this, "AGLERIE", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
