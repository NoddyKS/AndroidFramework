package com.noddy.androidframework.asynctask;

import android.app.Application;

import com.noddy.androidframework.asynctask.base.AdvancedAsyncTask;
import com.noddy.androidframework.contracts.CallbackContract;
import com.noddy.androidframework.asynctask.specification.base.QuerySpecification;

import java.net.HttpURLConnection;

/**
 * Created by NoddyLaw on 2016/12/5.
 */

public class ConnectionAsyncTask extends AdvancedAsyncTask {

    private CallbackContract.ConnectionCallback mCallback;

    private  final String mSpectificationQueryError = "BaseQuerySpectification query error: ";

    public ConnectionAsyncTask(Application application, CallbackContract.ConnectionCallback callback, QuerySpecification spectification) {
        super(application, spectification);
        mCallback = callback;
    }

    public CallbackContract.ConnectionCallback getCallback() {
        return mCallback;
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
            }else if(responseCode ==0 && result!=null && result instanceof  String){
                mCallback.onApiRequestFail((String)result);
            }
            else {
                mCallback.onApiResponseSuccess(responseCode, result);
            }
        }catch (Exception e){

        }
    }
}
