
package pl.magot.vetch.ancal.agenda;


import java.util.*;
import pl.magot.vetch.ancal.AnCal;
import pl.magot.vetch.ancal.R;
import pl.magot.vetch.ancal.dataview.DataView;
import pl.magot.vetch.ancal.dataview.DataViewItem;
import pl.magot.vetch.ancal.views.ViewTodayItemAppointment;
import pl.magot.vetch.ancal.views.ViewTodayItemHeader;
import pl.magot.vetch.ancal.views.ViewTodayItemNote;
import pl.magot.vetch.ancal.views.ViewTodayItemTask;
import android.view.View;
import android.widget.LinearLayout;
import android.graphics.*;


public class AgendaViewToday extends AgendaView
{	
	//fields
	private Vector<ViewTodayItemHeader> tdhitems = new Vector<ViewTodayItemHeader>();
	
	//fields
	private final int iTopPadding = 2;
	private final int iBottomPadding = 2;
	private final int iHeadAppts = 0;
	private final int iHeadTasks = 1;
	private final int iHeadNotes = 2;
	
	//fields
	private int iSpaceWidthTime = 0;
	private int iSpaceWidthMinutes = 0; 
	private int iSpaceWidthUSTimeMark = 0;
	
	//fields
	private Paint mpt = new Paint();
	
	//methods
	public AgendaViewToday(AnCal main)
	{
		super(main);
		iSpaceWidthTime = ViewTodayItemAppointment.GetSpaceWidthTime(mpt);
		iSpaceWidthMinutes = ViewTodayItemAppointment.GetSpaceWidthMinutes(mpt);
		iSpaceWidthUSTimeMark = ViewTodayItemAppointment.GetSpaceWidthUSTimeMark(mpt);
	}

	@Override
	public int GetViewType()
	{
		return viewMode.TODAY;
	}
	
	@Override
	public int GetViewIndex()
	{
		return 0;
	}
	
	@Override
	public void Rebuild()
	{
		llayParent.removeAllViews();

		ViewTodayItemHeader tdhi = null;
		
		//create today item headers
		for (int i = 0; i < 3; i++)
			tdhitems.add(new ViewTodayItemHeader(main));
		
		//init appointments
		tdhi = InitHeaderItem(iHeadAppts, ViewTodayItemHeader.ViewType.Appointments, R.string.labTodayItemAppointments);		
  	llayParentAppt.addView(tdhi, lparams);

		//init tasks
		tdhi = InitHeaderItem(iHeadTasks, ViewTodayItemHeader.ViewType.Tasks, R.string.labTodayItemTasks);
  	llayParentTask.addView(tdhi, lparams);

		//init notes
		tdhi = InitHeaderItem(iHeadNotes, ViewTodayItemHeader.ViewType.Notes, R.string.labTodayItemNotes);
  	llayParentNote.addView(tdhi, lparams);
  	
  	llayParent.addView(llayParentAppt, lparams);
  	llayParent.addView(llayParentTask, lparams);
  	llayParent.addView(llayParentNote, lparams);
	}

	public ViewTodayItemHeader InitHeaderItem(int index, ViewTodayItemHeader.ViewType type, int iResStrId)
	{
		ViewTodayItemHeader tdhi = null;
		//init appointments
		tdhi = tdhitems.get(index);
		tdhi.SetType(type);
		tdhi.SetText(main.utils.GetResStr(iResStrId));
		tdhi.SetInfoText(sTextNone);
		tdhi.setPadding(0, iTopPadding, 0, iBottomPadding);
		//set event
		tdhi.SetItemClick(new ViewTodayItemHeader.OnHeaderItemClick() {
			public void OnClick(View v, ViewTodayItemHeader.ViewType type) {
				doHeaderItemClick(v, type);
			}
		});
		return tdhi;
	}
	
	public void RemoveChildViewsFromHeader(LinearLayout llay)
	{
		while (llay.getChildCount() > 1)
		{
			View v = llay.getChildAt(1);
			if (v == null)
				break;
			if (v.getClass() != ViewTodayItemHeader.class)
				llay.removeViewInLayout(v);
		}
		llayParent.invalidate();
	}

	public void UpdateInfoText(ViewTodayItemHeader tdhi, int iRowsCount)
	{
		if (iRowsCount == 0)
		{
			tdhi.SetInfoText(sTextNone);			
		} else {
			tdhi.SetInfoText(Integer.toString(iRowsCount));
		}
	}
	
	@Override
	public void RebuildViewAppointments(DataView dataView)
	{
		RemoveChildViewsFromHeader(llayParentAppt);
		
		for (int i = 0; i < dataView.GetRowsCountTotal(); i++)
		{
			DataViewItem row = dataView.GetRow(i, this.GetViewType());
			if (row != null)
			{
				ViewTodayItemAppointment item = new ViewTodayItemAppointment(main);
				item.SetRowId(row.lID);
				item.SetItemTime(row.GetStartHour(), row.GetStartMinute(), false, main.prefs.b24HourMode, iSpaceWidthTime, iSpaceWidthMinutes, iSpaceWidthUSTimeMark);
				item.SetItemData(row.sSubject, row.bAlarm, row.IsRepeat());
				item.SetItemClick(onApptItemClick);
				llayParentAppt.addView(item, lparams);
			}
		}
		UpdateInfoText(tdhitems.get(iHeadAppts), dataView.GetRowsCountForView(this.GetViewType()));
	}

	@Override
	public void RebuildViewTasks(DataView dataView)
	{
		RemoveChildViewsFromHeader(llayParentTask);
		
		for (int i = 0; i < dataView.GetRowsCountTotal(); i++)
		{
			DataViewItem row = dataView.GetRow(i, this.GetViewType());
			if (row != null)
			{
				ViewTodayItemTask item = new ViewTodayItemTask(main);
				item.SetRowId(row.lID);
				item.SetItemData(row.bDone, row.sSubject, row.bAlarm);
				item.SetItemClick(onTaskItemClick);
				llayParentTask.addView(item, lparams);
			}
		}			
		UpdateInfoText(tdhitems.get(iHeadTasks), dataView.GetRowsCountForView(this.GetViewType()));
	}

	@Override
	public void RebuildViewNotes(DataView dataView)
	{
		RemoveChildViewsFromHeader(llayParentNote);
		
		for (int i = 0; i < dataView.GetRowsCountTotal(); i++)
		{
			DataViewItem row = dataView.GetRow(i, this.GetViewType());
			if (row != null)
			{
				ViewTodayItemNote item = new ViewTodayItemNote(main);
				item.SetRowId(row.lID);
				item.SetItemData(row.sSubject);
				item.SetItemClick(onNoteItemClick);
				llayParentNote.addView(item, lparams);
			}
		}			
		UpdateInfoText(tdhitems.get(iHeadNotes), dataView.GetRowsCountForView(this.GetViewType()));
	}

	@Override
	public void UpdateTimeFormat()
	{
	}
	
}
