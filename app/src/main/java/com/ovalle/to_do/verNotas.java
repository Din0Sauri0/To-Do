package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Adapters.tareasAdapter;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Amigo;
import com.ovalle.to_do.entidades.Tarea;

public class verNotas extends AppCompatActivity implements View.OnClickListener {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private TextView txtTituloNota, txtDescripNota, txtCurpoNota;
    private ImageButton btnCompartir, btnModificar, btnEliminar, btnGuardarModificaion;
    //Variables
    Amigo amigo;
    boolean estado;
    String idUsuario;
    String nombreNota;
    String descripcionNota;
    String cuerpoNota;
    String idNota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notas);
        conectarFirebase();
        idUsuario = mAuth.getCurrentUser().getUid();
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
        btnCompartir = findViewById(R.id.btnCompartir);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnGuardarModificaion = findViewById(R.id.btnGuardarModificaion);
        estado = false;
        desabilitarTxt(estado);

        btnModificar.setOnClickListener(this);
        btnCompartir.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);

    }

    private void desabilitarTxt(boolean estado) {
        txtCurpoNota.setEnabled(estado);
        txtTituloNota.setEnabled(estado);
        txtDescripNota.setEnabled(estado);
;
    }


    public void conectarFirebase() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        if (reference != null) {
            Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCompartir:
                //Mensaje.mensajeShort(getApplicationContext(), "Compartir");
                compartirNota(idNota);
                break;
            case R.id.btnEliminar:
                //Mensaje.errorMensaje(getApplicationContext(), "Eliminar");
                eliminar(idNota);

                break;
            case R.id.btnModificar:
                Mensaje.warningMensaje(getApplicationContext(),"Modificar");
                modificar();
                break;
            case R.id.btnGuardarModificaion:
                //Mensaje.mensajeShort(getApplicationContext(),"Guardar");
                guardarModificacion(idNota);
                estado = false;
                desabilitarTxt(estado);
                btnGuardarModificaion.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void eliminar(final String id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("¿Esta seguro que desea eliminar la nota?");
        dialog.setTitle("Eliminar");
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Mensaje.mensajeShort(getApplicationContext(), "Has presionado si");
                reference.child("Usuarios").child(idUsuario).child("Tareas").child(id).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Mensaje.warningMensaje(getApplicationContext(), "Se ha eliminado la nota");
                        startActivity(new Intent(verNotas.this, VerTareas.class));
                        finish();
                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mensaje.errorMensaje(getApplicationContext(), "Has presionado no");
            }
        });
        dialog.show();
    }

    public void modificar(){
        estado = true;
        desabilitarTxt(estado);
        btnGuardarModificaion.setVisibility(View.VISIBLE);
        btnGuardarModificaion.setOnClickListener(this);
    }
    public void guardarModificacion(String id){
        String newTitulo = txtTituloNota.getText().toString().trim();
        String newDescripcion = txtDescripNota.getText().toString().trim();
        String newCuerpo = txtCurpoNota.getText().toString().trim();
        Tarea newTarea = new Tarea(id,newTitulo,newDescripcion,newCuerpo);
        reference.child("Usuarios").child(idUsuario).child("Tareas").child(id).setValue(newTarea, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Mensaje.mensajeShort(getApplicationContext(), "La nota ha sido actualizada");
            }
        });
    }

    public void compartirNota(final String id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Ingrese el correo del usuario al desea compartir la nota");
        dialog.setTitle("Compartir");
        final EditText input = new EditText(this);
        dialog.setView(input);

        dialog.setPositiveButton("Compartir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                //Mensaje.mensajeShort(getApplicationContext(), "Has presionado si");
                final String correoShare = input.getText().toString();
                reference.child("Usuarios").child(idUsuario).child("Amigos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data: snapshot.getChildren()) {
                            amigo = data.getValue(Amigo.class);
                            if (amigo.getEmail().equals(correoShare)){
                                String idAmigo = amigo.getId();
                                Tarea tarea = new Tarea(idNota, nombreNota, descripcionNota,cuerpoNota);
                                reference.child("Usuarios").child(idAmigo).child("Notas compartidas").setValue(tarea, new DatabaseReference.CompletionListener() {
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
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mensaje.errorMensaje(getApplicationContext(), "Has presionado no");
            }
        });
        dialog.show();


    }



}