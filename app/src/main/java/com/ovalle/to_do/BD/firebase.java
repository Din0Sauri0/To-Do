package com.ovalle.to_do.BD;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class firebase {
    public FirebaseDatabase database;
    public DatabaseReference reference;

    public void conectarFirebase(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }


}
