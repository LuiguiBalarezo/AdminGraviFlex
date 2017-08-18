package com.scriptgo.www.admingraviflex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.interfaces.ObrasClickRecyclerView;
import com.scriptgo.www.admingraviflex.models.Obra;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 11/08/2017.
 */

public class ObraAdapter extends  RecyclerView.Adapter<ObraAdapter.ObraViewHolder>{




    private RealmList<Obra> obras;
    private Context context;

    ObrasClickRecyclerView listener;

    public ObraAdapter(Context ctx, RealmList<Obra> obras , ObrasClickRecyclerView listener) {
        context = ctx;
        this.obras = obras;
        this.listener = listener;
    }

    @Override
    public ObraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.obras_item_recycler, parent, false);

        final  ObraViewHolder  obraViewHolder = new ObraViewHolder(v);
        obraViewHolder.img_iconsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSync(v,  obraViewHolder.getAdapterPosition());
            }
        });
        return obraViewHolder;
    }


    @Override
    public void onBindViewHolder(ObraViewHolder holder, int position) {
        Obra obra = obras.get(position);
        holder.txt_nombre.setText(obra.id + " " + obra.idlocal + " " + obra.nombre);

        if(obra.id != null){
            holder.img_iconsync.setImageResource(R.drawable.ic_cloud_done_black_24dp);
        }else{
            holder.img_iconsync.setImageResource(R.drawable.ic_access_time_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return obras.size();
    }

    public class ObraViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_nombre;
        private ImageView img_iconsync;

        public ObraViewHolder(View itemView) {
            super(itemView);
            txt_nombre = (TextView)itemView.findViewById(R.id.txt_nombre_obra);
            img_iconsync = (ImageView)itemView.findViewById(R.id.img_iconsync);
        }

    }

}
