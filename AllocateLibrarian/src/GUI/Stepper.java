package GUI;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Stepper extends SelectionAdapter{
	GUI gui;
	boolean fw;
	Stepper(GUI gui, boolean fw) {
		this.gui = gui;
		this.fw = fw;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		String filepath = this.gui.path + this.gui.week.csvfilename;
		//String filepath = path + "beosztas" + String.valueOf(this.gui.week.year) + "-" + String.valueOf(this.gui.week.ordinal) + ".csv";
		this.gui.week.writetocsv(filepath);
		if (fw) {
			if (gui.week.next == null) {this.gui.week.createnext(true);}
			gui.week = gui.week.next;
		} else {
			if (gui.week.previous == null) {this.gui.week.createprev(true);}
			gui.week = gui.week.previous;
				
		}
		this.gui.update(true);
	}
}
