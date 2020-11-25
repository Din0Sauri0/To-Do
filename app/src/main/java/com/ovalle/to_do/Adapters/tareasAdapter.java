package com.ovalle.to_do.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ovalle.to_do.R;
import com.ovalle.to_do.entidades.Tarea;

import java.util.ArrayList;

public class tareasAdapter extends RecyclerView.Adapter<tareasAdapter.viewHolderDatos> {
    ArrayList<Tarea> tareas;
    Tarea tarea;

    public tareasAdapter(ArrayList<Tarea> tareas) {
        this.tareas = tareas;
    }

    @NonNull
    @Override
    public tareasAdapter.viewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tareas_item,parent,false);
        viewHolderDatos vhd = new viewHolderDatos(view);
        return vhd;
    }

    @Override
    public void onBindViewHolder(@NonNull tareasAdapter.viewHolderDatos holder, int position) {
        holder.cargarTarea(tareas.get(position));
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public class viewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtTituloTarea, txtDescripcionTareaRecycler;
        public viewHolderDatos(@NonNull View itemView) {
            super(itemView);
            //Referencias
            txtTituloTarea = itemView.findViewById(R.id.txtTituloTarea);
            txtDescripcionTareaRecycler = itemView.findViewById(R.id.txtDescripcionTareaRecycler);
        }
        public void cargarTarea(Tarea tarea){
            txtTituloTarea.setText(tarea.getNombre());
            txtDescripcionTareaRecycler.setText(tarea.getDescripcion());
        }
    }
}
