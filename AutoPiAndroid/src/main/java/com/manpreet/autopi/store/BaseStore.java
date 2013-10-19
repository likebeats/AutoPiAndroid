package com.manpreet.autopi.store;

import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class BaseStore {

    protected static final String API_URL = "http://autopi.herokuapp.com/api/v1";

    protected static String getJSONApi(String urlPath, String username, String password) {

        HttpURLConnection urlConnection = null;
        try {

            System.out.println("*** BEGIN CALLING API: "+API_URL+urlPath+" ***");

            String authString = username + ":" + password;
            System.out.println("auth string: " + authString);
            byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT);
            String authStringEnc = new String(authEncBytes);
            System.out.println("Base64 encoded auth string: " + authStringEnc);

            URL url = new URL(API_URL+urlPath);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            System.out.println("ResponseCode: "+urlConnection.getResponseCode());

            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();

            System.out.println("*** END CALLING API: "+API_URL+urlPath+" ***");

            urlConnection.disconnect();

            return result;

        } catch (ProtocolException pe) {
            pe.printStackTrace();
        } catch (IllegalStateException ie) {
            ie.printStackTrace();
        } catch (IOException e) {
            if (urlConnection != null) {
                try {
                    System.out.println("IOException- ResponseCode: "+urlConnection.getResponseCode());
                } catch (IOException e2) {
                    //e2.printStackTrace();
                }
            }
            //e.printStackTrace();
        }

        return null;
    }
}
