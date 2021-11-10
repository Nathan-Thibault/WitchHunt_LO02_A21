package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.StrategyEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

public class CommandLineInterface implements IOInterface {

    private final PrintStream info;
    private final PrintStream error;
    private final InputStream inputStream;

    private boolean windowsConsole = true;

    public CommandLineInterface(PrintStream info, PrintStream error, InputStream inputStream) {
        this.info = Objects.requireNonNull(info);
        this.error = Objects.requireNonNull(error);
        this.inputStream = Objects.requireNonNull(inputStream);

        windowsConsole = System.console() != null && System.getProperty("os.name").contains("Windows");
    }

    @Override
    public void titleScreen() {
        resetScreen();
        info.println("\n"
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

        info.println("Press enter to continue.");
        try {
            inputStream.read();//blocks until input data is available, i.e. until enter is pressed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printInfo(String msg) {
        resetScreen();
        info.println(msg);
    }

    @Override
    public void printError(String msg) {
        resetScreen();
        error.println(msg);
    }

    @Override
    public int readIntBetween(int min, int max) {
        String message = "Please enter an integer between "
                .concat(Integer.toString(min))
                .concat(" and ")
                .concat(Integer.toString(max))
                .concat(" :");

        Scanner sc = new Scanner(inputStream);

        Integer result = null;
        while (result == null || result < min || result > max) {
            info.println(message);
            if (sc.hasNextInt()) {
                result = sc.nextInt();
            } else if (sc.hasNext()) {
                info.println(sc.next().concat(" is not an integer."));
            }
        }

        if (!inputStream.equals(System.in))//close scanner if input stream isn't System.in
            sc.close();

        return result;
    }

    @Override
    public Class<? extends Strategy> readStrategy(Strategy.StrategyType strategyType) {
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

        info.print(listOfOptions);

        int code = readIntBetween(minCode, maxCode);

        return StrategyEnum.getByCode(code).getStrategyClass();
    }

    private void resetScreen() {
        if (windowsConsole) {
            try {
                //run cls command
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (IOException | InterruptedException ignored) {
            }
        } else {
            /*
            \033[H moves the cursor to the top left corner of the screen or console.
            \033[2J clears the screen from the cursor to the end of the screen.
             */
            info.print("\033[H\033[2J");
            info.flush();
        }
    }
}
