package GUI;

import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Stepper extends SelectionAdapter{
	GUI gui;
	boolean fw;
	Stepper(GUI gui, boolean fw) {
		this.gui = gui;
		this.fw = fw;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (fw) {
			if (gui.week.next == null) {gui.week.createnext();}
			gui.week = gui.week.next;
			gui.populateddfields();
			this.gui.shell.pack();
		} else {
			if (gui.week.previous != null) {
				gui.week = gui.week.previous;
				gui.populateddfields();
				this.gui.shell.pack();
			}
		}
				
		
		//this.gui.shell.open();
	}
}
