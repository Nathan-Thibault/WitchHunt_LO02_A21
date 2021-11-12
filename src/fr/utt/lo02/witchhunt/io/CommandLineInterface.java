package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.StrategyEnum;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class CommandLineInterface implements IOInterface {

    private final boolean windowsConsole;
    private boolean waiting;

    public CommandLineInterface() {
        windowsConsole = System.console() != null && System.getProperty("os.name").contains("Windows");
    }

    @Override
    public void clear() {
        waiting = false;
        resetScreen();
    }

    @Override
    public void titleScreen() {
        resetScreen();
        System.out.println("\n"
                .concat(" █     █░ ██▓▄▄▄█████▓ ▄████▄   ██░ ██     ██░ ██  █    ██  ███▄    █ ▄▄▄█████▓\n")
                .concat("▓█░ █ ░█░▓██▒▓  ██▒ ▓▒▒██▀ ▀█  ▓██░ ██▒   ▓██░ ██▒ ██  ▓██▒ ██ ▀█   █ ▓  ██▒ ▓▒\n")
                .concat("▒█░ █ ░█ ▒██▒▒ ▓██░ ▒░▒▓█    ▄ ▒██▀▀██░   ▒██▀▀██░▓██  ▒██░▓██  ▀█ ██▒▒ ▓██░ ▒░\n")
                .concat("░█░ █ ░█ ░██░░ ▓██▓ ░ ▒▓▓▄ ▄██▒░▓█ ░██    ░▓█ ░██ ▓▓█  ░██░▓██▒  ▐▌██▒░ ▓██▓ ░ \n")
                .concat("░░██▒██▓ ░██░  ▒██▒ ░ ▒ ▓███▀ ░░▓█▒░██▓   ░▓█▒░██▓▒▒█████▓ ▒██░   ▓██░  ▒██▒ ░ \n")
                .concat("░ ▓░▒ ▒  ░▓    ▒ ░░   ░ ░▒ ▒  ░ ▒ ░░▒░▒    ▒ ░░▒░▒░▒▓▒ ▒ ▒ ░ ▒░   ▒ ▒   ▒ ░░   \n")
                .concat("  ▒ ░ ░   ▒ ░    ░      ░  ▒    ▒ ░▒░ ░    ▒ ░▒░ ░░░▒░ ░ ░ ░ ░░   ░ ▒░    ░    \n")
                .concat("  ░   ░   ▒ ░  ░      ░         ░  ░░ ░    ░  ░░ ░ ░░░ ░ ░    ░   ░ ░   ░      \n")
                .concat("    ░     ░           ░ ░       ░  ░  ░    ░  ░  ░   ░              ░          \n")
                .concat("                      ░                                                        \n")
                .concat("\n"));

        System.out.println("Press enter to continue.");
        try {
            System.in.read();//blocks until input data is available, i.e. until enter is pressed
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOController.getInstance().stopWaiting();
    }

    @Override
    public void printInfo(String msg) {
        resetScreen();
        System.out.println(msg);
    }

    @Override
    public void printError(String msg) {
        resetScreen();
        System.err.println(msg);
    }

    @Override
    public void readIntBetween(int min, int max) {
        IOController.getInstance().setReadInt(intBetween(min, max));
    }

    @Override
    public void readStrategy(Strategy.StrategyType strategyType) {
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

        int code = intBetween(minCode, maxCode);

        IOController.getInstance().setReadStrategy(StrategyEnum.getByCode(code).getStrategyClass());
    }

    private int intBetween(int min, int max) throws NullPointerException {
        String message = "Please enter an integer between "
                .concat(Integer.toString(min))
                .concat(" and ")
                .concat(Integer.toString(max))
                .concat(" :");

        Scanner sc = new Scanner(System.in);

        Integer result = null;
        waiting = true;
        while (waiting && result == null) {
            System.out.println(message);
            if (sc.hasNextInt()) {
                result = sc.nextInt();
                if (result < min || result > max) {
                    System.out.println(result.toString().concat(" is out of range."));
                    result = null;
                }
            } else if (sc.hasNext()) {
                System.out.println(sc.next().concat(" is not an integer."));
            }
        }

        return Objects.requireNonNull(result);
    }

    private void resetScreen() {
        if (windowsConsole) {
            try {
                //run cls command
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException ignored) {
                for (int i = 0; i < 50; i++) {
                    System.out.print("\n");
                }
            }
        } else {
            /*
            \033[H moves the cursor to the top left corner of the screen or console.
            \033[2J clears the screen from the cursor to the end of the screen.
             */
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}
