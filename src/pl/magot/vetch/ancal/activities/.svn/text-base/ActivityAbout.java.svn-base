
package pl.magot.vetch.ancal.activities;


import pl.magot.vetch.ancal.CommonActivity;
import pl.magot.vetch.ancal.R;
import pl.magot.vetch.ancal.Utils;
import android.os.Bundle;
import android.widget.TextView;


//New Note activity
public class ActivityAbout extends CommonActivity
{
	//views
	private TextView txtView = null;
	private TextView txtVersionLabel = null;
	
  //methods
  @Override
  public void onCreate(Bundle icicle)
  {
      super.onCreate(icicle);
      setContentView(R.layout.about);

      //initialize views
      InitViews();    	
  }
  
  @Override
  public void onStart()
  {
  	super.onStart();
  	    
    InitState();
  }
	
  private void InitViews()
  {
  	txtView = (TextView)findViewById(R.id.txtViewAbout);
  	txtVersionLabel = (TextView)findViewById(R.id.txtVersionLabel);
  }
  
  private void InitState()
  {
  	String sSubTitle = utils.GetResStr(R.string.titleDefaultAbout); 
		SetActivityTitle(sSubTitle);

		txtVersionLabel.setText(Utils.getAppVersionName(this));
  	
  	txtView.requestFocus();
  }

	@Override
	protected void restoreStateFromFreeze()
	{
		
	}
   
}
