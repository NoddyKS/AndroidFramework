package com.noddy.androidframework.asynctask.specification.base;

/**
 * Created by NoddyLaw on 2016/12/6.
 */

public  abstract class BaseQuerySpecification {
    private int mResponseCode;

    public abstract Object onQuery();

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int responseCode) {
        this.mResponseCode = mResponseCode;
    }
}
