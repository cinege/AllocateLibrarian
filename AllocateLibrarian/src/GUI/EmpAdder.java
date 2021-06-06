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
		System.out.println(this.gui.empinputbox.getText());
		this.gui.staff.employees.add(new Person(this.gui.empinputbox.getText()));
		this.gui.emplistcmb.setItems(this.gui.staff.toArray());
		this.gui.staff.write();
	}
}
