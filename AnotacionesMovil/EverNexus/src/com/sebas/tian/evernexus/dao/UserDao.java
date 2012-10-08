package com.sebas.tian.evernexus.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sebas.tian.evernexus.entity.User;

public class UserDao extends SQLiteOpenHelper{
	// All Static variables
		// Database Version
		private static final int DATABASE_VERSION = 1;

		// Database Name
		private static final String DATABASE_NAME = "evernexus";

		// User table name
		private static final String TABLE_USERS = "user";

		// User Table Columns names
		private static final String ID = "id";
		private static final String USERNAME = "usename";
		private static final String EMAIL = "email";
		private static final String PASSWORD = "password";

		public UserDao(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// Creating Tables
		public void onCreate(SQLiteDatabase database) {
			// Database creation SQL statement
			String CREATE_USERS_TABLE = "create table " + TABLE_USERS + "(" 
					+ ID + " integer primary key autoincrement, " 
					+ USERNAME + " varchar(50) not null, " 
					+ PASSWORD + " varchar(50), "
					+ EMAIL + " varchar(100));";
			database.execSQL(CREATE_USERS_TABLE);
		}

		// Upgrading database
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Drop older table if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

			// Create tables again
			onCreate(db);
		}

		/**
		 * All CRUD Operations
		 */

		// Adding new user
		public void addUser(User user) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(USERNAME, user.getUsername()); // User Username
			values.put(PASSWORD, user.getPassword()); // User Password
			values.put(EMAIL, user.getEmail()); // User Email

			// Inserting Row
			db.insert(TABLE_USERS, null, values);
			db.close(); // Closing database connection
		}

		// Getting single user
		public User getUser() {
			User user = new User();
			// Select last user Query
			String selectQuery = "SELECT " + USERNAME + " FROM " + TABLE_USERS + " ORDER BY " + ID + " DESC LIMIT 1";
			
			SQLiteDatabase db = this.getWritableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
		    
		    if (cursor.moveToFirst()) {
		        do {
		            user.setUsername(cursor.getString(0));
		        } while (cursor.moveToNext());
		    }
			// return user
			return user;
		}

		// Getting users Count
		public int getUsersCount() {
			return 0;
		}

		// Updating single user
		public int updateUser(User user) {
			return 0;
		}

		// Deleting single user
		public void deleteUser(User user) {
		}
		
		// Deleting all users
		public void deleteAllUser() {
			SQLiteDatabase db = this.getWritableDatabase();
			onUpgrade(db, 1, 1);
		}
}
