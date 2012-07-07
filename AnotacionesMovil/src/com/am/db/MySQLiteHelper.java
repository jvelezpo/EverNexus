package com.am.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_USERS = "users";
	public static final String COLUMN_USER_ID = "_id";
	public static final String COLUMN_USER_NAME = "name";
	public static final String COLUMN_USER_EMAIL = "email";
	public static final String COLUMN_USER_PASSWORD = "password";
	
	public static final String TABLE_NOTES = "notes";
	public static final String COLUMN_NOTE_ID = "_id";
	public static final String COLUMN_NOTE_NOTE = "nota";

	private static final String DATABASE_NAME = "anotaciones.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE_USER_TABLE = "create table " + TABLE_USERS  
			+ "(" 
			+ COLUMN_USER_ID + " integer primary key autoincrement, " 
			+ COLUMN_USER_NAME + " text not null, " 
			+ COLUMN_USER_EMAIL + " text not null, "
			+ COLUMN_USER_PASSWORD + " text not null "
			+  ");";
	private static final String DATABASE_CREATE_NOTE_TABLE = "create table " + TABLE_NOTES  
			+ "(" 
			+ COLUMN_NOTE_ID + " integer primary key autoincrement, " 
			+ COLUMN_NOTE_NOTE + " text not null " 
			+  ");";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_USER_TABLE);
		database.execSQL(DATABASE_CREATE_NOTE_TABLE);
		database.execSQL("INSERT INTO notes VALUES (1, 'holasas')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}

}