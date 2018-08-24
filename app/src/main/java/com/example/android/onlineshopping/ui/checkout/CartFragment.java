package com.example.android.onlineshopping.ui.checkout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalConfiguration;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.example.android.onlineshopping.Config.Config;
import com.example.android.onlineshopping.R;
import com.example.android.onlineshopping.adapter.CartAdapter;
import com.example.android.onlineshopping.dataItem.CouponInfo;
import com.example.android.onlineshopping.dataItem.OrderHistoryInfo;
import com.example.android.onlineshopping.dataItem.OrderInfo;
import com.example.android.onlineshopping.database.DatabaseManager;
import com.example.android.onlineshopping.network.VolleyHandler;
import com.example.android.onlineshopping.utils.MainControllor;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class CartFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE = 1234;

    public static final int PAYPAL_REQUEST_CODE = 7171;

    //use sandbox beause we are on test
    private static com.paypal.android.sdk.payments.PayPalConfiguration config =
            new com.paypal.android.sdk.payments.PayPalConfiguration()
                    .environment( com.paypal.android.sdk.payments.PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);


    private RecyclerView rv_cart;
    private Button btn_proceed_checkout;
    private DatabaseManager database_manager;
    private List<OrderInfo> item_list;
    private LinearLayout ll_checkout, ll_ordersum;
    private EditText et_coupon;
    private Button btn_coupon;
    private TextView tv_tax_total;
    private TextView tv_deli_total;
    private TextView tv_total;
    private Button btn_checkout_confirm;
    private Button btn_checkout_cancel;
    private TextView tv_orderid;
    private TextView tv_time;
    private TextView tv_address;
    private TextView tv_mobile;
    private TextView tv_payment;
    private Button btn_track;
    private CartAdapter adpter;
    private int deliveryCost;
    private List<CouponInfo> coupon_list;
    private List<OrderHistoryInfo> order_list;

    private int totalNeedToPay;

    private final String APT_TOKEN = "http://rjtmobile.com/aamir/braintree-paypal-payment/main.php";
    String token, amount;
    HashMap<String, String> paramsHash;

    String paymentType;
    String cardNumber;

    private static final String TAG = "CartFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_cart, container, false );

        initDatabase();
        initView(view);

        initRecycleView();

        return view;
    }

    public void initView(View view){
        rv_cart = view.findViewById(R.id.rv_cart);
        btn_proceed_checkout = view.findViewById(R.id.btn_proceed_checkout);
        btn_proceed_checkout.setOnClickListener(this);

        ll_checkout = view.findViewById(R.id.ll_checkout);
        et_coupon = view.findViewById(R.id.et_coupon);
        btn_coupon = view.findViewById(R.id.btn_coupon);
        btn_coupon.setOnClickListener(this);

        tv_tax_total = view.findViewById(R.id.tv_tax_total);
        tv_deli_total = view.findViewById(R.id.tv_deli_total);
        tv_total = view.findViewById(R.id.tv_total);
        btn_checkout_confirm = view.findViewById(R.id.btn_checkout_confirm);
        btn_checkout_confirm.setOnClickListener(this);

        btn_checkout_cancel = view.findViewById(R.id.btn_checkout_cancel);
        btn_checkout_cancel.setOnClickListener(this);

        ll_ordersum = view.findViewById(R.id.ll_ordersum);
        tv_orderid = view.findViewById(R.id.tv_orderid);
        tv_time = view.findViewById(R.id.tv_time);
        tv_address = view.findViewById(R.id.tv_address);
        tv_mobile = view.findViewById(R.id.tv_mobile);
        tv_payment = view.findViewById(R.id.tv_payment);
        btn_track = view.findViewById(R.id.btn_track);
        btn_track.setOnClickListener(this);
        
        new getToken().execute();
    }

    public void initDatabase(){
        database_manager = new DatabaseManager(getContext());
        database_manager.openDatabase();

        item_list = database_manager.getCartList(SharedPreferencesUtil.getSp(getContext()).getString("id", null));
        Log.i("item_list", item_list.size() +"");
    }

    public void initRecycleView(){
        adpter = new CartAdapter(getContext(), item_list);
        rv_cart.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv_cart.setAdapter(adpter);
    }


    @Override
    public void onClick(View v) {
        int position = v.getId();
        switch (position){
            case R.id.btn_proceed_checkout:
                ll_checkout.setAnimation( AnimationUtils.loadAnimation(getContext(), R.anim.bot_in));
                ll_checkout.setVisibility(View.VISIBLE);
                paymentTotal();
                break;
            case R.id.btn_checkout_cancel:
                ll_checkout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bot_out));
                ll_checkout.setVisibility(View.GONE);
                break;
            case R.id.btn_checkout_confirm:
                onBraintreeSystemSubmit();
                break;
            case R.id.btn_coupon:
                applyCoupon("2147483648");
                break;
            case R.id.btn_track:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new OrderFragment(), "OrderFgt")
                        .addToBackStack(null).commit();
                break;

        }
    }

    private void onBraintreeSystemSubmit(){
       DropInRequest request = new DropInRequest().clientToken(token);
       request.disablePayPal();
       startActivityForResult(request.getIntent(getActivity()), REQUEST_CODE);
//
//        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalNeedToPay), "USD", "Purchase products", PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(getActivity(), PaymentActivity.class);
//        intent.putExtra( PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        
        if(requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                DropInResult result = data.getParcelableExtra( DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();


                String strNonce = nonce.getNonce();
                Log.i( TAG, "onActivityResult: " + strNonce );

                //get the Payment Type(Visa, MasterCard...) and the card number
                cardNumber= nonce.getDescription();
                paymentType= nonce.getTypeLabel();
                Log.i( TAG, "onActivityResult: " + cardNumber);
                Log.i( TAG, "onActivityResult: " + paymentType );

                if(!tv_total.getText().toString().isEmpty()){
                    amount = tv_total.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount);
                    paramsHash.put("nonce", strNonce);

                    sendPayments();
                }
                else {
                        Toast.makeText(getContext(), "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }


            }else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(getContext(), "User Cancel", Toast.LENGTH_SHORT).show();
            else {
                Exception error = (Exception)data.getSerializableExtra( DropInActivity.EXTRA_ERROR);
                Log.d("EDMT_ERROR", error.toString());
            }
        }
    }

    private void sendPayments() {

        order_list = new ArrayList<>();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    Log.i("sendpayment", "start payment");
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Order confirmed");

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

                        order_list.add(orderHistoryInfo);
                    }
                    Log.i("orderlist", order_list.size() + "");

                    ll_ordersum.setVisibility(View.VISIBLE);


                    int total_cost = 0;
                    StringBuilder stringBuilder = new StringBuilder("Order ID: ");
                    for(OrderHistoryInfo orderHistoryInfo : order_list){
                        total_cost += Integer.parseInt(orderHistoryInfo.getTotalprice());
                    }

                    for(int i = 0; i < order_list.size() - 1; i++){
                        stringBuilder.append(order_list.get(i).getOrderid() + ",");
                    }

                    stringBuilder.append(order_list.get(order_list.size() - 1).getOrderid());

                    tv_orderid.setText(stringBuilder.toString());
                    tv_time.setText(getString(R.string.placed_time) + order_list.get(0).getPlacedon());
                    tv_address.setText(getString(R.string.delivery_address) + order_list.get(0).getDeliveryadd());
                    tv_mobile.setText(getString(R.string.mobile_no) + order_list.get(0).getMobile());

                    tv_payment.setText("Payment Summary" + "\n"
                            +"Total Price: $" + String.valueOf(totalNeedToPay) + "\n"
                            + "You Paid: $" + String.valueOf(totalNeedToPay) + "\n"
                            + "Payment Type:" + paymentType + "\n" + "Card Number" + cardNumber);

                    database_manager.deleteItem(SharedPreferencesUtil.getSp(getContext()).getString("id", null));
                    item_list.clear();
                    adpter.notifyDataSetChanged();
                    EventBus.getDefault().post(-1);
                    Toast.makeText(getActivity(), "Trascation successfully", Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Transaction failed", error.toString());
            }
        };

        for(int i = 0; i < item_list.size(); i++){
            JsonObjectRequest paymentRequest = VolleyHandler.getInstance().paymentRequestObject(item_list.get(i).getPid(), item_list.get(i).getPname(),
                    String.valueOf(item_list.get(i).getQuantity()), item_list.get(i).getPrize(),
                    SharedPreferencesUtil.getSp(getContext()).getString("appapikey", null),
                    SharedPreferencesUtil.getSp(getContext()).getString("id", null),
                    SharedPreferencesUtil.getSp(getContext()).getString("firstname", null),
                    SharedPreferencesUtil.getSp(getContext()).getString("mobile", null),
                    SharedPreferencesUtil.getSp(getContext()).getString("email", null),
                    listener, errorListener, paramsHash);

            MainControllor.getAppInstance().addToRequestQueue(paymentRequest, "paymentRequest");
        }
    }

    public void paymentTotal(){
        deliveryCost = 50;
        tv_deli_total.setText("$" + deliveryCost);
        totalNeedToPay = 0;
        for (OrderInfo orderInfo : item_list){
            totalNeedToPay += Integer.parseInt(orderInfo.getPrize()) * orderInfo.getQuantity();
        }

        int tax = (int) (totalNeedToPay * 0.08);
        tv_tax_total.setText("$"+totalNeedToPay + " + $"+tax + "(8% tax) = $" + totalNeedToPay);
        totalNeedToPay += tax + deliveryCost;
        tv_total.setText("$" + totalNeedToPay);

    }

    public void applyCoupon(String coupon){

        Log.i("coupon", "this is coupon");
        //http://rjtmobile.com/aamir/e-commerce/android-app/coupon.php?api_key=7057bc8168b477b9420aeca3fda3c868&user_id=1217&couponno=2147483648

        /*
        {
              "Coupon discount": [
                {
                "couponno": "2147483648",
                "discount": "10"
                }
              ]
            }

         */
        coupon_list = new ArrayList<>();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response;
                    JSONArray jsonArray = jsonObject.getJSONArray("Coupon discount");

                    for(int i= 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CouponInfo couponInfo = new CouponInfo(jsonObject1.getString("couponno"), jsonObject1.getString("discount"));

                        coupon_list.add(couponInfo);
                    }
                    et_coupon.setText(coupon_list.get(0).getCouponno());
                    int curtotal = totalNeedToPay;
                    curtotal = (int)((1 - (Integer.parseInt(coupon_list.get(0).getDiscount().toString()) / 100.0)) * curtotal);
                    Log.i("discount", (Integer.parseInt(coupon_list.get(0).getDiscount().toString())) +"");
                    tv_total.setText("price + tax + delivery after" + coupon_list.get(0).getDiscount().toString() + "% off, the total is " +"$"+curtotal + "");
                    totalNeedToPay = curtotal;



                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("applyCoupon", error.getMessage());
            }
        };

        JsonObjectRequest couponRequest = VolleyHandler.getInstance().couponRequestObject(
                SharedPreferencesUtil.getSp(getContext()).getString("appapikey", null)
                , SharedPreferencesUtil.getSp(getContext()).getString("id", null)
                , coupon, listener, errorListener);

        MainControllor.getAppInstance().addToRequestQueue(couponRequest, "couponRequest");

    }

    private class getToken extends AsyncTask{
        ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setMessage("Please wait");
            mDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client = new HttpClient();
            client.get( APT_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            token = responseBody;
                        }
                    } );
                }

                @Override
                public void failure(final Exception exception) {
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Log.i("Fail to get the Token", exception.toString());
                        }
                    } );
                }
            } );
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute( o );
            mDialog.dismiss();
        }
    }


}
