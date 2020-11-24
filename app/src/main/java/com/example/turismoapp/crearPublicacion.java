package com.example.turismoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoapp.database.FirebaseGuardarLeer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class crearPublicacion extends AppCompatActivity {
    public Button btnFoto;
    public Button btnBuscar;
    public Button btnSubir;
    public ImageView Imagen;
    public EditText Localizacion;
    private Uri mImageCaptureUri;
    public String imageUrl;
    private ContentValues values;
    private Bitmap thumbnail;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int CAMERA_PIC_REQUEST = 1337;
    public FirebaseGuardarLeer firebaseGuardarLeer;
    public String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_publicacion);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!checkExternalStoragePermission()){
                return;
            }
        }
        Relaciones();
        Eventos();
    }

    private boolean checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            Log.i("Mensaje","No tiene permiso para leer");
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},225);
        }else{
            Log.i("Mensaje","Se tiene permiso para leer");
            return true;
        }
        return false;
    }

    public void Relaciones(){
        Intent intent = getIntent();

        btnFoto = findViewById(R.id.btnFoto);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnSubir = findViewById(R.id.btnSubir);
        Imagen = findViewById(R.id.imgCargada);
        Localizacion = findViewById(R.id.tvLocalizacionPub);

        nombre = intent.getStringExtra("nombre");
        firebaseGuardarLeer = new FirebaseGuardarLeer();
    }

    public void Eventos(){
        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE,"MyPicture");
                values.put(MediaStore.Images.Media.DESCRIPTION,"Photo taken on "+System.currentTimeMillis());
                mImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
                //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                //}
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"), CAMERA_PIC_REQUEST);
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Imagen.getDrawable()!=null){
                    if (!Localizacion.getText().toString().equalsIgnoreCase("")){
                        firebaseGuardarLeer.nuevoPublicacion(FirebaseAuth.getInstance().getCurrentUser().getEmail(),nombre
                                ,Localizacion.getText().toString(),Imagen,getApplicationContext());
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"No ingresado una localizacion",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"No se ha cargado una imagen",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            mImageCaptureUri = data.getData();
            Imagen.setImageURI(mImageCaptureUri);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(),mImageCaptureUri);
                Imagen.setImageBitmap(thumbnail);
                imageUrl = getRealPathFromURI(mImageCaptureUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri,proj,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}