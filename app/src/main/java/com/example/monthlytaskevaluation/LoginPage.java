package com.example.monthlytaskevaluation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class LoginPage extends AppCompatActivity {

    Animation topAnim;

    ImageView app_logo;
    EditText email, password;

    Button logIn;

    KProgressHUD hud;

    int count = 0;


    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //load animation
        topAnim = AnimationUtils.loadAnimation(LoginPage.this, R.anim.top_animation);


        //Hooks
        app_logo = findViewById(R.id.app_logo);
        logIn = findViewById(R.id.logIn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);


        //animation set
        app_logo.setAnimation(topAnim);



        LoadUrl loadUrl = new LoadUrl();
        loadUrl.execute("");

        checkConnection();
    }


    boolean checkConnection() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(getApplicationContext(), "Wifi Enabled", Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(getApplicationContext(), "Mobile Data Enabled", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }


    class LoadUrl extends AsyncTask<String, Integer, String> {

        public LoadUrl() {

        }

        @Override
        protected void onPreExecute() {
            //forProgressHud
            hud = KProgressHUD.create(LoginPage.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Loading").setCancellable(true);
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkConnection()) {
                        hud.show();
                        parseJSON();
                    }else {
                        Toast.makeText(LoginPage.this, "no internet connection", Toast.LENGTH_SHORT).show();
                    }
                }

            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    private void parseJSON() {
        String url = "https://fakestoreapi.com/users";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hud.show();
                Log.d("String", "" + response);

                if ((!email.getText().toString().equals("") && !password.getText().toString().equals(""))) {

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            if (obj.getString("username").equals(email.getText().toString())) {
                                if (obj.getString("password").equals(password.getText().toString())) {
                                    hud.dismiss();
                                    startActivity(new Intent(LoginPage.this, Dashboard.class));
                                    hud.dismiss();
                                    finish();
                                    count = 1;
                                    break;
                                } else {
                                    count = 0;
                                }
                            } else {
                                count = 0;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Complete all field", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                    count = 1;
                }

                if (count == 0) {
                    Toast.makeText(getApplicationContext(), "Wrong user name or Password", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }

        });
        queue.add(request);
    }

}