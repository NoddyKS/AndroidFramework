package com.noddy.androidframework.repository.base;

import android.app.Application;

import com.noddy.androidframework.asynctask.contracts.CallbackContract;
import com.noddy.androidframework.asynctask.specification.GetQuerySpectification;
import com.noddy.androidframework.asynctask.contracts.Entity;
import com.noddy.androidframework.baseModel.BaseModel;
import com.noddy.androidframework.sample.asynctask.AsyncTask_Sample;

import static com.noddy.androidframework.Until.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class BaseRepository {

    private Object mEntity;

    private BaseModel mModel;

    private Application mApplication;

    public BaseRepository(Application application, Object entity,BaseModel model){
        mEntity = checkNotNull(entity, "BaseRepository: entityClass cannot be null!");
        mModel= checkNotNull(model, "BaseRepository: model cannot be null!");
        mApplication= checkNotNull(application, "BaseRepository: application cannot be null!");
        //check entry object can't be EntityContract object
        if(entity.getClass().getName().equals(Entity.class.getName()))
            throw new ClassCastException(" can't not be EntityContract");

    }

    public void getData(String url, final CallbackContract.ConnectionCallback callBack){
        AsyncTask_Sample async_sample= new AsyncTask_Sample(mApplication, callBack, new GetQuerySpectification(url,mModel.getmOAuthAoken(), mEntity.getClass().getName()));

        async_sample.setmNumberToRetryQuery(3);//set number to try query times
        async_sample.setTimeoutlimit(15000); //set timeout connect mini seconds
        async_sample.execute();
    }

}
