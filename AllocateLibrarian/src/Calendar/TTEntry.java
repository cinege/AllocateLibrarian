package Calendar;
import java.util.ArrayList;

import Calendar.Enums.*;

public class TTEntry {
	public int month;
	public int day;
	public Location location;
	public Shift shift;
	public Employee emp;

	
	public TTEntry (int month, int day, Location location, Shift shift) {
		this.month = month;
		this.day = day;
		this.location = location;
		this.shift = shift;
	}
	public TTEntry clone() {
		TTEntry newentry = new TTEntry(this.month, this.day, this.location, this.shift);
		return newentry;
	}
	
	@Override
	public String toString() {
		String temp = "";
		temp += this.month + "-" + this.day + "," + this.location + "," + this.shift + "," + this.emp;
		return temp.substring(0, temp.length()-1);
	}
}
