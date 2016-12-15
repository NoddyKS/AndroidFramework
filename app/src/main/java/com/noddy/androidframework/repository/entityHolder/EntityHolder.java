package com.noddy.androidframework.repository.entityHolder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class EntityHolder<T> implements Serializable {//JsonContainer
    private T[] results;

    private boolean canRequestMore;
    public int offset;//end +1
    public int zoom;
    public int page;
    public int numPages;
    public int pageSize;
    public int start;
    public int end;
    public int total;

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
                        append(this.results, p);
                    }
                }else{
                    this.results = holder.results;
                }

            }
            return true;//success merge
        } catch (Exception e) {

        }
        return false;//success merge
    }

    public void clear() {
        if (results != null)
            clearArray(results);
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

    private <T> T[] clearArray(T[] arr) {
        arr = Arrays.copyOf(arr, 0);
        return arr;
    }

    private <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
}