package com.ovalle.to_do.Utilidades;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class Mensaje {


    public static void mensaje(Context context, String mensaje){
        Toasty.success(context, mensaje, Toast.LENGTH_LONG,true).show();
    }
    public static  void errorMensaje(Context context, String mensaje){
        Toasty.error(context, mensaje, Toast.LENGTH_LONG,true).show();
    }
    public static void warningMensaje(Context context, String mensaje){
        Toasty.warning(context, mensaje, Toast.LENGTH_LONG,true).show();
    }
}

