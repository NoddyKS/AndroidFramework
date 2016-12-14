package com.noddy.androidframework.sample.asynctask.presenter;

import android.app.Application;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.noddy.androidframework.asynctask.contracts.Contracts;
import com.noddy.androidframework.asynctask.contracts.Entity;
import com.noddy.androidframework.repository.ResultSetObject;
import com.noddy.androidframework.repository.entityHolder.EntityHolder;
import com.noddy.androidframework.sample.asynctask.model.SampleModel;

import static com.google.api.client.repackaged.com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/14.
 */

public class Presenter implements Contracts.Presenter{

    private SampleModel mModel;

    public Presenter(@NonNull Application application) {

        mModel = new SampleModel(application, this);

        start();

    }
    @Override
    public void start() {
        getDevelopments();
    }

    @Override
    public void end() {
    }

    public void getDevelopment() {

        mModel.getSingleData();
    }

    public void getDevelopments() {

        mModel.getListData(false);
    }

    @Override
    public void receivedSingleEntity(EntityHolder data) {
        Toast.makeText(mModel.getApplication(), "get receivedSingleEntity success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void receivedEntitys(EntityHolder data) {
        Toast.makeText(mModel.getApplication(), "get receivedEntitys success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void receivedCustomData(Object data) {
        Toast.makeText(mModel.getApplication(), "get receivedCustomData success", Toast.LENGTH_SHORT).show();
    }

}
