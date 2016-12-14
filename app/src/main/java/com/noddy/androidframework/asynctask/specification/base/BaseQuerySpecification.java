package com.noddy.androidframework.asynctask.specification.base;

/**
 * Created by NoddyLaw on 2016/12/6.
 */

public  abstract class BaseQuerySpecification {
    private int mResponseCode;
    private String mResponseMsg;

    public abstract Object onQuery();

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int responseCode) {
        this.mResponseCode = responseCode;
    }

    public String getResponseMsg() {
        return mResponseMsg;
    }

    public void setResponseMsg(String mResponseMsg) {
        this.mResponseMsg = mResponseMsg;
    }
}
