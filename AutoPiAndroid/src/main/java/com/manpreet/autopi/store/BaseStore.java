package com.manpreet.autopi.store;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class BaseStore {

    protected static final String API_URL = "http://autopi.herokuapp.com/api/v1";
    //public static final String API_URL = "http://192.168.1.120:8000/api/v1";

    public static String getApi(String query, String authStringEnc) {
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

    public static String api(String query, String method, JSONObject params, String authStringEnc) {

        try {

            System.out.println("*** BEGIN CALLING API: "+API_URL+query+" ***");

            System.out.println("PARAMS: "+params.toString());
            System.out.println("Basic " + authStringEnc);

            URL url = new URL(API_URL+query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(String.format(params.toString()));
            osw.flush();
            osw.close();
            System.err.println("RESPONSE: "+connection.getResponseCode());

            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder sb = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            System.err.println(sb.toString());

            System.out.println("*** END CALLING API: "+API_URL+query+" ***");

            return null;

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
