package com.hajau.lockscreen;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service {

	@Override
	public void onStart(Intent intent, int startId) {

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		// filter.addAction(Intent.ACTION_SCREEN_OFF);
		LockScreenReveicer mReceiver = new LockScreenReveicer();
		registerReceiver(mReceiver, filter);
		Log.i("LSS", "RegisterReceiver Successful!");

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
