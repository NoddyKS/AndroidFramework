package com.noddy.androidframework.asynctask.specification;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noddy.androidframework.asynctask.specification.base.BaseQuerySpecification;
import com.noddy.androidframework.config.Configs;

import org.mortbay.jetty.HttpException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;

import static com.noddy.androidframework.Until.checkNotNull;

/**
 * Created by NoddyLaw on 2016/12/6.
 */

public class GetQuerySpectification extends BaseQuerySpecification {
    private final String TAG = getClass().getName();

    private String mQueryUrl, mToken,mDateFormat;

    private Class mEntityClass;

    private final String mDefaultDateFormatForJson = "yyyy-MM-dd'T'HH:mm:ss";

    public GetQuerySpectification(String queryUrl, String token, Class className) {

        mQueryUrl = checkNotNull(queryUrl, "GetQuerySpectification: queryUrl cannot be null!");
        mToken = checkNotNull(token, "GetQuerySpectification: token cannot be null!");
        mEntityClass = checkNotNull(className, "GetQuerySpectification: className cannot be null!");
        mDateFormat = mDefaultDateFormatForJson;
    }

    public GetQuerySpectification(String queryUrl, String token, Class className, String dateFormat) {

        mQueryUrl = checkNotNull(queryUrl, "GetQuerySpectification: queryUrl cannot be null!");
        mToken = checkNotNull(token, "GetQuerySpectification: token cannot be null!");
        mEntityClass = checkNotNull(className, "GetQuerySpectification: className cannot be null!");
        mDateFormat = (dateFormat == null) ? mDefaultDateFormatForJson : dateFormat;
    }

    @Override
    public Object onQuery() {
        return getContent(mQueryUrl, mEntityClass, mToken);
    }

    private Object getContent(String url, Class className, String token) {
        try {
            String content = getResult(url, token);
            Gson gson = new GsonBuilder().setDateFormat(mDateFormat).create();

            return gson.fromJson(content, className);

        } catch (Exception e) {
            Log.d(TAG, "Connection getContent fail: "+e.getMessage());
            String s = e.getMessage();
            return null;
        }
    }

    private String getResult(String url, String token) throws IOException, HttpException {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setConnectTimeout(Configs.HTTP_CONNECT_TIMEOUT);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            String responseMessage = urlConnection.getResponseMessage();
            Log.d("runtime", responseCode + " " + url);

            setResponseCode(responseCode);
            setResponseMsg(responseMessage);
            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream inputStream = new InflaterInputStream(urlConnection.getInputStream(), new Inflater(true));
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();

                return stringBuilder.toString();
            } else {
                return null;
            }

        } catch (HttpException sex) {
            Log.d("runtime", "Connection-getResult-error HttpException msg:" + sex.getMessage());
        } catch (SocketTimeoutException stex) {
            Log.d("runtime", "Connection-getResult-error SocketTimeoutException msg:" + stex.getMessage());
        } catch (IOException iex) {
            Log.d("runtime", "Connection-getResult-error IOException msg:" + iex.getMessage());
        } catch (Exception ex) {
            Log.d("runtime", "Connection-getResult-error Exception msg:" + ex.getMessage());
        }
        return null;
    }
}
