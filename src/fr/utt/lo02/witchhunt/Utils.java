package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

        Scanner sc = new Scanner(System.in);

        Integer result = null;
        while (result == null || result < min || result > max) {
            System.out.println(message);
            if (sc.hasNextInt()) {
                result = sc.nextInt();
            } else if (sc.hasNext()) {
                System.out.println(sc.next().concat(" is not an integer."));
            }
        }

        return result;
    }

    public static <T extends Strategy> T readStrategy(Class<T> strategyType) {
        Scanner sc = new Scanner(System.in);

        String listOfOptions = switch (strategyType.getSimpleName()){
            case "IdentityStrategy" -> "(RandomIdentityStrategy, VillagerStrategy, WitchStrategy)";
            case "RespondStrategy" -> "(AlwaysReveal, NeverReveal, RevealIfVillager)";
            case "TurnStrategy" -> "(AlwaysAccuse)";
            default -> "";
        };

        while (true) {
            System.out.println("Select the ".concat(strategyType.getSimpleName()).concat(" from the list bellow:"));
            System.out.println(listOfOptions);

            if (sc.hasNextLine()) {
                String input = sc.nextLine();
                try {
                    return strategyType.cast(Class.forName(strategyType.getPackageName().concat(".").concat(input)).getConstructor().newInstance());
                } catch (ClassNotFoundException | NoClassDefFoundError | NoSuchMethodException e) {
                    System.out.println("Couldn't find ".concat(input).concat(" strategy. Did you correctly wrote your selection (with the correct case)?"));
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setWindowsConsole(boolean b) {
        windowsConsole = b;
    }
}
