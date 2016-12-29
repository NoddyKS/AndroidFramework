package com.noddy.androidframework.basePresenter;

import com.noddy.androidframework.errorHandle.CatchedRequestErrorType;
import com.noddy.androidframework.repository.entityHolder.EntityHolder;

/**
 * Created by NoddyLaw on 2016/12/29.
 */

public interface BasePresenter {

    void onSingleDataReceived(int responseCode,EntityHolder result);

    void onListDataReceived(int responseCode, EntityHolder result);

    void onPostResultReceived(int responseCode, Object result);

    void onDataQueryFail(CatchedRequestErrorType error);

}
