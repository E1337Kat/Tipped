package com.pcpsoftware.tipped;

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
import java.sql.Timestamp;

public class MainStats extends Activity {
	
	private TextView statsHighestDate; //displays date of highest tip
	private TextView statsHighestAmount; //displays amount of highest tip
	private TextView statsLowestDate; //displays date of lowest tip
	private TextView statsLowestAmount; //displays amount of lowest tip
	private TextView statsAverageAmount; //displays amount of average tip
	//private long rowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stats);
        
        //get references to tabs and set listeners
        Button mainStatsShiftTab = (Button) findViewById(R.id.mainShiftsTab);
        mainStatsShiftTab.setOnClickListener(mainStatsShiftsTabListener);
        Button mainStatsOptionsTab = (Button) findViewById(R.id.mainOptionsTab);
        mainStatsOptionsTab.setOnClickListener(mainStatsOptionsTabListener);
        
        //get references to highest, lowest, and average date and amount editTexts
        statsHighestDate = (TextView) findViewById(R.id.mainStatsHighestDate);
        statsHighestAmount = (TextView) findViewById(R.id.mainStatsHighestAmount);
        statsLowestDate = (TextView) findViewById(R.id.mainStatsLowestDate);
        statsLowestAmount = (TextView) findViewById(R.id.mainStatsLowestAmount);
        statsAverageAmount = (TextView) findViewById(R.id.mainStatsAverageAmount);
        
    } //end method onCreate
    
    //called when the activity is first created
    @Override
    protected void onResume()
    {
    	super.onResume();
    	new LoadTipsTask().execute();
    } // end method onResume
    
    //performs database query outside GUI thread
    private class LoadTipsTask extends AsyncTask<Long, Object, Cursor>
    {
    	//DatabaseConnector databaseConnector = new DatabaseConnector(MainStats.this);
    	
    	// perform database access
    	@Override
    	protected Cursor doInBackground(Long...params)
    	{
    		
    		//databaseConnector.open();
    		
    		Tipped.getInstance().tipdb.open();
    		
    		return (Cursor) Tipped.getInstance().tipdb.getAllTips();
    	} //end doInBackground
    	
    	//use Cursor returned from doInBackground method
    	@Override
    	protected void onPostExecute(Cursor result)
    	{
    		super.onPostExecute(result);
    		
    		double currentTip;
    		
    		result.moveToFirst(); // move cursor to the first row
    		
    		//find highest tip
    		currentTip = result.getDouble(result.getColumnIndex("amount"));
    		double highestTip = currentTip;
    		//String highestDate = (new Timestamp(result.getLong(result.getColumnIndex("date")))).toString();
    		
    		// while cursor isn't pointing to position after last row
    		while (!result.isLast()) //TODO I think that's how you do that?
    		{
    			if (currentTip > highestTip)
    			{
    				highestTip = currentTip;
    				//highestDate = (new Timestamp(result.getLong(result.getColumnIndex("date")))).toString();
    			}
    			
    			result.moveToNext(); //move to next row
    			currentTip = result.getDouble(result.getColumnIndex("amount")); //get the next tip
    		} // end while loop
    		
    		
    		result.moveToFirst(); // move cursor to the first row
    		
    		// find lowest tip
    		currentTip = result.getDouble(result.getColumnIndex("amount"));
    		double lowestTip = currentTip;
    		//String lowestDate = (new Timestamp(result.getLong(result.getColumnIndex("date")))).toString();

    		while (!result.isLast())
    		{
 
    			if (lowestTip < currentTip)
    			{
    				lowestTip = currentTip;
    				//lowestDate = (new Timestamp(result.getLong(result.getColumnIndex("date")))).toString();
    			}
    			result.moveToNext(); //move to next row
    			currentTip = result.getDouble(result.getColumnIndex("amount")); //get the next tip
    			
    		} // end while loop
    		
    		result.moveToFirst(); // move cursor to the first row
    		int i = 1;
    		double tipSum = result.getDouble(result.getColumnIndex("amount"));
    		while (!result.isLast())
    		{
    			result.moveToNext(); //move to next row
    			tipSum += result.getColumnIndex("amount");
    			i++;
    		} // end while loop
    		
    		double averageTip = tipSum/i;
    		
    		result.close(); // close the cursor
    		//databaseConnector.close(); //close database connection 
    		
    		//statsHighestDate.setText(highestDate);
    		statsHighestAmount.setText(String.format("%.02f", highestTip));
    		//statsLowestDate.setText(lowestDate);
    		statsLowestAmount.setText(String.format("%.02f", lowestTip));
    		statsAverageAmount.setText(String.format("%.02f", averageTip));
    		
    	} //end method onPostExecute
      } //end LoadTipsTask
    
    // listener for main view button
    // switches to stats view
    public OnClickListener mainStatsShiftsTabListener = new OnClickListener()
    {
       @Override
       public void onClick(View v) 
       {
    	   //Create an intent for the stats view, and launch it
    	   Intent mainIntent = new Intent(MainStats.this, MainView.class);
    	   MainStats.this.startActivity(mainIntent);
       } // end method onClick
    }; // end OnClickListener anonymous inner class

    // listener for main view button
    // switches to stats view
    public OnClickListener mainStatsOptionsTabListener = new OnClickListener()
    {
       @Override
       public void onClick(View v) 
       {
    	   //Create an intent for the stats view, and launch it
    	   Intent optionsIntent = new Intent(MainStats.this, Options.class);
    	   MainStats.this.startActivity(optionsIntent);
       } // end method onClick
    }; // end OnClickListener anonymous inner class


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_shift_view, menu);
//        return true;
//    }
    
}
