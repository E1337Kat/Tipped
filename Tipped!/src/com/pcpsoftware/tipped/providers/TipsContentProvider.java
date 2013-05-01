//package com.pcpsoftware.tipped.providers;
//
////Imports
//import java.util.HashMap;
//
//import com.pcpsoftware.tipped.TipTable;
//import com.pcpsoftware.tipped.TipTable;
//
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
////End Imports
//
//public class TipsContentProvider extends ContentProvider {
//	private static final String TAG = "TipsContentProvider";
//	 
//    private static final String DATABASE_NAME = "tips.db";
// 
//    private static final int DATABASE_VERSION = 1;
// 
//    private static final String TIPS_TABLE_NAME = "tips";
// 
//    public static final String AUTHORITY = "com.pcpsoftware.tipped.providers.TipsContentProvider";
// 
//    private static final UriMatcher sUriMatcher;
// 
//    private static final int TIPS = 1;
// 
//    private static final int TIPS_ID = 2;
// 
//    private static HashMap<String, String> tipsProjectionMap;
//    
//    
//    
//    static {
//        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        sUriMatcher.addURI(AUTHORITY, TIPS_TABLE_NAME, TIPS);
//        sUriMatcher.addURI(AUTHORITY, TIPS_TABLE_NAME + "/#", TIPS_ID);
// 
//        tipsProjectionMap = new HashMap<String, String>();
//        tipsProjectionMap.put(TipTable.COLUMN_ID, TipTable.COLUMN_ID);
//        tipsProjectionMap.put(TipTable.COLUMN_AMOUNT, TipTable.COLUMN_AMOUNT);
//        tipsProjectionMap.put(TipTable.COLUMN_TOTAL, TipTable.COLUMN_TOTAL);
//        tipsProjectionMap.put(TipTable.COLUMN_PERCENT, TipTable.COLUMN_PERCENT);
//        tipsProjectionMap.put(TipTable.COLUMN_TIME, TipTable.COLUMN_TIME);
//        tipsProjectionMap.put(TipTable.COLUMN_CHECKNUM, TipTable.COLUMN_CHECKNUM);
//        tipsProjectionMap.put(TipTable.COLUMN_NOTES, TipTable.COLUMN_NOTES);
//        tipsProjectionMap.put(TipTable.COLUMN_SHIFT, TipTable.COLUMN_SHIFT);
//        
//        
//    }
//}
