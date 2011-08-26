package org.ale.lectureleaks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AboutActivity extends Activity{
     
	TextView lead;
	EditText email;
	EditText username;
	EditText password;
	Button b;
	ProgressBar p;
	TextView loading;
	
	public void onCreate(Bundle icicle) { 
          super.onCreate(icicle); 
          
          //no title bar
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          setContentView(R.layout.about);
          
          lead = (TextView) findViewById(R.id.carrier);
          loading = (TextView) findViewById(R.id.loadingtext);
          b = (Button) findViewById(R.id.post_button);
          p = (ProgressBar) findViewById(R.id.progressbar);
          
	      lead.setText("Welcome to LectureLeaks.org, your personal OpenCourseWare repository." + 
	    		  "\n\nYou can now record, save, and upload your college lectures directly " + 
	    		  "from your iPhone or Android device.  You can also browse our library " + 
	    		  "of recordings and learn any time, anywhere." + 

	    		  "\n\nWe believe that higher education should be available to all, for the " + 
	    		  "good of society. Anybody who wants to learn should be able to, so" + 
	    		  "we're trying to develop technology which allows that." + 

	    		  "\n\nBegin recording by pressing Record during all of your lectures, then " + 
	    		  "upload them to us so we can share them with the rest of the world." + 

	    		  "\n\nBefore sharing any recordings, we encourage you to ask your " + 
	    		  "instructor's permission.  We are affirmative for open access "  + 
	    		  "education, but we also maintain full compliance with the law (see our " + 
	    		  "website for more details.)" + 

	    		  "\n\nAll recordings are released under the Creative-Commons Attribution " + 
	    		  "license, and our server doesn't record any personally identifying " + 
	    		  "information like IP addresses." + 

	    		  "\n\nLectureLeaks is a 100% Free and Open Source Project, and uses " + 
	    		  "technology produced by the OpenWatch Project. Only you can spread knowledge from the privileged few to curious minds " + 
	    		  "everywhere, one lecture at a time. \n\n- Rich, Andrew and Chris, August 2011.");

	}
	
}
