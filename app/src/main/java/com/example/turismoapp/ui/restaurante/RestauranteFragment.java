package com.example.turismoapp.ui.restaurante;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.turismoapp.R;
import com.example.turismoapp.database.Establecimiento;
import com.example.turismoapp.database.FirebaseGuardarLeer;
import com.example.turismoapp.ui.slideshow.SlideshowViewModel;
import com.example.turismoapp.vistaInfoEstablecimiento;

public class RestauranteFragment extends Fragment {

    private MainViewModel mViewModel;
    public ListView ListRestaurante;
    public FirebaseGuardarLeer firebaseGuardarLeer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.restaurante_fragment, container, false);
        Relaciones(root);
        Eventos();
        return root;
    }

    public void Relaciones(View r){
        firebaseGuardarLeer = new FirebaseGuardarLeer();
        ListRestaurante = r.findViewById(R.id.ListaRestaurantes);
        firebaseGuardarLeer.obtenerEstablecimiento(ListRestaurante,getContext(),"Establecimiento/Restaurante");
    }

    public void Eventos(){
        ListRestaurante.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Establecimiento item = (Establecimiento) ListRestaurante.getItemAtPosition(position);

                Intent intent = new Intent (getContext(), vistaInfoEstablecimiento.class);
                intent.putExtra("EXTRA_MESSAGE", item);
                intent.putExtra("vista", "restaurantes");
                startActivity(intent);
            }
        });
    }
}