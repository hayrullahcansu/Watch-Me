package com.watchme.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.watchme.android.DefaultExceptionHandler;
import com.watchme.android.R;
import com.watchme.android.hbclustering.clustering.HBClustering;
import com.watchme.android.services.BackgroundService;
import com.watchme.android.services.HBClusteringDB;

public class MainActivity extends AppCompatActivity {
  Button bOpenStatistics, bOpenItemGenerator;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate ( savedInstanceState );
	setContentView ( R.layout.activity_main );
	HBClustering hbClustering = new HBClustering ();
	stopService ( new Intent ( getBaseContext (), BackgroundService.class ) );
	initialize ();

  }

  private void initialize() {
	Thread.setDefaultUncaughtExceptionHandler ( new DefaultExceptionHandler ( this ) );
	new HBClusteringDB ( this );
	bOpenStatistics = (Button) findViewById ( R.id.b_open_statistics );
	bOpenStatistics.setOnClickListener ( new View.OnClickListener () {
	  @Override
	  public void onClick(View view) {
		startActivity ( new Intent ( MainActivity.this, ActivityStatics.class ) );
	  }
	} );
	bOpenItemGenerator = (Button) findViewById ( R.id.b_open_item_generator );
	bOpenItemGenerator.setOnClickListener ( new View.OnClickListener () {
	  @Override
	  public void onClick(View view) {
		startActivity ( new Intent ( MainActivity.this, ActivityItemGenerator.class ) );
	  }
	} );

  }

    /*
	facebook path      com.facebook.katana
    twitter path      com.twitter.android
    snapchat path      com.snapchat.android
    instagram path      com.instagram.android

     */


  @Override
  protected void onDestroy() {
	super.onDestroy ();
	startService ( new Intent ( getBaseContext (), BackgroundService.class ) );
  }
}
