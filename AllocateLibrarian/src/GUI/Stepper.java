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
		gui.removecontents();
		gui.now = gui.now.plusDays(this.fw ? 7 : -7);
		gui.dynamicContents();
		//gui.shell.open();
		gui.shell.layout();
	}
}
