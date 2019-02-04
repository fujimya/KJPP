package com.example.fujimiya.kjpp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    Context context;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    public AdapterOrder(Context contxt, ArrayList<String> NoOrderget, ArrayList<String> NamaPerusahaanGet, ArrayList<String> TanggalGet,ArrayList<String> StatusGet){
        NoOrder =  NoOrderget;
        NamaPerusahaan =  NamaPerusahaanGet;
        TanggalPenawaran = TanggalGet;
        Status = StatusGet;
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
        holder.txt_NoOrder.setText(NoOrder.get(position));
        holder.txt_NamaPerusahaan.setText(NamaPerusahaan.get(position));
        holder.txt_TanggalOrder.setText(TanggalPenawaran.get(position));
    }

    @Override
    public int getItemCount() {
        return NoOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_NamaPerusahaan;
        public TextView txt_NoOrder;
        public TextView txt_TanggalOrder;
        public CardView cvMain;
        public ViewHolder(View v) {
            super(v);
            txt_NamaPerusahaan = (TextView) v.findViewById(R.id.title);
            txt_NoOrder = (TextView) v.findViewById(R.id.subtitle);
            txt_TanggalOrder = (TextView) v.findViewById(R.id.date);
            cvMain = (CardView) v.findViewById(R.id.cv_main);
            cvMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
