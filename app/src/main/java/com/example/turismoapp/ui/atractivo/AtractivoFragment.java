package com.example.turismoapp.ui.atractivo;

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
import com.example.turismoapp.vistaInfoEstablecimiento;

public class AtractivoFragment extends Fragment {

    private MainViewModel mViewModel;
    public ListView ListAtractivos;
    public FirebaseGuardarLeer firebaseGuardarLeer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel =
                new ViewModelProvider(this).get(com.example.turismoapp.ui.atractivo.MainViewModel.class);
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        Relaciones(root);
        Eventos();
        return root;
    }

    public void Relaciones(View r){
        firebaseGuardarLeer = new FirebaseGuardarLeer();
        ListAtractivos = r.findViewById(R.id.ListaAtractivos);
        firebaseGuardarLeer.obtenerEstablecimiento(ListAtractivos,getContext(),"Establecimiento/Atractivos");
    }

    public void Eventos(){
        ListAtractivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Establecimiento item = (Establecimiento) ListAtractivos.getItemAtPosition(position);

                Intent intent = new Intent (getContext(), vistaInfoEstablecimiento.class);
                intent.putExtra("EXTRA_MESSAGE", item);
                intent.putExtra("vista", "atractivos");
                startActivity(intent);
            }
        });
    }

}