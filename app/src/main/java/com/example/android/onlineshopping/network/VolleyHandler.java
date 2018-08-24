package com.example.android.onlineshopping.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyHandler {
    private static final String TAG = "VolleyHandler";
    private static VolleyHandler instance;
    private final String BASE_URL = "http://rjtmobile.com/aamir/e-commerce/android-app/";
    private final String BASE_URL_CATEGORY = "http://rjtmobile.com/ansari/shopingcart/androidapp/";
    private final String BASE_URL_ORDER = "http://rjtmobile.com/aamir/e-commerce/android-app/";

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

    public JsonObjectRequest categorysObject(String api_key, String user_id, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){

        //http://rjtmobile.com/ansari/shopingcart/androidapp/cust_category.php?api_key=7057bc8168b477b9420aeca3fda3c868&user_id=1217

        String categoryUrl = BASE_URL_CATEGORY + "cust_category.php?" + "api_key=" + api_key + "&user_id=" + user_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(categoryUrl, null, listener, errorListener);
        return jsonObjectRequest;
    }

    public JsonObjectRequest subCategorysObject(String id, String api_key, String user_id, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        //http://rjtmobile.com/ansari/shopingcart/androidapp/cust_sub_category.php?Id=107&api_key=7057bc8168b477b9420aeca3fda3c868&user_id=1217
        String subCategoryUrl = BASE_URL_CATEGORY + "cust_sub_category.php?" + "Id=" + id + "&api_key=" + api_key + "&user_id=" + user_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(subCategoryUrl, null,listener, errorListener);
        return jsonObjectRequest;
    }

    public JsonObjectRequest productListObject(String cid, String scid, String api_key, String user_id, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        //http://rjtmobile.com/ansari/shopingcart/androidapp/product_details.php?cid=107&scid=205&api_key=7a89e02e90239a0fa2d17adb209ade18&user_id=1249
        String productListUrl = BASE_URL_CATEGORY + "product_details.php?" + "cid=" + cid + "&scid=" + scid + "&api_key=" + api_key + "&user_id=" + user_id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(productListUrl, null, listener, errorListener);
        return jsonObjectRequest;
    }

    public JsonObjectRequest couponRequestObject(String apikey, String userId, String couponno, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        //http://rjtmobile.com/aamir/e-commerce/android-app/coupon.php?api_key=7057bc8168b477b9420aeca3fda3c868&user_id=1217&couponno=2147483648
        String couponRequestUrl = BASE_URL + "coupon.php?" + "api_key=" + apikey + "&user_id=" + userId + "&couponno=" + couponno;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(couponRequestUrl, null, listener, errorListener);
        return jsonObjectRequest;
    }

    public JsonObjectRequest paymentRequestObject(String pid, String pname, String quantity, String price,
                                                  String api_key, String user_id, String user_name, String mobile,
                                                  String email, Response.Listener<JSONObject> listener,
                                                  Response.ErrorListener errorListener,
                                                  final Map<String, String> paraHash){
        //http://rjtmobile.com/aamir/e-commerce/android-app/orders.php?&item_id=701&item_names=laptop&item_quantity=1&final_price=100000&api_key=7057bc8168b477b9420aeca3fda3c868&user_id=1217&user_name=Aamir&billingadd=Noida&deliveryadd=Noida&mobile=55565454&email=aa@gmail.com
        String paymentRequestUrl = BASE_URL_ORDER + "orders.php?" + "&item_id=" +
                pid + "&item_names=" + pname + "&item_quantity=" + quantity +
                "&final_price=" + price + "&api_key=" + api_key + "&user_id=" + user_id +
                "&user_name=" + user_name + "&billingadd=Noida&deliveryadd=1840WesselCt,St.Charles,Il,60174" +
                "&mobile=" + mobile + "&email=" + email;

        Log.i("paymentRequestUrl", paymentRequestUrl);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(paymentRequestUrl, null, listener, errorListener){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        if(paraHash == null)
                            return null;
                        Map<String, String> params = new HashMap<>();
                        for(String key : paraHash.keySet()){
                            params.put(key, paraHash.get(key));
                        }

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        paraHash.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
        };

        return jsonObjectRequest;
    };

    //http://rjtmobile.com/aamir/e-commerce/android-app/order_history.php?api_key=7057bc8168b477b9420aeca3fda3c868&user_id=1217&mobile=55565454
    public JsonObjectRequest orderHistoryRequestObject(String api_key, String user_id, String mobile,
                                                       Response.Listener listener,
                                                       Response.ErrorListener errorListener){
        String orderHistoryUrl = BASE_URL + "order_history.php?" + "api_key=" + api_key + "&user_id=" + user_id + "&mobile=" + mobile;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(orderHistoryUrl, null, listener, errorListener);

        return jsonObjectRequest;
    }


    public JsonObjectRequest sellerRequestObject(Response.Listener listener, Response.ErrorListener errorListener){
        String sellerUrl = "http://rjtmobile.com/aamir/e-commerce/android-app/shop_top_sellers.php?";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(sellerUrl, null, listener, errorListener);
        return jsonObjectRequest;
    }

}
