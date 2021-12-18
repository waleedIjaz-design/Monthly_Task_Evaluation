package com.example.monthlytaskevaluation.RecyclerAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monthlytaskevaluation.Model.ProductsModel;
import com.example.monthlytaskevaluation.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.UniAdapterHolder> implements Filterable{

    private Context mContext;

     ArrayList<ProductsModel> mUniModels;

    ArrayList<ProductsModel> backup;

    OnUniversityClick onUniversityClick;


    public RecyclerviewAdapter(Context mContext, ArrayList<ProductsModel> mUniModels, OnUniversityClick onUniversityClick) {
        this.mContext = mContext;
        this.mUniModels = mUniModels;
        this.onUniversityClick = onUniversityClick;

        backup = new ArrayList<>(mUniModels);

    }


    @NonNull
    @Override
    public UniAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.product_row,parent,false);
        return  new UniAdapterHolder(v,onUniversityClick);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapter.UniAdapterHolder holder, int position) {
        ProductsModel currentItem = mUniModels.get(position);

        String image = currentItem.getImage();
        String title_name = currentItem.getTitle();
        String product_description = currentItem.getDescription();
        double price = currentItem.getPrice();
        String rating = currentItem.getRating();
        String category = currentItem.getCategory();
        holder.title_name.setText(mUniModels.get(position).getTitle());

        holder.price.setText("$"+mUniModels.get(position).getPrice());
        holder.rating.setText("   "+mUniModels.get(position).getRate()+"("+mUniModels.get(position).getCount()+")");

        Picasso.get()
                .load(mUniModels.get(position).getImage())
                .into(holder.image);

    }


    @Override
    public int getItemCount() {
        Log.d("String" , "getItemCount: "+mUniModels.size());
        return mUniModels.size();

    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ProductsModel> filterData = new ArrayList<>();

            if (constraint.toString().isEmpty())
                filterData.addAll(backup);
            else {
                for (ProductsModel obj : backup) {
                    if (obj.getTitle().toString().toLowerCase().contains(constraint.toString().toLowerCase()))
                        filterData.add(obj);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUniModels.clear();
            mUniModels.addAll((Collection<? extends ProductsModel>) results.values);
            notifyDataSetChanged();

        }
    };



    public class UniAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;
        public TextView title_name;
        public TextView price;
        public TextView rating;
        OnUniversityClick onUniversityClick;
        View mView;



        public UniAdapterHolder(View itemView,OnUniversityClick onUniversityClick) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title_name = itemView.findViewById(R.id.title_name);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.rating);

            this.onUniversityClick = RecyclerviewAdapter.this.onUniversityClick;

            mView = itemView;

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            ProductsModel model_items= mUniModels.get(getAdapterPosition());
            onUniversityClick.onUniClick(model_items);

        }

    }
    public void filterList(ArrayList<ProductsModel> filteredList) {
        mUniModels = filteredList;
        notifyDataSetChanged();

    }


    public interface OnUniversityClick{
        void onUniClick(ProductsModel uniModel);

    }


}
