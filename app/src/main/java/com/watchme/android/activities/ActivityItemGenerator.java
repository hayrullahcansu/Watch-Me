package com.watchme.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.watchme.android.R;
import com.watchme.android.adapters.HBDatabaseAdapter;
import com.watchme.android.hbclustering.others.HBTime;
import com.watchme.android.utils.Message;

/**
 * Created by hayru on 11/12/2016.
 */
public class ActivityItemGenerator extends Activity {
  Button bAdd;
  EditText etAppName, et_time_hour, et_time_minute;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_item_generator);
	initialize();
  }

  private void initialize() {
	etAppName = (EditText) findViewById(R.id.et_app_name);
	et_time_hour = (EditText) findViewById(R.id.et_time_hour);
	et_time_minute = (EditText) findViewById(R.id.et_time_minute);
	bAdd = (Button) findViewById(R.id.b_add_item);
	bAdd.setOnClickListener(new View.OnClickListener() {
	  @Override
	  public void onClick(View view) {
		if (CheckInputs())
		  insertNew();
	  }
	});
  }

  private void insertNew() {
	int hour = Integer.parseInt(et_time_hour.getText().toString());
	int minute = Integer.parseInt(et_time_minute.getText().toString());
	HBDatabaseAdapter.getInstance().setContextForDB(this);
	HBDatabaseAdapter.getInstance().InsertInto(etAppName.getText().toString().trim().toLowerCase(), new HBTime(hour, minute));
	et_time_minute.setText("");
  }

  private boolean CheckInputs() {
	if (etAppName.getText().toString().trim().toLowerCase().equals("") && etAppName.getText().toString().trim().toLowerCase().contains("com.")) {
	  Message.message(this, "Uygulama Yolu Girilmelidir");
	  return false;
	}
	if (et_time_hour.getText().toString().equals("")) {
	  Message.message(this, "Saat girilmelidir");
	  return false;

	}
	if (et_time_minute.getText().toString().equals("")) {
	  Message.message(this, "Dakika girilmelidir");
	  return false;
	}
	int hour = Integer.parseInt(et_time_hour.getText().toString());
	if (hour < 0 && hour > 23) {
	  Message.message(this, "Saat düzgün girilmeli");
	  return false;
	}
	int minute = Integer.parseInt(et_time_minute.getText().toString());
	if (minute < 0 && minute > 59) {
	  Message.message(this, "Dakika düzgün girilmeli");
	  return false;
	}
	return true;
  }
}
