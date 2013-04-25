package com.pcpsoftware.tipped;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import java.util.Date; //for timestamp
import java.sql.Timestamp; //same as above
import android.util.Log;

public class TipEdit extends Activity {
	
			long id; //id of the tip we are editing
			long shift; //id of the shift of the tip we are editing
			Tip tip; //Tip object of the tip we are working with
			EditText amountEditText;
			EditText totalEditText;
			EditText checkEditText;
	
	  @Override protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tip_edit);
	        
	        //retrieve the shift ID from MainView
	        Bundle extras = getIntent().getExtras();
	        id = extras.getLong("tip_id");
	        shift = extras.getLong("shift_id");
	        
	        //DEBUG: print  tip id
	        Log.d("TipIdValue","Tip ID: " + Long.toString(id));
	        
	        //get references to editTexts
	        amountEditText = (EditText) findViewById(R.id.amountEditText);
	        totalEditText = (EditText) findViewById(R.id.totalEditText);
	        checkEditText = (EditText) findViewById(R.id.checkEditText);	        
	        
	        //get references to button
	        Button doneButton = (Button) findViewById(R.id.doneButton);
	        
	        //set listeners for buttons
	        doneButton.setOnClickListener(doneListener);
	        
    		tip = Tipped.getInstance().tipdb.getTipObj(id); //create a tip object from the database
    		
    		
//    		checkEditText.setText(String.valueOf(tip.getAmount()));
//    		checkEditText.setText(String.valueOf(tip.getTotal()));
//    		checkEditText.setText(String.valueOf(tip.getCheckNum()));
    		
    		//repopulate text fields with tip's values if not equal to zero
    		if (tip.getAmount() != 0.0 ) {
    			String tipAmount = Double.toString(tip.getAmount());
    			amountEditText.setText(tipAmount);
    		}
    		if (tip.getTotal() != 0.0 ) {
    			String totalAmount = Double.toString(tip.getTotal());
    			totalEditText.setText(totalAmount);
    		}
    		if (tip.getCheckNum() != 0) {
    			String checkNumber = Integer.toString(tip.getCheckNum());
    			checkEditText.setText(checkNumber);   // checkEditText.setText(String.valueOf(tip.getCheckNum()));
    		}
    		
	  }
	  
	  public OnClickListener doneListener = new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		
	    		    		
	    		//get input to build new tip
	    		if(amountEditText.getText().length() > 0)
	    		{
	                tip.setAmount(Double.parseDouble(amountEditText.getText().toString()));
	                //amountEditText.setText(""); // clear entry
	    		}
	    		else
	    		{
	    			tip.setAmount(0); //just give default value if none provided
	    		}
	    		
	    		if(totalEditText.getText().length() > 0)
	    		{
	    			tip.setTotal(Double.parseDouble(totalEditText.getText().toString()));
	    		}
	    		else
	    		{
	    			tip.setTotal(0);
	    		}
	    		
	    		if(checkEditText.getText().length() > 0)
	    		{
	    			tip.setCheckNum(Integer.parseInt(checkEditText.getText().toString()));
	    		}
	    		else
	    		{
	    			tip.setCheckNum(0);
	    		}
	    		
	             
	                // hide the soft keyboard TODO necessary?
	                ((InputMethodManager) getSystemService(
	                   Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
	                   amountEditText.getWindowToken(), 0);
	    		
	    		Tipped.getInstance().tipdb.updateTip(tip);	//update tip entry in database
	    		
	    		//go back to tip view
	    		Intent shiftIntent = new Intent(TipEdit.this, ShiftView.class);
	      	   	shiftIntent.putExtra("shift_id",  shift); //give ShiftView back its shift id
	      	   	TipEdit.this.startActivity(shiftIntent);
	    	}
	  };
	  
	  
}
