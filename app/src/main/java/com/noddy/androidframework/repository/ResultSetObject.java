package com.noddy.androidframework.repository;

/**
 * Created by NoddyLaw on 2016/12/6.
 */

public class ResultSetObject<T> {

    boolean isResultOk = false;
    boolean canRequestMore = false;
    int pageResultCount, resultCount;

    T data;

    public boolean isResultOk() {
        return isResultOk;
    }

    public boolean canRequestMore() {
        return canRequestMore;
    }

    public int getPageResultCount() {
        return pageResultCount;
    }

    public int getResultCount() {
        return resultCount;
    }

    public T getData() {
        return data;
    }

    public void setResultOk(boolean resultOk) {
        isResultOk = resultOk;
    }

    public void setCanRequestMore(boolean canRequestMore) {
        this.canRequestMore = canRequestMore;
    }

    public void setPageResultCount(int pageResultCount) {
        this.pageResultCount = pageResultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public void setData(T data) {
        this.data = data;
    }
}
