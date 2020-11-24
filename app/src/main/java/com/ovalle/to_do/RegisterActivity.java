package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ovalle.to_do.entidades.Usuario;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private EditText txtName, txtLastName, txtEmail, txtPassword, txtConfirmPassword;
    private Button btnRegister, btnCancel;
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
        conectarFirebase();
        //Escuchadores
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailLogin = txtEmail.getText().toString();
                String passwordLogin = txtPassword.getText().toString();
                crearUsuario(emailLogin, passwordLogin);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCancel = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intentCancel);
            }
        });
    }

    //Metodos
    public void conectarFirebase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        if(reference != null){
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
        }
    }

    public void crearUsuario(String emailAuth, String passAuth){
        mAuth.createUserWithEmailAndPassword(emailAuth, passAuth).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuth.getCurrentUser().getUid();
                    String nombre = txtName.getText().toString();
                    String apellido = txtLastName.getText().toString();
                    String email = txtEmail.getText().toString();
                    String password = txtPassword.getText().toString();
                    Usuario usuario = new Usuario(id,nombre,apellido,email,password);
                    reference.child("Usuarios").child(id).setValue(usuario, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(getApplicationContext(),"El usuario ha sido creado exitosamente", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"No se ha podido registrar el usuario", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}