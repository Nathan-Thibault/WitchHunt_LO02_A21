package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.PlayerManager;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WitchHunt {
    public static void main(String[] args) {
        IOController.getInstance().titleScreen();
        createPlayers();
        CardManager.getInstance();//create cards
        RoundManager.getInstance().startNewRound();
    }

    private static void createPlayers() {
        IOController io = IOController.getInstance();

        io.printInfo("Choose the number of physical players you want.");

        int p = io.readIntBetween(0, 6);

        Scanner sc = new Scanner(System.in);
        PlayerManager pManager = PlayerManager.getInstance();

        for (int i = 1; i <= p; i++) {
            io.printInfo("Enter the name of the player ".concat(Integer.toString(i)).concat(" :"));
            try {
                pManager.addPhysicalPlayer(sc.nextLine());
                io.printInfo("Player successfully added.");
            } catch (IllegalStateException | NoSuchElementException e) {
                io.printError("An exception occurred. Please try again.");
                i--;
            }
        }

        io.printInfo("Now, choose the number of artificial players you want.");

        int a = io.readIntBetween(Math.max(3 - p, 0), 6 - p);

        for (int i = 0; i < a; i++) {
            io.printInfo("Choose strategies for artificial player ".concat(Integer.toString(i)));

            HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies = new HashMap<>();
            for (Strategy.StrategyType sType : Strategy.StrategyType.values()) {
                strategies.put(sType, io.readStrategy(sType));
            }

            pManager.createArtificialPlayer(strategies);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Players in game: ");
        for (String player : pManager.getAllPlayers()) {
            sb.append(player);
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));//remove useless last comma
        io.printInfo(sb.toString());
        io.pause();
    }
}
