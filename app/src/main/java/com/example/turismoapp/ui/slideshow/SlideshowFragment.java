package com.example.turismoapp.ui.slideshow;

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

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    public ListView ListCabanias;
    public FirebaseGuardarLeer firebaseGuardarLeer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        Relaciones(root);
        Eventos();
        return root;
    }

    public void Relaciones(View r){
        firebaseGuardarLeer = new FirebaseGuardarLeer();
        ListCabanias = r.findViewById(R.id.ListaCabanias);
        firebaseGuardarLeer.obtenerEstablecimiento(ListCabanias,getContext(),"Establecimiento/Cabanias");
    }

    public void Eventos(){
        ListCabanias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Establecimiento item = (Establecimiento) ListCabanias.getItemAtPosition(position);

                Intent intent = new Intent (getContext(), vistaInfoEstablecimiento.class);
                intent.putExtra("EXTRA_MESSAGE", item);
                intent.putExtra("vista", "cabanias");
                startActivity(intent);
            }
        });
    }

}