package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.StrategyEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public final class CommandLineInterface implements IOInterface {

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
    public void displayGameInfos() {
        PlayerManager pManager = PlayerManager.getInstance();

        StringBuilder sb = new StringBuilder();

        sb.append("\nList of the players in game :\n");
        for (String pName : pManager.getInGamePlayers()) {
            Player p = pManager.getByName(pName);
            IdentityCard ic = p.getIdentityCard();

            //player name
            sb.append(pName);
            //identity
            sb.append(" [Identity: ");
            sb.append(ic.isRevealed() ? ic.getIdentity() : "Unrevealed");
            //score
            sb.append(", Score: ");
            sb.append(p.getScore());
            //number of cards in hand
            sb.append(", Cards in hand: ");
            sb.append(p.getHand().size());
            //revealed cards if any
            formatList(sb, ", Revealed cards: ", p.getRevealedCards());
            sb.append("]\n");
        }

        //list of discarded cards if any
        formatList(sb, "List of discarded cards:\n", CardManager.getInstance().getDiscardedCards());
        sb.append("\n");

        System.out.println(sb);
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
    public int readIntBetween(int min, int max) {
        IOController.getInstance().read("int", intBetween(min, max));

        return 0;
    }

    @Override
    public Class<? extends Strategy> readStrategy(Strategy.StrategyType strategyType) {
        //TODO: try to refactor duplicated code with listOfOptions building
        int minCode = 0;
        int maxCode = 0;
        StringBuilder listOfOptions = new StringBuilder();

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
        IOController.getInstance().read("strategy", StrategyEnum.getByCode(code).getStrategyClass());

        return null;
    }

    @Override
    public <T> T readFromList(List<T> list) {
        StringBuilder listOfOptions = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            listOfOptions.append(i);
            listOfOptions.append(" -> ");
            listOfOptions.append(list.get(i).toString());
            listOfOptions.append("\n");
        }
        System.out.print(listOfOptions);

        int code = intBetween(0, list.size() - 1);
        IOController.getInstance().read("from_list", list.get(code));

        return null;
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

    private void formatList(StringBuilder sb, String msg, ArrayList<String> list) {
        if (!list.isEmpty()) {
            sb.append(msg);
            sb.append("{");
            for (String str : list) {
                sb.append(str);
                sb.append(", ");
            }
            sb.delete(sb.length() - 3, sb.length() - 1);//remove last ", "
            sb.append("}");
        }
    }
}
