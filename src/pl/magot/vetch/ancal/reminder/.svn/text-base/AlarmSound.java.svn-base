package pl.magot.vetch.ancal.reminder;

import android.content.Context;
import android.media.MediaPlayer;

public class AlarmSound
{
	// fields
	private Context context = null;
	MediaPlayer mMediaPlayer = null;

	// methods
	public AlarmSound(Context context)
	{
		this.context = context;
	}

	public void play()
	{
		mMediaPlayer = MediaPlayer.create(context, pl.magot.vetch.ancal.R.raw.ancalalarm);
		mMediaPlayer.start();
	}
	
	public void finalize()
	{
		mMediaPlayer.release();
	}

}
