
package pl.magot.vetch.ancal.activities;


import java.util.*;
import pl.magot.vetch.widgets.*;
import pl.magot.vetch.ancal.CommonActivity;
import pl.magot.vetch.ancal.R;
import pl.magot.vetch.ancal.database.DataRowTask;
import pl.magot.vetch.ancal.database.DataTable;
import pl.magot.vetch.widgets.DateWidget;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;


//New task activity
public class ActivityTask extends CommonActivity
{
	//private fields
	private Calendar dateDue = null;	
	private DataRowTask dataRow = null;
	private DataTable dataTable = null;	
  private int iTaskPriority = 2;
  
	//views
	private TouchEdit edSubject = null;
	private CheckBox chkDone = null;
	private CheckBox chkAlarm = null;
	private Button btnDueDate = null;
	private ImageButton btnDelete = null;
	private ImageButton btnSave = null;
	
	//priority views
	private Button btnTaskPriorityLow = null;
	private Button btnTaskPriorityDefault = null;
	private Button btnTaskPriorityHigh = null;	
		
  //methods
  @Override
  public void onCreate(Bundle icicle)
  {
    super.onCreate(icicle);      
    setContentView(R.layout.task);
    
    //initialize objects
    dataRow = new DataRowTask(userdb);
  	dataTable = new DataTable(dataRow);      

    //check startup mode and open data
  	if (GetStartMode() == StartMode.EDIT)
  		if (!OpenDataForEdit(dataTable))
  			finish();
        	
    //initialize views
    InitViews();    	
    InitState();
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
  	edSubject = (TouchEdit)findViewById(R.id.edTaskSubject);
  	edSubject.setOnOpenKeyboard(new TouchEdit.OnOpenKeyboard()
    {
			public void OnOpenKeyboardEvent()
			{
        KeyboardWidget.Open(ActivityTask.this, edSubject.getText().toString());						
			}        	        	
    });  	
  	
  	chkDone = (CheckBox)findViewById(R.id.chkTaskDone);   
  	
    chkAlarm = (CheckBox)findViewById(R.id.chkTaskAlarm);  	
  	btnDueDate = (Button)findViewById(R.id.btnTaskDueDate);

  	btnSave = (ImageButton)findViewById(R.id.btnTaskSave);
  	btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SaveData();
			}
  	});
  	
  	btnDelete = (ImageButton)findViewById(R.id.btnTaskDelete);
  	btnDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DeleteData();
			}		
  	});
  	
  	btnDueDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				DateWidget.Open(ActivityTask.this, true, dateDue, prefs.iFirstDayOfWeek);
				
			}
		});
  	
  	//priority
  	btnTaskPriorityLow = (Button)findViewById(R.id.btnTaskPriorityLow);
  	btnTaskPriorityLow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetPriority(3);
			}
  	});
  	btnTaskPriorityDefault = (Button)findViewById(R.id.btnTaskPriorityDefault);
  	btnTaskPriorityDefault.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetPriority(2);
			}
  	});
  	btnTaskPriorityHigh = (Button)findViewById(R.id.btnTaskPriorityHigh);
  	btnTaskPriorityHigh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SetPriority(1);
			}
  	});
  	
  }
  
  public void SetPriority(int iPriority)
  {
  	//update global value
  	this.iTaskPriority = iPriority;
  	//enable buttons
  	btnTaskPriorityHigh.setEnabled((iPriority != 1));
  	btnTaskPriorityDefault.setEnabled((iPriority != 2));
  	btnTaskPriorityLow.setEnabled((iPriority != 3));
  	Button btn = null;
  	if (iPriority == 1)
  		btn = btnTaskPriorityHigh;
  	// 1: Daily
  	if (iPriority == 2)
  		btn = btnTaskPriorityDefault;
  	// 2: Weekly
  	if (iPriority == 3)
  		btn = btnTaskPriorityLow;
  	//set focus
  	if ((btn != null) && (!btn.isFocused())) 
  		btn.requestFocus();  	  	
  }  
  
  private void InitState()
  {
  	//title
  	String sSubTitle = utils.GetResStr(R.string.titleDefaultAppointment); 

  	dateDue = Calendar.getInstance();

  	//INSERT MODE
  	if (GetStartMode() == StartMode.NEW)
  	{
  		dateDue.setTimeInMillis(0);
  		sSubTitle = utils.GetResStr(R.string.titleNewTask);
      btnDelete.setVisibility(View.INVISIBLE);
      chkDone.setChecked(false);      
      chkDone.setVisibility(View.GONE);
      SetPriority(2);
    	chkAlarm.setChecked(false);
  	}
  	
  	//EDIT MODE
  	if (GetStartMode() == StartMode.EDIT)
  	{  		
  		dateDue.setTimeInMillis(dataRow.GetDueDate().getTimeInMillis());      
  		sSubTitle = utils.GetResStr(R.string.titleEditTask);
  		edSubject.setText(dataRow.GetSubject());
      btnDelete.setVisibility(View.VISIBLE);      
      chkDone.setChecked(dataRow.GetDone());
      chkDone.setVisibility(View.VISIBLE);      
      SetPriority(dataRow.GetPriority());
    	chkAlarm.setChecked(dataRow.GetAlarm());
  	}

  	restoreStateFromFreezeIfRequired();
  	
  	SetActivityTitle(sSubTitle);
    UpdateDueDateView();

  	//set focus to subject
  	edSubject.requestFocus();
  	if (GetStartMode() == StartMode.EDIT)
  		edSubject.setSelection(edSubject.length());  	
  }
   
  private void UpdateDueDateView()
  {
  	if (dateDue.getTimeInMillis() != 0)
		{
	  	btnDueDate.setText(utils.GetLongDate(dateDue));
		} else {
			btnDueDate.setText(utils.GetResStr(R.string.labNoDate));
		}	
  }  

  public void SaveData()
  {
  	if (dateDue.getTimeInMillis() != 0)
  		if (DateBeforeNow(dateDue))
  			return;
  	
  	dataRow.SetSubject(edSubject.getText().toString());
  	dataRow.SetDone(chkDone.isChecked());
  	dataRow.SetPriority(this.iTaskPriority);
  	dataRow.SetAlarm(chkAlarm.isChecked());
  	
  	if (dateDue.getTimeInMillis() != 0)
  	{
    	dataRow.SetDueDate(dateDue);
  	} else {
    	dataRow.SetDueDate(null);
  	}
  	
  	if (SaveDataToTable(dataTable))
			CloseActivity(dataTable);
  }  
	
  public void DeleteData()
  {  	
		if (DeleteDataFromTable(dataTable))
			CloseActivity(dataTable);
  }
  
  @Override
  public void SaveDateValuesBeforeChange(Bundle data)
  {
  	super.SaveDateValuesBeforeChange(data);
  	if (GetStartMode() == StartMode.EDIT)
  	{
  		if (dataRow.UsingDueDate())
  			data.putLong("dateDue", dataRow.GetDueDate().getTimeInMillis());
  	}
  }  
  
  @Override
  public boolean DateValuesChanged(Bundle data)
  {
  	super.DateValuesChanged(data);
  	if (GetStartMode() == StartMode.EDIT)
  	{
  		if (dataRow.UsingDueDate())
  			if (dateDue.getTimeInMillis() != data.getLong("dateDue"))
  				return true;
  	}
  	return false;
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
	    	final long lDate = DateWidget.GetSelectedDateOnActivityResult(requestCode, resultCode, extras, dateDue);
	    	if (lDate != -1)
	    	{
	    		UpdateDueDateView();
	    	}
	  	}
	
	  	//get KeyboardWidget result
	  	if ((requestCode == KeyboardWidget.EDIT_TEXT_REQUEST) && (resultCode == RESULT_OK)) 
	  	{
				String sText = KeyboardWidget.GetTextOnActivityResult(requestCode, resultCode, extras);    			
				edSubject.setText(sText);
	  	}
	  	
  	}  	
  }

  @Override
  protected void onSaveInstanceState(Bundle outState)
  {
  	super.onSaveInstanceState(outState);
  	//save controls state
  	outState.putString("subject", edSubject.getText().toString());
  	outState.putBoolean("alarm", chkAlarm.isChecked());  	
  	outState.putInt("priority", this.iTaskPriority);
  	outState.putLong("dateDue", dateDue.getTimeInMillis());
  }  
  
	@Override
	protected void restoreStateFromFreeze()
	{
 		edSubject.setText(freeze.getString("subject"));  	
 		chkAlarm.setChecked(freeze.getBoolean("alarm"));
 		SetPriority(freeze.getInt("priority"));
 		dateDue.setTimeInMillis(freeze.getLong("dateDue"));
	}  
  
}
