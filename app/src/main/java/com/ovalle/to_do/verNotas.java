package com.ovalle.to_do;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class verNotas extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private TextView txtTituloNota, txtDescripNota, txtCurpoNota;
    //Variables
    String nombreNota;
    String descripcionNota;
    String cuerpoNota;
    String idNota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notas);
        conectarFirebase();
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

    }


    public void conectarFirebase() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        if (reference != null) {
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
        }

    }

}