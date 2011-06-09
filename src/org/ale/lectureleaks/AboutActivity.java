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
          
	      lead.setText("About this application. LectureLeaks is a way for students to blah blah blah");

	}
	
}
