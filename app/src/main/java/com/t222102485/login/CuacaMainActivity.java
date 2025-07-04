package com.t222102485.login;

import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CuacaMainActivity extends AppCompatActivity {
    private RecyclerView _recyclerView2;
    private CuacaRootModel _rootModel;
    private SwipeRefreshLayout _swipeRefreshLayout2;
    private TextView _totalTextView;
    private TextView _buttonViewCityInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cuaca_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerView2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _recyclerView2 = findViewById(R.id.recyclerView2);
        _swipeRefreshLayout2 = findViewById(R.id.swipeRefreshLayout2);

        initSwipeRefreshLayout();
        initRecyclerView2();
    }

    private void initSwipeRefreshLayout() {

        _swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                initRecyclerView2();
            }
        });
    }
    private void initRecyclerView2()
    {
        _swipeRefreshLayout2.setRefreshing(true);

        String url = "https://api.openweathermap.org/data/2.5/forecast?id=1630789&appid=ebb1ea45bb4a5290b5ec666e79d39c99";
        AsyncHttpClient ahc = new AsyncHttpClient();

        ahc.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("*tw*", new String(responseBody));

                Gson gson = new Gson();
                CuacaRootModel rm = gson.fromJson(new String(responseBody), CuacaRootModel.class);

                RecyclerView.LayoutManager lm = new LinearLayoutManager(CuacaMainActivity.this);
                CuacaAdapter ca = new CuacaAdapter(CuacaMainActivity.this,rm);

                _recyclerView2.setLayoutManager(lm);
                _recyclerView2.setAdapter(ca);

                _swipeRefreshLayout2.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                _swipeRefreshLayout2.setRefreshing(false);
            }
        });
    }
}