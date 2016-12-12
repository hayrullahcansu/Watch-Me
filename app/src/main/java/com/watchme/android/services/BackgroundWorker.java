package com.watchme.android.services;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.watchme.android.adapters.HBDatabaseAdapter;
import com.watchme.android.hbclustering.others.HBTime;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by hayru on 11/11/2016.
 */
public class BackgroundWorker extends Thread {
  ActivityManager am = null;
  Context context = null;
  static String oldActivity = "";
  static String topActivity = "";

  public BackgroundWorker(Context _context) {
	context = _context;
	am = (ActivityManager) context.getSystemService ( Context.ACTIVITY_SERVICE );
  }

  @Override
  public void run() {
	Looper.prepare ();
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
	  AppOpsManager appOps = (AppOpsManager) context
			.getSystemService ( Context.APP_OPS_SERVICE );
	  int mode = appOps.checkOpNoThrow ( "android:get_usage_stats",
			android.os.Process.myUid (), context.getPackageName () );
	  boolean granted = mode == AppOpsManager.MODE_ALLOWED;
	  if (granted) {
		while (true) {
		  try {
			Thread.sleep ( 2000 );
		  } catch (InterruptedException e) {
			e.printStackTrace ();
		  }
		  UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService ( "usagestats" );
		  long time = System.currentTimeMillis ();
		  // We get usage stats for the last 10 seconds
		  List<UsageStats> stats = mUsageStatsManager.queryUsageStats ( UsageStatsManager.INTERVAL_DAILY, time - 1000 * 2, time );
		  // Sort the stats by the last time used
		  if (stats != null) {
			SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats> ();
			for (UsageStats usageStats : stats) {
			  mySortedMap.put ( usageStats.getLastTimeUsed (), usageStats );
			}
			if (!mySortedMap.isEmpty ()) {
			  String topActivity = mySortedMap.get ( mySortedMap.lastKey () ).getPackageName ();
			  Calendar c = Calendar.getInstance ();
			  try {
				PackageManager pm = context.getPackageManager ();
				ApplicationInfo ai = pm.getApplicationInfo ( topActivity, 0 );
				if (isUserApp ( ai ) && !topActivity.equals ( oldActivity ) && !topActivity.equals ( "com.watchme.android" )) {
				  oldActivity = topActivity;
				  //TODO Aç burayı

				  HBDatabaseAdapter.getInstance ().setContextForDB ( context );
				  HBDatabaseAdapter.getInstance ().InsertInto ( topActivity, new HBTime ( c.get ( Calendar.HOUR_OF_DAY ), c.get ( Calendar.MINUTE ) ) );
				  Log.e ( "Eklendi", "Eklendi lolipop->" + topActivity );
				}
			  } catch (PackageManager.NameNotFoundException e) {
				Log.e ( "HATA", e.toString () );
			  }
			}
		  }
		}
	  } else {
		Intent intent = new Intent ( Settings.ACTION_USAGE_ACCESS_SETTINGS );
		intent.setFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
		context.startActivity ( intent );
	  }

	} else {
	  while (true) {
		try {
		  Thread.sleep ( 2000 );
		} catch (InterruptedException e) {
		  e.printStackTrace ();
		}
		ActivityManager m = (ActivityManager) context.getSystemService ( Context.ACTIVITY_SERVICE );
		List<ActivityManager.RunningTaskInfo> runningTaskInfoList = m.getRunningTasks ( 1 );
		Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator ();
		while (itr.hasNext ()) {
		  ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) itr.next ();
		  int id = runningTaskInfo.id;
		  CharSequence desc = runningTaskInfo.description;
		  topActivity = runningTaskInfo.topActivity.getPackageName ();
		  int numOfActivities = runningTaskInfo.numActivities;
		  Calendar c = Calendar.getInstance ();
		  try {
			PackageManager pm = context.getPackageManager ();
			ApplicationInfo ai = pm.getApplicationInfo ( topActivity, 0 );
			if (isUserApp ( ai ) && !topActivity.equals ( oldActivity ) && !topActivity.equals ( "com.watchme.android" )) {
			  oldActivity = topActivity;
			  //TODO Aç burayı

			  HBDatabaseAdapter.getInstance ().setContextForDB ( context );
			  HBDatabaseAdapter.getInstance ().InsertInto ( topActivity, new HBTime ( c.get ( Calendar.HOUR_OF_DAY ), c.get ( Calendar.MINUTE ) ) );
			  Log.e ( "Eklendi", "Eklendi->" + topActivity );
			}
		  } catch (PackageManager.NameNotFoundException e) {
			Log.e ( "HATA", e.toString () );
		  }
		}
	  }
	}


	//  Looper.loop();
  }

  private boolean isUserApp(ApplicationInfo ai) {
	int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
	return (ai.flags & mask) == 0;
  }
}


// Return a list of the tasks that are currently running,
// with the most recent being first and older ones after in order.
// Taken 1 inside getRunningTasks method means want to take only
// top activity from stack and forgot the olders.

 /*
  * List<ActivityManager.RunningAppProcessInfo> taskInfo = am.getRunningAppProcesses();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("WEBIS",taskInfo.size()+"  vardır");
            String currentRunningActivityName = taskInfo.get(0).processName;
            Log.e("WEBIS",currentRunningActivityName);*/

          /*  if(!taskInfo.isEmpty())
			{
                //String currentRunningActivityName = taskInfo.get(0).processName;

                if (!currentRunningActivityName.equals("com.hayro.watchme")) {
                    Log.e("WEBIS",currentRunningActivityName);
                    break;
                }
            }*/
		   /* try {
				Process process = Runtime.getRuntime().exec("logcat");
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {

                    if (line.contains("com."))
                        publishProgress(line);
                    else Log.e("SONUC ALINDI", line);

                }
            } catch (IOException e) {
                Log.e("HATA ALINDI",e.toString());
            }
**/
