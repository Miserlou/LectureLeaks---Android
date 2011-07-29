package pl.magot.vetch.ancal;

import pl.magot.vetch.ancal.reminder.AlarmService;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;

public class AnCalReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if ((context != null) && (intent != null))
		{
			if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
			{
				try
				{
					context.startService(new Intent(context, AlarmService.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
