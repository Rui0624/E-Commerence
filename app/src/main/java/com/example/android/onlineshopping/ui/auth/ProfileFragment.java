package com.example.android.onlineshopping.ui.auth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView tv_pro_firstname, tv_pro_lastname, tv_pro_mobile, et_pro_email, tv_cur_mobile, et_cur_password, et_new_password;
    private Button btn_update_profile, btn_reset_password;
    private static final String TAG = "ProfileFragment";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_profile, container, false );
        initView(view);

        return view;
    }

    private void initView(View view){
        tv_pro_firstname = view.findViewById(R.id.tv_pro_firstname );
        tv_pro_lastname = view.findViewById(R.id.tv_pro_lastname );
        tv_pro_mobile = view.findViewById(R.id.tv_pro_mobile );
        et_pro_email = view.findViewById(R.id.et_pro_email);

        tv_cur_mobile = view.findViewById(R.id.tv_cur_mobile );
        et_cur_password = view.findViewById(R.id.et_cur_password);
        et_new_password = view.findViewById(R.id.et_new_password);

        tv_pro_firstname.setText(SharedPreferencesUtil.getSp(getContext()).getString("firstname", null));
        tv_pro_lastname.setText(SharedPreferencesUtil.getSp(getContext()).getString("lastname", null));
        tv_pro_mobile.setText(SharedPreferencesUtil.getSp(getContext()).getString("mobile", null));
        et_pro_email.setText(SharedPreferencesUtil.getSp(getContext()).getString("email", null));

        tv_cur_mobile.setText(SharedPreferencesUtil.getSp(getContext()).getString("mobile", null));

        btn_update_profile = view.findViewById(R.id.btn_update_profile);
        btn_update_profile.setOnClickListener(this);

        btn_reset_password = view.findViewById(R.id.btn_reset_password);
        btn_reset_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = v.getId();
        switch (position){
            case R.id.btn_update_profile:
            {
                SharedPreferencesUtil.updateUserInfo(getContext(), tv_pro_firstname.getText().toString(), tv_pro_lastname.getText().toString(), et_pro_email.getText().toString(), tv_pro_mobile.getText().toString());
                updateProfile();
                break;
            }

            case R.id.btn_reset_password:
            {
                resetPassword();
                break;
            }
        }
    }

    public void updateProfile(){
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i( TAG, "onResponse: " + tv_pro_firstname.getText().toString());
                //SharedPreferencesUtil.updateUserInfo(getContext(), et_pro_firstname.getText().toString(), et_pro_lastname.getText().toString(), et_pro_email.getText().toString(), et_pro_mobile.getText().toString());
                Log.i( TAG, "onResponse:"+ SharedPreferencesUtil.getSp(getContext()).getString("firstname", null) );

                Toast.makeText( getContext(), R.string.successfully_updated, Toast.LENGTH_SHORT ).show();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());
                Toast.makeText(getActivity().getBaseContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        };


        Log.i( TAG, "onResponse:"+ SharedPreferencesUtil.getSp(getContext()).getString("firstname", null) );
        StringRequest stringRequest = VolleyHandler.getInstance().updateProfileRequest(
                SharedPreferencesUtil.getSp(getContext()).getString("firstname", null),
                SharedPreferencesUtil.getSp(getContext()).getString("lastname", null),
                SharedPreferencesUtil.getSp(getContext()).getString("address", null),
                SharedPreferencesUtil.getSp(getContext()).getString("mobile", null),
                SharedPreferencesUtil.getSp(getContext()).getString("email", null),
                listener, errorListener);

        MainControllor.getAppInstance().addToRequestQueue(stringRequest, "updateProfileRequest");
    }

    public void  resetPassword(){

        if(TextUtils.isEmpty(et_cur_password.getText().toString())){
            Toast.makeText( getContext(), "Please input the current password", Toast.LENGTH_SHORT ).show();
            return;
        }else if(TextUtils.isEmpty(et_new_password.getText().toString())){
            Toast.makeText( getContext(), "Please input the new password", Toast.LENGTH_SHORT ).show();
            return;
        }else if(et_cur_password.getText().toString().equals(et_new_password.getText().toString())){
            Toast.makeText( getContext(), "The new password should not be same as old password", Toast.LENGTH_SHORT ).show();
            return;
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText( getContext(), R.string.reset_successfully, Toast.LENGTH_SHORT ).show();
                
                try{
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("msg");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject mJsonObject = jsonArray.getJSONObject(i);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, error.getMessage());
                Toast.makeText(getActivity().getBaseContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest jsonObjectRequest = VolleyHandler.getInstance().resetPasswordRequest(tv_cur_mobile.getText().toString(),
                et_cur_password.getText().toString(),
                et_new_password.getText().toString(), listener, errorListener);
        MainControllor.getAppInstance().addToRequestQueue(jsonObjectRequest, "resetPasswordRequest");

    }

}
