package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

public class GetDataTask extends AsyncTask<String, String, String>{

    String url;
    List<HashMap<String, String>> l;
    Context c;
    LectureLeaksAdapterInterface adapter;
    String resultString;
    ArrayList<ArrayList<HashMap<String,String>>> al;
    ProgressBar spinner;
    LectureLeaksJSONParser parser;
    boolean finished;
    ArrayList<ArrayList<HashMap<String, String>>> rezz;
    
    @Override
    protected String doInBackground(String... urls) {
        System.out.println(urls[0]);
        
        try {
            resultString = Utilities.queryRESTurl(urls[0]);
            System.out.println("Got result: " + resultString);
            Utilities.writeStringToFile(urls[1], resultString, c);
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
        return null;
    }
    
    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
        if(parser!=null) {
            finished = true;
            try {
                rezz = parser.parse(resultString);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(rezz);
            if(adapter!=null) {
                adapter.setItems(rezz);
                //Yuck.
                if( adapter instanceof BaseAdapter) {
                    BaseAdapter a = (BaseAdapter) adapter;
                    a.notifyDataSetChanged();
                }
            }
        }
        if(spinner != null) {
            spinner.setAnimation(null);
        }
    }
    
    public void setContext(Context co){
        c = co;
    }
    
    public void setAdapter(LectureLeaksAdapterInterface a){
        adapter = a;
    }
    
    public void setUrl(String surl) {
        url = surl;
    }
    
    public List<HashMap<String, String>> getList() {
        return l;
    }

    public void setList(List<HashMap<String, String>> l) {
        this.l = l;
    }
    
    public void setSpinner(ProgressBar pb) {
        spinner = pb;
    }

    public void setParser(LectureLeaksJSONParser lectureLeaksJSONParser) {
        parser = lectureLeaksJSONParser;
        
    }
    
}
