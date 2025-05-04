package src;

import java.io.*;
import java.util.*;

public class CSVSorter{
    public static void sortCSV(String inputFile, String outputFile, boolean sortByFirstColumn){
        List<String[]> rows = new ArrayList<>();

        //read the CSV file
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String line;
            while((line=br.readLine()) !=null) {
                rows.add(line.split(","));
            }
        } catch(IOException e){
            System.out.println("Error reading the CSV file: " + e.getMessage());
            return;
        }

        //sort the list
        rows.sort((a,b)->{
            String keyA = sortByFirstColumn ? a[0] : String.join(",", a);
            String keyB = sortByFirstColumn ? b[0] : String.join(",", b);
            return keyA.compareToIgnoreCase(keyB);
        });

        //Write to a new CSV file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))){
            for (String[] row : rows){
                bw.write(String.join(",", row));
                bw.newLine();
            }
            System.out.println("New file written");
        } catch (IOException e){
            System.out.println("Error writing the CSV file: "+e.getMessage());
        }
    }
    public static void main(String[] args) {
        sortCSV("input.csv", "sorted_output.csv", true); //True does single row. Change to false to sort by full row
    }
}
