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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
    recordService r_service;
    private ServiceConnection r_connection = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder service) {
            r_service = recordService.Stub.asInterface(service);
            }

        public void onServiceDisconnected(ComponentName name) {
            r_service = null;
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
        
        title = (EditText)findViewById(R.id.username);
        classcode = (EditText)findViewById(R.id.email);
        school = (EditText)findViewById(R.id.username);
        
        mHandler = new Handler();
        
        final Context c = this;
        
        final Button recButton = (Button)findViewById(R.id.record_button);
        recButton.setVisibility(View.GONE);
        final TextView recTime = (TextView)findViewById(R.id.rec_time);
        recTime.setVisibility(View.GONE);
        
            recButton.setVisibility(View.VISIBLE);
            recTime.setVisibility(View.VISIBLE);
            recButton.setText("Record audio");
            recButton.setOnClickListener(new OnClickListener() {
                
                public void onClick(View v) {
                    
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
                        stopService(new Intent(c, rService.class));
                        topRecording++;
                        editor.putString("rec" + topRecording, "Asdf");
                        editor.putString("title" + topRecording, "Asdf");
                        editor.putString("class" + topRecording, "Asdf");
                        editor.putString("school" + topRecording, "Asdf");
                        editor.putInt("top_recording", topRecording);
                        editor.commit();
                    }
                    
              
                }});

        
        final Button sendButton = (Button)findViewById(R.id.thebutton);
        sendButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                }});
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        topRecording = prefs.getInt("top_recording", 0);
        editor = prefs.edit();
        
        }
    
    
private void bindRecordService(){
    r_servicedBind = bindService(new Intent(this, rService.class), 
        r_connection, Context.BIND_AUTO_CREATE);
}
 
    
}
