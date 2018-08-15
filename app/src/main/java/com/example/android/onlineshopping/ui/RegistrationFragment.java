package com.example.android.onlineshopping.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;


public class RegistrationFragment extends Fragment implements View.OnClickListener {
    private ImageView iv_close;
    private EditText et_firstname, et_lastname, et_address, et_mobile, et_email, et_password;
    private CheckBox cbx_agree;
    private Button btn_signup;
    private TextView tv_gotologin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_signup, container, false );

        //initialize the views and set click listener
        initView(view);

        return view;
    }

    private void initView(View view){
        iv_close = view.findViewById(R.id.iv_close);
        et_firstname = view.findViewById(R.id.et_firstname);
        et_lastname = view.findViewById(R.id.et_lastname);
        et_address = view.findViewById(R.id.et_address);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        cbx_agree = view.findViewById(R.id.cbx_agree);

        btn_signup = view.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);

        tv_gotologin = view.findViewById(R.id.tv_gotologin);
        tv_gotologin.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        int position = v.getId();

        switch (position){
            case R.id.btn_signup:
            {
                if(TextUtils.isEmpty(et_firstname.getText().toString()) || TextUtils.isEmpty(et_lastname.getText().toString())
                        || TextUtils.isEmpty(et_address.getText().toString()) || TextUtils.isEmpty(et_mobile.getText().toString())
                        || TextUtils.isEmpty(et_email.getText().toString()) || TextUtils.isEmpty(et_password.getText().toString())){
                    Toast.makeText(getActivity().getBaseContext(), R.string.sign_up_empty, Toast.LENGTH_SHORT).show();
                }else if(et_mobile.getText().toString().length() != 10){
                    Toast.makeText(getActivity().getBaseContext(), R.string.mobile_invaild, Toast.LENGTH_SHORT).show();
                }else if(et_password.getText().toString().length() < 6){
                    Toast.makeText(getActivity().getBaseContext(), R.string.password_invaild, Toast.LENGTH_SHORT).show();
                }else if (!cbx_agree.isChecked()){
                    Toast.makeText(getActivity().getBaseContext(), R.string.please_agree, Toast.LENGTH_SHORT).show();
                }else {
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity().getBaseContext(), response, Toast.LENGTH_SHORT).show();
                            et_firstname.setText("");
                            et_lastname.setText("");
                            et_address.setText("");
                            et_email.setText("");
                            et_mobile.setText("");
                            et_password.setText("");
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity().getBaseContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    };

                    StringRequest stringRequest = VolleyHandler.getInstance().registerRequest(et_firstname.getText().toString(), et_lastname.getText().toString()
                            , et_address.getText().toString(), et_mobile.getText().toString(), et_email.getText().toString(), et_password.getText().toString()
                            , listener, errorListener);

                    MainControllor.getAppInstance().addToRequestQueue(stringRequest, "StringRequest");
                }
                break;
            }
            case R.id.tv_gotologin:{
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new LoginFragment(), "Logfrg")
                        .addToBackStack(null)
                        .commit();

                break;
            }
        }


    }
}
