package GUI;

import Calendar.Enums.Locations;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

import Calendar.Enums.Employees;

public class Selector extends SelectionAdapter{
	GUI gui;
	int[] pos;
	Selector(GUI gui, int[] pos){
		this.gui = gui;
		this.pos = pos;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		for (int i= 0; i < 4; i++ ) {
			this.gui.lastfocus[i] = pos[i];
		}
		int newempindex = this.gui.cmbs[pos[0]][pos[1]][pos[2]][pos[3]].getSelectionIndex();
		boolean refresh = removeEmp(newempindex, pos);
		this.gui.week.days[pos[0]].slots[pos[1]][pos[2]][pos[3]].emp = Employees.values()[newempindex];	
		if (refresh) {
			this.gui.shell.pack();
			//this.gui.shell.open();
		}
	}
	
	boolean removeEmp(int newempindex, int[] pos) {
		boolean result = false;
		for (int i = 0; i < 2 ; i++) {
			for (int j = 0; j < Locations.values().length ; j++) {
				for (int k = 0; k < 3 ; k++) {
					if (i != pos[1] || j != pos[2] || k != pos[3]) {
						Employees iteredemp = this.gui.week.days[pos[0]].slots[i][j][k].emp;
						if (iteredemp != null && iteredemp.ordinal() == newempindex) {
							result = true;
							Combo respectivecmb = this.gui.cmbs[pos[0]][i][j][k];
							respectivecmb.deselectAll();
							this.gui.week.days[pos[0]].slots[i][j][k].emp = null;
						}
					}
				}
			}
		}
		return result;
	}	
}
