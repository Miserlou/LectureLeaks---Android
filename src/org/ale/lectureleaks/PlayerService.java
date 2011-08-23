package org.ale.lectureleaks;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
//import android.net.Uri;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class PlayerService extends Service implements OnBufferingUpdateListener, OnCompletionListener, OnErrorListener, OnInfoListener, OnPreparedListener{
	
	boolean running = false;
	boolean started = false;
	NotificationManager mNotificationManager;
	int id;
	PlayerActivity parent;
	int lastPosition;
	long lastBuffer;
	boolean problem = false;
	boolean preparing = false;
	Runnable playerRunner;
	Runnable prepareRunner;
	Thread playThread;
	String playUrl;
	
	  // we don't use this stuff yet
	  private Handler handler = new Handler() {
		  };
	Notification notification;
	RemoteViews contentView;
	Intent notificationIntent;
	PendingIntent contentIntent;
	
	String streamURL;
	String title = "Nothing playing";
	
	// Lets us do fun things with intents
	public static final String EXTRA_CONTENT_URL = "extra_content_url";
	public static final String EXTRA_CONTENT_TITLE = "extra_content_title";
	public static final String EXTRA_CONTENT_ID = "extra_content_id";
	public static final String EXTRA_ENQUEUE = "extra_enqueue";
	public static final String EXTRA_PLAY_IMMEDIATELY = "extra_play_immediately";
	public static final String EXTRA_STREAM = "extra_stream";
	
	private MediaPlayer mediaPlayer;
	private Thread updateProgressThread;
	
	private boolean isLiveStream = false;
	private boolean isAudioExtra = false;
	private boolean isRadioEpisode = true;
	public boolean isLocal = false;
	private long lastPlay = 0;
	int playId = 0;
	
	public class PlayerBinder extends Binder {

	    public void setPlayer(PlayerActivity l) {
	      parent = l;
	    }

		public PlayerService getService() {
			// TODO Auto-generated method stub
			return PlayerService.this;
		}

	  }
	
	// If we want to regularly update the notifaction status
	final Runnable mNotifier = new Runnable() {

    	   public void run() {
    		   	
    		   	update_notification();
    		   	if (!running){
    		   		handler.postDelayed(this, 10000);
    		   	}
    	   }
    	};
    	
    // Is the Eclair player fucked? Just try again.
    final Runnable mBufferCheck = new Runnable() {

        	   public void run() {
        		   	if (preparing){
        		   		try {
        		   			preparing = false;
        		   			mediaPlayer.release();
        		   			mediaPlayer = null;
        		   			makeNewMediaPlayer();
							listen(streamURL, true);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
//        		   		handler.postDelayed(this, 10000);
        		   	}
        	   }
        	};
    	
	@Override
	public void onStart(Intent intent, int startId) {	
		super.onStart(intent, startId);	
		
	    if(mediaPlayer == null){
		    makeNewMediaPlayer();
	    }
	    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}


	public void makeNewMediaPlayer() {
		
		mediaPlayer = null;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnInfoListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnErrorListener(new OnErrorListener(){
			public boolean onError(MediaPlayer arg0, final int arg1, final int arg2) {
				

			
				handler.postDelayed(new Runnable(){

					public void run() {
						if(!(mediaPlayer.isPlaying())){
							lastPosition = mediaPlayer.getCurrentPosition();
							mediaPlayer.reset();
							mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
							
							// XXX: Gingerbread madness
							// -38 = "unknown error"
							if(arg1 != -38){
								parent.makeErrorDiag();
							}
							
							if(mNotificationManager != null && notification != null){
								mNotificationManager.notify(60666, notification);
							}
							if(parent != null){
								parent.setDoneBuffering();
							}
							ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
							NetworkInfo info=connManager.getActiveNetworkInfo();
						}
						
					}
					
				}, 2000);
				
				return false;
			}});
		preparing = false;
	}
	    		
	
	@Override
	public IBinder onBind(Intent intent) {
		return new PlayerBinder();
	}
	
	public boolean play(String stream) throws RemoteException {
		return _play(stream);
	}
	
	public boolean playStream(String stream) throws RemoteException {
		return _playStream(stream);
	}
//	
//	public boolean playExtra(String stream) throws RemoteException {
//		return _playExtra(stream);
//	}
	
	public boolean pause() throws RemoteException {
		return _pause();
	}
	
	public boolean unpause() throws RemoteException {
		return _unpause();
	}

	public boolean hasStarted() throws RemoteException {
		return started;
	}
	
	
	public String getStreamURL() throws RemoteException {
		return streamURL;
	}
	
	public String getTitle() throws RemoteException {
		return title;
	}
	
	public void setTitle(String t) throws RemoteException {
		title = t;
	}

	public int getID() throws RemoteException {
		return id;
	}


	public boolean putID(int i) throws RemoteException {
		id = i;
		return true;
	}
	
	public boolean _play(String stream){
		try {
			streamURL = stream;
			isLiveStream = false;
			isAudioExtra = false;
			isRadioEpisode = true;
			isLocal = false;
		    notificationIntent = new Intent(this, PlayerActivity.class); 
//		    notificationIntent.putExtra("id", new Integer(id).toString());
//		    notificationIntent.putExtra("listType", "AllShows"); 
//		    notificationIntent.putExtra("queryInfo","");
		    notificationIntent.putExtra("setup", true);
            notificationIntent.putExtra("title",parent.title);
            notificationIntent.putExtra("School", parent.school);
            notificationIntent.putExtra("Subject", parent.subject);
            notificationIntent.putExtra("Course", parent.course);
            notificationIntent.putExtra("streamURL", parent.streamURL);
		    contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		    update_notification();
			listen(stream, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return true;

	}

//	public boolean _playLocal(String stream){
//		try {
//			streamURL = stream;
//			isLiveStream = false;
//			isAudioExtra = false;
//			isRadioEpisode = true;
//			isLocal = true;
//		    notificationIntent = new Intent(this, RadioPagingViewGroupCursorActivity.class);
//		    notificationIntent.putExtra("id", new Integer(id).toString());
//		    notificationIntent.putExtra("listType", "AllShows"); 
//		    notificationIntent.putExtra("queryInfo",""); 
//		    contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//		    update_notification();
//			listen(stream, true);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return true;
//
//	}
	
	public boolean _playStream(String stream){
		try {
			streamURL = stream;
			isLiveStream = true;
			isAudioExtra = false;
			isRadioEpisode = false;
//		    notificationIntent = new Intent(this, LiveStreamActivity.class);
		    contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		    update_notification();
			listen(stream, true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;

	}
	
//	public boolean _playExtra(String stream){
//		try {
//			streamURL = stream;
//			isLiveStream = false;
//			isAudioExtra = true;
//			isRadioEpisode = false;
//		    notificationIntent = new Intent(this, ExtrasPagingViewGroupCursorActivity.class);
//		    notificationIntent.putExtra("id", new Integer(id).toString());
//		    notificationIntent.putExtra("listType", "AllShows"); 
//		    notificationIntent.putExtra("queryInfo",""); 
//		    contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//		    update_notification();
//			listen(stream, true);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return true;
//
//	}
	
	public boolean _pause(){
		if (running){
			mNotificationManager.cancel(60666);
			mediaPlayer.pause();
		}
		running = false;
		return true;
	}
	
	public boolean _unpause(){
		if(!running){
			notificationIntent = new Intent(this, MainMenuActivity.class);
			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		    notificationIntent.putExtra("id", new Integer(id).toString());
		    contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		    update_notification();
		    
		    int sdkVersion = 0;
		    try {
		      sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		    } catch (NumberFormatException e) { }
		    
		    if(problem && sdkVersion >=8) {
		    	//Something went wrong and we're on Froyo, seek to where we left off
		    	mediaPlayer.seekTo(lastPosition);
		    	problem = false;
		    	mediaPlayer.start();
		    }
		    else if (problem && sdkVersion <8){
		    	//Something went wrong and we're on <=Eclair, nuke site from orbit
		    	try {
		    		problem = false;
					listen(streamURL, true);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    else{
		    	//We're good.
				mediaPlayer.start();
		    }
		    
			
		}
		running = true;
		return true;
	}
	
	public void seekToZero(){
		mediaPlayer.seekTo(0);
	}

	public boolean _silent_unpause(){
		if(!running){
		    
		    int sdkVersion = 0;
		    try {
		      sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		    } catch (NumberFormatException e) { }
		    
		    if(problem && sdkVersion >=8) {
		    	//Something went wrong and we're on Froyo, seek to where we left off
		    	mediaPlayer.seekTo(lastPosition);
		    	problem = false;
		    	mediaPlayer.start();
		    }
		    else if (problem && sdkVersion <8){
		    	//Something went wrong and we're on <=Eclair, nuke site from orbit
		    	try {
		    		problem = false;
					listen(streamURL, true);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    else{
		    	//We're good.
				mediaPlayer.start();
		    }
		    
			
		}
		running = true;
		return true;
	}
	
	  private void listen(final String url, final boolean stream) throws IllegalArgumentException,
      IllegalStateException, IOException {
		
		//this should never happen  
		if (url == null || url == ""){
			return;
		}
		playId++;
		
		//XXX: Using a runnable hangs the GUI and causes ocassional ANRs, which are recoverable
		//XXX: Using a new Thread relieves the GUI, but causes bizarre error codes
		//XXX: ??????
		// XXX: Soln: http://www.xoriant.com/blog/mobile-application-development/android-async-task.html ?
		
		if( playThread != null && playerRunner != null){
			handler.removeCallbacks(playerRunner);
			handler.removeCallbacks(prepareRunner);
			playerRunner = null;
			playThread = null;
		}
		
		playerRunner = new Runnable() {
            public void run() {

			// New stream to play
			if ((streamURL != null) && (url != streamURL)){
				
			    if(mediaPlayer.isPlaying()) {
			        mediaPlayer.stop();
			      }
			}
		
		    if (updateProgressThread != null && updateProgressThread.isAlive()) {
		      updateProgressThread.interrupt();
		      try {
		        updateProgressThread.join();
		      } catch (InterruptedException e) {
		      }
		    }
		    playUrl = url;
		    
		    // From 2.2 on (SDK ver 8), the local mediaplayer can handle Shoutcast
		    // streams natively. Let's detect that, and not proxy.
		    int sdkVersion = 0;
		    try {
		      sdkVersion = Integer.parseInt(Build.VERSION.SDK);
		    } catch (NumberFormatException e) { }

		    //XXX: If we call mediaPlayer.prepare() before it has finished preparing
		    //XXX: whatever it was previously told to prepare, we get an error.
		    //XXX: Unfortunately, we have no way to tell if we're in the middle
		    //XXX: of a prepare. So, instead we throttle the amount of playing
		    //XXX: that can be done to one show every five seconds and hope
		    //XXX: that gives us enough time, which experimentally it seems to.
		    
		    if(prepareRunner != null){
		    	handler.removeCallbacks(playerRunner);
		    }
		    
		    long now = System.currentTimeMillis();
		    if (now - lastPlay > 6000 && !preparing){
			    lastPlay = now;
			    setSourceAndPlay(playUrl, stream, playId);
		    }else{
		    	prepareRunner = new Runnable(){
					public void run(){
						lastPlay = System.currentTimeMillis();
						setSourceAndPlay(playUrl, stream, playId);
					}
					
		    	};
		    	handler.postDelayed(prepareRunner, 6000);
			}
            }
		};
       playThread = new Thread(playerRunner);
       playThread.start();
  }
	  
	public void resetMediaPlayer(String playUrl){

		mediaPlayer.reset();
	    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	    try {
	    	if(!isLocal){
	    		mediaPlayer.setDataSource(playUrl);
	    	}
	    	else{
	    		try {
	    			File file = new File(playUrl);
	    			FileInputStream fis = new FileInputStream(file);
	    			mediaPlayer.setDataSource(fis.getFD());

	    		} catch(FileNotFoundException e){
	    		e.printStackTrace();
	    		} catch (IllegalArgumentException e) {
	    		e.printStackTrace();
	    		} catch (IllegalStateException e) {
	    		e.printStackTrace();
	    		} catch (IOException e) {

	    		}

	    	}
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isLocalEpisode(){
		if(playUrl.contains("http")){
			return false;
		}
		return true;
	}

	public void setSourceAndPlay(String playUrl, boolean stream, int tpId){
		
		if(tpId<playId){
			return;
		}
		
		if(stream){
			isLocal = false;
		}
		
		if(preparing){
			resetMediaPlayer(playUrl);
			preparing = false;
			try {
				handler.removeCallbacks(mBufferCheck);
				handler.postDelayed(mBufferCheck, 20000);
				listen(playUrl, stream);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		
		
		resetMediaPlayer(playUrl);
		
		if(tpId<playId){
			return;
		}
		
		handler.removeCallbacks(mBufferCheck);
		handler.postDelayed(mBufferCheck, 20000);
	    try {
	      preparing = true;	
	      mediaPlayer.prepareAsync();
	    } catch (Exception e) {
	    	
				resetMediaPlayer(playUrl);
				preparing = true;
				try {
					handler.removeCallbacks(mBufferCheck);
					handler.postDelayed(mBufferCheck, 10000);
					listen(playUrl, stream);
				} catch (IllegalArgumentException f) {
					f.printStackTrace();
				} catch (IllegalStateException f) {
					f.printStackTrace();
				} catch (IOException f) {
					f.printStackTrace();
				}
	    }
	    

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mNotificationManager.cancel(60666);
		handler.removeCallbacks(mNotifier);
	}
	
	public void update_notification(){
	    
		if (notification == null){
			notification = new Notification(R.drawable.icon, "Playing.." , System.currentTimeMillis());}
		
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		
		if(contentView == null){
			contentView = new RemoteViews(getPackageName(), R.layout.playing);
			contentView.setImageViewResource(R.id.notificationimage, R.drawable.icon);
		}
		
		if(notificationIntent == null){
			 notificationIntent = new Intent(this, PlayerActivity.class);
			 contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		}
		
		contentView.setTextViewText(R.id.text, title);
		notification.contentView = contentView;
		notification.contentIntent = contentIntent;
		mNotificationManager.notify(60666, notification);
	}

	  public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		lastBuffer = arg1;
	    if (parent != null) {
	      parent.onBufferingUpdate(arg0, arg1);
	    }
	  }

	  public void onCompletion(MediaPlayer mp) {
		  lastPosition = mp.getCurrentPosition();
		  if(lastBuffer != 100){
			  // Something has gone wrong - we have completed without finishing the track.
			  // The network has probably disconnected. Let's save the position
			  // and resume from there.
			  parent.setLastPosition(lastPosition);
			  running = false;
			  problem = true;
		  }
		  mNotificationManager.cancel(60666);
	  }
	  
	  public boolean onError(MediaPlayer mp, int what, int extra) {
	    if (parent != null) {
	      return parent.onError(mp, what, extra);
	    }
	    return false;
	  }

	  
	  public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
	    if (parent != null) {
	      return parent.onInfo(arg0, arg1, arg2);
	    }
	    return false;
	  }

	  
	  public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
	    if (parent != null) {
	      parent.onProgressChanged(arg0, arg1, arg2);
	    }
	  }

	  
	  public void onStartTrackingTouch(SeekBar seekBar) {
	    if (parent != null) {
	      parent.onStartTrackingTouch(seekBar);
	    }
	  }

	  public void onStopTrackingTouch(SeekBar seekBar) {
	    if (parent != null) {
	      parent.onStopTrackingTouch(seekBar);
	    }
	  }
	  
	  public int getPosition() {
		    return mediaPlayer.getCurrentPosition();
		  }

	  public int getDuration() {
		if(mediaPlayer.isPlaying()){
			return mediaPlayer.getDuration();}
		else{
			return 3600000;
		}
	  }

	  public int getCurrentPosition() {
	    return mediaPlayer.getCurrentPosition();
	  }

	  public void seekTo(int pos) {
		//XXX: seekTo in MediaPlayer is broken.
		// The Android MediaPlayer framework is so much less
		// capable than Google claims it is. 
		// http://code.google.com/p/android/issues/detail?id=4124;  
	    mediaPlayer.seekTo(pos);
	  }
	  
	  public void rewind() {
		  	int pos = mediaPlayer.getCurrentPosition();
		    mediaPlayer.seekTo(pos - 30000);
		  }

		public void onPrepared(MediaPlayer mp) {
			parent.setDoneBuffering();
		    mp.start();
		    preparing = false;
			started = true;
		    running = true;
		    handler.removeCallbacks(prepareRunner);
		}

		public boolean isPlaying() {
			return mediaPlayer.isPlaying();
		}
		
		public void setParent(PlayerActivity p){
			parent = p;
		}
		
		public int isPlayingWhat(){
			if(isRadioEpisode){
				return 0;
			}
			else if(isAudioExtra){
				return 1;
			}
			else{
				return 2;
			}
		}
	
}