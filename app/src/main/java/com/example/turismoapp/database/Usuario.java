package com.example.turismoapp.database;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario {
    public String email;
    public String rutaFoto;
    public String nombre;

    public Usuario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Usuario(String email, String rFoto, String nombre) {
        this.email = email;
        this.rutaFoto = rFoto;
        this.nombre = nombre;
    }
}
