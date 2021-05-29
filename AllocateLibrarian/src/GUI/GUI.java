package GUI;


import Calendar.Enums;
import Calendar.Month;
import Calendar.Week;
import Calendar.Enums.Employee;
import Calendar.Enums.Location;

import org.eclipse.swt.custom.ScrolledComposite;

import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridData;

public class GUI {
    protected Display display;
	protected Shell shell;
	LocalDate now;
	Month month;
	Week week;
	String[] employees;
	String[] stations;
	String[] shifts;
	String[] buttons;
	Button[] btns;
	Combo[][][][] cmbs;
	Label[] lbls;
	int cellcount;
	int rowcount;
	int columncount;
	int[] lastfocus;
	
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
		//this.month = new Month(this.now);
		//this.month.randomize();
		this.week = new Week(this.now);
		this.week.randemptoslots();
		this.employees = Enums.enumtoarray("Employee");
	    this.stations = Enums.enumtoarray("Location");
	    this.shifts = Enums.enumtoarray("Shift");
	    this.buttons = Enums.enumtoarray("Button");
	    this.columncount = stations.length + 1;
	    this.rowcount = shifts.length * 3 + 2;
	    this.cellcount = this.columncount * rowcount;
        this.btns = new Button[cellcount];
		this.cmbs = new Combo[5][2][Location.values().length][3];
		this.lbls = new Label[cellcount];
		this.lastfocus = new int[] {0,0,0,0};
		createControls();
		populateddfields();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}
	
	void createControls() {
		this.shell = new Shell( new Display() );
		this.shell.setText("Beosztas");
		ScrolledComposite scrolledComposite = new ScrolledComposite( shell, SWT.V_SCROLL );
	    Composite parent = new Composite( scrolledComposite, SWT.NONE );
	    parent.setLayout( new GridLayout( this.columncount, false ) );
		createFields(parent);
		scrolledComposite.setContent( parent );
	    scrolledComposite.setExpandVertical( true );
	    scrolledComposite.setExpandHorizontal( true );
	    
	    scrolledComposite.addListener( SWT.Resize, event -> {
	      int width = scrolledComposite.getClientArea().width;
	      scrolledComposite.setMinSize( parent.computeSize( width, SWT.DEFAULT ) );
	    } );
	    shell.setLayout( new GridLayout( 1, false ) );
	    GridData gridData = new GridData( SWT.FILL, SWT.FILL, true, true );
	    gridData.heightHint = 600;
	    gridData.widthHint = 800;
	    scrolledComposite.setLayoutData(gridData);
	    shell.pack();
	    shell.open();
	}
	/**
	 * Create contents of the window.
	 */
	protected void createFields(Composite parent) {
		for (int i = 0; i < cellcount; i++) {
	    	int m = (i - this.columncount * 2) % (this.columncount * 3);
	    	int n = (i - this.columncount * 2) % (this.columncount * 6);
	    	if (i == 0 ) {
	    		this.lbls[i] = newlbl(parent, "");
	    	} else if (i == this.columncount) {
	    		this.lbls[i] = newlbl(parent, "");
	    	} else if(i < this.columncount) {
	    		this.btns[i] = newbtn(parent, buttons[i-1]);
	    	} else if (i > this.columncount && i < this.columncount * 2){
	    		this.lbls[i] = newlbl(parent, stations[i - 1 - this.columncount]);
	    	} else if ((i - this.columncount * 2) % (this.columncount * 3) == 0) {
	    		this.lbls[i] = newlbl(parent, shifts[(i - this.columncount * 2) / (this.columncount * 3)]);
	    	} else if ((i - this.columncount) % (this.columncount) == 0) {
	    		this.lbls[i] = newlbl(parent, "");
	    	} else if  (m < this.columncount && n != 1 + this.columncount * 3 ) {
	    		int[] pos = fromvektor(i);
	    		this.cmbs[pos[0]][pos[1]][pos[2]][pos[3]] = newcmb(parent, pos, true);
	    	} else {
	    		int[] pos = fromvektor(i);
	    		this.cmbs[pos[0]][pos[1]][pos[2]][pos[3]] = newcmb(parent, pos, false);
	    	}    	
	    }
	    btns[1].addSelectionListener(new Stepper(this, false));
	    btns[2].addSelectionListener(new Stepper(this, true));
	    btns[4].addSelectionListener(new RoleAdder(this));
	    btns[5].addSelectionListener(new IO(this, false));
	    btns[6].addSelectionListener(new IO(this, true));
	    btns[7].addSelectionListener(new Exit());
	}
	
	void populateddfields() {
		this.lbls[0].setText(this.week.mondaystr + "-" + this.week.fridaystr);
		this.lbls[this.columncount].setText(String.valueOf(this.week.ordinal) + (this.week.weeka ? ". het A" : ". het B"));
		Employee emp = null;
		for (int i = 0; i < 5; i++) {
			for  (int j = 0; j < 2; j++) {
				for  (int k = 0; k < Location.values().length; k++) {
					for  (int l = 0; l < 3; l++) {
						emp = this.week.days[i].slots[j][k][l].emp;
						if (emp == null) {
							this.cmbs[i][j][k][l].deselectAll();
						}	else {
							this.cmbs[i][j][k][l].select(emp.ordinal());
						}
					}
				}
	    	}   
		}
	}
			
	GridData newgrd() {
		GridData grd = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
		grd.widthHint = 110;
		grd.heightHint = 10;
		return grd;
	}
	Combo newcmb(Composite parent, int[] pos, boolean highlight) {
		Combo cmb = new Combo(parent, SWT.DROP_DOWN);
		GridData grd = new GridData( SWT.FILL, SWT.CENTER, true, false );
		grd.widthHint = 20;
	    cmb.setLayoutData(grd);
	    cmb.setItems(Enums.enumtoarray("Employee"));
		if (highlight) {cmb.setBackground(this.shell.getDisplay().getSystemColor(SWT.COLOR_GRAY));}
		cmb.addSelectionListener(new Selector(this, pos));
		return cmb;
	}
	Label newlbl(Composite parent, String text) {
		Label l = new Label(parent, SWT.CENTER);
		GridData grd = new GridData( SWT.FILL, SWT.CENTER, true, false );
		grd.widthHint = 15;
		l.setLayoutData(grd);
		l.setText(text);
		return l;
	}
	
	Button newbtn(Composite parent, String title) {
		Button btn = new Button(parent,SWT.BORDER);
		GridData grd = new GridData( SWT.FILL, SWT.CENTER, true, false );
		grd.widthHint = 20;
		btn.setLayoutData( grd);
		btn.setText(title);
		return btn;
	}
	
int[] fromvektor (int v){
		int loc = v % (7 + 1) - 1;
		int row = v / (7 + 1) - 2;
		int pos = row % 3;
		int day = row / 6 ;
		int shift = row / 3 % 2;		
		return new int[] {day, shift, loc, pos};
	}
}
