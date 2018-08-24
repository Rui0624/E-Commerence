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
import com.example.android.onlineshopping.dataItem.MainCategoryItem;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ContentViewHolder> implements View.OnClickListener{
    private Context context;
    private List<MainCategoryItem> mainCategoryItemsList;
    private OnItemClickListener mOnItemClickListener;


    public MainCategoryAdapter(Context context, List<MainCategoryItem> mainCategoryItemsList) {
        this.context = context;
        this.mainCategoryItemsList = mainCategoryItemsList;

    }

//    public MainCategoryAdapter(Context context, List<String> testList) {
//        this.context = context;
//        this.testList = testList;
//
//    }

    @NonNull
    @Override
    public MainCategoryAdapter.ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContentViewHolder holder;

        View view = LayoutInflater.from(context).inflate( R.layout.item_cardview_main_category, parent, false);
        holder = new ContentViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Log.i("tag", "glide");
        holder.tv_mainCategory.setText(mainCategoryItemsList.get(position).getCname());
        Log.i("tag", mainCategoryItemsList.get(position).getCname());
        Log.i("text", holder.tv_mainCategory.getText().toString());
        Glide.with(context).load(mainCategoryItemsList.get(position).getCimagerl()).into(((ContentViewHolder) holder).iv_mainCategory);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        Log.i("size", mainCategoryItemsList.size() + "");
        return mainCategoryItemsList.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Integer)v.getTag());
        }
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder{
        TextView tv_mainCategory;
        ImageView iv_mainCategory;

        public ContentViewHolder(@NonNull View view) {
            super(view);
            Log.i("tag", "viewholder");
            tv_mainCategory = (TextView) view.findViewById(R.id.tv_main_category_item );
            iv_mainCategory = (ImageView) view.findViewById(R.id.iv_main_category_item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
