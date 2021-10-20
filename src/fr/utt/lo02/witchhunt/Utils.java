package fr.utt.lo02.witchhunt;

import java.io.IOException;
import java.util.*;

public final class Utils {

    public static boolean windowsConsole = true;

    public static <T> T randomFromList(ArrayList<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static void resetScreen() {
        if (windowsConsole) {
            try {
                //run cls command
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException ignored) {
            }
        } else {
            /*
            \033[H moves the cursor at the top left corner of the screen or console.
            \033[2J clears the screen from the cursor to the end of the screen.
             */
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    public static int readIntBetween(int min, int max) {
        String message = "Please enter an integer between "
                .concat(Integer.toString(min))
                .concat(" and ")
                .concat(Integer.toString(max))
                .concat(" :");

        try (Scanner sc = new Scanner(System.in)) {
            int result;
            do {
                System.out.println(message);
                //TODO : fix NoSuchElementException because scanner is exhausted
                result = sc.nextInt();
            } while (result < min || result > max);

            return result;
        } catch (InputMismatchException e) {
            System.err.println("This is not an integer.");

            return readIntBetween(min, max);
        }
    }

    public static void setWindowsConsole(boolean b) {
        windowsConsole = b;
    }
}
