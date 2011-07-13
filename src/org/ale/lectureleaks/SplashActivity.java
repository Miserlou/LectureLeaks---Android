package org.ale.lectureleaks; 

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.os.Handler; 
import android.view.Window;

public class SplashActivity extends Activity { 
      
     private final int SPLASH_DISPLAY_LENGHT = 4000; 
     private Handler mHandler; 

     /** Called when the activity is first created. */ 
     @Override 
     public void onCreate(Bundle icicle) { 
          super.onCreate(icicle); 
          
          //no title bar
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          
          setContentView(R.layout.splash); 
          mHandler = new Handler();
     }
     
     public void onStart() {
         super.onStart();
         mHandler.postDelayed(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                Intent mainIntent = new Intent(SplashActivity.this,MainMenuActivity.class); 
                SplashActivity.this.startActivity(mainIntent); 
                SplashActivity.this.finish();
                
            }}, SPLASH_DISPLAY_LENGHT);
 
     }
     
}