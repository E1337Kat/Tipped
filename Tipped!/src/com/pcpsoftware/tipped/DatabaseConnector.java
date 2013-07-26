//TODO: Add more comments to comply with Dr. Yoo's criteria
package com.pcpsoftware.tipped;

import java.sql.Timestamp;
import java.util.ArrayList; //for getAllTip() and getAllShift() methods
import java.util.Date;
import java.util.List; //likewise
import java.util.HashMap;

import com.pcpsoftware.tipped.database.Shift;
import com.pcpsoftware.tipped.database.ShiftTable;
import com.pcpsoftware.tipped.database.Tip;
import com.pcpsoftware.tipped.database.TipTable;

//import android.content.ContentProvider;
//import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
//import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
import android.util.Log;

public class DatabaseConnector {
	// database name
	private static final String DATABASE_NAME = "tipped.db";
	private static final int DATABASE_VERSION = 1; //named constant for database version
	
//	private static final String TAG = "TipsContentProvider";
//	public static final String AUTHORITY = "com.pcpsoftware.tipped.TipsContentProvider";
//	private static final String TIPS_TABLE_NAME = "Tips";
//	private static final String SHIFTS_TABLE_NAME = "Shifts";
//	private static final UriMatcher sUriMatcher;
//	 
//    private static final int TIPS = 1;
// 
//    private static final int TIPS_ID = 2;
// 
//    private static HashMap<String, String> notesProjectionMap;
//	
	private SQLiteDatabase database; //initializes database object
	private DatabaseOpenHelper databaseOpenHelper; //creates a database helper object
	
	private class DatabaseOpenHelper extends SQLiteOpenHelper 
	{
		
		// public constructor
		public DatabaseOpenHelper(Context context, String name, int version)
		{
			super(context, name, null, version);
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
	
	// public constructor for DatabaseConnector
	public DatabaseConnector(Context context) 
	{
		// create new DatabaseOpenHelper
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, DATABASE_VERSION);
	}	// end DatabaseConnector constructor
	
	// open the database connection
	public void open() throws SQLException 
	{
		// create or open a database for reading/writing
		database = databaseOpenHelper.getWritableDatabase();
	}	// end method open
	
	// close the database connection
	public void close() 
	{
		if(database != null)
			database.close();	// close the database connection	
	}	// end method close
	
	// inserts a new tip entry in database
	public long addTip(Tip tip) 
	{

		ContentValues newEntry = new ContentValues();
		newEntry.put("amount", tip.getAmount());
		newEntry.put("total", tip.getTotal());
		newEntry.put("percent", tip.getPercent());
		newEntry.put("time",tip.getTipTime().getTime()); //.getNanos()SQLite doesn't have a timestamp type like SQL so convert to int
		newEntry.put("checknum",tip.getCheckNum());
		newEntry.put("notes",tip.getNotes());
		newEntry.put("shift_parent",tip.getShiftId()); //TODO: How do we really do this?
		
		
		return database.insert("tip", null, newEntry);
		
	}	// end method addTip
	
	// updates a previous tip entry in the database
	public void updateTip(Tip tip)
	{
		ContentValues editEntry = new ContentValues();
		editEntry.put("amount", tip.getAmount());
		editEntry.put("total", tip.getTotal());
		editEntry.put("percent", tip.getPercent());
		editEntry.put("checknum",tip.getCheckNum());
		editEntry.put("notes",tip.getNotes());
		
		database.update("tip", editEntry, "_id=" + tip.getId(), null);
	}	// end method updateTip
	
	// insert a new shift into the database and return its id
	public long addShift(Shift shift)
	{
		ContentValues newEntry = new ContentValues();
		newEntry.put("date", shift.getDate());
		newEntry.put("time", shift.getShiftTime().getTime()); 
		newEntry.put("total",  shift.getTotal());
		newEntry.put("shift_type", shift.getShiftType());
		newEntry.put("notes",  shift.getNotes());	
		
		return database.insert("shift", null, newEntry);
	} // end method addShift
	
	//update a previous shift entry in the database
	public void updateShift(Shift shift)
	{
		//TODO: do we need this?
	}
	
	// delete the Shift specified by the given id
	public void deleteShift(long id)
	{
		database.delete("shift", "_id=" + id, null);
	}	// end method deleteShift
	
	// delete the Tip specified by the given id
	public void deleteTip(long id)
	{
		database.delete("tip",  "_id=" + id,  null);
	}	//end method deleteTip
	
	public void deleteAllShifts()
	{
		database.delete("shift", null, null);
	}
	
	//***Query section***
	//The following methods make queries and return cursors for use with adapter
	//classes for the interface
	
	//Return a Cursor referencing a tip in the database
	public Cursor getOneTip(long id)
	{
		//I think it's ok to get all columns here because where are using a WHERE clause
		//but correct me if I'm wrong -AC
		return database.query("tip", null, "_id=" + id, 
				null, null, null, null); 
	} // end method getOneTip
	
	//Return a Tip object created from a database entry
	//Tip ID will be -1 upon error
	public Tip getTipObj(long id)
	{
		Cursor c = database.query("tip", null, "_id=" + id, 
				null, null, null, null); //get cursor for tip
		
		if(c != null && c.moveToFirst()) //make sure cursor is valid and reset position
		{
		return new Tip(	c.getLong(c.getColumnIndex("_id")), //id
						c.getDouble(c.getColumnIndex("amount")), //amount
						c.getDouble(c.getColumnIndex("total")), //total
						c.getDouble(c.getColumnIndex("percent")), //percent
						new Timestamp(c.getLong(c.getColumnIndex("time"))),  //time
						c.getInt(c.getColumnIndex("checknum")), //check number
						c.getLong(c.getColumnIndex("shift_parent"))); //shift id
		}
		else return new Tip(-1); //signals an error
	
	} // end method getOneShift

	//Return a Cursor referencing all tips in the database
	public Cursor getAllTips()
	{
		//store the query results temporarily so we can close the db and THEN return
		Cursor c = database.query("tip", new String[] {"_id", "amount", "time"}, 
				null, null, null, null, null); //return id and time stamp columns

		return c;
	} // end method getAllTips
	
	//Return a Cursor referencing all tips in the database belonging to a shift
	public Cursor getAllTipsFromShift(long id)
	{
		return database.query("tip", new String[] {"_id", "amount", "time"}, 
				"shift_parent=" + id, null, null, null, null); //return id and time stamp columns
	} // end method getAllTipsFromShift
	
	//Return a Cursor referencing a shift in the database
	public Cursor getOneShift(long id)
	{
		return database.query("shift", null, "_id=" + id, 
				null, null, null, null); 
	}
	
	//Return a Cursor referencing all shifts in the database
	public Cursor getAllShifts()
	{
		return database.query("shift", new String[] {"_id", "time"}, 
				null, null, null, null, null); //return id and time stamp columns
	}
	
	//Return the total of the tips in a shift
	public double getShiftTotal(long id)
	{
		Cursor tip = getAllTipsFromShift(id); //get a cursor with all tips in the shift
		double total = 0.0; //initialize total

		Log.d("getShiftTotal:",tip.getCount() + " tips retrieved.");
		
		tip.moveToFirst(); //just in case
		
		if(tip.getCount() == 0)
			return 0.0; //return 0 if there are no tips
		do
		{
			total += tip.getDouble(tip.getColumnIndex("amount"));
			Log.d("getShiftTotal","tip total is now " + total);
		} while(tip.moveToNext());
		
		return total;
	}
	
}	// end class DatabaseConnector
