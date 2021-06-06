package GUI;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.FontFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import Calendar.Day;
import Calendar.Enums;
import Calendar.Enums.Holidays;
import Calendar.Enums.Locations;
import Calendar.Enums.Roles;
import Calendar.Enums.Shifts;
import Personal.Person;

public class IO extends SelectionAdapter{
	GUI gui;
	boolean save;
	public IO(GUI gui, boolean save) {
		this.gui = gui;
		this.save = save;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		String path = "c:\\temp\\";
		String filepath = path + "beosztas" + String.valueOf(this.gui.week.year) + "-" + String.valueOf(this.gui.week.ordinal) + ".csv";
		if (this.save) {
			save(filepath);
			writePDF(filepath);
		} else {
			load(filepath);
		}
	}
	
	void load(String filepath) {
		int n = 30;
		String[] lines = new String[n];
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filepath));
			int i = 0;
			String line = reader.readLine();
			while (line != null) {
				lines[i] = line;
				i++;
				line = reader.readLine();
				
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean modification = false;
		Person newempdata;
		String roles;
		List<Roles> newroles = new ArrayList<Roles>(); 
		for (int m = 0; m < n; m++) {
			int i = m / 6;
			int j = (m / 3) % 2;
			int l = m % 3;
			String[] locs = new String[Locations.values().length];
			locs = lines[m].split(",");
			for  (int k = 0; k < Locations.values().length; k++) {
				if (locs[k].equals("-")) {
					newempdata = null;
					roles = "";
				} else {
					newempdata = this.gui.staff.getperson(locs[k].split("\\|")[0]);
					roles  = locs[k].split("\\|").length == 1 ? "" : locs[k].split("\\|")[1];
				}
				boolean diffinroles = getrolediff(roles, this.gui.week.days[i].slots[j][k][l].holiday, this.gui.week.days[i].slots[j][k][l].roles);
				if (this.gui.week.days[i].slots[j][k][l].emp != newempdata || diffinroles) {
					modification = true;
					if (k == 0) {
						this.gui.week.days[i].slots[j][k][l].holiday = Enums.getholiday(roles);
						this.gui.week.days[i].slots[j][k][l].roles.clear();
					} else {
						this.gui.week.days[i].slots[j][k][l].roles.clear();
						for (int o = 0; o < roles.length(); o++) {
							this.gui.week.days[i].slots[j][k][l].roles.add(Enums.getrole(roles.substring(o,o + 1)));
						}
						this.gui.week.days[i].slots[j][k][l].holiday = null;
					}
					this.gui.week.days[i].slots[j][k][l].emp = newempdata;
				}	 	
			}
		}
		if (modification) {
			this.gui.populateddfields();
			this.gui.shell.pack();
		}
	}
	
	void save(String filepath) {
		Person emp;
		String roles = "";
		String estr;
		String newline;
		try {
			Writer fileWriter = new FileWriter(filepath, false);
			for (int i = 0; i < 5; i++) {
				for  (int j = 0; j < 2; j++) {
					for  (int l = 0; l < 3; l++) {
						newline = "";
						for  (int k = 0; k < Locations.values().length; k++) {
							emp = this.gui.week.days[i].slots[j][k][l].emp;
							if (emp != null) {
								roles = "";
								for (Roles r : this.gui.week.days[i].slots[j][k][l].roles) {
									roles += r.name().substring(0,1);
								}
								if (k == 0 && this.gui.week.days[i].slots[j][k][l].holiday != null) {
									roles += this.gui.week.days[i].slots[j][k][l].holiday.name().substring(0,1);
								}
								estr = emp.name + "|" + roles;
							} else {
								estr =  "-";
							}
							newline += estr + ",";
						}
						newline = newline.substring(0, newline.length()-1) + "\n";
						fileWriter.write(newline);
					}
		    	}   
			}
			fileWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean getrolediff(String saved, Holidays h, List<Roles> roles) {
		String ref = h != null ? h.name().substring(0,1) : "";
		for (Roles r : roles) {
			ref += r.name().substring(0,1);
		}
		return ref.equals(saved);
	}
	
	void writePDF(String filepath) {
		Document document = new Document();
		BaseFont bf;
		Font f = new Font();
		BaseColor gray = new BaseColor(245, 245, 245);
		BaseColor white = new BaseColor(255, 255, 255); 
		try {
        	FileOutputStream fs = new FileOutputStream(filepath.replace("csv", "pdf"));
        	//PrintStream stream  = new PrintStream(fs, true, "UTF_8");
			PdfWriter.getInstance(document, fs);
			document.setPageSize(PageSize.A4.rotate());
			document.open();
			//Fejlec
			String title = this.gui.week.weeka ? "A-hét " : "B-hét ";
			title += "(" + String.valueOf(this.gui.week.ordinal) + ".) " + this.gui.week.mondaystr + "-" + this.gui.week.fridaystr;
			Paragraph p = new Paragraph(title);
			p.setAlignment(1);
        	document.add(p);
			document.add(new Paragraph(" "));
			bf = BaseFont.createFont("Helvetica", "ISO-8859-2", false);
			f = new Font(bf, 9 ,Font.NORMAL);
			f.setSize(8);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Tablazat
		 PdfPTable table = new PdfPTable(10);
		//Fejlec
		newcell(table, "", f, white);
        newcell(table, "Távollét", f, white);
        newcell(table, "Háttér", f, white);	
        newcell(table, "Tájékoztató", f, white);
        newcell(table, "Nyelvstúdió", f, white);
        newcell(table, "Kölcsönzõ", f, white);
        newcell(table, "Aláírás", f, white);
        newcell(table, "Beiratkozás", f, white);
        newcell(table, "Aláírás", f, white);
        newcell(table, "Raktár", f, white);
        Day day;
        String cellcontent;
        BaseColor c;
        for(int i = 0; i < 5; i++){
        	day = this.gui.week.days[i];
        	for(int j = 0; j < 2; j++){
        		c = i % 2 == 0 ? gray : white;
        		// elso oszlop
        		newcell(table, Shifts.values()[i*2 + j].name().replace("_", " "), f, white);
        		// masodik oszlop - Tavollet
        		cellcontent = "";
        		for (int k = 0; k < 3; k++) {
        			if (cellcontent != "") {cellcontent += "\n";}
        			if (this.gui.week.days[i].slots[j][0][k].emp != null) { 
        				cellcontent = this.gui.week.days[i].slots[j][0][k].emp.name;
        				if (this.gui.week.days[i].slots[j][0][k].holiday != null) { cellcontent += " " + this.gui.week.days[i].slots[j][0][k].holiday.name().substring(0,1);}
        			}
        		}
        		newcell(table, cellcontent, f, c);
        		// 3. - 6. oszlop - Hatter - Kolcsonzo
        		for (int l = 1; l < 5; l++) {
        			newcell(table, getcellcontent(i, j, l), f, c);
        		}
        		newcell(table, "", f, c);
        		newcell(table, getcellcontent(i, j, 5), f, c);
        		newcell(table, "", f, c);
        		newcell(table, getcellcontent(i, j, 6), f, c);
        	}
        }
        
        try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        document.close();
	}
	
	void newcell(PdfPTable t, String content, Font f, BaseColor bc) {
		PdfPCell c = new PdfPCell(new Phrase(content, f));
		c.setFixedHeight(40);
		c.setBackgroundColor(bc);
    	c.setHorizontalAlignment( Element.ALIGN_CENTER);
    	t.addCell(c);
	}
	
	String getcellcontent(int i, int j, int k) {
		String cellcontent = "";
		for (int l = 0; l < 3; l++) {
			if (cellcontent != "") {cellcontent += "\n";}
			if (this.gui.week.days[i].slots[j][k][l].emp != null) { 
				cellcontent = this.gui.week.days[i].slots[j][k][l].emp.name;
				for (Roles r : this.gui.week.days[i].slots[j][k][l].roles) {
					cellcontent += " " + r.name().substring(0, 1);
				}
			}
		}
		return cellcontent;
	}
	
	
}
