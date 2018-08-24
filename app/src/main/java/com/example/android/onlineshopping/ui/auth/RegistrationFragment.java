package com.example.android.onlineshopping.ui.auth;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.example.android.onlineshopping.ui.auth.LoginFragment;
import com.example.android.onlineshopping.utils.MainControllor;


public class RegistrationFragment extends Fragment implements View.OnClickListener {
    private EditText et_firstname, et_lastname, et_address, et_mobile, et_email;
    private Button btn_signup;
    private TextView tv_gotologin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_signup1, container, false );

        //initialize the views and set click listener
        initView(view);

        return view;
    }

    private void initView(View view){
        et_firstname = view.findViewById(R.id.et_firstname);
        et_lastname = view.findViewById(R.id.et_lastname);
        et_address = view.findViewById(R.id.et_address);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_email = view.findViewById(R.id.et_email);

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
                        || TextUtils.isEmpty(et_email.getText().toString())){
                    Toast.makeText(getActivity().getBaseContext(), R.string.sign_up_empty, Toast.LENGTH_SHORT).show();
                    break;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()){
                    Toast.makeText( getContext(), "Please input a validated email address", Toast.LENGTH_SHORT ).show();
                    return;
                }else if(!PhoneNumberUtils.isGlobalPhoneNumber(et_mobile.getText().toString()) || !Patterns.PHONE.matcher(et_mobile.getText().toString()).matches()){
                    Toast.makeText( getContext(), "Please input a validated phone number", Toast.LENGTH_SHORT ).show();
                    return;
                }else if(et_mobile.getText().toString().length() != 10){
                    Toast.makeText(getActivity().getBaseContext(), R.string.mobile_invaild, Toast.LENGTH_SHORT).show();
                    break;
                }

                RegistrationFragment2 registrationFragment2 = new RegistrationFragment2();
                Bundle bundle = new Bundle();
                bundle.putString("firstname", et_firstname.getText().toString());
                bundle.putString("lastname", et_lastname.getText().toString());
                bundle.putString("address", et_address.getText().toString());
                bundle.putString("mobile", et_mobile.getText().toString());
                bundle.putString("email", et_email.getText().toString());
                et_firstname.setText("");
                et_lastname.setText("");
                et_address.setText("");
                et_email.setText("");
                et_mobile.setText("");

                registrationFragment2.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, registrationFragment2, "Regfrg2")
                        .addToBackStack(null).commit();

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
