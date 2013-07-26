//package com.pcpsoftware.tipped.contentprovider;
//
//import android.content.ContentProvider;
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.text.TextUtils;
//
//import com.pcpsoftware.tipped.database.ShiftTable;
//import com.pcpsoftware.tipped.database.DatabaseOpenHelper;
//import com.pcpsoftware.tipped.database.Shift;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//public class ShiftsContentProvider extends ContentProvider {
//
//	// databases
//	private DatabaseOpenHelper dbHelper;
//	
//	private Shift shift = new Shift();
//	
//	// used for the UriMatcher
//	private static final int SHIFT = 10;
//	private static final int SHIFT_ID = 20;
//	
//	private static final String AUTHORITY = "com.pcpsoftware.tipped.contentprovider";
//	
//	private static final String BASE_PATH = "shifts";
//	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
//	
//	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/shifts";
//	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/shifts";
//	
//	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//	static {
//		sURIMatcher.addURI(AUTHORITY, BASE_PATH, SHIFT);
//		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SHIFT_ID);
//	}
//	
//	@Override
//	  public boolean onCreate() {
//	  dbHelper = new DatabaseOpenHelper(getContext());
//	  return false;
//	}
//	
//	@Override
//	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//		
//		//Using SQLiteQueryBuilder instead of query() method
//		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//		
//		// Check if the caller has requested a column which does not exist
//		checkColumns(projection);
//		
//		// Set the Tables
//		qb.setTables(ShiftTable.TABLE_SHIFT);
//		
//		int uriType = sURIMatcher.match(uri);
//		switch (uriType) {    
//			case SHIFT:
//				break;
//			case SHIFT_ID:
//				// Adding the ID to the original query
//				qb.appendWhere(ShiftTable.COLUMN_ID + "=" + uri.getLastPathSegment());
//				break;
//			default:
//				throw new IllegalArgumentException("Unknown URI " + uri);
//		}
//		
//		SQLiteDatabase db = dbHelper.getReadableDatabase();
//		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
//		// Make sure that potential listeners are getting notified
//		c.setNotificationUri(getContext().getContentResolver(), uri);
//		
//		return c;
//	}
//	
//	@Override
//	public String getType(Uri uri) {
//	  return null;
//	}
//	
//	@Override
//	public Uri insert(Uri uri, ContentValues initialValues) {
//		
//		int uriType = sURIMatcher.match(uri);
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		int rowsDeleted = 0;
//		long id = 0;
//		
////		ContentValues values;
////	  	if (initialValues != null) {
////	  		values = new ContentValues(initialValues);
////	  	} else {
////	  		values = new ContentValues();
////	  	}
////	  
////	 	ContentValues newEntry = new ContentValues();
////		newEntry.put("date", shift.getDate());
////		newEntry.put("time", shift.getShiftTime().getTime()); 
////		newEntry.put("total",  shift.getTotal());
////		newEntry.put("notes",  shift.getNotes());
//		
//		switch (uriType) {
//			case SHIFT:
//				id = db.insert(ShiftTable.TABLE_SHIFT, null, initialValues);
//				break;
//			default:
//				throw new IllegalArgumentException("Unknown URI: " + uri);
//		}
//		getContext().getContentResolver().notifyChange(uri, null);
//		return Uri.parse(BASE_PATH + "/" + id);
//	}
//
//	
//	@Override
//	public int delete(Uri uri, String selection, String[] selectionArgs) {
//		int uriType = sURIMatcher.match(uri);
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		int rowsDeleted = 0;
//		
//		switch (uriType) {
//		case SHIFT:
//			rowsDeleted = db.delete(ShiftTable.TABLE_SHIFT, selection, selectionArgs);
//			break;
//		case SHIFT_ID:
//			String id = uri.getLastPathSegment();
//			if (TextUtils.isEmpty(selection)) {
//				rowsDeleted = db.delete(ShiftTable.TABLE_SHIFT, 
//						ShiftTable.COLUMN_ID + "=" + id, 
//						null);
//			} else {
//				rowsDeleted = db.delete(ShiftTable.TABLE_SHIFT, 
//						ShiftTable.COLUMN_ID + "=" + id + " and " + selection, 
//						selectionArgs);
//			}
//			break;
//		default:
//			throw new IllegalArgumentException("Unknown URI: " + uri);
//		}
//		
//		getContext().getContentResolver().notifyChange(uri, null);
//		return rowsDeleted;
//	}
//	
//	@Override 
//	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
//		
//		int uriType = sURIMatcher.match(uri);
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		int rowsUpdated = 0;
//		
//		switch (uriType) {
//		case SHIFT:
//			rowsUpdated = db.update(ShiftTable.TABLE_SHIFT, 
//					values, 
//					selection, 
//					selectionArgs);
//			break;
//		case SHIFT_ID:
//			String id = uri.getLastPathSegment();
//			if (TextUtils.isEmpty(selection)) {
//				rowsUpdated = db.update(ShiftTable.TABLE_SHIFT,
//						values, 
//						ShiftTable.COLUMN_ID + "=" + id, 
//						null);
//			} else {
//				rowsUpdated = db.update(ShiftTable.TABLE_SHIFT, 
//						values, 
//						ShiftTable.COLUMN_ID + "=" + id + " and " + selection, 
//						selectionArgs);
//			}
//			break;
//		default:
//			throw new IllegalArgumentException("Unknown URI: " + uri);
//		}
//		
//		getContext().getContentResolver().notifyChange(uri, null);
//		return rowsUpdated;
//	}
//	
//	private void checkColumns(String[] projection) {
//		String[] available = { ShiftTable.COLUMN_DATE,
//				ShiftTable.COLUMN_TIME, ShiftTable.COLUMN_SHIFT, 
//				ShiftTable.COLUMN_TOTAL, ShiftTable.COLUMN_NOTES,
//				ShiftTable.COLUMN_ID};
//		if (projection != null) {
//			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
//			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
//			// Check if all columns which are requested are available
//			if (!availableColumns.containsAll(requestedColumns)) {
//				throw new IllegalArgumentException("Unknown columns in projection");
//			}
//		}
//	}
//}