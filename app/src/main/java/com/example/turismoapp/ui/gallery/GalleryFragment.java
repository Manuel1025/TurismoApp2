package com.example.turismoapp.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.turismoapp.R;
import com.example.turismoapp.database.Establecimiento;
import com.example.turismoapp.database.FirebaseGuardarLeer;
import com.example.turismoapp.vistaInfoEstablecimiento;

public class GalleryFragment extends Fragment {
    private GalleryViewModel galleryViewModel;
    public ListView ListHoteles;
    public FirebaseGuardarLeer firebaseGuardarLeer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        Relaciones(root);
        Eventos();

        return root;
    }

    public void Relaciones(View r){
        firebaseGuardarLeer = new FirebaseGuardarLeer();
        ListHoteles = r.findViewById(R.id.ListaHoteles);
        firebaseGuardarLeer.obtenerEstablecimiento(ListHoteles,getContext(),"Establecimiento/Hoteles");
    }

    public void Eventos(){
        ListHoteles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Establecimiento item = (Establecimiento) ListHoteles.getItemAtPosition(position);

                Intent intent = new Intent (getContext(), vistaInfoEstablecimiento.class);
                intent.putExtra("EXTRA_MESSAGE", item);
                intent.putExtra("vista", "hoteles");
                startActivity(intent);
            }
        });
    }
}