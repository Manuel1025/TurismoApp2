package com.example.turismoapp.database;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@IgnoreExtraProperties
public class Publicacion {
    public String email;
    public String rutaFoto;
    public String nombre;
    public String localizacion;
    public float coordenadaX;
    public float coordenadaY;
    public String fecha;
    public String hora;

    public Publicacion() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Publicacion(String email, String rFoto, String nombre, float X, float Y,String loc) {
        this.email = email;
        this.rutaFoto = rFoto;
        this.nombre = nombre;
        this.coordenadaX = X;
        this.coordenadaY = Y;
        this.fecha = fechaActual();
        this.hora = horaActual();
        this.localizacion = loc;
    }

    public Publicacion(String email, String rFoto, String nombre, float X, float Y, String f, String h, String loc) {
        this.email = email;
        this.rutaFoto = rFoto;
        this.nombre = nombre;
        this.coordenadaX = X;
        this.coordenadaY = Y;
        this.fecha = f;
        this.hora = h;
        this.localizacion = loc;
    }

    public String fechaActual(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public String horaActual(){
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        return hourFormat.format(date).substring(0,5);
    }
}
