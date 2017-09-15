package com.scriptgo.www.admingraviflex.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.models.Obra;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public class SpinnerObraAdapter extends ArrayAdapter<Obra> {

    View view = null;
    int layout = 0;
    Activity activity = null;
    RealmList<Obra> obras = null;
    LayoutInflater layoutInflater = null;
    private SpinnerViewHolder spinnerViewHolder;


    public SpinnerObraAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull RealmList<Obra> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = resource;
        obras = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        view = layoutInflater.inflate(layout, parent, false);
        SpinnerViewHolder spinnerViewHolder = new SpinnerViewHolder(view);
        spinnerViewHolder.txt_nombre.setText(obras.get(position).name);
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public class SpinnerViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_nombre;

        public SpinnerViewHolder(View itemView) {
            super(itemView);
            txt_nombre = (TextView) itemView.findViewById(R.id.txt_obra);
        }

    }

}
