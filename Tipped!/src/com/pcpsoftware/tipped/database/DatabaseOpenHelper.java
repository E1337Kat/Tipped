package com.pcpsoftware.tipped.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper 
{
	private static final String DATABASE_NAME = "tipped.db";
	private static final int DATABASE_VERSION = 1; //named constant for database version
	
	// public constructor
	public DatabaseOpenHelper(Context context) // did incorporate , String name, int version
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}	// end DatabaseOpenHelper constructor
	
	// creates table of data values when database is created
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Enable foreign key constraints to link shift and tip tables
		if(!db.isReadOnly())
			db.execSQL("PRAGMA foreign_keys=ON;");
		
		ShiftTable.onCreate(db); //create shift table by calling shift table's onCreate
		TipTable.onCreate(db); //create tip table
	}	//end method onCreate
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		ShiftTable.onUpgrade(db,  oldVersion, newVersion); //call shift table's onUpgrade
		TipTable.onUpgrade(db, oldVersion, newVersion); //call tip table's onUpgrade
	}	// end method onUpgrade
}	// end class DatabaseOpenHelper
