package Calendar;

import java.util.stream.Stream;

public class Enums {
	public enum Location {
		TAVOLLET,	
		HATTER,
		TAJEKOZTATO,
		NYELVSTUDIO,
		KOLCSONZO,
		BEIRATKOZAS,
		RAKTAR;
	}
	public enum Shift {
		Hetfo_DE,
		Hetfo_DU,
		Kedd_DE,
		Kedd_DU,
		Szerda_DE,
		Szerda_DU,
		Csutortok_DE,
		Csutortok_DU,
		Pentek_DE,
		Pentek_DU;
	}
	public enum Employee {
		E01,
		E02,
		E03,
		E04,
		E05,
		E06,
		E07,
		E08,
		E09,
		E10,
		E11,
		E12,
		E13,
		E14,
		E15,
		E16,
		E17
	}
	
	public enum Button {
		Korabbi,
		Kovetkezo,
		Delutanosok,
		SpecFunkcio,
		Betoltes,
		Mentes,
		Exit	
	}
	
	public enum Role {
		Penztaros,
		Ugyeletvezeto,
		Egeszsegugyi,
		HomeOffice,
		Betegseg,
		Szabadsag,
		Kitelepules
	}
	
	
	public static String[] enumtoarray(String myenum) {
		String[] result = null;
		switch (myenum) {
			case "Employee":
				result = Stream.of(Employee.values()).map(Employee::name).toArray(String[]::new);
				break;
			case "Location":
				result = Stream.of(Location.values()).map(Location::name).toArray(String[]::new);
				break;
			case "Shift":
				result = Stream.of(Shift.values()).map(Shift::name).toArray(String[]::new);
				break;
			case "Button":
				result = Stream.of(Button.values()).map(Button::name).toArray(String[]::new);
				break;
			default:
				result = null;
		}
		return result;
	}
	
}
