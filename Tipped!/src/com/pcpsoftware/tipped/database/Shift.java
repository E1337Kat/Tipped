//Note: I'm not sure yet if it's going to be a better idea to use this class or just work with
//DatabaseConnector directly, but here it is anyway just in case
package com.pcpsoftware.tipped.database;

import java.util.Date; //for timestamp
import java.sql.Timestamp; //same as above

public class Shift {
	//member data
	private long id;
	private long date; //TODO: probably to be deprecated
	private Timestamp time; //Unix timestamp of shift creation
	private enum ShiftType {BREAK, LUNCH, DINNER};
	private int shiftType;
	private double total;
	private String notes;
	
	//Public constructor with args TODO: make this useful
	public Shift(Timestamp time, double total) {
		this.time = time;
		this.total = total;
	}
	
	//default constructor
	public Shift() {
		Date d = new Date();
		time = new Timestamp(d.getTime()); //create SQL timestamp from Date's Unix timestamp
		total = 0.0; //initialize tip total to 0
	}
	
	/* getter and setter for the shift id*/
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}/* end getter and setter for the shift id
	 * 
	 * 
	 * 
	 * getter and setter for the date*/
	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}/* end getter and setter for the date
	 * 
	 * 
	 * 
	 * getter and setter for the shift time*/
	public Timestamp getShiftTime() {
		return time;
	}
	
	public void setShiftTime(Timestamp time) {
		this.time = time;
	}/* end getter and setter for the shift time
	 * 
	 * 
	 * 
	 * getter and setter for the shift type*/
	public int getShiftType() {
		return shiftType;
	}
	
	public void setShiftType(ShiftType type) {
		switch (type) {
		case BREAK:
			this.shiftType = type.compareTo(type);
			break;
			
		case LUNCH:
			this.shiftType = type.compareTo(type);
			break;
			
		case DINNER:
			this.shiftType = type.compareTo(type);
			break;
			
		default:
			break;
		}
	}/* end getter and setter for the shift type
	 * 
	 * 
	 * 
	 * getter and setter for the total*/
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}/* end getter and setter for the total
	 * 
	 * 
	 * 
	 * getter and setter for any notes */	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}/* end getter and setter for any notes*/
} //end helper class Shift
