package com.example.fujimiya.kjpp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private ArrayList<String> NoOrder;
    private ArrayList<String> NamaPerusahaan;
    private ArrayList<String> TanggalPenawaran;
    private ArrayList<String> Status;
    private ArrayList<String> Status_Pengerjaan;
    private ArrayList<String> Fee_Penawaran;
    private ArrayList<String> Status_Order= new ArrayList<String>();
    private ArrayList<String> Harga_Deal= new ArrayList<String>();
    private ArrayList<String> Nama_Property= new ArrayList<String>();
    private ArrayList<String> Due_Date= new ArrayList<String>();
    private ArrayList<String> Lokasi_Aset= new ArrayList<String>();
    private ArrayList<String> Nama_Surveyor= new ArrayList<String>();
    Context context;
    private static View.OnClickListener clickListener;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
 int posisi;

    public AdapterOrder(Context contxt, ArrayList<String> NoOrderget, ArrayList<String> NamaPerusahaanGet, ArrayList<String> TanggalGet,ArrayList<String> StatusGet,ArrayList<String> StatusPengerjaanGet,ArrayList<String> StatusOrderGet,ArrayList<String> FeePenawaranGet,ArrayList<String> Harga_DealGet,ArrayList<String> NamaPropertyGet,ArrayList<String> DueDateGet,ArrayList<String> LokasiAsetGet,ArrayList<String> NamaSurveyorGet){
        NoOrder =  NoOrderget;
        NamaPerusahaan =  NamaPerusahaanGet;
        TanggalPenawaran = TanggalGet;
        Status = StatusGet;
        Status_Pengerjaan = StatusPengerjaanGet;
        Status_Order= StatusOrderGet;
        Fee_Penawaran=FeePenawaranGet;
        Harga_Deal=Harga_DealGet;
        Nama_Property=NamaPropertyGet;
        Due_Date=DueDateGet;
        Lokasi_Aset=LokasiAsetGet;
        Nama_Surveyor=NamaSurveyorGet;
        context = contxt;
    }

    @Override
    public AdapterOrder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_order, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterOrder.ViewHolder vh = new AdapterOrder.ViewHolder(v);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterOrder.ViewHolder holder, final int position) {
        holder.txt_NoOrder.setText("No. Order : " + NoOrder.get(position));
        holder.txt_NamaPerusahaan.setText(NamaPerusahaan.get(position));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = sdf.parse(TanggalPenawaran.get(position));
            sdf.applyPattern("dd-MM-yyyy");
           holder.txt_TanggalOrder.setText(sdf.format(d));
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        holder.txt_StatusPengerjaan.setText(Status_Pengerjaan.get(position));
        holder.txt_Status_order.setText(Status_Order.get(position));
        posisi = position;
        if(Status_Order.get(position).equals("MENUNGGU")){
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#ffff8800"));
        }else if (Status_Order.get(position).equals("DEAL")){
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FF99cc00"));
        }else{
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FFF72E1A"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   context.startActivity(MenuOwner.getActIntent((Activity) context).putExtra("data", NoOrder.get(position)));
              //  Toast.makeText(context, "Anda Memilih" + NamaPerusahaan.get(position), Toast.LENGTH_SHORT).show();
               Intent intent = new Intent("custom-message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("no_order",NoOrder.get(position));
                intent.putExtra("Nama_PT",NamaPerusahaan.get(position));
                intent.putExtra("Lokasi_Aset",Lokasi_Aset.get(position));
                intent.putExtra("Tanggal_Penawaran",TanggalPenawaran.get(position));
                intent.putExtra("Harga_Deal",Harga_Deal.get(position));
                intent.putExtra("Nama_Surveyor",Nama_Surveyor.get(position));
                intent.putExtra("Nama_Property",Nama_Property.get(position));
                intent.putExtra("Due_Date",Due_Date.get(position));
                intent.putExtra("fee_penawaran",Fee_Penawaran.get(position));
                intent.putExtra("Status_Pengerjaan",Status_Pengerjaan.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }
    public void clearItems()
    {

        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return NoOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_NamaPerusahaan;
        public TextView txt_NoOrder;
        public TextView txt_TanggalOrder;
        public TextView txt_StatusPengerjaan;
        public Button txt_Status_order;
        public CardView cvMain;
        public ViewHolder(View v) {
            super(v);
            txt_NamaPerusahaan = (TextView) v.findViewById(R.id.title);
            txt_NoOrder = (TextView) v.findViewById(R.id.subtitle);
            txt_TanggalOrder = (TextView) v.findViewById(R.id.date);
            txt_StatusPengerjaan= (TextView) v.findViewById(R.id.status_pengerjaan);
            txt_Status_order= (Button) v.findViewById(R.id.btn_deal);


        }
    }
}
