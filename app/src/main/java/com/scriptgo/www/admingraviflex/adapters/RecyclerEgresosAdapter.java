package com.scriptgo.www.admingraviflex.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scriptgo.www.admingraviflex.R;
import com.scriptgo.www.admingraviflex.interfaces.EgresosClickRecyclerView;
import com.scriptgo.www.admingraviflex.models.Egreso;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 11/08/2017.
 */

public class RecyclerEgresosAdapter extends RecyclerView.Adapter<RecyclerEgresosAdapter.ObraViewHolder> {

    private RealmList<Egreso> egresos;
    private Context context;

    EgresosClickRecyclerView listener;

    public RecyclerEgresosAdapter(Context ctx, RealmList<Egreso> egresos, EgresosClickRecyclerView listener) {
        this.context = ctx;
        this.egresos = egresos;
        this.listener = listener;
    }

    @Override
    public ObraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.egresos_item_recycler, parent, false);

        final ObraViewHolder obraViewHolder = new ObraViewHolder(v);

//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClickViewDetail(v, obraViewHolder.getAdapterPosition());
//            }
//        });
//
//        v.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                listener.onLongClickOptions(v, obraViewHolder.getAdapterPosition());
//                return true;
//            }
//        });
//
//        obraViewHolder.img_iconsync.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClickSync(v,  obraViewHolder.getAdapterPosition());
//            }
//        });

        return obraViewHolder;

    }


    @Override
    public void onBindViewHolder(ObraViewHolder holder, int position) {
        Egreso egreso = egresos.get(position);
        holder.txt_fecha_pago.setText(egreso.date.toString());

//        if(obra.id != 0){
//            holder.img_iconsync.setImageResource(R.drawable.ic_cloud_done_black_24dp);
//            holder.img_iconsync.setEnabled(false);
//        }else{
//            holder.img_iconsync.setImageResource(R.drawable.ic_access_time_black_24dp);
//            holder.img_iconsync.setEnabled(true);
//        }
    }

    @Override
    public int getItemCount() {
        return egresos.size();
    }

    public class ObraViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_fecha_pago;
        private TextView txt_monto;
        private TextView txt_creador;
        private TextView txt_fecha_creacion;
        private ImageView img_doc;

        public ObraViewHolder(View itemView) {
            super(itemView);
            txt_fecha_pago = (TextView) itemView.findViewById(R.id.txt_fecha_pago);
            txt_monto = (TextView) itemView.findViewById(R.id.txt_monto);
            txt_creador = (TextView) itemView.findViewById(R.id.txt_creador);
            txt_fecha_creacion = (TextView) itemView.findViewById(R.id.txt_fecha_creacion);
            img_doc = (ImageView) itemView.findViewById(R.id.img_doc);
        }

    }

}
