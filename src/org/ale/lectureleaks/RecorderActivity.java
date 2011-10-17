package org.ale.lectureleaks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ale.lectureleaks.rService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecorderActivity extends Activity{
    
    GetDataTask getDataTask;
    ArrayList<ArrayList<HashMap<String, String>>> al;
    Context c;
    Handler mHandler;
    String fileLoc;
    
    EditText title;
    EditText classcode;
    EditText school;
    EditText professor;
    CheckBox checkbox;
    ImageButton email_button;
    recordService r_service;
    private ServiceConnection r_connection = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder service) {
            r_service = recordService.Stub.asInterface(service);
            try {
                r_service.start();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }

        public void onServiceDisconnected(ComponentName name) {
            r_service = null;
            }
    };
    
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
	
    
    private boolean r_servicedBind;
    private boolean recording = false;
    TimerTask updateRec;
    int time = 0;
    private Timer timer;
    
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int topRecording;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recorder);
        
        TextView titlebar = (TextView)findViewById(R.id.title_bar);
        
        title = (EditText)findViewById(R.id.title);
        classcode = (EditText)findViewById(R.id.course);
        school = (EditText)findViewById(R.id.school);
        professor = (EditText)findViewById(R.id.email);
        
        mHandler = new Handler();
        
        final Context c = this;
        
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        final Button recButton = (Button)findViewById(R.id.record_button);
        recButton.setVisibility(View.GONE);
        final TextView recTime = (TextView)findViewById(R.id.rec_time);
        recTime.setVisibility(View.GONE);
        
            recButton.setVisibility(View.VISIBLE);
            recTime.setVisibility(View.VISIBLE);
            recButton.setText("Record audio");
            recButton.setOnClickListener(new OnClickListener() {
                
                public void onClick(View v) {
                    
                    if((title.getText().toString().equals("")) || (classcode.getText().toString().equals("")) || (school.getText().toString().equals(""))) {
                        Toast.makeText(c, "Please fill out the required fields.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    if(!recording) {
                        recording = true;
                        timer = new Timer("recTimer"); 
                        time = 0;
                        updateRec = new TimerTask() {

                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() { 
                                        time++;
                                        Integer mins = time/60;
                                        String secs;
                                        if(time%60<10) {
                                            secs = "0" + new Integer(time%60).toString();
                                        }
                                        else{
                                            secs = new Integer(time%60).toString();
                                        }
                                        recTime.setText(mins.toString() + ":" + secs);
                                    }
                                } ); 
                                
                            }};
                        timer.scheduleAtFixedRate(updateRec, 0, 1000);
                        recButton.setText("Stop recording!");
                        Intent intent = new Intent(rService.ACTION_FOREGROUND);
                        intent.setClass(RecorderActivity.this, rService.class);
                        startService(intent);
                        bindRecordService();


                    }
                    else {
                        Toast.makeText(c, "Recording saved! Now, please upload it.", Toast.LENGTH_SHORT).show();
                        recording = false;
                        timer.cancel();
                        timer.purge();
                        recButton.setText("Recording saved!");
                        recButton.setClickable(false);
                        recButton.setBackgroundColor(Color.rgb(0, 33, 66));
                        String path;
                        try {
                            path = r_service.getPath();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            path = "fuck.";
                        }
                        try {
                            r_service.stop();
                            stopService(new Intent(c, rService.class));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        
                        topRecording++;
                        editor.putString("rec" + topRecording, Environment.getExternalStorageDirectory().getAbsolutePath() + path);
                        editor.putString("title" + topRecording, title.getText().toString());
                        editor.putString("class" + topRecording, classcode.getText().toString());
                        editor.putString("school" + topRecording, school.getText().toString());
                        editor.putString("prof" + topRecording, professor.getText().toString());
                        editor.putInt("top_recording", topRecording);
                        editor.commit();
                        
                    }
                    
              
                }});

        
        final Button sendButton = (Button)findViewById(R.id.thebutton);
        sendButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	
            	if(!checkbox.isChecked()){
            		return;
            	}
            	
                if((title.getText().toString().equals("")) || (classcode.getText().toString().equals("")) || (school.getText().toString().equals(""))) {
                    Toast.makeText(c, "Please fill out the required fields.", Toast.LENGTH_SHORT).show();
                    return;
                }
            	
            	sendButton.setClickable(false);
            	sendButton.setBackgroundColor(Color.rgb(0, 33, 66));
		        
            	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		        
		        final SharedPreferences.Editor editor = prefs.edit();
		        editor.putString("class", classcode.getText().toString());
		        editor.putString("school", school.getText().toString());
	            editor.putString("title", title.getText().toString());
	            editor.putString("prof", professor.getText().toString());
	            
	            
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
            	
                }});
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        topRecording = prefs.getInt("top_recording", 0);
        editor = prefs.edit();
        
        email_button = (ImageButton)findViewById(R.id.email_button);
        email_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				professor.setVisibility(View.VISIBLE);
				TextView p_desc = (TextView)findViewById(R.id.email_desc);
				p_desc.setVisibility(View.VISIBLE);
				
			}});
        
        String s = prefs.getString("da_school", null);
        
        if(s!=null){
        	school.setText(s);
        }
        
        startService(new Intent(this, uService.class));
        bindUploadService();
        
        final SharedPreferences.Editor editor2;
        String first = prefs.getString("first_time_rec", "feck");
        if(first.contains("feck")){
        	
            editor2 = prefs.edit();
            editor2.putString("first_time_rec", "shitballs");
            editor2.commit();
        	
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);

        	alert.setTitle("Please describe this class!");
        	alert.setMessage("Please be as specific as possible when describing this lecture.\n\nIt makes our lives a whole lot easier when we are cataloging the data we receive. I know it's annoying to type on a phone, but it's worth it because it lets more people learn from this lecture. \n\nThank you!");

        	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int whichButton) {
        		return;

        	  }
        	});

        	alert.show();
            
           
        }
	}
	
  private void bindUploadService(){
      u_servicedBind = bindService(new Intent(this, uService.class), 
              u_connection, Context.BIND_AUTO_CREATE);
  }
    
private void bindRecordService(){
    r_servicedBind = bindService(new Intent(this, rService.class), 
        r_connection, Context.BIND_AUTO_CREATE);
}
 
    
}
