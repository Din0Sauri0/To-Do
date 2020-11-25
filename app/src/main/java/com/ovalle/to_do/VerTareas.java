package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Adapters.tareasAdapter;
import com.ovalle.to_do.entidades.Tarea;

import java.util.ArrayList;

public class VerTareas extends AppCompatActivity {
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private RecyclerView tareasRecycler;
    //Ayuda recycler
    ArrayList<Tarea> arrayListTarea;
    Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tareas);
        //Referencias
        tareasRecycler = findViewById(R.id.tareasRecycler);
        //Configurar recycler
        tareasRecycler.setLayoutManager(new LinearLayoutManager(this));
        arrayListTarea = new ArrayList<>();
        conectarFirebase();
        cargarRecycler();
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

    public void cargarRecycler(){
        reference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("Tareas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListTarea.clear();
                for (DataSnapshot dato : snapshot.getChildren()) {
                    tarea = dato.getValue(Tarea.class);
                    arrayListTarea.add(tarea);
                }
                //Crear adaptador
                tareasAdapter adapter = new tareasAdapter(arrayListTarea);
                tareasRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}