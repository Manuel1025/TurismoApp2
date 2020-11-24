package com.example.turismoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarPassword extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public EditText email;
    public ProgressBar barraProg;
    public Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);
        Relaciones();
        Eventos();
    }

    public void Relaciones(){
        email = findViewById(R.id.userEmailRecup);
        barraProg = findViewById(R.id.progressBar3);
        btnLogin = findViewById(R.id.btnRecuperarCuenta);
        mAuth = FirebaseAuth.getInstance();
    }

    public void Eventos(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarCampos();
            }
        });
    }

    public void ValidarCampos(){//Valida los campos si estan vacios o no cumplen otros criterios
        String email = this.email.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            this.email.setError("El email es requerido");
            return;
        }

        barraProg.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){//redirige a la pantalla de Login
                    Toast.makeText(getApplicationContext(),"Se ha enviado a tu correo un link de recuperacion",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }else {//envia mensaje de error
                    Toast.makeText(getApplicationContext(),"Error ! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    barraProg.setVisibility(View.GONE);
                }
            }
        });
    }
}