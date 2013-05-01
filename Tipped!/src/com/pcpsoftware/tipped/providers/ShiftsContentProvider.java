//package com.pcpsoftware.tipped.providers;
//
////Imports
//import java.util.HashMap;
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.util.Log;
//
//import com.pcpsoftware.tipped.*;
////End Imports
//
//public class ShiftsContentProvider extends ContentProvider {
//	private static final String TAG = "ShiftsContentProvider";
//	 
//    private static final String DATABASE_NAME = "shifts.db";
// 
//    private static final int DATABASE_VERSION = 1;
// 
//    private static final String SHIFTS_TABLE_NAME = "shift";
// 
//    public static final String AUTHORITY = "com.pcpsoftware.tipped.providers.ShiftsContentProvider";
// 
//    private static final UriMatcher sUriMatcher;
// 
//    private static final int SHIFTS = 1;
// 
//    private static final int SHIFTS_ID = 2;
// 
//    private static HashMap<String, String> shiftsProjectionMap;
//    
//    private static class DatabaseHelper extends SQLiteOpenHelper {
//    	 
//        DatabaseHelper(Context context) {
//            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        }
// 
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//        	// Enable foreign key constraints to link shift and tip tables
//        	if(!db.isReadOnly())
//        		db.execSQL("PRAGMA foreign_keys=ON;");
//            ShiftTable.onCreate(db);
//        }
// 
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            ShiftTable.onUpgrade(db,  oldVersion, newVersion); //call shift table's onUpgrade
//        }
//    }// end DatabaseHelper class
//    
//    private DatabaseHelper dbHelper;
//    
//    //start ContentProvider required classes
//    /*
//     * (non-Javadoc)
//     * @see android.content.ContentProvider#onCreate()
//     */
//    @Override
//    public boolean onCreate() {
//        dbHelper = new DatabaseHelper(getContext());
//        return true;
//    }
//    
//    @Override
//    public String getType(Uri uri) {
//        switch (sUriMatcher.match(uri)) {
//            case SHIFTS:
//                return ShiftTable.CONTENT_TYPE;
//            default:
//                throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//    }
//    
//    @Override
//    public int delete(Uri uri, String where, String[] whereArgs) {
//    	
//    	int count = 0;
//    	return count;
//    }
//    
//    @Override 
//    public int update(Uri uri, ContentValues contentValues, String where, String[] whereArgs) {
//    	
//    	int count = 0;
//    	return count;
//    }
//    
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//    	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//        qb.setTables(SHIFTS_TABLE_NAME);
//        qb.setProjectionMap(shiftsProjectionMap);
// 
//        switch (sUriMatcher.match(uri)) {    
//            case SHIFTS:
//                break;
//            case SHIFTS_ID:
//                selection = selection + "_id = " + uri.getLastPathSegment();
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI " + uri);
//        }
//    	
//    	SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
// 
//        c.setNotificationUri(getContext().getContentResolver(), uri);
//        return c;
//    }
//    
//    @Override
//    public Uri insert(Uri uri, ContentValues initialValues) {
//    	if (sUriMatcher.match(uri) != SHIFTS) {
//            throw new IllegalArgumentException("Unknown URI " + uri);
//        }
// 
//        ContentValues values;
//        if (initialValues != null) {
//            values = new ContentValues(initialValues);
//        } else {
//            values = new ContentValues();
//        }
//        
//        ContentValues newEntry = new ContentValues();
//		newEntry.put("date", shift.getDate());
//		newEntry.put("time", shift.getTime().getTime()); 
//		newEntry.put("total",  shift.getTotal());
//		newEntry.put("notes",  shift.getNotes());
// 
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        long rowId = db.insert(SHIFTS_TABLE_NAME, null, values);
//        if (rowId > 0) {
//            Uri noteUri = ContentUris.withAppendedId(ShiftTable.CONTENT_URI, rowId);
//            getContext().getContentResolver().notifyChange(noteUri, null);
//            return noteUri;
//        }
//        
//        throw new SQLException("Failed to insert row into " + uri);
//    }
//    
//    // end ContentProvider required methods
//    
//    static {
//        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        sUriMatcher.addURI(AUTHORITY, SHIFTS_TABLE_NAME, SHIFTS);
//        sUriMatcher.addURI(AUTHORITY, SHIFTS_TABLE_NAME + "/#", SHIFTS_ID);
// 
//        shiftsProjectionMap = new HashMap<String, String>();
//        shiftsProjectionMap.put(ShiftTable.COLUMN_ID, ShiftTable.COLUMN_ID);
//        shiftsProjectionMap.put(ShiftTable.COLUMN_DATE, ShiftTable.COLUMN_DATE);
//        shiftsProjectionMap.put(ShiftTable.COLUMN_TIME, ShiftTable.COLUMN_TIME);
//        shiftsProjectionMap.put(ShiftTable.COLUMN_TOTAL, ShiftTable.COLUMN_TOTAL);
//        shiftsProjectionMap.put(ShiftTable.COLUMN_NOTES, ShiftTable.COLUMN_NOTES);
//    }
//}
