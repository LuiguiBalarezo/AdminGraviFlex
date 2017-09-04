package com.scriptgo.www.admingraviflex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.interfaces.ObrasClickRecyclerView;
import com.scriptgo.www.admingraviflex.models.Obra;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 03/09/2017.
 */

public class SpinnerObraAdapter extends RecyclerView.Adapter<SpinnerObraAdapter.ObraViewHolder> {

    private RealmList<Obra> obras;
    private Context context;

    private View view;

    public SpinnerObraAdapter(Context ctx, RealmList<Obra> obras, ObrasClickRecyclerView listener) {
        this.context = ctx;
        this.obras = obras;
        //this.listener = listener;
    }

    @Override
    public ObraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.obras_item_spinner, parent, false);
        final ObraViewHolder obraViewHolder = new ObraViewHolder(view);
        return obraViewHolder;
    }


    @Override
    public void onBindViewHolder(ObraViewHolder holder, int position) {
        Obra obra = obras.get(position);
        holder.txt_nombre.setText(obra.name);
    }

    @Override
    public int getItemCount() {
        return obras.size();
    }

    public class ObraViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_nombre;

        public ObraViewHolder(View itemView) {
            super(itemView);
            txt_nombre = (TextView) itemView.findViewById(R.id.txt_obra);
        }

    }

}
