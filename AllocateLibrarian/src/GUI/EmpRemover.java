package GUI;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import Personal.Person;

public class EmpRemover extends SelectionAdapter{
	GUI gui;
	EmpRemover(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		
		this.gui.staff.employees.remove(this.gui.emplistcmb.getSelectionIndex());
		this.gui.emplistcmb.setItems(this.gui.staff.toArray());
		this.gui.staff.write();
	}
}
