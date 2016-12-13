package com.noddy.androidframework.repository.entityHolder;

import java.util.ArrayList;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class EntityHolder<T> {
    private ArrayList<T> data;

    private int offset;
    private int total;
    private int numPage;
    private int page;

    private EntityHolder() {
        data = new ArrayList<>();
        offset = 0;
        total = 0;
        numPage = 0;
        page = 0;
    }
}