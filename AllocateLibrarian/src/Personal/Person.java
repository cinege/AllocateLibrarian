package Personal;


import Calendar.Enums.Shifts;

public class Person {
	public String name;
	public int kod;
	public boolean expert;
	public Shifts preferred;
	public Person(String name) {
		this.name = name;
	}
}
