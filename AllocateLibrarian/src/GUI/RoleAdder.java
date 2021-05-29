package GUI;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

import Calendar.Enums.Employee;
import Calendar.Enums.Location;

public class RoleAdder extends SelectionAdapter{
	GUI gui;
	RoleAdder(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		int[] ls = this.gui.lastfocus;
		//this.gui.cmbs[ls[0]][ls[1]][ls[2]][ls[3]].sel
		for (int i= 0; i < 4; i++ ) {
			//System.out.println(lastselected[i]);
		}
		
	}
}
