package com.example.fujimiya.kjpp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.fujimiya.kjpp.app.AppController;
import com.example.fujimiya.kjpp.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderOwner extends AppCompatActivity {
    private ActionBar actionBar;
    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    static OrderOwner INSTANCE;
    String data;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> NoOrder =  new ArrayList<String>();
    private ArrayList<String> NamaPerusahaan = new ArrayList<String>();
    private ArrayList<String> TanggalPenawaran = new ArrayList<String>();
    private ArrayList<String> Status = new ArrayList<String>();
    private ArrayList<String> Status_Pengerjaan = new ArrayList<String>();
    private ArrayList<String> Status_Order= new ArrayList<String>();
    private ArrayList<String> Fee_Penawaran= new ArrayList<String>();
    private ArrayList<String> Harga_Deal= new ArrayList<String>();
    private ArrayList<String> Nama_Property= new ArrayList<String>();
    private ArrayList<String> Due_Date= new ArrayList<String>();
    private ArrayList<String> Lokasi_Aset= new ArrayList<String>();
    private ArrayList<String> Nama_Surveyor= new ArrayList<String>();
    ProgressDialog pDialog;
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = OrderOwner.class.getSimpleName();
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    BroadcastReceiver receiver;
    TextView txt_id,txt_fee,txt_deal;
    Button btn_dealx,btn_notx;
    String id,fee,harga_deal,status="DEAL";
    int success;
    private static String url_select 	 = Server.URL + "order/get_order.php";
   // private static String url_cari 	 = Server.URL + "order/get_order.php";
    private static String url_fee 	 = Server.URL + "order/post_fee.php";
    private static String url_survey 	 = Server.URL + "survey/post_survey.php";
    private static String url_deal 	 = Server.URL + "order/post_deal.php";//no_order fee_penawaran
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "msg";
    Locale localeID=new Locale("in","ID");
    double saldorupiah;
    public final static String TAG_ID_ORDER= "id_order";
    public final static String TAG_NO_ORDER = "no_order";
    public final static String TAG_NAMA_PT = "nama_pt";
    public final static String TAG_LOKASI_ASET = "lokasi_aset";
    public final static String TAG_TANGGAL_PENAWARAN= "tanggal_penawaran";
    public final static String TAG_DUEDATE= "duedate";
    public final static String TAG_NAMA_PROPERTY= "nama_properti";
    public final static String TAG_FEE_PENAWARAN = "fee_penawaran";
    public final static String TAG_HARGA_DEAL= "harga_deal";
    public final static String TAG_STATUS_BAYAR = "status_bayar";
    public final static String TAG_STATUS_SELESAI = "status_pengerjaan";
    public final static String TAG_STATUS_ORDER = "status_order";
    public final static String TAG_NAMA_SURVEYOR = "nama_surveyor";
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    String tag_json_obj = "json_obj_req";
    IntentFilter filter;
    SearchView editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        INSTANCE=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        editText = (SearchView) findViewById(R.id.svCari);
        actionBar = getSupportActionBar();
        actionBar.setTitle("DATA ORDER");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));


        rvView = findViewById(R.id.rv_main_barang);
        boolean hasText=false;
        editText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getOrder(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    getOrder("");
                }
                return true;
            }
        });
    getOrder("");
    }
    @Override
    public void onDestroy (){
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if(item.getItemId() == R.id.refresh){
                getOrder(editText.getQuery().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    private void getOrder(String cari){
        // membuat request JSON
        final String url;
        if(cari.isEmpty()){
            url=url_select;
        }else{
         cari=   cari.replaceAll(" ","%20");
            url=url_select+"?cari="+cari;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Mengambil Data Order ...");
        showDialog();
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(OrderOwner.this, 1));

        clear();
        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
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
                        Fee_Penawaran.add(obj.getString(TAG_FEE_PENAWARAN));
                        Harga_Deal.add(obj.getString(TAG_HARGA_DEAL));
                        Nama_Property.add(obj.getString(TAG_NAMA_PROPERTY));
                        Due_Date.add(obj.getString(TAG_DUEDATE));
                        Lokasi_Aset.add(obj.getString(TAG_LOKASI_ASET));
                        Nama_Surveyor.add(obj.getString(TAG_NAMA_SURVEYOR));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /*   saldorupiah = Double.parseDouble(tot);*/

                adapter = new AdapterOrder(OrderOwner.this,NoOrder,NamaPerusahaan,TanggalPenawaran,Status,Status_Pengerjaan,Status_Order,Fee_Penawaran,Harga_Deal,Nama_Property,Due_Date,Lokasi_Aset,Nama_Surveyor);
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
        NoOrder.clear();
        Status_Order.clear();
        NamaPerusahaan.clear();
        TanggalPenawaran.clear();
        Status_Pengerjaan.clear();
        Status.clear();
        Fee_Penawaran.clear();
        Harga_Deal.clear();
        Lokasi_Aset.clear();
        Nama_Property.clear();
        Nama_Surveyor.clear();
        Due_Date.clear();
    }
   public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String no_order = intent.getStringExtra("no_order");
            String fees = intent.getStringExtra("fee_penawaran");
            String status = intent.getStringExtra("Status_Pengerjaan");
            String harga_deal= intent.getStringExtra("Harga_Deal");
            String tanggal_penawaran= intent.getStringExtra("Tanggal_Penawaran");
            String nama_pt = intent.getStringExtra("Nama_PT");
            String lokasi_aset = intent.getStringExtra("Lokasi_Aset");
            String nama_surveyor= intent.getStringExtra("Nama_Surveyor");
            String due_date = intent.getStringExtra("Due_Date");
            String nama_property = intent.getStringExtra("Nama_Property");

            // Toast.makeText(OrderOwner.this,no_order ,Toast.LENGTH_SHORT).show();
            // DialogFee(no_order);
            id = no_order;
            if (status.equals("INPUT FEE PENAWARAN"))
                DialogFee(no_order);
            else if (status.equals("INPUT DEAL OR NOT") || status.equals("CANCEL"))
                DialogDeal(no_order, fees);
            else if (status.equals("MEMILIH SURVEYOR")) {
         pick();
            }else if(status.equals("MENUNGGU SURVEYOR")){
                DialogDetail(no_order,nama_pt,lokasi_aset,tanggal_penawaran,fees,harga_deal,due_date,nama_property,nama_surveyor);
            }
            }
    };
    public void pick(){
        Intent intent = new Intent(OrderOwner.this, ActivitySurveyor.class);
        //   intent.putExtra("kode_barang",kode_baru);
        startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);
    }
    private void simpan_survey(final String idx) {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update


        final StringRequest strReq = new StringRequest(Request.Method.POST, url_survey, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        getOrder("");
                        fee="";
                        harga_deal="";
                        id="";
                        Toast.makeText(OrderOwner.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(OrderOwner.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(OrderOwner.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update

                    params.put("no_order", id);
                    params.put("id_surveyor", idx);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    private void update(final String feex) {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
         if (feex.isEmpty()){
            url = url_fee;
        } else {
            url = url_deal;
        }

        final StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        getOrder("");
                        fee="";
                        harga_deal="";
                        Toast.makeText(OrderOwner.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(OrderOwner.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(OrderOwner.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
              if (feex.isEmpty()){
                    params.put("no_order", id);
                    params.put("fee_penawaran", fee);
               }else{
                  params.put("no_order", id);
                  params.put("status_order", status);
                  params.put("harga_deal", harga_deal);
              }

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    private void DialogFee(String idx) {
        dialog = new AlertDialog.Builder(OrderOwner.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.activity_dialog_fee, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Input Fee Penawaran");

        txt_id      = (EditText) dialogView.findViewById(R.id.txt_id);
        txt_fee   = (EditText) dialogView.findViewById(R.id.txt_fee_penawaran);
        txt_fee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_fee.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                 //   fee = originalString;
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    fee=originalString;
                    longval = Long.parseLong(originalString);
                    //fee = longval;
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    txt_fee.setText(formattedString);
                    int selection = txt_fee.getText().length();
                    Selection.setSelection((Spannable) txt_fee.getText(),selection);
                    //txt_fee.setSelection(txt_fee.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                txt_fee.addTextChangedListener(this);
            }
        });
        id=idx;
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

               // fee    = txt_fee.getText().toString();
                update("");
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               // kosong();
            }
        });

        dialog.show();
    }
    private void DialogDeal(String idx,String feex) {
        dialog = new AlertDialog.Builder(OrderOwner.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_deal_or_not, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Deal Or Not");
        status="DEAL";
        txt_id      = (EditText) dialogView.findViewById(R.id.txt_id);
        txt_fee   = (EditText) dialogView.findViewById(R.id.txt_fee_penawaran);
        btn_dealx   = (Button) dialogView.findViewById(R.id.btn_deal);
        btn_notx     = (Button) dialogView.findViewById(R.id.btn_not);
        txt_deal    = (EditText) dialogView.findViewById(R.id.txt_harga_deal);
        txt_fee.setEnabled(false);
        saldorupiah= Double.parseDouble(feex);
        txt_fee.setText(formatRupiah.format(saldorupiah)+",00");
        btn_dealx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               btn_dealx.setBackgroundColor(Color.parseColor("#FF99cc00"));
                btn_notx.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                txt_deal.setEnabled(true);
                status="DEAL";

            }
        });
        btn_notx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btn_dealx.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                btn_notx.setBackgroundColor(Color.parseColor("#FFF72E1A"));
                txt_deal.setEnabled(false);
               txt_deal.setText("0");
                status="NOT";
            //    btn_dealx.setText(status);
            }
        });
        txt_deal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txt_deal.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();
                    //   fee = originalString;
                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    harga_deal =originalString;
                    longval = Long.parseLong(originalString);
                    //fee = longval;
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    txt_deal.setText(formattedString);
                    int selection = txt_deal.getText().length();
                    Selection.setSelection((Spannable) txt_deal.getText(),selection);
                    //txt_fee.setSelection(txt_fee.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                txt_deal.addTextChangedListener(this);
            }
        });
        id=idx;
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                fee    = txt_fee.getText().toString();


                update("1");
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // kosong();
            }
        });

        dialog.show();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                //   if(kod)
                // get String data from Intent
                String ID = data.getStringExtra("ID");//     edit(returnString);
                String nama = data.getStringExtra("nama");
         //       Toast.makeText(OrderOwner.this,nama + ID ,Toast.LENGTH_SHORT).show();
           simpan_survey(ID);
            }
        }
    }
    private void DialogDetail(String idx,String nama_pt,String lokasi_aset,String tanggal_penawaran, String fee_penawaran,String harga_deal,String duedate,String nama_properti, String nama_sruveyor) {
        dialog = new AlertDialog.Builder(OrderOwner.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_menunggu, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Detail Order");
        TextView txt_noorder     = (TextView) dialogView.findViewById(R.id.tv_kode);
        TextView txt_namapt     = (TextView) dialogView.findViewById(R.id.tv_namapt);
        TextView txt_lokasiaset     = (TextView) dialogView.findViewById(R.id.tv_lokasiaset);
        TextView txt_fee = (TextView) dialogView.findViewById(R.id.tv_fee);
        TextView txt_tanggal = (TextView) dialogView.findViewById(R.id.tv_tanggal);
        TextView tv_harga_deal  = (TextView) dialogView.findViewById(R.id.tv_hargadeal);
        TextView tx_duedate = (TextView) dialogView.findViewById(R.id.tv_duedate);
        TextView tx_surveyor = (TextView) dialogView.findViewById(R.id.tv_namasurveyor);
        TextView tx_property = (TextView) dialogView.findViewById(R.id.tv_properti);
        txt_noorder.setText("No Order :" + idx);
        txt_namapt.setText(nama_pt);
        txt_lokasiaset.setText(lokasi_aset);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(tanggal_penawaran);
            sdf.applyPattern("dd-MM-yyyy");
          txt_tanggal.setText(sdf.format(d));
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        saldorupiah = Double.parseDouble(fee_penawaran);
        txt_fee.setText(formatRupiah.format(saldorupiah)+",00");
        saldorupiah = Double.parseDouble(harga_deal);
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dx = sdfx.parse(duedate);
            sdfx.applyPattern("dd-MM-yyyy");
            tx_duedate.setText(sdfx.format(dx));
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        tv_harga_deal.setText(formatRupiah.format(saldorupiah)+",00");
        tx_property.setText(nama_properti);
        tx_surveyor.setText(nama_sruveyor);
        //

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }
}
