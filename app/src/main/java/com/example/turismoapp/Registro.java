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
import android.widget.TextView;
import android.widget.Toast;

import com.example.turismoapp.database.FirebaseGuardarLeer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public EditText name;
    public EditText email;
    public EditText passw;
    public EditText passwConf;
    public Button btnReg;
    public TextView regLog;
    public ProgressBar status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Relaciones();
        Eventos();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void Relaciones(){
        name = findViewById(R.id.nombre);
        email = findViewById(R.id.userEmailReg);
        passw = findViewById(R.id.passReg);
        passwConf = findViewById(R.id.passRegConf);
        btnReg = findViewById(R.id.btnRegistrar);
        regLog = findViewById(R.id.regresarLog);
        status = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
    }

    public void Eventos(){
        regLog.setOnClickListener(new View.OnClickListener() {//Redirige a la vista de Login
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {//Registrar al usuario
            @Override
            public void onClick(View v) {
                ValidarCamposVacios();
            }
        });
    }

    public void ValidarCamposVacios(){
        String name = this.name.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String passw = this.passw.getText().toString().trim();
        String passwConf = this.passwConf.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            this.name.setError("El nombre es requerido");
            return;
        }

        if(TextUtils.isEmpty(email)){
            this.email.setError("El email es requerido");
            return;
        }

        if(TextUtils.isEmpty(passw)){
            this.passw.setError("La contraseña es requerida");
            return;
        }

        if(TextUtils.isEmpty(passwConf)){
            this.passwConf.setError("La contraseña es requerida");
            return;
        }

        if(passw.length() <= 6){
            this.passw.setError("La contraseña debe contener mas de 6 caracteres");
            return;
        }

        if(!passw.equals(passwConf)){
            this.passwConf.setError("La contraseña no es igual");
            return;
        }

        status.setVisibility(View.VISIBLE);
        this.mAuth.createUserWithEmailAndPassword(email,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseGuardarLeer firebaseGuardarLeer = new FirebaseGuardarLeer();
                    firebaseGuardarLeer.nuevoUsuario(email, name);
                        Toast.makeText(getApplicationContext(),"Usuario registrado",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else {
                    Toast.makeText(getApplicationContext(),"Error ! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    status.setVisibility(View.GONE);
                }
            }
        });
    }

}