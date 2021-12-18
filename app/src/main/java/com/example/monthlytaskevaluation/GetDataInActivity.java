package com.example.monthlytaskevaluation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GetDataInActivity extends AppCompatActivity {

    ImageView image;
    TextView title_name, price,rating,decription,count;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_in);


        image =findViewById(R.id.image);
        title_name =findViewById(R.id.title_name);
        price =findViewById(R.id.price);
        rating =findViewById(R.id.rating);
        count =findViewById(R.id.count);
        decription =findViewById(R.id.decription);

        Picasso.get().load(getIntent().getStringExtra("image")).into(image);
        title_name.setText(getIntent().getStringExtra("title"));
        decription.setText(getIntent().getStringExtra("description"));
        rating.setText(getIntent().getStringExtra("rate"));
        count.setText(getIntent().getStringExtra("count"));
        price.setText(getIntent().getStringExtra("price"));


    }
}