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
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.dataItem.SellerInfo;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {
    private static final String TAG = "SellerAdapter";
    Context context;
    List<SellerInfo> sellerList;

    public SellerAdapter(Context context, List<SellerInfo> sellerList) {
        this.context = context;
        this.sellerList = sellerList;
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Log.i( TAG, "onCreateViewHolder: " + position );
        SellerViewHolder holder;
        View view = LayoutInflater.from(context).inflate( R.layout.item_seller, parent, false);

        holder = new SellerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
        holder.tv_seller_name.setText(sellerList.get(position).getSellername());
        holder.tv_seller_deal.setText(sellerList.get(position).getSellerdeal());
        holder.rb_seller_rating.setRating(Float.parseFloat(sellerList.get(position).getSellerrating()));

        Glide.with(context).load(sellerList.get(position).getSellerlogo()).into(holder.iv_seller_icon);
    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    class SellerViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_seller_icon;
        private TextView tv_seller_name;
        private TextView tv_seller_deal;
        private MaterialRatingBar rb_seller_rating;

        public SellerViewHolder(View itemView) {
            super(itemView);
            iv_seller_icon = itemView.findViewById(R.id.iv_seller_icon);
            tv_seller_name = itemView.findViewById(R.id.tv_seller_name);
            tv_seller_deal = itemView.findViewById(R.id.tv_seller_deal);
            rb_seller_rating = itemView.findViewById(R.id.rb_seller_rating);
        }
    }

}
