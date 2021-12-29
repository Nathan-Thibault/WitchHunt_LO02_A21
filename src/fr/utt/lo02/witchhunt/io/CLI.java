package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.WitchHunt;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.card.IdentityCard;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public final class CLI implements IOInterface {

    private final boolean windowsConsole;
    private Thread thread = null;

    public CLI() {
        windowsConsole = System.console() != null && System.getProperty("os.name").contains("Windows");
    }

    @Override
    public void clear() {
        if (thread != null) thread.interrupt();

        if (WitchHunt.isTest())
            System.out.println("\n---\n\n");
        else
            resetScreen();
    }

    @Override
    public void titleScreen() {
        resetScreen();
        System.out.println("""

                 █     █░ ██▓▄▄▄█████▓ ▄████▄   ██░ ██     ██░ ██  █    ██  ███▄    █ ▄▄▄█████▓
                ▓█░ █ ░█░▓██▒▓  ██▒ ▓▒▒██▀ ▀█  ▓██░ ██▒   ▓██░ ██▒ ██  ▓██▒ ██ ▀█   █ ▓  ██▒ ▓▒
                ▒█░ █ ░█ ▒██▒▒ ▓██░ ▒░▒▓█    ▄ ▒██▀▀██░   ▒██▀▀██░▓██  ▒██░▓██  ▀█ ██▒▒ ▓██░ ▒░
                ░█░ █ ░█ ░██░░ ▓██▓ ░ ▒▓▓▄ ▄██▒░▓█ ░██    ░▓█ ░██ ▓▓█  ░██░▓██▒  █ ██▒░ ▓██▓ ░\s
                ░░██▒██▓ ░██░  ▒██▒ ░ ▒ ▓███▀ ░░▓█▒░██▓   ░▓█▒░██▓▒▒█████▓ ▒██░   ▓██░  ▒██▒ ░\s
                ░ ▓░▒ ▒  ░▓    ▒ ░░   ░ ░▒ ▒  ░ ▒ ░░▒░▒    ▒ ░░▒░▒░▒▓▒ ▒ ▒ ░ ▒░   ▒ ▒   ▒ ░░  \s
                  ▒ ░ ░   ▒ ░    ░      ░  ▒    ▒ ░▒░ ░    ▒ ░▒░ ░░░▒░ ░ ░ ░ ░░   ░ ▒░    ░   \s
                  ░   ░   ▒ ░  ░      ░         ░  ░░ ░    ░  ░░ ░ ░░░ ░ ░    ░   ░ ░   ░     \s
                    ░     ░           ░ ░       ░  ░  ░    ░  ░  ░   ░              ░         \s
                                      ░                                                       \s

                """);

        pause();
    }

    @Override
    public void pause() {
        if (thread != null) thread.interrupt();
        thread = new Thread(() -> {
            System.out.println("Press enter to continue.");
            try {
                //blocks until input data is available, i.e. until enter is pressed
                while (System.in.available() < 1) {
                    Thread.sleep(200);
                }
                System.in.read();
            } catch (InterruptedException ignored) {
                //sleep interrupted
            } catch (IOException e) {
                e.printStackTrace();
            }
            IOController.getInstance().stopWaiting();
        });
        thread.start();
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
    public int readIntBetween(int min, int max) {
        if (thread != null) thread.interrupt();
        thread = new Thread(() -> IOController.getInstance().read("int", intBetween(min, max)));
        thread.start();

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

        if (thread != null) thread.interrupt();
        thread = new Thread(() -> {
            int code = intBetween(0, list.size() - 1);
            IOController.getInstance().read("from_set", list.get(code));
        });
        thread.start();

        return null;
    }

    private int intBetween(int min, int max) {
        String message = "Please enter an integer between "
                .concat(Integer.toString(min))
                .concat(" and ")
                .concat(Integer.toString(max))
                .concat(" :");

        Scanner sc = new Scanner(System.in);

        Integer result = null;
        while (result == null) {
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

        return result;
    }

    private void resetScreen() {
        try {
            if (windowsConsole) {
                //run cls command
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                //run clear command
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ignored) {
            //jump 50 lines
            for (int i = 0; i < 50; i++) {
                System.out.print("\n");
            }
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
