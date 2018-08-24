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
import com.example.android.onlineshopping.dataItem.SubCategoryItem;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubContentViewHolder> implements View.OnClickListener{
    private Context context;
    private List<SubCategoryItem> subCategoryItemList;
    private List<MainCategoryItem> mainCategoryItemList;
    private OnItemClickListener mOnItemClickListener;


    public SubCategoryAdapter(Context context, List<SubCategoryItem> subCategoryItemList, List<MainCategoryItem> mainCategoryItemList) {
        this.context = context;
        this.subCategoryItemList = subCategoryItemList;
        this.mainCategoryItemList = mainCategoryItemList;
    }



    @NonNull
    @Override
    public SubCategoryAdapter.SubContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        SubContentViewHolder holder;

        View view = LayoutInflater.from(context).inflate(R.layout.item_cardview_sub_category, parent, false);
        holder = new SubContentViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubContentViewHolder holder, int position) {
        Log.i("tag", "glide");
        holder.tv_subCategory.setText(subCategoryItemList.get(position).getScname());
        Glide.with(context).load(subCategoryItemList.get(position).getScimageurl()).into(holder.iv_subCategory);
        holder.itemView.setTag(position);
    }



    @Override
    public int getItemCount() {
        return subCategoryItemList.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, (Integer)v.getTag());
        }
    }

    public class SubContentViewHolder extends RecyclerView.ViewHolder{
        TextView tv_subCategory;
        ImageView iv_subCategory;

        public SubContentViewHolder(@NonNull View view) {
            super( view );
            tv_subCategory = (TextView) view.findViewById( R.id.tv_sub_category_item);
            iv_subCategory = (ImageView) view.findViewById(R.id.iv_sub_category_item);
        }
    }

    public void setOnItemClickListener(SubCategoryAdapter.OnItemClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
