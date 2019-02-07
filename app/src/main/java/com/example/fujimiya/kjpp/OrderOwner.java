package com.example.fujimiya.kjpp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.fujimiya.kjpp.app.AppController;
import com.example.fujimiya.kjpp.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderOwner extends AppCompatActivity {
    private ActionBar actionBar;

    private ArrayList<String> NoOrder =  new ArrayList<String>();
    private ArrayList<String> NamaPerusahaan = new ArrayList<String>();
    private ArrayList<String> TanggalPenawaran = new ArrayList<String>();
    private ArrayList<String> Status = new ArrayList<String>();
    private ArrayList<String> Status_Pengerjaan = new ArrayList<String>();
    private ArrayList<String> Status_Order= new ArrayList<String>();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = OrderOwner.class.getSimpleName();

    private static String url_select 	 = Server.URL + "order/get_order.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "msg";

    public final static String TAG_ID_ORDER= "id_order";
    public final static String TAG_NO_ORDER = "no_order";
    public final static String TAG_NAMA_PT = "nama_pt";
    public final static String TAG_LOKASI_ASET = "lokasi_aset";
    public final static String TAG_TANGGAL_PENAWARAN= "tanggal_penawaran";
    public final static String TAG_DUEDATE= "duedate";
    public final static String TAG_NAMA_PROPERTY= "nama_property";
    public final static String TAG_FEE_PENAWARAN = "fee_penawaran";
    public final static String TAG_HARGA_DEAL= "harga_deal";
    public final static String TAG_STATUS_BAYAR = "status_bayar";
    public final static String TAG_STATUS_SELESAI = "status_pengerjaan";
    public final static String TAG_STATUS_ORDER = "status_order";
    String tag_json_obj = "json_obj_req";
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

    getOrder();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
    private void getOrder(){
        // membuat request JSON
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(OrderOwner.this, 1));



        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject((i));
                        NoOrder.add(obj.getString(TAG_NO_ORDER));
                        NamaPerusahaan.add(obj.getString(TAG_NAMA_PT));
                        TanggalPenawaran.add(obj.getString(TAG_TANGGAL_PENAWARAN));
                        Status.add(obj.getString(TAG_STATUS_ORDER));
                        Status_Pengerjaan.add(obj.getString(TAG_STATUS_SELESAI));
                        Status_Order.add(obj.getString(TAG_STATUS_ORDER));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /*   saldorupiah = Double.parseDouble(tot);*/

                adapter = new AdapterOrder(OrderOwner.this,NoOrder,NamaPerusahaan,TanggalPenawaran,Status,Status_Pengerjaan,Status_Order);
                rvView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
}
