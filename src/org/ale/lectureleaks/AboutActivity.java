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
          
	      lead.setText("Welcome to LectureLeaks.org, your personal open courseware repository.  You can now record, save, and upload your college lectures directly from your iPhone, or Android device.  You can also browse our library of lecture recordings to satisfy your curiosity any time, anywhere.\n" + 
	      		"\n" + 
	      		"Begin recording with a click or tap, then share your lessons with some basic details about the class.  By contributing to Lecture Leaks you are helping yourself and others gain access to knowledge.  If you choose to share your recordings, we will never expose your identity to anyone for any reason.  We also respect the privacy and copyright concerns of professors.  So please ask permission of anyone whose lessons you plan to record and keep a local copy of anything you post online.  We will always comply with legitimate DMCA take-down requests and post the details on ChillingEffects.org.\n" + 
	      		"\n" + 
	      		"Now, you can spread knowledge from the privileged few to curious minds everywhere.  Thank you for helping us open access to higher education.  Together we can disrupt the ivory tower's monopoly on learning and help all people exercise their fundamental human right to education. ");

	}
	
}
