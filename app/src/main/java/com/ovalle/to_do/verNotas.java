package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ovalle.to_do.Utilidades.Mensaje;

public class verNotas extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private TextView txtTituloNota, txtDescripNota, txtCurpoNota;
    private ImageButton btnCompartir, btnModificar, btnEliminar;
    //Variables
    String idUsuario;
    String nombreNota;
    String descripcionNota;
    String cuerpoNota;
    String idNota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notas);
        conectarFirebase();
        idUsuario = mAuth.getCurrentUser().getUid();
        txtTituloNota = findViewById(R.id.txtTituloNota);
        txtDescripNota = findViewById(R.id.txtDescripNota);
        txtCurpoNota = findViewById(R.id.txtCuerpoNota);
        nombreNota = getIntent().getStringExtra("Nombre nota");
        descripcionNota = getIntent().getStringExtra("Descripcion nota");
        cuerpoNota = getIntent().getStringExtra("Cuerpo nota");
        idNota = getIntent().getStringExtra("Id nota");
        txtTituloNota.setText(nombreNota);
        txtDescripNota.setText(descripcionNota);
        txtCurpoNota.setText(cuerpoNota);
        btnCompartir = findViewById(R.id.btnCompartir);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);

        txtCurpoNota.setFocusable(false);
        txtCurpoNota.setCursorVisible(false);
        txtCurpoNota.setKeyListener(null);

        btnModificar.setOnClickListener(this);
        btnCompartir.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);

    }


    public void conectarFirebase() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        if (reference != null) {
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCompartir:
                Mensaje.mensajeShort(getApplicationContext(), "Compartir");
                break;
            case R.id.btnEliminar:
                //Mensaje.errorMensaje(getApplicationContext(), "Eliminar");
                eliminar(idNota);

                break;
            case R.id.btnModificar:
                Mensaje.warningMensaje(getApplicationContext(),"Modificar");
                break;
        }
    }

    public void eliminar(final String id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("¿Esta seguro que desea eliminar la nota?");
        dialog.setTitle("Eliminar");
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Mensaje.mensajeShort(getApplicationContext(), "Has presionado si");
                reference.child("Usuarios").child(idUsuario).child("Tareas").child(id).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        startActivity(new Intent(verNotas.this, VerTareas.class));
                        VerTareas ver = new VerTareas();
                        //Mensaje.warningMensaje(ver.getApplicationContext(), "Se ha eliminado la nota");
                        finish();
                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mensaje.errorMensaje(getApplicationContext(), "Has presionado no");
            }
        });
        dialog.show();
    }


}