 package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Amigo;
import com.ovalle.to_do.entidades.Tarea;

public class verNotaCom extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    private EditText txtTituloCom, txtDescripCom, txtCuerpoCom;
    private ImageButton btnEliminarCom, btnEditarCom, btnCompartirCom, btnGuardarModiCom;

    boolean exist;
    boolean estadoTxt;
    String idUsuario;
    String nombreNota;
    String descripcionNota;
    String cuerpoNota;
    String idNota;

    Amigo amigo;
    Amigo amigoEncontrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_nota_com);
        conectarFirebase();
        //Widget
        txtTituloCom = findViewById(R.id.txtTituloCom);
        txtDescripCom = findViewById(R.id.txtDescripCom);
        txtCuerpoCom = findViewById(R.id.txtCuerpoCom);

        btnEliminarCom = findViewById(R.id.btnEliminarCom);
        btnEditarCom = findViewById(R.id.btnEditarCom);
        btnCompartirCom = findViewById(R.id.btnCompartirCom);
        btnGuardarModiCom = findViewById(R.id.btnGuardarModiCom);

        idUsuario = mAuth.getCurrentUser().getUid();
        nombreNota = getIntent().getStringExtra("Nombre nota");
        descripcionNota = getIntent().getStringExtra("Descripcion nota");
        cuerpoNota = getIntent().getStringExtra("Cuerpo nota");
        idNota = getIntent().getStringExtra("Id nota");

        txtTituloCom.setText(nombreNota);
        txtDescripCom.setText(descripcionNota);
        txtCuerpoCom.setText(cuerpoNota);

        btnEliminarCom.setOnClickListener(this);
        btnEditarCom.setOnClickListener(this);
        btnCompartirCom.setOnClickListener(this);
        btnGuardarModiCom.setOnClickListener(this);

        estadoTxt = false;

        desabilitarTxt(estadoTxt);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEliminarCom:
                //Mensaje.errorMensaje(getApplicationContext(), "has eliminado");
                eliminar(idNota);
                break;
            case R.id.btnEditarCom:
                editar();
                break;
            case R.id.btnCompartirCom:
                //Mensaje.mensajeShort(getApplicationContext(), "compartir");
                compartirNota(idNota);
                break;
            case R.id.btnGuardarModiCom:
                guardarModificacion(idNota);
                estadoTxt=false;
                desabilitarTxt(estadoTxt);
                btnGuardarModiCom.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void compartirNota(final String idNota) {
        exist = false;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Ingrese el correo del usuario al que desee compartir la nota");
        dialog.setTitle("Compartir");
        final EditText input = new EditText(this);
        dialog.setView(input);
        dialog.setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final String correoShare = input.getText().toString().trim();
                reference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Amigos").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data:snapshot.getChildren()){
                            amigoEncontrado = data.getValue(Amigo.class);
                            if(amigoEncontrado.getEmail().equals(correoShare)){
                                exist=true;
                                reference.child("Usuarios").child(idUsuario).child("Amigos").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot data: snapshot.getChildren()) {
                                            amigo = data.getValue(Amigo.class);
                                            if (amigo.getEmail().equals(correoShare)){
                                                String idAmigo = amigo.getId();
                                                Tarea tarea = new Tarea(idNota, nombreNota, descripcionNota,cuerpoNota);
                                                reference.child("Usuarios").child(idAmigo).child("NotasCompartidas").child(idNota).setValue(tarea, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        dialog.dismiss();
                                                        Mensaje.mensajeShort(getApplicationContext(), "Se ha compartido la nota con: "+correoShare);
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        if (exist==false || correoShare.equals("")){
                            dialog.dismiss();
                            Mensaje.errorMensaje(getApplicationContext(), "No se ha encontrado este usuario en sus amigos");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void guardarModificacion(String id) {
        String newTitulo = txtTituloCom.getText().toString().trim();
        String newDecripcion = txtDescripCom.getText().toString().trim();
        String newCuerpo = txtCuerpoCom.getText().toString().trim();
        Tarea tareaModificada = new Tarea(id, newTitulo, newDecripcion, newCuerpo);
        reference.child("Usuarios").child(idUsuario).child("NotasCompartidas").child(id).setValue(tareaModificada, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Mensaje.mensajeShort(getApplicationContext(), "La nota ha sido actualizada");
            }
        });
    }

    private void editar() {
        estadoTxt=true;
        desabilitarTxt(estadoTxt);
        btnGuardarModiCom.setVisibility(View.VISIBLE);
    }

    private void eliminar(final String id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Eliminar");
        dialog.setMessage("¿Esta seguro que desea eliminar la nota?");
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reference.child("Usuarios").child(idUsuario).child("NotasCompartidas").child(id).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Mensaje.warningMensaje(getApplicationContext(), "Se ha eliminado la nota");
                        startActivity(new Intent(getApplicationContext(), verNotasCompartidas.class));
                        finish();
                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
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

    private void desabilitarTxt(boolean estadoTxt) {
        txtTituloCom.setEnabled(estadoTxt);
        txtDescripCom.setEnabled(estadoTxt);
        txtCuerpoCom.setEnabled(estadoTxt);
        btnGuardarModiCom.setOnClickListener(this);
    }


}