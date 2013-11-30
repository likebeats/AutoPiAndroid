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

    //protected static final String API_URL = "http://autopi.herokuapp.com/api/v1";
    public static final String API_URL = "http://192.168.1.120:8000/api/v1";

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

            /*HttpPut postRequest = new HttpPut(API_URL+query);
            //postRequest.setHeader("Accept", "application/json");
            StringEntity input = new StringEntity(params.toString(), HTTP.UTF_8);
            input.setContentType("application/json");
            postRequest.setHeader("Content-Type", "application/json;charset=UTF-8");
            postRequest.setHeader("Authorization", "Basic " + authStringEnc);
            postRequest.setEntity(input);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            BasicHttpResponse response = (BasicHttpResponse)httpClient.execute(postRequest);

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity);

            System.out.println("RESPONSE: "+responseString);
            System.out.println("STATUS: "+response.getStatusLine().toString());
            httpClient.getConnectionManager().shutdown();*/

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

        /*Session session = Session.getInstance();
        if (authStringEnc == null) authStringEnc = session.authString;

        HttpURLConnection connection = null;
        try {

            System.out.println("*** BEGIN CALLING API: "+API_URL+query+" ***");

            String authString = username + ":" + password;
            System.out.println("auth string: " + authString);
            byte[] authEncBytes = Base64.encode(authString.getBytes(), Base64.DEFAULT);
            String authStringEnc = new String(authEncBytes);
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

        return null;*/
    }
}
