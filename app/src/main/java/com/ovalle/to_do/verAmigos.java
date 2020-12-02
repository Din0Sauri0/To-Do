package com.ovalle.to_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ovalle.to_do.Adapters.amigosAdapter;
import com.ovalle.to_do.Utilidades.Mensaje;
import com.ovalle.to_do.entidades.Amigo;
import com.ovalle.to_do.entidades.Usuario;

import java.util.ArrayList;

public class verAmigos extends AppCompatActivity {
    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    //Widget
    private RecyclerView recyclerAmigos;
    //Ayuda recycler
    ArrayList<Amigo> arrayListAmigo;
    Amigo amigo;
    Usuario user;
    String miId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_amigos);
        //Referencias
        recyclerAmigos = findViewById(R.id.recyclerAmigos);
        //Configurar recycler
        recyclerAmigos.setLayoutManager(new LinearLayoutManager(this));
        arrayListAmigo = new ArrayList<>();
        conectarFirebase();
        miId = mAuth.getCurrentUser().getUid();
        obtenerUsuarioActual(miId);
        cargarRecycler();
        //prueba recycler

        recyclerAmigos.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View view = rv.findChildViewUnder(e.getX(), e.getY());
                if(view != null){
                    int position = rv.getChildAdapterPosition(view);
                    amigo = arrayListAmigo.get(position);
                    Mensaje.mensajeShort(getApplicationContext(), amigo.getNombre());
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

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
    public void cargarRecycler(){
        reference.child("Usuarios").child(miId).child("Amigos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListAmigo.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    amigo = data.getValue(Amigo.class);
                    arrayListAmigo.add(amigo);
                }
                //Crear adaptador
                amigosAdapter adapter = new amigosAdapter(arrayListAmigo);
                recyclerAmigos.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void obtenerUsuarioActual(String idUsuario){
        reference.child("Usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id = snapshot.child("id").getValue().toString();
                String nombre = snapshot.child("nombre").getValue().toString();
                String apellido = snapshot.child("apellido").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String password = snapshot.child("password").getValue().toString();
                user = new Usuario(id, nombre, apellido, email, password);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}