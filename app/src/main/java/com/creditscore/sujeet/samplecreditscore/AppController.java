package com.creditscore.sujeet.samplecreditscore;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by SJBIT_C2 on 10/16/2016.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        ourInstance = this;
    }

    private static AppController ourInstance = new AppController();

    public static synchronized AppController getInstance() {
        return ourInstance;
    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if(mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }
}
