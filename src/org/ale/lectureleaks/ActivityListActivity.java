package org.ale.lectureleaks;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityListActivity extends Activity{
    
    ActivityAdapter adapter;
    ListView lv;
    Context c;
    Handler mHandler;
    ArrayList<HashMap<String,String>> al;
    GetDataTask gdt;
    ProgressBar pb;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lister);    	

        al = new ArrayList<HashMap<String,String>>();
        
        //Setup the adapter views;
        adapter = new ActivityAdapter();
        adapter.setList(al);
        adapter.setContext(getBaseContext());
        adapter.setParent(this);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);
        pb = (ProgressBar)findViewById(R.id.progressbar);
        
        c=this;
        mHandler = new Handler();
        
        gdt = new GetDataTask();
        gdt.setAdapter(adapter);
        gdt.setContext(this);
        gdt.setList(al);
        gdt.setSpinner(pb);
        gdt.execute("");
        
//        mHandler.postDelayed(new Runnable() {
//
//            public void run() {
//                al = ru.getBeacons();
//                adapter.setList(al);
//                adapter.notifyDataSetChanged();
//                
//            }}, 1);
        
        
        final ActivityListActivity c = ActivityListActivity.this;
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String, String> shmap = al.get(0);
//                Intent i = new Intent(c, DetailContactActivity.class);
//                i.putExtra("title", shmap.get("activity"));
//                i.putExtra("desc", shmap.get("description"));
//                i.putExtra("user", shmap.get("user"));
//                startActivity(i);
                }
                
            });
        
    }

}
