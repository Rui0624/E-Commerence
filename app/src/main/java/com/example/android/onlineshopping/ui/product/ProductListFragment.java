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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.adapter.ProductsAdapter;
import com.example.android.onlineshopping.dataItem.ProductsListItem;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductListFragment extends Fragment {
    RecyclerView rv_product_list;
    List<ProductsListItem> productsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_product_list, container, false );
        rv_product_list = view.findViewById(R.id.rv_product_list);

        getProductListAPI();
        return view;
    }

    private void getProductListAPI(){
        String cid = getArguments().getString("cid");
        String scid = getArguments().getString("scid");

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                productsList = new ArrayList<>();
                try {
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ProductsListItem productsListItem = new ProductsListItem(jsonObject1.getString("id")
                                , jsonObject1.getString("pname"), jsonObject1.getString("quantity")
                                , jsonObject1.getString("prize"), jsonObject1.getString("discription")
                                , jsonObject1.getString("image"));

                        Log.i("product info", productsListItem.getId());
                        Log.i("product info", productsListItem.getPname());
                        Log.i("product info", productsListItem.getQuantity());
                        Log.i("product info", productsListItem.getDiscription());
                        Log.i("product info", productsListItem.getImage());
                        productsList.add(productsListItem);
                    }

                    initRecyclerView(productsList);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest jsonObjectRequest = VolleyHandler.getInstance().productListObject(cid, scid
                , SharedPreferencesUtil.getSp(getContext()).getString("appapikey"
                        , null), SharedPreferencesUtil.getSp(getContext()).getString("id", null), listener, errorListener);
        MainControllor.getAppInstance().addToRequestQueue(jsonObjectRequest, "ProductListFgt");
    }

    private void initRecyclerView(List<ProductsListItem> list){
        ProductsAdapter adapter = new ProductsAdapter(getContext(), list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
        rv_product_list.setLayoutManager(layoutManager);
        rv_product_list.setAdapter(adapter);

        adapter.setOnItemClickListener( new ProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("image", productsList.get(position).getImage());
                bundle.putString("name", productsList.get(position).getPname());
                bundle.putString("price", productsList.get(position).getPrice());
                bundle.putString("description", productsList.get(position).getDiscription());
                bundle.putString("id", productsList.get(position).getId());

                productDetailFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, productDetailFragment, "productDetailFragment")
                        .addToBackStack(null)
                        .commit();
            }
        } );
    }




}
