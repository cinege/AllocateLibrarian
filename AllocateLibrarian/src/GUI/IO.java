package GUI;

import com.itextpdf.text.BaseColor;
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
import java.io.FileOutputStream;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import Calendar.Day;
import Calendar.Enums.Roles;
import Calendar.Enums.Shifts;

public class IO extends SelectionAdapter{
	GUI gui;
	boolean save;
	boolean template;
	public IO(GUI gui, boolean save, boolean template) {
		this.gui = gui;
		this.save = save;
		this.template = template;
	}
	@Override
	public void widgetSelected(SelectionEvent e) {
		String filepath = "";
		if (template) {
			filepath = this.gui.path + (this.gui.week.weeka ? "A" : "B") + "-sablon.csv";
		} else {
			filepath = this.gui.path + this.gui.week.csvfilename;
		}
		if (this.save) {
			this.gui.week.writetocsv(filepath);
			if (!template) {writePDF(filepath);}
		} else {
			this.gui.week.loadcsvcontent(filepath);
		}
	}
	
	void writePDF(String filepath) {
		Document document = new Document();
		BaseFont bf;
		Font f = new Font();
		BaseColor gray = new BaseColor(245, 245, 245);
		BaseColor white = new BaseColor(255, 255, 255); 
		try {
        	FileOutputStream fs = new FileOutputStream(filepath.replace("csv", "pdf"));
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
        				cellcontent += this.gui.week.days[i].slots[j][0][k].emp.name;
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
				cellcontent += this.gui.week.days[i].slots[j][k][l].emp.name;
				for (Roles r : this.gui.week.days[i].slots[j][k][l].roles) {
					cellcontent += " " + r.name().substring(0, 1);
				}
			}
		}
		return cellcontent;
	}
	
	
}
