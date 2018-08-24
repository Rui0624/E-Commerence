package com.example.android.onlineshopping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.dataItem.ProductsListItem;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductListViewHolder> implements View.OnClickListener{
    private Context context;
    private List<ProductsListItem> productsList;
    private OnItemClickListener mOnItemClickListener;

    public ProductsAdapter(Context context, List<ProductsListItem> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Log.i("product_list_position", position + "");
        ProductListViewHolder holder;
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_list, parent, false);
        view.setOnClickListener(this);
        holder = new ProductListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        Log.i("price", productsList.get(position).getPrice());
        holder.tv_product_price.setText("$" + productsList.get(position).getPrice());
        holder.tv_product_name.setText(productsList.get(position).getPname());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_not_available);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(productsList.get(position).getImage()).into(holder.iv_product_pic);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Integer)v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_product_pic;
        TextView tv_product_name, tv_product_price;
        public ProductListViewHolder(@NonNull View view) {
            super(view);
            iv_product_pic = view.findViewById( R.id.iv_product_pic);
            tv_product_name = view.findViewById(R.id.tv_product_name);
            tv_product_price = view.findViewById(R.id.tv_product_price);
        }
    }
}
