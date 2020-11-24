package com.example.turismoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.turismoapp.database.Establecimiento;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

public class vistaInfoEstablecimiento extends AppCompatActivity {

    private TextView titulo;
    private ImageView image;
    private TextView localizacion;
    private TextView descripcion;
    private TextView valor;
    private TextView numero;
    private TextView email;
    public Establecimiento message;
    public String vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_info_establecimiento);

        Intent intent = getIntent();
        message = (Establecimiento) intent.getSerializableExtra("EXTRA_MESSAGE");
        vista =  intent.getStringExtra("vista");
        Relaciones();
    }

    public void Relaciones(){
        titulo = findViewById(R.id.tvTituloVInfo);
        image = findViewById(R.id.imageVInfo);
        localizacion = findViewById(R.id.tvLocalizacionVinfo);
        descripcion = findViewById(R.id.tvDescripcionVinfo);
        valor = findViewById(R.id.tvValorVinfo);
        numero = findViewById(R.id.tvNumeroVinfo);
        email = findViewById(R.id.tvEmailVinfo);

        titulo.setText(message.titulo);
        localizacion.setText(message.localizacion);
        descripcion.setText(message.descripcion);
        valor.setText("Valor: "+message.vNoche);
        numero.setText("Numero contacto: "+message.telefono);
        email.setText("Email de contacto: "+message.email);

        if (vista.equalsIgnoreCase("restaurantes")){
            valor.setVisibility(View.GONE);
        }else if(vista.equalsIgnoreCase("atractivos")){
            valor.setVisibility(View.GONE);
            numero.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
        }else{
            valor.setVisibility(View.VISIBLE);
            numero.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
        }
        Imagen(message.rFoto);
    }

    public void Imagen(String ruta){
        FirebaseStorage.getInstance().getReference().child(ruta).getBytes((1024*1024)*3)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        //mImageView.setImageBitmap(bitmap);
                        //imagen.setImageBitmap(bitmap);
                        Glide.with(getApplicationContext()).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                image.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                    }
                });
    }
}