package com.ovalle.to_do;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class task extends AppCompatActivity {
    private FloatingActionButton btnAddTask;
    private RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        btnAddTask = findViewById(R.id.btnAddTask);
        recycler = findViewById(R.id.recycler);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(task.this, registerTask.class);
                startActivity(intent);
            }
        });
    }
}