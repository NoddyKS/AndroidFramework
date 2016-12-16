package com.noddy.androidframework.baseModel;

import android.app.Application;
import android.util.Log;

import com.noddy.androidframework.asynctask.contracts.CallbackContract;
import com.noddy.androidframework.asynctask.contracts.Entity;
import com.noddy.androidframework.asynctask.specification.base.BaseQuerySpecification;
import com.noddy.androidframework.config.Configs;
import com.noddy.androidframework.repository.ResultSetObject;
import com.noddy.androidframework.repository.base.BaseRepository;
import com.noddy.androidframework.repository.entityHolder.EntityHolder;
import com.noddy.androidframework.sample.asynctask.AsyncTask_With_CallBack;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;

import static com.noddy.androidframework.Until.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/2.
 */

public abstract class BaseModel<T extends Entity> {

    private EntityHolder mEntityHolder;

    private BaseRepository mRepository;

    private Application mApplication;

    private String mOAuthAoken;

    private Class mEntityHolderClass;

    public abstract BaseRepository onRepositorySetUp();

    public abstract String onTokenSetUp();

    public abstract String getListUrl();

    public abstract String getSingleUrl();

    public abstract String getPostUrl();

    public abstract void onSingleDataReceived(int resultCode, Object data);

    public abstract void onListDataReceived(int resultCode, Object data);

    public abstract void onPostResultReceived(int resultCode, Object data);

    public abstract void onDataQueryFail(String failMsg);

    public BaseModel(Application application, Class entityHolderName) {
        mApplication = checkNotNull(application, "RcBaseModel: application cannot be null!");
        mOAuthAoken = checkNotNull(onTokenSetUp(), "RcBaseModel: OAuth token cannot be null!");

        mRepository = checkNotNull(onRepositorySetUp(), "RcBaseModel: repository cannot be null!");

        mEntityHolderClass = checkNotNull(entityHolderName, "RcBaseModel: entityHolderName cannot be null!");

        mEntityHolder = new EntityHolder();
    }

    public String getmOAuthAoken() {
        return mOAuthAoken;
    }

    public Application getApplication() {
        return mApplication;
    }

    public void postData(Object data) {
        String postUrl = getPostUrl();
        mRepository.postData(postUrl, data, new CallbackContract.ConnectionCallback() {
            @Override
            public void onApiResponseSuccess(int responseCode, Object data) {//entity
                onPostResultReceived(responseCode, data);
            }

            @Override
            public void onApiResponseFail(int responseCode) {
                onPostResultReceived(responseCode, null);
            }

            @Override
            public void onApiRequestFail(String errorMsg) {
                onDataQueryFail(errorMsg);
                //coding fail
            }
        });

    }

    public void getSingleData() {

        String urlWithOutPaging = getSingleUrl();

        mRepository.getData(urlWithOutPaging, mEntityHolderClass, new CallbackContract.ConnectionCallback() {
            @Override
            public void onApiResponseSuccess(int responseCode, Object data) {//entity
                receiveSingleData(responseCode, data);
            }

            @Override
            public void onApiResponseFail(int responseCode) {
                receiveSingleData(responseCode, null);
            }

            @Override
            public void onApiRequestFail(String errorMsg) {
                //coding fail
            }
        });
    }

    public void getListData(boolean getMoreList) {
        if (!getMoreList)//clear entity list holder
            mEntityHolder.clear();

        if (mEntityHolder != null && !mEntityHolder.isCanRequestMore()&&mEntityHolder.getResults()!=null&&mEntityHolder.getResults().length>0) {
            onDataQueryFail("can't get more lsit data (page == numPages)");
            return;
        }

        String urlWithOutPaging = getListUrl();

        urlWithOutPaging += generatePagingPara();//add pag


        mRepository.getData(urlWithOutPaging, mEntityHolderClass, new CallbackContract.ConnectionCallback() {
            @Override
            public void onApiResponseSuccess(int responseCode, Object data) {//entityholder
                if (responseCode != HttpURLConnection.HTTP_OK || data == null) {
                    onDataQueryFail("coding fail");
                } else {
                    receiveListData(responseCode, data);
                }
            }

            @Override
            public void onApiResponseFail(int responseCode) {
                receiveListData(responseCode, null);
            }

            @Override
            public void onApiRequestFail(String errorMsg) {
                onDataQueryFail(errorMsg);
                //coding fail
            }
        });
    }

    private String generatePagingPara() {
        String paginPara = Configs.PARAMETER_LIMIT + String.format(Configs.PARAMETER_OFFSET, mEntityHolder.offset);
        return paginPara;
    }

    public void customQuery(CallbackContract.ConnectionCallback callBack, BaseQuerySpecification specification) {
        customQuery(callBack,specification,0,0);
    }

    public void customQuery(CallbackContract.ConnectionCallback callBack, BaseQuerySpecification specification,int retryQuery,int secondOfTimeout) {
        // specification = what to do query , callBack = what to do when response
        AsyncTask_With_CallBack async_sample= new AsyncTask_With_CallBack(mApplication, callBack, specification);
        if(retryQuery>0)//at last 1 times
            async_sample.setmNumberToRetryQuery(retryQuery);//set number to try query times
        if(secondOfTimeout>5000)//at last 5 seconds
            async_sample.setTimeoutlimit(secondOfTimeout); //set timeout connect mini seconds
        async_sample.execute();
    }

    private void receiveSingleData(int resultCode, Object data) {

        if (data instanceof EntityHolder) {

            EntityHolder entityHolder = (EntityHolder) data;

            onSingleDataReceived(resultCode, entityHolder);
        }
    }

    private void receiveListData(int resultCode, Object data) {
        boolean updatedHolder = false;
        if (data instanceof EntityHolder) {
            EntityHolder entityHolder = (EntityHolder) data;

            boolean isResultOK = resultCode == HttpURLConnection.HTTP_OK;

            try {
                if (isResultOK) {
                    //merge received form api entity holder
                    updatedHolder = mEntityHolder.merge(entityHolder);
                }
            } catch (Exception e) {
                Log.d("Tag", e.getMessage());

            } finally {
                if (updatedHolder) {
                    onListDataReceived(resultCode, mEntityHolder);
                } else {
                    onDataQueryFail("merage/update holder fail");
                }
            }
        }
    }

    private ResultSetObject getPackagedDevelopments(boolean resultOk, boolean canRequest, EntityHolder<T> entityHolder) {
        ResultSetObject<ArrayList<T>> packagedDevelopments = new ResultSetObject();

        packagedDevelopments.setResultOk(resultOk);
        packagedDevelopments.setCanRequestMore(canRequest);
        packagedDevelopments.setPageResultCount(entityHolder.offset);
        packagedDevelopments.setResultCount(entityHolder.total);
        packagedDevelopments.setData(new ArrayList(Arrays.asList(entityHolder.getResults())));

        return packagedDevelopments;
    }
}
