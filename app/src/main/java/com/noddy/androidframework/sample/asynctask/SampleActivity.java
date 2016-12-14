package com.noddy.androidframework.sample.asynctask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.noddy.androidframework.asynctask.contracts.CallbackContract;
import com.noddy.androidframework.asynctask.specification.PostQuerySpectification;
import com.noddy.androidframework.sample.asynctask.model.SampleModel;
import com.noddy.androidframework.sample.asynctask.presenter.Presenter;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Presenter presenter = new Presenter(getApplication());

        presenter.start();

//        AsyncTask_Sample  async_sample= new AsyncTask_Sample(getApplication(), new CallbackContract.ConnectionCallback() {
//            /* setup processed result handle*/
//            @Override
//            public void onApiResponseSuccess(int resultCode, Object data) {
//
//            }
//
//            @Override
//            public void onApiResponseFail(int resultCode) {
//
//            }
//
//            @Override
//            public void onApiRequestFail(String errorMsg) {
//
//            }
//        }, new PostQuerySpectification());
//
//        async_sample.setmNumberToRetryQuery(3);//set number to try query times
//        async_sample.setTimeoutlimit(15000); //set timeout connect mini seconds
//        async_sample.execute();
    }
}
