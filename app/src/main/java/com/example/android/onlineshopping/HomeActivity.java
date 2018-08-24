package com.example.android.onlineshopping;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.android.onlineshopping.database.DatabaseManager;
import com.example.android.onlineshopping.ui.auth.LoginActivity;
import com.example.android.onlineshopping.ui.auth.ProfileFragment;
import com.example.android.onlineshopping.ui.checkout.CartFragment;
import com.example.android.onlineshopping.ui.product.CategoryFragment;
import com.example.android.onlineshopping.ui.checkout.OrderFragment;
import com.example.android.onlineshopping.ui.product.TechFragment;
import com.example.android.onlineshopping.ui.product.TopSellerFragment;
import com.example.android.onlineshopping.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener {
    private DrawerLayout dl_drawerLayout;
    private NavigationView nv_navigationView;
    private Toolbar mToolbar;
    private TextView tv_toolbarTitle, tv_username;
    private DatabaseManager database_manager;
    private ImageButton ib_cart;
    private int cart_item_count;
    private Badge badge_view;

    private final String mAuthorization = "http://rjtmobile.com/aamir/braintree-paypal-payment/main.php";
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );

        initDatabase();

        initToolBar();

        initView();

        initDrawer();

        try {
            BraintreeFragment mBraintreeFragment = BraintreeFragment.newInstance(this, mAuthorization);
            // mBraintreeFragment is ready to use!
        } catch (InvalidArgumentException e) {
            // There was an issue with your authorization string.
        }
    }
    private void initDatabase(){
        if(database_manager == null){
            database_manager = new DatabaseManager(this);
        }
        database_manager.openDatabase();
    }

    private void initView(){
        dl_drawerLayout = findViewById(R.id.drawer_layout);
        nv_navigationView = findViewById(R.id.nav_view);
        nv_navigationView.setNavigationItemSelectedListener(this);


    }

    private void initToolBar(){
        mToolbar = findViewById(R.id.mytoolbar);
        tv_toolbarTitle = mToolbar.findViewById(R.id.tv_toolbar_title);
        ib_cart = mToolbar.findViewById(R.id.ib_cart);
        ib_cart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(R.string.cart, new CartFragment(), "Cartfgt");
            }
        } );

        tv_toolbarTitle.setText(R.string.home);
        setSupportActionBar(mToolbar);

        cart_item_count = database_manager.getCartList(SharedPreferencesUtil.getSp(getBaseContext()).getString("id", null)).size();
        Log.i("size", cart_item_count + "");
        if(cart_item_count <= 0){
            badge_view = new QBadgeView(getBaseContext()).bindTarget(ib_cart)
                    .setBadgeGravity( Gravity.END | Gravity.TOP)
                    .setBadgeNumber(0);
        }else{
            badge_view = new QBadgeView(getBaseContext()).bindTarget(ib_cart)
                    .setBadgeGravity( Gravity.END | Gravity.TOP)
                    .setBadgeNumber(cart_item_count);
        }


    }

    private void initDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                dl_drawerLayout, mToolbar, R.string.open, R.string.close);

        //set the listener of toggle
        dl_drawerLayout.setDrawerListener(toggle);

        //set the connection between drawer layout and toggle
        toggle.syncState();

        //set the header in the navigation view
        View head = nv_navigationView.getHeaderView(0);
        tv_username = head.findViewById(R.id.tv_username);
        tv_username.setText(SharedPreferencesUtil.getSp(getApplicationContext()).getString("firstname", null) + " "
                + SharedPreferencesUtil.getSp(getApplicationContext()).getString("lastname", null));

        nv_navigationView.setCheckedItem(R.id.shop);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new CategoryFragment()).addToBackStack(null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.wish_menu, menu);
        return true;
    }

    public void updateTitile(String title){
        tv_toolbarTitle.setText(title);
    }

    public void replaceFragment(int title, Fragment fragment, String tag){
        tv_toolbarTitle.setText(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, fragment, tag).
                addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.profile:{
                replaceFragment(R.string.profile, new ProfileFragment(), "ProfileFgt");
                break;
            }

            case R.id.shop:{
                replaceFragment(R.string.home, new CategoryFragment(), "CateFgt");
                break;
            }

            case R.id.order:{
                replaceFragment(R.string.my_order, new OrderFragment(), "OrderFgt");
                break;
            }

            case R.id.seller:{
                replaceFragment(R.string.top_seller, new TopSellerFragment(), "SellerFgt");
                break;
            }

            case R.id.techology:{
                replaceFragment(R.string.tech, new TechFragment(), "TechFgt");
                break;
            }

            case R.id.logout:{
                SharedPreferencesUtil.clearUserInfo(getBaseContext());
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }

        menuItem.setChecked(true);

        dl_drawerLayout.closeDrawers();

        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database_manager.closeDatabase();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Integer num) {
        cart_item_count = cart_item_count + num;
        if (cart_item_count <= 0) {
            badge_view.setBadgeNumber(0);

        } else {
            badge_view.setBadgeNumber(cart_item_count);
        }

    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        // Send this nonce to your server
        String nonce = paymentMethodNonce.getNonce();
    }

    @Override
    public void onCancel(int requestCode) {
        // Use this to handle a canceled activity, if the given requestCode is important.
        // You may want to use this callback to hide loading indicators, and prepare your UI for input
    }

    @Override
    public void onError(Exception error) {
        if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                   // setErrorMessage(expirationMonthError.getMessage());
                    Log.i( TAG, "onError: "+ expirationMonthError.getMessage() );
                }
            }
        }
    }


}
