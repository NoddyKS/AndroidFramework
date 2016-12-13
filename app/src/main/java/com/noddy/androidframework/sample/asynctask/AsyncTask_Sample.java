package com.noddy.androidframework.sample.asynctask;

import android.app.Application;

import com.noddy.androidframework.asynctask.ConnectionAsyncTask;
import com.noddy.androidframework.asynctask.contracts.CallbackContract;
import com.noddy.androidframework.specification.base.BaseQuerySpecification;

import java.net.HttpURLConnection;

/**
 * Created by NoddyLaw on 2016/12/5.
 */

public class AsyncTask_Sample extends ConnectionAsyncTask {

    private CallbackContract.ConnectionCallback mCallback;

    private  final String mSpectificationQueryError = "BaseQuerySpectification query error: ";

    protected AsyncTask_Sample(Application application, CallbackContract.ConnectionCallback callback, BaseQuerySpecification spectification) {
        super(application, spectification);
        mCallback = callback;
    }

    public CallbackContract.ConnectionCallback getCallback() {
        return mCallback;
    }

    public void setmCallback(CallbackContract.ConnectionCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public Object onApiRequest() {
        try {
            return getSpectification().onQuery();
        } catch (Exception e) {
            return mSpectificationQueryError + e;
        }
    }

    @Override
    public void onApiResponse(int responseCode, Object result) {
        try{
            if (result == null)
                mCallback.onApiResponseFail(responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                mCallback.onApiResponseFail(responseCode);
            } else {
                mCallback.onApiResponseSuccess(responseCode, result);
            }
        }catch (Exception e){

        }
    }
}
