package com.example.fujimiya.kjpp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by fujimiya on 2/2/19.
 */

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private ArrayList<String> NoOrder;
    private ArrayList<String> NamaPerusahaan;
    private ArrayList<String> TanggalPenawaran;
    private ArrayList<String> Status;
    private ArrayList<String> Status_Pengerjaan;
    private ArrayList<String> Status_Order= new ArrayList<String>();
    Context context;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    public AdapterOrder(Context contxt, ArrayList<String> NoOrderget, ArrayList<String> NamaPerusahaanGet, ArrayList<String> TanggalGet,ArrayList<String> StatusGet,ArrayList<String> StatusPengerjaanGet,ArrayList<String> StatusOrderGet){
        NoOrder =  NoOrderget;
        NamaPerusahaan =  NamaPerusahaanGet;
        TanggalPenawaran = TanggalGet;
        Status = StatusGet;
        Status_Pengerjaan = StatusPengerjaanGet;
        Status_Order= StatusOrderGet;
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
    public void onBindViewHolder(AdapterOrder.ViewHolder holder, int position) {
        holder.txt_NoOrder.setText("No. Order : " + NoOrder.get(position));
        holder.txt_NamaPerusahaan.setText(NamaPerusahaan.get(position));
        holder.txt_TanggalOrder.setText(TanggalPenawaran.get(position));
        holder.txt_StatusPengerjaan.setText(Status_Pengerjaan.get(position));
        holder.txt_Status_order.setText(Status_Order.get(position));
        if(Status_Order.get(position).equals("MENUNGGU")){
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#ffff8800"));
        }else if (Status_Order.get(position).equals("DEAL")){
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FF99cc00"));
        }else{
            holder.txt_Status_order.setBackgroundColor(Color.parseColor("#FFF72E1A"));
        }
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
            cvMain = (CardView) v.findViewById(R.id.cv_main);
            cvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
