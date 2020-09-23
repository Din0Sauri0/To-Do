package com.ovalle.to_do;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //animaciones

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        TextView txtTodo = findViewById(R.id.txtTodo);
        ImageView imgToDo = findViewById(R.id.imgTodo);

        imgToDo.setAnimation(desplazamiento_arriba);
        txtTodo.setAnimation(desplazamiento_abajo);

    }
}