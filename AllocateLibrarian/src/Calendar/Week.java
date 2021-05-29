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
	public boolean thisweek;
	public int year;
	public int month;
	public LocalDate monday;
	public String mondaystr;
	public String fridaystr;
	public Week previous;
	public Week next;
	public Day[] days; 
	
	public Week(LocalDate refdate) {
		LocalDate dinday = null;
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear(); 
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
	}
	
	public void createnext() {
		if (this.next == null) {
			this.next = new Week(this.monday.plusDays(7));
			this.next.previous = this;
		}
	}
	public void randemptoslots() {
		int days = 5;
		for (int i = 0; i < days; i++) {
			this.days[i].randemptoslots();
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
    	for (int i = 0; i < 5; i++) {
    		result += "\n" + this.days[i].toString();
    	}
    	return result;
    }
    	
}
