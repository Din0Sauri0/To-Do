package com.ovalle.to_do.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ovalle.to_do.R;
import com.ovalle.to_do.entidades.Solicitud;

import java.util.ArrayList;

public class solicitudesAdapter extends RecyclerView.Adapter<solicitudesAdapter.viewHolderDatos> {
    ArrayList<Solicitud> solicitudes;
    Solicitud solicitud;

    public solicitudesAdapter(ArrayList<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }

    @NonNull
    @Override
    public solicitudesAdapter.viewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.solicitudes_item,parent,false);
        viewHolderDatos vhd = new viewHolderDatos(view);
        return vhd;
    }

    @Override
    public void onBindViewHolder(@NonNull solicitudesAdapter.viewHolderDatos holder, int position) {
        holder.cargarSolicitud(solicitudes.get(position));
    }

    @Override
    public int getItemCount() {
        return solicitudes.size();
    }

    public class viewHolderDatos extends RecyclerView.ViewHolder {
        private ImageButton btnRechazar, btnAceptar;
        private TextView txtNombreSolicitante, txtEmailSolicitante;
        public viewHolderDatos(@NonNull View itemView) {
            super(itemView);
            btnAceptar = itemView.findViewById(R.id.btnAceptar);
            btnRechazar = itemView.findViewById(R.id.btnRechazar);
            txtNombreSolicitante = itemView.findViewById(R.id.txtNombreSolicitante);
            txtEmailSolicitante = itemView.findViewById(R.id.txtEmailSolicitante);
        }
        public void cargarSolicitud(Solicitud solicitud){
            txtNombreSolicitante.setText(solicitud.getNombre()+" "+solicitud.getApellido());
            txtEmailSolicitante.setText(solicitud.getEmail());
        }
    }
}
