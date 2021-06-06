package Personal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Staff {
	public List<Person> employees;
	public int nextavailable; 
	public Staff(){
		this.employees = new ArrayList<Person>();
		read();
	}
	
	public void add(String name) {
		this.employees.add(new Person(name));
		write();
	}
	
	public void write() {
		String filepath = "c:\\temp\\staff.csv";
		Writer fileWriter;
		try {
			fileWriter = new FileWriter(filepath, false);
			for (Person p : this.employees) { 
				fileWriter.write(p.name + "\n");
			}
			fileWriter.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void read() {
		String filepath = "c:\\temp\\staff.csv";
		String empname;
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(filepath));
			String line = reader.readLine();
			while (line != null) {
				empname = line.split(",")[0];
				this.employees.add(new Person(empname));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			write();
		}
	}
	
	public String[] toArray() {
		String[] result = new String[this.employees.size()];
		for (int i = 0; i < this.employees.size(); i++) {
			result[i] = this.employees.get(i).name;
		}
		return result;
	}
	public Person getperson(String name) {
		Optional<Person> matchingObject = this.employees.stream().filter(p -> p.name.equals(name)).findFirst();
		Person p = matchingObject.orElse(null);
		return p;
	}
	
}


