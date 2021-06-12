package Calendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import Calendar.Enums.Holidays;
import Calendar.Enums.Locations;
import Calendar.Enums.Roles;
import Personal.Person;
import Personal.Staff;

public class Week {
	public Staff staff;
	public int ordinal;
	public boolean weeka;
	public boolean thisweek;
	public int year;
	public int month;
	public LocalDate monday;
	public String mondaystr;
	public String fridaystr;
	public Week previous;
	public Week next;
	public Day[] days;
	public String path;
	public String csvfilename;
	
	public Week(LocalDate refdate, String path, boolean force) {
		LocalDate dinday = null;
		TemporalField woy = WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear(); 
		this.staff = new Staff();
		this.ordinal = refdate.get(woy);
		DateTimeFormatter f = DateTimeFormatter.ofPattern("M.d");
		int dayoftheweek = refdate.getDayOfWeek().getValue();
		this.weeka = (ChronoUnit.DAYS.between(LocalDate.of(2021, 5, 3), refdate.minusDays(dayoftheweek - 1)) / 7) % 2 == 0;
		this.thisweek = this.ordinal == LocalDate.now().get(woy); 
		this.monday = refdate.minusDays(dayoftheweek - 1);
		this.year = this.monday.getYear();
		this.month = this.monday.getMonthValue();
		this.mondaystr = refdate.minusDays(dayoftheweek - 1).format(f);
		this.fridaystr = refdate.minusDays(dayoftheweek - 5).format(f);
		this.days = new Day[5];
		for (int i = 0; i < 5; i++) {
			dinday = this.monday.plusDays(i);
			this.days[i] = new Day(dinday);
		}
		this.path = path;
		this.csvfilename = "beosztas" + String.valueOf(this.year) + "-" + String.valueOf(this.ordinal) + "-" + (this.weeka ? "A" : "B") + "-" + this.mondaystr + "-" + this.fridaystr + ".csv";
		String filepath1 = this.path + csvfilename;
		String filepath2 = this.path + (this.weeka ? "A" : "B") + "-sablon.csv";
		File file1 = new File(filepath1);
		File file2 = new File(filepath2);
		if(file1.exists() && !file1.isDirectory()) { 
			processcsvcontent(loadcsvcontent(filepath1));
		} else if(file2.exists() && !file2.isDirectory() && force){
			processcsvcontent(loadcsvcontent(filepath2));
		}
		
	}
	
	public void createnext(boolean force) {
		if (this.next == null) {
			this.next = new Week(this.monday.plusDays(7), this.path, force);
			this.next.previous = this;
		}
	}
	
	public void createprev(boolean force) {
		if (this.previous == null) {
			this.previous = new Week(this.monday.minusDays(7), this.path, force);
			this.previous.next = this;
		}
	}
	
	public String[] loadcsvcontent(String filepath) {
		
		String[] lines = new String[31];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filepath));
			int i = 0;
			String line = reader.readLine();
			while (line != null) {
				lines[i] = line;
				i++;
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	
	public void processcsvcontent(String[] lines){
		//boolean modification = false;
		Person newempdata;
		String roles;
		//List<Roles> newroles = new ArrayList<Roles>(); 
		if (lines[0] !=null) {
		String[] employees = lines[0].split(",");
			for (String e : employees ) {
				if (e.length() > 0) {
					this.staff.employees.add(new Person(e));
				}
			}
		}
		if (lines[1] !=null) {
			for (int m = 0; m < 30; m++) {
				int i = m / 6;
				int j = (m / 3) % 2;
				int l = m % 3;
				String[] locs = new String[Locations.values().length];
				locs = lines[m + 1].split(",");
				for  (int k = 0; k < Locations.values().length; k++) {
					if (locs[k].equals("-")) {
						newempdata = null;
						roles = "";
					} else {
						newempdata = this.staff.getperson(locs[k].split("\\|")[0]);
						roles  = locs[k].split("\\|").length == 1 ? "" : locs[k].split("\\|")[1];
					}
					boolean diffinroles = getrolediff(roles, this.days[i].slots[j][k][l].holiday, this.days[i].slots[j][k][l].roles);
					if (this.days[i].slots[j][k][l].emp != newempdata || diffinroles) {
						//modification = true;
						if (k == 0) {
							this.days[i].slots[j][k][l].holiday = Enums.getholiday(roles);
							this.days[i].slots[j][k][l].roles.clear();
						} else {
							this.days[i].slots[j][k][l].roles.clear();
							for (int o = 0; o < roles.length(); o++) {
								this.days[i].slots[j][k][l].roles.add(Enums.getrole(roles.substring(o,o + 1)));
							}
							this.days[i].slots[j][k][l].holiday = null;
						}
						this.days[i].slots[j][k][l].emp = newempdata;
					}	 	
				}
			}
		}
	}
	
	public void writetocsv(String filepath) {
		
		try {
			Writer fileWriter = new FileWriter(filepath, false);
			String[] lines = collectweekdata();
			for (String line : lines) {
				String c = line == null ? "" : line;
				fileWriter.write(c);
			}
			fileWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] collectweekdata() {
		Person emp;
		String roles = "";
		String estr;
		String newline;
		String[] result = new String[31];
		result[0] = this.staff.toString() + "\n";
		for (int i = 0; i < 5; i++) {
			for  (int j = 0; j < 2; j++) {
				for  (int l = 0; l < 3; l++) {
					newline = "";
					for  (int k = 0; k < Locations.values().length; k++) {
						emp = this.days[i].slots[j][k][l].emp;
						if (emp != null) {
							roles = "";
							for (Roles r : this.days[i].slots[j][k][l].roles) {
								roles += r.name().substring(0,1);
							}
							if (k == 0 && this.days[i].slots[j][k][l].holiday != null) {
								roles += this.days[i].slots[j][k][l].holiday.name().substring(0,1);
							}
							estr = emp.name + "|" + roles;
						} else {
							estr =  "-";
						}
						newline += estr + ",";
					}
					newline = newline.substring(0, newline.length()-1) + "\n";
					result[i*6 + j*3 + l + 1] = newline;
				}
	    	}   
		}
		return result;
	}
	
	public void remove(String name) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < Locations.values().length; k++) {
					for (int l = 0; l < 3; l++) {
						Person p = this.days[i].slots[j][k][l].emp;
						if (p != null && p.name.equals(name)) {
							this.days[i].slots[j][k][l].emp = null;
						}
					}
				}
			}
		}
	}

	public void randemptoslots(Staff staff) {
		int days = 5;
		for (int i = 0; i < days; i++) {
			this.days[i].randemptoslots(staff);
		}
	}
	
    public int[][] shuffle (int count) {
    	int[][] array = new int[3][count];
    	for (int i = 0; i < 3; i++) {
    		List<Integer> list = new ArrayList<>();
    		for (int j = 0; j < count; j++) {
    			list.add(j);
    		}
    		Collections.shuffle(list);
    		for (int j = 0; j < count; j++) {
    			array[i][j] = list.get(j);
    		}
    	}
    	return array;
    }
    
    boolean getrolediff(String saved, Holidays h, List<Roles> roles) {
		String ref = h != null ? h.name().substring(0,1) : "";
		for (Roles r : roles) {
			ref += r.name().substring(0,1);
		}
		return ref.equals(saved);
	}
    
    public String toString() {
    	String result = "";
    	for (Locations l : Locations.values()) {
    		result += l.name() + "|"; 
    	}
    	result += "\n";
    	for (int i = 0; i < 5; i++) {
    		result += "\n" + this.days[i].toString();
    	}
    	return result;
    }
}
