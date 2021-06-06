package Calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Calendar.Enums.Employees;
import Calendar.Enums.Locations;
import Calendar.Enums.Shifts;

public class Month {
	public LocalDate refdate;
	public int year;
	public int month;
	public List<Week> weeks;
	public Week thisweek;
	public List<Day> days;
	public Month(LocalDate refdate) {
		Week newweek = null;
		this.refdate = refdate;
		this.year = this.refdate.getYear();
		this.month = this.refdate.getMonthValue();
		LocalDate monthfirstday = refdate.minusDays(this.refdate.getDayOfMonth() - 1);
		int fpow = monthfirstday.getDayOfWeek().getValue();
		LocalDate firstrelweeksmonday = monthfirstday.plusDays(7 * ((fpow - fpow % 5) / 5));
		int weekcount = firstrelweeksmonday.plusDays(7*5).getMonth().getValue() == this.month ? 5 : 4;
		this.weeks = new ArrayList<Week>();
		Week prevweek = null;
		for (int i = 0; i < weekcount; i++) {
			newweek = new Week(firstrelweeksmonday.plusDays(i * 7));
			this.weeks.add(newweek);
			if (prevweek != null) {prevweek.next = newweek;}
			newweek.previous = prevweek;
			prevweek = newweek;
			if (newweek.thisweek) {this.thisweek = newweek;}
		}

		this.days = new ArrayList<Day>();
		for (Week week : this.weeks) {
			for (Day day : week.days) {
				if (day.thisd.getMonth().getValue() == this.month) {
					this.days.add(day);
				}
			}
		}
	}
	
	public int evshiftcount(Employees emp) {
		int  count = 0;
		for (Day day : this.days) {
			for (int i = 0; i < Locations.values().length; i++) {
				for (int j = 0; j < 3; j++) {
					if (day.slots[1][i][j].emp != null && day.slots[1][i][j].emp.equals(emp)) {count++;}
				}
			}
		}
		return count;
	}
	
	public void randomize( ) {
		for (Week week : this.weeks ) {
			week.randemptoslots();
		}
	}
	@Override
	public String toString() {
		String result = "";
    	result += "\n";
    	for (Week week : this.weeks) {
    		result += "\n" + week.toString();
    	}
    	return result;
	}
}
