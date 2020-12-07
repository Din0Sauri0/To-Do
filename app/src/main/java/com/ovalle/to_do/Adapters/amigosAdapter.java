package com.ovalle.to_do.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ovalle.to_do.R;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Amigo;

import java.util.ArrayList;

public class amigosAdapter extends RecyclerView.Adapter<amigosAdapter.viewHolderDatos> {
    ArrayList<Amigo> amigos;
    Amigo amigo;

    public amigosAdapter(ArrayList<Amigo> amigos) {
        this.amigos = amigos;
    }

    @NonNull
    @Override
    public amigosAdapter.viewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amigos_items, parent, false);
        viewHolderDatos vhd = new viewHolderDatos(view);
        return vhd;
    }

    @Override
    public void onBindViewHolder(@NonNull final amigosAdapter.viewHolderDatos holder, final int position) {
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amigo = amigos.get(position);
                Mensaje.mensajeShort(holder.itemView.getContext(), amigo.getNombre());

            }
        });*/
        holder.cargarAmigo(amigos.get(position));
    }

    @Override
    public int getItemCount() {
        return amigos.size();
    }

    public class viewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtNombreAmigo, txtEmailAmigo;
        public viewHolderDatos(@NonNull View itemView) {
            super(itemView);
            //Referencias
            txtNombreAmigo = itemView.findViewById(R.id.txtNombreAmigo);
            txtEmailAmigo = itemView.findViewById(R.id.txtEmailAmigo);

        }
        public void cargarAmigo(Amigo amigo){
            txtNombreAmigo.setText(amigo.getNombre()+" "+amigo.getApellido());
            txtEmailAmigo.setText(amigo.getEmail());
        }
    }
}
