package com.noddy.androidframework.repository.base;

import android.app.Application;

import com.noddy.androidframework.asynctask.contracts.CallbackContract;
import com.noddy.androidframework.asynctask.specification.GetQuerySpectification;
import com.noddy.androidframework.asynctask.specification.PostQuerySpectification;
import com.noddy.androidframework.asynctask.specification.base.BaseQuerySpecification;
import com.noddy.androidframework.baseModel.BaseModel;
import com.noddy.androidframework.sample.asynctask.AsyncTask_With_CallBack;

import static com.noddy.androidframework.Until.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class BaseRepository {

    private BaseModel mModel;

    private Application mApplication;

    private int mRetryQuery = 3 , mTimeOut = 3*1000;

    public BaseRepository(Application application,BaseModel model){

        mModel= checkNotNull(model, "BaseRepository: model cannot be null!");
        mApplication= checkNotNull(application, "BaseRepository: application cannot be null!");
        //check entry object can't be EntityContract object


    }

    public void setRetryQuery(int retryQuery) {
        this.mRetryQuery = retryQuery;
    }

    public void setTimeOut(int timeOut) {
        this.mTimeOut = timeOut;
    }

    public void getData(String url,Class entityHolder ,final CallbackContract.ConnectionCallback callBack){
        GetQuerySpectification  specification = new GetQuerySpectification(url,mModel.getmOAuthAoken(), entityHolder);
        executeQuery(specification,callBack);
    }

    public void postData(String url,Object objectForUpload ,final CallbackContract.ConnectionCallback callBack){
        PostQuerySpectification  specification = new PostQuerySpectification(url,mModel.getmOAuthAoken(), objectForUpload);
        executeQuery(specification,callBack);
    }

    private void executeQuery(BaseQuerySpecification specification,CallbackContract.ConnectionCallback callBack){
        // specification = what to do query , callBack = what to do when response
        AsyncTask_With_CallBack async_sample= new AsyncTask_With_CallBack(mApplication, callBack, specification);
        async_sample.setmNumberToRetryQuery(mRetryQuery);//set number to try query times
        async_sample.setTimeoutlimit(mTimeOut); //set timeout connect mini seconds
        async_sample.execute();
    }
}
