package pl.magot.vetch.ancal.agenda;


import java.util.Calendar;
import pl.magot.vetch.ancal.AnCal;
import pl.magot.vetch.ancal.CommonActivity;
import pl.magot.vetch.ancal.R;
import pl.magot.vetch.ancal.dataview.DataView;
import pl.magot.vetch.ancal.views.ViewTodayItemAppointment;
import pl.magot.vetch.ancal.views.ViewTodayItemTask;
import pl.magot.vetch.ancal.views.ViewTodayItemNote;
import pl.magot.vetch.ancal.views.ViewTodayItem;
import pl.magot.vetch.ancal.views.ViewTodayItemHeader;
import android.os.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;


public abstract class AgendaView
{
	//types
	public class viewMode
	{
		public final static int NONE = 0;
		public final static int TODAY = 1;
		public final static int DAY = 2;
		public final static int WEEK = 3;
		public final static int MONTH = 4;
		public final static int TODAY_ALARM = 5;
	};
	
	//types
	public interface OnViewItemClick
	{
		public void OnClick(View v, Bundle extras);
	}
	
	//appointment click listener
	public ViewTodayItemAppointment.OnItemClick onApptItemClick = new ViewTodayItemAppointment.OnItemClick()
	{
		public void OnClick(ViewTodayItem item) {
			doSubItemClick(item, ViewTodayItemHeader.ViewType.Appointments, item.GetRowId());
		}		
	};
	
	//task click listener
	public ViewTodayItemTask.OnItemClick onTaskItemClick = new ViewTodayItemTask.OnItemClick()
	{
		public void OnClick(ViewTodayItem item) {
			doSubItemClick(item, ViewTodayItemHeader.ViewType.Tasks, item.GetRowId());
		}
	};
	
	//note click listener
	public ViewTodayItemNote.OnItemClick onNoteItemClick = new ViewTodayItemNote.OnItemClick()
	{
		public void OnClick(ViewTodayItem item) {
			doSubItemClick(item, ViewTodayItemHeader.ViewType.Notes, item.GetRowId());
		}		
	};	

	
	//fields
	protected AnCal main = null;
	protected LinearLayout llayParent = null;
	protected LinearLayout.LayoutParams lpParent = null;
	protected LinearLayout.LayoutParams lparams = null;
	
	//fields	
	protected LinearLayout llayParentAppt = null;
	protected LinearLayout llayParentTask = null;
	protected LinearLayout llayParentNote = null;

	//fields
	protected static OnViewItemClick itemAgendaViewClick = null;

	//fields
	protected String sTextNone = "";
	private boolean bLastTimeFormat = false; 
	
	//fields
	private Calendar calDateToday = Calendar.getInstance();
	private Calendar calViewStartDate = Calendar.getInstance();
	protected Calendar calCurrMonth = Calendar.getInstance();			

	//fields
	private int iMonthViewCurrentMonth = -1;
	private int iMonthViewCurrentYear = -1;
	
	//methods
	AgendaView(AnCal main)
	{
		this.main = main;
		
		//layout params
  	lpParent = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);  	
		lparams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		//create parent layout
		llayParent = new LinearLayout(main);
  	llayParent.setPadding(0, 0, 0, 0);
  	llayParent.setOrientation(LinearLayout.VERTICAL);
  	llayParent.setLayoutParams(lpParent);
  	
		//create parent appointment layout
		llayParentAppt = new LinearLayout(main);
		llayParentAppt.setPadding(0, 0, 0, 2);
		llayParentAppt.setOrientation(LinearLayout.VERTICAL);
		llayParentAppt.setLayoutParams(lparams);
		//create parent task layout
		llayParentTask = new LinearLayout(main);
		llayParentTask.setPadding(0, 0, 0, 2);
		llayParentTask.setOrientation(LinearLayout.VERTICAL);
		llayParentTask.setLayoutParams(lparams);
		//create parent note  layout
		llayParentNote = new LinearLayout(main);
		llayParentNote.setPadding(0, 0, 0, 2);
		llayParentNote.setOrientation(LinearLayout.VERTICAL);
		llayParentNote.setLayoutParams(lparams);
		
		//init strings
		sTextNone = main.utils.GetResStr(R.string.labTodayItemNone);
		
		bLastTimeFormat = main.prefs.b24HourMode;
	}
	
	public static void SetItemClick(OnViewItemClick itemClick)	
	{
		itemAgendaViewClick = itemClick;		
	}
	
	public LinearLayout GetParentLayout()
	{	
		return llayParent;
	}	
	
	public abstract void Rebuild();
	public abstract int GetViewType();
	public abstract int GetViewIndex();
	
	public void doHeaderItemClick(View v, ViewTodayItemHeader.ViewType type)
	{
		if (itemAgendaViewClick != null)
		{
			Bundle extras = new Bundle();
			extras.putString("type", type.toString());
			extras.putLong(CommonActivity.bundleRowId, -1L);
			itemAgendaViewClick.OnClick(v, extras);
		}		
	}

	public void doSubItemClick(View v, ViewTodayItemHeader.ViewType type, long lRowId)
	{
		if (itemAgendaViewClick != null)
		{
			Bundle extras = new Bundle();
			extras.putString("type", type.toString());  				
			extras.putLong(CommonActivity.bundleRowId, lRowId);
			itemAgendaViewClick.OnClick(v, extras);
		}
	}

	public void doHourOfDayClick(View v, ViewTodayItemHeader.ViewType type, int iHourOfDay, int iMinutes)
	{
		if (itemAgendaViewClick != null)
		{
			Bundle extras = new Bundle();
			extras.putString("type", type.toString());  				
			extras.putLong(CommonActivity.bundleRowId, -1L);			
			extras.putInt(CommonActivity.bundleHourOfDay, iHourOfDay);
			extras.putInt(CommonActivity.bundleMinutes, iMinutes);			
			itemAgendaViewClick.OnClick(v, extras);
		}
	}
		
	public abstract void RebuildViewAppointments(DataView dataView);
	public abstract void RebuildViewTasks(DataView dataView);
	public abstract void RebuildViewNotes(DataView dataView);
	
	public abstract void UpdateTimeFormat();

	public void SetViewStartDate(Calendar date)
	{
		if (date == null)
		{
			calViewStartDate.setTimeInMillis(System.currentTimeMillis());
		} else {
			calViewStartDate.setTimeInMillis(date.getTimeInMillis());
		}
		
		calViewStartDate.setFirstDayOfWeek(main.prefs.iFirstDayOfWeek);
		
		if (GetViewType() == viewMode.WEEK)
			UpdateStartDateForWeek();
		if (GetViewType() == viewMode.MONTH)
			UpdateStartDateForMonth();
	}

	private void UpdateStartDateForWeek()
	{
		int iDay = 0;		
		int iStartDay = main.prefs.iFirstDayOfWeek;			
		if (iStartDay == Calendar.MONDAY)
		{
			iDay = calViewStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;			
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY)
		{
			iDay = calViewStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}		
		calViewStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	private void UpdateStartDateForMonth()
	{		
		iMonthViewCurrentMonth = calViewStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calViewStartDate.get(Calendar.YEAR);
		calViewStartDate.set(Calendar.DAY_OF_MONTH, 1);		
		UpdateStartDateForWeek();
	}
	
	public Calendar GetViewStartDate()
	{
		return calViewStartDate;
	}

	public void SetPrevViewItem()
	{
		if (GetViewType() == viewMode.DAY)
			calViewStartDate.add(Calendar.DAY_OF_YEAR, -1);
		if (GetViewType() == viewMode.WEEK)
			calViewStartDate.add(Calendar.WEEK_OF_YEAR, -1);
		
		if (GetViewType() == viewMode.MONTH)
		{
			iMonthViewCurrentMonth--;
			if (iMonthViewCurrentMonth == -1)
			{
				iMonthViewCurrentMonth = 11;
				iMonthViewCurrentYear--;
			}			
			calViewStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calViewStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calViewStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
							
			UpdateStartDateForMonth();
		}
	}
	
	public void SetTodayViewItem()
	{
		SetViewStartDate(null);
	}
	
	public void SetNextViewItem()
	{
		if (GetViewType() == viewMode.DAY)
			calViewStartDate.add(Calendar.DAY_OF_YEAR, 1);
		if (GetViewType() == viewMode.WEEK)
			calViewStartDate.add(Calendar.WEEK_OF_YEAR, 1);
		
		if (GetViewType() == viewMode.MONTH)
		{
			iMonthViewCurrentMonth++;
			if (iMonthViewCurrentMonth == 12)
			{
				iMonthViewCurrentMonth = 0;
				iMonthViewCurrentYear++;
			}			
			calViewStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calViewStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calViewStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
			
			UpdateStartDateForMonth();
		}
	}
	
	public int GetCurrentSelectedMonth()
	{
		return iMonthViewCurrentMonth;
	}

	public int GetCurrentSelectedYear()
	{
		return iMonthViewCurrentYear;
	}

	public Calendar GetCurrentSelectedMonthAsCalendar()
	{
		calCurrMonth.set(Calendar.DAY_OF_MONTH, 1);
		calCurrMonth.set(Calendar.MONTH, iMonthViewCurrentMonth);
		calCurrMonth.set(Calendar.YEAR, iMonthViewCurrentYear);
		return calCurrMonth;
	}
	
	public boolean TimeFormatChanged()	
	{
		if (bLastTimeFormat == main.prefs.b24HourMode)
			return false;
		bLastTimeFormat = main.prefs.b24HourMode;
		return true;				
	}
	
	protected boolean IsViewToday()
	{
		calDateToday.setTimeInMillis(System.currentTimeMillis());
		if (calDateToday.get(Calendar.YEAR) == calViewStartDate.get(Calendar.YEAR))
			if (calDateToday.get(Calendar.MONTH) == calViewStartDate.get(Calendar.MONTH))
				if (calDateToday.get(Calendar.DAY_OF_MONTH) == calViewStartDate.get(Calendar.DAY_OF_MONTH))
					return true;
		return false;
	}
	
	protected int getTodayCurrentHour()
	{
		calDateToday.setTimeInMillis(System.currentTimeMillis());
		return calDateToday.get(Calendar.HOUR_OF_DAY);
	}
	
}
