
package pl.magot.vetch.ancal.dataview;


import java.util.*;

import pl.magot.vetch.ancal.Prefs;
import pl.magot.vetch.ancal.Utils;
import pl.magot.vetch.ancal.agenda.AgendaView;
import pl.magot.vetch.ancal.database.DataRowTask;
import pl.magot.vetch.ancal.database.Database;
import android.database.Cursor;


public class DataViewTask extends DataView
{
	//Comparator type
	public class RowsComparator implements Comparator<DataViewItem>
	{
		public int compare(DataViewItem item1, DataViewItem item2)
		{
			long iPriority1 = item1.lPriority;
			long iPriority2 = item2.lPriority;
			
			if (iPriority1 > iPriority2)
				return 1;
			if (iPriority1 < iPriority2)
				return -1;
			
			if (iPriority1 == iPriority2)
			{
				String s1 = item1.sSubject;
				String s2 = item2.sSubject;
				return s1.compareTo(s2);
			}
				
			return 0;
		}		
	};
	
	//fields
	private Calendar calDueDateCmp = Calendar.getInstance();	
	private RowsComparator fnCmp = null; 

	//methods
	public DataViewTask(Database db, Prefs prefs)
	{
		super(db, prefs);
		sTableName = Database.sTableNameTasks;
		fnCmp = new RowsComparator();
	}
	
	@Override
	public void AddItem(Cursor cr)
	{
		DataViewItem item = new DataViewItem();
		
		item.lID = cr.getLong(DataRowTask.fid.ID);
		item.sSubject = cr.getString(DataRowTask.fid.Subject);				
		item.bDone = (cr.getLong(DataRowTask.fid.Done) == 1); 							
		item.lPriority = cr.getLong(DataRowTask.fid.Priority);
		item.bAlarm = (cr.getLong(DataRowTask.fid.Alarm) == 1);
				
		if (!cr.isNull(DataRowTask.fid.DueDate))
			item.lDueDate = cr.getLong(DataRowTask.fid.DueDate);
		
		rows.add(item);
	}
	
	@Override
	public void FilterDataForView(DataViewItem item, final Calendar calStartDate, final int agendaViewType)
	{		
		//agenda view Today
		if (agendaViewType == AgendaView.viewMode.TODAY)
		{
			if (prefs.bShowAllTasks)
			{
				item.viewMode = agendaViewType;				
			} else {
				if (item.UseDueDate())
				{
					calDueDateCmp.setTimeInMillis(item.lDueDate);
					if (Utils.YearDaysEqual(calStartDate, calDueDateCmp))
						item.viewMode = agendaViewType;
				} else {				
					item.viewMode = agendaViewType;				
				}
			}
		}
		
		//view for alarm
		if (agendaViewType == AgendaView.viewMode.TODAY_ALARM)
		{
			if (!item.bDone)
			{
				if (item.UseDueDate())
				{
					calDueDateCmp.setTimeInMillis(item.lDueDate);
					if (Utils.YearDaysGreater(calStartDate, calDueDateCmp))
						item.viewMode = agendaViewType;
				} else {				
					item.viewMode = agendaViewType;				
				}
			}
		}
		
	}
		
	@Override
	protected void FilterDataPrepare(final Calendar calStartDate, final int agendaViewType)
	{
		
	}
	
	@Override
	public void SortView()
	{
		Collections.sort(rows, fnCmp);
	}

}
