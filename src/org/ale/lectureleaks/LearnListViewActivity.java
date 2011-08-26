package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LearnListViewActivity extends Activity{
    
    LearnArrayListAdapter adapter;
    ListView lv;
    GetDataTask getDataTask;
    ArrayList<ArrayList<HashMap<String, String>>> mal;
    Context c;
    String u;
    ImageView headerButton;
    Animation rotate;
    
    View lastTouched;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detailed_menu_list);    	
        
        TextView title = (TextView)findViewById(R.id.title_bar);
        title.setText("LectureLeaks - Learn");
        
        mal = new ArrayList<ArrayList<HashMap<String,String>>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("head", "true");
        hm.put("title", "Your Recordings");
        al.add(hm);
        mal.add(al);
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("description", "Play back recordings you have made.");
        hm.put("title", "Your Recordings");
        hm.put("recordings", "true");
        al.add(hm);
        mal.add(al);
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("head", "true");
        hm.put("title", "Featured Schools");
        al.add(hm);
        mal.add(al);
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("description", "Harvard University");
        hm.put("title", "Harvard");
        hm.put("featured", "true");
        al.add(hm);
        mal.add(al);
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("description", "Massachusetts Institute Of Technology");
        hm.put("title", "MIT");
        hm.put("featured", "true");
        al.add(hm);
        mal.add(al);
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("description", "Yale University");
        hm.put("title", "Yale");
        hm.put("featured", "true");
        al.add(hm);
        mal.add(al);
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("head", "true");
        hm.put("title", "All Schools");
        al.add(hm);
        mal.add(al);
        
        hm = new HashMap<String, String>();
        al = new ArrayList<HashMap<String,String>>();
        hm.put("description", "A Complete List of Schools with Lectures");
        hm.put("title", "All Schools");
        al.add(hm);
        mal.add(al);

        //Setup the adapter views;
        adapter = new LearnArrayListAdapter(this, R.layout.list_view_row);
        System.out.println(al.size());
        System.out.println(al);
        adapter.setItems(mal);
        adapter.setContext(getBaseContext());
        adapter.setParent(this);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);
        
        final LearnListViewActivity c = LearnListViewActivity.this;
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ArrayList<HashMap<String, String>> in = mal.get(arg2);
                HashMap<String, String> shmap = in.get(0);
 
                if(shmap.containsKey("head") && shmap.get("head").equals("true")) {
                    //fuck
                }
                else if(shmap.containsKey("title") && shmap.get("title").equals("All Schools")){
                    arg1.setBackgroundResource(R.layout.header_bar_gradient);
                    Intent i = new Intent(c, LearnSubListViewActivity.class);
                    i.putExtra("title", shmap.get("title"));
                    startActivity(i);
                    lastTouched = arg1;
                }
                else if(shmap.containsKey("title") && shmap.get("title").equals("Your Recordings")){
                    arg1.setBackgroundResource(R.layout.header_bar_gradient);
                    Intent i = new Intent(c, LearnYourRecordingsListViewActivity.class);
                    i.putExtra("title", shmap.get("title"));
                    startActivity(i);
                    lastTouched = arg1;
                }
                else {
                    arg1.setBackgroundResource(R.layout.header_bar_gradient);
                    Intent i = new Intent(c, LearnSchoolListViewActivity.class);
                    i.putExtra("School", shmap.get("description"));
                    i.putExtra("body_text", shmap.get("full_text"));
                    startActivity(i);
                    lastTouched = arg1;
                }
                
            }});
        
    }
    
    public void onResume() {
        super.onResume();
        ///XXX HACK CHANGE THIS OMG
        if(lastTouched != null) {
            lastTouched.setBackgroundColor(Color.WHITE);
        }
    }

}
