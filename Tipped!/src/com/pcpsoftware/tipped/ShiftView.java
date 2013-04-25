package com.pcpsoftware.tipped;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date; //for timestamp
import java.sql.Timestamp; //same as above

public class ShiftView extends ListActivity //implements LoaderCallbacks<Cursor> 
{
	double newTip;
	long shift_id; //shift we are working with
	long tip_id; //tip currently focused
	TipListAdapter tipAdapter; //cursor adapter for formatting ListView
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_view);
        
        Log.d("Trace","ShiftView.java onCreate() called");
        
        //retrieve the shift ID from MainView
        Bundle extras = getIntent().getExtras();
        shift_id = extras.getLong("shift_id");
            
        double shiftTotal = Tipped.getInstance().tipdb.getShiftTotal(shift_id);
        
        //Set up our list view for tips
        Tipped.getInstance().tipdb.open(); //open the database
        //Cursor tipCursor = Tipped.getInstance().tipdb.getAllTipsFromShift(shift_id);
        //startManagingCursor(tipCursor);
        
        
        tipAdapter = new TipListAdapter(this, null);
        setListAdapter(tipAdapter); //register our custom cursor adapter
        
        getListView().setOnItemClickListener(clickTipListener);	//set onItemClickListener for ListView

        //get references to add buttons and fake tabs
        //Button shiftTipsButton = (Button) findViewById(R.id.shiftTipsTab);
        Button shiftStatsButton = (Button) findViewById(R.id.shiftStatsTab);
        ImageButton addTipButton = (ImageButton) findViewById(R.id.addTipButton);
        Button closeShiftButton = (Button) findViewById(R.id.closeShiftButton);
        
        //set listeners for buttons
        shiftStatsButton.setOnClickListener(shiftStatsListener);
        addTipButton.setOnClickListener(addTipListener);
        closeShiftButton.setOnClickListener(closeShiftListener);
        
        //update total display
        TextView totalTextView = (TextView) findViewById(R.id.shiftTotalTextView);
        totalTextView.setText(String.format("%1$,.2f", shiftTotal));
    }
    
    @Override
    protected void onResume()
    {
    	Log.d("Trace","ShiftView.java onResume() called");
    	super.onResume();	// call super's onResume method
    }
    
     
    //***BEGIN LISTENER DEFINITIONS***

    
    public OnClickListener shiftStatsListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		
    		//TODO pass an id to statsIntent
    		
    		
    		//Create an intent for the stats view, and launch it
     	   Intent statsIntent = new Intent(ShiftView.this, ShiftStats.class);
     	   statsIntent.putExtra("shift_id", shift_id);
     	   ShiftView.this.startActivity(statsIntent);
    	}
    };
    
    // add a new edit text and button for user input.
    public OnClickListener addTipListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//Tip new_tip = new Tip(0,3.50,18.00,19.44,new Timestamp(new Date().getTime()),100,id); // create a new dummy tip TODO
    		Tip new_tip = new Tip(shift_id);	//create new blank tip
    		long tip_id = Tipped.getInstance().tipdb.addTip(new_tip); //add it to the database
    		
    		//let user edit this new tip
    		Intent tipIntent = new Intent(ShiftView.this, TipEdit.class);
      	   	tipIntent.putExtra("tip_id", tip_id); //this will pass the shift to the shift view
      	   	tipIntent.putExtra("shift_id", shift_id); //this is just so TipEdit can pass the shift id BACK to ShiftView (seems kinda stupid)
      	   	ShiftView.this.startActivity(tipIntent);

    	}
    };
    
    public OnClickListener closeShiftListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
							
			//launch intent to MainView
			Intent mainIntent = new Intent(ShiftView.this, MainView.class);
			ShiftView.this.startActivity(mainIntent);
    	}
    };
    
    public OnClickListener deleteTipListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		
    			// create an alert dialog so user does not accidentally delete a tip
	    		AlertDialog.Builder builder = 
	    				new AlertDialog.Builder(ShiftView.this);
	    		builder.setTitle(R.string.delete_title);
	    		builder.setMessage(R.string.delete_message);
	    		
	    		builder.setNegativeButton(R.string.cancel, null);
	    		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Tipped.getInstance().tipdb.deleteTip(tip_id); //delete tip form database
					}
				});
	    		builder.show();
    			//Tipped.getInstance().tipdb.deleteTip(tip_id); //delete tip form database

    	    	tipAdapter.changeCursor(Tipped.getInstance().tipdb.getAllTipsFromShift(shift_id)); //refresh cursor
    	    	
    	        //update total display
    	        TextView totalTextView = (TextView) findViewById(R.id.shiftTotalTextView);
    	        totalTextView.setText(String.format("%1$,.02f", Tipped.getInstance().tipdb.getShiftTotal(shift_id)));
    	        
    	    	tipAdapter.notifyDataSetChanged(); //refresh ListView 
    	}
    };
    
    
    public AdapterView.OnItemClickListener clickTipListener = new AdapterView.OnItemClickListener() {
    	@Override
    	public void onItemClick(AdapterView<?> parent, final View clicked,
    	          int position, long id)
    	{
    		//get the tip ID
    		Cursor c = (Cursor) parent.getItemAtPosition(position);
    		tip_id = c.getLong(c.getColumnIndex("_id"));
    		
    		//call the tip edit activity
    		Intent tipIntent = new Intent(ShiftView.this, TipEdit.class);
      	   	tipIntent.putExtra("tip_id", tip_id); //this will pass the shift to the shift view
      	   	tipIntent.putExtra("shift_id", shift_id); //this is just so TipEdit can pass the shift id BACK to ShiftView (seems kinda stupid)
      	   	ShiftView.this.startActivity(tipIntent);
      	   	
      	   	
      	   	Log.d("trace","Leaving clickTipListener");
    	}
    };
    
    //***END LISTENER DEFINITIONS***
    
    //***START LOADER METHODS***
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//    	//Uri uri = "Tipped.getInstance().tipdb.getAllTipsFromShift(shift_id)";
//    	CursorLoader loader = new CursorLoader(this.getBaseContext());
//    			this.getCallingActivity();
//    			Tipped.getInstance().tipdb.getAllTipsFromShift(shift_id),
//    			new String[] {"time", "amount"},
//    			null,
//    			null,
//    			null);
//    	loader.setUri(uri);
//    	loader.setProjection(new String[] {"time", "amount"});
//    	loader.setSelection(null);
//    	loader.setSelectionArgs(null);
//    	loader.setSortOrder(null);
//    	return loader;
//    }
//    
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//    	((TipListAdapter)this.getListAdapter()).swapCursor(cursor);
//    }
//    
//    public void onLoaderReset(Loader<Cursor> loader) {
//    	((TipListAdapter)this.getListAdapter()).swapCursor(null);
//    }
    //***END LOADER METHODS***
    
    //Create a custom CursorAdapter so we can format the ListView items
    public class TipListAdapter extends CursorAdapter {
        private final LayoutInflater mInflater;

        public TipListAdapter(Context context, Cursor cursor) {
            super(context, cursor, false);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
             return mInflater.inflate(R.layout.shift_view_item, parent, false);
             
        }
        
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) 
        {
        	
    		Log.d("trace","getView called");
    		
        	if(convertView == null)
        	{
        		convertView = mInflater.inflate(R.layout.shift_view_item, parent, false);
        	}

        	
        	 ImageButton deleteTipButton = (ImageButton) convertView.findViewById(R.id.newDeleteTipButton);
        	       	 
             deleteTipButton.setOnClickListener(new OnClickListener()
             {
            	 @Override
            	 public void onClick(View v) {
            		//get id of current tip
             		Cursor c = (Cursor) ((AdapterView<?>) parent).getItemAtPosition(position);
            		tip_id = c.getLong(c.getColumnIndex("_id"));
            		
         			Tipped.getInstance().tipdb.deleteTip(tip_id); //delete tip form database

         	    	tipAdapter.changeCursor(Tipped.getInstance().tipdb.getAllTipsFromShift(shift_id)); //refresh cursor
         	    	
         	        //update total display
         	        TextView totalTextView = (TextView) findViewById(R.id.shiftTotalTextView);
         	        totalTextView.setText(String.format("%1$,.2f", Tipped.getInstance().tipdb.getShiftTotal(shift_id)));
         	        
         	    	tipAdapter.notifyDataSetChanged(); //refresh ListView 
            	 }
             });
             
             return super.getView(position, convertView, parent);

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            long time = cursor.getLong(cursor.getColumnIndex("time"));
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);

            String format = "M/dd h:mm:ss a";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            String dateString = dateFormat.format(calendar.getTime());
            
            String amountString = "$ " + String.valueOf(amount);

            ((TextView) view.findViewById(R.id.newDateTextView)).setText(dateString);
            ((TextView) view.findViewById(R.id.newTipTextView)).setText(amountString);   
        } // end bindview method
    } // end TipListAdapter class
}
