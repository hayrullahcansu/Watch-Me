package com.watchme.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.watchme.android.services.BackgroundService;

import java.io.IOException;

/**
 * Created by hayru on 12/7/2016.
 */
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
  private Thread.UncaughtExceptionHandler defaultUEH;
  Activity activity;

  public DefaultExceptionHandler(Activity activity) {
	this.activity = activity;
  }

  @Override
  public void uncaughtException(Thread thread, Throwable throwable) {
	Log.e ("HATA","UYGULAMA OLAGAN DISI SONLANDIRILDI");
	Intent intent;
	if (activity != null) intent = new Intent ( activity, BackgroundService.class );
	else
	  intent = new Intent ( WatchMeApplication.getInstace ().getApplicationContext (), BackgroundService.class );
	intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP
		  | Intent.FLAG_ACTIVITY_CLEAR_TASK
		  | Intent.FLAG_ACTIVITY_NEW_TASK );
	PendingIntent pendingIntent = PendingIntent.getActivity ( WatchMeApplication.getInstace ().getBaseContext (), 0, intent, intent.getFlags () );
	AlarmManager alarmManager = (AlarmManager) WatchMeApplication.getInstace ().getBaseContext ().getSystemService ( Context.ALARM_SERVICE );
	alarmManager.set ( AlarmManager.RTC, System.currentTimeMillis () + 2000, pendingIntent );
	if (activity != null) activity.finish ();
	System.exit ( 2 );
  }
}
