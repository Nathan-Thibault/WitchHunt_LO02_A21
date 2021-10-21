package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.player.PlayerManager;
import fr.utt.lo02.witchhunt.player.strategy.identity.IdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.respond.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.turn.TurnStrategy;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WitchHunt {

    public static void main(String[] args) {
        Utils.setWindowsConsole(System.console() != null && System.getProperty("os.name").contains("Windows"));

        Utils.resetScreen();
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(" █     █░ ██▓▄▄▄█████▓ ▄████▄   ██░ ██     ██░ ██  █    ██  ███▄    █ ▄▄▄█████▓\n");
        sb.append("▓█░ █ ░█░▓██▒▓  ██▒ ▓▒▒██▀ ▀█  ▓██░ ██▒   ▓██░ ██▒ ██  ▓██▒ ██ ▀█   █ ▓  ██▒ ▓▒\n");
        sb.append("▒█░ █ ░█ ▒██▒▒ ▓██░ ▒░▒▓█    ▄ ▒██▀▀██░   ▒██▀▀██░▓██  ▒██░▓██  ▀█ ██▒▒ ▓██░ ▒░\n");
        sb.append("░█░ █ ░█ ░██░░ ▓██▓ ░ ▒▓▓▄ ▄██▒░▓█ ░██    ░▓█ ░██ ▓▓█  ░██░▓██▒  ▐▌██▒░ ▓██▓ ░ \n");
        sb.append("░░██▒██▓ ░██░  ▒██▒ ░ ▒ ▓███▀ ░░▓█▒░██▓   ░▓█▒░██▓▒▒█████▓ ▒██░   ▓██░  ▒██▒ ░ \n");
        sb.append("░ ▓░▒ ▒  ░▓    ▒ ░░   ░ ░▒ ▒  ░ ▒ ░░▒░▒    ▒ ░░▒░▒░▒▓▒ ▒ ▒ ░ ▒░   ▒ ▒   ▒ ░░   \n");
        sb.append("  ▒ ░ ░   ▒ ░    ░      ░  ▒    ▒ ░▒░ ░    ▒ ░▒░ ░░░▒░ ░ ░ ░ ░░   ░ ▒░    ░    \n");
        sb.append("  ░   ░   ▒ ░  ░      ░         ░  ░░ ░    ░  ░░ ░ ░░░ ░ ░    ░   ░ ░   ░      \n");
        sb.append("    ░     ░           ░ ░       ░  ░  ░    ░  ░  ░   ░              ░          \n");
        sb.append("                      ░                                                        \n");
        sb.append("\n");
        System.out.println(sb);

        System.out.println("Press enter to continue.");
        try {
            System.in.read();//blocks until input data is available, i.e. until enter is pressed
        } catch (IOException e) {
            e.printStackTrace();
        }

        createPlayers();
    }

    private static void createPlayers() {
        Utils.resetScreen();
        System.out.println("Choose the number of physical players you want.");

        int p = Utils.readIntBetween(0, 6);

        Utils.resetScreen();
        System.out.println("Now, choose the number of artificial players you want.");

        int a = Utils.readIntBetween(Math.max(3 - p, 0), 6 - p);

        Utils.resetScreen();
        Scanner sc = new Scanner(System.in);
        PlayerManager pManager = PlayerManager.getInstance();

        for (int i = 1; i <= p; i++) {
            System.out.println("Enter the name of the player ".concat(Integer.toString(i)).concat(" :"));
            try {
                pManager.addPhysicalPlayer(sc.nextLine());
                System.out.println("Player successfully added.");
            } catch (IllegalStateException | NoSuchElementException e) {
                System.err.println("An exception occurred. Please try again.");
                i--;
            }
        }

        for (int i = 0; i < a; i++) {
            Utils.resetScreen();
            System.out.println("Choose strategies for artificial player ".concat(Integer.toString(i)));

            IdentityStrategy identityS = Utils.readStrategy(IdentityStrategy.class);
            RespondStrategy respondS = Utils.readStrategy(RespondStrategy.class);
            TurnStrategy turnS = Utils.readStrategy(TurnStrategy.class);

            pManager.createArtificialPlayer(turnS, respondS, identityS);
        }

        Utils.resetScreen();
        StringBuffer sb = new StringBuffer();
        sb.append("Players in game: ");
        for(String player : pManager.getAllPlayers()){
            sb.append(player);
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));//remove useless last comma
        System.out.println(sb);

        //only for test
        System.out.println("Press enter to continue.");
        try {
            System.in.read();//blocks until input data is available, i.e. until enter is pressed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
