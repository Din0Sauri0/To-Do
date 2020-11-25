package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Adapters.tareasAdapter;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Solicitud;
import com.ovalle.to_do.entidades.Tarea;
import com.ovalle.to_do.entidades.Usuario;

import java.util.ArrayList;
import java.util.UUID;

public class SolicitudesAmistad extends AppCompatActivity {
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Referencias
    private TextView txtCodigo;
    private Button btnEnviarSolicitud;
    private EditText txtCodigoAmigo;
    //Ayuda solicitud
    ArrayList<Usuario> arrayListUsuario;
    Usuario userSolicitudAmistad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_amistad);
        conectarFirebase();
        arrayListUsuario = new ArrayList<>();
        //Referencias
        txtCodigo = findViewById(R.id.txtCodigo);
        btnEnviarSolicitud = findViewById(R.id.btnEnviarSolicitud);
        txtCodigoAmigo = findViewById(R.id.txtCodigoAmigo);
        cargarCodigo();

        btnEnviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idUsuarioSolicitud = txtCodigoAmigo.getText().toString();
                reference.child("Usuarios").child(idUsuarioSolicitud).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayListUsuario.clear();
                        for (DataSnapshot dato: snapshot.getChildren()) {
                            userSolicitudAmistad = dato.getValue(Usuario.class);
                        }
                        if(userSolicitudAmistad != null){
                            String idSolicitud = UUID.randomUUID().toString();

                            String idUsuarioSolicitante = userSolicitudAmistad.getId();
                            String nombeUsuarioSolicitante = userSolicitudAmistad.getNombre();
                            String apellidoUsuarioSolicitante = userSolicitudAmistad.getApellido();
                            String emailUsuarioSolicitante = userSolicitudAmistad.getEmail();
                            Solicitud soli = new Solicitud(idSolicitud, idUsuarioSolicitante,nombeUsuarioSolicitante,apellidoUsuarioSolicitante,emailUsuarioSolicitante);
                            solicitud(soli, idSolicitud);
                        }else{
                            Mensaje.errorMensaje(getApplicationContext(), "El usuario no ha sido encontrado");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    //Metodos
    public void conectarFirebase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        if(reference != null){
            //Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
        }
    }

    public void cargarCodigo(){
        String idUsuario = mAuth.getCurrentUser().getUid();
        txtCodigo.setText(idUsuario);
    }

    public void solicitud(Solicitud solicitud, String codigoSolicitudAmigo){
        reference.child("Usuarios").child(codigoSolicitudAmigo).child("Solicitudes").child(solicitud.getIdSolicitud()).setValue(solicitud, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Mensaje.mensaje(getApplicationContext(), "Solicitud enviada");
            }
        });
    }
}