package com.watchme.android.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.watchme.android.R;


/**
 * Created by hayru on 12/3/2016.
 */
public class NotifyReciever extends BroadcastReceiver {
  String app_path = "";
  String timeText = "";

  @Override
  public void onReceive(Context context, Intent intent) {
	app_path = intent.getExtras ().getString ( "app_path" );
	timeText = intent.getExtras ().getString ( "time_text" );
	ShowNotify ( app_path, context );
  }

  private void ShowNotify(String _path, Context _context) {
	Log.e ( "BİLDİRİM", "PATH : " + _path + "time:" + timeText );
	String app_name = "";
	PackageManager pm = _context.getPackageManager ();
	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder ( _context );
	mBuilder.setSmallIcon ( R.mipmap.ic_launcher );
	mBuilder.setContentTitle ( "Merhaba,Hatırlatıcımız var !" );
	mBuilder.setContentText ( _path + " Uygulamasını bu saatlerde çok kullandığınız tespit ettik. Girmek ister misin?" );
	mBuilder.setAutoCancel ( true );
	try {
	  Drawable drawable = pm.getApplicationIcon ( pm.getApplicationInfo ( _path, PackageManager.GET_META_DATA ) );
	  app_name = (String) pm.getApplicationLabel ( pm.getApplicationInfo ( _path, PackageManager.GET_META_DATA ) );
	  if (drawable != null) {
		mBuilder.setLargeIcon ( ((BitmapDrawable) drawable).getBitmap () );
	  }
	  if (app_name != null) {
		mBuilder.setContentText ( app_name + " Uygulamasını bu saatlerde çok kullandığınız tespit ettik. Girmek ister misin?" );
	  }
	  Intent intent = pm.getLaunchIntentForPackage ( _path );
	  TaskStackBuilder tsb = null;
	  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
		tsb = TaskStackBuilder.create ( _context );
		tsb.addNextIntent ( intent );
		PendingIntent pint = tsb.getPendingIntent ( 0, PendingIntent.FLAG_UPDATE_CURRENT );
		mBuilder.setContentIntent ( pint );
	  }
	} catch (Exception e) {
	  Log.e ( "BILDIRIM-HATA", e.toString () );
	}
	NotificationManager mNotificationManager = (NotificationManager) _context.getSystemService ( Context.NOTIFICATION_SERVICE );
	// notificationID allows you to update the notification later on.
	mNotificationManager.notify ( 4, mBuilder.build () );

  }
}
