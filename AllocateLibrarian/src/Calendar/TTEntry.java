package Calendar;
import java.util.ArrayList;
import java.util.List;

import Calendar.Enums.*;
import Personal.Person;

public class TTEntry {
	public int month;
	public int day;
	public Locations location;
	public Shifts shift;
	public Person emp;
	public List<Roles> roles;
	public Holidays holiday;

	
	public TTEntry (int month, int day, Locations location, Shifts shift) {
		this.month = month;
		this.day = day;
		this.location = location;
		this.shift = shift;
		this.roles = new ArrayList<Roles>();
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
