package com.watchme.android.services;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.watchme.android.utils.Message;

import java.io.File;

/**
 * Created by hayru on 11/11/2016.
 */
public class HBClusteringDB extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;

  public static final String DATABASE_NAME = "HBClusteringDB.db";

  public static final String T1NAME = "items";
  public static final String T1CID = "_ID";
  public static final String T1CAPPID = "APP_ID";
  public static final String T1CPURETIME = "PURE_TIME";
  public static final String T1CHOUR = "HOUR";
  public static final String T1CMINUTE = "MINUTE";

  public static final String T2NAME = "app_ids";
  public static final String T2CID = "APP_ID";
  public static final String T2CEN = "APP_NAME";
  public static final String T2CTR = "IS_DELETED";


  private Context context;

  public HBClusteringDB(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	this.context = context;
	SQLiteDatabase db = getWritableDatabase();
	db.close();
	try {
	  context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
	} catch (Exception e) {

	  context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
	}
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
	SQLiteDatabase _db;
	try {
	  File databaseFile = context.getDatabasePath(DATABASE_NAME);
	  _db = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);

	  _db.close();
	} catch (Exception e) {
			/*
            String databasePath =  context.getDir +  "/" + DATABASE_NAME;
            File databaseFile = new File(databasePath);
            _db = SQLiteDatabase.openOrCreateDatabase(databaseFile,null);
            _db.close();
            */
	}
	Message.message(context, "Veritabanı oluşturulacak");

	try {
	  String query = "";
	  query = "CREATE TABLE " + T1NAME + " (_ID INTEGER PRIMARY KEY AUTOINCREMENT, APP_ID INTEGER DEFAULT 0, PURE_TIME INTEGER DEFAULT 0, HOUR INTEGER DEFAULT 0 , MINUTE INTEGER DEFAULT 0);";
	  db.execSQL(query);
	  query = "CREATE TABLE " + T2NAME + " (APP_ID INTEGER PRIMARY KEY, APP_NAME VARCHAR(255), IS_DELETED BOOLEAN DEFAULT 0);";
	  db.execSQL(query);
	} catch (SQLException e) {
	  e.printStackTrace();
	  Message.message(context, "" + e);
	}
	Message.message(context, "Veritabanı oluşturuldu");

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	try {
	  String query = "";
	  query = "DROP TABLE IF EXISTS " + T1NAME;
	  db.execSQL(query);
	  query = "DROP TABLE IF EXISTS " + T2NAME;
	  db.execSQL(query);
	  onCreate(db);
	} catch (SQLException e) {
	  e.printStackTrace();
	  Message.message(context, "" + e);
	}
  }
}