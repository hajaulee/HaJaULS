package com.hajau.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockScreenReveicer extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (!Main_Activity.active && Integer.parseInt(Setting.load(context)) < 4) {
			Intent i = new Intent(context, Main_Activity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			Log.i("LSR", "Start Activity by " + intent.getAction());
		} else
			Log.i("LSR", "Do nothing!");
	}

}
