package GUI;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import Calendar.Enums.Employee;
import Calendar.Enums.Location;

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
		for (int m = 0; m < n; m++) {
			int i = m / 6;
			int j = (m / 3) % 2;
			int l = m % 3;
			String[] locs = new String[Location.values().length];
			locs = lines[m].split(",");
			for  (int k = 0; k < Location.values().length; k++) {
				Employee newempdata = locs[k].equals("-") ? null : Employee.valueOf(locs[k]);
				if (this.gui.week.days[i].slots[j][k][l].emp != newempdata) {
					System.out.println("ok");
					modification = true;
					this.gui.week.days[i].slots[j][k][l].emp = newempdata;
					if (newempdata != null) {
						this.gui.cmbs[i][j][k][l].select(newempdata.ordinal());
					} else {
						this.gui.cmbs[i][j][k][l].deselectAll();
					}
				}	 
			}
		}
		if (modification) {this.gui.shell.pack();}
	}
	
	void save(String filepath) {
		Employee emp;
		String estr;
		String newline;
		try {
			Writer fileWriter = new FileWriter(filepath, false);
			for (int i = 0; i < 5; i++) {
				for  (int j = 0; j < 2; j++) {
					for  (int l = 0; l < 3; l++) {
						newline = "";
						for  (int k = 0; k < Location.values().length; k++) {
							emp = this.gui.week.days[i].slots[j][k][l].emp;
							estr = emp == null ? "-" : emp.name();
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
	
	void writePDF(String filepath) {
		Font f = new Font();
		f.setSize(8);
		Document document = new Document();
        try {
			PdfWriter.getInstance(document, new FileOutputStream(filepath.replace("csv", "pdf")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        document.setPageSize(PageSize.A4.rotate());
        document.open();
        
        String title = this.gui.week.weeka ? "A-het " : "B-het " + String.valueOf(this.gui.week.ordinal) + ". " + this.gui.week.mondaystr + "-" + this.gui.week.fridaystr;
        Paragraph p = new Paragraph(title);
        p.setAlignment(1);
        try {
			document.add(p);
			document.add(new Paragraph(" "));
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
        int cc = this.gui.columncount + 2;
        PdfPTable table = new PdfPTable(cc);
        table.addCell("");
        for (int i = 0; i < Location.values().length; i++){
        	PdfPCell c = new PdfPCell(new Phrase(Location.values()[i].name(), f));
        	c.setHorizontalAlignment( Element.ALIGN_CENTER);
        	table.addCell(c);
        	
        
        	
        }
        for(int i = 0; i < 240; i++){
        	if (i > 0 && i < cc) {
        		table.addCell("u");
        	}
            
        }
        table.addCell("hi8");
        try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        document.close();
	}
	
}
