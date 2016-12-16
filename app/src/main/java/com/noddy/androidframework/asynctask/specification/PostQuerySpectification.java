package com.noddy.androidframework.asynctask.specification;

import android.util.Log;

import com.google.gson.Gson;
import com.noddy.androidframework.asynctask.specification.base.BaseQuerySpecification;
import com.noddy.androidframework.config.Configs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by NoddyLaw on 2016/12/7.
 */

public class PostQuerySpectification extends BaseQuerySpecification {

    private String mQueryUrl, mToken;

    private Object mUploadObject;

    public PostQuerySpectification(String queryUrl, String token, Object uploadObject){
        mQueryUrl = queryUrl;
        mToken = token;
        mUploadObject = uploadObject;

    }

    @Override
    public Object onQuery() {

        try {

            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(mQueryUrl).openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + mToken);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setConnectTimeout(Configs.HTTP_CONNECT_POST_TIMEOUT);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            SSLSocketFactory sslSocketFactory = createSslSocketFactory();
            urlConnection.setSSLSocketFactory(sslSocketFactory);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            Gson gson = new Gson();
            //to json format string
            String json = gson.toJson(mUploadObject);

            writer.write(json);
            writer.flush();
            writer.close();
            os.close();

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();

            setResponseCode(responseCode);
            setResponseMsg(responseMessage);

            Log.d("runtime","postContent query url: "+mQueryUrl + " responseCode: " +responseCode);

            if (responseCode == 200) {

                StringBuilder stringBuilder = new StringBuilder();
                InputStream inputStream = new InflaterInputStream(urlConnection.getInputStream(), new Inflater(true));
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();

                return true;
            }else {
                return null;
            }

        } catch (Exception ex) {
            setResponseCode(0);
            setResponseMsg("plase check your url / token ,whether is correct !");
            Log.d("", ex.getMessage());
            return false;
        }
    }

    private SSLSocketFactory createSslSocketFactory() throws Exception {
        TrustManager[] byPassTrustManagers = new TrustManager[]{new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, byPassTrustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }
}
