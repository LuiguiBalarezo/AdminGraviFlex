package com.scriptgo.www.admingraviflex.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.models.Obra;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 11/08/2017.
 */

public class ObraAdapter extends  RecyclerView.Adapter<ObraAdapter.ObraViewHolder>{

    private RealmList<Obra> obras;

    public ObraAdapter(RealmList<Obra> obras) {
        this.obras = obras;
    }

    @Override
    public ObraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.obras_item_recycler, parent, false);
        return new ObraViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ObraViewHolder holder, int position) {
        Obra obra = obras.get(position);
        holder.txtNombreObra.setText(obra.nombre);
    }

    @Override
    public int getItemCount() {
        return obras.size();
    }

    public class ObraViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNombreObra;

        public ObraViewHolder(View itemView) {
            super(itemView);
            txtNombreObra = (TextView)itemView.findViewById(R.id.txt_nombre_obra);
        }
    }

}
