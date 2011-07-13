package org.ale.lectureleaks;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.content.res.AssetManager;

public class Utilities {
   
    public static String queryRESTurl(String url) {
        System.out.println("Quering RESTUrl");
        HttpClient httpclient = new DefaultHttpClient();
        
        url = url.replace(" ", "%20");
        
        HttpGet httpget = new HttpGet(url);
        System.out.println(url);
        HttpResponse response;
        
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String queryRESTurlWithParams(String url, HashMap<String, String> params) {
        System.out.println("Quering RESTUrl");
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpParams p = httpget.getParams();
        
        Iterator<String> i = params.keySet().iterator();
        String s = "";
        while(i.hasNext()) {
            s = (String) i.next();
            System.out.println(s + ": " + params.get(s));
            p.setParameter(s, params.get(s));
        }
        httpget.setParams(p);
        
        HttpResponse response;
        
        try {
            System.out.println(httpget);
            System.out.println(httpget.getURI());
            System.out.println(httpget.getParams());
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    public static void writeStringToFile(String filename, String contents, Context c) {
        System.out.println("Writing to local file: " + filename); 
        
        // Something's gone wrong.
        if(contents == null || contents == "")
            return;
        
        try
        {
            FileOutputStream fos = c.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(contents.getBytes());
            fos.close();
        }
        catch(FileNotFoundException ex)
        {
            ex.printStackTrace(); 
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
         
    
    }
    
    public static String readStringFromFile(String filename, Context c) {
        System.out.println("Reading from local file: " + filename); 
        InputStream fis;
        try {
            fis = c.openFileInput(filename);
        } catch (FileNotFoundException e) {
            // File doesn't exist (first time view is rendered - use local copy.)
            AssetManager am = c.getAssets();
            try {
                fis = am.open(filename);
            } catch (IOException e1) {
                //Something had gone disasterously wrong.
                e1.printStackTrace();
                return "";
            }
        }
        DataInputStream dis = new DataInputStream(fis);
        String ret = "";
        String strline = "";
        try {
            while((strline = dis.readLine()) != null) {
                ret = ret + strline;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return ret;
    }
    
}
