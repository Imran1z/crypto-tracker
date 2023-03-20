package com.imran.cryptotracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText searchEdt;
    private RecyclerView currenciesRv;
    private ProgressBar loadingPb;
    private ArrayList<currencyRVModal> currencyRVModalArrayList;
    private CurrencyRVAdapter currencyRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEdt=findViewById(R.id.idEdtSearch);
        currenciesRv=findViewById(R.id.idRvCurrencies);
        loadingPb=findViewById(R.id.idPBLoading);

        currencyRVModalArrayList=new ArrayList<>();
        currencyRVAdapter=new CurrencyRVAdapter(currencyRVModalArrayList,this);
        currenciesRv.setLayoutManager(new LinearLayoutManager(this));
        currenciesRv.setAdapter(currencyRVAdapter);
        getCurrencyData();

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterCurrencies(s.toString());

            }
        });



    }

    private void filterCurrencies(String currency){
        ArrayList<currencyRVModal> filterList =new ArrayList<>();
        for (currencyRVModal item :currencyRVModalArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                filterList.add(item);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this, "No currency found ", Toast.LENGTH_SHORT).show();
        }
        else{
            currencyRVAdapter.filteredList(filterList);
        }

    }

    private void getCurrencyData(){
        loadingPb.setVisibility(View.VISIBLE);
        String url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue queue;
        queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPb.setVisibility(View.GONE);
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for(int i=0;i<dataArray.length();i++){
                        JSONObject dataobj =dataArray.getJSONObject(i);
                        String name =dataobj.getString("name");
                        String symbol =dataobj.getString("symbol");

                        JSONObject quote =dataobj.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price =USD.getDouble("price");

                        currencyRVModalArrayList.add(new currencyRVModal(name,symbol,price));


                    }
                    currencyRVAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to extract json data", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPb.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers=new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","f1ec9719-47e1-4a97-bcd8-30bf7339ba65");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}