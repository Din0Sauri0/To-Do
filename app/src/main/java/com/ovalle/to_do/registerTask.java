package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Tarea;

import java.util.UUID;

public class registerTask extends AppCompatActivity {
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private EditText txtNombreTarea, txtDescripcionTarea, txtTarea;
    private ImageButton btnRegistrarTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_task);
        conectarFirebase();
        //Enlace Widget
        txtNombreTarea = findViewById(R.id.txtNombreTarea);
        txtDescripcionTarea = findViewById(R.id.txtDescripcionTarea);
        txtTarea = findViewById(R.id.txtTarea);
        btnRegistrarTarea = findViewById(R.id.btnRegistrarTarea);
        //Escuchadores
        btnRegistrarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarTarea();

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

    public void registrarTarea(){
        if(txtNombreTarea.getText().toString().equals("")){
            txtNombreTarea.setError("Ingrese un titulo");
        }else if(txtDescripcionTarea.getText().toString().equals("")){
            txtDescripcionTarea.setError("Ingrese una descripcion");
        }else if(txtTarea.getText().toString().equals("")){
            Mensaje.warningMensaje(getApplicationContext(), "Ingrese el contenido de la nota");
        }else{
            String idTarea = UUID.randomUUID().toString();
            String nombreTarea = txtNombreTarea.getText().toString().trim();
            String descripcionTarea = txtDescripcionTarea.getText().toString().trim();
            String contenidoTarea = txtTarea.getText().toString().trim();
            Tarea tarea = new Tarea(idTarea,nombreTarea, descripcionTarea,contenidoTarea);
            reference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Tareas").child(tarea.getId()).setValue(tarea, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Mensaje.mensajeShort(getApplicationContext(), "La nota ha sido guardada");
                    limpiarCajas();
                }
            });
        }
    }

    public void limpiarCajas(){
        txtNombreTarea.setText(null);
        txtDescripcionTarea.setText(null);
        txtTarea.setText(null);
    }
}