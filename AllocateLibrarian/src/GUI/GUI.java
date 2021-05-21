package GUI;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import Calendar.Enums;
import Calendar.MSchedule;
import Calendar.Enums.Employee;
import Calendar.Enums.Location;
import Calendar.Enums.Shift;

import org.eclipse.swt.custom.ViewForm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.*;

public class GUI {
    protected Display display;
	protected Shell shell;
	MSchedule schedule;
	LocalDate now;
	String[] employees;
	String[] stations;
	String[] shifts;
	String[] buttons;
	Button[] btns;
	Combo[] cmbs;
	Label[] lbls;
	int cellcount;
	int rowcount;
	int columncount;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUI window = new GUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		this.now = LocalDate.now();
		this.schedule = new MSchedule();
		this.schedule.randomize(this.now.getYear(),this.now.getMonth().getValue());
		this.schedule.genMonthOverview();
		this.employees = Enums.enumtoarray("Employee");
	    this.stations = Enums.enumtoarray("Location");
	    this.shifts = Enums.enumtoarray("Shift");
	    this.buttons = Enums.enumtoarray("Button");
	    this.columncount = stations.length + 1;
	    this.rowcount = shifts.length * 3 + 2;
	    this.cellcount = this.columncount * rowcount;
        this.btns = new Button[cellcount];
		this.cmbs = new Combo[cellcount];
		this.lbls = new Label[cellcount];
		staticConents();
		//dynamicContents();
		populateddfields();
		this.shell.open();
		this.shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	void staticConents() {
		this.display = Display.getDefault();
		this.shell = new Shell();
		this.shell.setSize((this.columncount + 1) * 82, this.rowcount * 40);
		this.shell.setLayout(new GridLayout(this.columncount, false));
		this.shell.setText("Beosztas");
	}
	/**
	 * Create contents of the window.
	 */
	protected void dynamicContents() {
		
		
		
		    
	    for (int i = 0; i < cellcount; i++) {
	    	int m = (i - this.columncount * 2) % (this.columncount * 3);
	    	int n = (i - this.columncount * 2) % (this.columncount * 6);
	    	if (i == 0 ) {
	    		this.lbls[i] = newlbl(weekinterval);
	    	} else if (i == this.columncount) {
	    		this.lbls[i] = newlbl(abhet ? "A-hét":"B-hét");
	    	} else if(i < this.columncount) {
	    		this.btns[i] = newbtn(buttons[i-1]);
	    	} else if (i > this.columncount && i < this.columncount * 2){
	    		this.lbls[i] = newlbl(stations[i - 1 - this.columncount]);
	    	} else if ((i - this.columncount * 2) % (this.columncount * 3) == 0) {
	    		this.lbls[i] = newlbl(shifts[(i - this.columncount * 2) / (this.columncount * 3)]);
	    	} else if ((i - this.columncount) % (this.columncount) == 0) {
	    		this.lbls[i] = newlbl("");
	    	} else if  (m < this.columncount && n != 1 + this.columncount * 3 ) {

	    		this.cmbs[i] = newcmb(true);
	    	} else {
	    		this.cmbs[i] = newcmb(false);
	    	}    	
	    }
	    btns[1].addSelectionListener(new Stepper(this, false));
	    btns[2].addSelectionListener(new Stepper(this, true));
	    btns[7].addSelectionListener(new Exit());
	}
	
	void populateddfields() {
		int dayoftheweek = this.now.getDayOfWeek().getValue();
		DateTimeFormatter f = DateTimeFormatter.ofPattern("M-d");
		for (Shift s: Shift.values()) {
			String monthdayshift = this.now.plusDays(1 + s.ordinal() / 2 - dayoftheweek).format(f) + "-" + String.valueOf(s);
			System.out.println(monthdayshift);
			for (Location l : Location.values()) {
				System.out.println(l.toString());
                ArrayList<Employee> employees = this.schedule.monthoverview.get(monthdayshift).get(l);
                if (employees != null) {System.out.println(employees.toString());} else {System.out.println("ures");}
			}
		}
	}
	
	void removecontents() {
		for (int i = 0; i < this.cellcount; i++) {
			if (this.btns[i] != null) {this.btns[i].dispose();}
			if (this.cmbs[i] != null) {this.cmbs[i].dispose();}
			if (this.lbls[i] != null) {this.lbls[i].dispose();}
		}
	}
		
	GridData newgrd() {
		GridData grd = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		grd.widthHint = 110;
		grd.heightHint = 16;
		return grd;
	}
	Combo newcmb(boolean highlight) {
		GridData grd = newgrd();
		Combo cmb = new Combo(shell, SWT.DROP_DOWN);
		cmb.setLayoutData(grd);
		cmb.setItems(Enums.enumtoarray("Employee"));
		//cmb.select(employee);
		if (highlight) {cmb.setBackground(display.getSystemColor(SWT.COLOR_GRAY));}
		cmb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {}
		});
		return cmb;
	}
	Label newlbl(String text) {
		GridData grd = newgrd();
		Label l = new Label(shell, SWT.CENTER);
		l.setLayoutData(grd);
		l.setText(text);
		return l;
	}
	
	Button newbtn(String title) {
		GridData grd = newgrd();
		Button btn = new Button(shell,SWT.BORDER);
		btn.setLayoutData(grd);
		btn.setText(title);
		return btn;
	}
	
	
	
}
