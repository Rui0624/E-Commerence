package com.example.android.onlineshopping.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.utils.MainControllor;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText et_mobile, et_password;
    private CheckBox cbx_remember;
    private TextView tv_forgotpassword, tv_gotosignup;
    private Button btn_signin;
    private ImageView iv_close;
    private static final String TAG = "Fragment Login";
    private ProgressDialog progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //initialize the views and set the click listeners
        initView( view );


        return view;
    }

    //initialize the view
    private void initView(View view){
        et_mobile = view.findViewById(R.id.et_mobile);
        et_password = view.findViewById( R.id.et_password );
        cbx_remember = view.findViewById( R.id.cbx_remember );

        tv_forgotpassword = view.findViewById(R.id.tv_forgot_password);
        tv_forgotpassword.setOnClickListener(this);

        tv_gotosignup = view.findViewById( R.id.tv_go_to_sign_up );
        tv_gotosignup.setOnClickListener(this);

        btn_signin = view.findViewById( R.id.btn_sign_in);
        btn_signin.setOnClickListener(this);

        iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);

        String remember = SharedPreferencesUtil.getRemember(getContext());

        if(remember != null){
            et_mobile.setText(remember);
            cbx_remember.setChecked(true);
        }else {
            cbx_remember.setChecked(false);
        }

    }


    @Override
    public void onClick(View v) {
        int position = v.getId();
        switch (position){
            case R.id.btn_sign_in:
            {
                //initilize the progress dialog while log in
                progress = new ProgressDialog(getActivity());
                progress.setTitle("Loging");
                progress.setCancelable(false);
                progress.show();

                //save the Remember of mobile for log in
                saveCredentials();

                if(Util.getUtilInstance().isValidMobile(et_mobile.getText().toString())){
                    progress.dismiss();
                    Toast.makeText(getContext(), R.string.mobile_number_invalid, Toast.LENGTH_SHORT).show();
                }
                //check whether the mobile for log in is valid
                else if(et_mobile.getText().toString().length() != 10){
                    progress.dismiss();
                    Toast.makeText(getContext(), R.string.mobile_invaild, Toast.LENGTH_SHORT).show();
                } else if(et_password.getText().toString().length() < 6){
                    progress.dismiss();
                    Toast.makeText(getContext(), R.string.password_invaild, Toast.LENGTH_SHORT).show();
                } else {
                    Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>(){
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                /*
                                JsonObject:
                                [{"msg":"success","id":"1249","firstname":"patel","lastname":"husain","email":"vansh3vns@gmail.com","mobile":"55565454","appapikey ":"4e200ca562c8f3dcda5f025b72508644"}]
                                 */
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String msg = jsonObject.getString("msg");
                                    String id = jsonObject.getString("id");
                                    String firstname = jsonObject.getString("firstname");
                                    String lastname = jsonObject.getString("lastname");
                                    String email = jsonObject.getString("email");
                                    String mobile = jsonObject.getString("mobile");
                                    String appapikey = jsonObject.getString("appapikey");

                                    SharedPreferencesUtil.setUserInfo(getContext(), msg, id, firstname, lastname, email, mobile, appapikey);

                                    progress.dismiss();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("error", error.getMessage());
                            Toast.makeText(getContext(), R.string.log_in_error, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    };

                    //create the Request Queue and add the login Request into the Queue
                    JsonArrayRequest jsonArrayRequest = VolleyHandler.getInstance()
                            .loginRequest(et_mobile.getText().toString(), et_password.getText().toString(), listener, errorListener);
                    MainControllor.getAppInstance().addToRequestQueue(jsonArrayRequest, "JsonArrayRequest");
                }
                break;
            }
            case R.id.tv_go_to_sign_up:
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new RegistrationFragment(), "signup")
                        .addToBackStack(null)
                        .commit();
                break;
            }

            case R.id.iv_close:
            {
                saveCredentials();
                //close app
                System.exit(0);
                break;
            }

        }
    }

    public void saveCredentials(){

        if(cbx_remember.isChecked())
            SharedPreferencesUtil.setRemember(getContext(), et_mobile.getText().toString());

    }
}
