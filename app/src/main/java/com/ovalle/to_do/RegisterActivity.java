package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ovalle.to_do.BD.firebase;
import com.ovalle.to_do.entidades.Usuario;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private EditText txtName, txtLastName, txtEmail, txtPassword, txtConfirmPassword;
    private Button btnRegister, btnCancel;
    //firebase bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Enlaces con widget.
        txtName = findViewById(R.id.txtName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);
        //bd.conectarFirebase();
        conectarFirebase();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = UUID.randomUUID().toString();
                String nombre = txtName.getText().toString();
                String apellido = txtLastName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                Usuario usuario = new Usuario(id,nombre,apellido,email,password);

                crearUsuario(usuario);
            }
        });
        //Escuchadores.
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCancel = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intentCancel);
            }
        });



    }

    public void conectarFirebase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        if(reference != null){
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
        }
    }

    public void crearUsuario(Usuario user){
        reference.child("Usuarios").child(user.getId()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getApplicationContext(), "El usuario ha sido registrado", Toast.LENGTH_LONG).show();
            }
        });
    }
}