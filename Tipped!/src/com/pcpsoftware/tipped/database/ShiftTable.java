package com.pcpsoftware.tipped.database;

import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
import android.util.Log;

public class ShiftTable
{
	//Declare fields to assemble database create string with
//	public static final Uri CONTENT_URI = Uri.parse("content://"
//            + ShiftsContentProvider.AUTHORITY + "/shifts");
//	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pcpsw.shifts";
	public static final String TABLE_SHIFT = "shift"; //table name
	public static final String COLUMN_ID = "_id"; //primary key
	public static final String COLUMN_DATE = "date"; //shift date: long (optional)
	public static final String COLUMN_TIME = "time"; //shift time stamp: integer (required)
	public static final String COLUMN_SHIFT = "shift_type"; //shift type: integer (required)
	public static final String COLUMN_TOTAL = "total"; //shift total: real (required)
	public static final String COLUMN_NOTES = "notes"; //shift note string: text (optional)
	
	//assemble into creation string
	private static final String DATABASE_CREATE =
			"create table "
		+	TABLE_SHIFT
		+ 	"("
		+	COLUMN_ID + " integer primary key autoincrement, "
		+	COLUMN_DATE + " integer, "
		+	COLUMN_TIME + " integer not null, "
		+	COLUMN_SHIFT + " integer, "
		+	COLUMN_TOTAL + " real not null, "
		+	COLUMN_NOTES + " text"
		+ ");";
	
	public static void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(ShiftTable.class.getName(),"Upgrading database from version "
				+ oldVersion + " to " + newVersion +  ", this will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_SHIFT); //clear table
		onCreate(db); //and recreate
		
	}
}
