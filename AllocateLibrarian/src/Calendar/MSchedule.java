package Calendar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;

import Calendar.Enums.Employee;
import Calendar.Enums.Location;
import Calendar.Enums.Shift;

public class MSchedule {
	public ArrayList<TTEntry> days = new ArrayList<TTEntry>();
	public HashMap<String, HashMap<Location, ArrayList<Employee>>> monthoverview = new HashMap<String, HashMap<Location, ArrayList<Employee>>>();
			
	public void genMonthOverview() {
		String monthdayshift = "";
		HashMap<Location, ArrayList<Employee>> tmphashmap = null;
		for (TTEntry entry : this.days) {
			monthdayshift = String.valueOf(entry.month) + "-" + String.valueOf(entry.day) + "-" + String.valueOf(entry.shift);
			
			if (!monthoverview.containsKey(monthdayshift)) {
				HashMap<Location, ArrayList<Employee>> newdailysched = new HashMap<Location, ArrayList<Employee>>();
				if(!newdailysched.containsKey(entry.location)) {newdailysched.put(entry.location, entry.emps);}
				monthoverview.put(monthdayshift, newdailysched);
			} else {
				tmphashmap = monthoverview.get(monthdayshift);
				if(!tmphashmap.containsKey(entry.location)) {tmphashmap.put(entry.location, entry.emps);}
			}
		}
	}
		
	public void randomize(int year, int month) {
		Random rand = new Random();
		int randint = 0;
		int weekday = 0;
		YearMonth cal = YearMonth.of(year, month);
		int monthlength = cal.lengthOfMonth();
		LocalDate ldate = null;
		// Create stations and time slots on a month
		for (int i = 1; i <= monthlength; i++) {
			ldate = LocalDate.of(year, month, i);
			weekday = ldate.getDayOfWeek().getValue() - 1;
			for (int k = 0; k < 2; k++) {
				for (Location l : Location.values()) {
					this.days.add(new TTEntry(month, i, l, Shift.values()[weekday*2 + k]));
				}
			}
		}
		// 
		for (int i = 1; i <= monthlength; i++) {
			ldate = LocalDate.of(year, month, i);
			weekday = ldate.getDayOfWeek().getValue() - 1;
			if (weekday < 5) {
				tEmpList = new ArrayList<Employee>();
				for (Employee emp : Employee.values()) {
					tEmpList.add(emp);
				}
				boolean newflag = true;
				while (tEmpList.size() > 0) {
					for (int k = 0; k < 2; k++) {
						for (int j = 0; j < Location.values().length; j++) {
							tmpentry = new TTEntry(month, i, Location.values()[j], Shift.values()[weekday*2 + k]);
							if (tEmpList.size() > 0 && j > 0) {
								randint = rand.nextInt(tEmpList.size());
								tEmployee = tEmpList.get(randint);
								tEmpList.remove(randint);
								tmpentry.emps.add(tEmployee);
							} 
							this.days.add(tmpentry);
						}
					}
				}
			}
		}
	}
	
	public void writeCSV() {
		try {
			Writer fileWriter = new FileWriter("c:\\temp\\OIK.csv", false);
			for (TTEntry entry : days) {
				fileWriter.write(entry.toString() + "\n");
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readCSV() {
		BufferedReader reader;
		TTEntry tmpentry = null;
		String monthday = "";
		int month;
		int day;
		Location loc = null;
		Shift shift = null;
		ArrayList<Employee> emps = new ArrayList<Employee>();
		try {
			reader = new BufferedReader(new FileReader("c:\\temp\\OIK.csv"));
			String line = reader.readLine();
			while (line != null) {
				monthday = line.split(",")[0];
				month = Integer.valueOf(monthday.split("-")[0]);
				day = Integer.valueOf(monthday.split("-")[1]);
				loc = Location.valueOf(line.split(",")[1]);
				shift = Shift.valueOf(line.split(",")[2]);
				emps.clear();
				for(String tmp : line.split(",")[3].split(":")) {
					emps.add(Employee.valueOf(tmp));
				}
				
				tmpentry = new TTEntry( month, day, loc, shift);
				tmpentry.emps.addAll(emps);
				days.add(tmpentry);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printdays() {
		for (TTEntry entry : days) {
			//System.out.println(entry.toString());
		}
	}
	
	public void printmonth(int year, int month) {
		
		System.out.print("Mikor,");
		for (Location location: Location.values()) {
			System.out.print(location + ",");
		}
		System.out.println("");
		
		YearMonth cal = YearMonth.of(year, month);
		int monthlength = cal.lengthOfMonth();
		String[] d = new String[2];
		for (int i = 1; i <= monthlength; i++) {
			LocalDate ldate = LocalDate.of(year, month, i);
			if (ldate.getDayOfWeek().getValue() < 6) {
				d[0] = String.valueOf(month) + "-" + String.valueOf(i) + "DE";
			    d[1] = String.valueOf(month) + "-" + String.valueOf(i) + "DU";
			    //System.out.println(this.monthoverview.toString());
			    for (int j = 0; j < 2; j++) {
			    	System.out.print(d[j] + ",");
			    	for (Location loc : Location.values()) {System.out.print(this.monthoverview.get(d[j]).get(loc).toString() + ",");}
			    	System.out.println("");
			    	
			    }
			}
			
		}
		
	}
	
	public int evshiftcount(Employee emp) {
		int  i = 0;
		for (TTEntry entry : this.days) {
			if (entry.emps.contains(emp) && entry.shift.equals(Shift.Hetfo_DU)) {
				i++;
			}
		}
		return i;
	}
}
