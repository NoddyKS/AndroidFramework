package com.noddy.androidframework.repository.base;

import com.noddy.androidframework.repository.ResultSetObject;
import com.noddy.androidframework.repository.entityHolder.EntityHolder;

import java.util.HashMap;

import static com.noddy.androidframework.Until.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class BaseRepository<T> {
    private HashMap<String,EntityHolder<T>> mModel;

    private Class mEntity;

    public BaseRepository(Class entityClass){
        mEntity = checkNotNull(entityClass, "RcConnectionAsyncTask: MainApplication cannot be null!");
    }

    private void getUrlFormEntity(){

    }
}
