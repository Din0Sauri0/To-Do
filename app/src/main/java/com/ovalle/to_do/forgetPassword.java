package com.ovalle.to_do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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