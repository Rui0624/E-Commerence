package com.example.android.onlineshopping.ui.product;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.database.DatabaseManager;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class ProductDetailFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_product_pic_detail;
    private ImageButton btn_like;
    private TextView tv_product_detail_name, tv_product_detail_price, tv_product_description, tv_rating;
    private Button btn_add_cart;
    private MaterialRatingBar rb_rating;
    private String id, name, price, description, image;
    private DatabaseManager mDatabaseManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_product_detail, container, false );

        //open the database
        mDatabaseManager = new DatabaseManager(getContext());
        mDatabaseManager.openDatabase();

        initView(view);

        return view;
    }

    private void initView(View view){
        iv_product_pic_detail = view.findViewById(R.id.iv_product_pic_detail);

        btn_like = view.findViewById(R.id.btn_like);
        btn_like.setOnClickListener(this);

        tv_product_detail_name = view.findViewById(R.id.tv_product_detail_name);
        tv_product_detail_price = view.findViewById(R.id.tv_product_detail_price);
        tv_product_description = view.findViewById(R.id.tv_product_description);
        tv_rating = view.findViewById(R.id.tv_rating);
        rb_rating = view.findViewById(R.id.rb_rating);

        btn_add_cart = view.findViewById(R.id.btn_add_cart);
        btn_add_cart.setOnClickListener(this);

        Bundle bundle = getArguments();

        if(bundle != null){
            id = bundle.getString("id");
            name = bundle.getString("name");
            price = bundle.getString("price");
            description = bundle.getString("description");
            image = bundle.getString("image");

            tv_product_detail_name.setText(name);
            tv_product_detail_price.setText("$" + price);
            tv_product_description.setText(description);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_not_available);
            Glide.with(getContext()).setDefaultRequestOptions(requestOptions).load(image).into(iv_product_pic_detail);
        }

        rb_rating.setOnRatingChangeListener( new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                tv_rating.setText(String.valueOf(rating));
            }
        } );

        if(mDatabaseManager.verifyItemWish(id, SharedPreferencesUtil.getSp(getContext()).getString("id", null)) == -1){
            btn_like.setImageResource(R.drawable.ic_like_unselected);
            btn_like.setTag(R.id.tag_iswish, "unwished");
        } else {
            btn_like.setImageResource(R.drawable.ic_like);
            btn_like.setTag(R.id.tag_iswish, "wished");
        }
    }

    @Override
    public void onClick(View v) {
        int position = v.getId();
        switch (position){
            case R.id.btn_add_cart:
                int quantity = mDatabaseManager.verifyItemInCart(name
                        , SharedPreferencesUtil.getSp(getContext()).getString("id", null));
                if(quantity == -1){
                    mDatabaseManager.addItemToCart(SharedPreferencesUtil.getSp(getContext()).getString("id"
                            , null), id, name, 1, price, image);
                    EventBus.getDefault().post(1);
                }else{
                    mDatabaseManager.updataCartQuantity(quantity + 1, name
                            , SharedPreferencesUtil.getSp(getContext()).getString("id", null));
                }

                Log.i("list size", mDatabaseManager.getCartList(SharedPreferencesUtil.getSp(getContext()).getString("id", null)).size() + "");

                Toast.makeText(getContext(), "The item has been added into cart", Toast.LENGTH_SHORT).show();

            case R.id.btn_like:
                if(btn_like.getTag(R.id.tag_iswish).equals("unwish")){
                    btn_like.setImageResource(R.drawable.ic_like);
                    btn_like.setTag(R.id.tag_iswish, "wished");
                    mDatabaseManager.addItemToWish(SharedPreferencesUtil.getSp(getContext()).getString("id", null)
                            , id, name, 1, price, image);

                }else {
                    btn_like.setImageResource(R.drawable.ic_like_unselected);
                    btn_like.setTag(R.id.tag_iswish, "unwish");
                    mDatabaseManager.deleteItemWish(SharedPreferencesUtil.getSp(getContext()).getString("id", null), id);
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDatabaseManager != null){
            mDatabaseManager.closeDatabase();
        }
    }
}
