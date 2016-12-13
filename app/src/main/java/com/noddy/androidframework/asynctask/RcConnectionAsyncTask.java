package com.noddy.androidframework.asynctask;

import android.util.Log;

/**
 * Created by NoddyLaw on 2016/12/5.
 */

public abstract class RcConnectionAsyncTask extends android.os.AsyncTask<Object, Object, Object> {
    private final int TIME_WAIT = 4 * 1000;
    private final int NUM_TO_TRY_TO_SEND = 3;

    public abstract Object onApiRequest();

    public abstract void onApiResponse(Object result);

    @Override
    protected Object doInBackground(Object... arg0) {

        for (int i = 0; i < NUM_TO_TRY_TO_SEND; i++) {

            Object resultObject = onApiRequest();

            if (resultObject != null) {

                return resultObject;
            }
            try {
                Thread.sleep(TIME_WAIT);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception ex) {
                Log.d("TAG", ex.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
    }
}
