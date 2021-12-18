package com.example.monthlytaskevaluation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.monthlytaskevaluation.Model.ProductsModel;
import com.example.monthlytaskevaluation.RecyclerAdapter.RecyclerViewAdapterCategory;
import com.example.monthlytaskevaluation.RecyclerAdapter.RecyclerviewAdapter;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements RecyclerViewAdapterCategory.OnProductListener, RecyclerviewAdapter.OnUniversityClick, AdapterView.OnItemSelectedListener {


    EditText search_view;

    KProgressHUD hud;

    String selectedCategory = "";

    RecyclerView mRecyclerViewHor, mRecyclerViewVer;
    public List<String> myArray;

    public ArrayList<ProductsModel> mProductsModels;
    ArrayList<ProductsModel> filterList;
    List<String> categories;
    private RecyclerViewAdapterCategory mCategoryAdapter;
    private RecyclerviewAdapter mProductsAdapter;
    private RequestQueue mRequestQueue;


    RecyclerviewAdapter.OnUniversityClick onUniversityClick = this;
    RecyclerViewAdapterCategory.OnProductListener onProductListener = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        search_view = findViewById(R.id.search_view);

        mRecyclerViewHor = findViewById(R.id.recycler_view_horizontal);
        mRecyclerViewVer = findViewById(R.id.recycler_view_vertical);


        //forProgressHud
        hud = KProgressHUD.create(Dashboard.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading").setCancellable(true)
                .show();


        mRecyclerViewHor.setHasFixedSize(true);
        mRecyclerViewHor.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewVer.setHasFixedSize(true);
        mRecyclerViewVer.setLayoutManager(new LinearLayoutManager(this));
        mProductsModels = new ArrayList<>();

        myArray = new ArrayList<>();


        //Initialize Layout Manager for RecyclerView Horizontal
        mRecyclerViewHor.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        mCategoryAdapter = new RecyclerViewAdapterCategory(getApplicationContext(), myArray, onProductListener);
        mRecyclerViewHor.setAdapter(mCategoryAdapter);

        //Initialize Layout Manager for RecyclerView Vertical
        mRecyclerViewVer.setLayoutManager(new GridLayoutManager(this, 2));
        mProductsAdapter = new RecyclerviewAdapter(getApplicationContext(), mProductsModels, onUniversityClick);
        mRecyclerViewVer.setAdapter(mProductsAdapter);


        parseJSON();
        parseJSONproduct("https://fakestoreapi.com/products");


        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterText(editable.toString());

            }
        });

        // mProductsAdapter.getFilter().filter(newText);


        // Spinner element
        Spinner spinner_id = (Spinner) findViewById(R.id.spinner_id);

        // Spinner click listener
        spinner_id.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        categories = new ArrayList<String>();

        categories.add("ascending");
        categories.add("descending");
        categories.add("1");
        categories.add("3");
        categories.add("5");
        categories.add("7");
        categories.add("9");
        categories.add("11");
        categories.add("13");
        categories.add("15");
        categories.add("17");
        categories.add("19");
        categories.add("all");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_id.setAdapter(dataAdapter);
    }

    private void filterText(String toString) {
        ArrayList<ProductsModel> filteredList = new ArrayList<>();

        for (ProductsModel item : mProductsModels) {
            if (item.getTitle().toLowerCase().contains(toString.toLowerCase()) ||
                    item.getCategory().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mProductsAdapter.filterList(filteredList);
    }



    private void parseJSON() {
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://fakestoreapi.com/products/categories";
        RequestQueue queue = Volley.newRequestQueue(this);
        myArray.add("all");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override

            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        myArray.add(response.getString(i));
                        mCategoryAdapter.notifyDataSetChanged();



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hud.dismiss();
                }
            }

        }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "ERRRRRORR");
                // Log.d(TAG, "onErrorResponse: ");
            }

        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    private void parseJSONproduct(String url) {
        mRequestQueue = Volley.newRequestQueue(this);
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override

            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject sonObj = response.getJSONObject(i);
                        ProductsModel model = new ProductsModel();

                        model.setId(sonObj.getInt("id"));
                        model.setTitle(sonObj.getString("title").toString());
                        model.setDescription(sonObj.getString("description").toString());
                        model.setImage(sonObj.getString("image").toString());
                        model.setPrice(sonObj.getDouble("price"));
                        model.setCategory(sonObj.getString("category").toString());
                        model.setRate((sonObj.getJSONObject("rating").getDouble("rate")));
                        model.setCount((sonObj.getJSONObject("rating").getInt("count")));

                        mProductsModels.add(model);
                        mProductsAdapter.notifyDataSetChanged();
                        filterList = mProductsModels;

                        if (selectedCategory.length() > 0) {
                            filter(selectedCategory);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hud.dismiss();
                }
            }

        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "ERRRRRORR");
                parseJSON();
            }

        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000, 5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void filter(String text) {
        ArrayList<ProductsModel> filteredList = new ArrayList<>();

        if (text.equals("all")) {


            filteredList = filterList;

        } else {
            for (ProductsModel f : mProductsModels) {

                if (f.getCategory().toLowerCase().equals(text.toLowerCase())) {
                    filteredList.add(f);
                }
            }
        }
        mProductsAdapter.filterList(filteredList);
    }


    //For category
    @Override
    public void onProductListener(String position) {
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        filter(position);

    }


    //For products

    @Override
    public void onUniClick(ProductsModel uniModel) {

        Intent i = new Intent(Dashboard.this, GetDataInActivity.class);
        i.putExtra("image", uniModel.getImage());
        i.putExtra("title", "" + uniModel.getTitle());
        i.putExtra("description", "Description : " + uniModel.getDescription());
        i.putExtra("price", "$" + uniModel.getPrice());
        i.putExtra("rate", "   " + uniModel.getRate());
        i.putExtra("count", "" + "(" + uniModel.getCount() + ")");
        startActivity(i);

    }

    boolean checkConnection()
    {
        ConnectivityManager manager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();
        if(activeNetwork!=null)
        {
            if(activeNetwork.getType()==ConnectivityManager.TYPE_WIFI)
            {
                Toast.makeText(getApplicationContext(), "Wifi Enabled", Toast.LENGTH_SHORT).show();
                return true;
            }
            else if(activeNetwork.getType()==ConnectivityManager.TYPE_MOBILE)
            {
                Toast.makeText(getApplicationContext(), "Mobile Data Enabled", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        else
        {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            String item = adapterView.getItemAtPosition(i).toString();

            if (checkConnection()) {

                mProductsModels.clear();
                if (categories.get(i).equals("descending")) {
                    parseJSONproduct("https://fakestoreapi.com/products?sort=desc");
                } else {
                    parseJSONproduct("https://fakestoreapi.com/products?limit=" + categories.get(i));
                }
                hud.show();
            }

        } catch (Exception e) {

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}