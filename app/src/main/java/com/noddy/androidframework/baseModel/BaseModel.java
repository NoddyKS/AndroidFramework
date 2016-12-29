package com.noddy.androidframework.baseModel;

import android.app.Application;
import android.util.Log;

import com.noddy.androidframework.basePresenter.BasePresenter;
import com.noddy.androidframework.contracts.CallbackContract;
import com.noddy.androidframework.asynctask.specification.base.QuerySpecification;
import com.noddy.androidframework.config.Configs;
import com.noddy.androidframework.errorHandle.CatchedRequestErrorType;
import com.noddy.androidframework.repository.base.BaseRepository;
import com.noddy.androidframework.repository.entityHolder.EntityHolder;
import com.noddy.androidframework.asynctask.ConnectionAsyncTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;

import static com.noddy.androidframework.Until.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/2.
 */

public abstract class BaseModel{

    private EntityHolder mEntityHolder;

    private BasePresenter mPresenter;

    private BaseRepository mRepository;

    private Application mApplication;

    private String mOAuthToken;

    public abstract BaseRepository onRepositorySetUp();

    public abstract String onTokenSetUp();

//    public abstract String getListUrl();
//
//    public abstract String getSingleUrl();
//
//    public abstract String getPostUrl();

//    public abstract void onSingleDataReceived(int resultCode, Object data);
//
//    public abstract void onListDataReceived(int resultCode, Object data);
//
//    public abstract void onPostResultReceived(int resultCode, Object data);
//
//    public abstract void onDataQueryFail(String failMsg);

    public BaseModel(Application application, EntityHolder entityHolder,BasePresenter presenter) {
        mApplication = checkNotNull(application, "RcBaseModel: application cannot be null!");

        mPresenter = checkNotNull(presenter, "RcBaseModel: presenter cannot be null!");

        mEntityHolder =checkNotNull(entityHolder, "RcBaseModel: entityHolder cannot be null!");;
    }

    public String getOAuthToken() {
        return mOAuthToken;
    }

    public Application getApplication() {
        return mApplication;
    }

    public Class getEntityHolderClass() {
        return mEntityHolder.getClass();
    }

    public void postData(Object data) {
        mRepository = checkNotNull(onRepositorySetUp(), "RcBaseModel: repository cannot be null!");
        String postUrl = getUrl(UrlType.POST);
        mOAuthToken = checkNotNull(onTokenSetUp(), "RcBaseModel: OAuth token cannot be null!");

        mRepository.postData(postUrl, data, new CallbackContract.ConnectionCallback() {
            @Override
            public void onApiResponseSuccess(int responseCode, Object data) {//entity
                mPresenter.onPostResultReceived(responseCode, (EntityHolder) data);
            }

            @Override
            public void onApiResponseFail(int responseCode) {
                mPresenter.onPostResultReceived(responseCode, null);
            }

            @Override
            public void onApiRequestFail(String errorMsg) {
                mPresenter.onDataQueryFail(CatchedRequestErrorType.CODING_ERROR);
                //coding fail
            }
        });

    }

    public void getSingleData(String parameter) {
        String urlWithOutPaging = getUrl(UrlType.GET_SINGLE) + ((parameter != null) ? parameter : "");
        mRepository = checkNotNull(onRepositorySetUp(), "RcBaseModel: repository cannot be null!");
        mOAuthToken = checkNotNull(onTokenSetUp(), "RcBaseModel: OAuth token cannot be null!");

        mRepository.getData(urlWithOutPaging, getEntityHolderClass(), new CallbackContract.ConnectionCallback() {
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
                mPresenter.onDataQueryFail(CatchedRequestErrorType.CODING_ERROR);
                //coding fail
            }
        });
    }

    public void getListData(boolean getMoreList, String parameter) {
        if (!getMoreList)//clear entity list holder
            mEntityHolder.clear();

        if (mEntityHolder != null && !mEntityHolder.isCanRequestMore() && mEntityHolder.getResults() != null && mEntityHolder.getResults().length > 0) {
            mPresenter.onDataQueryFail(CatchedRequestErrorType.CAN_NOT_GET_MORE);
            return;
        }

        String urlWithOutPaging = getUrl(UrlType.GET_LIST) + ((parameter != null) ? parameter : "");

        urlWithOutPaging += generatePagingPara();//add pag
        mOAuthToken = checkNotNull(onTokenSetUp(), "RcBaseModel: OAuth token cannot be null!");

        mRepository = checkNotNull(onRepositorySetUp(), "RcBaseModel: repository cannot be null!");
        mRepository.getData(urlWithOutPaging, getEntityHolderClass(), new CallbackContract.ConnectionCallback() {
            @Override
            public void onApiResponseSuccess(int responseCode, Object data) {//entityholder
                if (responseCode != HttpURLConnection.HTTP_OK || data == null) {
                    mPresenter.onDataQueryFail(CatchedRequestErrorType.getTypeByCode(responseCode));
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
                mPresenter.onDataQueryFail(CatchedRequestErrorType.CODING_ERROR);
                //coding fail
            }
        });
    }

    private String generatePagingPara() {
        String paginPara = Configs.PARAMETER_LIMIT + String.format(Configs.PARAMETER_OFFSET, mEntityHolder.getOffset());
        return paginPara;
    }

    public void customQuery(CallbackContract.ConnectionCallback callBack, QuerySpecification specification) {
        customQuery(callBack, specification, 0, 0);
    }

    public void customQuery(CallbackContract.ConnectionCallback callBack, QuerySpecification specification, int retryQuery, int secondOfTimeout) {
        // specification = what to do query , callBack = what to do when response
        ConnectionAsyncTask async_sample = new ConnectionAsyncTask(mApplication, callBack, specification);
        if (retryQuery > 0)//at last 1 times
            async_sample.setmNumberToRetryQuery(retryQuery);//set number to try query times
        if (secondOfTimeout > 5)//at last 5 seconds
            async_sample.setTimeoutlimit(secondOfTimeout*1000); //set timeout connect mini seconds
        async_sample.execute();
    }

    private void receiveSingleData(int resultCode, Object data) {

        if(resultCode ==HttpURLConnection.HTTP_OK ){
            if (data instanceof EntityHolder) {

                EntityHolder entityHolder = (EntityHolder) data;

                mPresenter.onSingleDataReceived(resultCode, entityHolder);
            }
        }else{
            mPresenter.onDataQueryFail(CatchedRequestErrorType.getTypeByCode(resultCode));
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
                    mPresenter.onListDataReceived(resultCode, mEntityHolder);
                } else {
                    mPresenter.onDataQueryFail(CatchedRequestErrorType.ENTITY_HOLDER_CONVERTION_FAIL);
                }
            }
        }
    }

    private String getUrl(UrlType getType){
        String finalUrl="";
        Method method ;
        try {
            String methodName = "";
            switch (getType){
                case GET_SINGLE:
                    methodName = mEntityHolder.GET_SINGLE;
                    break;
                case GET_LIST:
                    methodName = mEntityHolder.GET_LIST;
                    break;
                case POST:
                    methodName = mEntityHolder.POST;
                    break;
            }
            method = mEntityHolder.getClass().getMethod(methodName);
            finalUrl = (String)(method.invoke(mEntityHolder));

        } catch (SecurityException e) {
            Log.d("runtime","testMethod getMethod error: "+e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("runtime","testMethod getMethod error: "+e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("runtime","testMethod getMethod error: "+e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("runtime","testMethod getMethod error: "+e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("runtime","testMethod getMethod error: "+e.getMessage());
        }  catch (Exception e) {
            Log.d("runtime","testMethod getMethod error: "+e.getMessage());
        }

        return finalUrl;
    }

    private enum UrlType{
        GET_SINGLE,
        GET_LIST,
        POST
    }

//    private ResultSetObject getPackagedDevelopments(boolean resultOk, boolean canRequest, EntityHolder<T> entityHolder) {
//        ResultSetObject<ArrayList<T>> packagedDevelopments = new ResultSetObject();
//
//        packagedDevelopments.setResultOk(resultOk);
//        packagedDevelopments.setCanRequestMore(canRequest);
//        packagedDevelopments.setPageResultCount(entityHolder.offset);
//        packagedDevelopments.setResultCount(entityHolder.total);
//        packagedDevelopments.setData(new ArrayList(Arrays.asList(entityHolder.getResults())));
//
//        return packagedDevelopments;
//    }
}
