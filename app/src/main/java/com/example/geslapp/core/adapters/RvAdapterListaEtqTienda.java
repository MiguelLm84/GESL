package com.example.geslapp.core.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geslapp.R;
import com.example.geslapp.core.clases.Barcode;
import java.util.ArrayList;



public class RvAdapterListaEtqTienda extends RecyclerView.Adapter<RvAdapterListaEtqTienda.MyViewHolder> {

    Context context;
    ArrayList<Barcode> listaEtqsCodigos;
    Button btnRemove, btnCancel;
    TextView tvBarcodeRemove;

    public RvAdapterListaEtqTienda(ArrayList<Barcode> listaEtqsCodigos, Context context) {

        this.listaEtqsCodigos = listaEtqsCodigos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_codes,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvCodeEtq.setText(listaEtqsCodigos.get(position).getCode());
        holder.btn_remove.setOnClickListener(v -> showDialog(/*holder,*/ position));

        /*holder.tamanho.setText(listaEtqsEscaneadas.get(position).getTamanho());
        holder.ceco.setText(listaEtqsEscaneadas.get(position).getCeco());
        holder.nomCentro.setText(listaEtqsEscaneadas.get(position).getNomCentro());
        holder.tipoEtq.setText(listaEtqsEscaneadas.get(position).getTipoEtq());

        holder.btn_option.setOnClickListener(v -> {

        });*/
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showDialog(/*@NonNull MyViewHolder holder,*/ int position) {

        /*this.listaEtqsEscaneadas.remove(position);
        notifyDataSetChanged();*/

        //int newPosition = holder.getAdapterPosition();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_eliminar, null);
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
        initDialogRemove(dialogLayout);
        tvBarcodeRemove.setText(listaEtqsCodigos.get(position).getCode());
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnRemove.setOnClickListener(v -> removeItem(position, dialog));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    //Método para mapear los componentes del dialogo eliminar.
    private void initDialogRemove(View dialogLayout) {

        btnRemove = dialogLayout.findViewById(R.id.btn_eliminar_dialog);
        btnCancel = dialogLayout.findViewById(R.id.btn_cancelar_dialog);
        tvBarcodeRemove = dialogLayout.findViewById(R.id.tvBarcodeRemove);
    }

    private void removeItem(int position, final AlertDialog dialog) {

        listaEtqsCodigos.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaEtqsCodigos.size());
        dialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return listaEtqsCodigos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        /*TextView tamanho, ceco, nomCentro, tipoEtq;
        ImageButton btn_option;*/

        TextView tvCodeEtq;
        ImageButton btn_remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCodeEtq = itemView.findViewById(R.id.tvCodeEtq);
            btn_remove = itemView.findViewById(R.id.btn_remove);

            /*tamanho = itemView.findViewById(R.id.tvNumTamanho);
            ceco = itemView.findViewById(R.id.tvNumCeco);
            nomCentro = itemView.findViewById(R.id.tvNomCentro);
            tipoEtq = itemView.findViewById(R.id.tvCodigoTipo);
            btn_option = itemView.findViewById(R.id.btn_option);*/
        }
    }
}
