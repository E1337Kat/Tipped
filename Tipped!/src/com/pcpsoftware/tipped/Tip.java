//Note: I'm not sure yet if it's going to be a better idea to use this class or just work with
//DatabaseConnector directly, but here it is anyway just in case
package com.pcpsoftware.tipped;

import java.util.Date; //for timestamp
import java.sql.Timestamp; //same as above

public class Tip {
	//private member data goes here
	private long id;
	private double amount;
	private double total;
	private double percent;
	private Timestamp time; //Unix timestamp of tip creation
	private int check_num;
	private String notes;
	private long shift_id;
	
	//shortform public constructor
	public Tip(long shift_id)
	{
		Date d = new Date();
		this.shift_id = shift_id;
		amount = 0.0;
		total = 0.0;
		percent = 0.0;
		time = new Timestamp(d.getTime());
		check_num = 0;		
	}
	
	//full public constructor
	public Tip(long id, double amount, double total, double percent, Timestamp time, int check_num, long shift_id)
	{
		this.id = id;
		this.amount = amount;
		this.total = total;
		this.percent = percent;
		this.time = time;
		this.check_num = check_num;
		this.shift_id = shift_id;
	}
	
	//accessors and mutators
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public double getAmount()
	{
		return amount;
	}
	
	public void setAmount(double amount)
	{
		this.amount = amount;
	}
	
	public double getTotal()
	{
		return total;
	}
	
	public void setTotal(double total)
	{
		this.total = total;
	}
	
	public double getPercent()
	{
		return percent;
	}
	
	public void setPercent(double percent)
	{
		this.percent = percent;
	}
	
	public Timestamp getTipTime()
	{
		return time;
	}
	
	public void setTipTime(Timestamp time)
	{
		this.time = time;
	}
	
	public int getCheckNum()
	{
		return check_num;
	}
	
	public void setCheckNum(int check_num)
	{
		this.check_num = check_num;
	}
	
	
	public String getNotes()
	{
		return notes;
	}
	
	public void setNotes(String notes)
	{
		this.notes = notes;
	}
	
	public long getShiftId()
	{
		return shift_id;
	}
	
	public void setShiftId(long shift_id)
	{
		this.shift_id = shift_id;
	}
	
}
