package com.noddy.androidframework.repository.entityHolder;

import com.noddy.androidframework.contracts.Contracts;

import java.io.Serializable;
import java.util.Arrays;

import static com.noddy.androidframework.Until.append;
import static com.noddy.androidframework.Until.clearArray;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public abstract class EntityHolder<T> implements Serializable {//JsonContainer
    private T[] results;

    private boolean canRequestMore;
    private int offset;//end +1
    private int zoom;
    private int page;
    private int numPages;
    private int pageSize;
    private int start;
    private int end;
    private int total;

    public static final String GET_SINGLE = "getSingleUrl";

    public static final String GET_LIST = "getListUrl";

    public static final String POST = "getPostUrl";

    abstract String getSingleUrl();

    abstract String getListUrl();

    abstract String getPostUrl();

    public boolean isCanRequestMore() {
        return canRequestMore;
    }

    public void setCanRequestMore(boolean canRequestMore) {
        this.canRequestMore = canRequestMore;
    }

    public T[] getResults() {
        return results;
    }

    public void setResults(T[] results) {
        this.results = results;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }


    public boolean merge(EntityHolder<T> holder) {

        try {
            this.total = holder.total;
            this.offset = holder.end + 1;
            this.page = holder.page;
            this.numPages = holder.numPages;

            this.canRequestMore = holder.page != holder.numPages;

            if (holder.results != null && holder.results.length > 0) {
                if(this.results!=null){
                    for (T p : holder.results) {
                        this.results = append(this.results, p);
                    }
                }else{
                    this.results = holder.results;
                }

            }
            return true;//success merge EntityHolder
        } catch (Exception e) {

        }
        return false;//fail merge EntityHolder
    }

    public void clear() {
        if (results != null)
            results =clearArray(results);
        canRequestMore = false;
        offset = 0;//end +1
        zoom = 0;
        page = 0;
        numPages = 0;
        pageSize = 0;
        start = 0;
        end = 0;
        total = 0;
    }
}