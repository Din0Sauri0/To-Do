package com.ovalle.to_do;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // creando las variables que aran referencia a los widget (aspectos visuales de la aplicacion)
    // crear estas variables en el metodo mainactivity para que asi todos los metodos creados en este puedan acceder a las variables
    private TextView txtTitulo;
    private Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //referencias para unir variable con los aspectos visuales
        txtTitulo = findViewById(R.id.txtTitulo); // este metodo hace referencia al aspecto visual
        btnIniciar = findViewById(R.id.btnIniciar);

        //txtTitulo.setTextColor(Color.BLACK); //este metodo me permite cambiar el color de un texto
    }
}