package com.pcpsoftware.tipped;

import android.app.Application;
import android.util.Log;


public class Tipped extends Application {
	DatabaseConnector tipdb;
	
	
	private static Tipped singleton;
	 
	public static Tipped getInstance() {
		return singleton;
	}
	 
	@Override
	public void onCreate() {
	super.onCreate();
	singleton = this;
	tipdb = new DatabaseConnector(Tipped.this); //create a DatabaseConnector object that will be shared across activities
	}
	

}
