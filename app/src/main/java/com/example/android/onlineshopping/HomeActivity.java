package com.example.android.onlineshopping;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout dl_drawerLayout;
    private NavigationView nv_navigationView;
    private Toolbar mToolbar;
    private TextView tv_toolbarTitle, tv_username;
    private ImageView iv_avatar;
    private ImageButton ib_cart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
    }
}
