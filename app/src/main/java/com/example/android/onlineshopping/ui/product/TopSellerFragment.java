package com.example.android.onlineshopping.ui.product;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.adapter.SellerAdapter;
import com.example.android.onlineshopping.dataItem.SellerInfo;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TopSellerFragment extends Fragment {
    private static final String TAG = "TopSellerFragment";
    private RecyclerView rv_seller;
    private List<SellerInfo> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_top_seller, container, false );
        initView(view);

        getTopSellerAPI();
        return view;
    }

    private void initView(View view){
        rv_seller = view.findViewById(R.id.rv_seller);
    }

    private void getTopSellerAPI(){
        list = new ArrayList<>();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Top sellers");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        SellerInfo sellerInfo = new SellerInfo(jsonObject1.getString("id"),
                                jsonObject1.getString("sellername"),
                                jsonObject1.getString("sellerdeal"),
                                jsonObject1.getString("sellerrating"),
                                jsonObject1.getString("sellerlogo"));

                        list.add(sellerInfo);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                iniRecyclerView(list);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i( TAG, "onErrorResponse: error" );
            }
        };

        JsonObjectRequest jsonObjectRequest = VolleyHandler.getInstance().sellerRequestObject(listener, errorListener);
        MainControllor.getAppInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void iniRecyclerView(List<SellerInfo> list){
        SellerAdapter adapter = new SellerAdapter(getContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_seller.setLayoutManager(layoutManager);
        rv_seller.setAdapter(adapter);
    }

}
