package GUI;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import Calendar.Enums;
import Calendar.Enums.Locations;
import Calendar.Enums.Roles;
import Personal.Person;

public class EditTile extends SelectionAdapter{
	GUI gui;
	boolean add;
	EditTile(GUI gui, boolean add) {
		this.gui = gui;
		this.add = add;
	}
		
	@Override
	public void widgetSelected(SelectionEvent e) {

		int empindex = this.gui.emplistcmb.getSelectionIndex();
		int dayshiftindex = this.gui.shiftcmb.getSelectionIndex();
		int locindex = this.gui.loccmb.getSelectionIndex();
		int holindex = this.gui.holcmb.getSelectionIndex();
		int roleindex = this.gui.rolecmb.getSelectionIndex();
		boolean valid = (empindex > -1) && (dayshiftindex > -1) && (locindex > -1);
		if (valid) {	
			int dayindex = dayshiftindex / 2;
			int shiftindex = dayshiftindex % 2;
			if (this.add && empindex > -1) {
				adduser(empindex, dayindex, shiftindex, locindex, holindex, roleindex);
			} else if (!this.add && empindex > -1){
				removeuser(empindex, dayindex, shiftindex, locindex);
			}
		}
		
	}
	
	void adduser(int empindex, int dayindex, int shiftindex, int locindex, int holindex, int roleindex) {
		Person emp;
		Person newemp = this.gui.staff.employees.get(empindex);
		Person[] empsinslot = new Person[3];
		int pos = -1;
		int freepos = 0;
		for (int i = 0; i < 3; i++) {
			emp = this.gui.week.days[dayindex].slots[shiftindex][locindex][i].emp;
			if (emp != null) {
				freepos++;
				empsinslot[i] = emp;
				if (emp.equals(newemp)) {
					pos = i;
					setlabels(locindex, dayindex, shiftindex, i, holindex, roleindex);
				}
			}
		}
		if (pos == -1) {
			//freepos = freepos > 2 ? 2 : freepos;
			this.gui.week.days[dayindex].slots[shiftindex][locindex][freepos].emp = newemp;
			setlabels(locindex, dayindex, shiftindex, freepos, holindex, roleindex);
			for (int i = 0; i < 2 ; i++) {
				for (int j = 0; j < Locations.values().length ; j++) {
					for (int k = 0; k < 3 ; k++) {
						if ((i != shiftindex || j != locindex) && this.gui.week.days[dayindex].slots[i][j][k].emp == newemp)  {
							this.gui.week.days[dayindex].slots[i][j][k].emp = null;
						}
					}
				}
			}
			updateview();
		}
		
		
	}
	
	void removeuser(int empindex, int dayindex, int shiftindex, int locindex) {
		Person iteremp;
		Person emp = this.gui.staff.employees.get(empindex);
		for (int i = 0; i < 3; i++) {
			iteremp = this.gui.week.days[dayindex].slots[shiftindex][locindex][i].emp;
			if (iteremp == emp) {
				System.out.println("OK");
				this.gui.week.days[dayindex].slots[shiftindex][locindex][i].emp = null;
				this.gui.week.days[dayindex].slots[shiftindex][locindex][i].holiday = null;
				this.gui.week.days[dayindex].slots[shiftindex][locindex][i].roles.clear();
			}
		}
		updateview();
	}
	
	void updateview() {
		removeemptyslots();
		this.gui.populateddfields();
		//this.gui.holcmb.getItem(0);
		this.gui.shell.pack();
	}
	
	void removeemptyslots() {
		Person iterempprev;
		Person iteremp;
		for (int i = 0; i < 5 ; i++) {
			for (int j = 0; j < 2 ; j++) {
				for (int k = 0; k < Locations.values().length ; k++) {
					for (int l = 1; l < 3 ; l++) {
						iterempprev = this.gui.week.days[i].slots[j][k][l-1].emp;
						iteremp = this.gui.week.days[i].slots[j][k][l].emp;
						if ( iteremp != null && iterempprev == null) {
							iterempprev = iteremp;
							iteremp = null;
						}
					}
				}
			}
		}
	}
	
	void setlabels(int locindex, int dayindex, int shiftindex, int posindex, int holindex, int roleindex) {
		if (holindex > -1 && locindex == 0) {
			this.gui.week.days[dayindex].slots[shiftindex][locindex][posindex].holiday = Enums.Holidays.values()[holindex];
			updateview();
		}
		if (roleindex > -1) {
			List<Roles> rolelist = this.gui.week.days[dayindex].slots[shiftindex][locindex][posindex].roles;
			rolelist.add(Enums.Roles.values()[roleindex]);
			rolelist = rolelist.stream().distinct().collect(Collectors.toList());
			this.gui.week.days[dayindex].slots[shiftindex][locindex][posindex].roles = rolelist;
			updateview();
		}
	}
}
