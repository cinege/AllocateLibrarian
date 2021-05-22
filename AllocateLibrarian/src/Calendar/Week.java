package Calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import Calendar.Enums.Employee;
import Calendar.Enums.Location;
import Calendar.Enums.Shift;

public class Week {
	public int ordinal;
	public boolean weeka;
	public int year;
	public int month;
	public LocalDate monday;
	public String mondaystr;
	public String fridaystr;
	public TTEntry[][][][] slots; 
	
	Week(LocalDate refdate) {
		LocalDate dinday = null;
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
		this.ordinal = refdate.get(woy);
		DateTimeFormatter f = DateTimeFormatter.ofPattern("M.d");
		int dayoftheweek = refdate.getDayOfWeek().getValue();
		this.weeka = (ChronoUnit.DAYS.between(LocalDate.of(2021, 5, 3), refdate.minusDays(dayoftheweek - 1)) / 7) % 2 == 0;
		this.monday = refdate.minusDays(dayoftheweek - 1);
		this.mondaystr = refdate.minusDays(dayoftheweek - 1).format(f);
		this.fridaystr = refdate.minusDays(dayoftheweek - 5).format(f);
		this.slots = new TTEntry[5][2][Location.values().length][3];
		for (int i = 0; i < 5; i++) {
			dinday = this.monday.plusDays(i);
			for (int j = 0; j < 2; j++) {
				for (Location l : Location.values()) {
					for (int k = 0; k < 3; k++) {
						this.slots[i][j][l.ordinal()][k] = new TTEntry(dinday.getMonthValue(), dinday.getDayOfMonth(), l, Shift.values()[i*2 + j]);	
					}
				}
			}
		}
	}
	
	public void randemptoslots() {
		// The employee cannot be at 2 locations at the same time and can't work 2 shifts in a day but one is compulsory.
		int days = 5;
		int loclength = Location.values().length - 1;
		int extloclength = loclength * 2;
		for (int i = 0; i < days; i++) {
			int[][] nshuffle = shuffle(extloclength);
			for (int j = 0; j < Employee.values().length; j++) {
				int eordinal = nshuffle[j / extloclength][j % extloclength];
				int day = i;
				int dedu = (eordinal / loclength) % 2;
				int loc = eordinal % loclength + 1;
				int hier = j / extloclength;
				this.slots[day][dedu][loc][hier].emp = Employee.values()[j];
			}
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
    		System.out.println(list);
    		for (int j = 0; j < count; j++) {
    			array[i][j] = list.get(j);
    		}
    	}
    	return array;
    }
    
    
    public String toString() {
    	String result = "";
    	for (Location l : Location.values()) {
    		result += l.name() + "|"; 
    	}
    	result += "\n";
    	for (int i = 0; i < 5; i++) {
    		result += "\n";
    		for (int j = 0; j < 2; j++) {
    			result += "\n";
    			for (int k = 0; k < 3; k++) {
    				result += "\n";
    				for (Location l : Location.values()) {
    					if (this.slots[i][j][l.ordinal()][k].emp != null) {
    						result += this.slots[i][j][l.ordinal()][k].emp + "|";
    					} else {
    						result += "000" + "|";
    					}
    				}
    	    	}
    		}		
    	}
    	return result;
    }
    	
}
