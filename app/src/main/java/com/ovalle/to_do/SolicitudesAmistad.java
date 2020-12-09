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
import com.ovalle.to_do.entidades.Amigo;
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
    Usuario usuario;
    Usuario user;
    Amigo amigo;
    boolean exist;
    boolean soliExist;
    Solicitud solicitudes;
    String idAmigo;
    //ayuda Recycler
    ArrayList<Solicitud> arrayListSolicitudes;
    Solicitud solicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_amistad);
        conectarFirebase();
        cargarUsuario();
        //obtenerUsuarioActual(mAuth.getCurrentUser().getEmail());
        //Referencias
        btnEnviarSolicitud = findViewById(R.id.btnEnviarSolicitud);
        txtCodigoAmigo = findViewById(R.id.txtCodigoAmigo);
        recyclerSolicitudes = findViewById(R.id.recyclerSolicitudes);
        recyclerSolicitudes.setLayoutManager(new LinearLayoutManager(this));
        arrayListSolicitudes = new ArrayList<>();
        //cargarCodigo();
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
        if(reference == null){
            //Dialog
            Mensaje.errorMensaje(getApplicationContext(), "No se ha podido conectar con el servidor");
        }
    }

    public void solicitudAmistad(final String email){
        soliExist = false;
        reference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    usuario = data.getValue(Usuario.class);
                    if(usuario.getEmail().equals(email)){
                        //soliExist=true;
                        idAmigo = usuario.getId();
                    }
                }
                reference.child("Usuarios").child(idAmigo).child("Solicitudes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data : snapshot.getChildren()){
                            solicitudes = data.getValue(Solicitud.class);
                            if(solicitudes.getEmail().equals(mAuth.getCurrentUser().getEmail())){
                                soliExist = true;
                                Mensaje.warningMensaje(getApplicationContext(), "Este usuario ya tiene solicitud de amistad suya");
                            }
                        }
                        if(soliExist == false){
                            if(email.equals(mAuth.getCurrentUser().getEmail())){
                                Mensaje.errorMensaje(getApplicationContext(), "Ha ocurrido un error");
                            }else{
                                reference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Amigos").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        exist = false;
                                        for(DataSnapshot data : snapshot.getChildren()){
                                            amigo = data.getValue(Amigo.class);
                                            if(amigo.getEmail().equals(email)){
                                                exist = true;
                                                Mensaje.errorMensaje(getApplicationContext(), "Este usuario ya ha sido agregado a su lista de amigos");
                                            }
                                        }
                                        if(exist==false){
                                            if(txtCodigoAmigo.getText().toString().equals("")){
                                                txtCodigoAmigo.setError("Ingrese un correo");
                                            }else{
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
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
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

    public void cargarUsuario(){
        final String miId = mAuth.getCurrentUser().getUid();
        reference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    usuario = data.getValue(Usuario.class);
                    if (usuario.getId().equals(miId)){
                        String miID = usuario.getId();
                        String miNombre = usuario.getNombre();
                        String miApellido = usuario.getApellido();
                        String miEmail = usuario.getEmail();
                        String miPass = usuario.getPassword();
                        user = new Usuario(miID,miNombre,miApellido,miEmail,miPass);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}