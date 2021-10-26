package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.StrategyEnum;

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

    public static <T extends Strategy> T readStrategy(Class<T> sTypeClass) {
        Strategy.StrategyType strategyType = Strategy.StrategyType.getByClass(sTypeClass);

        StringBuilder listOfOptions = new StringBuilder();
        int minCode = 0;
        int maxCode = 0;

        listOfOptions.append("Select a strategy from the list bellow for the ");
        listOfOptions.append(strategyType.getName());
        listOfOptions.append(":\n");

        for (StrategyEnum strategy : StrategyEnum.values()) {
            if (strategy.getType().equals(strategyType)) {
                maxCode = strategy.getCode();
                if (minCode == 0)
                    minCode = strategy.getCode();

                listOfOptions.append(strategy.getCode());
                listOfOptions.append(" -> ");
                listOfOptions.append(strategy.getDescription());
                listOfOptions.append("\n");
            }
        }

        System.out.print(listOfOptions);

        int code = readIntBetween(minCode, maxCode);

        try {
            return sTypeClass.cast(StrategyEnum.getByCode(code).getStrategyClass().getConstructor().newInstance());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setWindowsConsole(boolean b) {
        windowsConsole = b;
    }
}
