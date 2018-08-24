package com.example.android.onlineshopping.ui.checkout;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.adapter.OrderAdapter;
import com.example.android.onlineshopping.dataItem.OrderHistoryInfo;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderFragment extends Fragment {
    private RecyclerView rv_order;
    private List<OrderHistoryInfo> order_history_list;
    ProgressDialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_order, container, false );
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.mytoolbar) ;
        TextView titleToolbar = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
        titleToolbar.setText(R.string.my_order);
        initView(view);
        getOrderHistoryAPI();
        return view;
    }

    private void initView(View view){
        rv_order = view.findViewById(R.id.rv_order);
    }

    private void getOrderHistoryAPI(){
        mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.show();

        order_history_list = new ArrayList<>();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Order history");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        OrderHistoryInfo orderHistoryInfo = new OrderHistoryInfo(jsonObject1.getString("orderid")
                                , jsonObject1.getString("orderstatus")
                                , jsonObject1.getString("name")
                                , jsonObject1.getString("billingadd")
                                , jsonObject1.getString("deliveryadd")
                                , jsonObject1.getString("mobile")
                                , jsonObject1.getString("email")
                                , jsonObject1.getString("itemid")
                                , jsonObject1.getString("itemname")
                                , jsonObject1.getString("itemquantity")
                                , jsonObject1.getString("totalprice")
                                , jsonObject1.getString("paidprice")
                                , jsonObject1.getString("placedon"));

                        order_history_list.add(orderHistoryInfo);
                    }

                    initRecyclerView();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("orderhistorylist", error.toString());
            }
        };

        JsonObjectRequest jsonObjectRequest = VolleyHandler.getInstance().orderHistoryRequestObject( SharedPreferencesUtil.getSp(getContext()).getString("appapikey", null),
                SharedPreferencesUtil.getSp(getContext()).getString("id", null),
                SharedPreferencesUtil.getSp(getContext()).getString("mobile", null), listener, errorListener);

        MainControllor.getAppInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void initRecyclerView(){
        Collections.sort(order_history_list, new Comparator(){

            @Override
            public int compare(Object o1, Object o2) {
                OrderHistoryInfo orderHistoryInfo1 = (OrderHistoryInfo) o1;
                OrderHistoryInfo orderHistoryInfo2 = (OrderHistoryInfo) o2;

                if (orderHistoryInfo1.getPlacedon().compareTo( orderHistoryInfo2.getPlacedon() ) > 0) {
                    return -1;
                }
                return 1;
            }
        });
        OrderAdapter adapter = new OrderAdapter(getContext(), order_history_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_order.setLayoutManager(linearLayoutManager);
        rv_order.setAdapter(adapter);

        mDialog.dismiss();



    }
}
