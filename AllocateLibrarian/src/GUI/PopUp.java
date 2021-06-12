package GUI;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import Calendar.Week;
import Calendar.Enums.Locations;
import Personal.Person;

public class PopUp extends SelectionAdapter{
	GUI gui;
	String type;
	boolean flag;
	
	PopUp(GUI gui, String type){
		this.gui = gui;
		this.type = type;
		this.flag = true;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		String message;
		String title;
		if (this.type.equals("premium")) {
			title = "Delutanosok";
			message = getpremium();
		} else {
			title = "Nincsenek beosztva";
			message = getidle();
		}
		
		this.flag = this.flag ? false : true;
		openpopup(title, message);
	}
	
	String getpremium() {
		boolean dualmonth = this.gui.week.monday.getMonthValue() != this.gui.week.monday.plusDays(4).getMonthValue();
		int month = this.flag ? this.gui.week.month : (dualmonth ? (this.gui.week.month + 1) : this.gui.week.month);
		month = month == 13 ? 1 : month;
		System.out.println(month);
		Week iter;
		int[] relweeks = getrelweeks(this.gui.week.year, month);
		int first = relweeks[0];
		int curr = this.gui.week.ordinal;
		int last = relweeks[1];
		int weekcount = relweeks[1] - relweeks[0] + 1;
		Week[] weeks= new Week[weekcount];
		iter = this.gui.week;
		for (int i = 0; i < curr - first ; i++) {
			iter.createprev(false);
			weeks[curr - first - i - 1] = iter.previous;
			iter = iter.previous;
		}
		iter = this.gui.week;
		for (int j = 0; j < last - curr + 1; j++) {
			iter.createnext(false);
			weeks[curr - first + j] = iter;
			iter = iter.next;
		}
		String name;
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		for (int k = 0; k < weekcount; k++) {
			for (int l = 0; l < 5; l++ ) {
				if (weeks[k].monday.plusDays(l).getMonthValue() == month) {
					for (int m = 1; m < Locations.values().length; m++) {
						for (int n = 0; n < 3; n++ ) {
							Person p = weeks[k].days[l].slots[1][m][n].emp;
							if (p != null) {
								name = p.name;
								
								if(!ht.containsKey(name)) {
									ht.put(name, 1);
								} else {
									ht.put(name, ht.get(name) + 1);
								}
							}
						}
					}
				}
			}
		}
		String[] months = new String[] {"Január","Február","Március","Április","Május","Június","Július","Augusztus","Szeptember","Október","November", "December"};
		String result = months[this.gui.week.month - 1] + "\n";
		
		Set<String> names = ht.keySet();
		for(String n: names) {
			result += "\n" + "Név : " + n + "\t\t" + "delutánok : " + String.valueOf(ht.get(n));
		}
	
		return result;
		
	}
	
	int[] getrelweeks(int year, int month) {
		LocalDate date = LocalDate.of(year, month, 1);
		int wpos = date.getDayOfWeek().getValue();
		LocalDate fwday = wpos < 6 ? date : date.plusDays(8-wpos);
		LocalDate lastday = LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth());
		TemporalField woy = WeekFields.of(Locale.GERMANY).weekOfWeekBasedYear();
		
		return new int[] {fwday.get(woy),lastday.get(woy)};
	}
	
	String getidle() {
		String[] days = new String[] {"Hétfõ","Kedd","Szerda","Csütörtök","Péntek"};
		String result = "";
		for (int i = 0; i < 5; i++) {
			result += "\n\n" + days[i] + ":";
			for (Person p : this.gui.week.staff.employees) {
				if (isidle(p.name, i)) {
					result +=  " " + p.name + ",";
				}
			}
		}
		return result;
	}
	
	boolean isidle(String name, int i) {
		boolean result = true;
		for (int j = 0; j < 2; j++) {
			for (int k = 0; k < Locations.values().length; k++) {
				for (int l = 0; l < 3; l++) {
					Person piter = this.gui.week.days[i].slots[j][k][l].emp;
					if (piter != null && piter.name.equals(name)) {	
						result = false;
					}
				}
			}
		}
		return result;
	}
	
	void openpopup(String title, String message){
		Shell parent = this.gui.shell;
	    Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	    dialog.setLayout(new FillLayout());
	    dialog.setSize(250, 250);
	    dialog.setText(title);
	    StyledText input = new StyledText(dialog, SWT.BORDER);
	    input.setText(message);
	    dialog.setLocation(400, 200);
	    
	    dialog.open();
	}
}
