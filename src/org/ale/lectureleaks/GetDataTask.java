package org.ale.lectureleaks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class GetDataTask extends AsyncTask<String, String, String>{

    String url;
    List<HashMap<String, String>> l;
    Context c;
    ActivityAdapter adapter;
    String resultString;
    ArrayList<HashMap<String,String>> al;
    ProgressBar spinner;
    
    @Override
    protected String doInBackground(String... urls) {
        try {
//            al = ru.getBeacons();
        }
        catch(Exception e){
            return null;
        }
        return null;
    }
    
    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
        adapter.setList(al);
        adapter.notifyDataSetChanged();
        spinner.setVisibility(View.GONE);
    } 
    
    public void setContext(Context co){
        c = co;
    }
    
    public void setAdapter(ActivityAdapter a){
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
    
}
