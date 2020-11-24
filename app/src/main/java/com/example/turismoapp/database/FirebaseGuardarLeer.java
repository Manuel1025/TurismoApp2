package com.example.turismoapp.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.turismoapp.complementos.EstablecimientoAdapter;
import com.example.turismoapp.complementos.PublicacionAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirebaseGuardarLeer {
    private StorageReference mStorageRef;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public Usuario user;
    public String pEmail;
    public ArrayList<Publicacion> arrayListPub;
    public ArrayList<Establecimiento> arrayListEst;
    ArrayAdapter arrayAdapter;

    public FirebaseGuardarLeer(){
        user = new Usuario();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        arrayListPub = new ArrayList<Publicacion>();
        arrayListEst = new ArrayList<Establecimiento>();
    }

    public void nuevoUsuario(String email, String nombre) {//Guarda otros datos del usuario en bd
        String rFoto = "Ruta en Storage de la imagen de perfil";
        Usuario user = new Usuario(email, rFoto, nombre);
        myRef.child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }

    public void obtenerDatos(String uuid, TextView name) {
        String rFoto = "Ruta en Storage de la imagen de perfil";

        myRef.child("Usuarios").child(uuid).addValueEventListener(new ValueEventListener() {//mostramos el nombre en el menu
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.email=dataSnapshot.child("email").getValue().toString();
                user.rutaFoto=dataSnapshot.child("rutaFoto").getValue().toString();
                user.nombre=dataSnapshot.child("nombre").getValue().toString();
                name.setText(user.nombre);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAGLOG", "Error!", databaseError.toException());
            }
        });
    }

    public void nuevoPublicacion(String email,String nomb, String loc, ImageView img,Context c) {//Guarda publicacion del usuario
        String rFoto = "imagenes/publicacion/"+nombreImagen()+".jpg";
        Publicacion publicacion = new Publicacion(email, rFoto, nomb, 0, 0, loc);
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();

        mStorageRef.child(rFoto).putBytes(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(c,"Hubo un problema al guardar la foto",Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                guardarDatos(publicacion,c);
            }
        });
    }

    public void obtenerPublicaciones(ListView ListObjetos, Context c) {//Obtener las publicaciones
        String rFoto = "Ruta en Storage de la imagen de perfil";

        myRef.child("Publicaciones").addValueEventListener(new ValueEventListener() {//Extraemos todas las publicaciones
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pEmail="";
                arrayListPub.clear();
                //ArrayList<Publicacion> arrayList = new ArrayList<Publicacion>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    arrayListPub.add(new Publicacion(postSnapshot.child("email").getValue().toString()
                                    , postSnapshot.child("rutaFoto").getValue().toString()
                                    , postSnapshot.child("nombre").getValue().toString()
                                    , Float.parseFloat(postSnapshot.child("coordenadaX").getValue().toString())
                                    , Float.parseFloat(postSnapshot.child("coordenadaY").getValue().toString())
                                    , postSnapshot.child("fecha").getValue().toString()
                                    , postSnapshot.child("hora").getValue().toString()
                                    , postSnapshot.child("localizacion").getValue().toString()));
                }

                /*for (int i = arrayList.size(); i-1>=0; i--){
                    arrayListPub.add(arrayList.get(i));
                }*/
                arrayAdapter = new PublicacionAdapter(c, arrayListPub);
                ListObjetos.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAGLOG", "Error!", databaseError.toException());
            }
        });
    }

    public void obtenerEstablecimiento(ListView ListObjetos, Context c, String ruta) {//Obtener los Establecimientos
        String rFoto = "Ruta en Storage de la imagen de perfil";

        myRef.child(ruta).addValueEventListener(new ValueEventListener() {//Extraemos todas los Establecimientos
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pEmail="";
                arrayListEst.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    arrayListEst.add(new Establecimiento(postSnapshot.child("titulo").getValue().toString()
                            , postSnapshot.child("localizacion").getValue().toString()
                            , postSnapshot.child("descripcion").getValue().toString()
                            , postSnapshot.child("vNoche").getValue().toString()
                            , postSnapshot.child("telefono").getValue().toString()
                            , postSnapshot.child("email").getValue().toString()
                            , postSnapshot.child("rfoto").getValue().toString()));
                }

                arrayAdapter = new EstablecimientoAdapter(c, arrayListEst);
                ListObjetos.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAGLOG", "Error!", databaseError.toException());
            }
        });
    }


    public void guardarDatos(Publicacion p, Context c){
        myRef.child("Publicaciones").push().setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(c,"Se ha guardado la publicacion",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c,"Hubo un problema al crear la publicacion",Toast.LENGTH_LONG).show();
            }
        });
    }

    public String nombreImagen(){
        return UUID.randomUUID().toString();
    }
}
