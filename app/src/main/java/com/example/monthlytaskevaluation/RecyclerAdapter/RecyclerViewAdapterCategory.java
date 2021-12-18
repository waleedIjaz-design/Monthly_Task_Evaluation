package com.example.monthlytaskevaluation.RecyclerAdapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monthlytaskevaluation.Model.ProductsModel;
import com.example.monthlytaskevaluation.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.ViewHolder>{
    List<String> list;
    Context context;
    LayoutInflater inflater;
    OnProductListener onPrductListener;
    int posisioned =0;


    public RecyclerViewAdapterCategory( Context applicationContext, List<String> list, OnProductListener onProductListener) {


        this.list = list;
        this.inflater=LayoutInflater.from(applicationContext);
        this.onPrductListener = onProductListener;
        this.context = applicationContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.category_row,parent,false);
        return new ViewHolder(view,onPrductListener);

    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder( RecyclerViewAdapterCategory.ViewHolder holder, int position) {

        String model=list.get(position);
        holder.ct_name.setText(""+model);

        if (position==posisioned){
            holder.ct_name.setBackground(context.getDrawable(R.drawable.gradient));
        }else
        {
            holder.ct_name.setBackground(context.getDrawable(R.drawable.customborder3));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ct_name;

        View mview;
        LinearLayout linear_layout_id;
        OnProductListener onPrductlistener;

        public ViewHolder(@NonNull View itemView, OnProductListener onProductListener) {
            super(itemView);
            ct_name = itemView.findViewById(R.id.categories_id);

            mview = itemView;
            linear_layout_id = itemView.findViewById(R.id.linear_layout_id);

            this.onPrductlistener = onProductListener;
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            String model_items= list.get(getAdapterPosition());
            onPrductlistener.onProductListener(model_items);
            posisioned = getAdapterPosition();
            notifyDataSetChanged();

        }

    }
    public interface OnProductListener{
        void onProductListener(String position);
    }


}