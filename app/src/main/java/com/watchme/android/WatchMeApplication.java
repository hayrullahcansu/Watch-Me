package com.watchme.android;

import android.app.Application;
import android.content.Context;

/**
 * Created by hayru on 12/7/2016.
 */
public class WatchMeApplication extends Application {
  private static Context mContext;

  public static WatchMeApplication instace;

  @Override
  public void onCreate() {
	super.onCreate ();
	mContext = getApplicationContext ();
	instace = this;
  }

  @Override
  public Context getApplicationContext() {
	return super.getApplicationContext ();
  }

  public static WatchMeApplication getInstace() {
	return instace;
  }

}
