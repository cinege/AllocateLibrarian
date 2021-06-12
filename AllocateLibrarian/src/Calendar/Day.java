package Calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Calendar.Enums.Locations;
import Calendar.Enums.Shifts;
import Personal.Staff;

public class Day {
	public LocalDate thisd;
	public boolean today;
	public int weekday;
	public TTEntry[][][] slots;
	Day (LocalDate thisd) {
		this.thisd = thisd;
		this.weekday = this.thisd.getDayOfWeek().getValue() - 1;
		this.today = this.thisd == LocalDate.now();
		this.slots = new TTEntry[2][Locations.values().length][3];
		for (int i = 0; i < 2; i++) {
			for (Locations l : Locations.values()) {
				for (int k = 0; k < 3; k++) {
					this.slots[i][l.ordinal()][k] = new TTEntry(
						this.thisd.getMonthValue(), 
						this.thisd.getDayOfMonth(), 
						l, 
						Shifts.values()[(this.weekday) * 2 + i]
					);	
				}
			}
		}
	}
	
	public void randemptoslots(Staff staff) {
		// The employee cannot be at 2 locations at the same time and can't work 2 shifts in a day but one is compulsory.
		int loclength = Locations.values().length - 1;
		int extloclength = loclength * 2;
		
		int[][] nshuffle = shuffle(extloclength);
		for (int j = 0; j < staff.employees.size(); j++) {
			int eordinal = nshuffle[j / extloclength][j % extloclength];
			int dedu = (eordinal / loclength) % 2;
			int loc = eordinal % loclength + 1;
			int hier = j / extloclength;
			this.slots[dedu][loc][hier].emp = staff.employees.get(j);
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
    	for (Locations l : Locations.values()) {
    		result += l.name() + "|"; 
    	}
    	
    	result += "\n";
    	for (int j = 0; j < 2; j++) {
    		result += "\n";
    		for (int k = 0; k < 3; k++) {
    			result += "\n";
    			for (Locations l : Locations.values()) {
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
