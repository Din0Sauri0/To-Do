package com.ovalle.to_do;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;


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
        //conexion a BD
        conectarFirebase();
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
                String UserEmailLogin = txtEmail.getText().toString();
                String userPasswordLogin = txtPassword.getText().toString();
                loginUser(UserEmailLogin, userPasswordLogin);
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

    public void conectarFirebase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        if(reference != null){
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
        }
    }

    public void loginUser(String userEmail, String userPassword){
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this, Menu.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "No se ha podido iniciar sesion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this, Menu.class));
            finish();
        }
    }
}