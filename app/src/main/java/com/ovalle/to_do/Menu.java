package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private Button btnCerrarSesion, btnAgregarTarea, btnVerTarea, btnAgregarAmigos;
    private TextView txtNombreBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        conectarFirebase();
        obtenerUsuario();
        //Referencias
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea);
        txtNombreBienvenida = findViewById(R.id.txtNombreBienvenida);
        btnVerTarea = findViewById(R.id.btnVerTarea);
        btnAgregarAmigos = findViewById(R.id.btnAgregarAmigos);
        //Escuchadores
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
        btnAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, registerTask.class));
            }
        });
        btnVerTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, VerTareas.class));
            }
        });
        btnAgregarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SolicitudesAmistad.class));
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

    public void cerrarSesion(){
        mAuth.signOut();
        startActivity(new Intent(Menu.this, MainActivity.class));
        finish();
    }

    public void obtenerUsuario(){
        String id = mAuth.getCurrentUser().getUid();
        reference.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    String nombreBienvenida = snapshot.child("nombre").getValue().toString();
                    String apellidoBienvenida = snapshot.child("apellido").getValue().toString();
                    txtNombreBienvenida.setText(nombreBienvenida+" "+apellidoBienvenida);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}