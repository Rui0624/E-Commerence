package com.example.android.onlineshopping.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.dataItem.OrderInfo;
import com.example.android.onlineshopping.database.DatabaseManager;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private Context context;
    private List<OrderInfo> orderList;
    private DatabaseManager mDatabaseManager;


    public CartAdapter(Context context, List<OrderInfo> orderList) {
        this.context = context;
        this.orderList = orderList;
        mDatabaseManager = new DatabaseManager(context);
        mDatabaseManager.openDatabase();
    }


    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartViewHolder holder;
        if (viewType == -1){
            View view = LayoutInflater.from(context).inflate( R.layout.item_empty, parent, false);
            holder = new CartViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate( R.layout.item_cart, parent, false);
            holder = new CartViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        if(orderList.size() > 0){
            Log.i("orderlist_position", position + "");
            Glide.with(context).load(orderList.get(position).getImage()).into(holder.iv_cart_item_pic);

            holder.tv_item_name.setText(orderList.get(position).getPname());
            holder.tv_item_price.setText("$" + orderList.get(position).getPrize());
            holder.tv_item_quantity.setText(String.valueOf(orderList.get(position).getQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size() > 0 ? orderList.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(orderList.size() <= 0){
            return -1;
        }
        return super.getItemViewType( position );
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_cart_item_pic;
        private TextView tv_item_name, tv_item_price, tv_item_quantity;
        private ImageButton ib_item_add, ib_item_minus;

        public CartViewHolder(@NonNull View itemView) {
            super( itemView );
            iv_cart_item_pic = itemView.findViewById(R.id.iv_cart_item_pic);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_item_price = itemView.findViewById(R.id.tv_item_price);
            tv_item_quantity = itemView.findViewById(R.id.tv_item_quantity);
            ib_item_add = itemView.findViewById(R.id.ib_item_add);
            ib_item_minus = itemView.findViewById(R.id.ib_item_minus);

            if(ib_item_add != null){
                ib_item_add.setOnClickListener(this);
                ib_item_minus.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            int position = v.getId();
            switch (position){
                case R.id.ib_item_add:
                    int curAddQuantity = Integer.parseInt(tv_item_quantity.getText().toString());
                    curAddQuantity += 1;

                    //update the textview and list
                    tv_item_quantity.setText(String.valueOf(curAddQuantity));
                    orderList.get(getLayoutPosition()).setQuantity(curAddQuantity);

                    //update the database
                    mDatabaseManager.updataCartQuantity(curAddQuantity
                            , orderList.get(getLayoutPosition()).getPname()
                            , SharedPreferencesUtil.getSp(context).getString("id", null));
                    EventBus.getDefault().post(true);
                    break;

                case R.id.ib_item_minus:
                    int curMinusQuantity = Integer.parseInt(tv_item_quantity.getText().toString());
                    curMinusQuantity -= 1;

                    //update the textview and list
                    tv_item_quantity.setText(String.valueOf(curMinusQuantity));
                    orderList.get(getLayoutPosition()).setQuantity(curMinusQuantity);

                    //update the database
                    mDatabaseManager.updataCartQuantity(curMinusQuantity
                            , orderList.get(getLayoutPosition()).getPname()
                            , SharedPreferencesUtil.getSp(context).getString("id", null));

                    if(tv_item_quantity.getText().toString().equals("0")){
                        AlertDialog dialog = new AlertDialog.Builder(context).setNegativeButton( "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_item_quantity.setText("1");
                                orderList.get(getLayoutPosition()).setQuantity(1);

                                mDatabaseManager.updataCartQuantity(orderList.get(getLayoutPosition()).getQuantity()
                                        , orderList.get(getLayoutPosition()).getPname()
                                        , SharedPreferencesUtil.getSp(context).getString("id", null));
                            }
                        }).setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabaseManager.deleteItemCart(SharedPreferencesUtil.getSp(context).getString("id", null)
                                        , orderList.get(getLayoutPosition()).getPid());
                                orderList.remove(getLayoutPosition());
                                notifyDataSetChanged();
                                EventBus.getDefault().post(-1);
                            }
                        } ).setMessage("Do you want remove this item from cart?").create();
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    EventBus.getDefault().post(true);
                    break;
            }
        }
    }

}
