package com.pcpsoftware.tipped;


import java.sql.Timestamp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShiftStats extends Activity {

	private TextView statsHighestDate; //displays date of highest tip
	private TextView statsHighestAmount; //displays amount of highest tip
	private TextView statsLowestDate; //displays date of lowest tip
	private TextView statsLowestAmount; //displays amount of lowest tip
	private TextView statsAverageAmount; //displays amount of average tip
	double highestTip;
	double lowestTip;
	double tipSum;
	double averageTip;
	Timestamp highestDate;
	Timestamp lowestDate;
	long shift_id; // Shift we are currently working with
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_stats);
        
        //retrieve the shift ID from MainView
        Bundle extras = getIntent().getExtras();
        shift_id = extras.getLong("shift_id");
        
        //get references to tabs and set listeners
        Button shiftStatsShiftTab = (Button) findViewById(R.id.shiftsStatsShiftTab);
        shiftStatsShiftTab.setOnClickListener(shiftStatsShiftsTabListener);
        
        //get references to highest, lowest, and average date and amount editTexts
        statsHighestAmount = (TextView) findViewById(R.id.shiftStatsHighestAmount);
        statsHighestDate = (TextView) findViewById(R.id.shiftStatsHighestDate);
        statsLowestAmount = (TextView) findViewById(R.id.shiftStatsLowestAmount);
        statsLowestDate = (TextView) findViewById(R.id.shiftStatsLowestDate);
        statsAverageAmount = (TextView) findViewById(R.id.shiftStatsAverageAmount);        
    } //end onCreate method
    
    //called when the activity is first created
    @Override
    protected void onResume()
    {
    	super.onResume();
    	new LoadTipsTask().execute(shift_id);
    } //end method onResume
    
    //performs database query outside GUI thread
    private class LoadTipsTask extends AsyncTask<Long, Object, Cursor>
    {
    	// perform database access
    	@Override
    	protected Cursor doInBackground(Long...shifts)
    	{
    		double currentTip;
    		
    		// open the database
    		Tipped.getInstance().tipdb.open();
    		
    		// get the tips from the shift
    		Cursor tipsCursor = (Cursor) Tipped.getInstance().tipdb.getAllTipsFromShift(shifts[0]);
    		
    		// move cursor to the first row
    		tipsCursor.moveToFirst();
    		
    		//find highest tip
    		currentTip = tipsCursor.getDouble(tipsCursor.getColumnIndex("amount"));
    		highestTip = currentTip;
    		highestDate = new Timestamp(tipsCursor.getLong(tipsCursor.getColumnIndex("time")));
    		
    		// while amount column is not empty
    		while (!tipsCursor.isLast()) //TODO I think that's how you do that?
    		{
    			if (currentTip > highestTip)
    			{
    				highestTip = currentTip;
    				highestDate = new Timestamp(tipsCursor.getLong(tipsCursor.getColumnIndex("date")));
    			}
    			
    			tipsCursor.moveToNext(); //move to next row
    			currentTip = tipsCursor.getDouble(tipsCursor.getColumnIndex("amount")); //get the next tip
    		} // end while loop
    		
    		tipsCursor.moveToFirst(); // move cursor to the first row
    		
    		// find lowest tip
    		currentTip = tipsCursor.getDouble(tipsCursor.getColumnIndex("amount"));
    		lowestTip = currentTip;
    		lowestDate = new Timestamp(tipsCursor.getLong(tipsCursor.getColumnIndex("date")));
    		
    		// while amount column is not empty
    		while (!tipsCursor.isLast())
    		{
 
    			if (lowestTip < currentTip)
    			{
    				lowestTip = currentTip;
    				lowestDate = new Timestamp(tipsCursor.getLong(tipsCursor.getColumnIndex("date")));
    			}
    			tipsCursor.moveToNext(); //move to next row
    			currentTip = tipsCursor.getDouble(tipsCursor.getColumnIndex("amount")); //get the next tip
    			
    		} // end while loop
    		
    		tipsCursor.moveToFirst(); // move cursor to the first row
    		int i = 1;
    		tipSum = tipsCursor.getDouble(tipsCursor.getColumnIndex("amount"));
    		while (!tipsCursor.isAfterLast())
    		{
    			tipsCursor.moveToNext(); //move to next row
    			tipSum += tipsCursor.getColumnIndex("amount");
    			i++;
    		} // end while loop
    		
    		averageTip = tipSum/i;
    		
    		return tipsCursor;
    	} //end doInBackground
		
    	//use Cursor returned from doInBackground method
    	@Override
    	protected void onPostExecute(Cursor result)
    	{
    		//super.onPostExecute(result);
    		
    		result.close(); // close the cursor
    		Tipped.getInstance().tipdb.close(); //close database connection 
    		
    		//statsHighestDate.setText(highestDate);
    		statsHighestAmount.setText(String.format("%.02f", highestTip));
    		//statsLowestDate.setText(lowestDate);
    		statsLowestAmount.setText(String.format("%.02f", lowestTip));
    		statsAverageAmount.setText(String.format("%.02f", averageTip));
    	}

    } //end LoadTipsTask
    
    // listener for main view button
    // switches to stats view
    public OnClickListener shiftStatsShiftsTabListener = new OnClickListener()
    {
       @Override
       public void onClick(View v) 
       {
    	   //Create an intent for the stats view, and launch it
    	   Intent shiftIntent = new Intent(ShiftStats.this, ShiftView.class);
    	   shiftIntent.putExtra("shift_id", shift_id); // pass shift_id back to shift view.
    	   ShiftStats.this.startActivity(shiftIntent);
       } // end method onClick
    }; // end OnClickListener anonymous inner class

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_shift_view, menu);
//        return true;
//    }
    
}
