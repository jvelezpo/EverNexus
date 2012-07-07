package com.am.DAO;

import java.util.ArrayList;
import java.util.List;

import com.am.db.MySQLiteHelper;
import com.am.entidades.Nota;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NotasDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_NOTE_ID, MySQLiteHelper.COLUMN_NOTE_NOTE };

	public NotasDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Nota createNote(String nota) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NOTE_NOTE, nota);
		long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, 
				allColumns, MySQLiteHelper.COLUMN_NOTE_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Nota newNota = cursorToNota(cursor);
		cursor.close();
		return newNota;
	}
	
	public void updateNote(String nota){
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NOTE_NOTE, nota);
		String where = MySQLiteHelper.COLUMN_NOTE_ID + "='" + 1 + "'";   
		database.update(MySQLiteHelper.TABLE_NOTES, values, where, null);
	}

	public void deleteNote(Nota nota) {
		long id = nota.getId();
		System.out.println("Nota deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_NOTE_ID + " = " + id, null);
	}

	public List<Nota> getAllNotes() {
		List<Nota> notas = new ArrayList<Nota>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Nota nota = cursorToNota(cursor);
			notas.add(nota);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return notas;
	}

	private Nota cursorToNota(Cursor cursor) {
		Nota nota = new Nota();
		nota.setId(cursor.getLong(0));
		nota.setNota(cursor.getString(1));
		return nota;
	}
} 