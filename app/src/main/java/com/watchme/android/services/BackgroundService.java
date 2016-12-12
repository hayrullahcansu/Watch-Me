package com.watchme.android.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by hayru on 11/11/2016.
 */
public class BackgroundService extends Service {
  @Nullable
  @Override
  public IBinder onBind(Intent ıntent) {
	return null;
  }

  @Override
  public void onCreate() {
	super.onCreate ();
  }

  @Override
  public void onDestroy() {
	super.onDestroy ();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
	//işlemler
	new BackgroundWorker ( getApplicationContext () ).start ();
	setRepeatingCalculationClustering ();
	return START_STICKY;
  }

  private void setRepeatingCalculationClustering() {
	int hour = 0;
	int minute = 10;
	Log.e ( "HESAPLAMA-KURULDU", "00:10" );
	Calendar calendar = Calendar.getInstance ();
	calendar.setTimeInMillis ( System.currentTimeMillis () );
	calendar.set ( Calendar.HOUR_OF_DAY, hour );
	calendar.set ( Calendar.MINUTE, minute );
	AlarmManager am = (AlarmManager) getSystemService ( ALARM_SERVICE );
	Intent intent = new Intent ( getApplicationContext (), HBClusteringReceiver.class );
	PendingIntent pendingIntent = PendingIntent.getBroadcast ( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
	am.setRepeating ( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (), (12 * 60 * 60 * 1000), pendingIntent );
  }
}
