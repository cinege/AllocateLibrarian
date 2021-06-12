package Start;

import GUI.GUI;

public class OrganizeLabor {

	public static void main(String[] args) {
		String currentdir = System.getProperty("user.dir");
		currentdir = "c:\\temp\\";
		GUI newprg = new GUI();
		newprg.path = currentdir;
		newprg.run();
	}
	
}
