package Calendar;

import java.time.LocalDate;

import Calendar.Enums.Employee;

public class monthtester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDate now = LocalDate.now();
		Month month = new Month(now);
		month.randomize();
		System.out.println(month.evshiftcount(Employee.E04));
	}

}
