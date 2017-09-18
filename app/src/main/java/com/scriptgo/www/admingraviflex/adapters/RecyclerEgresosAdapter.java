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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmList;

/**
 * Created by BALAREZO on 11/08/2017.
 */

public class RecyclerEgresosAdapter extends RecyclerView.Adapter<RecyclerEgresosAdapter.ObraViewHolder> {

    private RealmList<Egreso> egresos;
    private Context context;

    EgresosClickRecyclerView listener;

    String fechapago = null;
    SimpleDateFormat simpleDateFormat = null;
    Date d_fechapago = null;
    Calendar calendar = null;

    int dia_pago = 0, mes_pago = 0, anio_pago = 0;
    int dia_create = 0, mes_create = 0, anio_create = 0;

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

        try {

            calendar = Calendar.getInstance();
            calendar.setTime(egreso.date);
            dia_pago = calendar.get(Calendar.DAY_OF_MONTH);
            mes_pago = calendar.get(Calendar.MONTH);
            anio_pago = calendar.get(Calendar.YEAR);

            holder.txt_fecha_pago.setText("Fecha de Pago: " + String.format("%02d", dia_pago) + "-" + String.format("%02d", mes_pago) + "-" + anio_pago);
            holder.txt_monto.setText("Monto Pago: S/." +  String.format("%.2f", egreso.amount));

            calendar.setTime(egreso.createdAt);

            dia_create = calendar.get(Calendar.DAY_OF_MONTH);
            mes_create =  calendar.get(Calendar.MONTH);
            anio_create =  calendar.get(Calendar.YEAR);



            holder.txt_fecha_creacion.setText("Fecha de Creacion: " + String.format("%02d", dia_create) + "-" + String.format("%02d", mes_create) + "-" + anio_create);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
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
            txt_fecha_creacion = (TextView) itemView.findViewById(R.id.txt_fecha_creacion);
            img_doc = (ImageView) itemView.findViewById(R.id.img_doc);
        }

    }

}
