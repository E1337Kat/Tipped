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
//import com.pcpsoftware.tipped.database.DatabaseOpenHelper;
//import com.pcpsoftware.tipped.database.TipTable;
//import com.pcpsoftware.tipped.database.Tip;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//public class TipContentProvider extends ContentProvider {
//
//	// databases
//	private DatabaseOpenHelper dbHelper;
//	
//	// used for the UriMatcher
//	private static final int TIP = 10;
//	private static final int TIP_ID = 20;
//	
//	private static final String AUTHORITY = "com.pcpsoftware.tipped.contentprovider";
//	
//	private static final String BASE_PATH = "tips";
//	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
//	
//	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tips";
//	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/tips";
//	
//	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//	static {
//		sURIMatcher.addURI(AUTHORITY, BASE_PATH, TIP);
//		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TIP_ID);
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
//		qb.setTables(TipTable.TABLE_TIP);
//		
//		int uriType = sURIMatcher.match(uri);
//		switch (uriType) {    
//			case TIP:
//				break;
//			case TIP_ID:
//				// Adding the ID to the original query
//				qb.appendWhere(TipTable.COLUMN_ID + "=" + uri.getLastPathSegment());
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
//			case TIP:
//				id = db.insert(TipTable.TABLE_TIP, null, initialValues);
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
//		case TIP:
//			rowsDeleted = db.delete(TipTable.TABLE_TIP, selection, selectionArgs);
//			break;
//		case TIP_ID:
//			String id = uri.getLastPathSegment();
//			if (TextUtils.isEmpty(selection)) {
//				rowsDeleted = db.delete(TipTable.TABLE_TIP, 
//						TipTable.COLUMN_ID + "=" + id, 
//						null);
//			} else {
//				rowsDeleted = db.delete(TipTable.TABLE_TIP, 
//						TipTable.COLUMN_ID + "=" + id + " and " + selection, 
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
//		case TIP:
//			rowsUpdated = db.update(TipTable.TABLE_TIP, 
//					values, 
//					selection, 
//					selectionArgs);
//			break;
//		case TIP_ID:
//			String id = uri.getLastPathSegment();
//			if (TextUtils.isEmpty(selection)) {
//				rowsUpdated = db.update(TipTable.TABLE_TIP,
//						values, 
//						TipTable.COLUMN_ID + "=" + id, 
//						null);
//			} else {
//				rowsUpdated = db.update(TipTable.TABLE_TIP, 
//						values, 
//						TipTable.COLUMN_ID + "=" + id + " and " + selection, 
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
//		String[] available = { TipTable.COLUMN_AMOUNT,
//				TipTable.COLUMN_TIME, TipTable.COLUMN_SHIFT,
//				TipTable.COLUMN_PERCENT, TipTable.COLUMN_CHECKNUM,
//				TipTable.COLUMN_TOTAL, TipTable.COLUMN_NOTES,
//				TipTable.COLUMN_ID};
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