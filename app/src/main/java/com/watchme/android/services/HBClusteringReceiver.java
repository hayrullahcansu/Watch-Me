package com.watchme.android.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.watchme.android.adapters.HBDatabaseAdapter;
import com.watchme.android.hbclustering.clustering.Cluster;
import com.watchme.android.hbclustering.clustering.HBClustering;
import com.watchme.android.hbclustering.clustering.IResultOfOrganizingItems;
import com.watchme.android.hbclustering.clustering.JuniorCluster;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by hayru on 12/4/2016.
 */
public class HBClusteringReceiver extends BroadcastReceiver implements IResultOfOrganizingItems {
  HBClustering hb = null;
  Context context;

  @Override
  public void onReceive(Context context, Intent ıntent) {
	this.context = context;
	getStatics ();
  }

  private void getStatics() {
	Log.e ( "HB-HESAPLAMA-CALISTI", "HESAPLANIYOR" );
	hb = new HBClustering ();
	HBDatabaseAdapter.getInstance ().setContextForDB ( this.context );
	HBDatabaseAdapter.getInstance ().OrganizeItems ( hb, this );
  }

  @Override
  public void onResultOfOrganizingItems(boolean isSuccess, String resultMessage, HBClustering hbClustering) {
	Log.e ( "HB-HESAPLAMA-CALISTI", "HESAPLANDI result " + isSuccess );
	if (isSuccess && resultMessage.equals ( "calculated" )) {
	  for (Cluster item : hbClustering.getClusterList ()
			) {
		for (JuniorCluster junior : item.getJuniorClusters ()
			  ) {

		  if (junior.getRatioAccumulation () > 0.333f) {
			int hour = junior.getAverageOfTime ().getHour ();
			int minute = junior.getAverageOfTime ().getMinutes ();
			Calendar calendar = Calendar.getInstance ();
			calendar.setTimeInMillis ( System.currentTimeMillis () );
			calendar.set ( Calendar.HOUR_OF_DAY, hour );
			calendar.set ( Calendar.MINUTE, minute );


			//if schedule time wass passed no set task to alarm!
			if ((calendar.getTimeInMillis () - System.currentTimeMillis ()) > 0) {
			  AlarmManager am = (AlarmManager) context.getSystemService ( context.ALARM_SERVICE );
			  Intent intent = new Intent ( context, NotifyReciever.class );
			  intent.putExtra ( "app_path", junior.getOwner ().getClusterName () );
			  intent.putExtra ( "time_text", hour + ":" + minute );
			  PendingIntent pendingIntent = PendingIntent.getBroadcast ( context, new Random ().nextInt ( 1000 ), intent, PendingIntent.FLAG_IMMUTABLE );
			  am.set ( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (), pendingIntent );
			  Log.e ( "BİLDİRİM-KURULDU", String.format ( "BILDIRIM app_path%s hour:%d minute:%d scheduled.\n", junior.getOwner ().getClusterName (), hour, minute ) );
			}
		  }
		}
	  }
	}
  }
}
