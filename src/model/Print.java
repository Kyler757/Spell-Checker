package src.model;

/**
 * @author Emmett Jaakkola
 * @version 1.0
 * @since 2025-01-02
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that handles different types of printing
 */
public class Print {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static boolean debug = false;

    /**
     * Prints a message
     * 
     * @param message The message to print
     */
    public static void msg(Object... message) {
        if (!debug) return;
        StringBuilder out = new StringBuilder();
        for (Object o : message) {
            out.append(o).append(" ");
        }
        System.out.println(out);
    }

    /**
     * Prints an error message in red
     * 
     * @param message The message to print
     */
    public static void error(String message) {
        System.err.println(ANSI_RED + message + ANSI_RESET);
    }

    /**
     * Prints a warning message in yellow
     * 
     * @param message The message to print
     */
    public static void warning(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    /**
     * Prints a horizontal list of strings
     * Prompt: https://chatgpt.com/share/679fee0a-8bac-8004-95e7-613e286c9a8d
     * 
     * @param strings The strings to print
     */
    public static void printHorizontal(List<String> strings) {
        List<String[]> splitStrings = new ArrayList<>();
        int numRows = 0;

        final String GAP = "   ";

        // Split strings into lines and find max width per column
        List<Integer> maxWidths = new ArrayList<>();
        for (String s : strings) {
            String[] lines = s.split("\n");
            splitStrings.add(lines);
            numRows = Math.max(numRows, lines.length);
            int maxWidth = Arrays.stream(lines).mapToInt(String::length).max().orElse(0);
            maxWidths.add(maxWidth);
        }

        // Print rows preserving alignment
        for (int i = 0; i < numRows; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < splitStrings.size(); j++) {
                String[] lines = splitStrings.get(j);
                String part = (i < lines.length) ? lines[i] : "";
                row.append(String.format("%-" + maxWidths.get(j) + "s", part));
                if (j < splitStrings.size() - 1)
                    row.append(GAP);
            }
            System.out.println(row);
        }
    }

    /**
     * Clears the console screen
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Toggles debug mode
     */
    public static void toggleDebug() {
        debug = !debug;
    }

    /**
     * Checks if debug mode is enabled
     * 
     * @return true if debug mode is enabled, false otherwise
     */
    public static boolean isDebug() {
        return debug;
    }
}