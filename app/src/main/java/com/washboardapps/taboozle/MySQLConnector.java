package com.washboardapps.taboozle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Christian on 23/08/2016.
 */
public class MySQLConnector {

    //executes query by sending an http request to the MySQL database. Returns boolean success.
    public static boolean query(String query) {

        InputStream is = null;

        String result = "";
        //the year data to send
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("query", query));

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://christianszucs.com/php/mysqlWebService.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            System.err.println("Error in http connection " + e.toString());
            return false;
        }
        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            if (is != null){
                is.close();
            }

            result = sb.toString();
        } catch (Exception e) {
            System.err.println("Error converting result " + e.toString());
            return false;
        }

        //parse json data
        try {
            System.out.println("result: " + result);
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                System.out.println("CALLED: " + json_data.getInt("CALLED"));
            }
        } catch (JSONException e) {
            System.err.println("Error parsing data " + e.toString());
            return false;
        }
        return true;
    }
}