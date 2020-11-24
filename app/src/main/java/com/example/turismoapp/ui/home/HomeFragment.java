package com.example.turismoapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turismoapp.R;
import com.example.turismoapp.database.FirebaseGuardarLeer;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public FirebaseGuardarLeer firebaseGuardarLeer;
    public static ListView ListPublicaciones;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Relaciones(root);
        Eventos();

        return root;
    }

    public void Relaciones(View r){
        firebaseGuardarLeer = new FirebaseGuardarLeer();

        ListPublicaciones = r.findViewById(R.id.ListaCabanias);
        firebaseGuardarLeer.obtenerPublicaciones(ListPublicaciones,getContext());
    }

    public void Eventos(){
        /*guardarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseGuardarLeer.nuevoPublicacion(cUser.getEmail(),"Ruta de la foto a cargar");
                Toast.makeText(getApplicationContext(),"Publicacion guardada",Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}