package org.ale.lectureleaks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import org.ale.lectureleaks.PlayerService.PlayerBinder;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.app.Activity; 
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class PlayerActivity extends Activity implements
OnBufferingUpdateListener, OnCompletionListener, OnErrorListener,
OnInfoListener, OnSeekBarChangeListener, OnPreparedListener { 
	
	
	String streamURL;
	String id;
	String title;
	String model;
	Boolean doneBuffering = false;
	Boolean setupAndPlayed = false;
	
	private PlayerService p_service;
	private boolean service_bound = false;
	private boolean paused = true;
	private BroadcastReceiver receiver = new ListenBroadcastReceiver();
	private Thread updateProgressThread;

	private boolean isPausedInCall = false;
	private TelephonyManager telephonyManager = null;
	private PhoneStateListener listener = null;
	
	private boolean onPaused = false;
	
	public int lastPosition;
	public boolean problem = false;
	
	// amount of time to rewind playback when resuming after call 
	private final static int RESUME_REWIND_TIME = 3000;
	
	public Context c;
	
	public String burstlyViewId = "";
	
    private ServiceConnection m_connection = new ServiceConnection(){

		public void onServiceConnected(ComponentName name, IBinder service) {
	        p_service = ((PlayerService.PlayerBinder) service).getService();
	        onBindComplete((PlayerService.PlayerBinder) service);
			}



		public void onServiceDisconnected(ComponentName name) {
			p_service = null;
			}
    };
    
    private Handler handler = new Handler() {};
    private UpdateHandler uHandle; 
      
    
	private void onBindComplete(PlayerBinder service) {
		
		System.out.println("ON BIND COMPLETE");
		
		service.setPlayer(this);
		startUpdateThread();
		
		System.out.println("ON BIND COMPLETE");
		
		if(p_service.isPlaying()){
			System.out.println("Im playing!!");
			paused = false;
    		 pausePlayButton.setImageResource(R.drawable.play_controls_pause);
    		 pausePlayButton.setOnClickListener(new View.OnClickListener() {
    	            public void onClick(View v) {
    	            	p_service._pause();
						swapPlayButton();
    	            }
    	 });
    	 }
    	 else{
    		 paused = true;
    		 pausePlayButton.setImageResource(R.drawable.play);
    		 pausePlayButton.setOnClickListener(new View.OnClickListener() {
 	            public void onClick(View v) {
 	            	
 	            	System.out.println("Settinupandplayed?");
 	            	if(!setupAndPlayed){
 	            		System.out.println("Yeeee");
 	            		setUpAndPlay(streamURL,title);
 	            		setupAndPlayed=true;
 	            		return;
 	            	}
 	            	
 	            	p_service._unpause();
					swapPlayButton();
 	            }
    	 });
    	 }
         
		System.out.println("ON BIND COMPLETE");
    	 if(p_service != null){
		        progressBar.setEnabled(true);
    	 }
    	
    	 System.out.println("ON BIND COMPLETE"); 
    	if(p_service != null && !p_service.isPlaying()){
    		paused = true;
   		 	pausePlayButton.setImageResource(R.drawable.play);
   		 	pausePlayButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	
 	            	System.out.println("Settinupandplayed?");
 	            	if(!setupAndPlayed){
 	            		System.out.println("Yeeee");
 	            		setUpAndPlay(streamURL,title);
 	            		setupAndPlayed=true;
 	            		return;
 	            	}
	            	
	            	p_service._unpause();
				swapPlayButton();
	            }
   		 	});
    	}
    	else{
    	}
	}
	
    ImageButton pausePlayButton;
    SeekBar progressBar;
    TextView position;
    TextView totalLength;
    TextView titleView;
    private LinearLayout handle;
    public String school;
    public String subject;
    public String course;
    public TextView lectureText;
    public TextView classText;
    public TextView schoolText;

     /** Called when the activity is first created. */ 
     @Override 
     public void onCreate(Bundle icicle) { 
          super.onCreate(icicle); 
          
          //no title bar
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          
         
          c = getApplicationContext();
          setContentView(R.layout.player); 
          model = Build.MODEL;
          try{
        	  streamURL = getIntent().getExtras().getString("streamURL");
        	  Integer i = getIntent().getExtras().getInt("id");
        	  title = getIntent().getExtras().getString("title"); 
        	  id = i.toString();
              title = getIntent().getStringExtra("title");
              school = getIntent().getStringExtra("School");
              subject  = getIntent().getStringExtra("Subject");
              course = getIntent().getStringExtra("Course");
        	  
          }
          catch(Exception e){
          }
          
          telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
          // Create a PhoneStateListener to watch for offhook and idle events
          listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
              switch (state) {
              case TelephonyManager.CALL_STATE_OFFHOOK:
                // phone going offhook, pause the player
                if (p_service != null && p_service.isPlaying()) {
                	try {
						p_service.pause();
						isPausedInCall = true;
	                  	swapPlayButton();
					} catch (RemoteException e) {
						// TODO Auto-glerated catch block
						e.printStackTrace();
					}
                }
                break;
              case TelephonyManager.CALL_STATE_IDLE:
                // phone idle.  rewind a couple of seconds and start playing
                if (isPausedInCall && p_service != null) {
                  int resumePosition = p_service.getPosition() - RESUME_REWIND_TIME;
                  if (resumePosition < 0) {
                    resumePosition = 0;
                  }
                  p_service.seekTo(resumePosition);
                  try {
					p_service.unpause();
					swapPlayButton();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                  
                }
                break;
              }
            }
          };

          // Register the listener with the telephony manager
          telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
         

     }
     
     @Override
     public void onStart(){
    	 super.onStart();

    	 System.out.println("onstart");
    	 startService(new Intent(this, PlayerService.class));
         
       pausePlayButton = (ImageButton)findViewById(R.id.play_pause); 
       progressBar = (SeekBar)findViewById(R.id.StreamProgressBar);
       progressBar.setMax(100);
       progressBar.setOnSeekBarChangeListener(this);
       progressBar.setEnabled(false);
       position = (TextView)findViewById(R.id.at);
       totalLength = (TextView)findViewById(R.id.length);
       
       titleView = (TextView)findViewById(R.id.title_bar);
       lectureText = (TextView)findViewById(R.id.lecture_title);
       classText = (TextView)findViewById(R.id.class_title);
       schoolText = (TextView)findViewById(R.id.school_title);
       
       titleView.setText(title);
       lectureText.setText(title);
       classText.setText(course);
       schoolText.setText(school);
       
       //bindService();
       //this.setUpAndPlay(streamURL, title);
     }
     
     @Override
     public void onResume(){
    	 super.onResume();

    	 System.out.println("onresume");
    	 bindService();
         registerReceiver(receiver, new IntentFilter(this.getClass().getName()));
    	 
    	 pausePlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	System.out.println("Settinupandplayed?");
            	if(!setupAndPlayed){
            		System.out.println("Yeeee");
            		setUpAndPlay(streamURL,title);
            		setupAndPlayed=true;
            		return;
            	}
            	
            	if(p_service != null){
	            	p_service._unpause();
					swapPlayButton();
            	}
            }
        });
    	 
    	if(p_service != null){
	    	if(!p_service.isPlaying()){
	    		System.out.println("I AM NOT PLAYING");
	    		paused = true;
	   		 	pausePlayButton.setImageResource(R.drawable.play);
	   		 	pausePlayButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	
		            	System.out.println("Settinupandplayed?");
		            	if(!setupAndPlayed){
		            		System.out.println("Yeeee");
		            		setUpAndPlay(streamURL,title);
		            		setupAndPlayed=true;
		            		return;
		            	}
		            	
		            	p_service._unpause();
		            	swapPlayButton();
		            }
	   		 	});
	    	}
	    	else{
	    		System.out.println("I AM PLAYING");
	    		paused = false;
	   		 	pausePlayButton.setImageResource(R.drawable.pause);
	   		 	pausePlayButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	p_service._pause();
					swapPlayButton();
		            }
	   		 	});
	    	}
    	}
    	else{
    		System.out.println("NULL PS");
    	}
       
       	 if(uHandle == null){
       		 uHandle = new UpdateHandler();
       	 }
       	 uHandle.removeMessages(1);
       	 uHandle.sendEmptyMessage(1);
       	 
     }
     
     public void playPause(){
	    	if(!p_service.isPlaying()){
	    		try {
	    			p_service.seekToZero();
					p_service._silent_unpause();
					p_service.pause();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
     }
     
     public void swapPlayButton(){
    	 if(paused){
    		 pausePlayButton.setImageResource(R.drawable.play_controls_pause);
    		 pausePlayButton.setOnClickListener(new View.OnClickListener() {
    	            public void onClick(View v) {
    	            	p_service._pause();
						swapPlayButton();
    	            }
    	 });
    	 }
    	 else{
    		 pausePlayButton.setImageResource(R.drawable.play);
    		 pausePlayButton.setOnClickListener(new View.OnClickListener() {
 	            public void onClick(View v) {
 	            	
 	            	System.out.println("Settinupandplayed?");
 	            	if(!setupAndPlayed){
 	            		System.out.println("Yeeee");
 	            		setUpAndPlay(streamURL,title);
 	            		setupAndPlayed=true;
 	            		return;
 	            	}
 	            	
 	            	p_service._unpause();
					swapPlayButton();
 	            }
    	 });
    	 }
    	 
    	 paused = !paused;
    	 onPaused = false;
     }
     
     
     public void startUpdateThread() {
    	  }
    	  
     
     public void updateProgress() {
    	 
    	 
    	    try {
    	      if (p_service.isPlaying()) {
    	        //int progress = 100 * p_service.getPosition() / p_service.getDuration();
    	        int progress = p_service.getPosition();
    	    	progressBar.setProgress(progress);
    	        updatePlayTime();
    	        
    	      }
    	    } catch (IllegalStateException e) {
    	    }
    	  }
     
     public void play(){
    	 
    	 //bindService();
         
         final Handler mHandler = new Handler();
         final Runnable getService = new Runnable() {
         	public void run(){
         		if(p_service==null){
         			mHandler.postDelayed(this, 100);
         		}
         		else{
                   try {
                   	
                   	String prevURL = p_service.getStreamURL();

                   	p_service.setTitle(title);
                   	  	
                   	if ((streamURL != null && (!(p_service.hasStarted())) || (prevURL != streamURL))){ 
                   		p_service.play(streamURL);
                   		
                   	}
                 
     		    int sdkVersion = 0;
    		    try {
    		      sdkVersion = Integer.parseInt(Build.VERSION.SDK);
    		    } catch (NumberFormatException e) { }
    		     if (sdkVersion >= 8){
//    		    	 progressBar.setMax(p_service.getDuration());
    		     }
                 
           		} catch (RemoteException e) {
           			e.printStackTrace();
           		}
         		}
         	}
         };
         paused = false;
    	 pausePlayButton.setImageResource(R.drawable.play_controls_pause);
    	 pausePlayButton.setOnClickListener(new View.OnClickListener() {
    	 public void onClick(View v) {
    		 p_service._pause();
    	 	swapPlayButton();
    	}
    	 });
         getService.run();
         
     }     
     
     public void bindService(){
    	 if(!service_bound){
	    	 service_bound = getApplicationContext().bindService(new Intent(this, PlayerService.class), 
	     			m_connection, Context.BIND_AUTO_CREATE);
    	 }
    	 
    	 if(p_service != null){
 	        int pos = p_service.getPosition();
 	        int max = progressBar.getMax();
 	        if(max == 0){ max = 100; }
	        double ratio = p_service.getDuration() / max; 
 	        if(ratio == 0){ ratio = 100; }
	        double progress = (int) (pos /  ratio);
	        if(p_service.isPlaying()){
	        	progressBar.setEnabled(true);
	        }
	        progressBar.setProgress((int) progress);
    	 }
    	 
     }
     
     public void rebind(){
    	 service_bound = getApplicationContext().bindService(new Intent(this, PlayerService.class), 
	     			m_connection, Context.BIND_AUTO_CREATE);
     }
     
     public void stopService(){

  		if(p_service!=null){
  			p_service._pause();
  		}
      	stopService(new Intent(this, PlayerService.class));
      }
     
     @Override
     public void onDestroy(){
     	super.onDestroy();

        if (telephonyManager != null && listener != null) {
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
          }
     	
     	try{
     		unbindService(m_connection);
     	}catch (Exception e){
     		return;
     	}
     }
     
     @Override
     public void onPause(){
     	super.onPause();
     	
     	System.out.println("onpausing");
     	
     	onPaused = true;
     	
     	unregisterReceiver(receiver);
     	uHandle.removeMessages(1);
     	uHandle = null;
     	
//        android.view.ViewGroup.LayoutParams lp = handle.getLayoutParams();
//        lp.height = 42;
//        handle.setLayoutParams(lp);
//     	burstlyView.destroy();
     	
     	}
     
     public void onBufferingUpdate(MediaPlayer mp, int percent) {
       progressBar.setSecondaryProgress(percent);
//       updatePlayTime();
     }

     public void onCompletion(MediaPlayer mp) {

     }

     public boolean onError(MediaPlayer mp, int what, int extra) {
       new AlertDialog.Builder(this).setMessage(
           "Received error: " + what + ", " + extra).setCancelable(true).show();
       swapPlayButton();
       return false;
     }

     public boolean onInfo(MediaPlayer mp, int what, int extra) {
       return false;
     }

     public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    	 
    	// This looks insane. seekBar's max simply isn't doing what it's supposed to.
    	 // and this fixes it it so it at least appears to behave normally. 
    	 
       if (fromUser) {
    	   
		    // From 2.2 on (SDK ver 8), the local mediaplayer can handle Shoutcast
		    // streams natively. Let's detect that, and not proxy.
		    int sdkVersion = 0;
		    try {
		      sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		    } catch (NumberFormatException e) { }
		    
		    if (sdkVersion < 8 && p_service.isLocalEpisode()) {
		    	int msec = p_service.getDuration() * seekBar.getProgress() / seekBar.getMax();
		    	p_service.seekTo(msec);	
		        position.setText(msecToTime(progress));
		        updatePlayTime();
		        return;
		    }

		    if (sdkVersion < 8 && !p_service.isLocal) {
		    	int possibleProgress = progress > seekBar.getSecondaryProgress() ? seekBar
		    			.getSecondaryProgress() : progress;
		    	// Only seek to position if we've downloaded the content.
		    	int msec = p_service.getDuration() * possibleProgress / seekBar.getMax();
		    	p_service.seekTo(msec);	
		    }
		    else{
		    	double ratio = p_service.getDuration() / progressBar.getMax();
		    	progress = (int) (ratio * progress); 
		    	p_service.seekTo(progress);
		    }
    	   
       }
       
       position.setText(msecToTime(progress));
       updatePlayTime();
     }
     
     private static String msecToTime(int msec) {
    	    int sec = (msec / 1000) % 60;
    	    int min = (msec / 1000 / 60) % 60;
    	    int hour = msec / 1000 / 60 / 60;
    	    StringBuilder output = new StringBuilder();
    	    if (hour > 0) {
    	      output.append(hour).append(":");
    	      output.append(String.format("%02d", min)).append(":");
    	    } else {
    	      output.append(String.format("%d", min)).append(":");
    	    }
    	    output.append(String.format("%02d", sec));
    	    return output.toString();
    	  }

     private void updatePlayTime() {
    	 
    	 if(onPaused){
    		 return;
    	 }
    	 
    	 if(p_service != null){
	    	 if (p_service.isPlaying()) {
	    		 
	    	        String current = msecToTime(p_service.getCurrentPosition());
	    	        String total = msecToTime(p_service.getDuration());
	    	        position.setText(current);
	    	        totalLength.setText(total);
	    	        
	    		    int sdkVersion = 0;
	    		    try {
	    		      sdkVersion = Integer.parseInt(Build.VERSION.SDK);
	    		    } catch (NumberFormatException e) { }
	
	    		    if (sdkVersion >= 8) {
		    	        int pos = p_service.getPosition();
		    	        int max = progressBar.getMax();
		    	        if(max == 0){
		    	        	max = 100;
		    	        }
		    	        double ratio = p_service.getDuration() / max;
		    	        if(ratio == 0){
		    	        	ratio = 1;
		    	        }
		    	        double progress = (int) (pos /  ratio); 
		    	        
//	    		    	progressBar.setMax(p_service.getDuration());
	    		    	
	    		    	// Known tested, required to update progress bar
	    		    	// Other phones, ex: Droid2, do not need this
	    		    	// and will StackOverflow if they do.
	    		    	if((!Build.DEVICE.contains("droid2")) && (!Build.DEVICE.contains("cdma_droid2")) && (!Build.DEVICE.contains("sholes")) && (!Build.DEVICE.contains("inc")) && (!Build.DEVICE.contains("cdma_shadow")) && (!Build.DEVICE.contains("cdma_droid2")) && (!Build.DEVICE.contains("dream")) && (!Build.DEVICE.contains("qsd8250_surf")) && (!Build.DEVICE.contains("miler"))&& (!Build.DEVICE.contains("bravo")) && (!Build.DEVICE.contains("GT-I9000"))&& (!Build.DEVICE.contains("passion"))&& (!Build.DEVICE.contains("sapphire"))&& (!Build.DEVICE.contains("supersonic")) && (!Build.DEVICE.contains("vision")) && (!Build.DEVICE.contains("SCH-I800"))&& (!Build.DEVICE.contains("thunderc"))&& (!Build.DEVICE.contains("thunderg"))&& (!Build.DEVICE.contains("thunderg"))&& (!Build.DEVICE.contains("streak"))&& (!Build.DEVICE.contains("SPH-D700"))&& (!Build.DEVICE.contains("SGH-T959"))&& (!Build.DEVICE.contains("cdma_venus2"))&& (!Build.DEVICE.contains("SGH-I897"))&& (!Build.DEVICE.contains("glacier"))&& (!Build.DEVICE.contains("SPH-P100"))&& (!Build.DEVICE.contains("ace"))){
//	    		    	if(model.equals("Droid") || model.equals("HTC+Dream") || model.equals("T-Mobile+G1") || model.equals("Nexus+One")){
	    		    		progressBar.setProgress((int) progress);	}
	    		    	
	    		    	// The old way of doing it. Better, but we need more
	    		    	// data to be able to do it this way reliably.
//	    		    	if(!Build.DEVICE.contains("droid2")){
//	    		    		progressBar.setProgress((int) progress);}
	    		    
	    		    }
	    		    else{
    	    		    	double d = (double) p_service.getPosition() / (double) p_service.getDuration();
    	    		    	progressBar.setProgress((int)(100 * d)); 
	    		    }
	    	        
	    	        try {
						titleView.setText(p_service.getTitle());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	    	        System.out.println("UPDATING PLAY TIME");
					pausePlayButton.setImageResource(R.drawable.play_controls_pause);
					
	    	      }
    	 }
    	 

	}

     public void onStartTrackingTouch(SeekBar seekBar) {
     }

     public void onStopTrackingTouch(SeekBar seekBar) {
     }

     public void onPrepared(MediaPlayer mp) {
       //current = player.getCurrentEntry();
       //resetUI();
     }
     
     public void setUpAndPlay(String streamUrl, String title) { 
         
    	 System.out.println("Settingupandplaying");
    	 
    	 this.streamURL = streamUrl; 
    	 this.title = title; 
    	 try {
    		if(p_service != null && streamUrl.contains("http")){ 
			p_service.pause();}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    	 handler.post(new Runnable() { 
				public void run() {
						play();		 
    	        }
    	 }
    	 );
     }
     
     public void setUp(String streamUrl, String title) { 
         
    	 this.streamURL = streamUrl; 
    	 this.title = title; 
    	 try {
    		if(p_service != null && streamUrl.contains("http")){ 
			p_service.pause();}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
     }
     
     public void setTitleViewText(String s){
    	 this.title = s;
    	 titleView.setText(this.title);
     }
     	
     class ListenBroadcastReceiver extends BroadcastReceiver {
    	
            @Override
    	    public void onReceive(Context context, final Intent intent) {
    	      
    	      streamURL = intent.getStringExtra("streamURL");
    	      id = intent.getStringExtra("id"); 

    	      titleView.setText(title);
    	      swapPlayButton();
    	      
    	      //XXX: THIS NEEDS THREADING!
    	      handler.post(new Runnable(){

				public void run() {
						play();
					}
				});
    	    }
    	  }

	
	public void setButtons(){
		p_service._unpause();
		p_service._pause();
	}
	
	public void resetMediaPlayer(){
		p_service.makeNewMediaPlayer();
	}
	
	public void setDoneBuffering(){
		doneBuffering = true;
	}
	
	public Boolean getDoneBuffering(){
		return doneBuffering;
	}
	
	public void setLastPosition(int lb){
		lastPosition = lb;
		problem = true;
		swapPlayButton();
	}
	
	public void makeErrorDiag(){
	      new AlertDialog.Builder(this)
	      .setMessage("There was a problem with the stream, which means streaming support may not be available on your phone. Please save this episode for offline listening instead by pressing Save.")
	      .setPositiveButton("Okay", null)
	      .show();
	
	}
	
	public class UpdateHandler extends Handler{
		public void handleMessage (Message msg){
//			updatePlayTime();
			if(p_service != null && p_service.isPlaying()){
				
    	        String current = msecToTime(p_service.getCurrentPosition());
    	        String total = msecToTime(p_service.getDuration());
    	        position.setText(current);
    	        totalLength.setText(total);
    	        
				
    	        int progress = p_service.getPosition();
    	    	
    	        progressBar.setMax(100);
    	    	if(progress<=100){
    	    		progressBar.setProgress(progress);
    	    	}
    	    	else{
    		    	double d = (double) p_service.getPosition() / (double) p_service.getDuration();
    		    	progressBar.setProgress((int)(100 * d)); 
    	    	}
			}
			this.sendEmptyMessageDelayed(1, 1000);
		}
		
	};
           
}