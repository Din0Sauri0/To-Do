package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ovalle.to_do.Utilidades.Mensaje;

public class forgetPassword extends AppCompatActivity {
    private EditText txtEmailForget;
    private Button btnEnviar, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        //Enlaces con los widget.
        txtEmailForget = findViewById(R.id.txtEmailForget);
        btnEnviar = findViewById(R.id.btnEnviar);
        btnCancelar = findViewById(R.id.btnCancelar);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Esta seccion aun no esta disponible");
        dialog.setTitle("Proximamente");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        dialog.show();

        //Escuchadores.
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCancelar = new Intent(forgetPassword.this, MainActivity.class);
                startActivity(intentCancelar);
            }
        });
    }
}