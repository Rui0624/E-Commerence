package com.example.android.onlineshopping.network;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class VolleyHandler {
    private static final String TAG = "VolleyHandler";
    private static VolleyHandler instance;
    private final String BASE_URL = "http://rjtmobile.com/aamir/e-commerce/android-app/";

    private VolleyHandler(){

    }

    public static VolleyHandler getInstance(){
        if(instance == null){
            synchronized (VolleyHandler.class){
                if(instance == null){
                    instance = new VolleyHandler();
                }
            }
        }
        return instance;
    }

    /**
     * Create the Login Url with the base url and mobile, password
     * initialize the JsonArrayRequest with the parameters and return it.
     * @param mobile
     * @param password
     * @param listener
     * @param errorListener
     * @return JsonArrayRequest
     */
    public JsonArrayRequest loginRequest(String mobile, String password, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        //http://rjtmobile.com/aamir/e-commerce/android-app/shop_login.php?mobile=55565454&password=7011
        String loginUrl = BASE_URL + "shop_login.php?" + "mobile=" + mobile + "&password=" + password;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(loginUrl, listener, errorListener);
        return jsonArrayRequest;
    }

    /**
     * Create the register Url with the base url and parameters
     * initialize the StringRequest with the parameters and return it.
     * @param firstname
     * @param lastname
     * @param address
     * @param mobile
     * @param email
     * @param password
     * @param listener
     * @param errorListener
     * @return StringRequest
     */

    public StringRequest registerRequest(String firstname, String lastname, String address, String mobile, String email, String password,
                                  Response.Listener<String> listener, Response.ErrorListener errorListener){
        //http://rjtmobile.com/aamir/e-commerce/android-app/shop_reg.php?fname=aamir&lname=husain&address=noida& email=aa@gmail.com&mobile=55565454&password=7011
        String registerUrl = BASE_URL + "shop_reg.php?" + "fname=" + firstname + "&lname="
                + lastname + "&address=" + address + "&email=" + email + "&mobile="
                + mobile + "&password=" + password;
        StringRequest stringRequest = new StringRequest(registerUrl, listener, errorListener);
        return stringRequest;
    }

    public StringRequest updateProfileRequest(String firstname, String lastname, String address, String mobile, String email,
                                              Response.Listener<String> listener, Response.ErrorListener errorListener){
        //http://rjtmobile.com/aamir/e-commerce/android-app/edit_profile.php?fname=aamir&lname=husain&address=noida& email=aa@gmail.com&mobile=55565454
        String updateProfileUrl = BASE_URL + "edit_profile.php?" + "fname=" + firstname + "&lname="
                + lastname + "&address=" + address + "&email=" + email + "&mobile="
                + mobile;

        StringRequest stringRequest = new StringRequest(updateProfileUrl, listener, errorListener);
        return stringRequest;
    }

    public JsonObjectRequest resetPasswordRequest(String mobile, String curPassword, String newPassword,
                                                  Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        //http://rjtmobile.com/aamir/e-commerce/android-app/shop_reset_pass.php?&mobile=55565454&password=2&newpassword=456
        String resetPasswordUrl = BASE_URL + "shop_reset_pass.php?" + "&mobile=" + mobile + "&password=" + curPassword + "&newpassword=" + newPassword;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(resetPasswordUrl, null,listener, errorListener);
        return jsonObjectRequest;
    }
}
