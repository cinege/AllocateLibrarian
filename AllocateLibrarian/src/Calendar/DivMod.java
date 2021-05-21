package Calendar;

public class DivMod {
	int[] r = new int[2];
	DivMod(int i, int j) {
		this.r[0] = i / j;
		this.r[1] = i % j;
	}
}
