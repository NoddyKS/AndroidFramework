package com.noddy.androidframework;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Created by NoddyLaw on 2016/12/13.
 */

public class Until {
    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }
    public static boolean isConnected(Application application) {
        try {
            ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static <T> T[] clearArray(T[] arr) {
        arr = Arrays.copyOf(arr, 0);
        return arr;
    }

    public static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
}
