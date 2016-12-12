package com.watchme.android.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hayru on 11/11/2016.
 */
public class AutoBroadcastReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent ıntent) {
	Intent pushIntent = new Intent ( context, BackgroundService.class );
	context.startService ( pushIntent );
  }
}
