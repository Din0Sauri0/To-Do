package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Adapters.notasCompartidasAdapter;
import com.ovalle.to_do.Adapters.tareasAdapter;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Tarea;

import java.util.ArrayList;

public class verNotasCompartidas extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private RecyclerView recyclerNotasCompartidas;
    //Ayuda
    ArrayList<Tarea> arrayListTareaCompartidas;
    Tarea tarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notas_compartidas);
        recyclerNotasCompartidas = findViewById(R.id.recyclerNotasCompartidas);
        //Recycler
        recyclerNotasCompartidas.setLayoutManager(new LinearLayoutManager(this));
        arrayListTareaCompartidas = new ArrayList<>();
        conectarFirebase();
        cargarRecycler();
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

    public void cargarRecycler(){
        reference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("NotasCompartidas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListTareaCompartidas.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    tarea = data.getValue(Tarea.class);
                    arrayListTareaCompartidas.add(tarea);
                }
                notasCompartidasAdapter adapter = new notasCompartidasAdapter(arrayListTareaCompartidas);
                recyclerNotasCompartidas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}