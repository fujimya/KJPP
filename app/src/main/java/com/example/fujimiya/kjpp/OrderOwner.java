package com.example.fujimiya.kjpp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

public class OrderOwner extends AppCompatActivity {
    private ActionBar actionBar;

    private ArrayList<String> NoOrder =  new ArrayList<String>();
    private ArrayList<String> NamaPerusahaan = new ArrayList<String>();
    private ArrayList<String> TanggalPenawaran = new ArrayList<String>();
    private ArrayList<String> Status = new ArrayList<String>();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("DATA ORDER");

        rvView = findViewById(R.id.rv_main_barang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(OrderOwner.this, 1));

        for(int a = 0; a < 10; a++){
            NoOrder.add("1234567Asdfgh");
            NamaPerusahaan.add("Ini Isi Nama Perusahaan Yang Order");
            TanggalPenawaran.add("DD-MM-YYYY");
            Status.add("apa");

        }

        adapter = new AdapterOrder(OrderOwner.this,NoOrder,NamaPerusahaan,TanggalPenawaran,Status);
        rvView.setAdapter(adapter);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
