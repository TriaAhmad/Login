package com.t222102485.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class CuacaMainActivity extends AppCompatActivity {
    private EditText _etKota;
    private MaterialButton _btnTampilkan, _buttonViewCityInfo;
    private RecyclerView _recyclerView2;
    private RootModel _rootModel;
    private SwipeRefreshLayout _swipeRefreshLayout2;
    private TextView _totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_cuaca_main);

            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            _etKota = findViewById(R.id.etKota);
            _btnTampilkan = findViewById(R.id.btnTampilkan);
            _buttonViewCityInfo = findViewById(R.id.buttonView_cityInfo);
            _recyclerView2 = findViewById(R.id.recyclerView2);
            _totalTextView = findViewById(R.id.TotalTextView);
            _swipeRefreshLayout2 = findViewById(R.id.swipeRefreshLayout2);

            initSwipeRefreshLayout();
            initButtonViewCityInfo();
            initTampilkanButton();

            bindRecyclerView();

        } catch (Exception e) {
            // Menampilkan error di log dan sebagai Toast
            Log.e("CuacaMainActivity", "Error di onCreate: ", e);
            Toast.makeText(this, "Terjadi error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void bindRecyclerView() {
        String namaKota = _etKota.getText().toString().trim();
        if (namaKota.isEmpty()) {
            Toast.makeText(CuacaMainActivity.this, "Nama kota tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            namaKota = URLEncoder.encode(namaKota, "utf-8");
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        _buttonViewCityInfo.setText("Memuat...");

        String apiKey = "57b9194c73cbd5c2712202542cfd8b87";
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + namaKota + "&appid=" + apiKey + "&units=metric";

        AsyncHttpClient ahc = new AsyncHttpClient();
        ahc.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                _rootModel = gson.fromJson(new String(responseBody), RootModel.class);

                if (_rootModel == null || _rootModel.getListModelList() == null) {
                    Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_LONG).show();
                    return;
                }

                initCityInfo();

                RecyclerView.LayoutManager lm = new LinearLayoutManager(CuacaMainActivity.this);
                _recyclerView2.setLayoutManager(lm);

               CuacaAdapter ca = new CuacaAdapter(CuacaMainActivity.this,_rootModel.getListModelList());
                _recyclerView2.setAdapter(ca);

                _totalTextView.setText("Total Record : " + ca.getItemCount());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Gagal terhubung: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initButtonViewCityInfo() {
        _buttonViewCityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_rootModel == null || _rootModel.getCityModel() == null) {
                    Toast.makeText(getApplicationContext(), "Data kota belum tersedia", Toast.LENGTH_SHORT).show();
                    return;
                }

                CityModel cm = _rootModel.getCityModel();
                CoordModel com = cm.getCoordModel();

                Bundle param = new Bundle();
                param.putDouble("lat", com.getLat());
                param.putDouble("lon", com.getLon());

                Intent intent = new Intent(CuacaMainActivity.this, GPSActivity.class);
                intent.putExtra("param", param);
                startActivity(intent);
            }
        });
    }

    private void initCityInfo() {
        if (_rootModel == null || _rootModel.getCityModel() == null) return;

        CityModel cm = _rootModel.getCityModel();
        long sunrise = cm.getSunrise();
        long sunset = cm.getSunset();
        String cityName = cm.getName();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String sunriseTime = sdf.format(new Date(sunrise * 1000));
        String sunsetTime = sdf.format(new Date(sunset * 1000));

        String cityInfo = "Kota : " + cityName + "\n" +
                "Matahari Terbit: " + sunriseTime + " (Lokal)\n" +
                "Matahari Terbenam: " + sunsetTime + " (Lokal)";

        _buttonViewCityInfo.setText(cityInfo);
    }

    private void initSwipeRefreshLayout() {
        _swipeRefreshLayout2.setOnRefreshListener(() -> {
            bindRecyclerView();
            _swipeRefreshLayout2.setRefreshing(false);
        });
    }

    private void initTampilkanButton() {
        _btnTampilkan.setOnClickListener(v -> bindRecyclerView());
    }
}