package com.example.turismoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    public EditText email;
    public EditText passw;
    public TextView registrar;
    public TextView forgotPass;
    public ProgressBar barraProg;
    public Button btnLogin;
    public Button btnGoogle;
    public GoogleSignInClient mGoogleSignInClient;
    public GoogleApiClient googleApiClient;

    public BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = manager.getActiveNetworkInfo();
            onNetworkChange(ni);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Relaciones();
        Eventos();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            LogIn();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    public void onNetworkChange(NetworkInfo networkInfo) {
        boolean isConnected = networkInfo != null &&
                networkInfo.isConnectedOrConnecting();
        if (isConnected) {
            Toast.makeText(getApplicationContext(),"Conectado a internet",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),"Sin conexion a internet",Toast.LENGTH_LONG).show();
        }
    }

    public void Relaciones(){
        email = findViewById(R.id.userEmailLog);
        passw = findViewById(R.id.passLog);
        registrar = findViewById(R.id.tvCrearCuenta);
        barraProg = findViewById(R.id.progressBar2);
        btnLogin = findViewById(R.id.btnIniciarSesion);
        forgotPass = findViewById(R.id.tvOlvideContra);
        btnGoogle = findViewById(R.id.googleButton);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),InicioMenu.class));
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
    }

    public void Eventos(){
        registrar.setOnClickListener(new View.OnClickListener() {//Redirige a la vista de registro
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Registro.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {//Click al boton para iniciar sesion
            @Override
            public void onClick(View v) {
                    ValidarCampos();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {//recuperar contraseña
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RecuperarPassword.class));
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {//Click al boton para iniciar sesion con google
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.googleButton:
                        signIn();
                        break;
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 123 ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            LogIn();
                        } else {
                            Toast.makeText(getApplicationContext(),"Authenticacion fallada",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(getApplicationContext(),"Login google completado",Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(MainActivity.this,Inicio.class);
            //startActivity(intent);
        }catch(ApiException e){
            Toast.makeText(getApplicationContext(),"Login fallado "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void ValidarCampos(){//Valida los campos si estan vacios o no cumplen otros criterios
        String email = this.email.getText().toString().trim();
        String passw = this.passw.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            this.email.setError("El email es requerido");
            return;
        }

        if(TextUtils.isEmpty(passw)){
            this.passw.setError("La contraseña es requerida");
            return;
        }

        barraProg.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        mAuth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {//verifica si existe usuario y contraseña
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){//redirige a la pantalla de inicio
                    Toast.makeText(getApplicationContext(),"Login completado",Toast.LENGTH_LONG).show();
                    LogIn();
                }else {//envia mensaje de error
                    Toast.makeText(getApplicationContext(),"Error ! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    barraProg.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                }
            }
        });
    }

    public void LogIn(){
        startActivity(new Intent(getApplicationContext(),InicioMenu.class));
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}