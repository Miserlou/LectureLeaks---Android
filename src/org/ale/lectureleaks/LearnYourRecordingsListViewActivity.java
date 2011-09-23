package org.ale.lectureleaks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
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

public class LearnYourRecordingsListViewActivity extends Activity{
    
    LearnAPIArrayListAdapter adapter;
    ListView lv;
    GetDataTask getDataTask;
    ArrayList<ArrayList<HashMap<String, String>>> mal;
    Context c;
    String u;
    ImageView headerButton;
    Animation rotate;
    
    View lastTouched;
    
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int topRecording;
    
    uploadService u_service;
    private boolean u_servicedBind = false;
	
    private ServiceConnection u_connection = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder service) {
            u_service = uploadService.Stub.asInterface(service);
            }

        public void onServiceDisconnected(ComponentName name) {
            u_service = null;
            }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detailed_menu_list);
        
        TextView title = (TextView)findViewById(R.id.title_bar);
        title.setText("LectureLeaks - Your Recordings");
        
        mal = new ArrayList<ArrayList<HashMap<String,String>>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        topRecording = prefs.getInt("top_recording", 0);
        editor = prefs.edit();
        
        String classcode;
        String classtitle;
        String schoolio;
        String path;
        
        for(int i=0;i<=topRecording;i++) {
            hm = new HashMap<String, String>();
            al = new ArrayList<HashMap<String,String>>();
            classcode = prefs.getString("class"+i, "");
            classtitle = prefs.getString("title"+i, "");
            schoolio = prefs.getString("school"+i, "");
            path = prefs.getString("rec"+i, "");
            hm.put("name", classcode + ": " + classtitle);
            hm.put("path", path);
            hm.put("school", schoolio);
            hm.put("class", classcode);
            hm.put("title", classtitle);
	        al.add(hm);
	        mal.add(al);
	            
        }
        
        //Setup the adapter views;
        adapter = new LearnAPIArrayListAdapter(this, R.layout.list_view_row);
        adapter.setItems(mal);
        adapter.setContext(getBaseContext());
        adapter.setParent(this);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(adapter);
        
        
        final LearnYourRecordingsListViewActivity c = LearnYourRecordingsListViewActivity.this;
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                ArrayList<HashMap<String, String>> in = mal.get(arg2);
                final HashMap<String, String> shmap = in.get(0);
 
                if(shmap.containsKey("head") && shmap.get("head").equals("true")) {
                    //fuck
                }
                else {
                    arg1.setBackgroundResource(R.layout.header_bar_gradient);
                    lastTouched = arg1;
                        final CharSequence[] items = {"Upload", "Play"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setTitle("Upload or Play?");
                        DialogInterface.OnClickListener DIO = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch(item) {
                                case 0:

                                	final SharedPreferences.Editor editor = prefs.edit();
                    		        editor.putString("class", shmap.get("subject"));
                    		        editor.putString("school", shmap.get("school"));
                    	            editor.putString("title", shmap.get("title"));
                    	            
                    		        editor.commit();
                    				
                    				Handler mHandler = new Handler();
                    				mHandler.postDelayed(new Runnable(){

                    					public void run() {
                    						
                    					    try {
                                                u_service.start();
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
                    					    
                    				
                    					}}, 200);
                    				
                    				lastTouched.setBackgroundColor(Color.WHITE);
                                    return;
                                   
                                case 1:
                                    Intent i = new Intent(c, PlayerActivity.class);
                                    i.putExtra("School", shmap.get("school"));
                                    i.putExtra("Subject", shmap.get("subject"));
                                    i.putExtra("Course", shmap.get("course"));
                                    i.putExtra("streamURL", shmap.get("path"));
                                    i.putExtra("title", shmap.get("title"));
                                    startActivity(i);
                                    return;
                                }
                                
                                
                            }
                            };
                        builder.setItems(items, DIO);
                        AlertDialog alert = builder.create();
                        alert.show();
                }
                    
            }
        });
        
        startService(new Intent(this, uService.class));
        bindUploadService();
    }
    
    public void onResume() {
        super.onResume();
        ///XXX HACK CHANGE THIS OMG
        if(lastTouched != null) {
            lastTouched.setBackgroundColor(Color.WHITE);
        }
    }
    


	private void bindUploadService(){
	  u_servicedBind = bindService(new Intent(this, uService.class), 
	          u_connection, Context.BIND_AUTO_CREATE);
	}

}
