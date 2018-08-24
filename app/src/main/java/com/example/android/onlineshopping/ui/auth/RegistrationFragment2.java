package com.example.android.onlineshopping.ui.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;

public class RegistrationFragment2 extends Fragment implements View.OnClickListener {
    private EditText et_password;
    private Button btn_create_account;
    private String firstname;
    private String lastname;
    private String address;
    private String mobile;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_signup2, container, false );
        et_password = view.findViewById(R.id.et_password);
        btn_create_account = view.findViewById(R.id.btn_create_account);
        Bundle bundle = getArguments();
        firstname = bundle.getString("firstname");
        lastname = bundle.getString("lastname");
        address = bundle.getString("address");
        mobile = bundle.getString("mobile");
        email = bundle.getString("email");

        Log.i("tag", firstname + lastname + address + mobile + email);

        btn_create_account.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {

        if(TextUtils.isEmpty(et_password.getText().toString())){
            Toast.makeText(getActivity().getBaseContext(), R.string.sign_up_empty, Toast.LENGTH_SHORT).show();
            return;
        }else if(et_password.getText().toString().length() < 6){
            Toast.makeText( getContext(), "The password should be at least 6 digits", Toast.LENGTH_SHORT ).show();

        }else {
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getActivity().getBaseContext(), response, Toast.LENGTH_SHORT).show();
                    et_password.setText("");
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity().getBaseContext(), error.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.i("error message", error.getMessage());
                }
            };

            StringRequest stringRequest = VolleyHandler.getInstance().registerRequest(firstname, lastname
                    , address, mobile, email, et_password.getText().toString()
                    , listener, errorListener);


            MainControllor.getAppInstance().addToRequestQueue(stringRequest);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new LoginFragment()).commit();
        }
    }
}
