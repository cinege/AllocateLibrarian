package GUI;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import Personal.Person;

public class EmpAdder  extends SelectionAdapter{
	GUI gui;
	EmpAdder(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		this.gui.week.staff.employees.add(new Person(this.gui.empinputbox.getText()));
		this.gui.emplistcmb.setItems(this.gui.week.staff.toArray());
		this.gui.update(true);
	}
}
