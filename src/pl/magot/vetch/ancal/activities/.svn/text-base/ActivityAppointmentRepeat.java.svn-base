
package pl.magot.vetch.ancal.activities;


import java.util.*;

import pl.magot.vetch.ancal.*;
import pl.magot.vetch.widgets.DateWidget;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class ActivityAppointmentRepeat extends Activity
{
	//fields
	private static final int SELECT_REPEAT_REQUEST = 201;
	
	private static final String DATAKEY_TYPE = "RepeatType";
	private static final String DATAKEY_EVERY = "Every";
	private static final String DATAKEY_ENDONDATE = "EndOnDate";
	
	//fields
  public Prefs prefs = null;
	public Utils utils = null;

	//repeat data
	private int iRepeatType = -1;
	private int iRepeatEvery = 0;
	private Calendar calRepeatEnd = Calendar.getInstance();
	
  //layouts
  private LinearLayout llayAppointmentRepeatEvery = null;
  private LinearLayout llayAppointmentRepeatEndOnDateLabel = null;
  private LinearLayout llayAppointmentRepeatEndOnDate = null;
	
	//views
  private TextView labRepeatEveryValue = null;  
	private Button btnEndOnDate = null;
	private Button btnRepeatDone = null;
	
	//repeat buttons
	private Button btnRepeatTypeNone = null;
	private Button btnRepeatTypeDaily = null;
	private Button btnRepeatTypeWeekly = null;
	private Button btnRepeatTypeMonthly = null;
	private Button btnRepeatTypeYearly = null;	
	
	//every value buttons
	private ImageButton btnRepeatEveryDown = null;
	private ImageButton btnRepeatEveryUp = null;
	
  //methods
  @Override
  public void onCreate(Bundle icicle)
  {
  	super.onCreate(icicle);    
    setContentView(R.layout.appointmentrepeat);

    GetExtrasData();
    
  	//restore data from freeze state
  	if (icicle != null)
  	{
      iRepeatType = icicle.getInt("repeat");
      iRepeatEvery = icicle.getInt("every");
      calRepeatEnd.setTimeInMillis(icicle.getLong("dateEndOn")); 
  	}

  	//initialize base objects
    prefs = new Prefs(this);
    utils = new Utils(this);

    //initialize views
    InitViews();    	
    InitState();
  }

  public void GetExtrasData()
  {
  	Bundle bundle = getIntent().getExtras();
    if ((bundle != null) && (bundle.containsKey("EditRepeat")))
    {
      iRepeatType = bundle.getInt(DATAKEY_TYPE);
      iRepeatEvery = bundle.getInt(DATAKEY_EVERY);
      calRepeatEnd.setTimeInMillis(bundle.getLong(DATAKEY_ENDONDATE)); 
    }
  }
  
  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
  	super.onSaveInstanceState(outState);
  	//save controls state
  	outState.putInt("repeat", iRepeatType);
  	outState.putInt("every", iRepeatEvery);
  	outState.putLong("dateEndOn", calRepeatEnd.getTimeInMillis());
  }  
  
  @Override
  public void onStart()
  {
  	super.onStart();
  	    
  }
  
  @Override
  public void onResume()
  {
  	super.onResume();

  }
    
  @Override
  public void onPause()
  {
  	super.onPause();
  	
  }
	
  private void InitViews()
  {
    llayAppointmentRepeatEvery = (LinearLayout)findViewById(R.id.llayAppointmentRepeatEvery);
    llayAppointmentRepeatEndOnDate = (LinearLayout)findViewById(R.id.llayAppointmentRepeatEndOnDate);
    llayAppointmentRepeatEndOnDateLabel = (LinearLayout)findViewById(R.id.llayAppointmentRepeatEndOnDateLabel);
  	
  	//repeat buttons
  	btnRepeatTypeNone = (Button)findViewById(R.id.btnRepeatTypeNone);
  	btnRepeatTypeNone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetRepeatType(0);
			}		
  	});
  	
  	btnRepeatTypeDaily = (Button)findViewById(R.id.btnRepeatTypeDaily);
  	btnRepeatTypeDaily.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetRepeatType(1);
			}		
  	});
  	
  	btnRepeatTypeWeekly = (Button)findViewById(R.id.btnRepeatTypeWeekly);
  	btnRepeatTypeWeekly.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetRepeatType(2);
			}		
  	});
  	
  	btnRepeatTypeMonthly = (Button)findViewById(R.id.btnRepeatTypeMonthly);
  	btnRepeatTypeMonthly.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetRepeatType(3);
			}		
  	});
  	
  	btnRepeatTypeYearly = (Button)findViewById(R.id.btnRepeatTypeYearly);
  	btnRepeatTypeYearly.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetRepeatType(4);
			}		
  	});
  	
  	btnEndOnDate = (Button)findViewById(R.id.btnAppointmentEndOnDate);
  	
  	btnRepeatDone = (Button)findViewById(R.id.btnRepeatDone);
  	btnRepeatDone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				EditDone();
			}
  	});

  	btnEndOnDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
								
				DateWidget.Open(ActivityAppointmentRepeat.this, true, calRepeatEnd, prefs.iFirstDayOfWeek);

			}
		});
  	
    labRepeatEveryValue = (TextView)findViewById(R.id.labRepeatEveryValue);
    
  	btnRepeatEveryDown = (ImageButton)findViewById(R.id.btnRepeatEveryDown);
  	btnRepeatEveryDown.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				iRepeatEvery--;
				if (iRepeatEvery < 1)
					iRepeatEvery = 1;
				UpdateEveryValueView();
			}
  	});

  	btnRepeatEveryUp = (ImageButton)findViewById(R.id.btnRepeatEveryUp);
  	btnRepeatEveryUp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				iRepeatEvery++;
				if (iRepeatEvery > 30)
					iRepeatEvery = 30;
				UpdateEveryValueView();
			}
  	});    
  }
  
  public void SetRepeatType(int iRepeatType)
  {
  	//update global value
  	this.iRepeatType = iRepeatType;
  	//enable buttons
  	btnRepeatTypeNone.setEnabled((iRepeatType != 0));
  	btnRepeatTypeDaily.setEnabled((iRepeatType != 1));
  	btnRepeatTypeWeekly.setEnabled((iRepeatType != 2));
  	btnRepeatTypeMonthly.setEnabled((iRepeatType != 3));
  	btnRepeatTypeYearly.setEnabled((iRepeatType != 4));  	
  	// 0: None
  	Button btn = null;
  	if (iRepeatType == 0)
  		btn = btnRepeatTypeNone;
  	// 1: Daily
  	if (iRepeatType == 1)
  		btn = btnRepeatTypeDaily;
  	// 2: Weekly
  	if (iRepeatType == 2)
  		btn = btnRepeatTypeWeekly;
  	// 3: Monthly
  	if (iRepeatType == 3)
  		btn = btnRepeatTypeMonthly;
  	// 4: Yearly	
  	if (iRepeatType == 4)
  		btn = btnRepeatTypeYearly;
  	//set focus
  	if ((btn != null) && (!btn.isFocused()))
  		btn.requestFocus();
  	//update title info
  	SetActivityTitle("");
  	if (btn != null)
  		SetActivityTitle(btn.getText().toString());  	
  	//update controls state
  	UpdateRepeatViewsState(iRepeatType);
  }
  
  public void SetActivityTitle(String sSubTitle)
  {
  	String sTitle = utils.GetResStr(R.string.titleDefaultRepeat);
  	if (sSubTitle.length() > 0)
  		sTitle += ": " + sSubTitle;
  	setTitle(sTitle);
  }
  
  private void InitState()
  {  	
  	SetActivityTitle("");
  	
  	SetRepeatType(iRepeatType);

  	UpdateEveryValueView();
    UpdateEndOnDateView();    
  }

  private void UpdateEveryValueView()
  {
  	labRepeatEveryValue.setText(Integer.toString(iRepeatEvery));
  }
  
  private void UpdateRepeatViewsState(int iRepeatType)
  { 
  	//change visibility of repeat views
    if (iRepeatType == 0) //none
    {
    	llayAppointmentRepeatEvery.setVisibility(View.GONE);
    	llayAppointmentRepeatEndOnDate.setVisibility(View.GONE);
    	llayAppointmentRepeatEndOnDateLabel.setVisibility(View.GONE);
    } else {    	
    	//yearly
    	if (iRepeatType == 4)
    	{
      	llayAppointmentRepeatEvery.setVisibility(View.GONE);
    	} else {
      	llayAppointmentRepeatEvery.setVisibility(View.VISIBLE);    		
      	llayAppointmentRepeatEndOnDate.setVisibility(View.VISIBLE);
      	llayAppointmentRepeatEndOnDateLabel.setVisibility(View.VISIBLE);
    	}    	
    }    
  }
  
  private void UpdateEndOnDateView()
  {  	  	
  	if (calRepeatEnd.getTimeInMillis() != 0)
		{
	    btnEndOnDate.setText(utils.GetLongDate(calRepeatEnd));
		} else {
	    btnEndOnDate.setText(utils.GetResStr(R.string.labNoEndDate));
		}
  }  
  
  public static int getExtraRepeatType(Bundle extras)
  {
  	return extras.getInt(DATAKEY_TYPE);
  }

  public static int getExtraRepeatEvery(Bundle extras)
  {
  	return extras.getInt(DATAKEY_EVERY);
  }

  public static long getExtraRepeatEndOnDate(Bundle extras)
  {
  	return extras.getLong(DATAKEY_ENDONDATE);
  }  
  
  public static void OpenRepeatForEdit(CommonActivity parent, Bundle extras, int iRepeatType, int iEvery, long lEndOnDate)
  {
  	extras.clear();
  	extras.putBoolean("EditRepeat", true);
  	extras.putInt(DATAKEY_TYPE, iRepeatType);
  	extras.putInt(DATAKEY_EVERY, iEvery);
  	extras.putLong(DATAKEY_ENDONDATE, lEndOnDate);
  	parent.OpenActivity(SELECT_REPEAT_REQUEST, "android.intent.action.AnCal.ACTION_MODE_EDIT_APPOINTMENTREPEAT");
  }
  
  public static boolean GetActivityResult(int requestCode, int resultCode, Bundle extras)
  {
  	if ((requestCode == SELECT_REPEAT_REQUEST) && (resultCode == RESULT_OK))
  	{
  		if ((extras != null) && (extras.containsKey("EditRepeat")))
  		{  		  		
    		return true;
  		}
  	}
  	return false;
  }
  
  private void EditDone()
  {
		Bundle bundleResult = new Bundle();
		bundleResult.putBoolean("EditRepeat", true);
		bundleResult.putInt(DATAKEY_TYPE, iRepeatType);
		bundleResult.putInt(DATAKEY_EVERY, iRepeatEvery);
		bundleResult.putLong(DATAKEY_ENDONDATE, calRepeatEnd.getTimeInMillis());
		
		Intent intentData = new Intent("");
		intentData.putExtras(bundleResult);
		this.setResult(RESULT_OK, intentData);

		finish();
  }

  @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
  	super.onActivityResult(requestCode, resultCode, data);
  	
  	Bundle extras = CommonActivity.getIntentExtras(data);
  	if (extras != null)
  	{
	  	//check for date widget edit request code
	  	if (requestCode == DateWidget.SELECT_DATE_REQUEST)
	  	{
	    	final long lDate = DateWidget.GetSelectedDateOnActivityResult(requestCode, resultCode, extras, calRepeatEnd);
	    	if (lDate != -1)
	    	{
	    		UpdateEndOnDateView();
	    	}
	  	}
  	}
  }
  
}
