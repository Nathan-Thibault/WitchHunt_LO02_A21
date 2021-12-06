package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.WitchHunt;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public final class CommandLineInterface implements IOInterface {

    private final boolean windowsConsole;
    private boolean waiting;

    public CommandLineInterface() {
        windowsConsole = System.console() != null && System.getProperty("os.name").contains("Windows");
    }

    @Override
    public void clear() {
        waiting = false;

        if (WitchHunt.isTest())
            System.out.println("\n---\n\n");
        else
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

        pause();
    }

    @Override
    public void pause() {
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

        sb.append("List of the players:\n");
        for (String pName : pManager.getAllPlayers()) {
            Player p = pManager.getByName(pName);
            IdentityCard ic = p.getIdentityCard();

            sb.append("  - ");
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
            formatSet(sb, ", Revealed cards: ", p.getRevealedCards());
            sb.append("]\n");
        }

        //list of discarded cards if any
        formatSet(sb, "\nList of discarded cards:\n    ", CardManager.getInstance().getDiscardedCards());
        sb.append("\n");

        System.out.println(sb);
    }

    @Override
    public void printInfo(String msg) {
        System.out.println(msg);
    }

    @Override
    public void printError(String msg) {
        System.err.println(msg);
    }

    @Override
    public int readIntBetween(int min, int max) {
        IOController.getInstance().read("int", intBetween(min, max));

        return 0;
    }

    @Override
    public <T> T readFromSet(Set<T> set) {
        ArrayList<T> list = new ArrayList<>(set);
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

    private void formatSet(StringBuilder sb, String msg, Set<String> list) {
        if (!list.isEmpty()) {
            sb.append(msg);
            sb.append("{");
            for (String str : list) {
                sb.append(str);
                sb.append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());//remove last ", "
            sb.append("}");
        }
    }
}
