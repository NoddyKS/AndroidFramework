package com.noddy.androidframework.repository.base;

import android.app.Application;

import com.noddy.androidframework.contracts.CallbackContract;
import com.noddy.androidframework.asynctask.specification.GetQuery;
import com.noddy.androidframework.asynctask.specification.PostQuery;
import com.noddy.androidframework.asynctask.specification.base.QuerySpecification;
import com.noddy.androidframework.asynctask.ConnectionAsyncTask;

import static com.noddy.androidframework.Until.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class BaseRepository {

    private String mOAuthToken;

    private Application mApplication;

    private int mRetryQuery = 3 , mTimeOut = 3*1000;

    private Object mUploadObject;

    private Class mEntityHolder;

    public BaseRepository(Application application,String token){

        mOAuthToken= checkNotNull(token, "BaseRepository: token cannot be null!");
        mApplication= checkNotNull(application, "BaseRepository: application cannot be null!");
    }

    public void setRetryQuery(int retryQuery) {
        this.mRetryQuery = retryQuery;
    }

    public void setTimeOut(int timeOut) {
        this.mTimeOut = timeOut;
    }

    public void getData(String url,Class entityHolder ,final CallbackContract.ConnectionCallback callBack){
       // GetQuerySpectification  specification = new GetQuerySpectification(url,mModel.getOAuthToken(), entityHolder);
        mEntityHolder  =checkNotNull(entityHolder, "BaseRepository: entityHolder cannot be null!");
        QuerySpecification specification = getSpecification(SpecType.GET,url);
        executeQuery(specification,callBack);
    }

    public void postData(String url,Object objectForUpload ,final CallbackContract.ConnectionCallback callBack){
        mUploadObject =checkNotNull(objectForUpload, "BaseRepository: objectForUpload cannot be null!");
        QuerySpecification specification = getSpecification(SpecType.POST,url);
        executeQuery(specification,callBack);
    }

    private void executeQuery(QuerySpecification specification, CallbackContract.ConnectionCallback callBack){
        // specification = what to do query , callBack = what to do when response
        ConnectionAsyncTask asyncProcess= new ConnectionAsyncTask(mApplication, callBack, specification);
        asyncProcess.setmNumberToRetryQuery(mRetryQuery);//set number to try query times
        asyncProcess.setTimeoutlimit(mTimeOut); //set timeout connect mini seconds
        asyncProcess.execute();
    }

    public QuerySpecification getSpecification(SpecType specType, String url){
        switch (specType){
            case GET:
                return new GetQuery(url,mOAuthToken, mEntityHolder);
            case POST:
                return new PostQuery(url,mOAuthToken, mUploadObject);
        }
        return  null;
    }

    public enum SpecType{
        GET,
        POST
    }
}
