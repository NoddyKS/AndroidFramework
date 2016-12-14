package com.noddy.androidframework.sample.asynctask.model;

import android.app.Application;
import android.util.Log;

import com.noddy.androidframework.baseModel.BaseModel;
import com.noddy.androidframework.repository.ResultSetObject;
import com.noddy.androidframework.repository.base.BaseRepository;
import com.noddy.androidframework.repository.entityHolder.EntityHolder;
import com.noddy.androidframework.sample.asynctask.data.DevelopmentHolder;
import com.noddy.androidframework.sample.asynctask.data.DevelopmentObject;
import com.noddy.androidframework.sample.asynctask.presenter.Presenter;

/**
 * Created by NoddyLaw on 2016/12/14.
 */

public class SampleModel extends BaseModel<DevelopmentObject> {
    public static final String TAG = "SampleModel";

    private  Presenter mPresenter;

    public SampleModel(Application application , Presenter presenter) {
        super(application);
        mPresenter =presenter;
    }

    @Override
    public String getListUrl() {
        return new DevelopmentObject().getListUrl();
    }

    @Override
    public String getSingleUrl() {
        return new DevelopmentObject().getSingleUrl();
    }

    @Override
    public String onTokenSetUp() {
        return "token";
    }

    @Override
    public BaseRepository onRepositorySetUp() {
        return new BaseRepository(getApplication(),new DevelopmentHolder(),this);
    }

    @Override
    public void onSingleDataReceived(int responseCode, Object data) {
        if (data instanceof EntityHolder) {

            EntityHolder entityHolder = (EntityHolder) data;
            mPresenter.receivedSingleEntity(entityHolder);
        }


    }

    @Override
    public void onListDataReceived(int responseCode, Object data) {//receive ResultSetObject type data
        if (data instanceof EntityHolder) {

            EntityHolder entityHolder = (EntityHolder) data;
            mPresenter.receivedEntitys(entityHolder);
        }
    }

    @Override
    public void onCustomQueryReceived(int resultCode, Object data) {
        mPresenter.receivedCustomData(data);
    }
}
