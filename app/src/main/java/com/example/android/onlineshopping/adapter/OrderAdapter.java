package com.example.android.onlineshopping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.dataItem.OrderHistoryInfo;
import com.example.android.onlineshopping.dataItem.OrderInfo;

import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.HistoryViewHolder> implements View.OnClickListener {
    private Context context;
    private List<OrderHistoryInfo> historyList;
    private OnItemClickListener mItemClickListener;


    public OrderAdapter(Context context, List<OrderHistoryInfo> historyList) {
        this.context = context;
        this.historyList = historyList;
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        HistoryViewHolder holder;
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        view.setOnClickListener(this);
        holder = new HistoryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.tv_order_history_id.setText("# " + historyList.get(position).getOrderid());
        holder.tv_order_history_time.setText(historyList.get(position).getPlacedon());
        holder.tv_order_history_price.setText("$ " + historyList.get(position).getPaidprice());

        switch (historyList.get(position).getOrderstatus()){
            case "1":
                holder.tv_order_history_track.setText("Order Confirm");
                break;
            case "2":
                holder.tv_order_history_track.setText("Order Dispatch");
                break;
            case "3":
                holder.tv_order_history_track.setText("Order On the Way");
                break;
            case "4":
                holder.tv_order_history_track.setText("Order Delivered");
                break;
        }

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    @Override
    public void onClick(View view) {
        if(mItemClickListener != null){
            mItemClickListener.onItemClick(view, (Integer) view.getTag());
        }

    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_order_history_id;
        private TextView tv_order_history_time;
        private TextView tv_order_history_price;
        private TextView tv_order_history_track;

        public HistoryViewHolder(View itemView) {
            super( itemView );

            tv_order_history_id = itemView.findViewById(R.id.tv_order_history_id);
            tv_order_history_time = itemView.findViewById(R.id.tv_order_history_time);
            tv_order_history_price = itemView.findViewById(R.id.tv_order_history_price);
            tv_order_history_track = itemView.findViewById(R.id.tv_order_history_track);

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, getLayoutPosition());
                }
            } );
        }
    }




    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        mItemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
