package GUI;


import Calendar.Enums;
import Calendar.Week;
import Personal.Person;
import Calendar.Enums.Locations;
import Calendar.Enums.Roles;
import Calendar.Enums.Shifts;
import Calendar.Enums.Buttons;
import Calendar.Enums.Holidays;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;

import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class GUI {
    protected Display display;
	protected Shell shell;
	LocalDate now;
	Week week;
	String[] shifts;
	Label[][][] tiles;
	Label interval;
	Label weeknumber;
	Label abweek;
	int cellcount;
	int rowcount;
	int columncount;
	Text empinputbox;
	Button[] btns;
	Combo emplistcmb;
	int width;
	Combo shiftcmb;
	Combo loccmb;
	Combo holcmb;
	Combo rolecmb;
	public String path;
	/**
	 * Launch the application.
	 * @param args
	 */
	public void run() {
		try {
			//GUI window = new GUI();
			this.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		this.now = LocalDate.now();
		this.week = new Week(this.now, this.path, true);
	    this.shifts = Enums.enumtoarray("Shift");
	    this.columncount = Locations.values().length + 1;
	    this.cellcount = this.columncount * rowcount;
        this.btns = new Button[cellcount];
		this.tiles = new Label[5][2][Locations.values().length];
		createControls();
		populateddfields(false);
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
	    gridData.heightHint = 620;
	    gridData.widthHint = 900;
	    scrolledComposite.setLayoutData(gridData);
	    shell.pack();
	    shell.open();
	}
	/**
	 * Create contents of the window.
	 */
	protected void createFields(Composite parent) {
		this.width = Locations.values().length;
		this.btns = new Button[width + 6];
		// 1. sor
		this.weeknumber = newlbl(parent, "", false, false);
		this.empinputbox = newtxtbox(parent,"");
		this.btns[0] = newbtn(parent, "Hozzaad");
		this.btns[1] = newbtn(parent, "Elvesz");
		newlbl(parent,"", false, false);
		this.btns[2] = newbtn(parent, "Mentes Sablonba");
		newlbl(parent,"", false, false);
		newlbl(parent,"", false, false);
		// 2. sor
		this.interval = newlbl(parent, "", false, false);
		
		for (int i = 3; i < width + 3; i++) {
			btns[i] = newbtn(parent, Buttons.values()[i-3].name());
		}
		
		
		// 3. sor
		this.abweek = newlbl(parent,"", false, false);
		newlbl(parent,"Munkatarsak", false, false);
		newlbl(parent,"Idopont", false, false);
		newlbl(parent,"Helyszin", false, false);
		newlbl(parent,"Cimkek", false, false);
		newlbl(parent,"Szabadsag", false, false);
		newlbl(parent,"", false, false);
		newlbl(parent,"", false, false);
			
		//4.sor		
		newlbl(parent,"", false, false);
		this.emplistcmb = newcmb(parent, this.week.staff.toArray());
		this.shiftcmb = newcmb(parent, Enums.enumtoarray("Shifts"));
		this.loccmb = newcmb(parent, Enums.enumtoarray("Locations"));
		this.rolecmb = newcmb(parent, Enums.enumtoarray("Roles"));
		this.holcmb = newcmb(parent, Enums.enumtoarray("Holidays"));

		btns[width + 3] = newbtn(parent, "Felvesz");
		btns[width + 4] = newbtn(parent, "Elvesz");
	
		// 5. sor
		newlbl(parent, "", false, false);
		for (int i = 0; i < width; i++) {
			newlbl(parent, Locations.values()[i].name(), false, false);
		}
		
		// Beoszt?scsemp?k
		for (int i = 0; i < this.columncount * 10; i++) {
			int cc = this.columncount;
			int loc = (i % cc) - 1;
			if (loc  == -1) {
				newlbl(parent, Shifts.values()[i / cc].name().replace("_", " "), false, false);
			} else {
				int day = i / (this.columncount * 2);
				int shift = (i / this.columncount) % 2;
				boolean highlight = day % 2 == 0;
				this.tiles[day][shift][loc] = newlbl(parent, "", true, highlight);
			}
			
		}
		
		this.btns[0].addSelectionListener(new EmpAdder(this));
		this.btns[1].addSelectionListener(new EmpRemover(this));
		this.btns[2].addSelectionListener(new IO(this, true, true));
		this.btns[3].addSelectionListener(new Stepper(this, false));
		this.btns[4].addSelectionListener(new Stepper(this, true));
		this.btns[5].addSelectionListener(new PopUp(this, "premium"));
		this.btns[6].addSelectionListener(new IO(this, false, false));
		this.btns[7].addSelectionListener(new IO(this, true, false));
		this.btns[8].addSelectionListener(new PopUp(this, "idle"));
		this.btns[9].addSelectionListener(new Exit());
		this.btns[this.width + 3].addSelectionListener(new EditTile(this, true));
		this.btns[this.width + 4].addSelectionListener(new EditTile(this, false));
	}
	
	void populateddfields(boolean emplistmodified) {
		this.weeknumber.setText(String.valueOf(this.week.ordinal) + ". het");
		this.interval.setText(this.week.mondaystr + "-" + this.week.fridaystr);
		this.abweek.setText((this.week.weeka ? "A-het" : "B-het"));
		if (emplistmodified) {this.emplistcmb.setItems(this.week.staff.toArray());}
		Person emp = null;
		for (int i = 0; i < 5; i++) {
			for  (int j = 0; j < 2; j++) {
				for  (int k = 0; k < Locations.values().length; k++) {
					String label = "";
					for  (int l = 0; l < 3; l++) {
						emp = this.week.days[i].slots[j][k][l].emp;
						String suffix = "";
						if (k == 0) {
							Holidays h = this.week.days[i].slots[j][k][l].holiday;
							suffix = h != null ? h.name().substring(0,1) : ""; 
						} else {
							for (Roles r : this.week.days[i].slots[j][k][l].roles) {
								suffix += " " + r.name().substring(0,1); 
							}
						}
							
						if (emp != null) {
							if (label != "") {
								label += "\n";
							}
							label += emp.name + " " + suffix;
						}
					}
					this.tiles[i][j][k].setText(label);
				}
	    	}   
		}
	}
	
	void update(boolean emplistmodified) {
		populateddfields(emplistmodified);
		shell.pack();
	}
				
	GridData newgrd(boolean high) {
		GridData grd = new GridData( SWT.FILL, SWT.FILL, true, false );
		grd.widthHint = high ? 70 : 50;
		grd.heightHint = high ? 46 : 20;
		return grd;
	}
	
	Combo newcmb(Composite parent, String[] items) {
		Combo cmb = new Combo(parent, SWT.DROP_DOWN);
	    cmb.setLayoutData(newgrd(false));
	    cmb.setItems(items);
		return cmb;
	}

	
	
	Button newbtn(Composite parent, String title) {
		Button btn = new Button(parent,SWT.BORDER);
		btn.setLayoutData(newgrd(false));
		btn.setText(title);
		return btn;
	}
	Label newlbl(Composite parent, String text, boolean high, boolean highlight) {
		final Color c = new Color(this.display, 190, 190, 190);
		Label l;
		if (high) {
			l = new Label(parent, SWT.CENTER | SWT.BORDER);
		} else {
			l = new Label(parent, SWT.CENTER);
		}
		if (highlight) {
			l.setBackground(c);
		}
		
		l.setLayoutData(newgrd(high));
		l.setText(text);
		return l;
	}
	
	Text newtxtbox(Composite parent, String text) {
		Text t = new Text(parent, SWT.LEFT);
		GridData grd = new GridData( SWT.FILL, SWT.FILL, true, false );
		//grd.widthHint = 20;
		t.setLayoutData(grd);
		t.setText(text);
		return t;
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
