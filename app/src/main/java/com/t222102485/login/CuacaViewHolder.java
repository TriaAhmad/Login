package com.t222102485.login;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CuacaViewHolder extends RecyclerView.ViewHolder{
    public ImageView cuacaImageView;
    public TextView namaTextView2, deskripsiTextView, tglWaktuTextView, suhuTextView, weatherEmojiView;

    public CuacaViewHolder(View itemView) {
        super(itemView);

        cuacaImageView = itemView.findViewById(R.id.cuacaImageView);
        namaTextView2 = itemView.findViewById(R.id.namaTextView2);
        deskripsiTextView = itemView.findViewById(R.id.deskripsiTextView);
        tglWaktuTextView = itemView.findViewById(R.id.tglWaktuTextView);
        suhuTextView = itemView.findViewById(R.id.suhuTextView);
        weatherEmojiView = itemView.findViewById(R.id.weatherEmojiView); //Tambahan ketika menggunakan emoji. yang sebelumnya tanpa menggunakn weatherEmojiView
    }
}
