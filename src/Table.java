import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Table {

	private static ArrayList<String> rawData = new ArrayList<>();
	private static ArrayList<String[]> splittedData = new ArrayList<>();
	private static ArrayList<String[]> data = new ArrayList<>();
	private static String[] header = { "null" };
	private static Integer totDiscipl = new Integer(0);
	protected static String[][] table;
	private static ArrayList<String> discipl = new ArrayList<>();

	public Table() {
	}
	
	public static String [][] getTable(){
		return table;
		
	}

	public static void printRawData() {
		for (String i : rawData) {
			System.out.println(i);
		}
	}

	public static void printSplittedData() {
		for (String[] i : splittedData) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < i.length; j++) {
				row.append(i[j] + "\t");
			}
			System.out.println(row);
		}
	}

	public static void printData() {
		for (String[] i : data) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < i.length; j++) {
				row.append(i[j] + "\t");
			}
			System.out.println(row);
		}
	}

	public static void printTable() {
		for (int i = 0; i < table.length; i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < table[0].length; j++) {
				row.append(table[i][j] + "\t");
			}
			System.out.println(row.toString());
		}
	}

	public static ArrayList<String> filterRawData() {
		ArrayList<String> filteredRaw = new ArrayList<>();

		for (int i = 0; i < rawData.size(); i++) {
			StringBuilder s = new StringBuilder();
			StringBuilder n = new StringBuilder();
			s.append(rawData.get(i));

			for (int j = 0; j < s.length(); j++) {
				if ((int) s.charAt(j) < 122) {
					n.append(s.charAt(j));
				}
			}
			filteredRaw.add(n.toString());
		}
		return filteredRaw;
	}

	// inlezen van data
	public static void read(String s) throws IOException {
		System.out.println("Inlezen " + "'" + s + "'...");
		BufferedReader in = new BufferedReader(new FileReader(s));

		String input = "";

		while (input != null) {
			input = in.readLine();
			if (input != null) {
				rawData.add(input.toString());
			}
		}
		rawData = filterRawData();
	}

	// verwerken data
	// Arraylist maken van de nuttige info
	public static void filterData() {
		System.out.println("Creating table...");

		for (int i = 0; i < rawData.size(); i++) {
			String line = rawData.get(i);
			String[] splittedLine = line.split("\t");

			if (splittedLine.length <= 10) // max aantal discip per blad is 9
				splittedData.add(splittedLine);
		}

		int lastTitle = 0;
		for (int i = 0; i < splittedData.size(); i++) {
			String[] selectedRow = splittedData.get(i);

			int last = selectedRow.length;

			if (last > 5) { // arbitraire cte, verondersteld min 4 discipl per
							// blad
				String firstField = selectedRow[0];
				String lastField = selectedRow[last - 1];

				if ((firstField.equals("Points") || lastField.equals("Points"))) {
					lastTitle = i;
					data.add(selectedRow);

					if (header[0].equals("null")
							|| !findInHeader(selectedRow[2], header)) {
						totDiscipl = new Integer(selectedRow.length - 1
								+ totDiscipl.intValue());
						addAllToHeader(selectedRow);
					}

				} else if ((lastTitle == 0 || i - lastTitle < 53)) {
					data.add(selectedRow);
				}
			}

		}

		System.out.println("Gevonden disciplines: " + totDiscipl);
		table = new String[1401][totDiscipl + 1];

		for (int i = 0; i < discipl.size(); i++) {
			table[0][i + 1] = discipl.get(i);
		}

		for (int i = 0; i <= 1399; i++) {
			table[i + 1][0] = String.valueOf(1400 - i);
		}

		int[] headerIndex = null;
		String Points = null;

		for (int i = 0; i < data.size(); i++) {
			String[] selectedRow = data.get(i);
			int headerIndexlast = 0;

			if (findInHeader("Points", selectedRow)) {
				if (selectedRow[0].equals("Points")) {
					Points = "left";
				} else {
					Points = "right";
				}
				headerIndex = new int[selectedRow.length - 1];

				for (int j = 1; j < table[0].length; j++) {
					if (findInHeader(table[0][j], selectedRow)) {
						headerIndex[headerIndexlast] = j;
						headerIndexlast++;
					}
				}
			} else {
				if (Points.equals("left")) {
					String score = selectedRow[0];
					for (int j = 1; j < selectedRow.length; j++) {
						table[1401 - (Integer.parseInt(score))][headerIndex[j - 1]] = selectedRow[j];
					}
				} else {

					String score = selectedRow[selectedRow.length - 1];
					for (int j = 0; j < selectedRow.length - 1; j++) {
						table[1401 - (Integer.parseInt(score))][headerIndex[j]] = selectedRow[j];
					}
				}
			}

		}

		System.out.println("Tabel is gegenereed! ");

	}

	public static boolean findInHeader(String searchFor, String[] header) {
		for (int i = 0; i < header.length; i++) {
			if (searchFor.equals(header[i])) {
				return true;
			}
		}
		return false;
	}

	public static void addAllToHeader(String[] newHeader) {
		header = new String[newHeader.length - 1];
		int pos = 0;
		for (int i = 0; i < newHeader.length; i++) {
			if (!newHeader[i].equals("Points")) {
				header[pos] = newHeader[i];
				discipl.add(newHeader[i]);
				pos++;
			}
		}
	}

	public static void saveTable(String name) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(name));
		for (int i = 0; i < table.length; i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < table[0].length; j++) {
				row.append(table[i][j] + "\t");
			}
			out.println(row.toString());
		}
		out.close();
		System.out.println("Table is opgeslagen: " + "\t" + name);
	}

	public static void saveData(String name) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(name));
		for (int i = 0; i < data.size(); i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < data.get(i).length; j++) {
				row.append(data.get(i)[j] + "\t");
			}
			out.println(row.toString());
		}
		out.close();
		System.out.println("Data is opgeslagen: " + "\t" + name);
	}

	public static void saveSplittedData(String name) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(name));
		for (int i = 0; i < splittedData.size(); i++) {
			StringBuilder row = new StringBuilder();
			for (int j = 0; j < splittedData.get(i).length; j++) {
				row.append(splittedData.get(i)[j] + "\t");
			}
			out.println(row.toString());
		}
		out.close();
		System.out.println("splitteData is opgeslagen: " + "\t" + name);
	}

	public static void saveRawData(String name) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(name));
		for (int i = 0; i < rawData.size(); i++) {
			out.println(rawData.get(i));
		}
		out.close();
		System.out.println("splitteData is opgeslagen: " + "\t" + name);
	}

	// public static void main(String[] args) throws IOException {
		/*
		 * read("2008_men.txt"); saveRawData("2008_RawData_men.txt");
		 * saveSplittedData("2008_Splitted_men.txt");
		 * saveData("2008_Data_men.txt"); filterData(); printSplittedData(); //
		 * saveRawData("RawData_men.txt");
		 * saveSplittedData("2008_Splitted_men.txt");
		 * saveData("2008_Data_Men.txt"); saveTable("2008_Table_men.txt");
		 * System.out.println("Done!");
		 */

		/*
		read("men.txt");
		filterData(); // saveRawData("RawData_men.txt"); //
		// saveSplittedData("Splitted_men.txt"); // saveData("Data_Men.txt");
		// saveTable("Table_men.txt");
		System.out.println("Done!");
		
		*/

	// }

}
