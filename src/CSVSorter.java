package src;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class CSVSorter{
    //uses regex to compare word to see if it is valid
    private static final Pattern validWord = Pattern.compile("[a-zA-Z]+$");

    //cleans words using regex rules
    public static List<String[]> cleanWords(List<String[]> rows){
        Set<String> seenWords = new HashSet<>();
        List<String[]> cleanedRows = new ArrayList<>();

        for (String[] row:rows){
            List<String> cleanedRow = new ArrayList<>();
            for(String cell : row){
                String trimmed = cell.trim();

                //check if word hasn't been seen yet
                if (validWord.matcher(trimmed).matches() && !seenWords.contains(trimmed)){
                    seenWords.add(trimmed);
                    cleanedRow.add(trimmed);
                }
            }
            //add the string to the string array
            if(!cleanedRow.isEmpty()){
                cleanedRows.add(cleanedRow.toArray(new String[0]));
            }
        }
        return cleanedRows;
    }

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

        //cleans rows of words that are not [a-zA-Z]
        rows = cleanWords(rows);

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
        sortCSV("./assets/input_long.csv", "./assets/sorted_output_long.csv", true); //True does single row. Change to false to sort by full row
    }
}
