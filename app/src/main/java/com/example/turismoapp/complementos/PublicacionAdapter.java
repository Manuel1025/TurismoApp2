package com.example.turismoapp.complementos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import com.example.turismoapp.database.Publicacion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class PublicacionAdapter extends ArrayAdapter<Publicacion> {
    public PublicacionAdapter(Context context, ArrayList<Publicacion> objects) {
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
                    R.layout.list_item,
                    parent,
                    false);
        }

        // Referencias UI.
        TextView name = convertView.findViewById(R.id.tvNombreItem);
        TextView fecha1 = convertView.findViewById(R.id.tvFechaItem);
        TextView loc = convertView.findViewById(R.id.tvLocalizacionItem);
        ImageView imagen = convertView.findViewById(R.id.imagenPublicacion);

        // publicacion actual.
        Publicacion lead = getItem(position);

        FirebaseStorage.getInstance().getReference().child(lead.rutaFoto).getBytes((1024*1024)*3)
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
        // Setup.
        //Glide.with(getContext()).load(lead.getmImage()).into(avatar);
        name.setText(lead.nombre);
        loc.setText(lead.localizacion);
        fecha1.setText(lead.fecha);
        return convertView;
    }
}
