package com.example.android.onlineshopping.ui.product;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.onlineshopping.HomeActivity;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.adapter.MainCategoryAdapter;
import com.example.android.onlineshopping.adapter.SubCategoryAdapter;
import com.example.android.onlineshopping.dataItem.MainCategoryItem;
import com.example.android.onlineshopping.dataItem.SubCategoryItem;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {
    private RecyclerView rv_category;
    private ProgressDialog progress;
    private List<MainCategoryItem> mainCategoryList;
    private List<SubCategoryItem> subCategoryList;
    private static final String TAG = "Category Fragment";
    private int mainCategoryPicked = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_category, container, false );
        rv_category = (RecyclerView) view.findViewById(R.id.rv_category);
        getCategoryAPI();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        } );

        return view;
    }



    private void getCategoryAPI(){
        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setCancelable(false);
        progress.show();


        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.dismiss();
                mainCategoryList = new ArrayList<>();
                JSONObject jsonObject = (JSONObject) response;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("category");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObjectInArray = jsonArray.getJSONObject(i);
                        MainCategoryItem mainCategoryItem = new MainCategoryItem(
                                jsonObjectInArray.getString("cid"),
                                jsonObjectInArray.getString("cname"),
                                jsonObjectInArray.getString("cdiscription"),
                                jsonObjectInArray.getString("cimagerl"));

                        mainCategoryList.add(mainCategoryItem);

                    }

                    initRecycleView(mainCategoryList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage());
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest categoryRequest = VolleyHandler.getInstance().categorysObject(
                SharedPreferencesUtil.getSp(getContext()).getString("appapikey", null),
                SharedPreferencesUtil.getSp(getContext()).getString("id", null),
                listener, errorListener);

        MainControllor.getAppInstance().addToRequestQueue(categoryRequest, "mainCategoryList");
    }


    private void initRecycleView(List<MainCategoryItem> list){
        MainCategoryAdapter adapter = new MainCategoryAdapter(getContext(), list);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_category.setLayoutManager(layoutManager);
        rv_category.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener( new MainCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getSubCategoryAPI(position);
                HomeActivity activity = (HomeActivity) getActivity();
                if(activity != null){
                    activity.updateTitile(mainCategoryList.get(position).getCname());
                    mainCategoryPicked = position;
                }

            }
        } );
    }

    private void getSubCategoryAPI(int position){
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                subCategoryList = new ArrayList<>();
                JSONObject jsonObject = (JSONObject) response;
                try{
                    JSONArray jsonArray = jsonObject.getJSONArray("subcategory");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        SubCategoryItem subCategoryItem = new SubCategoryItem(
                                jsonObject1.getString("scid"),
                                jsonObject1.getString("scname"),
                                jsonObject1.getString("scdiscription"),
                                jsonObject1.getString("scimageurl"));
                        subCategoryList.add(subCategoryItem);
                    }

                    resetRecyclerViewToSub(subCategoryList, mainCategoryList);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };
        //String id, String api_key, String user_id
        JsonObjectRequest subCategoryRequest = VolleyHandler.getInstance().subCategorysObject(mainCategoryList.get(position).getCid(),
                        SharedPreferencesUtil.getSp(getContext()).getString("appapikey", null),
                        SharedPreferencesUtil.getSp(getContext()).getString("id", null), listener, errorListener);

        MainControllor.getAppInstance().addToRequestQueue(subCategoryRequest, "subCategoryList");
    }

    private void resetRecyclerViewToSub(List<SubCategoryItem> subCategoryItemList, final List<MainCategoryItem> mainCategoryList){
        SubCategoryAdapter adapter = new SubCategoryAdapter(getContext(), subCategoryItemList, mainCategoryList);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv_category.setLayoutManager(layoutManager);
        rv_category.setAdapter(adapter);

        adapter.setOnItemClickListener( new SubCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ProductListFragment productListFragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cid", mainCategoryList.get(mainCategoryPicked).getCid());
                bundle.putString("scid", subCategoryList.get(position).getScid());
                productListFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.content, productListFragment, "ProductFgt")
                        .addToBackStack(null)
                        .commit();
            }
        } );
    }



    // TODO: Rename method, update argument and hook method into UI event

}
