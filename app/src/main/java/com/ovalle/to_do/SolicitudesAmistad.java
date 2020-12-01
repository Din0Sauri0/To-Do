package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Adapters.solicitudesAdapter;
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
    private RecyclerView recyclerSolicitudes;
    //Ayuda solicitud
    Usuario user;
    Solicitud solicitudes;
    //ayuda Recycler
    ArrayList<Solicitud> arrayListSolicitudes;
    Solicitud solicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_amistad);
        conectarFirebase();
        obtenerUsuarioActual(mAuth.getCurrentUser().getUid());
        //Referencias
        txtCodigo = findViewById(R.id.txtCodigo);
        btnEnviarSolicitud = findViewById(R.id.btnEnviarSolicitud);
        txtCodigoAmigo = findViewById(R.id.txtCodigoAmigo);
        recyclerSolicitudes = findViewById(R.id.recyclerSolicitudes);
        recyclerSolicitudes.setLayoutManager(new LinearLayoutManager(this));
        arrayListSolicitudes = new ArrayList<>();
        cargarCodigo();
        cargarSolicitudes();
        //Escuchadores
        btnEnviarSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = txtCodigoAmigo.getText().toString();
                solicitudAmistad(Email);
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

    public void solicitudAmistad(final String email){
        reference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            boolean noexist = false;
            Usuario usuarioObtenido;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dato:snapshot.getChildren()) {
                    usuarioObtenido = dato.getValue(Usuario.class);
                    if(usuarioObtenido.getEmail().equals(email)){
                        noexist = true;
                        String idUsuario = usuarioObtenido.getId();
                        String miIdUsuario = user.getId();
                        final String nombreUsuario = user.getNombre();
                        final String apellidoUsuario = user.getApellido();
                        String emailUsuario = user.getEmail();
                        Solicitud solicitudAmistad = new Solicitud(miIdUsuario,nombreUsuario,apellidoUsuario,emailUsuario);
                        reference.child("Usuarios").child(idUsuario).child("Solicitudes").child(miIdUsuario).setValue(solicitudAmistad, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Mensaje.mensaje(getApplicationContext(), "Se ha enviado una solicitud de amnista ha: "+email);
                            }
                        });
                    }
                }
                if(noexist != true){
                    Mensaje.errorMensaje(getApplicationContext(), "No se ha encontrado usuario con ese correo");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void obtenerUsuarioActual(String idUsuario){
        reference.child("Usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void cargarSolicitudes(){
        reference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Solicitudes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListSolicitudes.clear();
                for (DataSnapshot dato: snapshot.getChildren()) {
                    solicitud = dato.getValue(Solicitud.class);
                    arrayListSolicitudes.add(solicitud);
                }
                solicitudesAdapter adapter = new solicitudesAdapter(arrayListSolicitudes);
                recyclerSolicitudes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}