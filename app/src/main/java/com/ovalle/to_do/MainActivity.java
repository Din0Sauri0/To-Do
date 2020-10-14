package com.ovalle.to_do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnRegistrer, btnlogin;
    private EditText txtEmail, txtPassword;
    private TextView txtForgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Enlaces con los widget
        btnRegistrer = findViewById(R.id.btnRegistrer);
        btnlogin = findViewById(R.id.btnlogin);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtForgetPassword = findViewById(R.id.txtForgetPassword);

        //Escuchadores.
        txtForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForget = new Intent(MainActivity.this, forgetPassword.class);
                startActivity(intentForget);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTask = new Intent(MainActivity.this, task.class);
                startActivity(intentTask);
            }
        });

        btnRegistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
    }

}