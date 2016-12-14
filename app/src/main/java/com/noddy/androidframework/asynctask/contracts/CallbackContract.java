package com.noddy.androidframework.asynctask.contracts;

/**
 * Created by NoddyLaw on 2016/12/5.
 */

public interface CallbackContract {
    interface ConnectionCallback<T> {

        void onApiResponseSuccess(int responseCode, T data);

        void onApiResponseFail(int responseCode);

        //return catch error message
        void onApiRequestFail(String errorMsg);
    }

    interface RequestDataCallBack<T> {
        void onDataReceived(T data);

        void onDataReceiveFail(String errorMessage);
    }
}
