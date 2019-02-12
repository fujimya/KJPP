package com.example.fujimiya.kjpp;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class AdapterSurveyor extends RecyclerView.Adapter<AdapterSurveyor.ViewHolder> {
    private ArrayList<String> ID =  new ArrayList<String>();
    private ArrayList<String>  nama_surveyor = new ArrayList<String>();
    private ArrayList<String> no_hp = new ArrayList<String>();
    private ArrayList<String> total_pengerjaan = new ArrayList<String>();

    Context context;
    private static View.OnClickListener clickListener;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    int posisi;

    public AdapterSurveyor(Context contxt, ArrayList<String> IDget, ArrayList<String> nama_surveyorGet, ArrayList<String> no_hpGet,ArrayList<String> total_pengerjaanGet){
        ID= IDget;
        nama_surveyor=  nama_surveyorGet;
        no_hp = no_hpGet;
        total_pengerjaan = total_pengerjaanGet;
        context = contxt;
    }

    @Override
    public AdapterSurveyor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_pilih, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterSurveyor.ViewHolder vh = new AdapterSurveyor.ViewHolder(v);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterSurveyor.ViewHolder holder, final int position) {
        //holder.txt_NoOrder.setText("No. Order : " + NoOrder.get(position));
        holder.txt_namaSurveyor.setText(nama_surveyor.get(position));
        holder.txt_No_HP.setText("No Handphone :" +no_hp.get(position));
        holder.button_jumlahx.setText(total_pengerjaan.get(position));
        posisi = position;
/*        if(Status_Order.get(position).equals("MENUNGGU")){
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#ffff8800"));
        }else if (Status_Order.get(position).equals("DEAL")){
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FF99cc00"));
        }else{
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FFF72E1A"));
        }*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   context.startActivity(MenuOwner.getActIntent((Activity) context).putExtra("data", NoOrder.get(position)));
                //  Toast.makeText(context, "Anda Memilih" + NamaPerusahaan.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("surveyor-message");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("ID",ID.get(position));
                intent.putExtra("nama",nama_surveyor.get(position));
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
        return ID.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_namaSurveyor;
        public TextView txt_No_HP;

        public Button button_jumlahx;
        public CardView cvMain;
        public ViewHolder(View v) {
            super(v);
            txt_namaSurveyor= (TextView) v.findViewById(R.id.title);
            txt_No_HP= (TextView) v.findViewById(R.id.subtitle);
            button_jumlahx= (Button) v.findViewById(R.id.btn_jumlah);


        }
    }
}
