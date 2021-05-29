package Calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Calendar.Enums.Employee;
import Calendar.Enums.Location;
import Calendar.Enums.Shift;

public class Day {
	public LocalDate thisd;
	public boolean today;
	public int weekday;
	public TTEntry[][][] slots;
	Day (LocalDate thisd) {
		this.thisd = thisd;
		this.weekday = this.thisd.getDayOfWeek().getValue() - 1;
		this.today = this.thisd == LocalDate.now();
		this.slots = new TTEntry[2][Location.values().length][3];
		for (int i = 0; i < 2; i++) {
			for (Location l : Location.values()) {
				for (int k = 0; k < 3; k++) {
					this.slots[i][l.ordinal()][k] = new TTEntry(
						this.thisd.getMonthValue(), 
						this.thisd.getDayOfMonth(), 
						l, 
						Shift.values()[(this.weekday) * 2 + i]
					);	
				}
			}
		}
	}
	
	public void randemptoslots() {
		// The employee cannot be at 2 locations at the same time and can't work 2 shifts in a day but one is compulsory.
		int loclength = Location.values().length - 1;
		int extloclength = loclength * 2;
		
		int[][] nshuffle = shuffle(extloclength);
		for (int j = 0; j < Employee.values().length; j++) {
			int eordinal = nshuffle[j / extloclength][j % extloclength];
			int dedu = (eordinal / loclength) % 2;
			int loc = eordinal % loclength + 1;
			int hier = j / extloclength;
			this.slots[dedu][loc][hier].emp = Employee.values()[j];
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
	
	public String toString() {
    	String result = "";
    	for (Location l : Location.values()) {
    		result += l.name() + "|"; 
    	}
    	
    	result += "\n";
    	for (int j = 0; j < 2; j++) {
    		result += "\n";
    		for (int k = 0; k < 3; k++) {
    			result += "\n";
    			for (Location l : Location.values()) {
    				if (this.slots[j][l.ordinal()][k].emp != null) {
    					result += this.slots[j][l.ordinal()][k].emp + "|";
    				} else {
    					result += "000" + "|";
    				}
    			}
    	   	}
    	}		
    	return result;
    }
}
