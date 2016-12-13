package com.noddy.androidframework.asynctask.contracts;

/**
 * Created by NoddyLaw on 2016/12/5.
 */

public interface CallbackContract {

    interface ConnectionCallback<T> {

        void onApiResponseSuccess(int resultCode, T data);

        void onApiResponseFail(int resultCode);

        //return catch error message
        void onApiRequestFail(String errorMsg);
    }
}
