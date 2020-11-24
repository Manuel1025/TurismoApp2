package com.example.turismoapp.database;

import android.widget.ImageView;

import java.io.Serializable;

public class Establecimiento implements Serializable {
    public String titulo;
    public String localizacion;
    public String descripcion;
    public String vNoche;
    public String telefono;
    public String email;
    public String rFoto;


    public Establecimiento(String tit, String loc, String desc, String vNoc, String tel, String ema, String rfoto){
        this.titulo = tit;
        this.localizacion = loc;
        this.descripcion = desc;
        this.vNoche = vNoc;
        this.telefono = tel;
        this.email = ema;
        this.rFoto = rfoto;
    }
}