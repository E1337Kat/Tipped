package com.pcpsoftware.tipped;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.pcpsoftware.tipped.providers.*;

public class TipTable
{
	//Declare fields to assemble database create string with
//	public static final Uri CONTENT_URI = Uri.parse("content://"
//            + TipsContentProvider.AUTHORITY + "/tips");
//	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pcpsw.tips";
	public static final String TABLE_TIP = "tip"; //table name
	public static final String COLUMN_ID = "_id"; //primary key
	public static final String COLUMN_AMOUNT = "amount"; //tip amount: real (required)
	public static final String COLUMN_TOTAL = "total"; //bill total: real (optional)
	public static final String COLUMN_PERCENT = "percent"; //tip percent: real (optional)
	public static final String COLUMN_TIME = "time"; //time stamp: integer (required) 
	public static final String COLUMN_CHECKNUM = "checknum"; //check number: integer (optional)
	public static final String COLUMN_NOTES = "notes"; //note string: text (optional)
	public static final String COLUMN_SHIFT = "shift_parent"; //reference to parent shift table (required)
	
	//assemble into creation string
	private static final String DATABASE_CREATE =
			"create table "
		+	TABLE_TIP
		+ 	"("
		+	COLUMN_ID + " integer primary key autoincrement, "
		+	COLUMN_AMOUNT + " real not null, "
		+	COLUMN_TOTAL + " real, "
		+	COLUMN_PERCENT + " real, "
		+	COLUMN_TIME + " integer not null, "
		+ 	COLUMN_CHECKNUM + " integer, "
		+ 	COLUMN_NOTES + " text,"
		+	COLUMN_SHIFT + " integer not null, "
		+	" FOREIGN KEY(" + COLUMN_SHIFT + ") REFERENCES shift(_id)"
		+ ");";
	
	public static void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(TipTable.class.getName(),"Upgrading database from version "
				+ oldVersion + " to " + newVersion +  ", this will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_TIP); //clear table
		onCreate(db); //and recreate
	}
}
