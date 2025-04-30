import java.io.*;
import java.util.*;

public class CSVSorter {

    public static void sortCSV(String inputFile, String outputFile, boolean sortByFirstColumn) {
        List<String[]> rows = new ArrayList<>();

        // Read CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
            return;
        }

        // Sort the list
        rows.sort((a, b) -> {
            String keyA = sortByFirstColumn ? a[0] : String.join(",", a);
            String keyB = sortByFirstColumn ? b[0] : String.join(",", b);
            return keyA.compareToIgnoreCase(keyB);
        });

        // Write to new CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (String[] row : rows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing the CSV file: " + e.getMessage());
        }
    }

    // Example usage
    public static void main(String[] args) {
        sortCSV("input.csv", "sorted_output.csv", true); // change 'true' to 'false' to sort by full row
    }
}
