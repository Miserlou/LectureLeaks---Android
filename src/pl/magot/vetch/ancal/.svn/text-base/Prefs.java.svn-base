
package pl.magot.vetch.ancal;


import java.util.Calendar;
import android.content.*;


public class Prefs
{
	//private fields
	private final String sPrefsName = "AnCalPrefs";
	private SharedPreferences prefs = null;

	//prefs data
  private int iMinutesOffset; //offset start of new appointment
  public int iMinutesDuration; //duration of appointment
  public boolean b24HourMode; //time mode
  public int iFirstDayOfWeek; //first day of week
  public boolean bShowAllTasks; //show all tasks in today, or hide undued
  public int iSnoozeCount; //count of snooze times to change alarm to cleared
  public int iSnoozeMinutesOverdue; //snooze minutes alarm overdue
	
  public Prefs(Context ctx)
  {
  	prefs = ctx.getSharedPreferences(sPrefsName, Context.MODE_WORLD_WRITEABLE);
  	Load();
  }
  
	public boolean Save()
	{
		try
		{
			SharedPreferences.Editor ed = prefs.edit();
			
	    ed.putInt("iMinutesOffset", iMinutesOffset);
	    ed.putInt("iMinutesDuration", iMinutesDuration);       
	    ed.putBoolean("b24HourMode", b24HourMode);          
	    ed.putInt("iFirstDayOfWeek", iFirstDayOfWeek);	    
	    ed.putBoolean("bShowAllTasks", bShowAllTasks);
	    ed.putInt("iSnoozeCount", iSnoozeCount);	    
	    ed.putInt("iSnoozeMinutesOverdue", iSnoozeMinutesOverdue);	    
	
	    //return ed.commit();
	    
	    ed.commit();
	    
	    return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void Load()
	{
  	iMinutesOffset = prefs.getInt("iMinutesOffset", 30);
  	iMinutesDuration = prefs.getInt("iMinutesDuration", 15);
  	b24HourMode = prefs.getBoolean("b24HourMode", false);
  	iFirstDayOfWeek = prefs.getInt("iFirstDayOfWeek", Calendar.MONDAY);
  	bShowAllTasks = prefs.getBoolean("bShowAllTasks", false);
  	iSnoozeCount = prefs.getInt("iSnoozeCount", 5);
  	iSnoozeMinutesOverdue = prefs.getInt("iSnoozeMinutesOverdue", 5);  	
	}
}
