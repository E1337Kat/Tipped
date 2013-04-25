//Note: I'm not sure yet if it's going to be a better idea to use this class or just work with
//DatabaseConnector directly, but here it is anyway just in case
package com.pcpsoftware.tipped;

import java.util.Date; //for timestamp
import java.sql.Timestamp; //same as above

public class Shift
{
	//member data
	private long id;
	private int date; //TODO: probably to be deprecated
	private Timestamp time; //Unix timestamp of shift creation
	private double total;
	private String notes;
	
	//Public constructor with args TODO: make this useful
	public Shift(Timestamp time, double total)
	{
		this.time = time;
		this.total = total;
	}
	
	//default constructor
	public Shift()
	{
		Date d = new Date();
		time = new Timestamp(d.getTime()); //create SQL timestamp from Date's Unix timestamp
		total = 0.0; //initialize tip total to 0
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public int getDate()
	{
		return date;
	}
	
	public void setDate(int date)
	{
		this.date = date;
	}
	
	public Timestamp getTime()
	{
		return time;
	}
	
	public void setTime(Timestamp time)
	{
		this.time = time;
	}
	
	public double getTotal()
	{
		return total;
	}
	
	public void setTotal(double total)
	{
		this.total = total;
	}
	
	public String getNotes()
	{
		return notes;
	}
	
	public void setNotes(String notes)
	{
		this.notes = notes;
	}
	
}
