package com.ovalle.to_do.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ovalle.to_do.R;
import com.ovalle.to_do.entidades.Tarea;
import com.ovalle.to_do.verNotaCom;

import java.util.ArrayList;

public class notasCompartidasAdapter extends RecyclerView.Adapter<notasCompartidasAdapter.viewHolderDatos> {
    ArrayList<Tarea> tareas;
    Tarea tarea;

    public notasCompartidasAdapter(ArrayList<Tarea> tareas) {
        this.tareas = tareas;
    }

    @NonNull
    @Override
    public notasCompartidasAdapter.viewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tareascompartidas_adapter,parent,false);
        viewHolderDatos vhd = new viewHolderDatos(view);
        return vhd;
    }

    @Override
    public void onBindViewHolder(@NonNull notasCompartidasAdapter.viewHolderDatos holder, int position) {
        holder.cargarTarea(tareas.get(position));
        holder.verNota(tareas.get(position));
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public class viewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtTituloNotaCompartida, txtDescripNotaCompartida;
        ImageButton btnVerNotaCompartida;
        public viewHolderDatos(@NonNull View itemView) {
            super(itemView);
            txtDescripNotaCompartida = itemView.findViewById(R.id.txtDescripNotaCompartida);
            txtTituloNotaCompartida = itemView.findViewById(R.id.txtTituloNotaCompartida);
            btnVerNotaCompartida = itemView.findViewById(R.id.btnVerNotaCompartida);
        }
        public void cargarTarea(Tarea tarea){
            txtTituloNotaCompartida.setText(tarea.getNombre());
            txtDescripNotaCompartida.setText(tarea.getDescripcion());
        }
        public void verNota(final Tarea tarea){
            btnVerNotaCompartida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), verNotaCom.class);
                    intent.putExtra("Nombre nota", tarea.getNombre());
                    intent.putExtra("Descripcion nota", tarea.getDescripcion());
                    intent.putExtra("Cuerpo nota", tarea.getTarea());
                    intent.putExtra("Id nota", tarea.getId());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
