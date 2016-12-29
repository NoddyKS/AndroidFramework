package com.noddy.androidframework.basePresenter;

import com.noddy.androidframework.errorHandle.CatchedRequestErrorType;
import com.noddy.androidframework.repository.entityHolder.EntityHolder;

/**
 * Created by NoddyLaw on 2016/12/29.
 */

public abstract class BasePresenter {

    public abstract void onSingleDataReceived(int responseCode,EntityHolder result);

    public abstract void onListDataReceived(int responseCode, EntityHolder result);

    public abstract void onPostResultReceived(int responseCode, EntityHolder result);

    public abstract void onDataQueryFail(CatchedRequestErrorType error);

}
