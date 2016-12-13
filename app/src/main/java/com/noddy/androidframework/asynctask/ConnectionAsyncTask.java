package com.noddy.androidframework.asynctask;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import com.noddy.androidframework.specification.base.BaseQuerySpecification;
import com.noddy.androidframework.config.Configs;

/**
 * Created by NoddyLaw on 2016/12/5.
 */

public abstract class ConnectionAsyncTask extends android.os.AsyncTask<Object, Object, Object> {
    private static final String TAG = "RcConnectionAsyncTask";
    private final int DEFAULT_RETRY_QUERY = Configs.NUM_TO_TRY_TO_CONNECT;
    private final int TIME_WAIT = Configs.SECOND_TO_WAIT_IF_CONNECT_FAILS * 1000;

    private int mNumberToRetryQuery = DEFAULT_RETRY_QUERY;

    private Application mApplication;

    private BaseQuerySpecification mSpectification;

    public ConnectionAsyncTask(Application application, BaseQuerySpecification spectification) {
        mApplication = checkNotNull(application, "RcConnectionAsyncTask: MainApplication cannot be null!");
        mSpectification = checkNotNull(spectification, "RcConnectionAsyncTask: FeedEnum cannot be null!");
    }

    public Application getApplication() {
        return mApplication;
    }

    public BaseQuerySpecification getSpectification() {
        return mSpectification;
    }

    public int getmNumberToRetryQuery() {
        return mNumberToRetryQuery;
    }

    public void setmNumberToRetryQuery(int mNumberToRetryQuery) {
        this.mNumberToRetryQuery = mNumberToRetryQuery;
    }

    public abstract Object onApiRequest();

    public abstract void onApiResponse(int responseCode, Object result);

    @Override
    protected Object doInBackground(Object... arg0) {
        if (!isConnected())
            return null;

        for (int i = 0; i < DEFAULT_RETRY_QUERY; i++) {
            Object resultObject = onApiRequest();
            if (resultObject != null) {
                return resultObject;
            }
            try {
                Thread.sleep(TIME_WAIT);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception ex) {
                Log.d("TAG", ex.getMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        try {
            onApiResponse(mSpectification.getResponseCode(), result);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private boolean isConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    private static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }
}
