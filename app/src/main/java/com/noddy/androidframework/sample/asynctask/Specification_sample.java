package com.noddy.androidframework.sample.asynctask;

import com.noddy.androidframework.specification.base.BaseQuerySpecification;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class Specification_sample extends BaseQuerySpecification {
    @Override
    public Object onQuery() {
        int total = 0;
        for(int i =0; i<1000 ; i++){
            total =i;
        }
        setResponseCode(200);
        return total;
    }
}
