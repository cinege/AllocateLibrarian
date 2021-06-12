package GUI;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EmpRemover extends SelectionAdapter{
	GUI gui;
	EmpRemover(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		int index = this.gui.emplistcmb.getSelectionIndex();
		if (index > -1) {
			String name = this.gui.emplistcmb.getText();
			this.gui.week.staff.employees.remove(index);
			this.gui.emplistcmb.setItems(this.gui.week.staff.toArray());
			this.gui.week.remove(name);
			this.gui.update(true);
		}
	}
}
