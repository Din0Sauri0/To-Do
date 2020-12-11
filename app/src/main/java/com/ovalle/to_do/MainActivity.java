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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Usuario;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private Button btnRegistrer, btnlogin;
    private EditText txtEmail, txtPassword;
    private TextView txtForgetPassword;
    //Variables
    Usuario usuaio;
    boolean existEmail = false;
    boolean existPass = false;
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
                String UserEmailLogin = txtEmail.getText().toString().trim();
                String userPasswordLogin = txtPassword.getText().toString().trim();
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
        if(reference == null){
            //Dialog
            Mensaje.errorMensaje(getApplicationContext(), "No se ha podido conectar con el servidor");
        }
    }

    public void loginUser(final String userEmail, final String userPassword){
        if(userEmail.equals("") || userPassword.equals("")){
            Mensaje.warningMensaje(getApplicationContext(), "Ingrese todas las credenciales.");

        }else{
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(MainActivity.this, Menu.class));
                        finish();
                    }else{
                        reference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data: snapshot.getChildren()) {
                                    usuaio = data.getValue(Usuario.class);
                                    if(usuaio.getEmail().equals(userEmail)){
                                        existEmail = true;
                                        if(!usuaio.getPassword().equals(userPassword)){
                                            Mensaje.errorMensaje(getApplicationContext(), "La password no es correcta");
                                        }
                                    }
                                }
                                if(existEmail != true){
                                    Mensaje.warningMensaje(getApplicationContext(), "El correo no existe");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });
        }

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