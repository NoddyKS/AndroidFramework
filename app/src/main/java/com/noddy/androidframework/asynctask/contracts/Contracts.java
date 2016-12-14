package com.noddy.androidframework.asynctask.contracts;

import com.noddy.androidframework.repository.entityHolder.EntityHolder;

/**
 * Created by NoddyLaw on 2016/12/14.
 */

public interface Contracts {

    interface Presenter {
        void start();

        void end();

        void receivedSingleEntity(EntityHolder entityHolder);

        void receivedEntitys(EntityHolder entityHolder);

        void receivedCustomData(Object object);
    }

    interface BaseModel {
        void start();

        void end();

        void receivedSingleEntity(EntityHolder entityHolder);

        void receivedEntitys(EntityHolder entityHolder);

        void receivedCustomData(Object object);
    }
}
