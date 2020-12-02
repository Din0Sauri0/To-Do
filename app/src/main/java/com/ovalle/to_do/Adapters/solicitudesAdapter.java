package com.ovalle.to_do.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.R;
import com.ovalle.to_do.SolicitudesAmistad;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Amigo;
import com.ovalle.to_do.entidades.Solicitud;
import com.ovalle.to_do.entidades.Usuario;

import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

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

    public class viewHolderDatos extends RecyclerView.ViewHolder{
        private FirebaseDatabase database;
        private DatabaseReference reference;
        private FirebaseAuth mAuth;

        Usuario user;

        private ImageButton btnRechazar, btnAceptar;
        private TextView txtNombreSolicitante, txtEmailSolicitante;
        public viewHolderDatos(@NonNull final View itemView) {
            super(itemView);
            conectarFirebase();
            obtenerUsuarioActual(mAuth.getCurrentUser().getUid());
            btnAceptar = itemView.findViewById(R.id.btnAceptar);
            btnRechazar = itemView.findViewById(R.id.btnRechazar);
            txtNombreSolicitante = itemView.findViewById(R.id.txtNombreSolicitante);
            txtEmailSolicitante = itemView.findViewById(R.id.txtEmailSolicitante);
            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Email = txtEmailSolicitante.getText().toString();
                    aceptarSolicitud(Email);
                }
            });
            btnRechazar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = txtEmailSolicitante.getText().toString();
                    rechazarSolicitud(email);
                }
            });

        }
        public void cargarSolicitud(Solicitud solicitud){
            txtNombreSolicitante.setText(solicitud.getNombre()+" "+solicitud.getApellido());
            txtEmailSolicitante.setText(solicitud.getEmail());
        }
        public void rechazarSolicitud(final String email){
            reference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario usuario;
                    for (DataSnapshot dato:snapshot.getChildren()) {
                        usuario = dato.getValue(Usuario.class);
                        if(usuario.getEmail().equals(email)){
                            reference.child("Usuarios").child(user.getId()).child("Solicitudes").child(usuario.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Mensaje.warningMensaje(itemView.getContext(), "La solicitud ha sido rechazada");
                                }
                            });
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        public void aceptarSolicitud(final String email){
            reference.child("Usuarios").child(user.getId()).child("Solicitudes").addListenerForSingleValueEvent(new ValueEventListener() {
                Solicitud solicitudResivida;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        solicitudResivida = data.getValue(Solicitud.class);
                        if(solicitudResivida.getEmail().equals(email)){
                            String idAmigo = solicitudResivida.getIdUsuario();
                            String nombreAmigo = solicitudResivida.getNombre();
                            String apellidoAmigo = solicitudResivida.getApellido();
                            final String emailAmigo = solicitudResivida.getEmail();
                            Amigo miAmigo = new Amigo(idAmigo,nombreAmigo,apellidoAmigo,emailAmigo);
                            String miId = user.getId();
                            String miNombre = user.getNombre();
                            String miApellido = user.getApellido();
                            String miEmail = user.getEmail();
                            Amigo yoAmigo = new Amigo(miId,miNombre,miApellido,miEmail);
                            reference.child("Usuarios").child(miId).child("Amigos").child(idAmigo).setValue(miAmigo, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                }
                            });
                            reference.child("Usuarios").child(idAmigo).child("Amigos").child(miId).setValue(yoAmigo, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                }
                            });
                            reference.child("Usuarios").child(miId).child("Solicitudes").child(solicitudResivida.getIdUsuario()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Mensaje.mensaje(itemView.getContext(), "Se ha aceptado la solicitud de: "+emailAmigo);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            /*
            reference.child("Usuarios").child(user.getId()).child("Solicitudes").addListenerForSingleValueEvent(new ValueEventListener() {
                Solicitud solicitudResivida;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data:snapshot.getChildren()) {
                        solicitudResivida = data.getValue(Solicitud.class);

                        if(solicitudResivida.getEmail().equals(email)){
                            final String idUser = user.getId();
                            String nombreUser = user.getNombre();
                            String apellidoUser = user.getApellido();
                            String emailUser = user.getEmail();
                            Solicitud amigo = new Solicitud(idUser,nombreUser,apellidoUser,emailUser);
                            reference.child("Usuarios").child(solicitudResivida.getIdUsuario()).child("Amigos").child(idUser).setValue(amigo, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    reference.child("Usuarios").child(idUser).child("Amigos").child(solicitudResivida.getIdUsuario()).setValue(solicitudResivida, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Mensaje.mensaje(itemView.getContext(), "La solicitud ha sido aceptada");
                                        }
                                    });
                                }
                            });

                        }
                    }
                    reference.child("Usuarios").child(user.getId()).child("Solicitudes").child(solicitudResivida.getIdUsuario()).removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

             */


        }
        public void conectarFirebase(){
            database = FirebaseDatabase.getInstance();
            reference = database.getReference();
            mAuth = FirebaseAuth.getInstance();
            if(reference != null){
                //Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
            }
        }

        public void obtenerUsuarioActual(String idUsuario){
            reference.child("Usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String id = snapshot.child("id").getValue().toString();
                    String nombre = snapshot.child("nombre").getValue().toString();
                    String apellido = snapshot.child("apellido").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String password = snapshot.child("password").getValue().toString();
                    user = new Usuario(id, nombre, apellido, email, password);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
