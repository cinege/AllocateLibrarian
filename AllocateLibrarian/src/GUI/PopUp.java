package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PopUp extends SelectionAdapter{
	GUI gui;
	PopUp(GUI gui){
		this.gui = gui;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		openpopup();
	}
	
	void openpopup(){
		Shell parent = this.gui.shell;
	    Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
	    dialog.setLayout(new FillLayout());
	    dialog.setSize(400, 400);
	    dialog.setText("Delutanosok");
	    Text input = new Text(dialog, SWT.BORDER);
	    input.setText("message");
	    dialog.setLocation(500, 300);
	    
	    dialog.open();
	}
}
