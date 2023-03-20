package com.imran.cryptotracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder> {
private ArrayList<currencyRVModal> currencyRVModalArrayList;
private Context context;
private static DecimalFormat df2=new DecimalFormat("#.#######");

    public CurrencyRVAdapter(ArrayList<currencyRVModal> currencyRVModalArrayList, Context context) {
        this.currencyRVModalArrayList = currencyRVModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.currency_rv_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.ViewHolder holder, int position) {
        holder.currencyNameTV.setText(currencyRVModalArrayList.get(position).getName());
        holder.symbolTV.setText(currencyRVModalArrayList.get(position).getSymbol());
        holder.priceTV.setText("$"+df2.format(currencyRVModalArrayList.get(position).getPrice()));

    }
    void filteredList(ArrayList<currencyRVModal> filteredList){
        currencyRVModalArrayList=filteredList;
        notifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return currencyRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView currencyNameTV,symbolTV,priceTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           currencyNameTV =itemView.findViewById(R.id.idTVCurrencyName);
            symbolTV=itemView.findViewById(R.id.idTVSymbol);
            priceTV=itemView.findViewById(R.id.idTVCurrencyPrice);

        }
    }
}
