package Calculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FindScore extends Table {

	// protected static String[][] table = getTable();

	static String disc;
	static String perf;
	static int colmnr = 0;
	static int score = 0;

	// Snelwandelen heeft weer andere notaties, nog niet van toepassing!
	// vanaf 10km t.e.m. Marathon heeft geen honderdsten, nog niet van toepassing!
	static int notatie;
	static String not0 = "s.SS";
	static String not1 = "ss.SS";
	static String not2 = "m:ss.SS";
	static String not3 = "mm:ss.SS";
	static String not4 = "H:mm:ss.SS";
	static String not5 = "HH:mm:ss.SS";
	static String not6 = "Een nummer";

	static boolean kampnr = false;

	static SimpleDateFormat sdf;
	static SimpleDateFormat sdf0 = new SimpleDateFormat("s.SS");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("ss.SS");
	static SimpleDateFormat sdf2 = new SimpleDateFormat("m:ss.SS");
	static SimpleDateFormat sdf3 = new SimpleDateFormat("mm:ss.SS");
	static SimpleDateFormat sdf4 = new SimpleDateFormat("H:mm:ss.SS");
	static SimpleDateFormat sdf5 = new SimpleDateFormat("HH:mm:ss.SS");
	static int points;

	public static void disc() throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);
		System.out.println("Gelieve een discipline in te voeren: ");

		String input = "";
		if (input != null) {
			input = in.readLine();
		}

		for (int i = 1; i < getTable()[0].length; i++) { // juiste kolom vinden
			if (getTable()[0][i].equals(input)) {
				colmnr = i;
				disc = input;
				System.out.println(disc + " is gevonden!");
			}
		}

		String[] kamp = { "HJ", "PV", "LJ", "TJ", "SP", "DT", "HT", "JT" };
		if (findIn(disc, kamp)) {
			kampnr = true;
		}

	}

	public static boolean findIn(String sf, String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (sf.equals(arr[i])) {
				return true;
			}
		}
		return false;
	}

	public static void perform() throws IOException, ParseException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);

		System.out
				.println("Gelieve een prestatie in te voeren volgens volgende notatie:");
		System.out
				.println("H:mm:ss.SS \nIndien niet van toepassing mogen 'H', 'HH:m', 'HH:mm' of 'HH:mm:s', weggelaten worden");
		System.out.println("Voor kampnummers volstaat: MM.mm of M.mm");
		System.out.println("Prestatie: ");

		String input = "";
		if (input != null) {
			input = in.readLine();
		}
		perf = input;
		int pfl = perf.length();

		if (perf.contains(".")) {
			switch (pfl) {
			case 4:
				sdf = sdf0;
				notatie = 0;
				break;
			case 5:
				sdf = sdf1;
				notatie = 1;
				break;
			case 7:
				sdf = sdf2;
				notatie = 2;
				break;
			case 8:
				sdf = sdf3;
				notatie = 3;
				break;
			case 10:
				sdf = sdf4;
				notatie = 4;
				break;
			case 11:
				sdf = sdf5;
				notatie = 5;
				break;
			}
		} else {
			notatie = 6;
		}

		boolean found = false;
		if (!kampnr && notatie < 6) {
			for (int j = 1; !found && j < getTable().length; j++) {
				Date t = sdf.parse(perf);
				if (colmnr == 0) {
					score = 0;
				} else {
					if (pfl == getTable()[j][colmnr].length()) {

						if (t.equals(sdf.parse(getTable()[j][colmnr]))) {
							score = 1401 - j;
							found = true;
							System.out.println("score = " + score);
						} else if (sdf.parse(getTable()[j][colmnr]).after(t)) {
							score = 1401 - j + 1;
							found = true;
							System.out.println("score = " + score);
						}
					}
				}
			}
		} else if (notatie == 6 && disc.equals("Decathlon")) {
			for (int j = 1; !found && j < getTable().length; j++) {
				int points = Integer.parseInt(perf);
				if (colmnr == 0) {
					score = 0;
				} else {
					if (points == Integer.parseInt((getTable()[j][colmnr]))) {
						score = 1401 - j;
						found = true;
						System.out.println("score = " + score);
					}
					if (points > Integer.parseInt((getTable()[j][colmnr]))) {
						score = 1401 - j + 1;
						found = true;
						System.out.println("score = " + score);
					}
				}
			}
		} else if (kampnr) {
			for (int j = 1; !found && j < getTable().length; j++) {
				double meters = Double.parseDouble(perf);
				if (!getTable()[j][colmnr].equals("-")) {
					int pscore = 1401 - j + 1;
					if (meters == Double.parseDouble((getTable()[j][colmnr]))) {
						score = 1401 - j;
						found = true;
						System.out.println("score = " + score);
					}
					if (meters > Double.parseDouble((getTable()[j][colmnr]))) {
						score = pscore;
						found = true;
						System.out.println("score = " + score);
					}
				}
			}
		}

	}

	public static void getScore() throws ParseException, IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(isr);

		disc();
		perform();

		System.out.println("Druk 'enter' voor een nieuwe opzoeking");
		String input = in.readLine();
		if (input != null) {
			
			getScore();
		}

	}

	public static void main(String[] args) throws IOException, ParseException {
		read("men.txt");
		filterData();

		System.out.println("Find score... \n");
		getScore();

	}

}
