package com.t222102485.login;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CuacaAdapter extends RecyclerView.Adapter<CuacaViewHolder>{
    private final List<ListModel> cuacaList;
    private final Context context;

    public CuacaAdapter(Context context, List<ListModel> cuacaList) {
        this.context = context;
        this.cuacaList = cuacaList;
    }

    @Override
    public CuacaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_cuaca, parent, false);
        return new CuacaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CuacaViewHolder holder, int position) {
        ListModel cuaca = cuacaList.get(position);

        double suhuMin = cuaca.getMainModel().getTemp_min();
        double suhuMax = cuaca.getMainModel().getTemp_max();
        String tanggalWaktu = cuaca.getDt_txt();

        if (cuaca.getWeatherModelList() != null && !cuaca.getWeatherModelList().isEmpty()) {
            WeatherModel weather = cuaca.getWeatherModelList().get(0);
            holder.namaTextView2.setText(weather.getMain());
            holder.deskripsiTextView.setText(weather.getDescription());

            String iconCode = weather.getIcon();
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

            // Tampilkan gambar dari internet dengan Picasso
            holder.weatherEmojiView.setVisibility(View.GONE);
            holder.cuacaImageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.ic_launcher_background) // Gambar lokal
                    .error(R.drawable.ic_launcher_foreground)             // Gambar lokal jika gagal
                    .into(holder.cuacaImageView);
        }

        String suhuText = String.format("Min: %.0f°C / Max: %.0f°C", suhuMin, suhuMax);
        holder.suhuTextView.setText(suhuText);
        holder.tglWaktuTextView.setText(tanggalWaktu);
    }

    @Override
    public int getItemCount() {
        return cuacaList.size();
    }
}
