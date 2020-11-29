package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
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
    Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_amistad);
        conectarFirebase();
        obtenerUsuarioActual(mAuth.getCurrentUser().getUid());
        arrayListUsuario = new ArrayList<>();
        //Referencias
        txtCodigo = findViewById(R.id.txtCodigo);
        btnEnviarSolicitud = findViewById(R.id.btnEnviarSolicitud);
        txtCodigoAmigo = findViewById(R.id.txtCodigoAmigo);
        cargarCodigo();
        //Escuchadores
        btnEnviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idSolicitud = txtCodigoAmigo.getText().toString();
                solicitudAmistad(idSolicitud);
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

    public void solicitudAmistad(final String idAmigo){
        reference.child("Usuarios").child(idAmigo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Mensaje.errorMensaje(getApplicationContext(), "El usuario no se ha encontrado");
                }else if(user!=null){
                    String idUsuario = user.getId();
                    String nombreUsuario = user.getNombre();
                    String apellidoUsuario = user.getApellido();
                    String emailUsuario = user.getEmail();
                    Solicitud solicitud = new Solicitud(idUsuario,nombreUsuario, apellidoUsuario, emailUsuario);
                    reference.child("Usuarios").child(idAmigo).child("Solicitudes").child(idUsuario).setValue(solicitud, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Mensaje.mensaje(getApplicationContext(), "Solicitud enviada");
                        }
                    });
                }else{
                    Mensaje.warningMensaje(getApplicationContext(), "Se ha producido un error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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