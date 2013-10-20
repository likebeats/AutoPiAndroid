package com.manpreet.autopi.store;

import android.util.Base64;

import com.manpreet.autopi.Session;

import org.apache.http.NameValuePair;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class BaseStore {

    protected static final String API_URL = "http://autopi.herokuapp.com/api/v1";

    protected static String getApi(String query, String authStringEnc) {
        HttpURLConnection urlConnection = null;
        try {

            System.out.println("*** BEGIN CALLING API: "+API_URL+query+" ***");

            URL url = new URL(API_URL+query);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();

            System.out.println("*** END CALLING API: "+API_URL+query+" ***");

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

    protected static String api(String query, String method, List<NameValuePair> params, String authStringEnc) {

        Session session = Session.getInstance();
        if (authStringEnc == null) authStringEnc = session.authString;

        HttpURLConnection connection = null;
        try {

            System.out.println("*** BEGIN CALLING API: "+API_URL+query+" ***");

            /*String authString = username + ":" + password;
            System.out.println("auth string: " + authString);
            byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT);
            String authStringEnc = new String(authEncBytes);*/
            System.out.println("Base64 encoded auth string: " + authStringEnc);

            String urlParameters = getQuery(params);
            byte[] bodyData = null;
            bodyData = urlParameters.getBytes();
            System.out.println("PARAMS: "+urlParameters);

            URL url = new URL(API_URL+query);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod(method);
            //connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            connection.setRequestProperty("Content-Length", "" + Integer.toString(bodyData.length));

            //connection.setUseCaches(false);
            //System.out.println("ResponseCode: "+connection.getResponseCode());

            //System.out.println("REQUEST PROPERTIES: "+connection.getRequestProperties());

            if (urlParameters != null) {
                OutputStream os = connection.getOutputStream();
                //BufferedWriter writer = new BufferedWriter(
                        //new OutputStreamWriter(os, "UTF-8"));
                os.write(bodyData);
                os.flush();
                os.close();
            }

            //System.out.println("HEADER FIELDS: "+connection.getHeaderFields());


            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();

            System.out.println("*** END CALLING API: "+API_URL+query+" ***");

            connection.disconnect();

            return result;

        } catch (ProtocolException pe) {
            pe.printStackTrace();
        } catch (IllegalStateException ie) {
            ie.printStackTrace();
        } catch (IOException e) {
            if (connection != null) {
                try {
                    System.out.println("IOException- ResponseCode: "+connection.getResponseCode());
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            e.printStackTrace();
        }

        return null;
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        if (params == null) return null;

        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
