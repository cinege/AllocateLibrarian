package Calendar;

import java.util.stream.Stream;

public class Enums {
	public enum Locations {
		TAVOLLET,	
		HATTER,
		TAJEKOZTATO,
		NYELVSTUDIO,
		KOLCSONZO,
		BEIRATKOZAS,
		RAKTAR;
	}
	public enum Shifts {
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
	
	
	public enum Buttons {
		Korabbi,
		Kovetkezo,
		Delutanosok,
		Betoltes_CSV,
		CSV_PDF,
		Ellenorzes,
		Exit	
	}
	
	public enum Roles {
		Penztaros,
		Ugyeletvezeto,
		Egeszsegugyi		
	}
	
	public enum Holidays {
		HomeOffice,
		Betegseg,
		Szabadsag,
		Kitelepules
	}
	
	
	public static String[] enumtoarray(String myenum) {
		String[] result = null;
		switch (myenum) {
			case "Locations":
				result = Stream.of(Locations.values()).map(Locations::name).toArray(String[]::new);
				break;
			case "Shifts":
				result = Stream.of(Shifts.values()).map(Shifts::name).toArray(String[]::new);
				break;
			case "Buttons":
				result = Stream.of(Buttons.values()).map(Buttons::name).toArray(String[]::new);
				break;
			case "Holidays":
				result = Stream.of(Holidays.values()).map(Holidays::name).toArray(String[]::new);
				break;
			case "Roles":
				result = Stream.of(Roles.values()).map(Roles::name).toArray(String[]::new);
				break;
			default:
				result = null;
		}
		return result;
	}
	
	public static Roles getrole(String ch) {
		switch (ch) {
		case "E" :
			return Roles.Egeszsegugyi;
		case "P" :
			return Roles.Penztaros;
		case "U" :
			return Roles.Ugyeletvezeto;
		default:
			return null;
		}
	}
	
	public static Holidays getholiday(String ch) {
		switch (ch) {
		case "B" :
			return Holidays.Betegseg;
		case "H" :
			return Holidays.HomeOffice;
		case "S" :
			return Holidays.Szabadsag;
		case "K" :
			return Holidays.Kitelepules;
		default:
			return null;
		}
	}	
}
 