package com.example.turismoapp.complementos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.turismoapp.R;
import com.example.turismoapp.database.Establecimiento;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class EstablecimientoAdapter extends ArrayAdapter<Establecimiento> {
    public EstablecimientoAdapter(Context context, ArrayList<Establecimiento> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item_2,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView titulo = convertView.findViewById(R.id.itemTitulo);
        TextView localizacion = convertView.findViewById(R.id.itemLocalizacion);
        TextView descripcion = convertView.findViewById(R.id.itemDescrip);
        ImageView imagen = convertView.findViewById(R.id.itemImage);

        // publicacion actual.
        Establecimiento lead = getItem(position);

        FirebaseStorage.getInstance().getReference().child(lead.rFoto).getBytes((1024*1024)*3)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        //mImageView.setImageBitmap(bitmap);
                        //imagen.setImageBitmap(bitmap);
                        Glide.with(getContext()).asBitmap().load(bitmap).into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imagen.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
                    }
                });

        titulo.setText(lead.titulo);
        localizacion.setText(lead.localizacion);
        descripcion.setText(lead.descripcion);
        return convertView;
    }
}
