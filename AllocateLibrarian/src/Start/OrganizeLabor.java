package Start;


import Calendar.Enums.Employees;
import Calendar.MSchedule;

public class OrganizeLabor {

	public static void main(String[] args) {
		//String yearmonth = "2021.05";
		MSchedule schedule = new MSchedule();
		//schedule.randomize(2021,5);
		//schedule.writeCSV();
		schedule.readCSV();
		schedule.printdays();
		//for (Employee emp : Employee.values()) {
		//	System.out.println(String.valueOf(emp) + String.valueOf(schedule.evshiftcount(emp)));
		//
		schedule.genMonthOverview();
		schedule.printmonth(2021,5);
	}
		


}
