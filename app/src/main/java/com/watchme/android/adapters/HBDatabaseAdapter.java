package com.watchme.android.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.watchme.android.hbclustering.clustering.Cluster;
import com.watchme.android.hbclustering.clustering.HBClustering;
import com.watchme.android.hbclustering.clustering.IResultOfOrganizingItems;
import com.watchme.android.hbclustering.clustering.Item;
import com.watchme.android.hbclustering.others.HBTime;
import com.watchme.android.services.HBClusteringDB;

/**
 * Created by hayru on 11/11/2016.
 */
public class HBDatabaseAdapter {
  private static HBDatabaseAdapter ourInstance = new HBDatabaseAdapter ();
  private HBClusteringDB db;
  public SQLiteDatabase sqr, sqw;

  public static HBDatabaseAdapter getInstance() {
	return ourInstance;
  }

  public void setContextForDB(Context _context) {
	if (db == null || sqr == null || sqw == null) {
	  db = new HBClusteringDB ( _context );
	  sqr = db.getReadableDatabase ();
	  sqw = db.getWritableDatabase ();
	}

  }

  private HBDatabaseAdapter() {
  }


  /*
   * Insert a item to db
   */
  public void InsertInto(String app_name, HBTime time) {
	String query = String.format ( "INSERT INTO items(APP_ID, PURE_TIME,HOUR,MINUTE) VALUES(%d,%d," +
		  "%d,%d);", GetAPPID ( app_name ), time.getPureTime (), time.getHour (), time.getMinutes () );
	sqw.execSQL ( query );
  }

  /*
   *       gets all items from db and calculate clustering
   */
  public void OrganizeItems(HBClustering hb, IResultOfOrganizingItems result) {

	int[] ids = new int[0];
	String[] names = new String[0];
	int row = 0;
	String query = String.format ( "SELECT items.APP_ID, app_ids.APP_NAME FROM items " +
		  "LEFT OUTER JOIN app_ids " +
		  "ON (items.APP_ID = app_ids.APP_ID AND app_ids.IS_DELETED = 0) " +
		  "GROUP BY items.APP_ID;" );
	Cursor cursor = sqr.rawQuery ( query, null );
	if (cursor != null && cursor.moveToFirst ()) {
	  ids = new int[cursor.getCount ()];
	  names = new String[cursor.getCount ()];
	  do {
		ids[row] = cursor.getInt ( 0 );
		names[row] = cursor.getString ( 1 );
		row++;
	  } while (cursor.moveToNext ());
	  cursor.close ();
	} else {
	  result.onResultOfOrganizingItems ( false, "Not found any application", hb );
	  return;
	}
	//Log.e("TAZE_CIKTI", "0.id:" + ids[0] + "pck:" + names[0] + "   1.id:" + ids[1] + "pck:" + names[1]);
	for (int i = 0; i < ids.length; i++) {
	  Cluster cluster = new Cluster ();
	  cluster.setClusterName ( names[i] );
	  query = String.format ( "SELECT items.APP_ID,items.PURE_TIME FROM items " +
			"LEFT OUTER JOIN app_ids ON items.APP_ID = app_ids.APP_ID " +
			"WHERE app_ids.IS_DELETED = 0 AND items.APP_ID = %d " +
			"ORDER BY items.PURE_TIME ASC;", ids[i] );
	  cursor = sqr.rawQuery ( query, null );
	  if (cursor != null && cursor.moveToFirst ()) {
		do {
		  cluster.addNewItem ( new Item ( cursor.getInt ( 1 ) ) );
		} while (cursor.moveToNext ());
		cursor.close ();
		hb.addCluster ( cluster );
	  }
	}
	hb.calculateAllClusters ();
	result.onResultOfOrganizingItems ( true, "calculated", hb );
  }


  /*
   * gets application id from database.
   */
  private int GetAPPID(String app_name) {
	String query = String.format ( "SELECT APP_ID, APP_NAME FROM app_ids WHERE app_name = '%s';", app_name );
	Cursor cursor = sqr.rawQuery ( query, null );
	if (cursor != null && cursor.moveToFirst ()) {
	  int temp = cursor.getInt ( 0 );
	  cursor.close ();
	  return temp;
	}
	if (cursor != null) {
	  cursor.close ();
	}
	query = String.format ( "INSERT INTO app_ids(app_name) VALUES('%s');", app_name );
	sqw.execSQL ( query );
	return GetAPPID ( app_name );
  }

}
