package com.example.geslapp.core.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geslapp.R;
import com.example.geslapp.core.clases.Informes;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.General_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.ui.Activity_Info_General;
import com.example.geslapp.ui.Rv_etiquetas;
import java.util.ArrayList;


public class RvInformesAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<RvInformesAdapter.MyViewHolder>{

    Context context;
    ArrayList<Informes> listainformes;
    Activity activity;
    boolean con_invent;
    String centro, username;
    int ceco;

    public RvInformesAdapter(Context context,String username,String centro,ArrayList<Informes> listainformes,int ceco,Activity activity,boolean con_invent) {
        this.context = context;
        this.username = username;
        this.centro = centro;
        this.listainformes = listainformes;
        this.ceco = ceco;
        this.activity = activity;
        this.con_invent = con_invent;
    }

    @NonNull
    @Override
    public RvInformesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_infor,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(context);
        holder.txtMade.setText(listainformes.get(position).getUser());
        String tipo = listainformes.get(position).getTipo_informe();

        int id_invent = inventario_local_db.getIdInvent(position,ceco);
        holder.txtIdInvent.setText(String.valueOf(id_invent));

        if(tipo.equals("Material")) {
            holder.img.setImageResource(R.drawable.material);

        } else {
            holder.img.setImageResource(R.drawable.general);
        }
        String estado = listainformes.get(position).getEstado_informe();

        if(estado.equals("Abierto") || estado.equals("Cerrado")) {
            holder.butEdit.setBackgroundResource(R.drawable.viewicon);
        }

        holder.txtFecha.setText(listainformes.get(position).getFecha_apertura());
        holder.txtCentro.setText(centro);

        holder.butEdit.setOnClickListener(v -> {

            //int id_invent = inventario_local_db.getIdInvent(position,ceco);

            if(tipo.equals("General")) {
                if(estado.equals("Abierto")) {
                    General_Invent_Local_DB general_invent_local_db = new General_Invent_Local_DB(context);
                    boolean openBy = general_invent_local_db.getOpen(id_invent);

                    if (!openBy)
                        Toast.makeText(context, "Este inventario ha sido abierto por otro usuario", Toast.LENGTH_SHORT).show();
                    else {
                        Intent t = new Intent(context, Activity_Info_General.class);
                        t.putExtra("id_invent", id_invent);
                        t.putExtra("ceco", ceco);
                        t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(t);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                } else {
                    Intent t = new Intent(context, Activity_Info_General.class);
                    t.putExtra("id_invent", id_invent);
                    t.putExtra("ceco", ceco);
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(t);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            } else {
                Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(context);

                if(estado.equals("Abierto")) {
                    boolean openBy = etqs_invent_local_db.getOpen(id_invent);

                    if (!openBy)
                        Toast.makeText(context, "Este inventario ha sido abierto por otro usuario", Toast.LENGTH_SHORT).show();
                    else {
                        if(con_invent)etqs_invent_local_db.fillServerData(context);
                        Intent t = new Intent(context, Rv_etiquetas.class);
                        t.putExtra("id_invent", id_invent);
                        t.putExtra("ceco", ceco);
                        t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(t);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                } else {
                    if(con_invent)etqs_invent_local_db.fillServerData(context);
                    Intent t = new Intent(context, Rv_etiquetas.class);
                    t.putExtra("id_invent", id_invent);
                    t.putExtra("ceco", ceco);
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(t);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listainformes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //Aquí referenciamos los items de la vista
        TextView txtCentro, txtFecha, txtMade, txtIdInvent;
        Button butEdit;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCentro = itemView.findViewById(R.id.txtcardcentro);
            txtFecha = itemView.findViewById(R.id.txtcardfecha);
            txtMade = itemView.findViewById(R.id.txtcardmade);
            butEdit = itemView.findViewById(R.id.butcardedit);
            img = itemView.findViewById(R.id.imgcardtipo);
            txtIdInvent = itemView.findViewById(R.id.txtidinvent);
        }
    }
}
