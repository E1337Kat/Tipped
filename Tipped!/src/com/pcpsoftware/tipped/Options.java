package com.pcpsoftware.tipped;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class Options extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        
        //Register listeners for tabs
        Button optionsShiftTab = (Button) findViewById(R.id.optionsShiftTab);
        optionsShiftTab.setOnClickListener(optionsShiftsTabListener);
        Button optionsStatsTab = (Button) findViewById(R.id.optionsStatsTab);
        optionsStatsTab.setOnClickListener(optionsStatsTabListener);
        
        //Register listener for "Delete All Shifts" button
        Button deleteShiftsButton = (Button) findViewById(R.id.deleteAllShiftsButton);
        deleteShiftsButton.setOnClickListener(deleteShiftsListener);
    }
    
    public OnClickListener deleteShiftsListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		AlertDialog.Builder builder = 
    				new AlertDialog.Builder(Options.this);
    		builder.setTitle(R.string.delete_title);
    		builder.setMessage(R.string.delete_message);
    		
    		builder.setNegativeButton(R.string.cancel, null);
    		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
		    		Tipped.getInstance().tipdb.deleteAllShifts();
				}
			});
    		builder.show();
    	}
    };
    
    // listener for main view button
    // switches to stats view
    public OnClickListener optionsShiftsTabListener = new OnClickListener()
    {
       @Override
       public void onClick(View v) 
       {
    	   //Create an intent for the stats view, and launch it
    	   Intent statsIntent = new Intent(Options.this, MainView.class);
    	   Options.this.startActivity(statsIntent);
       } // end method onClick
    }; // end OnClickListener anonymous inner class
    
    // listener for main stats button
    // switches to stats view
    public OnClickListener optionsStatsTabListener = new OnClickListener()
    {
       @Override
       public void onClick(View v) 
       {
    	   //Create an intent for the stats view, and launch it
    	   Intent statsIntent = new Intent(Options.this, MainStats.class);
    	   Options.this.startActivity(statsIntent);
       } // end method onClick
    }; // end OnClickListener anonymous inner class
}
