
package pl.magot.vetch.ancal.dataview;


import java.util.*;

import pl.magot.vetch.ancal.Prefs;
import pl.magot.vetch.ancal.RepeatData;
import pl.magot.vetch.ancal.RepeatDataValue;
import pl.magot.vetch.ancal.Utils;
import pl.magot.vetch.ancal.agenda.AgendaView;
import pl.magot.vetch.ancal.database.DataRowAppointment;
import pl.magot.vetch.ancal.database.Database;
import android.database.Cursor;


public class DataViewAppointment extends DataView
{
	//fields
	private Calendar calUtilDate = Calendar.getInstance();
	
	//fields
	private RepeatData repeat = new RepeatData();
	
	//fields
	protected Calendar calStartDateForCache = Calendar.getInstance();	
	protected RepeatDataValue[] vecDateRangeCache = new RepeatDataValue[42];
	
	//fields
	private Calendar date1cmp = Calendar.getInstance();
	private Calendar date2cmp = Calendar.getInstance();
	
	//fields
	private Calendar calItemStartDate = Calendar.getInstance();
	
	//Comparator type
	public class RowsComparator implements Comparator<DataViewItem>
	{
		public int compare(DataViewItem item1, DataViewItem item2)
		{
			date1cmp.setTimeInMillis(item1.GetStartDateAsLong());
			date2cmp.setTimeInMillis(item2.GetStartDateAsLong());

			boolean bAllDay1 = (item1.bAllDay);
			boolean bAllDay2 = (item2.bAllDay);
			
			if (bAllDay1 && bAllDay2) 
			{
				String s1 = item1.sSubject;
				String s2 = item2.sSubject;				
				return s1.compareTo(s2);				
			} else {
				if (bAllDay1)
					return -1;
				if (bAllDay2)
					return 1;
				
				if (Utils.GetTimeAsSeconds(date1cmp) > Utils.GetTimeAsSeconds(date2cmp))
					return 1;
				if (Utils.GetTimeAsSeconds(date1cmp) < Utils.GetTimeAsSeconds(date2cmp))
					return -1;

				if (Utils.GetTimeAsSeconds(date1cmp) == Utils.GetTimeAsSeconds(date2cmp))
				{
					String s1 = item1.sSubject;
					String s2 = item2.sSubject;				
					return s1.compareTo(s2);
				}				
			}
			return 0;
		}		
	};
	
	//fields
	private RowsComparator fnCmp = null;

	//methods
	public DataViewAppointment(Database db, Prefs prefs)
	{
		super(db, prefs);
		sTableName = Database.sTableNameAppointments;
		fnCmp = new RowsComparator();
		
		//initialize date value cache
		for (int iDay = 0; iDay < 42; iDay++)
		{
			vecDateRangeCache[iDay] = new RepeatDataValue();
		}
	}

	@Override
	public void AddItem(Cursor cr)
	{
		DataViewItem item = new DataViewItem();
		
		item.lID = cr.getLong(DataRowAppointment.fid.ID);
		item.sSubject = cr.getString(DataRowAppointment.fid.Subject);			
		item.SetStartDate(calUtilDate, cr.getLong(DataRowAppointment.fid.StartDate));		
		item.iDurationInMinutes = cr.getInt(DataRowAppointment.fid.DurationInMinutes);				
		item.bAllDay = (cr.getLong(DataRowAppointment.fid.AllDay) == 1);		
		item.bAlarm = (cr.getLong(DataRowAppointment.fid.Alarm) == 1);
		
		item.iRepeatType = cr.getInt(DataRowAppointment.fid.RepeatType);
		
		if (!cr.isNull(DataRowAppointment.fid.RepeatEvery))
			item.iRepeatEvery = cr.getInt(DataRowAppointment.fid.RepeatEvery);
		
		if (!cr.isNull(DataRowAppointment.fid.RepeatEndOnDate))
			item.lRepeatEndOnDate = cr.getLong(DataRowAppointment.fid.RepeatEndOnDate);
		
		rows.add(item);
	}
	
	@Override
	public void FilterDataForView(DataViewItem item, final Calendar calStartDate, final int agendaViewType)
	{
		//set repeat comparer data
		repeat.SetRepeatTypeAsInt(item.iRepeatType);
		repeat.SetEndOnDate(null);
		repeat.SetEvery(1);
		
		//if repeat set
		if (item.IsRepeat())
		{
			repeat.SetEvery(item.iRepeatEvery);
			if (item.UseRepeatEndOnDate())
				repeat.SetEndOnDate(item.lRepeatEndOnDate);								
		}
				
		//get appt item start date
		calItemStartDate.setTimeInMillis(item.GetStartDateAsLong());
		calItemStartDate.setFirstDayOfWeek(prefs.iFirstDayOfWeek);
		repeat.SetStartDate(calItemStartDate);
				
		//filter item for date range
		if (agendaViewType != AgendaView.viewMode.NONE)
		{
			item.Clear();
			
			final int iDaysCount = getDaysRangeForView(agendaViewType);
			
			if (iDaysCount == 0)
			{
				
				if (repeat.IsDateEqual(calStartDate))
				{
					item.SetVisibleDay(0);
					item.viewMode = agendaViewType;
				}
				
			} else {
				
				RepeatDataValue dvDate = null;
				
				for (int iDay = 0; iDay < iDaysCount; iDay++)
				{				
					dvDate = vecDateRangeCache[iDay]; 
					
					if (repeat.IsDateEqual(dvDate))
					{
						item.SetVisibleDay(iDay);
						item.viewMode = agendaViewType;
					}
					
				}
				
			}
		}		
		
	}
	
	@Override
	protected void FilterDataPrepare(final Calendar calStartDate, final int agendaViewType)
	{		
		final int iDaysCount = getDaysRangeForView(agendaViewType);
		
		int iDayValue = 1;
		//cache date values backward for alarm service
		if (agendaViewType == AgendaView.viewMode.TODAY_ALARM)
			iDayValue = -1;
		
		calStartDateForCache.setTimeInMillis(calStartDate.getTimeInMillis());
		calStartDateForCache.setFirstDayOfWeek(prefs.iFirstDayOfWeek);

		for (int iDay = 0; iDay < (iDaysCount); iDay++)
		{
			vecDateRangeCache[iDay].getFromCalendar(calStartDateForCache);					
			calStartDateForCache.add(Calendar.DAY_OF_YEAR, iDayValue);
		}
		
	}
	
	@Override
	public void SortView()
	{
		Collections.sort(rows, fnCmp);  			
	}
		
}
