package com.example.fujimiya.kjpp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.fujimiya.kjpp.app.AppController;
import com.example.fujimiya.kjpp.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ActivitySurveyor extends AppCompatActivity {
    private ActionBar actionBar;
    static ActivitySurveyor INSTANCE;
    String data;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> ID =  new ArrayList<String>();
    private ArrayList<String>  nama_surveyor = new ArrayList<String>();
    private ArrayList<String> no_hp = new ArrayList<String>();
    private ArrayList<String> total_pengerjaan = new ArrayList<String>();

    ProgressDialog pDialog;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = OrderOwner.class.getSimpleName();
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;


    int success;
    private static String url_select 	 = Server.URL + "get_surveyor.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "msg";
    Locale localeID=new Locale("in","ID");
    double saldorupiah;
    public final static String TAG_ID= "ID";
    public final static String TAG_NAMA_SURVEYOR = "nama_surveyor";
    public final static String TAG_NO_HP = "no_hp";
    public final static String TAG_TOTAL_PENGERJAAN= "total_pengerjaan";

    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    String tag_json_obj = "json_obj_req";
    IntentFilter filter;
    SearchView editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyor);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);*/
        INSTANCE=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         editText = (SearchView) findViewById(R.id.svCari);
        actionBar = getSupportActionBar();
        actionBar.setTitle("PILIH SURVEYOR");


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("surveyor-message"));



        rvView =(RecyclerView) findViewById(R.id.rv_main_surveyor);
        boolean hasText=false;
        editText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSurveyor(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                    getSurveyor("");
                return true;
            }
        });
        getSurveyor("");
    }
    @Override
    public void onDestroy (){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        else if(item.getItemId() == R.id.refresh){
                getSurveyor(editText.getQuery().toString());
        }
        return super.onOptionsItemSelected(item);
    }

  public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
          // Get extra data included in the Intent
          final String ID = intent.getStringExtra("ID");
          final String nama_surveyor = intent.getStringExtra("nama");
          DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  switch (which){
                      case DialogInterface.BUTTON_POSITIVE:
                          //Yes button clicked
                          //simpan_barang();
                          selesai(ID,nama_surveyor);
                          dialog.dismiss();


                          // FragmentManager fm = getSupportFragmentManager();
                          // FragmentPesanan fragment =(FragmentPesanan)fm.findFragmentById(R.id.nav_menu3);
                          //     FragmentPesanan.callVolley();
                        /*        FragmentBarang nextFragment = new FragmentBarang();
                                nextFragment.setArguments(bundle);*/

                            /*    FragmentManager fragmentManager = Konfirmasi_Pesanan.this.getSupportFragmentManager();
                                FragmentTransaction tx=fragmentManager.beginTransaction();
                                tx.replace(R.id.content_frame, nextFragment).commit();*/
                          //   FragmentPesanan fragment = new FragmentPesanan();
                          //    ((FragmentPesanan) fragment).WOW();
                          break;

                      case DialogInterface.BUTTON_NEGATIVE:
                          //No button clicked
                          dialog.dismiss();
                          break;
                  }
              }
          };

          AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySurveyor.this);
          builder.setMessage("Anda Yakin Ingin Memilih "+nama_surveyor+" Sebagai Surveyor ?").setPositiveButton("Yes", dialogClickListener)
                  .setNegativeButton("No", dialogClickListener).show();

       //   String status = intent.getStringExtra("Status_Pengerjaan");
          // Toast.makeText(OrderOwner.this,no_order ,Toast.LENGTH_SHORT).show();
          // DialogFee(no_order);

      }
  };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owner, menu);
        return true;
    }
  public void selesai(String ID,String nama){
      Intent intent = new Intent();
      intent.putExtra("ID", ID);
      intent.putExtra("nama", nama);
      setResult(RESULT_OK, intent);

      finish();
  }
  public static ActivitySurveyor getActivityInstance()
  {
      return INSTANCE;
  }
    public String getData()
    {
        return this.data;
    }
    private void getSurveyor(String cari){
        // membuat request JSON
        final String url;
        if(cari.isEmpty()){
            url = url_select;
        }else{
            cari=   cari.replaceAll(" ","%20");
            url = url_select+"?cari="+cari;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mengambil Data Surveyor ...");
        showDialog();
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(ActivitySurveyor.this, 1));

        clear();
        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject((i));
                       ID.add(obj.getString(TAG_ID));
                        nama_surveyor.add(obj.getString(TAG_NAMA_SURVEYOR));
                        no_hp.add(obj.getString(TAG_NO_HP));
                        total_pengerjaan.add(obj.getString(TAG_TOTAL_PENGERJAAN));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /*   saldorupiah = Double.parseDouble(tot);*/

                adapter = new AdapterSurveyor(ActivitySurveyor.this,ID,nama_surveyor,no_hp,total_pengerjaan);
                rvView.setAdapter(adapter);
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }
    public void clear(){
        ID.clear();
        total_pengerjaan.clear();
        nama_surveyor.clear();
        no_hp.clear();


    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
