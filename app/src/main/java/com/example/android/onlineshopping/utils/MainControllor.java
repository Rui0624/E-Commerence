package com.example.android.onlineshopping.utils;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainControllor extends Application {

    private static final String TAG = MainControllor.class.getSimpleName();
    private static MainControllor appInstance;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }

    public static synchronized MainControllor getAppInstance(){
        //make the maincontrollor be synchronized which cannot be invoke and work at same time
        return appInstance;
    }

    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){
        //set the default tag
        request.setTag(tag);
        //add the request into queue
        mRequestQueue.add(request);
    }

    public <T> void addToRequestQueue(Request<T> request){
        //set the tag to the request
        request.setTag(TAG);
        //add the request into queue
        mRequestQueue.add(request);
    }

    public void cancelRequest(Object tag){
        //cancel the request in the queue with the tag
        mRequestQueue.cancelAll(tag);
    }
}
